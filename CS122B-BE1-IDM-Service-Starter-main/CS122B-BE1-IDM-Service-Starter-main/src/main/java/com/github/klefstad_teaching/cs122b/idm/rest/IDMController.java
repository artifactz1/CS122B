package com.github.klefstad_teaching.cs122b.idm.rest;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Pattern;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.IDMResults;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.idm.component.IDMAuthenticationManager;
import com.github.klefstad_teaching.cs122b.idm.component.IDMJwtManager;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.TokenStatus;
import com.github.klefstad_teaching.cs122b.idm.reponse.LoginResponse;
import com.github.klefstad_teaching.cs122b.idm.reponse.RefreshResponse;
import com.github.klefstad_teaching.cs122b.idm.reponse.RegisterResponse;
import com.github.klefstad_teaching.cs122b.idm.request.LoginRequest;
import com.github.klefstad_teaching.cs122b.idm.request.RefreshRequest;
import com.github.klefstad_teaching.cs122b.idm.request.RegisterRequest;
import com.github.klefstad_teaching.cs122b.idm.util.Validate;
import com.nimbusds.jose.JOSEException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IDMController {
    private final IDMAuthenticationManager authManager;
    private final IDMJwtManager jwtManager;
    private final Validate validate;

    @Autowired
    public IDMController(IDMAuthenticationManager authManager,
            IDMJwtManager jwtManager,
            Validate validate) {
        this.authManager = authManager;
        this.jwtManager = jwtManager;
        this.validate = validate;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest vars) {
        if (validate.isEmailValidLength(vars.getEmail()) == false) {
            throw new ResultError(IDMResults.EMAIL_ADDRESS_HAS_INVALID_LENGTH);
        }
        if (validate.isEmailValidFormat(vars.getEmail()) == false) {
            throw new ResultError(IDMResults.EMAIL_ADDRESS_HAS_INVALID_FORMAT);
        }
        if (validate.isPasswordValidChar(vars.getPassword()) == false) {
            throw new ResultError(IDMResults.PASSWORD_DOES_NOT_MEET_CHARACTER_REQUIREMENT);
        }
        if (validate.isPasswordValidLength(vars.getPassword()) == false) {
            throw new ResultError(IDMResults.PASSWORD_DOES_NOT_MEET_LENGTH_REQUIREMENTS);
        }
        // DONT NEED TO CHECK User with this email already exists because Database will
        // do that since we set emails to unique

        authManager.createAndInsertUser(vars.getEmail(), vars.getPassword());

        RegisterResponse good = new RegisterResponse()
                .setResult(IDMResults.USER_REGISTERED_SUCCESSFULLY);
        return ResponseEntity.status(HttpStatus.OK).body(good);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest vars) throws JOSEException, ParseException {

        if (validate.isEmailValidLength(vars.getEmail()) == false) {
            throw new ResultError(IDMResults.EMAIL_ADDRESS_HAS_INVALID_LENGTH);
        }
        if (validate.isEmailValidFormat(vars.getEmail()) == false) {
            throw new ResultError(IDMResults.EMAIL_ADDRESS_HAS_INVALID_FORMAT);
        }
        if (validate.isPasswordValidChar(vars.getPassword()) == false) {
            throw new ResultError(IDMResults.PASSWORD_DOES_NOT_MEET_CHARACTER_REQUIREMENT);
        }
        if (validate.isPasswordValidLength(vars.getPassword()) == false) {
            throw new ResultError(IDMResults.PASSWORD_DOES_NOT_MEET_LENGTH_REQUIREMENTS);
        }

        User u = authManager.selectAndAuthenticateUser(vars.getEmail(), vars.getPassword());

        String accessToken = jwtManager.buildAccessToken(u);
        RefreshToken refreshToken = jwtManager.buildRefreshToken(u);

        // Need to save refreshtoken to db
        authManager.insertRefreshToken(refreshToken);

        LoginResponse good = new LoginResponse()
                .setResult(IDMResults.USER_LOGGED_IN_SUCCESSFULLY)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(good);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            @RequestBody RefreshRequest vars) throws JOSEException, ParseException {

        if (vars.getRefreshToken().length() != 36) {
            throw new ResultError(IDMResults.REFRESH_TOKEN_HAS_INVALID_LENGTH);
        }
        if (validate.isValidUUID(vars.getRefreshToken()) == false) {
            throw new ResultError(IDMResults.REFRESH_TOKEN_HAS_INVALID_FORMAT);
        }

        RefreshToken refreshToken = authManager.getRefreshTokenFromDB(vars.getRefreshToken());
        User user = authManager.getUserFromRefreshToken(refreshToken);

        if (jwtManager.hasExpired(refreshToken) == true) {
            throw new ResultError(IDMResults.REFRESH_TOKEN_IS_EXPIRED);
        }
        if (refreshToken.getTokenStatus().id() == 3) {
            throw new ResultError(IDMResults.REFRESH_TOKEN_IS_REVOKED);
        }
        if (Instant.now().isAfter(refreshToken.getExpireTime())
                || Instant.now().isAfter(refreshToken.getMaxLifeTime())) {

            // NEED TO update refreshTokenStatus to Expired in DB
            authManager.expireRefreshToken(refreshToken);
            throw new ResultError(IDMResults.REFRESH_TOKEN_IS_EXPIRED);

        } else {

            jwtManager.updateRefreshTokenExpireTime(refreshToken);
        }
        if (refreshToken.getExpireTime().isAfter(refreshToken.getMaxLifeTime())) {

            // Need to update refresh token in DB to revoke
            authManager.revokeRefreshToken(refreshToken);

            // return new refreshToken and new accessToken
            RefreshToken rf = jwtManager.buildRefreshToken(user);
            authManager.insertRefreshToken(rf);

            String accessToken = jwtManager.buildAccessToken(user);

            RefreshResponse good = new RefreshResponse()
                    .setResult(IDMResults.RENEWED_FROM_REFRESH_TOKEN)
                    .setAccessToken(accessToken)
                    .setRefreshToken(rf.getToken());
            return ResponseEntity.status(HttpStatus.OK).body(good);

        }

        // Need to update refresh token expireTime in DB
        // - updated expire time then updating it in data base
        authManager.updateRefreshTokenExpireTime(refreshToken);

        // return same refreshToken and new accessToken
        String accessToken = jwtManager.buildAccessToken(user);

        RefreshResponse good = new RefreshResponse()
                .setResult(IDMResults.RENEWED_FROM_REFRESH_TOKEN)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(good);
    }

};

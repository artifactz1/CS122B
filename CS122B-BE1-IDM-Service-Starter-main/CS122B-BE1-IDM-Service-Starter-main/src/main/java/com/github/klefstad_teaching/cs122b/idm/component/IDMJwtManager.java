package com.github.klefstad_teaching.cs122b.idm.component;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.IDMResults;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.idm.config.IDMServiceConfig;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.TokenStatus;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.ParseException;
import java.time.Instant;
import java.util.UUID;

@Component
public class IDMJwtManager {
    private final JWTManager jwtManager;

    @Autowired
    public IDMJwtManager(IDMServiceConfig serviceConfig) {
        this.jwtManager = new JWTManager.Builder()
                .keyFileName(serviceConfig.keyFileName())
                .accessTokenExpire(serviceConfig.accessTokenExpire())
                .maxRefreshTokenLifeTime(serviceConfig.maxRefreshTokenLifeTime())
                .refreshTokenExpire(serviceConfig.refreshTokenExpire())
                .build();
    }

    private SignedJWT buildAndSignJWT(JWTClaimsSet claimsSet)
            throws JOSEException {

        JWSHeader header = new JWSHeader.Builder(JWTManager.JWS_ALGORITHM)
                .keyID(jwtManager.getEcKey().getKeyID())
                .type(JWTManager.JWS_TYPE)
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        return signedJWT;
    }

    private void verifyJWT(SignedJWT jwt)
            throws JOSEException, BadJOSEException, ParseException {

        try {
            SignedJWT rebuiltSignedJwt = SignedJWT.parse(jwt.serialize());

            rebuiltSignedJwt.verify(jwtManager.getVerifier());
            jwtManager.getJwtProcessor().process(rebuiltSignedJwt, null);

            // Do logic to check if expired manually
            rebuiltSignedJwt.getJWTClaimsSet().getExpirationTime();

        } catch (IllegalStateException | JOSEException | BadJOSEException e) {
            // LOG.error("This is not a real token, DO NOT TRUST");
            e.printStackTrace();

            // If the verify function throws an error that we know the
            // token can not be trusted and the request should not be continued

            throw new ResultError(IDMResults.ACCESS_TOKEN_IS_VALID);

        }

    }

    public String buildAccessToken(User user) throws JOSEException, ParseException, BadJOSEException {

        JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .expirationTime(Date.from(Instant.now().plus(this.jwtManager.getAccessTokenExpire())))
                .issueTime(Date.from(Instant.now()))
                .claim(JWTManager.CLAIM_ROLES, user.getRoles())
                .claim(JWTManager.CLAIM_ID, user.getId())
                .build();

        SignedJWT signedJWT = buildAndSignJWT(claimSet);
        signedJWT.sign(jwtManager.getSigner());
        verifyJWT(signedJWT);

        String serialized = signedJWT.serialize();
        return serialized;
    }

    public void verifyAccessToken(String jws) throws ParseException {

        SignedJWT rebuiltSignedJwt = SignedJWT.parse(jws);

        try {

            rebuiltSignedJwt.verify(jwtManager.getVerifier());
            jwtManager.getJwtProcessor().process(rebuiltSignedJwt, null);

            // Do logic to check if expired manually
            rebuiltSignedJwt.getJWTClaimsSet().getExpirationTime();

        } catch (IllegalStateException | JOSEException | BadJOSEException e) {

            throw new ResultError(IDMResults.ACCESS_TOKEN_IS_INVALID);
        }
        if (Instant.now().isAfter(rebuiltSignedJwt.getJWTClaimsSet().getExpirationTime().toInstant()) == true) {
            throw new ResultError(IDMResults.ACCESS_TOKEN_IS_EXPIRED);
        }
    }

    public RefreshToken buildRefreshToken(User user) {

        RefreshToken rt = new RefreshToken();
        rt.setToken(UUID.randomUUID().toString());
        rt.setUserId(user.getId());
        rt.setTokenStatus(TokenStatus.ACTIVE);

        updateRefreshTokenExpireTime(rt);

        // rt.setExpireTime(Date.from(Instant.now().plus(this.jwtManager.getRefreshTokenExpire())));
        return rt;
    }

    public boolean hasExpired(RefreshToken refreshToken) {

        if (refreshToken.getTokenStatus().id() == 2) {
            return true;
        }
        return false;
    }

    public boolean needsRefresh(RefreshToken refreshToken) {

        if (Instant.now().isAfter(refreshToken.getExpireTime())) {
            return true;
        }

        if (Instant.now().isAfter(refreshToken.getMaxLifeTime())) {

            return true;
        }

        return false;
    }

    public void updateRefreshTokenExpireTime(RefreshToken refreshToken) {
        refreshToken.setExpireTime(Instant.now().plus(this.jwtManager.getRefreshTokenExpire()));
        refreshToken.setMaxLifeTime(Instant.now().plus(this.jwtManager.getMaxRefreshTokenLifeTime()));
    }
}

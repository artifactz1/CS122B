package com.github.klefstad_teaching.cs122b.idm.component;

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
            throws JOSEException, BadJOSEException {

    }

    public String buildAccessToken(User user) throws JOSEException, ParseException {

        JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .expirationTime(Date.from(Instant.now().plus(this.jwtManager.getAccessTokenExpire())))
                .issueTime(Date.from(Instant.now()))
                .claim(JWTManager.CLAIM_ROLES, user.getRoles())
                .claim(JWTManager.CLAIM_ID, user.getId())
                .build();

        SignedJWT signedJWT = buildAndSignJWT(claimSet);
        signedJWT.sign(jwtManager.getSigner());

        String serialized = signedJWT.serialize();
        return serialized;
    }

    public void verifyAccessToken(String jws) throws ParseException {

        try {
            SignedJWT rebuiltSignedJwt = SignedJWT.parse(jws);

            rebuiltSignedJwt.verify(jwtManager.getVerifier());
            jwtManager.getJwtProcessor().process(rebuiltSignedJwt, null);

            // Do logic to check if expired manually
            rebuiltSignedJwt.getJWTClaimsSet().getExpirationTime();

        } catch (IllegalStateException | JOSEException | BadJOSEException e) {
            // LOG.error("This is not a real token, DO NOT TRUST");
            e.printStackTrace();
            // If the verify function throws an error that we know the
            // token can not be trusted and the request should not be continued
        }
    }

    public RefreshToken buildRefreshToken(User user) {

        RefreshToken rt = new RefreshToken();
        rt.setToken(UUID.randomUUID().toString());
        rt.setUserId(user.getId());
        rt.setTokenStatus(TokenStatus.ACTIVE);
        return rt;
    }

    public boolean hasExpired(RefreshToken refreshToken) {

        if (refreshToken.getTokenStatus().id() == 2) {
            return true;
        }
        return false;
    }

    public boolean needsRefresh(RefreshToken refreshToken) {
        return false;
    }

    public void updateRefreshTokenExpireTime(RefreshToken refreshToken) {

    }
}

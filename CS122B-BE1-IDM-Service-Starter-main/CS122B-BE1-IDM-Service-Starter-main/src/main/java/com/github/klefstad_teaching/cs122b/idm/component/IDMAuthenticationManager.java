package com.github.klefstad_teaching.cs122b.idm.component;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.IDMResults;
import com.github.klefstad_teaching.cs122b.idm.repo.IDMRepo;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.Role;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.UserStatus;
import com.github.klefstad_teaching.cs122b.idm.reponse.LoginResponse;
import com.github.klefstad_teaching.cs122b.idm.request.LoginRequest;
import com.github.klefstad_teaching.cs122b.idm.request.RegisterRequest;
import com.github.klefstad_teaching.cs122b.idm.util.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Component
public class IDMAuthenticationManager {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String HASH_FUNCTION = "PBKDF2WithHmacSHA512";

    private static final int ITERATIONS = 10000;
    private static final int KEY_BIT_LENGTH = 512;

    private static final int SALT_BYTE_LENGTH = 4;

    public final IDMRepo repo;

    @Autowired
    public IDMAuthenticationManager(IDMRepo repo) {
        this.repo = repo;
    }

    private static byte[] hashPassword(final char[] password, String salt) {
        return hashPassword(password, Base64.getDecoder().decode(salt));
    }

    private static byte[] hashPassword(final char[] password, final byte[] salt) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(HASH_FUNCTION);

            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_BIT_LENGTH);

            SecretKey key = skf.generateSecret(spec);

            return key.getEncoded();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] genSalt() {
        byte[] salt = new byte[SALT_BYTE_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }

    public User selectAndAuthenticateUser(String email, char[] password) {

        LoginRequest send = new LoginRequest().setEmail(email).setPassword(password);
        ResponseEntity<LoginResponse> response = repo.login(send);
        LoginResponse u = response.getBody();

        List<User> users = u.getUsers();

        if (users.isEmpty() == true) {
            throw new ResultError(IDMResults.USER_NOT_FOUND);
        }

        boolean match = false;
        int id = 0;
        for (int i = 0; i < users.size(); i++) {

            String userHashedPassword = users.get(i).getHashedPassword();
            String userSalt = users.get(i).getSalt();

            // Decoded to byte
            byte[] checkPassword = hashPassword(userHashedPassword.toCharArray(), userSalt);

            if (password.toString().equals(checkPassword.toString()) == true) {
                match = true;
                id = i;
                break;
            }
        }

        if (match == false) {
            throw new ResultError(IDMResults.INVALID_CREDENTIALS);
        }
        if (users.get(id).getUserStatus().value().equals("Locked")) {

            throw new ResultError(IDMResults.USER_IS_LOCKED);
        }
        if (users.get(id).getUserStatus().value().equals("Banned")) {

            throw new ResultError(IDMResults.USER_IS_BANNED);
        }

        return users.get(id);
    }

    public void createAndInsertUser(String email, char[] password) {

        String base64EncodedHashedSalt = Base64.getEncoder().encodeToString(genSalt());
        byte[] encodedPassword = hashPassword(password, base64EncodedHashedSalt);
        String base64EncodedHashedPassword = Base64.getEncoder().encodeToString(encodedPassword);

        // https://www.javatpoint.com/java-collections-emptylist-method
        List<Role> empty = Collections.<Role>emptyList();

        User user = new User()
                .setEmail(email)
                .setHashedPassword(base64EncodedHashedPassword)
                .setUserStatus(UserStatus.ACTIVE)
                .setSalt(base64EncodedHashedSalt)
                .setRoles(empty);

        RegisterRequest send = new RegisterRequest()
                .setUser(user);

        repo.register(send);
    }

    public void insertRefreshToken(RefreshToken refreshToken) {

        repo.insertRefresh(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token) {
        return null;
    }

    public void updateRefreshTokenExpireTime(RefreshToken token) {
    }

    public void expireRefreshToken(RefreshToken token) {
    }

    public void revokeRefreshToken(RefreshToken token) {
    }

    public User getUserFromRefreshToken(RefreshToken refreshToken) {
        return null;
    }
}

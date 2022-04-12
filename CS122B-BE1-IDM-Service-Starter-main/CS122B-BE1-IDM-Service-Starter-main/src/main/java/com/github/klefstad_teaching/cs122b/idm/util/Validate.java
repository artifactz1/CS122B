package com.github.klefstad_teaching.cs122b.idm.util;

import java.util.List;
import java.util.regex.Pattern;

import com.github.klefstad_teaching.cs122b.idm.component.IDMAuthenticationManager;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;

import org.springframework.stereotype.Component;

@Component
public final class Validate {

    public boolean isEmailValidLength(String email) {

        if (email.length() > 32 || email.length() < 6) {
            return false;
        }
        return true;
    }

    // https://www.geeksforgeeks.org/check-email-address-valid-not-java/
    // got help to have check if valid email adress using regex
    public boolean isEmailValidFormat(String email) {

        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        if (email == null) {
            return false;
        }

        Pattern p = Pattern.compile(regex);
        return p.matcher(email).matches();

    }

    public boolean isPasswordValidLength(char[] pass) {
        if (pass.length < 10 || pass.length > 20) {
            return false;
        }
        return true;
    }

    public boolean isPasswordValidChar(char[] pass) {

        // https://stackoverflow.com/questions/35333196/check-java-string-containing-one-alpha-numeric-characters-and-one-of-these-spe
        if (Validate.isAlphaNumeric(pass) == true) {
            boolean hasUpper = false;
            boolean hasLower = false;
            boolean hasNumeric = false;

            for (char ch : pass) {
                if (Character.isUpperCase(ch)) {
                    hasUpper = true;
                }
                if (Character.isLowerCase(ch)) {
                    hasLower = true;
                }
                if (Character.isDigit(ch)) {
                    hasNumeric = true;
                }
            }
            return (hasUpper && hasLower && hasNumeric);
        }
        return false;
    }

    public static boolean isAlphaNumeric(char[] pass) {

        String p = String.valueOf(pass);
        return p != null && p.matches("^[a-zA-Z0-9]*$");
    }
}

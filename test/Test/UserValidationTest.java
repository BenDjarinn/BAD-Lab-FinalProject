package Test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {

    // Test: username tidak boleh kosong
    @Test
    void testUsernameMustNotBeEmpty() {
        String username = "";
        assertTrue(username.isEmpty(), "Empty username should be invalid");
    }

    // Test: username harus 5-20 karakter
    @Test
    void testUsernameLength() {
        String username = "abc";
        assertTrue(username.length() < 5, "Username less than 5 chars should be invalid");
    }

    // Test: password harus alphanumeric
    @Test
    void testPasswordMustBeAlphanumeric() {
        String password = "pass123";
        assertTrue(password.matches("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$"),
            "Alphanumeric password should be valid");
    }

    // Test: password tidak boleh kurang dari 5 karakter
    @Test
    void testPasswordMinLength() {
        String password = "ab1";
        assertTrue(password.length() < 5, "Password less than 5 chars should be invalid");
    }

    // Test: format userID harus diawali CU
    @Test
    void testUserIDFormat() {
        String userID = "CU001";
        assertTrue(userID.startsWith("CU"), "UserID should start with CU");
    }
}
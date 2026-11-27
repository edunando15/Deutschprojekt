package edunando15.github.io.Deutschprojekt.model.utilities;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHasherTest {

    private final PasswordHasher passwordHasher = new PasswordHasher(new BCryptPasswordEncoder());

    @Test
    void hashesPasswordsAndMatchesOnlyTheCorrectPassword() {
        String firstHash = passwordHasher.hash("correct horse battery staple");
        String secondHash = passwordHasher.hash("correct horse battery staple");

        assertNotEquals("correct horse battery staple", firstHash);
        assertNotEquals(firstHash, secondHash);
        assertTrue(passwordHasher.matches("correct horse battery staple", firstHash));
        assertFalse(passwordHasher.matches("wrong password", firstHash));
    }
}

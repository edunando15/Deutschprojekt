package edunando15.github.io.Deutschprojekt.model.utilities;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    public PasswordHasher(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String hash(String rawPassword) {
        return passwordEncoder.encode(Objects.requireNonNull(rawPassword, "rawPassword must not be null"));
    }

    public boolean matches(String rawPassword, String passwordHash) {
        return passwordEncoder.matches(
                Objects.requireNonNull(rawPassword, "rawPassword must not be null"),
                Objects.requireNonNull(passwordHash, "passwordHash must not be null"));
    }
}

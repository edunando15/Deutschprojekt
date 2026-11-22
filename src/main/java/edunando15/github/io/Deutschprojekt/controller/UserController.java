package edunando15.github.io.Deutschprojekt.controller;

import edunando15.github.io.Deutschprojekt.model.dto.UserDTO;
import edunando15.github.io.Deutschprojekt.model.entities.User;
import edunando15.github.io.Deutschprojekt.model.services.JwtTokenService;
import edunando15.github.io.Deutschprojekt.model.services.UserService;
import edunando15.github.io.Deutschprojekt.model.utilities.PasswordHasher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("api/v0/users")
public class UserController {

    private final UserService userService;

    private final PasswordHasher passwordHasher;
    private final JwtTokenService jwtTokenService;

    public UserController(UserService userService, PasswordHasher passwordHasher,
                          JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.passwordHasher = passwordHasher;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = this.userService.findByUsername(userDTO.username());
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        try {
            String hashedPassword = this.passwordHasher.hash(userDTO.password());
            var user = userDTO.toEntity();
            user.setPassword(hashedPassword);
            this.userService.add(user);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("User added successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = this.userService.findByUsername(userDTO.username());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = optionalUser.get();
        boolean passwordMatches = this.passwordHasher.matches(userDTO.password(), user.getPassword());
        if (!passwordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Instant expiresAt = Instant.now().plus(JwtTokenService.TOKEN_LIFETIME);
        String token = this.jwtTokenService.createToken(user.getUsername(), expiresAt);
        return ResponseEntity.ok(new LoginResponse(token, "Bearer", expiresAt));
    }

    public record LoginResponse(String accessToken, String tokenType, Instant expiresAt) {}
}

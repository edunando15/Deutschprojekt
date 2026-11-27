package edunando15.github.io.Deutschprojekt.model.services;

import edunando15.github.io.Deutschprojekt.model.entities.User;
import edunando15.github.io.Deutschprojekt.model.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends GenericService<User> {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
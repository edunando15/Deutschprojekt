package edunando15.github.io.Deutschprojekt.model.dto;

import edunando15.github.io.Deutschprojekt.model.entities.User;

public record UserDTO(String username, String email, String password) implements GenericDTO<User> {
    @Override
    public User toEntity() {
        return new User(this.username, this.email, this.password);
    }
}

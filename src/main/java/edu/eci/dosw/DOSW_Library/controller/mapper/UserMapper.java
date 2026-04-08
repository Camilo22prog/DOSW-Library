package edu.eci.dosw.DOSW_Library.controller.mapper;

import edu.eci.dosw.DOSW_Library.controller.dto.UserDTO;
import edu.eci.dosw.DOSW_Library.core.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .role(user.isLibrarian() ? "LIBRARIAN" : "USER")
                .build();
    }

    public User toEntity(UserDTO dto) {
        boolean isLibrarian = dto.getLibrarian() != null
                ? dto.getLibrarian()
                : "LIBRARIAN".equalsIgnoreCase(dto.getRole());
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .librarian(isLibrarian)
                .build();
    }
}
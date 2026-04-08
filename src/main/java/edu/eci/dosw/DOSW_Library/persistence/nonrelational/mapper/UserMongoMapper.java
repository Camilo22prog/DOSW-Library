package edu.eci.dosw.DOSW_Library.persistence.nonrelational.mapper;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.UserDocument;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.UserRole;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMongoMapper {

    public UserDocument toDocument(User user) {
        return UserDocument.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .fullName(user.getName())
                // librarian=true → LIBRARIAN, false → USER
                .role(user.isLibrarian() ? UserRole.LIBRARIAN : UserRole.USER)
                .build();
    }

    public User toDomain(UserDocument document) {
        return User.builder()
                .id(document.getId())
                .username(document.getUsername())
                .password(document.getPassword())
                .name(document.getFullName())
                .librarian(document.getRole() == UserRole.LIBRARIAN)
                .build();
    }
}
package edu.eci.dosw.DOSW_Library.persistence.nonrelational.document;

import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.MembershipType;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.UserRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;
    private String fullName;

    @Indexed(unique = true)
    private String email;

    private UserRole role;
    private MembershipType membership;
    private LocalDateTime addedAt;
    private LocalDateTime createdAt;
}
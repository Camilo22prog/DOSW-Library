package edu.eci.dosw.DOSW_Library.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String name;
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean librarian;
}
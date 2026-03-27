package edu.eci.dosw.DOSW_Library.core.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String id;
    private String name;
    private String username;
    private String password;

    @Builder.Default
    private boolean librarian = false;
}
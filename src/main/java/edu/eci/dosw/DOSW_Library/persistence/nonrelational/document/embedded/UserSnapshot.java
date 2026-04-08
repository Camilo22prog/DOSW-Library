package edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSnapshot {
    private String username;
    private String fullName;
}
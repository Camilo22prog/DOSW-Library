package edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.embedded;

import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.LoanStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatusEvent {
    private LoanStatus status;
    private LocalDateTime executedAt;
    private String note;
}
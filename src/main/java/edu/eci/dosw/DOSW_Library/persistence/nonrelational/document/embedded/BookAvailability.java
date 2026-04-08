package edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.embedded;

import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.BookStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookAvailability {
    private BookStatus status;
    private Integer totalCopies;
    private Integer availableCopies;
    private Integer loanedCopies;
}
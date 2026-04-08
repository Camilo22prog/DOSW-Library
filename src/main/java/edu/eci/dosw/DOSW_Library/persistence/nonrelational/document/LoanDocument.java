package edu.eci.dosw.DOSW_Library.persistence.nonrelational.document;


import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.embedded.*;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.LoanStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "loans")
public class LoanDocument {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String bookId;

    private LoanStatus status;
    private LocalDateTime loanDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;

    private UserSnapshot userSnapshot;
    private BookSnapshot bookSnapshot;
    private List<LoanStatusEvent> statusHistory;
}
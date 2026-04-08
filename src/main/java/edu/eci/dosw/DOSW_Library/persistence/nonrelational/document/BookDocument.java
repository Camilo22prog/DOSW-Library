package edu.eci.dosw.DOSW_Library.persistence.nonrelational.document;

import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.embedded.BookAvailability;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.embedded.BookMetadata;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.PublicationType;
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
@Document(collection = "books")
public class BookDocument {

    @Id
    private String id;

    private String title;
    private String author;

    @Indexed(unique = true)
    private String isbn;

    private PublicationType publicationType;
    private List<String> categories;
    private LocalDateTime publishedAt;
    private LocalDateTime addedToCatalogAt;

    private BookMetadata metadata;
    private BookAvailability availability;
}
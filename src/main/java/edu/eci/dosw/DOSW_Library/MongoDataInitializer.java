package edu.eci.dosw.DOSW_Library;

import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.BookDocument;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.UserDocument;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.embedded.BookAvailability;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.embedded.BookMetadata;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.*;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository.BookMongoRepository;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository.UserMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@Profile("test")
@RequiredArgsConstructor
public class MongoDataInitializer implements CommandLineRunner {

    private final BookMongoRepository bookMongoRepository;
    private final UserMongoRepository userMongoRepository;

    @Override
    public void run(String... args) {

        if (bookMongoRepository.count() == 0) {
            BookDocument book = BookDocument.builder()
                    .title("Clean Code")
                    .author("Robert C. Martin")
                    .isbn("978-0-13-235088-4")
                    .publicationType(PublicationType.BOOK)
                    .categories(List.of("Ingeniería de Software", "Buenas Prácticas"))
                    .publishedAt(LocalDateTime.of(2008, 8, 1, 0, 0))
                    .addedToCatalogAt(LocalDateTime.now())
                    .metadata(BookMetadata.builder()
                            .pages(431)
                            .language("Español")
                            .publisher("Prentice Hall")
                            .build())
                    .availability(BookAvailability.builder()
                            .status(BookStatus.AVAILABLE)
                            .totalCopies(9)
                            .availableCopies(9)
                            .loanedCopies(0)
                            .build())
                    .build();

            bookMongoRepository.save(book);
            log.info(">>> Libro insertado en MongoDB: {}", book.getTitle());
        }

        if (userMongoRepository.count() == 0) {
            UserDocument user = UserDocument.builder()
                    .username("maria.lopez")
                    .password("$2a$12$hashedpassword")
                    .fullName("María López Rodríguez")
                    .email("maria.lopez@email.com")
                    .role(UserRole.USER)
                    .membership(MembershipType.PLATINUM)
                    .addedAt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .build();

            userMongoRepository.save(user);
            log.info(">>> Usuario insertado en MongoDB: {}", user.getUsername());
        }
    }
}
package edu.eci.dosw.DOSW_Library.persistence.relational.repository;

import edu.eci.dosw.DOSW_Library.persistence.relational.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {

    @Modifying
    @Query("UPDATE BookEntity b SET b.availableCopies = b.availableCopies - 1 WHERE b.id = :id AND b.availableCopies > 0")
    int decrementAvailableCopies(@Param("id") String id);

    @Modifying
    @Query("UPDATE BookEntity b SET b.availableCopies = b.availableCopies + 1 WHERE b.id = :id AND b.availableCopies < b.totalCopies")
    int incrementAvailableCopies(@Param("id") String id);
}

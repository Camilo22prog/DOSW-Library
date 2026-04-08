package edu.eci.dosw.DOSW_Library.persistence.relational.repository;

import edu.eci.dosw.DOSW_Library.persistence.relational.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}

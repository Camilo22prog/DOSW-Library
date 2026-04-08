package edu.eci.dosw.DOSW_Library.persistence.relational.repository.impl;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.repository.UserRepositoryPort;
import edu.eci.dosw.DOSW_Library.persistence.relational.mapper.UserPersistenceMapper;
import edu.eci.dosw.DOSW_Library.persistence.relational.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class UserRepositoryJpaImpl implements UserRepositoryPort {

    private final UserRepository repository;

    public UserRepositoryJpaImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return UserPersistenceMapper.toDomain(
                repository.save(UserPersistenceMapper.toEntity(user))
        );
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll()
                .stream()
                .map(UserPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }
}

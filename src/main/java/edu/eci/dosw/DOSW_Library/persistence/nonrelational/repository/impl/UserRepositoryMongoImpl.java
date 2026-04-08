package edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository.impl;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.repository.UserRepositoryPort;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.mapper.UserMongoMapper;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository.UserMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class UserRepositoryMongoImpl implements UserRepositoryPort {

    private final UserMongoRepository repository;

    public UserRepositoryMongoImpl(UserMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return UserMongoMapper.toDomain(
                repository.save(UserMongoMapper.toDocument(user))
        );
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id)
                .map(UserMongoMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(UserMongoMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll()
                .stream()
                .map(UserMongoMapper::toDomain)
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
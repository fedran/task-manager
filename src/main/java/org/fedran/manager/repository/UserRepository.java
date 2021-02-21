package org.fedran.manager.repository;

import org.fedran.manager.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByName(String name);

    List<User> findByNameLike(String name);
}

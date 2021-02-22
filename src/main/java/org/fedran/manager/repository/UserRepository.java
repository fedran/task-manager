package org.fedran.manager.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.fedran.manager.domain.User;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByName(String name);

    List<User> findByNameLike(String name);
}

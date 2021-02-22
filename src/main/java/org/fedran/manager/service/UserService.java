package org.fedran.manager.service;

import org.springframework.transaction.annotation.Transactional;
import org.fedran.manager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.fedran.manager.domain.User;

import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(final String name) {
        var user = new User();
        user.setName(name);
        return userRepository.save(user);
    }

    public List<User> findByNameLike(final String name) {
        if (name.isBlank()) {
            return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        }
        return userRepository.findByNameLike("%" + name + "%");
    }
}

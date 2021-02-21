package org.fedran.manager.service;

import org.fedran.manager.domain.User;
import org.fedran.manager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User create(String name) {
        var user = new User();
        user.setName(name);
        return userRepository.save(user);
    }

    @Transactional
    public List<User> findByNameLike(String name) {
        if (name == null || name.equals("")) {
            return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        }
        return userRepository.findByNameLike("%" + name + "%");
    }
}

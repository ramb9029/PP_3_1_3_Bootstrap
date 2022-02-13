package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Transactional
public interface UserService extends UserDetailsService {
    boolean saveUser(User user, String[] rolesFromHtml);
    boolean deleteUser(Long id);
    User findUserById(Long id);
    void updateUser(User user);
    List<User> getUsers();
    User findByUsername(String username);
}
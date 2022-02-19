package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    boolean saveUser(User user);
    boolean deleteUser(Long id);
    User findUserById(Long id);
    void updateUser(User user);
    List<User> getUsers();
    User findByUsername(String username);
}

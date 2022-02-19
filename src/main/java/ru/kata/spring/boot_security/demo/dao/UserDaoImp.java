package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao{

    private final EntityManager entityManager;

    @Autowired
    public UserDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean saveUser(User user) {
        try {
            entityManager.persist(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        try {
            entityManager.remove(user);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    @Override
    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public List<User> getUsers() {
        return entityManager.createQuery("SELECT user FROM User user", User.class).getResultList();
    }

    @Override
    public User findByUsername(String username) {
        User user;
        try {
            user = entityManager.createQuery("Select u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
        return user;
    }
}

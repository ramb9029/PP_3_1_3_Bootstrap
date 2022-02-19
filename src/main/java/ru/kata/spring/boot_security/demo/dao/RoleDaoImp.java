package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class RoleDaoImp implements RoleDao{

    private final EntityManager entityManager;

    @Autowired
    public RoleDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public List<Role> getRoles() {
        return entityManager.createQuery("SELECT role FROM Role role", Role.class).getResultList();
    }


    @Override
    public Role findById(Long id) {
        return entityManager.find(Role.class, id);
    }
}

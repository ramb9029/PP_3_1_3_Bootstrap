package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDaoImp;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@EnableJpaRepositories("ru.kata.spring.boot_security")
public class UserServiceImp implements UserService {

    private final UserDao userDao;
    private final ApplicationContext context;
    private final RoleDaoImp roleDaoImp;

    @Autowired
    public UserServiceImp(UserDao userDao, ApplicationContext context, RoleDaoImp roleDaoImp) {
        this.userDao = userDao;
        this.context = context;
        this.roleDaoImp = roleDaoImp;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }


    @Override
    public boolean saveUser(User user) {
        setEncryptedPassword(user);
        userDao.saveUser(user);
        return true;
    }


    @Override
    public boolean deleteUser(Long userId) {
        if (userDao.findUserById(userId) == null) {
            return false;
        }
        userDao.deleteUser(userId);
        return true;
    }


    @Override
    public User findUserById(Long id) {
        return userDao.findUserById(id);
    }


    @Override
    public void updateUser(User user) {
        User userFromDB = findUserById(user.getId());
        if (userFromDB != null) {
            user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
            user.setPassword(userFromDB.getPassword());
            userDao.updateUser(user);
        }
    }


    @Override
    public List<User> getUsers() {
        return userDao.getUsers();
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    public void setEncryptedPassword(User user) {
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
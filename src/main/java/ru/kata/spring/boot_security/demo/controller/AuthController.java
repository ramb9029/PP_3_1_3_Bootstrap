package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
public class AuthController {
    public final UserService userService;
    public final RoleDao roleDao;

    @Autowired
    public AuthController(UserService userService, RoleDao roleDao) {
        this.userService = userService;
        this.roleDao = roleDao;
    }

    @GetMapping("/admin")
    public String adminPanel(Model model, Principal principal) {
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("user", getAuthorizedUser(principal));
        model.addAttribute("allRoles", roleDao.getRoles());
        model.addAttribute("newUser", new User());
        return "admin";
    }
    private User getAuthorizedUser(Principal principal) {
//        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByUsername(principal.getName());
    }
}

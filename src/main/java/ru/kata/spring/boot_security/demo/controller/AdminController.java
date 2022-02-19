package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService service;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AdminController(UserService service, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/users")
    public String getUsers(ModelMap model, Principal principal) {
        model.addAttribute("authUser", getAuthorizedUser(principal));
        model.addAttribute("allRoles", roleDao.getRoles());
        model.addAttribute("user", new User());
        model.addAttribute("users", service.getUsers());
        return "users";
    }

    @GetMapping("/add_page")
    public String redirectToAddUserForm(ModelMap map, Principal principal) {
        map.addAttribute("authUser", getAuthorizedUser(principal));
        map.addAttribute("user", new User());
        map.addAttribute("allRoles", roleDao.getRoles());
        return "add_user";
    }

    @PostMapping("/add_user")
    public String addUser(@ModelAttribute User user, @RequestParam("roles") String[] rolesFromHtml) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        service.saveUser(user, rolesFromHtml);
        return "redirect:/admin/users";
    }

    @PostMapping("/del_user/")
    public String delUser(@ModelAttribute User user) {
        service.deleteUser(user.getId());
        return "redirect:/admin/users";
    }

    @PostMapping("/update/{id}")
    public String redirectToMergePage(@PathVariable @Validated Long id, ModelMap map) {
        map.addAttribute("user", service.findUserById(id));
        map.addAttribute("allRoles", roleDao.getRoles());
        return "redirect:/admin/users";
    }

    @GetMapping("/roles")
    public String getRoles() {
        List<Role> all = roleDao.getRoles();
        StringBuilder sb = new StringBuilder();
        sb.append("{[");
        for (Role role : all) {
            sb.append(role.getName()).append(" ");
        }
        sb.append("]}");
        return sb.toString();
    }
    @PostMapping("/update/merge")
    public String updateUser(@ModelAttribute User user) {
        service.updateUser(user);
        return "redirect:/admin/users";
    }

    private User getAuthorizedUser(Principal principal) {
        return service.findByUsername(principal.getName());
    }
}
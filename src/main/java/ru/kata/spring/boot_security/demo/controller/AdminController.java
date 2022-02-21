package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/admin/api")
public class AdminController {
    private final UserService userService;
    private final RoleDao roleDao;

    @Autowired
    public AdminController(UserService userService, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleDao = roleDao;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> showAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> showUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping(value = "/roles")
    public ResponseEntity<Iterable<Role>> findAllRoles() {
        return ResponseEntity.ok(roleDao.getRoles());
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.saveUser(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/users")
    public ResponseEntity<User> editUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        this.userService.updateUser(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
        User user = userService.findUserById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.userService.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @GetMapping(value = "/users")
//    public String getUsers(ModelMap model, Principal principal) {
//        model.addAttribute("authUser", getAuthorizedUser(principal));
//        model.addAttribute("allRoles", roleDao.getRoles());
//        model.addAttribute("user", new User());
//        model.addAttribute("users", service.getUsers());
//        return "users";
//    }
//
//    @GetMapping("/add_page")
//    public String redirectToAddUserForm(ModelMap map, Principal principal) {
//        map.addAttribute("authUser", getAuthorizedUser(principal));
//        map.addAttribute("user", new User());
//        map.addAttribute("allRoles", roleDao.getRoles());
//        return "add_user";
//    }
//
//    @PostMapping("/add_user")
//    public String addUser(@ModelAttribute User user, @RequestParam("roles") String[] rolesFromHtml) {
//        service.saveUser(user, rolesFromHtml);
//        return "redirect:/admin/users";
//    }
//
//    @PostMapping("/del_user/")
//    public String delUser(@ModelAttribute User user) {
//        service.deleteUser(user.getId());
//        return "redirect:/admin/users";
//    }
//
//    @PostMapping("/update/{id}")
//    public String redirectToMergePage(@PathVariable @Validated Long id, ModelMap map) {
//        map.addAttribute("user", service.findUserById(id));
//        map.addAttribute("allRoles", roleDao.getRoles());
//        return "redirect:/admin/users";
//    }
//
//    @GetMapping("/roles")
//    public String getRoles() {
//        List<Role> all = roleDao.getRoles();
//        StringBuilder sb = new StringBuilder();
//        sb.append("{[");
//        for (Role role : all) {
//            sb.append(role.getName()).append(" ");
//        }
//        sb.append("]}");
//        return sb.toString();
//    }
//    @PostMapping("/update/merge")
//    public String updateUser(@ModelAttribute User user) {
//        service.updateUser(user);
//        return "redirect:/admin/users";
//    }
//
//    private User getAuthorizedUser(Principal principal) {
//        return service.findByUsername(principal.getName());
//    }
}
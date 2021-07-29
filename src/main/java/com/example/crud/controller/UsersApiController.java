package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.service.RoleRepository;
import com.example.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/users")
public class UsersApiController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UsersApiController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/list")
    public String list(Model model) {
//        User user = new User();
//
        model.addAttribute("users", userService.findAll());
//        model.addAttribute("new_user", user);
//        model.addAttribute("roles", roleRepository.findAll());
//        model.addAttribute("currentPage", "admin_index");

        return "api/users/list";
    }

    @GetMapping("/new-user-form")
    public String userForm(Model model) {
        User user = new User();
//
//        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());
//        model.addAttribute("currentPage", "admin_index");

        return "api/users/new-form";
    }

}

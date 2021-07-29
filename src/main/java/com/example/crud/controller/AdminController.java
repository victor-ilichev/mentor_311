package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.service.RoleRepository;
import com.example.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @RequestMapping(value={"/", ""}, method={RequestMethod.GET})
    public String hello(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Hello!");
        messages.add("I'm Spring MVC-SECURITY application");
        messages.add("5.2.0 version by sep'19 ");
        model.addAttribute("messages", messages);

        return "admin/hello";
    }

    @GetMapping("/index")
    public String index(Model model) {
//        User user = new User();
//
//        model.addAttribute("users", userService.findAll());
//        model.addAttribute("new_user", user);
//        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("currentPage", "admin_index");

        return "admin/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id).orElse(null);

        if (null == user) {
            return "redirect:/admin/" + id + "/not-exists";
        }

        model.addAttribute("user", user);

        return "admin/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("user") User user) {
        return "admin/new";
    }

    @GetMapping("/{id}/not-exists")
    public String notExists(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);

        return "admin/not-exists";
    }

    @PostMapping()
    public ResponseEntity<User> create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());

            return new ResponseEntity<User>(user, HttpStatus.BAD_REQUEST);
        }

        userService.save(user);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id, @RequestParam(required = false) boolean disabled) {
        User user = userService.findById(id).orElse(null);

        if (null == user) {
            model.addAttribute("id", id);

            return "admin/not-exists";
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());

        if (disabled) {
            return "admin/disabled_form";
        }

        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @PathVariable("id") long id) {

        if (null == user) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<User>(user, HttpStatus.BAD_REQUEST);
        }

        userService.save(user);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@ModelAttribute("user") User user) {
        if (null == user) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        userService.delete(user);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}

package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
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
        model.addAttribute("users", userService.findAll());

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
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/new";

        userService.save(user);

        return "redirect:/admin/index";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        User user = userService.findById(id).orElse(null);

        if (null == user) {
            return "redirect:/admin/" + id + "/not-exists";
        }

        model.addAttribute("user", user);

        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @PathVariable("id") long id) {

        if (null == user) {
            return "redirect:/admin/" + id + "/not-exists";
        }

        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }

        userService.save(user);

        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@ModelAttribute("user") User user) {
        userService.delete(user);

        return "redirect:/admin/index";
    }
}

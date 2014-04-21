package ru.forxy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("oauth")
public class LoginController {

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String setupForm(final Model model) {
        model.addAttribute("name", "guest");
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@RequestParam("email") final String email, @RequestParam("password") final String password,
                        final Model model) {
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        return "hello";
    }
}

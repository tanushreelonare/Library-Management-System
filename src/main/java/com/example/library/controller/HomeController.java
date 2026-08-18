package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Home Page

    @GetMapping("/")
    public String home() {

        return "index";

    }

}
package br.com.fiap.java_afetto.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "PAGE-CONTROLLER")
public class PageController {


    @GetMapping("/")
    @Operation(summary = "Acessa o index")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    @Operation(summary = "Acessa a page de login")
    public String login() {
        return "login";
    }

    @GetMapping("/pets")
    @Operation(summary = "Acessa a page de pets")
    public String pets() {
        return "pets";
    }

    @GetMapping("/vacinas")
    @Operation(summary = "Acessa a page de vacinas")
    public String vacinas() {
        return "vacinas";
    }
}
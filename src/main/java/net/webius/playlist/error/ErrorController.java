package net.webius.playlist.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/error/400")
    public String error400() {
        return "/error/400";
    }

    @GetMapping("/error/403")
    public String error403() {
        return "/error/403";
    }

    @GetMapping("/error/404")
    public String error404() {
        return "/error/404";
    }

    @GetMapping("/error/405")
    public String error405() {
        return "/error/405";
    }

    @GetMapping("/error/500")
    public String error500() {
        return "/error/500";
    }
}

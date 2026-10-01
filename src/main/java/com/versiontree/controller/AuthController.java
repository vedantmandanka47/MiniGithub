package com.versiontree.controller;

// SYLLABUS: Spring MVC - Spring MVC Controller Annotation
import com.versiontree.model.User;
import com.versiontree.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// SYLLABUS: Cookies - Cookie management API
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
// SYLLABUS: HttpSession - Session State Management
import javax.servlet.http.HttpSession;
import java.util.Optional;

// SYLLABUS: MVC Architecture - Controller Layer handling User Authentication Flow
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(@CookieValue(name = "remember_username", defaultValue = "") String rememberedUsername, Model model) {
        model.addAttribute("username", rememberedUsername);
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam(required = false, defaultValue = "false") boolean rememberMe,
                              HttpSession session,
                              HttpServletResponse response,
                              Model model) {
        try {
            Optional<User> userOpt = userService.authenticateUser(username, password);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // SYLLABUS: HttpSession - Store authenticated user object in HTTP session
                session.setAttribute("currentUser", user);

                // SYLLABUS: Cookies - Create and store user preference Cookie
                if (rememberMe) {
                    Cookie cookie = new Cookie("remember_username", user.getUsername());
                    cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                    cookie.setPath("/");
                    response.addCookie(cookie);
                } else {
                    Cookie cookie = new Cookie("remember_username", "");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }

                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Invalid username or password.");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam String username,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam(required = false) String bio,
                                 @RequestParam(required = false) String skills,
                                 HttpSession session,
                                 Model model) {
        try {
            User user = userService.registerUser(username, email, password, bio, skills);
            // SYLLABUS: HttpSession - Auto-login upon registration
            session.setAttribute("currentUser", user);
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // SYLLABUS: HttpSession - Invalidate session on logout
        session.invalidate();
        return "redirect:/login?loggedOut=true";
    }
}

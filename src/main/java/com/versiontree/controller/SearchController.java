package com.versiontree.controller;

// SYLLABUS: Spring MVC - Search Controller
import com.versiontree.model.Repository;
import com.versiontree.model.User;
import com.versiontree.service.RepositoryService;
import com.versiontree.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public String search(@RequestParam(required = false, defaultValue = "") String q, Model model, HttpSession session) {
        String keyword = q.trim();
        List<Repository> matchedRepos = repositoryService.searchRepositories(keyword);
        List<User> matchedUsers = userService.searchDevelopers(keyword);

        User currentUser = (User) session.getAttribute("currentUser");

        model.addAttribute("query", keyword);
        model.addAttribute("repos", matchedRepos);
        model.addAttribute("users", matchedUsers);
        model.addAttribute("currentUser", currentUser);

        return "search";
    }

    @GetMapping("/profile")
    public String viewProfile(@RequestParam(required = false) String username, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        User profileUser = null;

        if (username != null && !username.trim().isEmpty()) {
            profileUser = userService.getUserByUsername(username).orElse(null);
        } else if (currentUser != null) {
            profileUser = currentUser;
        }

        if (profileUser == null) {
            return "redirect:/dashboard";
        }

        List<Repository> userRepos = repositoryService.getRepositoriesByOwner(profileUser.getId());

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("userRepos", userRepos);
        model.addAttribute("currentUser", currentUser);

        return "profile";
    }
}

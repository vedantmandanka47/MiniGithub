package com.versiontree.controller;

// SYLLABUS: Spring MVC - Routing and View Controllers
import com.versiontree.model.Activity;
import com.versiontree.model.Repository;
import com.versiontree.model.User;
import com.versiontree.service.RepositoryService;
import com.versiontree.service.UserService;
import com.versiontree.dao.ActivityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityDao activityDao;

    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/dashboard";
        }
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // SYLLABUS: HttpSession - Verify authentication
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Fetch user repositories
        List<Repository> userRepos = repositoryService.getRepositoriesByOwner(currentUser.getId());
        // Fetch activity feed of followed developers
        List<Activity> activityFeed = activityDao.findActivitiesByFollowedUsers(currentUser.getId(), 10);
        if (activityFeed.isEmpty()) {
            activityFeed = activityDao.findActivitiesByUser(currentUser.getId(), 10);
        }

        model.addAttribute("userRepos", userRepos);
        model.addAttribute("activityFeed", activityFeed);
        model.addAttribute("currentUser", currentUser);

        return "dashboard";
    }
}

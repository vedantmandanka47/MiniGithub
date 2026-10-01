package com.versiontree.controller;

// SYLLABUS: Spring MVC - Admin Controller
import com.versiontree.model.Activity;
import com.versiontree.model.User;
import com.versiontree.service.AdminService;
import com.versiontree.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RepositoryService repositoryService;

    @GetMapping("")
    public String adminPanel(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            return "redirect:/dashboard?error=Admin+Access+Required";
        }

        List<User> users = adminService.getAllUsers();
        List<Activity> auditLogs = adminService.getRecentAuditLogs(20);
        // SYLLABUS: JDBC - Retrieving raw JDBC platform statistics for syllabus demonstration
        Map<String, Object> jdbcStats = adminService.getLegacyJdbcStats();

        model.addAttribute("users", users);
        model.addAttribute("auditLogs", auditLogs);
        model.addAttribute("jdbcStats", jdbcStats);
        model.addAttribute("currentUser", currentUser);

        return "admin";
    }

    @PostMapping("/user/{id}/toggle")
    public String toggleUserStatus(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            adminService.toggleUserStatus(id);
        }
        return "redirect:/admin";
    }

    @PostMapping("/repo/{id}/delete")
    public String deleteRepository(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            repositoryService.deleteRepository(id);
        }
        return "redirect:/admin";
    }

    @PostMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable Long id, @RequestParam Long repoId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            adminService.deleteComment(id);
        }
        return "redirect:/repository/" + repoId;
    }
}

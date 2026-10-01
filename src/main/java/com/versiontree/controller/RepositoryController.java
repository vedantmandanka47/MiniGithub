package com.versiontree.controller;

// SYLLABUS: Spring MVC - Request Mapping & Controller Design
import com.versiontree.model.*;
import com.versiontree.service.RepositoryService;
import com.versiontree.service.UserService;
import com.versiontree.dao.FileDao;
import com.versiontree.dao.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/repository")
public class RepositoryController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private CommentDao commentDao;

    @GetMapping("/new")
    public String showNewRepoForm(HttpSession session) {
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/login";
        }
        return "repository-new";
    }

    @PostMapping("/create")
    public String createRepository(@RequestParam String name,
                                   @RequestParam String description,
                                   @RequestParam String visibility,
                                   @RequestParam String language,
                                   HttpSession session,
                                   Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        try {
            Repository repo = repositoryService.createRepository(currentUser.getId(), name, description, visibility, language);
            return "redirect:/repository/" + repo.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "repository-new";
        }
    }

    @GetMapping("/{id}")
    public String viewRepository(@PathVariable Long id, HttpSession session, Model model) {
        Optional<Repository> repoOpt = repositoryService.getRepositoryById(id);
        if (!repoOpt.isPresent()) {
            return "redirect:/dashboard?error=Repo+not+found";
        }

        Repository repo = repoOpt.get();
        User currentUser = (User) session.getAttribute("currentUser");

        if ("PRIVATE".equalsIgnoreCase(repo.getVisibility())) {
            if (currentUser == null || !repo.getOwner().getId().equals(currentUser.getId())) {
                return "redirect:/dashboard?error=Access+Denied";
            }
        }

        List<FileModel> files = fileDao.findFilesByRepository(id);
        List<Comment> comments = commentDao.findByRepositoryId(id);

        boolean isStarred = false;
        boolean isFollowingOwner = false;

        if (currentUser != null) {
            isStarred = repositoryService.getRepositoryById(id).isPresent() && 
                        repositoryService.getRepositoriesByOwner(currentUser.getId()).stream().anyMatch(r -> r.getId().equals(id));
            isFollowingOwner = repositoryService.toggleFollow(currentUser.getId(), repo.getOwner().getId());
            // Revert the check state toggle
            repositoryService.toggleFollow(currentUser.getId(), repo.getOwner().getId());
        }

        model.addAttribute("repo", repo);
        model.addAttribute("files", files);
        model.addAttribute("comments", comments);
        model.addAttribute("isStarred", isStarred);
        model.addAttribute("isFollowingOwner", isFollowingOwner);
        model.addAttribute("currentUser", currentUser);

        return "repository";
    }

    @PostMapping("/{id}/star")
    public String starRepository(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";
        repositoryService.toggleStar(currentUser.getId(), id);
        return "redirect:/repository/" + id;
    }

    @PostMapping("/follow/{userId}")
    public String followUser(@PathVariable Long userId, @RequestParam(required = false, defaultValue = "dashboard") String redirect, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";
        repositoryService.toggleFollow(currentUser.getId(), userId);
        return "redirect:/" + redirect;
    }

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String content, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";
        if (content != null && !content.trim().isEmpty()) {
            repositoryService.addComment(currentUser.getId(), id, content.trim());
        }
        return "redirect:/repository/" + id;
    }
}

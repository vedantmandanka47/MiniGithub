package com.versiontree.controller;

// SYLLABUS: Spring MVC - Spring MVC File Controller
import com.versiontree.model.*;
import com.versiontree.service.RepositoryService;
import com.versiontree.dao.FileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private RepositoryService repositoryService;

    @GetMapping("/{fileId}")
    public String viewFileDetails(@PathVariable Long fileId, Model model, HttpSession session) {
        FileModel file = fileDao.findFileById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found"));
        List<FileVersion> versions = fileDao.findVersionsByFile(fileId);

        String currentContent = "";
        FileVersion currentVersion = null;

        if (file.getCurrentVersionId() != null) {
            currentVersion = fileDao.findVersionById(file.getCurrentVersionId()).orElse(null);
            if (currentVersion != null && Files.exists(Paths.get(currentVersion.getContentPath()))) {
                try {
                    currentContent = new String(Files.readAllBytes(Paths.get(currentVersion.getContentPath())));
                } catch (Exception e) {
                    currentContent = "// Error reading file content: " + e.getMessage();
                }
            }
        }

        User currentUser = (User) session.getAttribute("currentUser");

        model.addAttribute("file", file);
        model.addAttribute("versions", versions);
        model.addAttribute("currentVersion", currentVersion);
        model.addAttribute("currentContent", currentContent);
        model.addAttribute("currentUser", currentUser);

        return "file-detail";
    }

    @PostMapping("/{fileId}/restore/{versionId}")
    public String restoreVersion(@PathVariable Long fileId, @PathVariable Long versionId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        try {
            FileVersion restored = repositoryService.restoreFileVersion(fileId, versionId, currentUser.getId());
            return "redirect:/file/" + fileId;
        } catch (Exception e) {
            return "redirect:/file/" + fileId + "?error=" + e.getMessage();
        }
    }

    @GetMapping("/download/{versionId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFileVersion(@PathVariable Long versionId) {
        FileVersion version = fileDao.findVersionById(versionId).orElseThrow(() -> new IllegalArgumentException("Version not found"));
        File diskFile = new File(version.getContentPath());
        if (!diskFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(diskFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + version.getFile().getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(diskFile.length())
                .body(resource);
    }
}

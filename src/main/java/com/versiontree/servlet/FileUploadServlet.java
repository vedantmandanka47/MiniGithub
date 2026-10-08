package com.versiontree.servlet;

// SYLLABUS: Servlet - Java Servlet API Implementation
import com.versiontree.model.User;
import com.versiontree.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// SYLLABUS: HttpSession - Servlet Session Validation
import javax.servlet.http.HttpSession;
import java.io.IOException;

// SYLLABUS: Servlet - Mapping Servlet URL pattern for file upload endpoint
@WebServlet(name = "FileUploadServlet", urlPatterns = "/servlet/upload-file")
@MultipartConfig(maxFileSize = 50L * 1024 * 1024, maxRequestSize = 50L * 1024 * 1024)
@Component
public class FileUploadServlet extends HttpServlet {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Inject Spring components into Servlet lifecycle
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    // SYLLABUS: Servlet - HttpServlet doPost request handling
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // SYLLABUS: HttpSession - Session verification inside Servlet
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Long repoId = Long.parseLong(request.getParameter("repoId"));
            Part archive = request.getPart("archive");
            String changeNote = request.getParameter("changeNote");

            if (archive == null || archive.getSize() == 0) {
                request.setAttribute("error", "Choose a ZIP folder before uploading.");
                request.getRequestDispatcher("/WEB-INF/jsp/repository.jsp").forward(request, response);
                return;
            }

            try (java.io.InputStream input = archive.getInputStream()) {
                repositoryService.uploadZip(repoId, currentUser.getId(), input, changeNote);
            }
            response.sendRedirect(request.getContextPath() + "/repository/" + repoId);

        } catch (Exception e) {
            System.err.println("[FileUploadServlet] Exception: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/repository/" + request.getParameter("repoId") + "?error=" + e.getMessage());
        }
    }
}

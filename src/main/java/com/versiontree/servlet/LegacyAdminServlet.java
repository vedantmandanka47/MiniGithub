package com.versiontree.servlet;

// SYLLABUS: Servlet - Legacy Raw JDBC Admin Servlet Implementation
import com.versiontree.dao.LegacyAdminJdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

// SYLLABUS: Servlet - WebServlet Endpoint Annotation
@WebServlet(name = "LegacyAdminServlet", urlPatterns = "/servlet/legacy-admin-report")
@Component
public class LegacyAdminServlet extends HttpServlet {

    @Autowired
    private LegacyAdminJdbcDao legacyAdminJdbcDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    // SYLLABUS: Servlet - doGet Request Handler
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        Map<String, Object> stats = legacyAdminJdbcDao.getPlatformSummaryStatistics();
        List<Map<String, String>> users = legacyAdminJdbcDao.getRawUserList();

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html><head><title>Legacy JDBC Admin Report</title>");
            out.println("<style>body{font-family:sans-serif;padding:20px;background:#161b22;color:#c9d1d9;}");
            out.println("table{width:100%;border-collapse:collapse;margin-top:15px;} th,td{border:1px solid #30363d;padding:8px;text-align:left;}");
            out.println("th{background:#21262d;color:#58a6ff;} a{color:#58a6ff;text-decoration:none;}</style></head><body>");
            out.println("<h2>Legacy Raw JDBC Audit & Metrics Report</h2>");
            out.println("<p><a href='" + request.getContextPath() + "/admin'>Back to Admin Panel</a></p>");
            out.println("<h3>Summary Statistics (via PreparedStatement & ResultSet)</h3>");
            out.println("<ul>");
            out.println("<li><strong>Total Users:</strong> " + stats.getOrDefault("totalUsers", 0) + "</li>");
            out.println("<li><strong>Total Repositories:</strong> " + stats.getOrDefault("totalRepositories", 0) + "</li>");
            out.println("<li><strong>Total Files:</strong> " + stats.getOrDefault("totalFiles", 0) + "</li>");
            out.println("</ul>");

            out.println("<h3>Raw Database User Query Results</h3>");
            out.println("<table><tr><th>ID</th><th>Username</th><th>Email</th><th>Role</th><th>Status</th></tr>");
            for (Map<String, String> u : users) {
                out.println("<tr><td>" + u.get("id") + "</td><td>" + u.get("username") + "</td><td>" + u.get("email") + "</td><td>" + u.get("role") + "</td><td>" + u.get("status") + "</td></tr>");
            }
            out.println("</table></body></html>");
        }
    }
}

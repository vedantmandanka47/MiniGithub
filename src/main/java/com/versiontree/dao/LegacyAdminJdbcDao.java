package com.versiontree.dao;

// SYLLABUS: JDBC - Core JDBC Imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LegacyAdminJdbcDao {

    @Autowired
    private DataSource dataSource;

    /**
     * Legacy raw JDBC method to fetch overall platform metrics for syllabus demonstration.
     */
    public Map<String, Object> getPlatformSummaryStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // SYLLABUS: JDBC - Explicit Database Connection via DataSource
        try (Connection connection = dataSource.getConnection()) {

            // 1. Total Users Query
            // SYLLABUS: PreparedStatement - Parameterized/Prepared SQL Execution
            String userQuery = "SELECT COUNT(*) FROM users";
            try (PreparedStatement pstmt = connection.prepareStatement(userQuery)) {
                // SYLLABUS: ResultSet - Iterating query results
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        stats.put("totalUsers", rs.getLong(1));
                    }
                }
            }

            // 2. Total Repositories Query
            String repoQuery = "SELECT COUNT(*) FROM repositories";
            try (PreparedStatement pstmt = connection.prepareStatement(repoQuery);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalRepositories", rs.getLong(1));
                }
            }

            // 3. Total Files & Versions Query
            String fileQuery = "SELECT COUNT(*) FROM files";
            try (PreparedStatement pstmt = connection.prepareStatement(fileQuery);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalFiles", rs.getLong(1));
                }
            }

        // SYLLABUS: SQLException - Error Handling for JDBC Operations
        } catch (SQLException e) {
            System.err.println("[LegacyAdminJdbcDao] JDBC Error occurred: " + e.getMessage());
            e.printStackTrace();
            stats.put("error", e.getMessage());
        }

        return stats;
    }

    /**
     * Legacy raw JDBC method to fetch recent admin audit logs.
     */
    public List<Map<String, String>> getRawUserList() {
        List<Map<String, String>> userList = new ArrayList<>();
        String query = "SELECT id, username, email, role, status FROM users ORDER BY id ASC";

        // SYLLABUS: JDBC - Connection Management
        try (Connection connection = dataSource.getConnection();
             // SYLLABUS: PreparedStatement
             PreparedStatement pstmt = connection.prepareStatement(query);
             // SYLLABUS: ResultSet
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", String.valueOf(rs.getLong("id")));
                row.put("username", rs.getString("username"));
                row.put("email", rs.getString("email"));
                row.put("role", rs.getString("role"));
                row.put("status", rs.getString("status"));
                userList.add(row);
            }

        // SYLLABUS: SQLException
        } catch (SQLException e) {
            System.err.println("[LegacyAdminJdbcDao] SQLException while fetching user list: " + e.getMessage());
        }

        return userList;
    }
}

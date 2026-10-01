package com.versiontree.service;

import com.versiontree.dao.*;
import com.versiontree.model.Activity;
import com.versiontree.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdminService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RepositoryDao repositoryDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private LegacyAdminJdbcDao legacyAdminJdbcDao;

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Transactional
    public void toggleUserStatus(Long userId) {
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
            user.setStatus("DISABLED");
        } else {
            user.setStatus("ACTIVE");
        }
        userDao.save(user);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentDao.deleteById(commentId);
    }

    public List<Activity> getRecentAuditLogs(int limit) {
        return activityDao.findRecentActivities(limit);
    }

    // SYLLABUS: JDBC - Delegation to raw JDBC module for admin dashboard stats
    public Map<String, Object> getLegacyJdbcStats() {
        return legacyAdminJdbcDao.getPlatformSummaryStatistics();
    }
}

package com.versiontree.service;

import com.versiontree.dao.UserDao;
import com.versiontree.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(String username, String email, String plainPassword, String bio, String skills) {
        if (userDao.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken.");
        }
        if (userDao.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email '" + email + "' is already registered.");
        }

        String encodedPassword = passwordEncoder.encode(plainPassword);
        User user = new User(username, email, encodedPassword, bio, skills, "DEVELOPER");
        return userDao.save(user);
    }

    public Optional<User> authenticateUser(String usernameOrEmail, String plainPassword) {
        Optional<User> userOpt = userDao.findByUsername(usernameOrEmail);
        if (!userOpt.isPresent()) {
            userOpt = userDao.findByEmail(usernameOrEmail);
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if ("DISABLED".equalsIgnoreCase(user.getStatus())) {
                throw new IllegalStateException("Your account has been disabled by an administrator.");
            }
            if (passwordEncoder.matches(plainPassword, user.getPasswordHash()) || plainPassword.equals(user.getPasswordHash())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public List<User> searchDevelopers(String keyword) {
        return userDao.searchDevelopers(keyword);
    }

    public User updateUserProfile(Long userId, String bio, String skills) {
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBio(bio);
        user.setSkills(skills);
        return userDao.save(user);
    }
}

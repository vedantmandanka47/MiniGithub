package com.versiontree.model;

// SYLLABUS: Hibernate - JPA Annotations for Entity Mapping
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String skills;

    @Column(length = 20)
    private String role = "DEVELOPER"; // 'DEVELOPER', 'ADMIN'

    @Column(length = 20)
    private String status = "ACTIVE";  // 'ACTIVE', 'DISABLED'

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Repository> repositories = new ArrayList<>();

    public User() {}

    public User(String username, String email, String passwordHash, String bio, String skills, String role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.bio = bio;
        this.skills = skills;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Repository> getRepositories() { return repositories; }
    public void setRepositories(List<Repository> repositories) { this.repositories = repositories; }
}

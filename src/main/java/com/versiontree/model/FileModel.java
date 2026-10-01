package com.versiontree.model;

// SYLLABUS: Hibernate - OneToMany and ManyToOne Relationships
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "files")
public class FileModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id", nullable = false)
    private Repository repository;

    @Column(nullable = false)
    private String filename;

    @Column(length = 50)
    private String filetype;

    @Column(name = "current_version_id")
    private Long currentVersionId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileVersion> versions = new ArrayList<>();

    public FileModel() {}

    public FileModel(Repository repository, String filename, String filetype) {
        this.repository = repository;
        this.filename = filename;
        this.filetype = filetype;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Repository getRepository() { return repository; }
    public void setRepository(Repository repository) { this.repository = repository; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getFiletype() { return filetype; }
    public void setFiletype(String filetype) { this.filetype = filetype; }

    public Long getCurrentVersionId() { return currentVersionId; }
    public void setCurrentVersionId(Long currentVersionId) { this.currentVersionId = currentVersionId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<FileVersion> getVersions() { return versions; }
    public void setVersions(List<FileVersion> versions) { this.versions = versions; }
}

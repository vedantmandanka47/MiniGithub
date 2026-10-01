package com.versiontree.model;

// SYLLABUS: Hibernate - ORM Versioning Mapping
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_versions")
public class FileVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileModel file;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(name = "content_path", nullable = false, columnDefinition = "TEXT")
    private String contentPath;

    @Column(name = "file_size")
    private Long fileSize = 0L;

    @Column(name = "change_note", columnDefinition = "TEXT")
    private String changeNote;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public FileVersion() {}

    public FileVersion(FileModel file, Integer versionNumber, String contentPath, Long fileSize, String changeNote) {
        this.file = file;
        this.versionNumber = versionNumber;
        this.contentPath = contentPath;
        this.fileSize = fileSize;
        this.changeNote = changeNote;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FileModel getFile() { return file; }
    public void setFile(FileModel file) { this.file = file; }

    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }

    public String getContentPath() { return contentPath; }
    public void setContentPath(String contentPath) { this.contentPath = contentPath; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getChangeNote() { return changeNote; }
    public void setChangeNote(String changeNote) { this.changeNote = changeNote; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

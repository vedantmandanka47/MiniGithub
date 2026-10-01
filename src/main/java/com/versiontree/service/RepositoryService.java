package com.versiontree.service;

// SYLLABUS: Transactions - Declarative Transaction Management for Multi-Step DB Operations
import com.versiontree.dao.ActivityDao;
import com.versiontree.dao.CommentDao;
import com.versiontree.dao.FileDao;
import com.versiontree.dao.RepositoryDao;
import com.versiontree.dao.UserDao;
import com.versiontree.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RepositoryService {

    @Autowired
    private RepositoryDao repositoryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ActivityDao activityDao;

    private static final String STORAGE_DIR = "vt_storage/";

    public RepositoryService() {
        File dir = new File(STORAGE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // SYLLABUS: Transactions - Transactional repository creation + activity logging
    @Transactional
    public Repository createRepository(Long ownerId, String name, String description, String visibility, String language) {
        User owner = userDao.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Repository repo = new Repository(owner, name, description, visibility, language);
        Repository savedRepo = repositoryDao.save(repo);

        // Audit activity log
        Activity activity = new Activity(owner, "CREATE_REPO", savedRepo.getId(), "Created repository " + name);
        activityDao.save(activity);

        return savedRepo;
    }

    public Optional<Repository> getRepositoryById(Long id) {
        return repositoryDao.findById(id);
    }

    public List<Repository> getRepositoriesByOwner(Long ownerId) {
        return repositoryDao.findByOwnerId(ownerId);
    }

    public List<Repository> getPublicRepositories() {
        return repositoryDao.findPublicRepositories();
    }

    public List<Repository> getPopularRepositories(int limit) {
        return repositoryDao.getPopularRepositories(limit);
    }

    public List<Repository> searchRepositories(String keyword) {
        return repositoryDao.searchRepositories(keyword);
    }

    // SYLLABUS: Transactions - Atomic multi-step operation: File upload + Version generation + Activity Log
    @Transactional
    public FileVersion uploadOrUpdateFile(Long repoId, Long userId, String filename, String content, String changeNote) throws IOException {
        Repository repo = repositoryDao.findById(repoId).orElseThrow(() -> new IllegalArgumentException("Repository not found"));
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if file already exists in repo
        List<FileModel> repoFiles = fileDao.findFilesByRepository(repoId);
        FileModel fileModel = repoFiles.stream()
                .filter(f -> f.getFilename().equalsIgnoreCase(filename))
                .findFirst()
                .orElse(null);

        if (fileModel == null) {
            String filetype = filename.contains(".") ? filename.substring(filename.lastIndexOf(".") + 1).toUpperCase() : "TXT";
            fileModel = new FileModel(repo, filename, filetype);
            fileModel = fileDao.saveFile(fileModel);
        }

        // Determine version number
        List<FileVersion> existingVersions = fileDao.findVersionsByFile(fileModel.getId());
        int nextVersionNumber = existingVersions.size() + 1;

        // Save file content to storage disk
        String storagePath = STORAGE_DIR + "repo_" + repoId + "_file_" + fileModel.getId() + "_v" + nextVersionNumber + "_" + filename;
        try (FileWriter writer = new FileWriter(storagePath)) {
            writer.write(content);
        }

        long fileSize = new File(storagePath).length();
        FileVersion version = new FileVersion(fileModel, nextVersionNumber, storagePath, fileSize, changeNote);
        version = fileDao.saveVersion(version);

        // Update current version pointer
        fileModel.setCurrentVersionId(version.getId());
        fileDao.saveFile(fileModel);

        // Record activity log
        Activity activity = new Activity(user, "UPLOAD_FILE", repoId, "Uploaded version " + nextVersionNumber + " of " + filename);
        activityDao.save(activity);

        return version;
    }

    // SYLLABUS: Transactions - Restore older file version by creating a new version pointing to older content
    @Transactional
    public FileVersion restoreFileVersion(Long fileId, Long versionId, Long userId) throws IOException {
        FileModel file = fileDao.findFileById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found"));
        FileVersion versionToRestore = fileDao.findVersionById(versionId).orElseThrow(() -> new IllegalArgumentException("Version not found"));
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Read restored content
        String restoredContent = "";
        if (Files.exists(Paths.get(versionToRestore.getContentPath()))) {
            restoredContent = new String(Files.readAllBytes(Paths.get(versionToRestore.getContentPath())));
        }

        return uploadOrUpdateFile(file.getRepository().getId(), userId, file.getFilename(), restoredContent, "Restored to version #" + versionToRestore.getVersionNumber());
    }

    // SYLLABUS: Transactions - Toggle Star
    @Transactional
    public boolean toggleStar(Long userId, Long repoId) {
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Repository repo = repositoryDao.findById(repoId).orElseThrow(() -> new IllegalArgumentException("Repository not found"));

        boolean isStarred = repositoryDao.isStarred(userId, repoId);
        if (isStarred) {
            repositoryDao.removeStar(userId, repoId);
            return false;
        } else {
            repositoryDao.addStar(new Star(user, repo));
            activityDao.save(new Activity(user, "STAR_REPO", repoId, "Starred repository " + repo.getName()));
            return true;
        }
    }

    // SYLLABUS: Transactions - Toggle Follow
    @Transactional
    public boolean toggleFollow(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) return false;
        User follower = userDao.findById(followerId).orElseThrow(() -> new IllegalArgumentException("Follower not found"));
        User followee = userDao.findById(followeeId).orElseThrow(() -> new IllegalArgumentException("Followee not found"));

        boolean isFollowing = repositoryDao.isFollowing(followerId, followeeId);
        if (isFollowing) {
            repositoryDao.removeFollow(followerId, followeeId);
            return false;
        } else {
            repositoryDao.addFollow(new Follow(follower, followee));
            activityDao.save(new Activity(follower, "FOLLOW", followeeId, "Followed developer " + followee.getUsername()));
            return true;
        }
    }

    @Transactional
    public Comment addComment(Long userId, Long repoId, String content) {
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Repository repo = repositoryDao.findById(repoId).orElseThrow(() -> new IllegalArgumentException("Repository not found"));

        Comment comment = new Comment(user, repo, content);
        Comment saved = commentDao.save(comment);
        activityDao.save(new Activity(user, "COMMENT", repoId, "Commented on " + repo.getName()));
        return saved;
    }

    // SYLLABUS: Transactions - Cascading deletion of repository
    @Transactional
    public void deleteRepository(Long repoId) {
        Repository repo = repositoryDao.findById(repoId).orElseThrow(() -> new IllegalArgumentException("Repository not found"));
        repositoryDao.delete(repo);
    }
}

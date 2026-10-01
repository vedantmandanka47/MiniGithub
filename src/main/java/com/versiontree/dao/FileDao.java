package com.versiontree.dao;

// SYLLABUS: HQL - Querying File Metadata and Version History
import com.versiontree.model.FileModel;
import com.versiontree.model.FileVersion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class FileDao {

    @PersistenceContext
    private EntityManager entityManager;

    public FileModel saveFile(FileModel file) {
        if (file.getId() == null) {
            entityManager.persist(file);
            return file;
        } else {
            return entityManager.merge(file);
        }
    }

    public FileVersion saveVersion(FileVersion version) {
        if (version.getId() == null) {
            entityManager.persist(version);
            return version;
        } else {
            return entityManager.merge(version);
        }
    }

    public Optional<FileModel> findFileById(Long id) {
        return Optional.ofNullable(entityManager.find(FileModel.class, id));
    }

    public Optional<FileVersion> findVersionById(Long versionId) {
        return Optional.ofNullable(entityManager.find(FileVersion.class, versionId));
    }

    public List<FileModel> findFilesByRepository(Long repoId) {
        // SYLLABUS: HQL
        String hql = "FROM FileModel f WHERE f.repository.id = :repoId ORDER BY f.filename ASC";
        TypedQuery<FileModel> query = entityManager.createQuery(hql, FileModel.class);
        query.setParameter("repoId", repoId);
        return query.getResultList();
    }

    public List<FileVersion> findVersionsByFile(Long fileId) {
        // SYLLABUS: HQL - Fetching historical file versions ordered by version number
        String hql = "FROM FileVersion fv WHERE fv.file.id = :fileId ORDER BY fv.versionNumber DESC";
        TypedQuery<FileVersion> query = entityManager.createQuery(hql, FileVersion.class);
        query.setParameter("fileId", fileId);
        return query.getResultList();
    }
}

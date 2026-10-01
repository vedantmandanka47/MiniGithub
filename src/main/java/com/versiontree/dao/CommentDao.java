package com.versiontree.dao;

// SYLLABUS: HQL - Querying Repository Discussion Comments
import com.versiontree.model.Comment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class CommentDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            entityManager.persist(comment);
            return comment;
        } else {
            return entityManager.merge(comment);
        }
    }

    public List<Comment> findByRepositoryId(Long repoId) {
        // SYLLABUS: HQL
        String hql = "FROM Comment c WHERE c.repository.id = :repoId ORDER BY c.createdAt ASC";
        TypedQuery<Comment> query = entityManager.createQuery(hql, Comment.class);
        query.setParameter("repoId", repoId);
        return query.getResultList();
    }

    public void deleteById(Long commentId) {
        Comment comment = entityManager.find(Comment.class, commentId);
        if (comment != null) {
            entityManager.remove(comment);
        }
    }
}

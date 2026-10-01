package com.versiontree.dao;

// SYLLABUS: HQL - Advanced Hibernate Query Language Operations
import com.versiontree.model.Repository;
import com.versiontree.model.Star;
import com.versiontree.model.Follow;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class RepositoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Repository save(Repository repository) {
        if (repository.getId() == null) {
            entityManager.persist(repository);
            return repository;
        } else {
            return entityManager.merge(repository);
        }
    }

    public Optional<Repository> findById(Long id) {
        Repository repo = entityManager.find(Repository.class, id);
        return Optional.ofNullable(repo);
    }

    public List<Repository> findByOwnerId(Long ownerId) {
        // SYLLABUS: HQL - Querying Repositories by Owner
        String hql = "FROM Repository r WHERE r.owner.id = :ownerId ORDER BY r.createdAt DESC";
        TypedQuery<Repository> query = entityManager.createQuery(hql, Repository.class);
        query.setParameter("ownerId", ownerId);
        return query.getResultList();
    }

    public List<Repository> findPublicRepositories() {
        // SYLLABUS: HQL
        String hql = "FROM Repository r WHERE r.visibility = 'PUBLIC' ORDER BY r.createdAt DESC";
        return entityManager.createQuery(hql, Repository.class).getResultList();
    }

    public List<Repository> getPopularRepositories(int limit) {
        // SYLLABUS: HQL - Aggregation and Order By HQL Query
        String hql = "SELECT r FROM Repository r LEFT JOIN r.stars s WHERE r.visibility = 'PUBLIC' GROUP BY r.id ORDER BY COUNT(s.id) DESC";
        TypedQuery<Repository> query = entityManager.createQuery(hql, Repository.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Repository> searchRepositories(String keyword) {
        // SYLLABUS: HQL - Multi-field search
        String hql = "FROM Repository r WHERE r.visibility = 'PUBLIC' AND (LOWER(r.name) LIKE LOWER(:kw) OR LOWER(r.description) LIKE LOWER(:kw) OR LOWER(r.language) LIKE LOWER(:kw))";
        TypedQuery<Repository> query = entityManager.createQuery(hql, Repository.class);
        query.setParameter("kw", "%" + keyword + "%");
        return query.getResultList();
    }

    public boolean isStarred(Long userId, Long repositoryId) {
        // SYLLABUS: HQL
        String hql = "SELECT COUNT(s) FROM Star s WHERE s.user.id = :userId AND s.repository.id = :repoId";
        TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
        query.setParameter("userId", userId);
        query.setParameter("repoId", repositoryId);
        return query.getSingleResult() > 0;
    }

    public void addStar(Star star) {
        entityManager.persist(star);
    }

    public void removeStar(Long userId, Long repositoryId) {
        // SYLLABUS: HQL
        String hql = "DELETE FROM Star s WHERE s.user.id = :userId AND s.repository.id = :repoId";
        entityManager.createQuery(hql)
                .setParameter("userId", userId)
                .setParameter("repoId", repositoryId)
                .executeUpdate();
    }

    public boolean isFollowing(Long followerId, Long followeeId) {
        // SYLLABUS: HQL
        String hql = "SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :followerId AND f.followee.id = :followeeId";
        TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
        query.setParameter("followerId", followerId);
        query.setParameter("followeeId", followeeId);
        return query.getSingleResult() > 0;
    }

    public void addFollow(Follow follow) {
        entityManager.persist(follow);
    }

    public void removeFollow(Long followerId, Long followeeId) {
        String hql = "DELETE FROM Follow f WHERE f.follower.id = :followerId AND f.followee.id = :followeeId";
        entityManager.createQuery(hql)
                .setParameter("followerId", followerId)
                .setParameter("followeeId", followeeId)
                .executeUpdate();
    }

    public void delete(Repository repository) {
        entityManager.remove(entityManager.contains(repository) ? repository : entityManager.merge(repository));
    }
}

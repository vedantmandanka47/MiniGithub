package com.versiontree.dao;

// SYLLABUS: Hibernate - HQL Querying and Persistence
import com.versiontree.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> findByUsername(String username) {
        // SYLLABUS: HQL - Hibernate Query Language
        String hql = "FROM User u WHERE u.username = :username";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class);
        query.setParameter("username", username);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        // SYLLABUS: HQL
        String hql = "FROM User u WHERE u.email = :email";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class);
        query.setParameter("email", email);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<User> findAll() {
        // SYLLABUS: HQL
        return entityManager.createQuery("FROM User u ORDER BY u.id ASC", User.class).getResultList();
    }

    public List<User> searchDevelopers(String keyword) {
        // SYLLABUS: HQL - Pattern Matching Query
        String hql = "FROM User u WHERE LOWER(u.username) LIKE LOWER(:kw) OR LOWER(u.skills) LIKE LOWER(:kw) OR LOWER(u.bio) LIKE LOWER(:kw)";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class);
        query.setParameter("kw", "%" + keyword + "%");
        return query.getResultList();
    }
}

package com.versiontree.dao;

// SYLLABUS: HQL - Activity Feed and Audit Log Queries
import com.versiontree.model.Activity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class ActivityDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Activity save(Activity activity) {
        if (activity.getId() == null) {
            entityManager.persist(activity);
            return activity;
        } else {
            return entityManager.merge(activity);
        }
    }

    public List<Activity> findRecentActivities(int limit) {
        // SYLLABUS: HQL - Recent Activity Feed Query
        String hql = "FROM Activity a ORDER BY a.createdAt DESC";
        TypedQuery<Activity> query = entityManager.createQuery(hql, Activity.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Activity> findActivitiesByFollowedUsers(Long followerId, int limit) {
        // SYLLABUS: HQL - Subquery and Join for Followed User Activity Feed
        String hql = "FROM Activity a WHERE a.user.id IN (SELECT f.followee.id FROM Follow f WHERE f.follower.id = :followerId) ORDER BY a.createdAt DESC";
        TypedQuery<Activity> query = entityManager.createQuery(hql, Activity.class);
        query.setParameter("followerId", followerId);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}

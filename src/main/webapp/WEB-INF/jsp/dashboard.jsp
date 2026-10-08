<%@ include file="header.jspf" %>

<div class="container">
    <div class="grid-2col">
        <!-- Main Column: Repositories & Activity -->
        <div>
            <div class="card">
                <div class="card-header">
                    <span>Your Repositories (${userRepos.size()})</span>
                    <a href="${pageContext.request.contextPath}/repository/new" class="btn btn-primary btn-sm">+ Create Repository</a>
                </div>

                <c:choose>
                    <c:when test="${not empty userRepos}">
                        <ul style="list-style: none;">
                            <c:forEach var="repo" items="${userRepos}">
                                <li style="padding: 12px 0; border-bottom: 1px solid var(--border-color);">
                                    <div style="display: flex; justify-content: space-between; align-items: center;">
                                        <a href="${pageContext.request.contextPath}/repository/${repo.id}" style="font-size: 1.1rem; font-weight: 600; color: var(--accent-blue); text-decoration: none;">
                                            ${repo.name}
                                        </a>
                                        <span class="badge ${repo.visibility eq 'PUBLIC' ? 'badge-public' : 'badge-private'}">${repo.visibility}</span>
                                    </div>
                                    <p style="font-size: 0.88rem; color: var(--text-secondary); margin-top: 4px;">${repo.description}</p>
                                    <div style="display: flex; gap: 12px; margin-top: 6px; font-size: 0.8rem; color: var(--text-secondary);">
                                        <span class="badge badge-lang">${repo.language}</span>
                                        <span>${repo.stars.size()} stars</span>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <p style="color: var(--text-secondary); padding: 10px 0;">You haven't created any repositories yet. Click <strong>+ Create Repository</strong> above to start!</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Recent Activity Feed -->
            <div class="card">
                <div class="card-header">
                    <span>Community Activity Feed</span>
                </div>
                <c:choose>
                    <c:when test="${not empty activityFeed}">
                        <c:forEach var="act" items="${activityFeed}">
                            <div class="activity-item">
                                <span class="activity-icon">Activity</span>
                                <div>
                                    <p style="font-size: 0.9rem;">
                                        <strong><a href="${pageContext.request.contextPath}/profile?username=${act.user.username}" style="color: var(--text-primary); text-decoration: none;">${act.user.username}</a></strong>
                                        ${act.details}
                                    </p>
                                    <span style="font-size: 0.78rem; color: var(--text-secondary);">${act.createdAt}</span>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p style="color: var(--text-secondary);">No recent activity recorded.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Sidebar: Quick Info -->
        <div>
            <div class="card">
                <div class="card-header">
                    <span>Developer Profile</span>
                </div>
                <p style="font-weight: 600; font-size: 1.05rem;">${currentUser.username}</p>
                <p style="font-size: 0.85rem; color: var(--text-secondary); margin-top: 4px;">${currentUser.email}</p>
                <p style="font-size: 0.85rem; color: var(--text-secondary); margin-top: 6px;">${currentUser.bio}</p>
                <div style="margin-top: 12px;">
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary btn-sm" style="width: 100%;">View Full Profile</a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jspf" %>

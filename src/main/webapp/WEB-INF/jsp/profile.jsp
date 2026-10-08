<%@ include file="header.jspf" %>

<div class="container" style="max-width: 800px;">
    <div class="card">
        <div style="display: flex; gap: 20px; align-items: flex-start; flex-wrap: wrap;">
            <div style="width: 80px; height: 80px; background-color: var(--accent-blue); border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 2.2rem; color: #fff; font-weight: bold;">
                ${profileUser.username.substring(0, 1).toUpperCase()}
            </div>
            <div style="flex: 1;">
                <h2>${profileUser.username}</h2>
                <p style="color: var(--text-secondary); font-size: 0.9rem;">${profileUser.email} - Member since ${profileUser.createdAt}</p>
                <div style="margin-top: 10px;">
                    <strong>Role:</strong> <span class="badge badge-public">${profileUser.role}</span>
                    <strong style="margin-left: 12px;">Status:</strong> <span class="badge ${profileUser.status eq 'ACTIVE' ? 'badge-public' : 'badge-private'}">${profileUser.status}</span>
                </div>
                <div style="margin-top: 12px;">
                    <strong>Skills:</strong> <span style="color: var(--accent-blue);">${profileUser.skills}</span>
                </div>
                <p style="margin-top: 10px; background-color: var(--bg-primary); padding: 10px; border-radius: 6px; border: 1px solid var(--border-color); font-size: 0.9rem;">
                    ${profileUser.bio}
                </p>
                <c:if test="${not empty currentUser and currentUser.id eq profileUser.id}">
                    <form action="${pageContext.request.contextPath}/profile/update" method="post" style="margin-top: 16px;">
                        <div class="form-group">
                            <label for="skills">Skills and Languages</label>
                            <input type="text" id="skills" name="skills" class="form-control" value="${profileUser.skills}" placeholder="Java, Spring, PostgreSQL">
                        </div>
                        <div class="form-group">
                            <label for="bio">Bio</label>
                            <textarea id="bio" name="bio" class="form-control" placeholder="Tell the community about your projects and interests...">${profileUser.bio}</textarea>
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm">Save Profile</button>
                    </form>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Repositories owned by this user -->
    <div class="card">
        <div class="card-header">
            <span>Repositories owned by ${profileUser.username} (${userRepos.size()})</span>
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
                <p style="color: var(--text-secondary);">No public repositories found for this developer.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="footer.jspf" %>

<%@ include file="header.jspf" %>

<div class="container">
    <div class="card" style="margin-bottom: 16px;">
        <div style="display: flex; justify-content: space-between; align-items: flex-start; flex-wrap: wrap; gap: 12px;">
            <div>
                <h2 style="display: flex; align-items: center; gap: 10px;">
                    <a href="${pageContext.request.contextPath}/profile?username=${repo.owner.username}" style="color: var(--accent-blue); text-decoration: none;">${repo.owner.username}</a> / ${repo.name}
                    <span class="badge ${repo.visibility eq 'PUBLIC' ? 'badge-public' : 'badge-private'}">${repo.visibility}</span>
                </h2>
                <p style="color: var(--text-secondary); margin-top: 6px;">${repo.description}</p>
                <div style="margin-top: 8px; font-size: 0.85rem; color: var(--text-secondary); display: flex; gap: 16px;">
                    <span class="badge badge-lang">${repo.language}</span>
                    <span>Created ${repo.createdAt}</span>
                    <span>${repo.stars.size()} Stars</span>
                </div>
            </div>

            <div style="display: flex; gap: 10px;">
                <c:if test="${not empty sessionScope.currentUser}">
                    <form action="${pageContext.request.contextPath}/repository/${repo.id}/star" method="post" style="display: inline;">
                        <button type="submit" class="btn btn-secondary btn-sm">
                            ${isStarred ? 'Unstar' : 'Star'} (${repo.stars.size()})
                        </button>
                    </form>

                    <c:if test="${sessionScope.currentUser.id ne repo.owner.id}">
                        <form action="${pageContext.request.contextPath}/repository/follow/${repo.owner.id}" method="post" style="display: inline;">
                            <input type="hidden" name="redirect" value="repository/${repo.id}">
                            <button type="submit" class="btn btn-secondary btn-sm">
                                ${isFollowingOwner ? 'Unfollow' : 'Follow'} ${repo.owner.username}
                            </button>
                        </form>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>

    <c:if test="${not empty param.error}">
        <div class="alert alert-error">${param.error}</div>
    </c:if>

    <!-- File Explorer & Upload Form -->
    <div class="card">
        <div class="card-header">
            <span>Repository Files (${files.size()})</span>
        </div>

        <c:choose>
            <c:when test="${not empty files}">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Filename</th>
                            <th>Type</th>
                            <th>Current Version</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="f" items="${files}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/file/${f.id}" style="color: var(--accent-blue); font-weight: 600; text-decoration: none;">
                                        ${f.filename}
                                    </a>
                                </td>
                                <td><span class="badge badge-lang">${f.filetype}</span></td>
                                <td>Version #${f.versions.size()}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/file/${f.id}" class="btn btn-secondary btn-sm">View Details & History</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p style="color: var(--text-secondary); padding: 10px 0;">No files in this repository yet.</p>
            </c:otherwise>
        </c:choose>

        <%-- SYLLABUS: Servlet - ZIP folder upload via FileUploadServlet (/servlet/upload-file) --%>
        <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.id eq repo.owner.id}">
            <div style="margin-top: 24px; border-top: 1px solid var(--border-color); padding-top: 16px;">
                <h4>Upload Project Folder</h4>
                <form action="${pageContext.request.contextPath}/servlet/upload-file" method="post" enctype="multipart/form-data" style="margin-top: 12px;">
                    <input type="hidden" name="repoId" value="${repo.id}">
                    <div class="form-group">
                        <label for="archive">Project ZIP file</label>
                        <input type="file" id="archive" name="archive" class="form-control" accept=".zip,application/zip" required>
                        <small style="color: var(--text-secondary);">Upload a ZIP folder. Nested folders and source paths will be preserved in the repository.</small>
                    </div>
                    <div class="form-group">
                        <label for="changeNote">Commit Note / Version Description</label>
                        <input type="text" id="changeNote" name="changeNote" class="form-control" placeholder="e.g. Added lexer and parser sources">
                    </div>
                    <button type="submit" class="btn btn-primary">Upload ZIP / Commit Versions</button>
                </form>
            </div>
        </c:if>
    </div>

    <!-- Collaboration & Comments Section -->
    <div class="card">
        <div class="card-header">
            <span>Project Discussion Thread (${comments.size()})</span>
        </div>

        <c:choose>
            <c:when test="${not empty comments}">
                <div style="display: flex; flex-direction: column; gap: 12px; margin-bottom: 20px;">
                    <c:forEach var="c" items="${comments}">
                        <div style="background-color: var(--bg-primary); border: 1px solid var(--border-color); border-radius: 6px; padding: 12px;">
                            <div style="display: flex; justify-content: space-between; margin-bottom: 6px; font-size: 0.85rem;">
                                <strong><a href="${pageContext.request.contextPath}/profile?username=${c.user.username}" style="color: var(--accent-blue); text-decoration: none;">${c.user.username}</a></strong>
                                <span style="color: var(--text-secondary);">${c.createdAt}</span>
                            </div>
                            <p style="font-size: 0.9rem;">${c.content}</p>
                            <c:if test="${sessionScope.currentUser.role eq 'ADMIN'}">
                                <form action="${pageContext.request.contextPath}/admin/comment/${c.id}/delete" method="post" style="margin-top: 6px;">
                                    <input type="hidden" name="repoId" value="${repo.id}">
                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Remove comment?');">Delete Comment</button>
                                </form>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <p style="color: var(--text-secondary); margin-bottom: 16px;">No comments yet. Start the conversation below!</p>
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty sessionScope.currentUser}">
            <form action="${pageContext.request.contextPath}/repository/${repo.id}/comment" method="post">
                <div class="form-group">
                    <textarea name="content" class="form-control" placeholder="Write a comment or discussion point..." required style="min-height: 80px;"></textarea>
                </div>
                <button type="submit" class="btn btn-secondary">Post Comment</button>
            </form>
        </c:if>
    </div>
</div>

<%@ include file="footer.jspf" %>

<%@ include file="header.jspf" %>

<div class="container">
    <div style="margin-bottom: 16px;">
        <a href="${pageContext.request.contextPath}/repository/${file.repository.id}" style="color: var(--accent-blue); text-decoration: none;">&larr; Back to ${file.repository.name}</a>
    </div>

    <div class="card">
        <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 12px;">
            <div>
                <h2>${file.filename}</h2>
                <div style="margin-top: 6px; font-size: 0.85rem; color: var(--text-secondary); display: flex; gap: 12px;">
                    <span class="badge badge-lang">${file.filetype}</span>
                    <span>Active Version: #${currentVersion.versionNumber}</span>
                    <span>Size: ${currentVersion.fileSize} bytes</span>
                    <span>Updated: ${currentVersion.createdAt}</span>
                </div>
            </div>

            <c:if test="${not empty currentVersion}">
                <a href="${pageContext.request.contextPath}/file/download/${currentVersion.id}" class="btn btn-secondary btn-sm">
                    &#x2193; Download Version #${currentVersion.versionNumber}
                </a>
            </c:if>
        </div>
    </div>

    <c:if test="${not empty param.error}">
        <div class="alert alert-error">${param.error}</div>
    </c:if>

    <!-- Code Content Preview -->
    <div class="card">
        <div class="card-header">
            <span>File Content (Version #${currentVersion.versionNumber})</span>
            <span style="font-size: 0.8rem; color: var(--text-secondary); font-weight: normal;">Note: ${currentVersion.changeNote}</span>
        </div>
        <pre class="code-block"><code><c:out value="${currentContent}"/></code></pre>
    </div>

    <!-- Version History Timeline -->
    <div class="card">
        <div class="card-header">
            <span>Version History Timeline (${versions.size()} versions)</span>
        </div>

        <table class="table">
            <thead>
                <tr>
                    <th>Version</th>
                    <th>Change Note / Commit Message</th>
                    <th>File Size</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="v" items="${versions}">
                    <tr style="${v.id eq file.currentVersionId ? 'background-color: rgba(88, 166, 255, 0.05);' : ''}">
                        <td>
                            <strong>Version #${v.versionNumber}</strong>
                            <c:if test="${v.id eq file.currentVersionId}">
                                <span class="badge badge-public" style="margin-left: 6px;">CURRENT</span>
                            </c:if>
                        </td>
                        <td>${v.changeNote}</td>
                        <td>${v.fileSize} bytes</td>
                        <td>${v.createdAt}</td>
                        <td>
                            <div style="display: flex; gap: 8px;">
                                <a href="${pageContext.request.contextPath}/file/download/${v.id}" class="btn btn-secondary btn-sm">Download</a>
                                <c:if test="${not empty sessionScope.currentUser and v.id ne file.currentVersionId}">
                                    <form action="${pageContext.request.contextPath}/file/${file.id}/restore/${v.id}" method="post" style="display: inline;">
                                        <button type="submit" class="btn btn-primary btn-sm" onclick="return confirm('Restore file to Version #${v.versionNumber}?');">Restore</button>
                                    </form>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="footer.jspf" %>

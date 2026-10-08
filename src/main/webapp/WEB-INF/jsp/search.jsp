<%@ include file="header.jspf" %>

<div class="container">
    <div class="card">
        <h2>Search Results for "${query}"</h2>
        <p style="color: var(--text-secondary); margin-top: 4px;">Found ${repos.size()} repositories and ${users.size()} developers</p>
    </div>

    <div class="grid-2col">
        <!-- Repositories Results -->
        <div>
            <div class="card">
                <div class="card-header">
                    <span>Matching Repositories (${repos.size()})</span>
                </div>

                <c:choose>
                    <c:when test="${not empty repos}">
                        <ul style="list-style: none;">
                            <c:forEach var="repo" items="${repos}">
                                <li style="padding: 12px 0; border-bottom: 1px solid var(--border-color);">
                                    <a href="${pageContext.request.contextPath}/repository/${repo.id}" style="font-size: 1.1rem; font-weight: 600; color: var(--accent-blue); text-decoration: none;">
                                        ${repo.owner.username} / ${repo.name}
                                    </a>
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
                        <p style="color: var(--text-secondary);">No repositories matched your query.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Developers Results -->
        <div>
            <div class="card">
                <div class="card-header">
                    <span>Matching Developers (${users.size()})</span>
                </div>

                <c:choose>
                    <c:when test="${not empty users}">
                        <ul style="list-style: none;">
                            <c:forEach var="u" items="${users}">
                                <li style="padding: 10px 0; border-bottom: 1px solid var(--border-color);">
                                    <a href="${pageContext.request.contextPath}/profile?username=${u.username}" style="font-weight: 600; color: var(--accent-blue); text-decoration: none;">
                                        ${u.username}
                                    </a>
                                    <p style="font-size: 0.8rem; color: var(--text-secondary);">${u.skills}</p>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <p style="color: var(--text-secondary);">No developers matched your query.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jspf" %>

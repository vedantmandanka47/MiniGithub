<%@ include file="header.jspf" %>

<div class="container">
    <div class="card">
        <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 12px;">
            <div>
                <h2>Administrator Operations Panel</h2>
                <p style="color: var(--text-secondary); font-size: 0.9rem;">Manage platform users, monitor system activities, and inspect raw JDBC metrics.</p>
            </div>
            <div>
                <%-- SYLLABUS: Servlet - Direct link to Legacy raw JDBC servlet report --%>
                <a href="${pageContext.request.contextPath}/servlet/legacy-admin-report" class="btn btn-secondary" target="_blank">
                    &#x1F4CA; View Raw JDBC Servlet Report
                </a>
            </div>
        </div>
    </div>

    <!-- Legacy JDBC Metrics Widget -->
    <div class="card" style="background: linear-gradient(135deg, #161b22 0%, #1f242d 100%); border-color: var(--accent-blue);">
        <div class="card-header">
            <span style="color: var(--accent-blue);">Raw JDBC System Metrics (Syllabus Demo)</span>
        </div>
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 16px; text-align: center;">
            <div style="padding: 12px; background: rgba(255,255,255,0.02); border-radius: 6px;">
                <span style="font-size: 1.8rem; font-weight: bold; color: var(--accent-green);">${jdbcStats.totalUsers}</span>
                <p style="font-size: 0.85rem; color: var(--text-secondary);">Registered Users</p>
            </div>
            <div style="padding: 12px; background: rgba(255,255,255,0.02); border-radius: 6px;">
                <span style="font-size: 1.8rem; font-weight: bold; color: var(--accent-purple);">${jdbcStats.totalRepositories}</span>
                <p style="font-size: 0.85rem; color: var(--text-secondary);">Total Repositories</p>
            </div>
            <div style="padding: 12px; background: rgba(255,255,255,0.02); border-radius: 6px;">
                <span style="font-size: 1.8rem; font-weight: bold; color: var(--accent-blue);">${jdbcStats.totalFiles}</span>
                <p style="font-size: 0.85rem; color: var(--text-secondary);">Tracked Files</p>
            </div>
        </div>
    </div>

    <!-- User Management Table -->
    <div class="card">
        <div class="card-header">
            <span>User Management (${users.size()} users)</span>
        </div>
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td>${u.id}</td>
                        <td><strong>${u.username}</strong></td>
                        <td>${u.email}</td>
                        <td><span class="badge badge-lang">${u.role}</span></td>
                        <td>
                            <span class="badge ${u.status eq 'ACTIVE' ? 'badge-public' : 'badge-private'}">${u.status}</span>
                        </td>
                        <td>
                            <c:if test="${u.id ne sessionScope.currentUser.id}">
                                <form action="${pageContext.request.contextPath}/admin/user/${u.id}/toggle" method="post" style="display: inline;">
                                    <button type="submit" class="btn ${u.status eq 'ACTIVE' ? 'btn-danger' : 'btn-primary'} btn-sm">
                                        ${u.status eq 'ACTIVE' ? 'Disable User' : 'Enable User'}
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Audit Logs -->
    <div class="card">
        <div class="card-header">
            <span>System Audit Activity Log (${auditLogs.size()})</span>
        </div>
        <table class="table">
            <thead>
                <tr>
                    <th>User</th>
                    <th>Action Type</th>
                    <th>Details</th>
                    <th>Timestamp</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="log" items="${auditLogs}">
                    <tr>
                        <td><strong>${log.user.username}</strong></td>
                        <td><span class="badge badge-public">${log.actionType}</span></td>
                        <td>${log.details}</td>
                        <td>${log.createdAt}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="footer.jspf" %>

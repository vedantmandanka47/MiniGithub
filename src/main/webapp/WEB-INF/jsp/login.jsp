<%@ include file="header.jspf" %>

<div class="container" style="max-width: 420px; margin-top: 50px;">
    <div class="card">
        <h2 style="text-align: center; margin-bottom: 20px;">Sign in to Version Tree</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${param.loggedOut eq 'true'}">
            <div class="alert alert-success">You have been logged out successfully.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Username or Email</label>
                <input type="text" id="username" name="username" class="form-control" value="${username}" required placeholder="Enter username or email">
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" class="form-control" required placeholder="Enter password">
            </div>

            <%-- SYLLABUS: Cookies - Remember me checkbox utilizing Cookie persistence --%>
            <div class="form-group" style="display: flex; align-items: center; gap: 8px;">
                <input type="checkbox" id="rememberMe" name="rememberMe" value="true" ${not empty username ? 'checked' : ''}>
                <label for="rememberMe" style="margin-bottom: 0; font-size: 0.85rem; color: var(--text-secondary);">Remember me on this browser</label>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 10px;">Sign In</button>
        </form>

        <p style="text-align: center; margin-top: 20px; font-size: 0.85rem; color: var(--text-secondary);">
            New to Version Tree? <a href="${pageContext.request.contextPath}/register" style="color: var(--accent-blue);">Create an account</a>.
        </p>
    </div>
</div>

<%@ include file="footer.jspf" %>

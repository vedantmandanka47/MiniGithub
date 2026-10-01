<%@ include file="header.jspf" %>

<div class="container" style="max-width: 480px; margin-top: 40px;">
    <div class="card">
        <h2 style="text-align: center; margin-bottom: 20px;">Join Version Tree</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-group">
                <label for="username">Username *</label>
                <input type="text" id="username" name="username" class="form-control" required placeholder="Choose a unique username">
            </div>

            <div class="form-group">
                <label for="email">Email Address *</label>
                <input type="email" id="email" name="email" class="form-control" required placeholder="name@domain.com">
            </div>

            <div class="form-group">
                <label for="password">Password *</label>
                <input type="password" id="password" name="password" class="form-control" required placeholder="Create a strong password">
            </div>

            <div class="form-group">
                <label for="skills">Skills & Languages</label>
                <input type="text" id="skills" name="skills" class="form-control" placeholder="e.g. Java, Spring, Hibernate, C++, MySQL">
            </div>

            <div class="form-group">
                <label for="bio">Bio / Developer Profile</label>
                <textarea id="bio" name="bio" class="form-control" placeholder="Tell the community about your projects and interests..."></textarea>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 10px;">Create Account</button>
        </form>

        <p style="text-align: center; margin-top: 20px; font-size: 0.85rem; color: var(--text-secondary);">
            Already registered? <a href="${pageContext.request.contextPath}/login" style="color: var(--accent-blue);">Sign in here</a>.
        </p>
    </div>
</div>

<%@ include file="footer.jspf" %>

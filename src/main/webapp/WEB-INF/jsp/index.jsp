<%@ include file="header.jspf" %>

<div class="container">
    <div class="card" style="text-align: center; padding: 40px 20px; background: linear-gradient(135deg, #161b22 0%, #21262d 100%);">
        <h1 style="font-size: 2.5rem; margin-bottom: 12px; color: var(--accent-blue);">Version Tree (VT)</h1>
        <div style="display: flex; gap: 16px; justify-content: center;">
            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary" style="padding: 10px 24px; font-size: 1rem;">Get Started - Sign Up</a>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary" style="padding: 10px 24px; font-size: 1rem;">Sign In</a>
        </div>
    </div>

</div>

<%@ include file="footer.jspf" %>

<%@ include file="header.jspf" %>

<div class="container">
    <div class="card" style="text-align: center; padding: 40px 20px; background: linear-gradient(135deg, #161b22 0%, #21262d 100%);">
        <h1 style="font-size: 2.5rem; margin-bottom: 12px; color: var(--accent-blue);">Version Tree (VT)</h1>
        <p style="font-size: 1.15rem; color: var(--text-secondary); max-width: 700px; margin: 0 auto 24px;">
            A student-friendly project collaboration & version management platform built for Advanced Java Programming. Track repository file history, share code, star projects, and follow developers.
        </p>
        <div style="display: flex; gap: 16px; justify-content: center;">
            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary" style="padding: 10px 24px; font-size: 1rem;">Get Started — Sign Up</a>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary" style="padding: 10px 24px; font-size: 1rem;">Sign In</a>
        </div>
    </div>

    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; margin-top: 30px;">
        <div class="card">
            <h3 style="color: var(--accent-green); margin-bottom: 8px;">Repository Management</h3>
            <p style="color: var(--text-secondary); font-size: 0.9rem;">
                Create public or private repositories, upload project source files, organize code assets, and manage project details.
            </p>
        </div>
        <div class="card">
            <h3 style="color: var(--accent-purple); margin-bottom: 8px;">Version Tracking & Restore</h3>
            <p style="color: var(--text-secondary); font-size: 0.9rem;">
                Every file update creates a new immutable version. Inspect historical diffs, notes, and restore older file states seamlessly.
            </p>
        </div>
        <div class="card">
            <h3 style="color: var(--accent-blue); margin-bottom: 8px;">Developer Collaboration</h3>
            <p style="color: var(--text-secondary); font-size: 0.9rem;">
                Star repositories, follow fellow developers, engage in discussion threads, and stay updated via personalized activity feeds.
            </p>
        </div>
    </div>
</div>

<%@ include file="footer.jspf" %>

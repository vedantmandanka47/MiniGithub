<%@ include file="header.jspf" %>

<div class="container" style="max-width: 600px;">
    <div class="card">
        <h2 style="margin-bottom: 16px;">Create a New Repository</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/repository/create" method="post">
            <div class="form-group">
                <label for="name">Repository Name *</label>
                <input type="text" id="name" name="name" class="form-control" required placeholder="e.g. Compiler-Design-Lab">
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" class="form-control" placeholder="Short description of your project..."></textarea>
            </div>

            <div class="form-group">
                <label for="language">Primary Language</label>
                <select id="language" name="language" class="form-control">
                    <option value="Java" selected>Java</option>
                    <option value="C++">C++</option>
                    <option value="Python">Python</option>
                    <option value="JavaScript">JavaScript</option>
                    <option value="HTML/CSS">HTML/CSS</option>
                    <option value="Other">Other</option>
                </select>
            </div>

            <div class="form-group">
                <label for="visibility">Visibility</label>
                <select id="visibility" name="visibility" class="form-control">
                    <option value="PUBLIC" selected>Public - Anyone on Version Tree can view</option>
                    <option value="PRIVATE">Private - Only you can view</option>
                </select>
            </div>

            <div style="display: flex; gap: 12px; margin-top: 20px;">
                <button type="submit" class="btn btn-primary">Create Repository</button>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="footer.jspf" %>

<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2 class="mb-3">
	<c:choose>
		<c:when test="${isAdd}">Add Suffix Type</c:when>
		<c:otherwise>Edit Suffix Type</c:otherwise>
	</c:choose>
</h2>

<form
	action="${pageContext.request.contextPath}/master/suffix/type/save"
	method="post" class="card shadow-sm p-4">

	<input type="hidden" name="isAdd" value="${isAdd}" />

	<div class="mb-3">
		<label class="form-label">Suffix ID</label> <input type="number"
			name="suffixId" class="form-control" value="${suffixType.suffixId}"
			${!isAdd ? "readonly" : ""} required />
	</div>

	<div class="mb-3">
		<label class="form-label">Suffix Name</label> <input type="text"
			name="suffixName" class="form-control"
			value="${suffixType.suffixName}" required />
	</div>

	<div class="d-flex justify-content-between">
		<a href="${pageContext.request.contextPath}/master/suffix"
			class="btn btn-secondary">Back</a>

		<button type="submit" class="btn btn-success">Save</button>
	</div>
</form>

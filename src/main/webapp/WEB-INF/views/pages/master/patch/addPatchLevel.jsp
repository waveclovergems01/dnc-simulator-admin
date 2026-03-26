<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="d-flex justify-content-between align-items-center mb-3">
	<h3 class="m-0">
		<c:choose>
			<c:when test="${mode == 'EDIT'}">Edit Patch Level</c:when>
			<c:otherwise>Add Patch Level</c:otherwise>
		</c:choose>
	</h3>

	<a class="btn btn-secondary"
		href="${pageContext.request.contextPath}/master/patch/viewPatchLevel">
		Back </a>
</div>

<div class="card">
	<div class="card-body">
		<form id="levelForm">
			<input type="hidden" id="id" name="id" value="${level.id}" />

			<div class="mb-3">
				<label class="form-label">Level</label> <input type="text"
					class="form-control" id="level" name="level" value="${level.level}"
					placeholder="e.g. 95" autocomplete="off" />
				<div class="form-text">Number only, must be greater than 0</div>
			</div>

			<button type="submit" class="btn btn-primary" id="btnSave">
				Save</button>
		</form>
	</div>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
	var APP_CTX = "${pageContext.request.contextPath}";
</script>

<script
	src="${pageContext.request.contextPath}/resources/js/master/patch/addPatchLevel.js"></script>
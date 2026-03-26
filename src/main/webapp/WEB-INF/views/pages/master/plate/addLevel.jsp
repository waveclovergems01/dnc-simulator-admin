<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="d-flex justify-content-between align-items-center mb-3">

	<h2 class="mb-0">

		<c:choose>
			<c:when test="${mode == 'EDIT'}">
				Edit Level
			</c:when>
			<c:otherwise>
				Add Level
			</c:otherwise>
		</c:choose>

	</h2>

	<a href="${pageContext.request.contextPath}/master/plate/viewLevel"
		class="btn btn-secondary"> Back </a>

</div>


<div class="card shadow-sm">

	<div class="card-body">

		<form id="levelForm">

			<c:if test="${mode == 'EDIT'}">
				<input type="hidden" id="id" name="id" value="${level.id}" />
			</c:if>

			<div class="mb-3">

				<label class="form-label">Level</label> <input type="number"
					class="form-control" id="level" name="level" value="${level.level}"
					placeholder="Example: 60" required />

			</div>


			<button type="submit" class="btn btn-primary" id="btnSave">

				Save</button>

		</form>

	</div>

</div>


<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
	window.APP_CTX = "${pageContext.request.contextPath}";
</script>

<script
	src="${pageContext.request.contextPath}/resources/js/master/plate/addLevel.js"></script>
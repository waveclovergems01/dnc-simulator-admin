<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">Plate Level</h2>

	<a href="${pageContext.request.contextPath}/master/plate/addLevel"
		class="btn btn-primary"> + Add Level </a>
</div>

<table id="levelTable"
	class="table table-striped table-hover shadow-sm w-100">

	<thead class="table-dark">
		<tr>
			<th style="width: 80px;">ID</th>
			<th>Level</th>
			<th style="width: 180px;">Action</th>
		</tr>
	</thead>

	<tbody>
		<c:if test="${not empty levelList}">
			<c:forEach items="${levelList}" var="l">
				<tr>
					<td>${l.id}</td>
					<td>${l.level}</td>
					<td class="text-center"><a
						href="${pageContext.request.contextPath}/master/plate/editLevel?id=${l.id}"
						class="btn btn-sm btn-warning me-1">Edit</a>

						<form
							action="${pageContext.request.contextPath}/master/plate/deleteLevel"
							method="post" style="display: inline;"
							onsubmit="return confirm('Are you sure you want to delete this level?');">
							<input type="hidden" name="id" value="${l.id}" />
							<button type="submit" class="btn btn-sm btn-danger">Delete</button>
						</form></td>
				</tr>
			</c:forEach>
		</c:if>
	</tbody>

</table>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/dataTables.bootstrap5.min.js"></script>

<script>
	window.APP_CTX = "${pageContext.request.contextPath}";
</script>

<script
	src="${pageContext.request.contextPath}/resources/js/master/plate/viewLevel.js"></script>
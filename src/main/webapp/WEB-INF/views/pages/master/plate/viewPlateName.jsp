<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<div class="d-flex align-items-center mb-3">
	<h2 class="mb-0">Plate Name</h2>

	<div class="ms-auto d-flex gap-2">
		<a href="${pageContext.request.contextPath}/master/plate/addPlateName"
			class="btn btn-primary"> + Add Plate Name </a> <a
			href="${pageContext.request.contextPath}/master/plate/viewPlate"
			class="btn btn-secondary"> Back </a>
	</div>
</div>

<table id="plateNameTable"
	class="table table-striped table-hover shadow-sm w-100">

	<thead class="table-dark">
		<tr>
			<th style="width: 80px;">ID</th>
			<th style="width: 90px;">Icon</th>
			<th>Plate Name</th>
			<th style="width: 180px;">Action</th>
		</tr>
	</thead>

	<tbody>
		<c:if test="${not empty plateNameList}">
			<c:forEach items="${plateNameList}" var="x">
				<tr>
					<td>${x.id}</td>

					<td class="text-center"><img
						src="${pageContext.request.contextPath}/master/plate/plateNameIcon?id=${x.id}"
						alt="icon"
						style="width: 40px; height: 40px; object-fit: contain; border-radius: 6px;"
						onerror="this.style.display='none';" /></td>

					<td>${fn:escapeXml(x.name)}</td>

					<td class="text-center"><a
						href="${pageContext.request.contextPath}/master/plate/editPlateName?id=${x.id}"
						class="btn btn-sm btn-warning me-1"> Edit </a>

						<form
							action="${pageContext.request.contextPath}/master/plate/deletePlateName"
							method="post" style="display: inline;"
							onsubmit="return confirm('Are you sure you want to delete this plate name?');">
							<input type="hidden" name="id" value="${x.id}" />
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
	src="${pageContext.request.contextPath}/resources/js/master/plate/viewPlateName.js"></script>
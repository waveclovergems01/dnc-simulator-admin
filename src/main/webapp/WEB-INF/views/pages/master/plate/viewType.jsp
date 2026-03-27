<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!-- DataTables CSS (ถ้า layout/main ยังไม่ include ให้ใส่ไว้ที่นี่ได้) -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">Plate Type</h2>

	<div class="ms-auto d-flex gap-2">
		<a href="${pageContext.request.contextPath}/master/plate/addType"
			class="btn btn-primary"> + Add Type </a>
		<a href="${pageContext.request.contextPath}/master/plate/viewPlate"
		class="btn btn-secondary"> Back </a>
	</div>
</div>

<table id="typeTable"
	class="table table-striped table-hover shadow-sm w-100">
	<thead class="table-dark">
		<tr>
			<th style="width: 80px;">ID</th>
			<th>Name</th>
			<th style="width: 180px;">Action</th>
		</tr>
	</thead>

	<tbody>
		<c:choose>
			<c:when test="${empty typeList}">
				<tr>
					<td colspan="3" class="text-center text-muted">No data</td>
				</tr>
			</c:when>

			<c:otherwise>
				<c:forEach items="${typeList}" var="t">
					<tr>
						<td>${t.id}</td>
						<td>${fn:escapeXml(t.name)}</td>
						<td class="text-center"><a
							href="${pageContext.request.contextPath}/master/plate/editType?id=${t.id}"
							class="btn btn-sm btn-warning me-1"> Edit </a>

							<form
								action="${pageContext.request.contextPath}/master/plate/deleteType"
								method="post" style="display: inline;"
								onsubmit="return confirm('Are you sure you want to delete this type?');">
								<input type="hidden" name="id" value="${t.id}" />
								<button type="submit" class="btn btn-sm btn-danger">
									Delete</button>
							</form></td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>

<!-- jQuery + DataTables JS (ถ้า layout/main include อยู่แล้ว ให้เอา 3 บรรทัดนี้ออกได้) -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/dataTables.bootstrap5.min.js"></script>

<script>
	window.APP_CTX = "${pageContext.request.contextPath}";
</script>
<script
	src="${pageContext.request.contextPath}/resources/js/master/plate/viewType.js"></script>
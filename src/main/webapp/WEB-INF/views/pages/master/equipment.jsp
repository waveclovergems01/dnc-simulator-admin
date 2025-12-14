<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- ================= DataTables CSS ================= -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<!-- ================= Page Header ================= -->
<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">Master Equipment Items</h2>

	<a href="${pageContext.request.contextPath}/master/equipment/add"
		class="btn btn-primary"> + Add Equipment </a>
</div>

<!-- ================= Equipment Table ================= -->
<table id="equipmentTable"
	class="table table-striped table-hover shadow-sm w-100">
	<thead class="table-dark">
		<tr>
			<th>Item ID</th>
			<th>Name</th>
			<th>Type ID</th>
			<th>Job ID</th>
			<th>Req Lv</th>
			<th>Rarity</th>
			<th>Durability</th>
			<th style="width: 180px;">Action</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${items}" var="e">
			<tr>
				<td>${e.itemId}</td>
				<td>${e.name}</td>
				<td>${e.typeId}</td>
				<td>${e.jobId}</td>
				<td>${e.requiredLevel}</td>
				<td>${e.rarityId}</td>
				<td>${e.durability}</td>
				<td class="text-center"><a
					href="${pageContext.request.contextPath}/master/equipment/edit?itemId=${e.itemId}"
					class="btn btn-sm btn-warning me-1"> Edit </a>

					<form
						action="${pageContext.request.contextPath}/master/equipment/delete"
						method="post" style="display: inline;"
						onsubmit="return confirm('Delete this equipment item?');">
						<input type="hidden" name="itemId" value="${e.itemId}" />
						<button type="submit" class="btn btn-sm btn-danger">
							Delete</button>
					</form></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<!-- ================= JS ================= -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/dataTables.bootstrap5.min.js"></script>

<script>
	$(document).ready(function() {
		$('#equipmentTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			searching : true,
			ordering : true,
			columnDefs : [ {
				orderable : false,
				targets : 7
			} ]
		});
	});
</script>

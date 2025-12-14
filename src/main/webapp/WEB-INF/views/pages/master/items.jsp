<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- ================= DataTables CSS ================= -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<!-- ================= Page Header ================= -->
<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">Master Item Types</h2>

	<!-- Add -->
	<a href="${pageContext.request.contextPath}/master/items/add"
		class="btn btn-primary"> + Add Item Type </a>
</div>

<!-- ================= Items Table ================= -->
<table id="itemTypeTable"
	class="table table-striped table-hover shadow-sm w-100">
	<thead class="table-dark">
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Slot</th>
			<th>Category</th>
			<th style="width: 180px;">Action</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${items}">
			<tr>
				<td>${item.typeId}</td>
				<td>${item.typeName}</td>
				<td>${item.slot}</td>
				<td class="text-center">${item.categoryId}</td>
				<td class="text-center">
					<!-- Edit --> <a
					href="${pageContext.request.contextPath}/master/items/edit?id=${item.typeId}"
					class="btn btn-sm btn-warning me-1"> Edit </a> <!-- Delete -->
					<form
						action="${pageContext.request.contextPath}/master/items/delete"
						method="post" style="display: inline;"
						onsubmit="return confirm('Are you sure you want to delete this item type?');">

						<input type="hidden" name="id" value="${item.typeId}" />

						<button type="submit" class="btn btn-sm btn-danger">
							Delete</button>
					</form>

				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<!-- ================= JS ================= -->

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<!-- DataTables JS -->
<script
	src="https://cdn.datatables.net/1.13.8/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/dataTables.bootstrap5.min.js"></script>

<script>
	$(document).ready(function() {
		$('#itemTypeTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			ordering : true,
			searching : true,
			info : true,
			columnDefs : [ {
				orderable : false,
				targets : 4
			// Action column
			} ]
		});
	});
</script>

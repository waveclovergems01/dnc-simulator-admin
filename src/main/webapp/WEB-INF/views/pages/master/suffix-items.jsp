<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- ================= DataTables CSS ================= -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<!-- ================= Page Header ================= -->
<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">Master Suffix Items</h2>

	<div>
		<a href="${pageContext.request.contextPath}/master/suffix-items/add"
			class="btn btn-primary"> + Add Suffix Item </a>
	</div>
</div>

<!-- ================= TABLE ================= -->
<table id="suffixItemTable"
	class="table table-striped table-hover shadow-sm w-100">

	<thead class="table-dark">
		<tr>
			<th>ID</th>
			<th>Item</th>
			<th>Suffix Type</th>
			<th>Suffix Name</th>
			<th style="width: 220px;">Action</th>
		</tr>
	</thead>

	<tbody>
		<c:forEach items="${items}" var="s">
			<tr>
				<td>${s.id}</td>

				<td><strong> <c:out
							value="${equipmentItemMap[s.itemId]}" />
				</strong> <br /> <span class="text-muted">ID: ${s.itemId}</span></td>

				<td><strong> <c:out
							value="${suffixTypeMap[s.suffixTypeId]}" />
				</strong> <br /> <span class="text-muted">ID: ${s.suffixTypeId}</span></td>

				<td><strong> <c:out value="${s.name}" />
				</strong></td>

				<td class="text-center">
					<!-- Edit --> <a
					href="${pageContext.request.contextPath}/master/suffix-items/edit?itemId=${s.itemId}"
					class="btn btn-sm btn-warning me-1"> Edit </a> <!-- Delete -->
					<form
						action="${pageContext.request.contextPath}/master/suffix-items/delete"
						method="post" style="display: inline;"
						onsubmit="return confirm('Delete this suffix item?');">

						<input type="hidden" name="id" value="${s.id}" />
						<button type="submit" class="btn btn-sm btn-danger">
							Delete</button>
					</form>
				</td>
			</tr>
		</c:forEach>

		<c:if test="${empty items}">
			<tr>
				<td colspan="5" class="text-center text-muted">No suffix items
					found.</td>
			</tr>
		</c:if>
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

		$('#suffixItemTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			searching : true, // ใช้ search box ของ DataTables อย่างเดียว
			ordering : true,
			info : true,
			columnDefs : [ {
				orderable : false,
				targets : 4
			// Action column
			} ]
		});

	});
</script>

<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- ================= DataTables CSS ================= -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<!-- ================= Page Header ================= -->
<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">Master Set Bonus</h2>

	<div>
		<a href="${pageContext.request.contextPath}/master/set-bonus/add"
			class="btn btn-primary"> + Add Set Bonus </a>
	</div>
</div>

<!-- ================= Filters ================= -->
<div class="card p-3 mb-3 shadow-sm">
	<div class="row g-3">

		<!-- Set Filter (by Name, show ID) -->
		<div class="col-md-4">
			<label class="form-label">Filter by Set</label> <select
				id="setFilter" class="form-select">
				<option value="">-- All Sets --</option>
				<c:forEach items="${sets}" var="s">
					<option value="${s.setName}">${s.setName} (ID: ${s.setId})
					</option>
				</c:forEach>
			</select>
		</div>

		<!-- Reset -->
		<div class="col-md-4 d-flex align-items-end">
			<button type="button" id="resetFilter"
				class="btn btn-outline-secondary w-100">Reset Filters</button>
		</div>

	</div>
</div>

<!-- ================= Set Bonus Table ================= -->
<table id="setBonusTable"
	class="table table-striped table-hover shadow-sm w-100">

	<thead class="table-dark">
		<tr>
			<th style="width: 140px;">Set ID</th>
			<th>Set Name</th>
			<th style="width: 260px;">Action</th>
		</tr>
	</thead>

	<tbody>
		<c:forEach items="${sets}" var="s">
			<tr>

				<!-- Set ID -->
				<td class="text-center">${s.setId}</td>

				<!-- Set Name (ใช้ column นี้ filter) -->
				<td><strong>${s.setName}</strong> <br /> <small
					class="text-muted"> ID: ${s.setId} </small></td>

				<!-- Action -->
				<td class="text-center"><a
					href="${pageContext.request.contextPath}/master/set-bonus/edit?setId=${s.setId}"
					class="btn btn-sm btn-warning me-1"> Edit </a> <a
					href="${pageContext.request.contextPath}/master/set-bonus/clone?setId=${s.setId}"
					class="btn btn-sm btn-info me-1"> Clone </a>

					<form
						action="${pageContext.request.contextPath}/master/set-bonus/delete"
						method="post" style="display: inline;"
						onsubmit="return confirm('Delete this set bonus?');">
						<input type="hidden" name="setId" value="${s.setId}" />
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

		var table = $('#setBonusTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			searching : true,
			ordering : true,
			info : true,
			columnDefs : [ {
				orderable : false,
				targets : 2
			// Action column
			} ]
		});

		// ===== Set filter (by Set Name column = index 1) =====
		$('#setFilter').on('change', function() {
			table.column(1).search(this.value).draw();
		});

		// ===== Reset all filters =====
		$('#resetFilter').on('click', function() {
			$('#setFilter').val('');
			table.search('').columns().search('').draw();
		});
	});
</script>

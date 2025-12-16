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
			class="btn btn-primary"> + Add Suffix Item </a> <a
			href="${pageContext.request.contextPath}/master/suffix-items/clone-suffix"
			class="btn btn-outline-info"> 📦 Clone Suffix </a>
	</div>
</div>

<!-- ================= Filters ================= -->
<div class="card p-3 mb-3 shadow-sm">
	<div class="row g-3">

		<!-- Item Filter -->
		<div class="col-md-4">
			<label class="form-label">Filter by Item</label> <select
				id="itemFilter" class="form-select">
				<option value="">-- All Items --</option>
				<c:forEach items="${filterSuffixItems}" var="e">
					<option value="${equipmentItemMap[e.itemId]}">
						${equipmentItemMap[e.itemId]} (ID: ${e.itemId})</option>
				</c:forEach>
			</select>
		</div>

		<!-- Job Filter -->
		<div class="col-md-4">
			<label class="form-label">Filter by Job</label> <select
				id="jobFilter" class="form-select">
				<option value="">-- All Jobs --</option>
				<c:forEach items="${jobFilterMap}" var="j">
					<option value="${j.value}">${j.value}</option>
				</c:forEach>
			</select>
		</div>

		<!-- Suffix Type Filter -->
		<div class="col-md-4">
			<label class="form-label">Filter by Suffix Type</label> <select
				id="suffixTypeFilter" class="form-select">
				<option value="">-- All Types --</option>
				<c:forEach items="${suffixTypeMap}" var="t">
					<option value="${t.value}">${t.value}(ID:${t.key})</option>
				</c:forEach>
			</select>
		</div>

		<!-- Reset -->
		<div class="col-md-12 text-end">
			<button type="button" id="resetFilter"
				class="btn btn-outline-secondary">Reset Filters</button>
		</div>

	</div>
</div>

<!-- ================= TABLE ================= -->
<table id="suffixItemTable"
	class="table table-striped table-hover shadow-sm w-100">

	<thead class="table-dark">
		<tr>
			<th>ID</th>
			<th>Item</th>
			<th>Job</th>
			<th>Suffix Type</th>
			<th>Suffix Name</th>
			<th style="width: 220px;">Action</th>
		</tr>
	</thead>

	<tbody>
		<c:forEach items="${items}" var="s">
			<tr>
				<!-- ID -->
				<td>${s.id}</td>

				<!-- Item -->
				<td><strong> <c:out
							value="${equipmentItemMap[s.itemId]}" />
				</strong> <br /> <small class="text-muted">ID: ${s.itemId}</small></td>

				<!-- Job -->
				<td><strong> <c:out value="${itemJobMap[s.itemId]}" />
				</strong></td>

				<!-- Suffix Type -->
				<td><strong> <c:out
							value="${suffixTypeMap[s.suffixTypeId]}" />
				</strong> <br /> <small class="text-muted">ID: ${s.suffixTypeId}</small></td>

				<!-- Suffix Name -->
				<td><strong> <c:out value="${s.name}" />
				</strong></td>

				<!-- Action -->
				<td class="text-center"><a
					href="${pageContext.request.contextPath}/master/suffix-items/edit?itemId=${s.itemId}"
					class="btn btn-sm btn-warning me-1"> Edit </a>

					<form
						action="${pageContext.request.contextPath}/master/suffix-items/delete"
						method="post" style="display: inline"
						onsubmit="return confirm('Delete this suffix item?');">

						<input type="hidden" name="id" value="${s.id}" />
						<button type="submit" class="btn btn-sm btn-danger">
							Delete</button>
					</form></td>
			</tr>
		</c:forEach>

		<c:if test="${empty items}">
			<tr>
				<td colspan="6" class="text-center text-muted">No suffix items
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

		var table = $('#suffixItemTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			searching : true,
			ordering : true,
			info : true,
			columnDefs : [ {
				orderable : false,
				targets : 5
			} ]
		});

		// Item filter (col 1)
		$('#itemFilter').on('change', function() {
			table.column(1).search(this.value).draw();
		});

		// Job filter (col 2)
		$('#jobFilter').on('change', function() {
			table.column(2).search(this.value).draw();
		});

		// Suffix Type filter (col 3)
		$('#suffixTypeFilter').on('change', function() {
			table.column(3).search(this.value).draw();
		});

		// Reset
		$('#resetFilter').on('click', function() {
			$('#itemFilter').val('');
			$('#jobFilter').val('');
			$('#suffixTypeFilter').val('');
			table.search('').columns().search('').draw();
		});

	});
</script>

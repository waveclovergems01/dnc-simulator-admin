<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- ================= DataTables CSS ================= -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<!-- ================= Page Header ================= -->
<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">Master Equipment Items</h2>

	<div>
		<a href="${pageContext.request.contextPath}/master/equipment/add"
			class="btn btn-primary me-1"> + Add Equipment </a> <a
			href="${pageContext.request.contextPath}/master/equipment/clone-set"
			class="btn btn-outline-info"> 📦 Clone Set </a>
	</div>
</div>

<!-- ================= Filters ================= -->
<div class="card p-3 mb-3 shadow-sm">
	<div class="row g-3">

		<!-- Job Filter -->
		<div class="col-md-3">
			<label class="form-label">Filter by Job</label> <select
				id="jobFilter" class="form-select">
				<option value="">-- All Jobs --</option>
				<c:forEach items="${jobs}" var="j">
					<option value="${j.name}">${j.name}</option>
				</c:forEach>
			</select>
		</div>

		<!-- Rarity Filter -->
		<div class="col-md-3">
			<label class="form-label">Filter by Rarity</label> <select
				id="rarityFilter" class="form-select">
				<option value="">-- All Rarities --</option>
				<c:forEach items="${rarities}" var="r">
					<option value="${r.rarityName}">${r.rarityName}</option>
				</c:forEach>
			</select>
		</div>

		<!-- Set ID Filter (DISTINCT จาก controller) -->
		<div class="col-md-3">
			<label class="form-label">Filter by Set ID</label> <select
				id="setFilter" class="form-select">
				<option value="">-- All Sets --</option>
				<c:forEach items="${setIds}" var="sid">
					<option value="${sid}">${sid}</option>
				</c:forEach>
			</select>
		</div>

		<!-- Reset -->
		<div class="col-md-3 d-flex align-items-end">
			<button type="button" id="resetFilter"
				class="btn btn-outline-secondary w-100">Reset Filters</button>
		</div>

	</div>
</div>

<!-- ================= Equipment Table ================= -->
<table id="equipmentTable"
	class="table table-striped table-hover shadow-sm w-100">
	<thead class="table-dark">
		<tr>
			<th>Item ID</th>
			<th>Name</th>
			<th>Item Type</th>
			<th>Job</th>
			<th>Req Lv</th>
			<th>Rarity</th>
			<th>Durability</th>
			<th>Set ID</th>
			<th style="width: 260px;">Action</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${items}" var="e">
			<tr>
				<!-- Item ID -->
				<td>${e.itemId}</td>

				<!-- Name -->
				<td><strong>${e.name}</strong></td>

				<!-- Item Type -->
				<td><c:forEach items="${itemTypes}" var="t">
						<c:if test="${t.typeId == e.typeId}">
							<strong>${t.typeName}</strong>
							<c:if test="${not empty t.slot}">
								<span class="text-muted">(${t.slot})</span>
							</c:if>
							<br />
							<small class="text-muted"> ID: ${t.typeId} </small>
						</c:if>
					</c:forEach></td>

				<!-- Job -->
				<td><c:forEach items="${jobs}" var="j">
						<c:if test="${j.id == e.jobId}">
							${j.name}
							<br />
							<small class="text-muted"> ID: ${j.id} </small>
						</c:if>
					</c:forEach></td>

				<!-- Required Level -->
				<td class="text-center">${e.requiredLevel}</td>

				<!-- Rarity (สีจาก DB) -->
				<td><c:forEach items="${rarities}" var="r">
						<c:if test="${r.rarityId == e.rarityId}">
							<span style="color:${r.color}; font-weight:600;">
								${r.rarityName} </span>
							<br />
							<small class="text-muted"> ID: ${r.rarityId} </small>
						</c:if>
					</c:forEach></td>

				<!-- Durability -->
				<td class="text-center">${e.durability}</td>

				<!-- Set ID -->
				<td class="text-center"><c:choose>
						<c:when test="${e.setId != null}">
							${e.setId}
						</c:when>
						<c:otherwise>
							<span class="text-muted">-</span>
						</c:otherwise>
					</c:choose></td>

				<!-- Action -->
				<td class="text-center"><a
					href="${pageContext.request.contextPath}/master/equipment/edit?itemId=${e.itemId}"
					class="btn btn-sm btn-warning me-1"> Edit </a> <a
					href="${pageContext.request.contextPath}/master/equipment/clone?itemId=${e.itemId}"
					class="btn btn-sm btn-info me-1"> Clone </a>

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

		var table = $('#equipmentTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			searching : true,
			ordering : true,
			info : true,
			columnDefs : [ {
				orderable : false,
				targets : 8
			// Action column
			} ]
		});

		// Job filter (column 3)
		$('#jobFilter').on('change', function() {
			table.column(3).search(this.value).draw();
		});

		// Rarity filter (column 5)
		$('#rarityFilter').on('change', function() {
			table.column(5).search(this.value).draw();
		});

		// Set ID filter (column 7)
		$('#setFilter').on('change', function() {
			table.column(7).search(this.value).draw();
		});

		// Reset all filters
		$('#resetFilter').on('click', function() {
			$('#jobFilter').val('');
			$('#rarityFilter').val('');
			$('#setFilter').val('');
			table.search('').columns().search('').draw();
		});
	});
</script>

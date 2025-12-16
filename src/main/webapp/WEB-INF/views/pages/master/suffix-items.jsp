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

<!-- ================= Filters ================= -->
<div class="card p-3 mb-3 shadow-sm">
	<div class="row g-3">

		<div class="col-md-3">
			<label class="form-label">Filter by Item</label> <select
				id="itemFilter" class="form-select">
				<option value="">-- All Items --</option>
			</select>
		</div>

		<div class="col-md-3">
			<label class="form-label">Filter by Suffix Type</label> <select
				id="suffixTypeFilter" class="form-select">
				<option value="">-- All Types --</option>
			</select>
		</div>

		<div class="col-md-3">
			<label class="form-label">Filter by Suffix Name</label> <select
				id="nameFilter" class="form-select">
				<option value="">-- All Names --</option>
			</select>
		</div>

		<div class="col-md-3 d-flex align-items-end">
			<button type="button" id="resetFilter"
				class="btn btn-outline-secondary w-100">Reset Filters</button>
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

				<td><strong><c:out value="${s.name}" /></strong></td>

				<td class="text-center">
					<!-- ✅ Edit = Manage suffixes by ITEM --> <a
					href="${pageContext.request.contextPath}/master/suffix-items/edit?itemId=${s.itemId}"
					class="btn btn-sm btn-warning me-1"> Edit </a> <!-- Delete suffix (single record) -->
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

		const table = $('#suffixItemTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			searching : true,
			ordering : true,
			info : true,
			columnDefs : [ {
				orderable : false,
				targets : 4
			} ]
		});

		/* ===== Populate filter dropdowns (unique values only) ===== */
		function populateDropdown(columnIndex, dropdownSelector) {
			const uniqueValues = new Set();

			table.column(columnIndex).data().each(function(value) {
				const text = $('<div>').html(value).text().trim();
				if (text) {
					uniqueValues.add(text);
				}
			});

			const dropdown = $(dropdownSelector);
			uniqueValues.forEach(function(value) {
				dropdown.append($('<option>', {
					value : value,
					text : value
				}));
			});
		}

		// column index:
		// 1 = Item
		// 2 = Suffix Type
		// 3 = Suffix Name
		populateDropdown(1, '#itemFilter');
		populateDropdown(2, '#suffixTypeFilter');
		populateDropdown(3, '#nameFilter');

		/* ===== Filter events ===== */
		$('#itemFilter').on('change', function() {
			table.column(1).search(this.value).draw();
		});

		$('#suffixTypeFilter').on('change', function() {
			table.column(2).search(this.value).draw();
		});

		$('#nameFilter').on('change', function() {
			table.column(3).search(this.value).draw();
		});

		$('#resetFilter').on('click', function() {
			$('#itemFilter').val('');
			$('#suffixTypeFilter').val('');
			$('#nameFilter').val('');
			table.search('').columns().search('').draw();
		});
	});
</script>

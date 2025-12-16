<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- ================= HEADER + BACK ================= -->
<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">Manage Suffixes</h2>

	<!-- BACK BUTTON (ADD ONLY) -->
	<button type="button" class="btn btn-secondary" onclick="goBack()">
		← Back</button>
</div>

<div class="card p-4 shadow-sm">

	<!-- ================= ITEM SELECT ================= -->
	<div class="row mb-4">
		<div class="col-md-6">
			<label class="form-label">Item</label> <select id="itemId"
				class="form-select">
				<option value="">-- Select Item --</option>
				<c:forEach items="${equipmentItems}" var="e">
					<option value="${e.itemId}" data-name="<c:out value='${e.name}'/>"
						<c:if test="${e.itemId == selectedItemId}">selected</c:if>>
						<c:out value="${e.name}" /> (ID: ${e.itemId})
					</option>
				</c:forEach>
			</select>
		</div>
	</div>

	<h5 class="mb-3">Suffixes</h5>

	<table class="table table-bordered">
		<thead class="table-dark">
			<tr>
				<th style="width: 80px;">ID</th>
				<th>Suffix Type</th>
				<th>Name</th>
				<th style="width: 320px;">Action</th>
			</tr>
		</thead>

		<tbody id="suffixTableBody">

			<c:forEach items="${existingSuffixes}" var="s">
				<tr class="suffix-row" data-suffix-id="${s.id}">
					<td>${s.id}</td>

					<td><select class="form-select suffix-type-select">
							<option value="">-- Select Suffix Type --</option>
							<c:forEach items="${suffixTypes}" var="t">
								<option value="${t.suffixId}"
									<c:if test="${t.suffixId == s.suffixTypeId}">selected</c:if>>
									<c:out value="${t.suffixName}" />
								</option>
							</c:forEach>
					</select></td>

					<td class="suffix-name"><c:out value="${s.name}" /></td>

					<td class="text-center">
						<button type="button" class="btn btn-sm btn-success saveSuffixBtn">
							Save</button>

						<button type="button"
							class="btn btn-sm btn-info openStatsBtn ms-1">Stats</button>

						<button type="button"
							class="btn btn-sm btn-danger deleteSuffixBtn ms-1">
							Delete</button>
					</td>
				</tr>
			</c:forEach>

		</tbody>
	</table>

	<button type="button" id="addSuffixBtn"
		class="btn btn-primary mt-3 w-100">+ Add Suffix</button>

</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
/* ================= BACK (ADD ONLY) ================= */
function goBack() {
	location.href = '${pageContext.request.contextPath}/master/suffix-items';
}

/* ================= CHANGE ITEM ================= */
$('#itemId').on('change', function () {
	const itemId = $(this).val();
	if (!itemId) return;

	location.href =
		'${pageContext.request.contextPath}/master/suffix-items/edit?itemId='
		+ itemId;
});

/* ================= ADD SUFFIX ================= */
$('#addSuffixBtn').on('click', function () {

	const itemId = $('#itemId').val();
	if (!itemId) {
		alert('Please select item first');
		return;
	}

	let options = '<option value="">-- Select Suffix Type --</option>';
	<c:forEach items="${suffixTypes}" var="t">
		options += '<option value="${t.suffixId}">${t.suffixName}</option>';
	</c:forEach>

	const row =
	'<tr class="suffix-row new-suffix">' +
		'<td>NEW</td>' +
		'<td>' +
			'<select class="form-select suffix-type-select">' +
				options +
			'</select>' +
		'</td>' +
		'<td class="suffix-name text-muted">-</td>' +
		'<td class="text-center">' +
			'<button class="btn btn-sm btn-success saveSuffixBtn">Save</button> ' +
			'<button class="btn btn-sm btn-secondary cancelSuffixBtn">Cancel</button>' +
		'</td>' +
	'</tr>';

	$('#suffixTableBody').append(row);
});

/* ================= AUTO NAME ================= */
$(document).on('change', '.suffix-type-select', function () {

	const suffixTypeName =
		$(this).find('option:selected').text();
	const itemName =
		$('#itemId option:selected').data('name');

	if (suffixTypeName && itemName) {
		$(this).closest('tr')
			.find('.suffix-name')
			.text(itemName + ' (' + suffixTypeName + ')');
	}
});

/* ================= CANCEL NEW ================= */
$(document).on('click', '.cancelSuffixBtn', function () {
	$(this).closest('tr').remove();
});

/* ================= SAVE SUFFIX ================= */
$(document).on('click', '.saveSuffixBtn', function () {

	const row = $(this).closest('tr');
	const suffixId = row.data('suffix-id') || '';
	const suffixTypeId = row.find('.suffix-type-select').val();
	const name = row.find('.suffix-name').text();
	const itemId = $('#itemId').val();

	if (!suffixTypeId) {
		alert('Please select suffix type');
		return;
	}

	$.post(
		'${pageContext.request.contextPath}/master/suffix-items/save',
		{
			id: suffixId,
			itemId: itemId,
			suffixTypeId: suffixTypeId,
			name: name
		},
		function (res) {

			if (!res.success) {
				alert(res.message || 'Save failed');
				return;
			}

			/* ===== NEW → EXISTING ===== */
			if (!suffixId && res.id) {

				row.attr('data-suffix-id', res.id);
				row.find('td:first').text(res.id);
				row.removeClass('new-suffix');

				row.find('td:last').html(
					'<button class="btn btn-sm btn-success saveSuffixBtn">Save</button> ' +
					'<button class="btn btn-sm btn-info openStatsBtn ms-1">Stats</button> ' +
					'<button class="btn btn-sm btn-danger deleteSuffixBtn ms-1">Delete</button>'
				);
			}

			row.addClass('table-success');
			setTimeout(() => row.removeClass('table-success'), 800);
		}
	).fail(function () {
		alert('Save error');
	});
});

/* ================= DELETE SUFFIX ================= */
$(document).on('click', '.deleteSuffixBtn', function () {

	if (!confirm('Delete this suffix?')) return;

	const row = $(this).closest('tr');
	const suffixId = row.data('suffix-id');

	$.post(
		'${pageContext.request.contextPath}/master/suffix-items/delete',
		{ id: suffixId },
		function () {
			row.remove();
		}
	).fail(function () {
		alert('Delete failed');
	});
});

/* ================= OPEN STATS PAGE ================= */
$(document).on('click', '.openStatsBtn', function () {

	const suffixItemId =
		$(this).closest('tr').data('suffix-id');

	if (!suffixItemId) {
		alert('Please save suffix first');
		return;
	}

	location.href =
		'${pageContext.request.contextPath}/master/suffix-items/stats'
		+ '?suffixItemId=' + suffixItemId;
});
</script>

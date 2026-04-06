<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">Manage Suffixes</h2>
	<button type="button" class="btn btn-secondary" onclick="goBack()">←
		Back</button>
</div>

<div class="card p-4 shadow-sm">

	<div class="row mb-3">
		<div class="col-md-4">
			<label class="form-label">Filter by Job</label>
			<select id="jobFilter" class="form-select">
				<option value="">-- All Jobs --</option>
				<c:forEach items="${jobFilterMap}" var="entry">
					<option value="${entry.key}"
						<c:if test="${selectedJobFilter != null and selectedJobFilter == entry.key}">selected</c:if>>
						${entry.value}
					</option>
				</c:forEach>
			</select>
		</div>

		<div class="col-md-4">
			<label class="form-label">Filter by Level</label>
			<select id="levelFilter" class="form-select">
				<option value="">-- All Levels --</option>
				<c:forEach items="${levelFilterList}" var="lv">
					<option value="${lv}"
						<c:if test="${selectedLevelFilter != null and selectedLevelFilter == lv}">selected</c:if>>
						${lv}
					</option>
				</c:forEach>
			</select>
		</div>

		<div class="col-md-4">
			<label class="form-label">Filter by Rarity</label>
			<select id="rarityFilter" class="form-select">
				<option value="">-- All Rarities --</option>
				<c:forEach items="${rarityFilterMap}" var="entry">
					<option value="${entry.key}"
						<c:if test="${selectedRarityFilter != null and selectedRarityFilter == entry.key}">selected</c:if>>
						${entry.value}
					</option>
				</c:forEach>
			</select>
		</div>
	</div>

	<div class="row mb-4">
		<div class="col-md-12 d-flex justify-content-end">
			<button type="button" class="btn btn-outline-secondary" id="resetItemFilterBtn">
				Reset Filters
			</button>
		</div>
	</div>

	<div class="row mb-2">
		<div class="col-md-8">
			<label class="form-label">Item</label>
			<select id="itemId" class="form-select">
				<option value="">-- Select Item --</option>
				<c:forEach items="${equipmentItems}" var="e">
					<option
						value="${e.itemId}"
						data-name="<c:out value='${e.name}'/>"
						data-job-id="${e.jobId}"
						data-level="${e.requiredLevel}"
						data-rarity-id="${e.rarityId}"
						<c:if test="${e.itemId == selectedItemId}">selected</c:if>>
						<c:out value="${e.name}" /> (ID: ${e.itemId})
					</option>
				</c:forEach>
			</select>
			<div class="form-text" id="itemFilterSummary"></div>
		</div>
	</div>

	<c:forEach var="tier" items="${tiers}">

		<h5 class="mt-4 mb-2 d-flex align-items-center gap-2">
			Suffix Tier ${tier}
			<span class="badge bg-secondary">
				<c:choose>
					<c:when test="${tier == 1}">Suffix I</c:when>
					<c:when test="${tier == 2}">Suffix II</c:when>
					<c:when test="${tier == 3}">Suffix III</c:when>
					<c:otherwise>Tier ${tier}</c:otherwise>
				</c:choose>
			</span>

			<c:if test="${tier > 1}">
				<button type="button"
					class="btn btn-sm btn-outline-primary cloneTier1Btn"
					data-target-tier="${tier}">Clone Suffix I</button>

				<button type="button"
					class="btn btn-sm btn-outline-danger cloneTier1WithStatsBtn"
					data-target-tier="${tier}">Clone Suffix I with Stats</button>
			</c:if>
		</h5>

		<table class="table table-bordered">
			<thead class="table-dark">
				<tr>
					<th style="width: 80px;">ID</th>
					<th>Suffix Type</th>
					<th>Name</th>
					<th style="width: 320px;">Action</th>
				</tr>
			</thead>

			<tbody id="suffixTableBody-tier-${tier}">
				<c:forEach items="${suffixByTier[tier]}" var="s">
					<tr class="suffix-row" data-suffix-id="${s.id}" data-tier="${tier}">

						<td>${s.id}</td>

						<td>
							<select class="form-select suffix-type-select">
								<option value="">-- Select Suffix Type --</option>
								<c:forEach items="${suffixTypes}" var="t">
									<option value="${t.suffixId}"
										<c:if test="${t.suffixId == s.suffixTypeId}">selected</c:if>>
										<c:out value="${t.suffixName}" />
									</option>
								</c:forEach>
							</select>
						</td>

						<td class="suffix-name"><c:out value="${s.name}" /></td>

						<td class="text-center">
							<button class="btn btn-sm btn-success saveSuffixBtn">Save</button>
							<button class="btn btn-sm btn-info openStatsBtn ms-1">Stats</button>
							<button class="btn btn-sm btn-danger deleteSuffixBtn ms-1">Delete</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<button type="button" class="btn btn-primary w-100 addSuffixBtn"
			data-tier="${tier}">+ Add Suffix (Tier ${tier})</button>

	</c:forEach>

</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
function tierToRoman(tier) {
	const map = { 2: 'II', 3: 'III', 4: 'IV', 5: 'V' };
	return map[tier] || '';
}

function buildSuffixName(itemName, suffixTypeName, tier) {
	let roman = tierToRoman(tier);
	let name = itemName + ' (' + suffixTypeName;
	if (roman) {
		name += ' ' + roman;
	}
	return name + ')';
}

function goBack() {
	location.href = '${pageContext.request.contextPath}/master/suffix-items';
}

function getCurrentFilterQueryString() {
	const jobFilter = ($('#jobFilter').val() || '').trim();
	const levelFilter = ($('#levelFilter').val() || '').trim();
	const rarityFilter = ($('#rarityFilter').val() || '').trim();

	const params = [];

	if (jobFilter !== '') {
		params.push('jobFilter=' + encodeURIComponent(jobFilter));
	}
	if (levelFilter !== '') {
		params.push('levelFilter=' + encodeURIComponent(levelFilter));
	}
	if (rarityFilter !== '') {
		params.push('rarityFilter=' + encodeURIComponent(rarityFilter));
	}

	return params.join('&');
}

function applyItemFilters() {

	const selectedJobId = ($('#jobFilter').val() || '').trim();
	const selectedLevel = ($('#levelFilter').val() || '').trim();
	const selectedRarityId = ($('#rarityFilter').val() || '').trim();

	let visibleCount = 0;
	let selectedStillVisible = false;
	const selectedItemId = ($('#itemId').val() || '').trim();

	$('#itemId option').each(function(index) {

		if (index === 0) {
			$(this).prop('hidden', false);
			$(this).prop('disabled', false);
			return;
		}

		const $option = $(this);
		const jobId = ($option.attr('data-job-id') || '').trim();
		const level = ($option.attr('data-level') || '').trim();
		const rarityId = ($option.attr('data-rarity-id') || '').trim();

		let matched = true;

		if (selectedJobId !== '' && jobId !== selectedJobId) {
			matched = false;
		}
		if (selectedLevel !== '' && level !== selectedLevel) {
			matched = false;
		}
		if (selectedRarityId !== '' && rarityId !== selectedRarityId) {
			matched = false;
		}

		$option.prop('hidden', !matched);
		$option.prop('disabled', !matched);

		if (matched) {
			visibleCount++;
			if (($option.val() || '').trim() === selectedItemId) {
				selectedStillVisible = true;
			}
		}
	});

	if (selectedItemId !== '' && !selectedStillVisible) {
		$('#itemId').val('');
	}

	if (visibleCount > 0) {
		$('#itemFilterSummary').text('Matched equipment items: ' + visibleCount);
	} else {
		$('#itemFilterSummary').text('No equipment item matched the selected filters');
	}
}

$('#itemId').on('change', function () {
	const itemId = ($(this).val() || '').trim();
	if (!itemId) {
		return;
	}

	const qs = getCurrentFilterQueryString();
	let url = '${pageContext.request.contextPath}/master/suffix-items/edit?itemId=' + encodeURIComponent(itemId);

	if (qs !== '') {
		url += '&' + qs;
	}

	location.href = url;
});

$('#jobFilter, #levelFilter, #rarityFilter').on('change', function () {
	applyItemFilters();
});

$('#resetItemFilterBtn').on('click', function() {
	$('#jobFilter').val('');
	$('#levelFilter').val('');
	$('#rarityFilter').val('');
	applyItemFilters();
});

$(document).on('click', '.addSuffixBtn', function () {

	const tier = $(this).data('tier');
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
		'<tr class="suffix-row new-suffix" data-tier="' + tier + '">' +
			'<td>NEW</td>' +
			'<td><select class="form-select suffix-type-select">' + options + '</select></td>' +
			'<td class="suffix-name text-muted">-</td>' +
			'<td class="text-center">' +
				'<button class="btn btn-sm btn-success saveSuffixBtn">Save</button> ' +
				'<button class="btn btn-sm btn-secondary cancelSuffixBtn">Cancel</button>' +
			'</td>' +
		'</tr>';

	$('#suffixTableBody-tier-' + tier).append(row);
});

$(document).on('click', '.cloneTier1Btn', function () {

	const targetTier = $(this).data('target-tier');
	const itemName = $('#itemId option:selected').data('name');

	const $tier1Rows = $('#suffixTableBody-tier-1')
		.find('tr.suffix-row')
		.not('.new-suffix');

	if ($tier1Rows.length === 0) {
		alert('No suffix in Tier 1 to clone');
		return;
	}

	$tier1Rows.each(function () {

		const $row = $(this).clone();
		const suffixTypeName =
			$row.find('.suffix-type-select option:selected').text();

		$row.removeAttr('data-suffix-id')
			.attr('data-tier', targetTier)
			.addClass('new-suffix');

		$row.find('td:first').text('NEW');
		$row.find('.suffix-name')
			.text(buildSuffixName(itemName, suffixTypeName, targetTier));

		$row.find('td:last').html(
			'<button class="btn btn-sm btn-success saveSuffixBtn">Save</button> ' +
			'<button class="btn btn-sm btn-secondary cancelSuffixBtn">Cancel</button>'
		);

		$('#suffixTableBody-tier-' + targetTier).append($row);
	});
});

$(document).on('click', '.cloneTier1WithStatsBtn', function () {

	const itemId = $('#itemId').val();
	const targetTier = $(this).data('target-tier');

	if (!itemId) {
		alert('Please select item first');
		return;
	}

	if (!confirm(
		'This action will:\n' +
		'- Clone ALL Suffix Tier I\n' +
		'- Clone stats & abilities\n' +
		'- SAVE directly to database\n\n' +
		'Continue?'
	)) {
		return;
	}

	location.href =
		'${pageContext.request.contextPath}/master/suffix-items/clone-tier1-with-stats'
		+ '?itemId=' + itemId
		+ '&targetTier=' + targetTier;
});

$(document).on('change', '.suffix-type-select', function () {

	const $row = $(this).closest('tr');
	const suffixTypeName = $(this).find('option:selected').text().trim();
	const itemName = $('#itemId option:selected').data('name');
	const tier = $row.data('tier');

	if (!suffixTypeName || !itemName) {
		return;
	}

	$row.find('.suffix-name')
		.text(buildSuffixName(itemName, suffixTypeName, tier));
});

$(document).on('click', '.cancelSuffixBtn', function () {
	$(this).closest('tr').remove();
});

$(document).on('click', '.saveSuffixBtn', function () {

	const row = $(this).closest('tr');
	const suffixId = row.data('suffix-id') || '';
	const suffixTypeId = row.find('.suffix-type-select').val();
	const name = row.find('.suffix-name').text();
	const tier = row.data('tier');
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
			tier: tier,
			name: name
		},
		function (res) {

			if (!res.success) {
				alert(res.message || 'Save failed');
				return;
			}

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
	).fail(() => alert('Save error'));
});

$(document).on('click', '.deleteSuffixBtn', function () {

	if (!confirm('Delete this suffix?')) {
		return;
	}

	const row = $(this).closest('tr');
	const suffixId = row.data('suffix-id');

	$.post(
		'${pageContext.request.contextPath}/master/suffix-items/delete',
		{ id: suffixId },
		function () {
			row.remove();
		}
	).fail(() => alert('Delete failed'));
});

$(document).on('click', '.openStatsBtn', function () {

	const suffixItemId = $(this).closest('tr').data('suffix-id');
	if (!suffixItemId) {
		alert('Please save suffix first');
		return;
	}

	location.href =
		'${pageContext.request.contextPath}/master/suffix-items/stats'
		+ '?suffixItemId=' + suffixItemId;
});

$(function() {
	applyItemFilters();
});
</script>
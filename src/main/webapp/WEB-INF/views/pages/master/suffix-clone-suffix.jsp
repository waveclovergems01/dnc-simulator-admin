<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2 class="mb-4">🧩 Clone Equipment Suffix</h2>

<!-- =========================================================
     STEP 1 : SELECT ORIGINAL EQUIPMENT (HAS SUFFIX)
     ========================================================= -->
<div class="card p-3 mb-4 shadow-sm border-primary">
	<div class="fw-bold text-primary mb-2">STEP 1: Select Original
		Equipment (Has Suffix)</div>

	<form method="get"
		action="${pageContext.request.contextPath}/master/suffix-items/clone-suffix">

		<div class="row g-3 mb-3">
			<div class="col-md-4">
				<label class="form-label">Filter by Job</label>
				<select id="sourceJobFilter" name="sourceJobFilter" class="form-select">
					<option value="">-- All Jobs --</option>
					<c:forEach items="${sourceJobFilterMap}" var="entry">
						<option value="${entry.key}"
							<c:if test="${selectedSourceJobFilter != null and selectedSourceJobFilter == entry.key}">selected</c:if>>
							${entry.value}
						</option>
					</c:forEach>
				</select>
			</div>

			<div class="col-md-4">
				<label class="form-label">Filter by Level</label>
				<select id="sourceLevelFilter" name="sourceLevelFilter" class="form-select">
					<option value="">-- All Levels --</option>
					<c:forEach items="${sourceLevelFilterList}" var="lv">
						<option value="${lv}"
							<c:if test="${selectedSourceLevelFilter != null and selectedSourceLevelFilter == lv}">selected</c:if>>
							${lv}
						</option>
					</c:forEach>
				</select>
			</div>

			<div class="col-md-4">
				<label class="form-label">Filter by Rarity</label>
				<select id="sourceRarityFilter" name="sourceRarityFilter" class="form-select">
					<option value="">-- All Rarities --</option>
					<c:forEach items="${sourceRarityFilterMap}" var="entry">
						<option value="${entry.key}"
							<c:if test="${selectedSourceRarityFilter != null and selectedSourceRarityFilter == entry.key}">selected</c:if>>
							${entry.value}
						</option>
					</c:forEach>
				</select>
			</div>
		</div>

		<div class="row align-items-end">
			<div class="col-md-7">
				<label class="form-label">Original Equipment</label>
				<select id="sourceEquipmentSelect"
					name="equipmentItemId" class="form-select" required>
					<option value="">-- Select Equipment With Suffix --</option>
					<c:forEach items="${sourceEquipments}" var="e">
						<option value="${e.itemId}"
							data-job-id="${e.jobId}"
							data-level="${e.requiredLevel}"
							data-rarity-id="${e.rarityId}"
							<c:if test="${equipmentItem != null && e.itemId == equipmentItem.itemId}">selected</c:if>>
							${e.name} (ID: ${e.itemId})
						</option>
					</c:forEach>
				</select>
				<div class="form-text" id="sourceFilterSummary"></div>
			</div>

			<div class="col-md-2">
				<button type="submit" class="btn btn-primary w-100">Load
					Suffix</button>
			</div>

			<div class="col-md-1">
				<button type="button" id="resetSourceFilter"
					class="btn btn-outline-secondary w-100">Reset</button>
			</div>

			<div class="col-md-2 text-end">
				<a href="${pageContext.request.contextPath}/master/suffix-items"
					class="btn btn-outline-secondary w-100">Back</a>
			</div>
		</div>
	</form>
</div>

<!-- =========================================================
     STEP 2 + 3 : REVIEW SUFFIX & SELECT TARGET
     ========================================================= -->
<c:if test="${not empty suffixes}">
	<form method="post"
		action="${pageContext.request.contextPath}/master/suffix-items/clone-suffix/save">

		<input type="hidden" name="originalEquipmentItemId"
			value="${equipmentItem.itemId}" />

		<!-- ================= STEP 2 : REVIEW SUFFIX ================= -->
		<div class="card shadow-sm mb-4">
			<div class="card-header fw-bold">STEP 2: Review Suffix Items</div>

			<div class="card-body p-0">
				<table class="table table-striped mb-0" id="cloneTable">
					<thead class="table-dark">
						<tr>
							<th style="width: 120px">Suffix ID</th>
							<th>Name</th>
							<th style="width: 120px">Item ID</th>
							<th style="width: 120px">Suffix Type</th>
							<th style="width: 120px">Action</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${suffixes}" var="s">
							<tr>
								<td>
									${s.id}
									<input type="hidden" name="suffixItemId" value="${s.id}" />
								</td>

								<td>
									<input type="text" class="form-control suffixName"
										value="${s.name}" readonly />
								</td>

								<td>${s.itemId}</td>
								<td>${s.suffixTypeId}</td>

								<td class="text-center">
									<button type="button"
										class="btn btn-sm btn-outline-danger btn-delete-row">
										Remove
									</button>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>

		<!-- ================= STEP 3 : TARGET EQUIPMENT ================= -->
		<div class="card shadow-sm border-success">
			<div class="card-header fw-bold text-success">STEP 3: Select
				Target Equipment (No Suffix)</div>

			<div class="card-body">

				<div class="row g-3 mb-3">
					<div class="col-md-4">
						<label class="form-label">Filter by Job</label>
						<select id="targetJobFilter" class="form-select">
							<option value="">-- All Jobs --</option>
							<c:forEach items="${targetJobFilterMap}" var="entry">
								<option value="${entry.key}">
									${entry.value}
								</option>
							</c:forEach>
						</select>
					</div>

					<div class="col-md-4">
						<label class="form-label">Filter by Level</label>
						<select id="targetLevelFilter" class="form-select">
							<option value="">-- All Levels --</option>
							<c:forEach items="${targetLevelFilterList}" var="lv">
								<option value="${lv}">
									${lv}
								</option>
							</c:forEach>
						</select>
					</div>

					<div class="col-md-4">
						<label class="form-label">Filter by Rarity</label>
						<select id="targetRarityFilter" class="form-select">
							<option value="">-- All Rarities --</option>
							<c:forEach items="${targetRarityFilterMap}" var="entry">
								<option value="${entry.key}">
									${entry.value}
								</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="row align-items-end">
					<div class="col-md-7">
						<label class="form-label">Target Equipment</label>
						<select id="targetEquipmentSelect"
							name="newEquipmentItemId" class="form-select" required>
							<option value="">-- Select Equipment Without Suffix --</option>
							<c:forEach items="${targetEquipments}" var="t">
								<option value="${t.itemId}"
									data-job-id="${t.jobId}"
									data-level="${t.requiredLevel}"
									data-rarity-id="${t.rarityId}">
									${t.name} (ID: ${t.itemId})
								</option>
							</c:forEach>
						</select>
						<div class="form-text" id="targetFilterSummary"></div>
					</div>

					<div class="col-md-2">
						<button type="submit" class="btn btn-success w-100"
							onclick="return confirm('Clone all suffix items to selected equipment?');">
							Clone Suffix
						</button>
					</div>

					<div class="col-md-2">
						<button type="button" id="resetTargetFilter"
							class="btn btn-outline-secondary w-100">Reset</button>
					</div>
				</div>

				<div class="form-text mt-2">
					⚠️ All selected suffix items will be duplicated to the target equipment.
				</div>
			</div>
		</div>

	</form>
</c:if>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
	function applyEquipmentFilter(jobSelector, levelSelector, raritySelector, itemSelector, summarySelector) {

		var selectedJobId = ($(jobSelector).val() || '').trim();
		var selectedLevel = ($(levelSelector).val() || '').trim();
		var selectedRarityId = ($(raritySelector).val() || '').trim();

		var visibleCount = 0;
		var selectedItemId = ($(itemSelector).val() || '').trim();
		var selectedStillVisible = false;

		$(itemSelector + ' option').each(function(index) {

			if (index === 0) {
				$(this).prop('hidden', false);
				$(this).prop('disabled', false);
				return;
			}

			var $option = $(this);
			var jobId = ($option.attr('data-job-id') || '').trim();
			var level = ($option.attr('data-level') || '').trim();
			var rarityId = ($option.attr('data-rarity-id') || '').trim();

			var matched = true;

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
			$(itemSelector).val('');
		}

		if (visibleCount > 0) {
			$(summarySelector).text('Matched equipment items: ' + visibleCount);
		} else {
			$(summarySelector).text('No equipment item matched the selected filters');
		}
	}

	function applySourceFilter() {
		applyEquipmentFilter(
			'#sourceJobFilter',
			'#sourceLevelFilter',
			'#sourceRarityFilter',
			'#sourceEquipmentSelect',
			'#sourceFilterSummary'
		);
	}

	function applyTargetFilter() {
		applyEquipmentFilter(
			'#targetJobFilter',
			'#targetLevelFilter',
			'#targetRarityFilter',
			'#targetEquipmentSelect',
			'#targetFilterSummary'
		);
	}

	$('#sourceJobFilter, #sourceLevelFilter, #sourceRarityFilter').on('change', function() {
		applySourceFilter();
	});

	$('#targetJobFilter, #targetLevelFilter, #targetRarityFilter').on('change', function() {
		applyTargetFilter();
	});

	$('#resetSourceFilter').on('click', function() {
		$('#sourceJobFilter').val('');
		$('#sourceLevelFilter').val('');
		$('#sourceRarityFilter').val('');
		applySourceFilter();
	});

	$('#resetTargetFilter').on('click', function() {
		$('#targetJobFilter').val('');
		$('#targetLevelFilter').val('');
		$('#targetRarityFilter').val('');
		applyTargetFilter();
	});

	/* ================= REMOVE SUFFIX ROW ================= */
	$(document).on('click', '.btn-delete-row', function() {
		if (!confirm('Remove this suffix from clone list?')) {
			return;
		}
		$(this).closest('tr').remove();
	});

	$(function() {
		applySourceFilter();
		applyTargetFilter();
	});
</script>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2 class="mb-3">📦 Clone Equipment Set</h2>

<!-- ================= SELECT SET ================= -->
<div class="card p-3 mb-4 shadow-sm">
	<form method="get"
		action="${pageContext.request.contextPath}/master/equipment/clone-set">

		<div class="row align-items-end">
			<div class="col-md-4">
				<label class="form-label">Select Original Set</label> <select
					name="setId" class="form-select" required>
					<option value="">-- Select Set --</option>
					<c:forEach items="${setBonuses}" var="s">
						<option value="${s.setId}" ${s.setId == setId ? 'selected' : ''}>
							${s.setName} (ID: ${s.setId})</option>
					</c:forEach>
				</select>
			</div>

			<div class="col-md-2">
				<button class="btn btn-primary w-100">Load Set</button>
			</div>

			<div class="col-md-6 text-end">
				<a href="${pageContext.request.contextPath}/master/equipment"
					class="btn btn-secondary">Back</a>
			</div>
		</div>
	</form>
</div>

<c:if test="${not empty originals}">
	<form method="post"
		action="${pageContext.request.contextPath}/master/equipment/clone-set/save">

		<!-- ================= GLOBAL SETTINGS ================= -->
		<div class="card p-3 mb-3 shadow-sm">
			<div class="row g-3">

				<div class="col-md-3">
					<label class="form-label">New Set ID</label> <input type="number"
						id="globalSetId" name="newSetId" class="form-control" required />
				</div>

				<div class="col-md-3">
					<label class="form-label">Item Type (Global)</label> <select
						id="globalType" name="globalType" class="form-select">
						<option value="">-- Same as original --</option>
						<c:forEach items="${itemTypes}" var="t">
							<option value="${t.typeId}">${t.typeName}
								(ID:${t.typeId})</option>
						</c:forEach>
					</select>
				</div>

				<div class="col-md-3">
					<label class="form-label">Job (Global)</label> <select
						id="globalJob" name="globalJob" class="form-select">
						<option value="">-- Same as original --</option>
						<c:forEach items="${jobs}" var="j">
							<option value="${j.id}">${j.name}</option>
						</c:forEach>
					</select>
				</div>

				<div class="col-md-3">
					<label class="form-label">Rarity (Global)</label> <select
						id="globalRarity" name="globalRarity" class="form-select">
						<option value="">-- Same as original --</option>
						<c:forEach items="${rarities}" var="r">
							<option value="${r.rarityId}" data-color="${r.color}">
								${r.rarityName}</option>
						</c:forEach>
					</select>
				</div>

				<div class="col-md-3">
					<label class="form-label">Req Lv (Global)</label> <input
						type="number" id="globalReqLv" name="globalReqLv"
						class="form-control" />
				</div>

				<div class="col-md-6">
					<label class="form-label">Name Prefix (Global)</label> <input
						type="text" id="globalNamePrefix" class="form-control"
						placeholder="My New Item" />
				</div>

			</div>
		</div>

		<!-- ================= ITEMS ================= -->
		<div class="card shadow-sm">
			<div
				class="card-header d-flex justify-content-between align-items-center">
				<strong>Items</strong>
				<button type="button" class="btn btn-sm btn-outline-primary"
					id="btnAddRow">+ Add Item</button>
			</div>

			<div class="card-body p-0">
				<table class="table table-striped mb-0" id="cloneTable">
					<thead class="table-dark">
						<tr>
							<th>Original</th>
							<th>New Item ID</th>
							<th>Name</th>
							<th>Type</th>
							<th>Job</th>
							<th>Rarity</th>
							<th>Req Lv</th>
							<th style="width: 120px">Action</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${originals}" var="e">
							<tr>
								<td>${e.itemId} <input type="hidden" name="originalItemId"
									value="${e.itemId}" />
								</td>

								<td><input type="number" class="form-control newItemId"
									name="newItemId" required /></td>

								<td><input type="text" class="form-control itemName"
									name="name_${e.itemId}" value="${e.name}"
									data-original="${e.name}" /></td>

								<td><select class="form-select typeSelect"
									name="typeId_${e.itemId}">
										<c:forEach items="${itemTypes}" var="t">
											<option value="${t.typeId}"
												${t.typeId == e.typeId ? 'selected' : ''}>
												${t.typeName}</option>
										</c:forEach>
								</select></td>

								<td><select class="form-select jobSelect"
									name="jobId_${e.itemId}">
										<c:forEach items="${jobs}" var="j">
											<option value="${j.id}" ${j.id == e.jobId ? 'selected' : ''}>
												${j.name}</option>
										</c:forEach>
								</select></td>

								<td><select class="form-select raritySelect"
									name="rarityId_${e.itemId}">
										<c:forEach items="${rarities}" var="r">
											<option value="${r.rarityId}" data-color="${r.color}"
												${r.rarityId == e.rarityId ? 'selected' : ''}>
												${r.rarityName}</option>
										</c:forEach>
								</select></td>

								<td><input type="number" class="form-control reqLvInput"
									name="requiredLevel_${e.itemId}" value="${e.requiredLevel}" />
								</td>

								<td class="text-center">
									<button type="button"
										class="btn btn-sm btn-danger btn-delete-row">Delete</button>
								</td>
							</tr>
						</c:forEach>

					</tbody>
				</table>
			</div>

			<div class="card-footer text-end">
				<button class="btn btn-success"
					onclick="return confirm('Clone all items in this set?');">
					Clone Set</button>
			</div>
		</div>
	</form>
</c:if>

<!-- ================= ROW TEMPLATE (NEW ITEM) ================= -->
<script type="text/template" id="rowTemplate">
<tr>
	<td>
		NEW
		<input type="hidden" name="originalItemId" value="0"/>
	</td>
	<td>
		<input type="number" class="form-control newItemId" name="newItemId" required/>
	</td>
	<td>
		<input type="text" class="form-control itemName" name="name_0"/>
	</td>
	<td>
		<select class="form-select typeSelect" name="typeId_0" required>
			<option value="">-- Select Type --</option>
			<c:forEach items="${itemTypes}" var="t">
				<option value="${t.typeId}">
					${t.typeName}
				</option>
			</c:forEach>
		</select>
	</td>
	<td>
		<select class="form-select jobSelect" name="jobId_0">
			<c:forEach items="${jobs}" var="j">
				<option value="${j.id}">${j.name}</option>
			</c:forEach>
		</select>
	</td>
	<td>
		<select class="form-select raritySelect" name="rarityId_0">
			<c:forEach items="${rarities}" var="r">
				<option value="${r.rarityId}" data-color="${r.color}">
					${r.rarityName}
				</option>
			</c:forEach>
		</select>
	</td>
	<td>
		<input type="number" class="form-control reqLvInput"
			name="requiredLevel_0"/>
	</td>
	<td class="text-center">
		<button type="button" class="btn btn-sm btn-danger btn-delete-row">
			Delete
		</button>
	</td>
</tr>
</script>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
	let newIndex = 1;

	/* ================= APPLY GLOBAL ================= */
	function applyGlobalValues($rows) {

		const gType = $('#globalType').val();
		const gJob = $('#globalJob').val();
		const gRarity = $('#globalRarity').val();
		const gLv = $('#globalReqLv').val();
		const prefix = $('#globalNamePrefix').val().trim();

		if (gType)
			$rows.find('.typeSelect').val(gType);
		if (gJob)
			$rows.find('.jobSelect').val(gJob);
		if (gRarity)
			$rows.find('.raritySelect').val(gRarity).trigger('change');
		if (gLv)
			$rows.find('.reqLvInput').val(gLv);

		if (prefix) {
			$rows.find('.itemName').each(function() {
				const original = $(this).data('original');
				if (!original)
					return;
				const last = original.trim().split(/\s+/).pop();
				$(this).val(prefix + ' ' + last);
			});
		}
	}

	/* ================= GLOBAL HANDLERS ================= */
	$('#globalType, #globalJob, #globalRarity, #globalReqLv, #globalNamePrefix')
			.on('change input', function() {
				applyGlobalValues($('#cloneTable tbody'));
			});

	/* ================= NEW ITEM ID ================= */
	$('#globalSetId').on('input', function() {
		let p = $(this).val();
		let i = 1;
		$('.newItemId').each(function() {
			$(this).val(p + String(i++).padStart(4, '0'));
		});
	});

	/* ================= ADD ROW ================= */
	$('#btnAddRow').on(
			'click',
			function() {

				let idx = 'n' + newIndex++;

				let html = $('#rowTemplate').html().replaceAll('name_0',
						'name_' + idx).replaceAll('typeId_0', 'typeId_' + idx)
						.replaceAll('jobId_0', 'jobId_' + idx).replaceAll(
								'rarityId_0', 'rarityId_' + idx).replaceAll(
								'requiredLevel_0', 'requiredLevel_' + idx);

				let $row = $(html);
				applyGlobalValues($row);

				$('#cloneTable tbody').append($row);
				$('#globalSetId').trigger('input');
			});

	/* ================= DELETE ROW ================= */
	$(document).on('click', '.btn-delete-row', function() {
		if (!confirm('Delete this item row?'))
			return;
		$(this).closest('tr').remove();
		$('#globalSetId').trigger('input');
	});

	/* ================= RARITY COLOR ================= */
	$(document).on('change', '.raritySelect', function() {
		const color = $(this).find(':selected').data('color');
		$(this).css('color', color || '');
	}).trigger('change');
</script>

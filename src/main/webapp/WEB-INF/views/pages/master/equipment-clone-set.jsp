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
					<label class="form-label">New Set ID (Global)</label> <input
						type="number" id="globalSetId" name="newSetId"
						class="form-control" required />
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
			<div class="card-header">
				<strong>Preview & Edit Items</strong>
			</div>

			<div class="card-body p-0">
				<table class="table table-striped mb-0" id="cloneTable">
					<thead class="table-dark">
						<tr>
							<th>Original ID</th>
							<th>New Item ID</th>
							<th>Name</th>
							<th>Job</th>
							<th>Rarity</th>
							<th>Req Lv</th>
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
							</tr>
						</c:forEach>

					</tbody>
				</table>
			</div>

			<div class="card-footer d-flex justify-content-between">
				<a href="${pageContext.request.contextPath}/master/equipment"
					class="btn btn-secondary">Cancel</a>

				<button class="btn btn-success"
					onclick="return confirm('Clone all items in this set?');">
					Clone Set</button>
			</div>
		</div>
	</form>
</c:if>

<!-- ================= JS ================= -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
	/* ===== New Item ID from Global Set ID ===== */
	$('#globalSetId').on('input', function() {
		let prefix = $(this).val();
		let i = 1;
		$('.newItemId').each(function() {
			$(this).val(prefix + String(i).padStart(4, '0'));
			i++;
		});
	});

	/* ===== Global Job ===== */
	$('#globalJob').on('change', function() {
		if (this.value)
			$('.jobSelect').val(this.value);
	});

	/* ===== Global Rarity ===== */
	$('#globalRarity').on('change', function() {
		if (this.value)
			$('.raritySelect').val(this.value).trigger('change');
	});

	/* ===== Global Req Lv ===== */
	$('#globalReqLv').on('input', function() {
		if (this.value)
			$('.reqLvInput').val(this.value);
	});

	/* ===== Global Name Prefix (USE LAST WORD ONLY) ===== */
	$('#globalNamePrefix').on('input', function() {
		const prefix = $(this).val().trim();

		$('.itemName').each(function() {
			const original = $(this).data('original');
			if (!original)
				return;

			const parts = original.trim().split(/\s+/);
			const lastWord = parts[parts.length - 1];

			$(this).val(prefix ? prefix + ' ' + lastWord : original);
		});
	});

	/* ===== Rarity color ===== */
	$(document).on('change', '.raritySelect', function() {
		const color = $(this).find(':selected').data('color');
		$(this).css('color', color);
	}).trigger('change');
</script>

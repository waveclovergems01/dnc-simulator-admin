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

		<div class="row align-items-end">
			<div class="col-md-7">
				<label class="form-label">Original Equipment</label> <select
					name="equipmentItemId" class="form-select" required>
					<option value="">-- Select Equipment With Suffix --</option>
					<c:forEach items="${sourceEquipments}" var="e">
						<option value="${e.itemId}"
							<c:if test="${equipmentItem != null && e.itemId == equipmentItem.itemId}">
								selected
							</c:if>>
							${e.name} (ID: ${e.itemId})</option>
					</c:forEach>
				</select>
			</div>

			<div class="col-md-3">
				<button type="submit" class="btn btn-primary w-100">Load
					Suffix</button>
			</div>

			<div class="col-md-2 text-end">
				<a href="${pageContext.request.contextPath}/master/suffix-items"
					class="btn btn-outline-secondary w-100"> Back </a>
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
								<td>${s.id} <input type="hidden" name="suffixItemId"
									value="${s.id}" />
								</td>

								<td><input type="text" class="form-control suffixName"
									value="${s.name}" readonly /></td>

								<td>${s.itemId}</td>
								<td>${s.suffixTypeId}</td>

								<td class="text-center">
									<button type="button"
										class="btn btn-sm btn-outline-danger btn-delete-row">
										Remove</button>
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
				<div class="row align-items-end">
					<div class="col-md-7">
						<label class="form-label">Target Equipment</label> <select
							name="newEquipmentItemId" class="form-select" required>
							<option value="">-- Select Equipment Without Suffix --</option>
							<c:forEach items="${targetEquipments}" var="t">
								<option value="${t.itemId}">${t.name} (ID: ${t.itemId})
								</option>
							</c:forEach>
						</select>
					</div>

					<div class="col-md-3">
						<button type="submit" class="btn btn-success w-100"
							onclick="return confirm('Clone all suffix items to selected equipment?');">
							Clone Suffix</button>
					</div>
				</div>

				<div class="form-text mt-2">⚠️ All selected suffix items will
					be duplicated to the target equipment.</div>
			</div>
		</div>

	</form>
</c:if>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
	/* ================= REMOVE SUFFIX ROW ================= */
	$(document).on('click', '.btn-delete-row', function() {
		if (!confirm('Remove this suffix from clone list?'))
			return;
		$(this).closest('tr').remove();
	});
</script>

<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<h2 class="mb-3">${isAdd ? 'Add' : 'Edit'} Equipment Item</h2>

<form id="equipmentForm"
	action="${pageContext.request.contextPath}/master/equipment/save"
	method="post"
	enctype="multipart/form-data"
	class="card p-4 shadow-sm"
	onsubmit="return validateEquipmentForm();">

	<input type="hidden" name="isAdd" value="${isAdd}" />

	<div class="row mb-3">
		<div class="col-md-4">
			<label class="form-label">Item ID</label>
			<input type="number"
				name="itemId"
				value="${item.itemId}"
				class="form-control"
				${!isAdd ? 'readonly' : ''}
				required />
		</div>

		<div class="col-md-8">
			<label class="form-label">Item Name</label>
			<input type="text"
				name="name"
				value="${item.name}"
				class="form-control"
				required />
		</div>
	</div>

	<div class="row mb-4">
		<div class="col-md-6">
			<label class="form-label">Icon File</label>
			<input type="file"
				name="iconFile"
				id="iconFile"
				class="form-control"
				accept=".png,.jpg,.jpeg,.webp,image/png,image/jpeg,image/webp" />
			<small class="text-muted">PNG / JPG / WEBP, max 2MB</small>

			<c:if test="${not empty item.iconName}">
				<div class="form-check mt-2">
					<input class="form-check-input"
						type="checkbox"
						id="keepOldIcon"
						name="keepOldIcon"
						value="true"
						checked />
					<label class="form-check-label" for="keepOldIcon">
						Keep old icon
					</label>
				</div>
			</c:if>
		</div>

		<div class="col-md-6">
			<label class="form-label">Preview</label>
			<div class="border rounded p-2 text-center bg-light"
				style="min-height: 140px;">
				<c:choose>
					<c:when test="${not empty item.iconName && not empty item.itemId}">
						<img id="iconPreview"
							src="${pageContext.request.contextPath}/master/equipment/icon?itemId=${item.itemId}"
							alt="icon preview"
							style="max-width: 120px; max-height: 120px;" />
						<div id="iconPreviewEmpty" class="text-muted d-none">No image</div>
					</c:when>
					<c:otherwise>
						<img id="iconPreview"
							src=""
							alt="icon preview"
							style="max-width: 120px; max-height: 120px; display:none;" />
						<div id="iconPreviewEmpty" class="text-muted">No image</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

	<div class="row mb-3">
		<div class="col-md-4">
			<label class="form-label">Item Type</label>
			<select name="typeId" class="form-select" required>
				<option value="">-- Select Type --</option>
				<c:forEach items="${itemTypes}" var="t">
					<option value="${t.typeId}"
						${t.typeId == item.typeId ? 'selected' : ''}>
						${t.typeName} [${t.typeId}]
					</option>
				</c:forEach>
			</select>
		</div>

		<div class="col-md-4">
			<label class="form-label">Job</label>
			<select name="jobId" class="form-select" required>
				<option value="">-- Select Job --</option>
				<c:forEach items="${jobs}" var="j">
					<option value="${j.id}" ${j.id == item.jobId ? 'selected' : ''}>
						${j.name}
					</option>
				</c:forEach>
			</select>
		</div>

		<div class="col-md-4">
			<label class="form-label">Rarity</label>
			<select name="rarityId" class="form-select" required>
				<option value="">-- Select Rarity --</option>
				<c:forEach items="${rarities}" var="r">
					<option value="${r.rarityId}"
						${r.rarityId == item.rarityId ? 'selected' : ''}>
						${r.rarityName}
					</option>
				</c:forEach>
			</select>
		</div>
	</div>

	<div class="row mb-4">
		<div class="col-md-3">
			<label class="form-label">Required Level</label>
			<input type="number"
				name="requiredLevel"
				value="${item.requiredLevel}"
				class="form-control"
				required />
		</div>

		<div class="col-md-3">
			<label class="form-label">Durability</label>
			<input type="number"
				name="durability"
				value="${item.durability}"
				class="form-control"
				required />
		</div>

		<div class="col-md-3">
			<label class="form-label">Set ID</label>
			<input type="number"
				name="setId"
				value="${item.setId}"
				class="form-control" />
		</div>
	</div>

	<h5 class="mb-3">Equipment Stats</h5>

	<table class="table table-bordered">
		<thead class="table-light">
			<tr>
				<th style="width: 40%">Stat</th>
				<th>Min</th>
				<th>Max</th>
				<th style="width: 120px">%</th>
				<th style="width: 60px"></th>
			</tr>
		</thead>
		<tbody id="statTableBody">

			<c:forEach items="${item.stats}" var="s" varStatus="st">
				<tr>
					<td>
						<select name="stats[${st.index}].statId" class="form-select">
							<option value="">-- Select Stat --</option>
							<c:forEach items="${stats}" var="stat">
								<option value="${stat.statId}"
									${stat.statId == s.statId ? 'selected' : ''}>
									${stat.displayName}
								</option>
							</c:forEach>
						</select>
					</td>

					<td>
						<input type="number"
							class="form-control value-min"
							step="${s.isPercentage == 1 ? 'any' : '1'}"
							name="stats[${st.index}].valueMin"
							value="${s.isPercentage == 1 ? s.valueMin : fn:substringBefore(s.valueMin, '.')}" />
					</td>

					<td>
						<input type="number"
							class="form-control value-max"
							step="${s.isPercentage == 1 ? 'any' : '1'}"
							name="stats[${st.index}].valueMax"
							value="${s.isPercentage == 1 ? s.valueMax : fn:substringBefore(s.valueMax, '.')}" />
					</td>

					<td class="text-center">
						<input type="checkbox"
							class="form-check-input is-percentage"
							name="stats[${st.index}].isPercentage"
							value="1"
							${s.isPercentage == 1 ? 'checked' : ''}
							onchange="onPercentageToggle(this)" />
					</td>

					<td class="text-center">
						<button type="button"
							class="btn btn-sm btn-danger"
							onclick="removeRow(this)">X</button>
					</td>
				</tr>
			</c:forEach>

		</tbody>
	</table>

	<button type="button"
		class="btn btn-sm btn-outline-primary mb-3"
		onclick="addStatRow()">+ Add Stat</button>

	<div class="d-flex justify-content-between mt-4">
		<a href="${pageContext.request.contextPath}/master/equipment"
			class="btn btn-secondary">Back</a>
		<button type="submit" class="btn btn-success">Save</button>
	</div>
</form>

<script>
	var APP_CTX = '${pageContext.request.contextPath}';
</script>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/master/equipment/equipment-form.js"></script>

<script type="text/template" id="statRowTemplate">
<tr>
	<td>
		<select name="stats[__INDEX__].statId" class="form-select">
			<option value="">-- Select Stat --</option>
			<c:forEach items="${stats}" var="stat">
				<option value="${stat.statId}">
					${stat.displayName}
				</option>
			</c:forEach>
		</select>
	</td>

	<td>
		<input type="number"
			class="form-control value-min"
			step="1"
			name="stats[__INDEX__].valueMin" />
	</td>

	<td>
		<input type="number"
			class="form-control value-max"
			step="1"
			name="stats[__INDEX__].valueMax" />
	</td>

	<td class="text-center">
		<input type="checkbox"
			class="form-check-input is-percentage"
			name="stats[__INDEX__].isPercentage"
			value="1"
			onchange="onPercentageToggle(this)" />
	</td>

	<td class="text-center">
		<button type="button"
			class="btn btn-sm btn-danger"
			onclick="removeRow(this)">X</button>
	</td>
</tr>
</script>
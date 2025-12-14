<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2 class="mb-3">${isAdd ? 'Add' : 'Edit'} Equipment Item</h2>

<form action="${pageContext.request.contextPath}/master/equipment/save"
	method="post" class="card p-4 shadow-sm">

	<input type="hidden" name="isAdd" value="${isAdd}" />

	<!-- ================= BASIC INFO ================= -->
	<div class="row mb-3">
		<div class="col-md-4">
			<label class="form-label">Item ID</label> <input type="number"
				name="itemId" value="${item.itemId}" class="form-control"
				${!isAdd ? 'readonly' : ''} required />
		</div>

		<div class="col-md-8">
			<label class="form-label">Item Name</label> <input type="text"
				name="name" value="${item.name}" class="form-control" required />
		</div>
	</div>

	<div class="row mb-3">
		<div class="col-md-4">
			<label class="form-label">Item Type</label> <select name="typeId"
				class="form-select" required>
				<option value="">-- Select Type --</option>
				<c:forEach items="${itemTypes}" var="t">
					<option value="${t.typeId}"
						${t.typeId == item.typeId ? 'selected' : ''}>
						${t.typeName} [${t.typeId}]</option>
				</c:forEach>
			</select>
		</div>

		<div class="col-md-4">
			<label class="form-label">Job</label> <select name="jobId"
				class="form-select" required>
				<option value="">-- Select Job --</option>
				<c:forEach items="${jobs}" var="j">
					<option value="${j.id}" ${j.id == item.jobId ? 'selected' : ''}>
						${j.name}</option>
				</c:forEach>
			</select>
		</div>

		<div class="col-md-4">
			<label class="form-label">Rarity</label> <select name="rarityId"
				class="form-select" required>
				<option value="">-- Select Rarity --</option>
				<c:forEach items="${rarities}" var="r">
					<option value="${r.rarityId}"
						${r.rarityId == item.rarityId ? 'selected' : ''}>
						${r.rarityName}</option>
				</c:forEach>
			</select>
		</div>
	</div>

	<div class="row mb-4">
		<div class="col-md-3">
			<label class="form-label">Required Level</label> <input type="number"
				name="requiredLevel" value="${item.requiredLevel}"
				class="form-control" required />
		</div>

		<div class="col-md-3">
			<label class="form-label">Durability</label> <input type="number"
				name="durability" value="${item.durability}" class="form-control"
				required />
		</div>

		<div class="col-md-3">
			<label class="form-label">Set ID</label> <input type="number"
				name="setId" value="${item.setId}" class="form-control" />
		</div>
	</div>

	<!-- ================= STATS ================= -->
	<h5 class="mb-3">Equipment Stats</h5>

	<table class="table table-bordered">
		<thead class="table-light">
			<tr>
				<th style="width: 40%;">Stat</th>
				<th>Min</th>
				<th>Max</th>
				<th style="width: 120px;">%</th>
				<th style="width: 60px;"></th>
			</tr>
		</thead>
		<tbody id="statTableBody">
			<c:forEach items="${item.stats}" var="s" varStatus="st">
				<tr>
					<td><select name="stats[${st.index}].statId"
						class="form-select">
							<c:forEach items="${stats}" var="stat">
								<option value="${stat.statId}"
									${stat.statId == s.statId ? 'selected' : ''}>
									${stat.statName}</option>
							</c:forEach>
					</select></td>
					<td><input type="number" name="stats[${st.index}].valueMin"
						value="${s.valueMin}" class="form-control" /></td>
					<td><input type="number" name="stats[${st.index}].valueMax"
						value="${s.valueMax}" class="form-control" /></td>
					<td class="text-center"><input type="checkbox"
						name="stats[${st.index}].isPercentage" value="1"
						${s.isPercentage == 1 ? 'checked' : ''} /></td>
					<td class="text-center">
						<button type="button" class="btn btn-sm btn-danger"
							onclick="removeRow(this)">X</button>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<button type="button" class="btn btn-sm btn-outline-primary mb-3"
		onclick="addStatRow()">+ Add Stat</button>

	<!-- ================= ACTION ================= -->
	<div class="d-flex justify-content-between mt-4">
		<a href="${pageContext.request.contextPath}/master/equipment"
			class="btn btn-secondary"> Back </a>

		<button type="submit" class="btn btn-success">Save</button>
	</div>
</form>

<script>
	let statIndex = ${item.stats != null ? item.stats.size() : 0};

	function addStatRow() {
		const row = `
		<tr>
			<td>
				<select name="stats[` + statIndex + `].statId"
						class="form-select">
					<c:forEach items="${stats}" var="stat">
						<option value="${stat.statId}">
							${stat.statName}
						</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<input type="number"
					   name="stats[` + statIndex + `].valueMin"
					   class="form-control" />
			</td>
			<td>
				<input type="number"
					   name="stats[` + statIndex + `].valueMax"
					   class="form-control" />
			</td>
			<td class="text-center">
				<input type="checkbox"
					   name="stats[` + statIndex + `].isPercentage"
					   value="1" />
			</td>
			<td class="text-center">
				<button type="button"
						class="btn btn-sm btn-danger"
						onclick="removeRow(this)">X</button>
			</td>
		</tr>`;
		$('#statTableBody').append(row);
		statIndex++;
	}

	function removeRow(btn) {
		$(btn).closest('tr').remove();
	}
</script>

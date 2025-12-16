<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<title>Manage Suffix Stats</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>

<body>

	<!-- ===================================================== -->
	<!-- HEADER -->
	<!-- ===================================================== -->
	<div class="d-flex justify-content-between align-items-center mb-3">
		<h3 class="mb-0">
			Manage Suffix Extra Stats <span class="text-muted fs-6">(Suffix
				Item ID: ${suffixItemId})</span>
		</h3>

		<!-- BACK BUTTON -->
		<button type="button" class="btn btn-secondary" onclick="goBack()">
			← Back</button>
	</div>

	<!-- ===================================================== -->
	<!-- EXTRA STATS (ของเดิมทั้งหมด) -->
	<!-- ===================================================== -->
	<form method="post"
		action="${pageContext.request.contextPath}/suffix-item/extra-stats/save-form"
		onsubmit="return validateSuffixStatsForm();"
		class="card p-4 shadow-sm mb-4">

		<input type="hidden" name="suffixItemId" value="${suffixItemId}" />

		<h5 class="mb-3">Extra Stats</h5>

		<table class="table table-bordered align-middle">
			<thead class="table-dark">
				<tr>
					<th style="width: 40%">Stat</th>
					<th>Min</th>
					<th>Max</th>
					<th style="width: 80px">%</th>
					<th style="width: 60px"></th>
				</tr>
			</thead>

			<tbody id="statTableBody">
				<c:forEach items="${extraStats}" var="s" varStatus="st">
					<tr>
						<td><select name="stats[${st.index}].statId"
							class="form-select">
								<option value="">-- Select Stat --</option>
								<c:forEach items="${stats}" var="stat">
									<option value="${stat.statId}"
										${stat.statId == s.statId ? 'selected' : ''}>${empty stat.displayName ? stat.statName : stat.displayName}
									</option>
								</c:forEach>
						</select></td>

						<td><input type="number" class="form-control value-min"
							name="stats[${st.index}].valueMin"
							step="${s.isPercentage == 1 ? 'any' : '1'}"
							value="${s.isPercentage == 1
                                ? s.valueMin
                                : fn:substringBefore(s.valueMin, '.')}" />
						</td>

						<td><input type="number" class="form-control value-max"
							name="stats[${st.index}].valueMax"
							step="${s.isPercentage == 1 ? 'any' : '1'}"
							value="${s.isPercentage == 1
                                ? s.valueMax
                                : fn:substringBefore(s.valueMax, '.')}" />
						</td>

						<td class="text-center"><input type="hidden"
							class="is-percentage-hidden"
							name="stats[${st.index}].isPercentage" value="${s.isPercentage}" />

							<input type="checkbox" class="form-check-input is-percentage"
							${s.isPercentage == 1 ? 'checked' : ''}
							onchange="onPercentageToggle(this)" /></td>

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

		<div class="text-end">
			<button type="submit" class="btn btn-success">Save Extra
				Stats</button>
		</div>
	</form>

	<!-- ===================================================== -->
	<!-- ABILITY -->
	<!-- ===================================================== -->
	<h3 class="mb-3">
		Manage Suffix Equip Ability <span class="text-muted fs-6">(Suffix
			Item ID: ${suffixItemId})</span>
	</h3>

	<form method="post"
		action="${pageContext.request.contextPath}/suffix-item/ability/save"
		onsubmit="return validateAbilityForm();" class="card p-4 shadow-sm">

		<input type="hidden" name="suffixItemId" value="${suffixItemId}" /> <input
			type="hidden" name="abilityId" value="${ability.abilityId}" />

		<div class="row mb-3">
			<div class="col-md-6">
				<label class="form-label">Raw Text</label> <input type="text"
					name="rawText" class="form-control" value="${ability.rawText}" />
			</div>

			<div class="col-md-6">
				<label class="form-label">Type</label> <input type="text"
					name="type" class="form-control" value="${ability.type}" />
			</div>
		</div>

		<h5 class="mb-3">Ability Stats</h5>

		<table class="table table-bordered align-middle">
			<thead class="table-dark">
				<tr>
					<th style="width: 40%">Stat</th>
					<th>Min</th>
					<th>Max</th>
					<th style="width: 80px">%</th>
					<th style="width: 60px"></th>
				</tr>
			</thead>

			<tbody id="abilityStatTableBody">
				<c:forEach items="${ability.abilityStats}" var="s" varStatus="st">
					<tr>
						<td><select name="stats[${st.index}].statId"
							class="form-select">
								<option value="">-- Select Stat --</option>
								<c:forEach items="${stats}" var="stat">
									<option value="${stat.statId}"
										${stat.statId == s.statId ? 'selected' : ''}>${empty stat.displayName ? stat.statName : stat.displayName}
									</option>
								</c:forEach>
						</select></td>

						<td><input type="number" class="form-control value-min"
							name="stats[${st.index}].valueMin"
							step="${s.isPercentage == 1 ? 'any' : '1'}" value="${s.valueMin}" />
						</td>

						<td><input type="number" class="form-control value-max"
							name="stats[${st.index}].valueMax"
							step="${s.isPercentage == 1 ? 'any' : '1'}" value="${s.valueMax}" />
						</td>

						<td class="text-center"><input type="hidden"
							class="is-percentage-hidden"
							name="stats[${st.index}].isPercentage" value="${s.isPercentage}" />

							<input type="checkbox" class="form-check-input is-percentage"
							${s.isPercentage == 1 ? 'checked' : ''}
							onchange="onPercentageToggle(this)" /></td>

						<td class="text-center">
							<button type="button" class="btn btn-sm btn-danger"
								onclick="removeRow(this)">X</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<button type="button" class="btn btn-sm btn-outline-primary mb-3"
			onclick="addAbilityStatRow()">+ Add Ability Stat</button>

		<div class="text-end">
			<button type="submit" class="btn btn-success">Save Ability</button>
		</div>
	</form>

	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

	<script>
/* ================= BACK ================= */
function goBack() {
    window.history.back();
}

/* ================= ROW UTILS ================= */
function removeRow(btn) {
    $(btn).closest('tr').remove();
}

/* ================= PERCENTAGE ================= */
function onPercentageToggle(cb) {
    const row = cb.closest('tr');
    const hidden = row.querySelector('.is-percentage-hidden');
    const min = row.querySelector('.value-min');
    const max = row.querySelector('.value-max');

    if (cb.checked) {
        hidden.value = 1;
        min.step = 'any';
        max.step = 'any';
    } else {
        hidden.value = 0;
        min.step = '1';
        max.step = '1';
        if (min.value !== '') min.value = Math.trunc(min.value);
        if (max.value !== '') max.value = Math.trunc(max.value);
    }
}

/* ================= ADD ROW ================= */
let statIndex = ${extraStats != null ? extraStats.size() : 0};

function addStatRow() {
    const row = `
    <tr>
        <td>
            <select name="stats[` + statIndex + `].statId" class="form-select">
                <option value="">-- Select Stat --</option>
                <c:forEach items="${stats}" var="stat">
                    <option value="${stat.statId}">
                        ${empty stat.displayName ? stat.statName : stat.displayName}
                    </option>
                </c:forEach>
            </select>
        </td>
        <td><input type="number" class="form-control value-min"
                   name="stats[` + statIndex + `].valueMin" step="1"/></td>
        <td><input type="number" class="form-control value-max"
                   name="stats[` + statIndex + `].valueMax" step="1"/></td>
        <td class="text-center">
            <input type="hidden" class="is-percentage-hidden"
                   name="stats[` + statIndex + `].isPercentage" value="0"/>
            <input type="checkbox" class="form-check-input is-percentage"
                   onchange="onPercentageToggle(this)"/>
        </td>
        <td class="text-center">
            <button type="button" class="btn btn-sm btn-danger"
                    onclick="removeRow(this)">X</button>
        </td>
    </tr>`;
    $('#statTableBody').append(row);
    statIndex++;
}

function addAbilityStatRow() {
    const idx = $('#abilityStatTableBody tr').length;
    const row = `
    <tr>
        <td>
            <select name="stats[` + idx + `].statId" class="form-select">
                <option value="">-- Select Stat --</option>
                <c:forEach items="${stats}" var="stat">
                    <option value="${stat.statId}">
                        ${empty stat.displayName ? stat.statName : stat.displayName}
                    </option>
                </c:forEach>
            </select>
        </td>
        <td><input type="number" class="form-control value-min"
                   name="stats[` + idx + `].valueMin" step="1"/></td>
        <td><input type="number" class="form-control value-max"
                   name="stats[` + idx + `].valueMax" step="1"/></td>
        <td class="text-center">
            <input type="hidden" class="is-percentage-hidden"
                   name="stats[` + idx + `].isPercentage" value="0"/>
            <input type="checkbox" class="form-check-input is-percentage"
                   onchange="onPercentageToggle(this)"/>
        </td>
        <td class="text-center">
            <button type="button" class="btn btn-sm btn-danger"
                    onclick="removeRow(this)">X</button>
        </td>
    </tr>`;
    $('#abilityStatTableBody').append(row);
}

/* ================= VALIDATION (เดิม) ================= */
function validateSuffixStatsForm() { return true; }
function validateAbilityForm() { return true; }
</script>

</body>
</html>

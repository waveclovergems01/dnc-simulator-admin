<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<title>Manage Suffix Extra Stats</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
body {
	padding: 20px;
}

.btn-xs {
	padding: 0.15rem 0.4rem;
	font-size: 0.75rem;
}
</style>
</head>

<body>

	<h3 class="mb-3">
		Manage Suffix Extra Stats <span class="text-muted fs-6">(Suffix
			Item ID: ${suffixItemId})</span>
	</h3>

	<form method="post"
		action="${pageContext.request.contextPath}/suffix-item/extra-stats/save-form"
		onsubmit="return validateSuffixStatsForm();"
		class="card p-4 shadow-sm">

		<!-- REQUIRED -->
		<input type="hidden" name="suffixItemId" value="${suffixItemId}" />

		<!-- ================= EXTRA STATS ================= -->
		<h5 class="mb-3">Extra Stats</h5>

		<table class="table table-bordered align-middle">
			<thead class="table-dark">
				<tr>
					<th style="width: 40%">Stat</th>
					<th style="width: 15%">Min</th>
					<th style="width: 15%">Max</th>
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
							step="${s.isPercentage == 1 ? 'any' : '1'}" value="${s.valueMin}" />
						</td>

						<td><input type="number" class="form-control value-max"
							name="stats[${st.index}].valueMax"
							step="${s.isPercentage == 1 ? 'any' : '1'}" value="${s.valueMax}" />
						</td>

						<td class="text-center">
							<!-- REAL SUBMIT VALUE --> <input type="hidden"
							class="is-percentage-hidden"
							name="stats[${st.index}].isPercentage" value="${s.isPercentage}" />

							<!-- UI ONLY --> <input type="checkbox"
							class="form-check-input is-percentage"
							${s.isPercentage == 1 ? 'checked' : ''}
							onchange="onPercentageToggle(this)" />
						</td>

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
			<a
				href="${pageContext.request.contextPath}/master/suffix-items/edit?itemId=${itemId}"
				class="btn btn-secondary"> Back </a>

			<button type="submit" class="btn btn-success">Save</button>
		</div>
	</form>

	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

	<script>
let statIndex = ${extraStats != null ? extraStats.size() : 0};

/* ===== add stat row ===== */
function addStatRow() {

    const row = `
    <tr>
        <td>
            <select name="stats[` + statIndex + `].statId"
                    class="form-select">
                <option value="">-- Select Stat --</option>
                <c:forEach items="${stats}" var="stat">
                    <option value="${stat.statId}">
                        ${empty stat.displayName ? stat.statName : stat.displayName}
                    </option>
                </c:forEach>
            </select>
        </td>

        <td>
            <input type="number"
                   class="form-control value-min"
                   step="1"
                   name="stats[` + statIndex + `].valueMin" />
        </td>

        <td>
            <input type="number"
                   class="form-control value-max"
                   step="1"
                   name="stats[` + statIndex + `].valueMax" />
        </td>

        <td class="text-center">
            <input type="hidden"
                   class="is-percentage-hidden"
                   name="stats[` + statIndex + `].isPercentage"
                   value="0" />
            <input type="checkbox"
                   class="form-check-input is-percentage"
                   onchange="onPercentageToggle(this)" />
        </td>

        <td class="text-center">
            <button type="button"
                    class="btn btn-sm btn-danger"
                    onclick="removeRow(this)">
                X
            </button>
        </td>
    </tr>`;

    $('#statTableBody').append(row);
    statIndex++;
}

/* ===== remove row ===== */
function removeRow(btn) {
    $(btn).closest('tr').remove();
}

/* ===== toggle percentage ===== */
function onPercentageToggle(checkbox) {

    const row = checkbox.closest('tr');
    const hidden = row.querySelector('.is-percentage-hidden');
    const minInput = row.querySelector('.value-min');
    const maxInput = row.querySelector('.value-max');

    if (checkbox.checked) {
        hidden.value = 1;
        minInput.step = 'any';
        maxInput.step = 'any';
    } else {
        hidden.value = 0;
        minInput.step = '1';
        maxInput.step = '1';

        if (minInput.value !== '') {
            minInput.value = Math.trunc(minInput.value);
        }
        if (maxInput.value !== '') {
            maxInput.value = Math.trunc(maxInput.value);
        }
    }
}

/* ===== validate before submit ===== */
function validateSuffixStatsForm() {

    const rows = document.querySelectorAll('#statTableBody tr');

    for (let i = 0; i < rows.length; i++) {

        const stat = rows[i].querySelector('select');
        const min  = rows[i].querySelector('.value-min');
        const max  = rows[i].querySelector('.value-max');
        const pct  = rows[i].querySelector('.is-percentage');

        if (!stat.value) {
            alert('Row ' + (i + 1) + ': Please select Stat');
            stat.focus();
            return false;
        }

        if (min.value === '' || max.value === '') {
            alert('Row ' + (i + 1) + ': Min / Max required');
            min.focus();
            return false;
        }

        let minVal = parseFloat(min.value);
        let maxVal = parseFloat(max.value);

        if (!pct.checked) {
            minVal = Math.trunc(minVal);
            maxVal = Math.trunc(maxVal);
            min.value = minVal;
            max.value = maxVal;
        }

        if (minVal > maxVal) {
            alert('Row ' + (i + 1) + ': Min must not exceed Max');
            min.focus();
            return false;
        }
    }
    return true;
}
</script>

</body>
</html>

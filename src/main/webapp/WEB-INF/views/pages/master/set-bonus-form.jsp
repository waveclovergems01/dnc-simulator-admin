<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<h2 class="mb-3">${isAdd ? 'Add' : 'Edit'}Set Bonus</h2>

<form action="${pageContext.request.contextPath}/master/set-bonus/save"
	method="post" class="card p-4 shadow-sm"
	onsubmit="return validateSetBonusForm();">

	<input type="hidden" name="isAdd" value="${isAdd}" />

	<!-- ================= SET INFO ================= -->
	<div class="row mb-3">
		<div class="col-md-4">
			<label class="form-label">Set ID</label> <input type="number"
				name="setId" value="${setBonus.setId}" class="form-control"
				${!isAdd ? 'readonly' : ''} required />
		</div>

		<div class="col-md-8">
			<label class="form-label">Set Name</label> <input type="text"
				name="setName" value="${setBonus.setName}" class="form-control"
				required />
		</div>
	</div>

	<!-- ================= COUNT SET ================= -->
	<h5 class="mb-3">Set Bonus Entries</h5>

	<table class="table table-bordered">
		<thead class="table-light">
			<tr>
				<th style="width: 120px;">Count</th>
				<th>Stats</th>
				<th style="width: 60px;"></th>
			</tr>
		</thead>
		<tbody id="entryTableBody">
			<c:forEach items="${setBonus.entries}" var="e" varStatus="ei">
				<tr>
					<td><input type="number" name="entries[${ei.index}].count"
						value="${e.count}" class="form-control" required /></td>
					<td>
						<table class="table table-sm mb-0">
							<tbody id="statBody-${ei.index}">
								<c:forEach items="${e.stats}" var="s" varStatus="si">
									<tr>
										<td><select
											name="entries[${ei.index}].stats[${si.index}].statId"
											class="form-select">
												<option value="">-- Select Stat --</option>
												<c:forEach items="${stats}" var="stat">
													<option value="${stat.statId}"
														${stat.statId == s.statId ? 'selected' : ''}>
														${stat.displayName}</option>
												</c:forEach>
										</select></td>

										<!-- valueMin -->
										<td><input type="number" class="form-control value-min"
											step="${s.isPercentage == 1 ? 'any' : '1'}"
											inputmode="decimal"
											name="entries[${ei.index}].stats[${si.index}].valueMin"
											value="${s.isPercentage == 1 
                                                            ? s.valueMin 
                                                            : fn:substringBefore(s.valueMin, '.')}" />
										</td>

										<!-- valueMax -->
										<td><input type="number" class="form-control value-max"
											step="${s.isPercentage == 1 ? 'any' : '1'}"
											inputmode="decimal"
											name="entries[${ei.index}].stats[${si.index}].valueMax"
											value="${s.isPercentage == 1 
                                                            ? s.valueMax 
                                                            : fn:substringBefore(s.valueMax, '.')}" />
										</td>

										<!-- is_percentage -->
										<td class="text-center"><input type="checkbox"
											class="form-check-input is-percentage"
											name="entries[${ei.index}].stats[${si.index}].isPercentage"
											value="1" ${s.isPercentage == 1 ? 'checked' : ''}
											onchange="onPercentageToggle(this)" /></td>

										<td>
											<button type="button" class="btn btn-sm btn-danger"
												onclick="removeRow(this)">X</button>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

						<button type="button" class="btn btn-sm btn-outline-primary"
							onclick="addStatRow(${ei.index})">+ Add Stat</button>
					</td>
					<td class="text-center">
						<button type="button" class="btn btn-sm btn-danger"
							onclick="removeRow(this)">X</button>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<button type="button" class="btn btn-outline-primary mb-3"
		onclick="addEntryRow()">+ Add Count Set</button>

	<!-- ================= ACTION ================= -->
	<div class="d-flex justify-content-between mt-4">
		<a href="${pageContext.request.contextPath}/master/set-bonus"
			class="btn btn-secondary">Back</a>

		<button type="submit" class="btn btn-success">Save</button>
	</div>
</form>

<!-- ================= JS ================= -->
<script>
let entryIndex = ${setBonus.entries != null ? setBonus.entries.size() : 0};

function addEntryRow() {
    const row = `
    <tr>
        <td>
            <input type="number"
                   name="entries[` + entryIndex + `].count"
                   class="form-control"
                   required />
        </td>
        <td>
            <table class="table table-sm mb-0">
                <tbody id="statBody-` + entryIndex + `"></tbody>
            </table>

            <button type="button"
                    class="btn btn-sm btn-outline-primary"
                    onclick="addStatRow(` + entryIndex + `)">
                + Add Stat
            </button>
        </td>
        <td class="text-center">
            <button type="button"
                    class="btn btn-sm btn-danger"
                    onclick="removeRow(this)">X</button>
        </td>
    </tr>`;
    $('#entryTableBody').append(row);
    entryIndex++;
}

function addStatRow(entryIdx) {
    const tbody = $('#statBody-' + entryIdx);
    const statIndex = tbody.children().length;

    const row = `
    <tr>
        <td>
            <select name="entries[` + entryIdx + `].stats[` + statIndex + `].statId"
                    class="form-select">
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
                   inputmode="decimal"
                   name="entries[` + entryIdx + `].stats[` + statIndex + `].valueMin" />
        </td>

        <td>
            <input type="number"
                   class="form-control value-max"
                   step="1"
                   inputmode="decimal"
                   name="entries[` + entryIdx + `].stats[` + statIndex + `].valueMax" />
        </td>

        <td class="text-center">
            <input type="checkbox"
                   class="form-check-input is-percentage"
                   name="entries[` + entryIdx + `].stats[` + statIndex + `].isPercentage"
                   value="1"
                   onchange="onPercentageToggle(this)" />
        </td>

        <td>
            <button type="button"
                    class="btn btn-sm btn-danger"
                    onclick="removeRow(this)">X</button>
        </td>
    </tr>`;
    tbody.append(row);
}

function removeRow(btn) {
    $(btn).closest('tr').remove();
}

/* ===== toggle percentage ===== */
function onPercentageToggle(checkbox) {
    const row = checkbox.closest('tr');
    const minInput = row.querySelector('.value-min');
    const maxInput = row.querySelector('.value-max');

    if (!minInput || !maxInput) return;

    if (checkbox.checked) {
        minInput.step = 'any';
        maxInput.step = 'any';
    } else {
        minInput.step = '1';
        maxInput.step = '1';

        if (minInput.value !== '') {
            const v = Math.trunc(Number(minInput.value));
            minInput.value = v;
            minInput.setAttribute('value', v);
        }
        if (maxInput.value !== '') {
            const v = Math.trunc(Number(maxInput.value));
            maxInput.value = v;
            maxInput.setAttribute('value', v);
        }
    }
}

/* ===== validation & final normalize ===== */
function validateSetBonusForm() {

    const entryRows = document.querySelectorAll('#entryTableBody > tr');
    if (entryRows.length === 0) {
        alert('Please add at least one Count Set.');
        return false;
    }

    for (let e = 0; e < entryRows.length; e++) {

        const countInput = entryRows[e].querySelector('input[name*=".count"]');
        if (!countInput || countInput.value.trim() === '') {
            alert('Count Set #' + (e + 1) + ': Count is required.');
            countInput.focus();
            return false;
        }

        const statRows = entryRows[e].querySelectorAll('tbody tr');
        if (statRows.length === 0) {
            alert('Count Set #' + (e + 1) + ': Please add at least one Stat.');
            return false;
        }

        for (let s = 0; s < statRows.length; s++) {

            const statSelect = statRows[s].querySelector('select');
            const minInput  = statRows[s].querySelector('.value-min');
            const maxInput  = statRows[s].querySelector('.value-max');
            const isPercent = statRows[s].querySelector('.is-percentage');

            if (!statSelect || statSelect.value === '') {
                alert('Count Set #' + (e + 1) +
                      ', Stat #' + (s + 1) +
                      ': Please select a Stat.');
                statSelect.focus();
                return false;
            }

            if (!minInput || minInput.value.trim() === '') {
                alert('Count Set #' + (e + 1) +
                      ', Stat #' + (s + 1) +
                      ': Min value is required.');
                minInput.focus();
                return false;
            }

            if (!maxInput || maxInput.value.trim() === '') {
                alert('Count Set #' + (e + 1) +
                      ', Stat #' + (s + 1) +
                      ': Max value is required.');
                maxInput.focus();
                return false;
            }

            let minVal = parseFloat(minInput.value);
            let maxVal = parseFloat(maxInput.value);

            if (!isPercent.checked) {
                minVal = Math.trunc(minVal);
                maxVal = Math.trunc(maxVal);
                minInput.value = minVal;
                maxInput.value = maxVal;
                minInput.setAttribute('value', minVal);
                maxInput.setAttribute('value', maxVal);
            }

            if (minVal > maxVal) {
                alert('Count Set #' + (e + 1) +
                      ', Stat #' + (s + 1) +
                      ': Min value must not be greater than Max value.');
                minInput.focus();
                return false;
            }
        }
    }
    return true;
}
</script>

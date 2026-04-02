<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2 class="mb-3">📦 Clone Equipment Set</h2>

<div class="card p-3 mb-4 shadow-sm">
	<form method="get"
		action="${pageContext.request.contextPath}/master/equipment/clone-set">

		<div class="row align-items-end">
			<div class="col-md-4">
				<label class="form-label">Select Original Set</label>
				<select name="setId" class="form-select" required>
					<option value="">-- Select Set --</option>
					<c:forEach items="${setBonuses}" var="s">
						<option value="${s.setId}" ${s.setId == setId ? 'selected' : ''}>
							${s.setName} (ID: ${s.setId})
						</option>
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

		<div class="card p-3 mb-3 shadow-sm">
			<div class="row g-3">

				<div class="col-md-3">
					<label class="form-label">New Set ID</label>
					<input type="number"
						id="globalSetId"
						name="newSetId"
						class="form-control"
						required />
				</div>

				<div class="col-md-3">
					<label class="form-label">Item Type (Global)</label>
					<select id="globalType" name="globalType" class="form-select">
						<option value="">-- Same as original --</option>
						<c:forEach items="${itemTypes}" var="t">
							<option value="${t.typeId}">
								${t.typeName} (ID:${t.typeId})
							</option>
						</c:forEach>
					</select>
				</div>

				<div class="col-md-3">
					<label class="form-label">Job (Global)</label>
					<select id="globalJob" name="globalJob" class="form-select">
						<option value="">-- Same as original --</option>
						<c:forEach items="${jobs}" var="j">
							<option value="${j.id}">${j.name}</option>
						</c:forEach>
					</select>
				</div>

				<div class="col-md-3">
					<label class="form-label">Rarity (Global)</label>
					<select id="globalRarity" name="globalRarity" class="form-select">
						<option value="">-- Same as original --</option>
						<c:forEach items="${rarities}" var="r">
							<option value="${r.rarityId}" data-color="${r.color}">
								${r.rarityName}
							</option>
						</c:forEach>
					</select>
				</div>

				<div class="col-md-3">
					<label class="form-label">Req Lv (Global)</label>
					<input type="number"
						id="globalReqLv"
						name="globalReqLv"
						class="form-control" />
				</div>

				<div class="col-md-6">
					<label class="form-label">Name Prefix (Global)</label>
					<input type="text"
						id="globalNamePrefix"
						class="form-control"
						placeholder="My New Item" />
				</div>

			</div>
		</div>

		<div class="card shadow-sm">
			<div class="card-header d-flex justify-content-between align-items-center">
				<strong>Items</strong>
				<button type="button" class="btn btn-sm btn-outline-primary"
					id="btnAddRow">+ Add Item</button>
			</div>

			<div class="card-body p-0">
				<table class="table table-striped mb-0" id="cloneTable">
					<thead class="table-dark">
						<tr>
							<th>Original</th>
							<th>Icon</th>
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
								<td>
									${e.itemId}
									<input type="hidden" name="originalItemId" value="${e.itemId}" />
								</td>

								<td class="text-center">
									<c:choose>
										<c:when test="${not empty e.iconName}">
											<img
												src="${pageContext.request.contextPath}/master/equipment/icon?itemId=${e.itemId}"
												alt="${e.name}"
												style="width:40px; height:40px; object-fit:contain;" />
										</c:when>
										<c:otherwise>
											<span class="text-muted">-</span>
										</c:otherwise>
									</c:choose>
								</td>

								<td>
									<input type="number" class="form-control newItemId"
										name="newItemId" required />
								</td>

								<td>
									<input type="text" class="form-control itemName"
										name="name_${e.itemId}"
										value="${e.name}"
										data-original="${e.name}" />
								</td>

								<td>
									<select class="form-select typeSelect"
										name="typeId_${e.itemId}">
										<c:forEach items="${itemTypes}" var="t">
											<option value="${t.typeId}"
												${t.typeId == e.typeId ? 'selected' : ''}>
												${t.typeName}
											</option>
										</c:forEach>
									</select>
								</td>

								<td>
									<select class="form-select jobSelect"
										name="jobId_${e.itemId}">
										<c:forEach items="${jobs}" var="j">
											<option value="${j.id}" ${j.id == e.jobId ? 'selected' : ''}>
												${j.name}
											</option>
										</c:forEach>
									</select>
								</td>

								<td>
									<select class="form-select raritySelect"
										name="rarityId_${e.itemId}">
										<c:forEach items="${rarities}" var="r">
											<option value="${r.rarityId}"
												data-color="${r.color}"
												${r.rarityId == e.rarityId ? 'selected' : ''}>
												${r.rarityName}
											</option>
										</c:forEach>
									</select>
								</td>

								<td>
									<input type="number" class="form-control reqLvInput"
										name="requiredLevel_${e.itemId}"
										value="${e.requiredLevel}" />
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
					Clone Set
				</button>
			</div>
		</div>
	</form>
</c:if>

<script type="text/template" id="rowTemplate">
<tr>
	<td>
		NEW
		<input type="hidden" name="originalItemId" value="0"/>
	</td>
	<td class="text-center">
		<span class="text-muted">-</span>
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

<script>
	var APP_CTX = '${pageContext.request.contextPath}';
</script>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/master/equipment/equipment-clone-set.js"></script>
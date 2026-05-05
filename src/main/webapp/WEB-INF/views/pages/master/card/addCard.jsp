<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">
		<c:choose>
			<c:when test="${mode == 'EDIT'}">Edit Card</c:when>
			<c:otherwise>Add Card</c:otherwise>
		</c:choose>
	</h2>

	<a href="${pageContext.request.contextPath}/master/card/viewCard"
		class="btn btn-secondary">Back</a>
</div>

<div class="card shadow-sm">
	<div class="card-body">

		<form id="cardForm">

			<c:if test="${mode == 'EDIT'}">
				<input type="hidden" id="id" name="id" value="${card.id}" />
			</c:if>

			<div class="row">
				<div class="col-md-9">

					<div class="row">
						<div class="col-md-4 mb-3">
							<label class="form-label">Item Type</label> <select
								class="form-select" id="typeId" name="typeId" required>
								<option value="">-- Select Item Type --</option>
								<c:forEach items="${itemTypeList}" var="t">
									<option value="${t.typeId}"
										<c:if test="${mode == 'EDIT' && card.typeId == t.typeId}">selected</c:if>>
										${fn:escapeXml(t.typeName)}</option>
								</c:forEach>
							</select>
						</div>

						<div class="col-md-4 mb-3">
							<label class="form-label">Card Name</label> <select
								class="form-select" id="cardNameId" name="cardNameId" required>
								<option value="">-- Select Card Name --</option>
								<c:forEach items="${cardNameList}" var="cn">
									<option value="${cn.id}"
										data-icon-url="${pageContext.request.contextPath}/master/card/cardNameIcon?id=${cn.id}"
										<c:if test="${mode == 'EDIT' && card.cardNameId == cn.id}">selected</c:if>>
										${fn:escapeXml(cn.name)}</option>
								</c:forEach>
							</select>
						</div>

						<div class="col-md-4 mb-3">
							<label class="form-label">Level</label> <select
								class="form-select" id="patchLevelId" name="patchLevelId"
								required>
								<option value="">-- Select Level --</option>
								<c:forEach items="${levelList}" var="l">
									<option value="${l.id}"
										<c:if test="${mode == 'EDIT' && card.cardLevelId == l.id}">selected</c:if>>
										${l.level}</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="row">
						<div class="col-md-4 mb-3">
							<label class="form-label">Rarity</label> <select
								class="form-select" id="rarityId" name="rarityId" required>
								<option value="">-- Select Rarity --</option>
								<c:forEach items="${rarityList}" var="r">
									<option value="${r.rarityId}"
										<c:if test="${mode == 'EDIT' && card.rarityId == r.rarityId}">selected</c:if>>
										${fn:escapeXml(r.rarityName)}</option>
								</c:forEach>
							</select>
						</div>

						<div class="col-md-4 mb-3">
							<label class="form-label">Slot Number</label> <input type="text"
								class="form-control number-only-input" id="slotNumber"
								name="slotNumber" value="${card.slotNumber}"
								placeholder="Slot Number" maxlength="10" required />
						</div>
					</div>

					<hr />

					<div class="d-flex align-items-center mb-2">
						<h5 class="mb-0">Stats</h5>
						<button type="button" class="btn btn-sm btn-success ms-auto"
							id="btnAddStat">+ Add Stat</button>
					</div>

					<table class="table table-bordered" id="cardStatTable">
						<thead class="table-light">
							<tr>
								<th>Stat</th>
								<th style="width: 160px;">Min Value</th>
								<th style="width: 160px;">Max Value</th>
								<th style="width: 120px;">Is %</th>
								<th style="width: 80px;">Action</th>
							</tr>
						</thead>

						<tbody>
							<c:choose>
								<c:when test="${mode == 'EDIT' && not empty card.statList}">
									<c:forEach items="${card.statList}" var="cs">
										<tr class="stat-row">
											<td><select class="form-select stat-id" name="statId"
												required>
													<option value="">-- Select Stat --</option>
													<c:forEach items="${statList}" var="s">
														<option value="${s.statId}"
															<c:if test="${cs.statId == s.statId}">selected</c:if>>
															${fn:escapeXml(s.displayName)}</option>
													</c:forEach>
											</select></td>

											<td><input type="text"
												class="form-control decimal-only-input value-min"
												name="valueMin" value="${cs.valueMin}" maxlength="20"
												required /></td>

											<td><input type="text"
												class="form-control decimal-only-input value-max"
												name="valueMax" value="${cs.valueMax}" maxlength="20"
												required /></td>

											<td><select class="form-select is-percentage"
												name="isPercentage">
													<option value="0"
														<c:if test="${cs.isPercentage == 0}">selected</c:if>>
														No</option>
													<option value="1"
														<c:if test="${cs.isPercentage == 1}">selected</c:if>>
														Yes</option>
											</select></td>

											<td class="text-center">
												<button type="button"
													class="btn btn-sm btn-danger btn-remove-stat">X</button>
											</td>
										</tr>
									</c:forEach>
								</c:when>

								<c:otherwise>
									<tr class="stat-row">
										<td><select class="form-select stat-id" name="statId"
											required>
												<option value="">-- Select Stat --</option>
												<c:forEach items="${statList}" var="s">
													<option value="${s.statId}">
														${fn:escapeXml(s.displayName)}</option>
												</c:forEach>
										</select></td>

										<td><input type="text"
											class="form-control decimal-only-input value-min"
											name="valueMin" maxlength="20" required /></td>

										<td><input type="text"
											class="form-control decimal-only-input value-max"
											name="valueMax" maxlength="20" required /></td>

										<td><select class="form-select is-percentage"
											name="isPercentage">
												<option value="0">No</option>
												<option value="1">Yes</option>
										</select></td>

										<td class="text-center">
											<button type="button"
												class="btn btn-sm btn-danger btn-remove-stat">X</button>
										</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>

					<button type="submit" class="btn btn-primary" id="btnSave">
						Save</button>
				</div>

				<div class="col-md-3">
					<label class="form-label d-block">Icon Preview</label>

					<div class="border rounded p-2 text-center"
						style="min-height: 120px;">
						<img id="cardNamePreview" alt="preview"
							style="display: none; width: 90px; height: 90px; object-fit: contain; border-radius: 8px;"
							onerror="this.style.display='none';" />
					</div>

					<div class="form-text mt-2">แสดง icon ของ Card Name ที่เลือก
					</div>
				</div>
			</div>

		</form>
	</div>
</div>

<table style="display: none;">
	<tbody>
		<tr id="statRowTemplate" class="stat-row">
			<td><select class="form-select stat-id" name="statId" required>
					<option value="">-- Select Stat --</option>
					<c:forEach items="${statList}" var="s">
						<option value="${s.statId}">
							${fn:escapeXml(s.displayName)}</option>
					</c:forEach>
			</select></td>

			<td><input type="text"
				class="form-control decimal-only-input value-min" name="valueMin"
				maxlength="20" required /></td>

			<td><input type="text"
				class="form-control decimal-only-input value-max" name="valueMax"
				maxlength="20" required /></td>

			<td><select class="form-select is-percentage"
				name="isPercentage">
					<option value="0">No</option>
					<option value="1">Yes</option>
			</select></td>

			<td class="text-center">
				<button type="button" class="btn btn-sm btn-danger btn-remove-stat">
					X</button>
			</td>
		</tr>
	</tbody>
</table>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
	window.APP_CTX = "${pageContext.request.contextPath}";
</script>

<script
	src="${pageContext.request.contextPath}/resources/js/master/card/addCard.js"></script>
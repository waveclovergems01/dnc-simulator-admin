<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="d-flex justify-content-between align-items-center mb-3">

	<h2 class="mb-0">
		<c:choose>
			<c:when test="${mode == 'EDIT'}">Edit Plate</c:when>
			<c:otherwise>Add Plate</c:otherwise>
		</c:choose>
	</h2>

	<a href="${pageContext.request.contextPath}/master/plate/viewPlate"
		class="btn btn-secondary"> Back </a>

</div>

<div class="card shadow-sm">
	<div class="card-body">

		<form id="plateForm">

			<c:if test="${mode == 'EDIT'}">
				<input type="hidden" id="id" name="id" value="${plate.id}" />
			</c:if>

			<div class="row">
				<div class="col-md-9">

					<div class="row">

						<div class="col-md-4 mb-3">
							<label class="form-label">Item Type</label>
							<select class="form-select" id="typeId" name="typeId" required>
								<option value="">-- Select Item Type --</option>
								<c:forEach items="${itemTypeList}" var="t">
									<option value="${t.typeId}"
										<c:if test="${mode == 'EDIT' && plate.typeId == t.typeId}">selected</c:if>>
										${fn:escapeXml(t.typeName)}
									</option>
								</c:forEach>
							</select>
						</div>

						<div class="col-md-4 mb-3">
							<label class="form-label">Plate Name</label>
							<select class="form-select" id="plateNameId" name="plateNameId" required>
								<option value="">-- Select Plate Name --</option>
								<c:forEach items="${plateNameList}" var="pn">
									<option value="${pn.id}"
										data-icon-url="${pageContext.request.contextPath}/master/plate/plateNameIcon?id=${pn.id}"
										<c:if test="${mode == 'EDIT' && plate.plateNameId == pn.id}">selected</c:if>>
										${fn:escapeXml(pn.name)}
									</option>
								</c:forEach>
							</select>
						</div>

						<div class="col-md-4 mb-3">
							<label class="form-label">Patch Level</label>
							<select class="form-select" id="patchLevelId" name="patchLevelId" required>
								<option value="">-- Select Patch Level --</option>
								<c:forEach items="${levelList}" var="l">
									<option value="${l.id}"
										<c:if test="${mode == 'EDIT' && plate.plateLevelId == l.id}">selected</c:if>>
										${l.level}
									</option>
								</c:forEach>
							</select>
						</div>

					</div>

					<div class="row">
						<div class="col-md-4 mb-3">
							<label class="form-label">Rarity</label>
							<select class="form-select" id="rarityId" name="rarityId" required>
								<option value="">-- Select Rarity --</option>
								<c:forEach items="${rarityList}" var="r">
									<option value="${r.rarityId}"
										<c:if test="${mode == 'EDIT' && plate.rarityId == r.rarityId}">selected</c:if>>
										${fn:escapeXml(r.rarityName)}
									</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div id="statDropdownWrapper" class="mb-3" style="display: none;">
						<label class="form-label">Stat</label>
						<select class="form-select" id="statId" name="statId">
							<option value="">-- Select Stat --</option>
							<c:forEach items="${statList}" var="s">
								<option value="${s.statId}"
									<c:if test="${mode == 'EDIT' && plate.statId == s.statId}">selected</c:if>>
									${fn:escapeXml(s.displayName)}
								</option>
							</c:forEach>
						</select>

						<div class="row mt-3">
							<div class="col-md-6 mb-3">
								<label class="form-label">Value (Unit)</label>
								<input type="text" class="form-control number-only-input"
									id="statValue" name="statValue" value="${plate.statValue}"
									placeholder="Enter unit value" maxlength="20" />
							</div>

							<div class="col-md-6 mb-3">
								<label class="form-label">Value (%)</label>
								<input type="text" class="form-control decimal-only-input"
									id="statPercent" name="statPercent" value="${plate.statPercent}"
									placeholder="Enter percent value" maxlength="20" />
							</div>
						</div>
					</div>

					<button type="submit" class="btn btn-primary" id="btnSave">Save</button>

				</div>

				<div class="col-md-3">
					<label class="form-label d-block">Icon Preview</label>

					<div class="border rounded p-2 text-center" style="min-height: 120px;">
						<img id="plateNamePreview" alt="preview"
							style="display: none; width: 90px; height: 90px; object-fit: contain; border-radius: 8px;"
							onerror="this.style.display='none';" />
					</div>

					<div class="form-text mt-2">แสดง icon ของ Plate Name ที่เลือก</div>
				</div>

			</div>

		</form>

	</div>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
	window.APP_CTX = "${pageContext.request.contextPath}";
</script>

<script src="${pageContext.request.contextPath}/resources/js/master/plate/addPlate.js"></script>
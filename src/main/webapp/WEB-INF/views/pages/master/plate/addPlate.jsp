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

		<!-- ต้องเป็น multipart เพราะมี upload -->
		<form id="plateForm" enctype="multipart/form-data">

			<c:if test="${mode == 'EDIT'}">
				<input type="hidden" id="id" name="id" value="${plate.id}" />
			</c:if>

			<div class="row">
				<div class="col-md-9">

					<div class="mb-3">
						<label class="form-label">Plate Name</label> <input type="text"
							class="form-control plate-name-input" id="plateName"
							name="plateName" value="${fn:escapeXml(plate.plateName)}"
							placeholder="Plate Name" maxlength="200" required />
					</div>

					<div class="row">

						<div class="col-md-4 mb-3">
							<label class="form-label">Plate Type</label> <select
								class="form-select" id="plateTypeId" name="plateTypeId" required>
								<option value="">-- Select Type --</option>
								<c:forEach items="${typeList}" var="t">
									<option value="${t.id}"
										<c:if test="${mode=='EDIT' && plate.plateTypeId == t.id}">selected</c:if>>
										${fn:escapeXml(t.name)}</option>
								</c:forEach>
							</select>
						</div>

						<div class="col-md-4 mb-3">
							<label class="form-label">Plate Level</label> <select
								class="form-select" id="plateLevelId" name="plateLevelId"
								required>
								<option value="">-- Select Level --</option>
								<c:forEach items="${levelList}" var="l">
									<option value="${l.id}"
										<c:if test="${mode=='EDIT' && plate.plateLevelId == l.id}">selected</c:if>>
										${l.level}</option>
								</c:forEach>
							</select>
						</div>

						<div class="col-md-4 mb-3">
							<label class="form-label">Rarity</label> <select
								class="form-select" id="rarityId" name="rarityId" required>
								<option value="">-- Select Rarity --</option>
								<c:forEach items="${rarityList}" var="r">
									<option value="${r.rarityId}"
										<c:if test="${mode=='EDIT' && plate.rarityId == r.rarityId}">selected</c:if>>
										${fn:escapeXml(r.rarityName)}</option>
								</c:forEach>
							</select>
						</div>

					</div>

					<div class="mb-3">
						<label class="form-label">Icon (PNG/JPG/WEBP)</label> <input
							type="file" class="form-control" id="iconFile" name="iconFile"
							accept="image/png,image/jpeg,image/webp" />
						<div class="form-text">อัปโหลดใหม่เพื่อแทนรูปเดิม
							(ถ้าไม่เลือกไฟล์ จะคงรูปเดิมไว้)</div>
					</div>

					<button type="submit" class="btn btn-primary" id="btnSave">Save</button>

				</div>

				<!-- Preview / Current icon -->
				<div class="col-md-3">
					<label class="form-label d-block">Icon Preview</label>

					<div class="border rounded p-2 text-center"
						style="min-height: 120px;">
						<!-- รูปเดิม (EDIT) -->
						<c:if test="${mode == 'EDIT'}">
							<img id="currentIcon"
								src="${pageContext.request.contextPath}/master/plate/plateIcon?id=${plate.id}"
								alt="current icon"
								style="width: 90px; height: 90px; object-fit: contain; border-radius: 8px;"
								onerror="this.style.display='none';" />
						</c:if>

						<!-- รูปใหม่ที่เลือก -->
						<img id="iconPreview" alt="preview"
							style="display: none; width: 90px; height: 90px; object-fit: contain; border-radius: 8px;" />
					</div>

					<div class="form-text mt-2">ถ้าเลือกไฟล์ใหม่
						จะโชว์ที่นี่ทันที</div>
				</div>

			</div>

		</form>

	</div>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
	window.APP_CTX = "${pageContext.request.contextPath}";
</script>

<script
	src="${pageContext.request.contextPath}/resources/js/master/plate/addPlate.js"></script>
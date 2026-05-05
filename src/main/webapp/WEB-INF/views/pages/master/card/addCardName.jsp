<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">
		<c:choose>
			<c:when test="${mode == 'EDIT'}">Edit Card Name</c:when>
			<c:otherwise>Add Card Name</c:otherwise>
		</c:choose>
	</h2>

	<a href="${pageContext.request.contextPath}/master/card/viewCardName"
		class="btn btn-secondary"> Back </a>
</div>

<div class="card shadow-sm">
	<div class="card-body">

		<form id="cardNameForm" enctype="multipart/form-data">

			<c:if test="${mode == 'EDIT'}">
				<input type="hidden" id="id" name="id" value="${cardName.id}" />
			</c:if>

			<div class="row">
				<div class="col-md-9">

					<div class="mb-3">
						<label class="form-label">Card Name</label> <input type="text"
							class="form-control card-name-input" id="name" name="name"
							value="${fn:escapeXml(cardName.name)}" placeholder="Card Name"
							maxlength="200" required />
					</div>

					<div class="mb-3">
						<label class="form-label">Icon (PNG/JPG/WEBP)</label> <input
							type="file" class="form-control" id="iconFile" name="iconFile"
							accept="image/png,image/jpeg,image/webp" />

						<div class="form-text">อัปโหลดใหม่เพื่อแทนรูปเดิม
							ถ้าไม่เลือกไฟล์ จะคงรูปเดิมไว้</div>
					</div>

					<button type="submit" class="btn btn-primary" id="btnSave">
						Save</button>
				</div>

				<div class="col-md-3">
					<label class="form-label d-block">Icon Preview</label>

					<div class="border rounded p-2 text-center"
						style="min-height: 120px;">
						<c:if test="${mode == 'EDIT'}">
							<img id="currentIcon"
								src="${pageContext.request.contextPath}/master/card/cardNameIcon?id=${cardName.id}"
								alt="current icon"
								style="width: 90px; height: 90px; object-fit: contain; border-radius: 8px;"
								onerror="this.style.display='none';" />
						</c:if>

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
	src="${pageContext.request.contextPath}/resources/js/master/card/addCardName.js"></script>
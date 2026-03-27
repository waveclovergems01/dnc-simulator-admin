<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="d-flex justify-content-between align-items-center mb-3">

	<h2 class="mb-0">
		<c:choose>
			<c:when test="${mode == 'EDIT'}">Edit Plate 3rd Stat</c:when>
			<c:otherwise>Add Plate 3rd Stat</c:otherwise>
		</c:choose>
	</h2>

	<a href="${pageContext.request.contextPath}/master/plate/view3rdStat"
		class="btn btn-secondary"> Back </a>

</div>

<div class="card shadow-sm">
	<div class="card-body">

		<form id="thirdStatForm">

			<c:if test="${mode == 'EDIT'}">
				<input type="hidden" id="id" name="id" value="${thirdStat.id}" />
			</c:if>

			<div class="row">
				<div class="col-md-4 mb-3">
					<label class="form-label">Stat</label> <select class="form-select"
						id="statId" name="statId" required>
						<option value="">-- Select Stat --</option>
						<c:forEach items="${statList}" var="s">
							<option value="${s.statId}"
								<c:if test="${mode == 'EDIT' && thirdStat.statId == s.statId}">selected</c:if>>
								${fn:escapeXml(s.displayName)}</option>
						</c:forEach>
					</select>
				</div>

				<div class="col-md-4 mb-3">
					<label class="form-label">Rarity</label> <select
						class="form-select" id="rarityId" name="rarityId" required>
						<option value="">-- Select Rarity --</option>
						<c:forEach items="${rarityList}" var="r">
							<option value="${r.rarityId}"
								<c:if test="${mode == 'EDIT' && thirdStat.rarityId == r.rarityId}">selected</c:if>>
								${fn:escapeXml(r.rarityName)}</option>
						</c:forEach>
					</select>
				</div>

				<div class="col-md-4 mb-3">
					<label class="form-label">Patch Level</label> <select
						class="form-select" id="patchLevelId" name="patchLevelId" required>
						<option value="">-- Select Patch Level --</option>
						<c:forEach items="${levelList}" var="l">
							<option value="${l.id}"
								<c:if test="${mode == 'EDIT' && thirdStat.patchLevelId == l.id}">selected</c:if>>
								${l.level}</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<div class="row">
				<div class="col-md-4 mb-3">
					<label class="form-label">Value</label> <input type="text"
						class="form-control number-only-input" id="value" name="value"
						value="${thirdStat.value}" placeholder="Enter value"
						maxlength="20" required />
				</div>
			</div>

			<button type="submit" class="btn btn-primary" id="btnSave">Save</button>

		</form>

	</div>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
	window.APP_CTX = "${pageContext.request.contextPath}";
</script>

<script
	src="${pageContext.request.contextPath}/resources/js/master/plate/add3rdStat.js"></script>
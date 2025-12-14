<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<!-- ================= Page Header ================= -->
<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">
		<c:choose>
			<c:when test="${isAdd}">Add Rarity</c:when>
			<c:otherwise>Edit Rarity</c:otherwise>
		</c:choose>
	</h2>

	<a href="${pageContext.request.contextPath}/master/rarities"
		class="btn btn-secondary"> Back </a>
</div>

<form action="${pageContext.request.contextPath}/master/rarities/save"
	method="post">

	<input type="hidden" name="isAdd" value="${isAdd}" />

	<div class="card shadow-sm">
		<div class="card-body">

			<!-- ================= BASIC INFO ================= -->
			<h5 class="mb-3">Rarity Info</h5>

			<div class="mb-3 row">
				<label class="col-sm-2 col-form-label">Rarity ID</label>
				<div class="col-sm-4">
					<input type="number" name="rarityId" class="form-control"
						value="${rarity.rarityId}" <c:if test="${!isAdd}">readonly</c:if>
						required />
				</div>
			</div>

			<div class="mb-3 row">
				<label class="col-sm-2 col-form-label">Rarity Name</label>
				<div class="col-sm-6">
					<input type="text" name="rarityName" class="form-control"
						value="${rarity.rarityName}" required />
				</div>
			</div>

			<c:set var="rarityColor"
				value="${not empty rarity.color ? fn:trim(rarity.color) : '#000000'}" />


			<div class="mb-3 row">
				<label class="col-sm-2 col-form-label">Color</label>

				<div class="col-sm-4 d-flex align-items-center gap-3">

					<!-- Color Picker -->
					<input type="color" id="colorPicker"
						class="form-control form-control-color" value="${rarityColor}"
						title="Choose color" oninput="colorCode.value = this.value" />

					<!-- Hex Code -->
					<input type="text" id="colorCode" name="color" class="form-control"
						style="max-width: 150px;" value="${rarityColor}" readonly required />
				</div>
			</div>



			<hr />

			<!-- ================= CATEGORY RULE ================= -->
			<h5 class="mb-2">Allowed Categories</h5>
			<div class="row">
				<c:forEach var="c" items="${categories}">
					<div class="col-md-3">
						<div class="form-check">
							<input class="form-check-input" type="checkbox"
								name="categoryIds" value="${c.categoryId}"
								<c:if test="${selectedCategoryRarityIds.contains(c.categoryId)}">
									checked
								</c:if>>
							<label class="form-check-label"> ${c.categoryName} </label>
						</div>
					</div>
				</c:forEach>
			</div>

			<hr />

			<!-- ================= ITEM TYPE RULE ================= -->
			<h5 class="mb-2">Allowed Item Types</h5>
			<div class="row">
				<c:forEach var="t" items="${itemTypes}">
					<div class="col-md-3">
						<div class="form-check">
							<input class="form-check-input" type="checkbox" name="typeIds"
								value="${t.typeId}"
								<c:if test="${selectedItemTypeRarityIds.contains(t.typeId)}">
									checked
								</c:if>>
							<label class="form-check-label"> ${t.typeName} </label>
						</div>
					</div>
				</c:forEach>
			</div>

		</div>

		<div class="card-footer text-end">
			<button type="submit" class="btn btn-success">Save</button>
			<a href="${pageContext.request.contextPath}/master/rarities"
				class="btn btn-secondary ms-2"> Cancel </a>
		</div>
	</div>

</form>

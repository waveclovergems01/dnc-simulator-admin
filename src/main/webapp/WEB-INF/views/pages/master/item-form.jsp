<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="container-fluid">

	<div class="mb-3">
		<h3>
			<c:choose>
				<c:when test="${isAdd}">Add Item Type</c:when>
				<c:otherwise>Edit Item Type</c:otherwise>
			</c:choose>
		</h3>
	</div>

	<form action="${pageContext.request.contextPath}/master/items/save"
		method="post">

		<!-- hidden isAdd -->
		<input type="hidden" name="isAdd" value="${isAdd}" />

		<div class="card">
			<div class="card-body">

				<!-- TYPE ID -->
				<div class="mb-3 row">
					<label class="col-sm-2 col-form-label">Type ID</label>
					<div class="col-sm-4">
						<input type="number" name="typeId" class="form-control"
							value="${itemType.typeId}" <c:if test="${!isAdd}">readonly</c:if>
							required />
					</div>
				</div>

				<!-- TYPE NAME -->
				<div class="mb-3 row">
					<label class="col-sm-2 col-form-label">Type Name</label>
					<div class="col-sm-6">
						<input type="text" name="typeName" class="form-control"
							value="${itemType.typeName}" required />
					</div>
				</div>

				<!-- SLOT -->
				<div class="mb-3 row">
					<label class="col-sm-2 col-form-label">Slot</label>
					<div class="col-sm-4">
						<input type="text" name="slot" class="form-control"
							value="${itemType.slot}" placeholder="weapon / armor / accessory"
							required />
					</div>
				</div>

				<!-- CATEGORY -->
				<div class="mb-3 row">
					<label class="col-sm-2 col-form-label">Category ID</label>
					<div class="col-sm-4">
						<input type="number" name="categoryId" class="form-control"
							value="${itemType.categoryId}" required />
					</div>
				</div>

			</div>

			<div class="card-footer text-end">
				<a href="${pageContext.request.contextPath}/master/items"
					class="btn btn-secondary me-2"> Cancel </a>

				<button type="submit" class="btn btn-success">Save</button>
			</div>
		</div>

	</form>

</div>

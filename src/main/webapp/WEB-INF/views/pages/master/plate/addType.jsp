<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="container-fluid">

	<div class="row mb-3">
		<div class="col-md-6">
			<h3>
				<c:choose>
					<c:when test="${mode == 'EDIT'}">Edit Plate Type</c:when>
					<c:otherwise>Add Plate Type</c:otherwise>
				</c:choose>
			</h3>
		</div>
	</div>

	<div class="card">
		<div class="card-body">

			<form id="plateTypeForm" autocomplete="off">

				<!-- hidden id (มีเฉพาะ edit) -->
				<input type="hidden" id="typeId" value="${type.id}" />

				<div class="row mb-3">
					<label class="col-md-2 col-form-label">Type Name</label>

					<div class="col-md-6">
						<input type="text" id="typeName" class="form-control"
							maxlength="100" placeholder="e.g. Wind Enahancement Plate"
							required value="${fn:escapeXml(type.name)}">
						<small class="text-muted"> English letters and spaces
							only. Example: Wind Enahancement Plate </small>
					</div>
				</div>

				<div class="row">
					<div class="col-md-8">

						<button type="button" class="btn btn-success" id="btnSaveType">
							Save</button>

						<button type="button" class="btn btn-secondary" id="btnCancelType">
							Cancel</button>

						<!-- ปุ่ม Delete แสดงเฉพาะ Edit -->
						<c:if test="${mode == 'EDIT'}">
							<button type="button" class="btn btn-danger ms-2"
								id="btnDeleteType">Delete</button>
						</c:if>

					</div>
				</div>

			</form>

		</div>
	</div>

</div>

<script>
	window.APP_CTX = "${pageContext.request.contextPath}";
</script>
<script
	src="${pageContext.request.contextPath}/resources/js/master/plate/addType.js"></script>
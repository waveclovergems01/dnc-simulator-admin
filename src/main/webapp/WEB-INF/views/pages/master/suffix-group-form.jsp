<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2 class="mb-3">${isAdd ? 'Add' : 'Edit'} Suffix Group</h2>

<form
	action="${pageContext.request.contextPath}/master/suffix/group/save"
	method="post" class="card p-4 shadow-sm">

	<input type="hidden" name="isAdd" value="${isAdd}" />

	<!-- Group ID -->
	<div class="mb-3">
		<label class="form-label">Group ID</label> <input type="number"
			name="groupId" value="${group.groupId}" class="form-control"
			${!isAdd ? 'readonly' : ''} required />
	</div>

	<!-- Item Type -->
	<div class="mb-3">
		<label class="form-label">Item Type</label> <select name="itemTypeId"
			class="form-select" required>
			<option value="">-- Select Item Type --</option>

			<c:forEach items="${itemTypes}" var="it">
				<option value="${it.typeId}"
					${it.typeId == group.itemTypeId ? 'selected' : ''}>
					${it.typeId}&nbsp;-&nbsp;${it.typeName}</option>
			</c:forEach>
		</select>
	</div>

	<!-- Action Buttons -->
	<div class="d-flex justify-content-between mt-4">
		<a href="${pageContext.request.contextPath}/master/suffix"
			class="btn btn-secondary"> Back </a>

		<button type="submit" class="btn btn-success">Save</button>
	</div>
</form>

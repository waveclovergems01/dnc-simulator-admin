<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- ================= Page Header ================= -->
<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">
		<c:choose>
			<c:when test="${empty category.categoryId}">
				Add Category
			</c:when>
			<c:otherwise>
				Edit Category
			</c:otherwise>
		</c:choose>
	</h2>

	<a href="${pageContext.request.contextPath}/master/categories"
		class="btn btn-secondary"> Back </a>
</div>

<!-- ================= Category Form ================= -->
<form action="${pageContext.request.contextPath}/master/categories/save"
	method="post" class="card shadow-sm">

	<div class="card-body">
		<!-- hidden isAdd -->
		<input type="hidden" name="isAdd" value="${isAdd}" />

		<!-- Category ID -->
		<div class="mb-3">
			<label class="form-label">Category ID</label> <input type="number"
				name="categoryId" class="form-control"
				value="${category.categoryId}"
				<c:if test="${not empty category.categoryId}">readonly</c:if>
				required />
		</div>

		<!-- Category Name -->
		<div class="mb-3">
			<label class="form-label">Category Name</label> <input type="text"
				name="categoryName" class="form-control"
				value="${category.categoryName}" required />
		</div>

	</div>

	<div class="card-footer text-end">
		<button type="submit" class="btn btn-success">Save</button>
		<a href="${pageContext.request.contextPath}/master/categories"
			class="btn btn-secondary ms-2"> Cancel </a>
	</div>

</form>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<title>Stat Form</title>
<link rel="stylesheet"
	href="<c:url value='/resources/css/bootstrap.min.css'/>">
</head>

<body>

	<div class="container mt-4">
		<h3>Stat Form</h3>

		<form action="<c:url value='/master/stats/save'/>" method="post">
			<input type="hidden" name="isAdd" value="${isAdd}" />

			<!-- Stat ID -->
			<div class="mb-3">
				<label class="form-label">Stat ID</label> <input type="number"
					name="statId" class="form-control" value="${stat.statId}" required />
			</div>

			<!-- Stat Name -->
			<div class="mb-3">
				<label class="form-label">Stat Name</label> <input type="text"
					name="statName" class="form-control" value="${stat.statName}"
					required />
			</div>

			<!-- Display Name -->
			<div class="mb-3">
				<label class="form-label">Display Name</label> <input type="text"
					name="displayName" class="form-control" value="${stat.displayName}"
					required />
			</div>

			<!-- Stat Category ID -->
			<div class="mb-3">
				<label class="form-label">Category ID</label> <input type="number"
					name="statCatId" class="form-control" value="${stat.statCatId}"
					required />
			</div>

			<!-- Stat Category Name -->
			<div class="mb-3">
				<label class="form-label">Category Name</label> <input type="text"
					name="statCatName" class="form-control" value="${stat.statCatName}"
					required />
			</div>

			<!-- Is Percentage -->
			<div class="mb-4">
				<label class="form-label">Is Percentage</label> <select
					name="isPercentage" class="form-control">
					<option value="false"
						<c:if test="${!stat.percentage}">selected</c:if>>No</option>
					<option value="true"
						<c:if test="${stat.percentage}">selected</c:if>>Yes (%)</option>
				</select>
			</div>

			<!-- Submit -->
			<div class="mb-3">
				<button type="submit" class="btn btn-primary">Save</button>
				<a href="<c:url value='/master/stats'/>" class="btn btn-secondary">
					Cancel </a>
			</div>

		</form>
	</div>

</body>
</html>

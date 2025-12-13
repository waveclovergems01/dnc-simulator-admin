<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<title>Job Form</title>
<link rel="stylesheet"
	href="<c:url value='/resources/css/bootstrap.min.css'/>">
</head>

<body>

	<div class="container mt-4">
		<h3>Job Form</h3>

		<form action="<c:url value='/master/jobs/save'/>" method="post">
			<input type="hidden" name="isAdd" value="${isAdd}" />
			
			<!-- Job Id -->
			<div class="mb-3">
				<label class="form-label">Job Id</label> <input type="text"
					name="id" class="form-control" value="${job.id}" required />
			</div>

			<!-- Job Name -->
			<div class="mb-3">
				<label class="form-label">Job Name</label> <input type="text"
					name="name" class="form-control" value="${job.name}" required />
			</div>

			<!-- Class ID -->
			<div class="mb-3">
				<label class="form-label">Class ID</label> <input type="number"
					name="classId" class="form-control" value="${job.classId}" required />
			</div>

			<!-- Class Name -->
			<div class="mb-3">
				<label class="form-label">Class Name</label> <input type="text"
					name="className" class="form-control" value="${job.className}"
					required />
			</div>

			<!-- Required Level -->
			<div class="mb-3">
				<label class="form-label">Required Level</label> <input
					type="number" name="requiredLevel" class="form-control"
					value="${job.requiredLevel}" required />
			</div>

			<!-- Inherit Job -->
			<div class="mb-3">
				<label class="form-label">Inherit From</label> <select
					name="inherit" class="form-control">
					<option value="-1">-- None (Base Class) --</option>

					<c:forEach var="j" items="${allJobs}">
						<option value="${j.id}"
							<c:if test="${j.id == job.inherit}">selected</c:if>>
							${j.name}</option>
					</c:forEach>
				</select>
			</div>

			<!-- Next Classes -->
			<div class="mb-4">
				<label class="form-label">Next Classes</label>

				<c:set var="currentClassId" value="-1" />

				<c:forEach var="j" items="${allJobs}">

					<c:if test="${currentClassId != j.classId}">
						<c:set var="currentClassId" value="${j.classId}" />
						<hr />
						<h5 class="mt-3">${j.className}</h5>
					</c:if>

					<div class="form-check ms-3">
						<input class="form-check-input" type="checkbox"
							name="nextClassIds" value="${j.id}" id="next_${j.id}"
							<c:if test="${nextClassIds != null && nextClassIds.contains(j.id)}">
							   checked
						   </c:if> />

						<label class="form-check-label" for="next_${j.id}">
							${j.name} </label>
					</div>

				</c:forEach>
			</div>

			<!-- Submit -->
			<div class="mb-3">
				<button type="submit" class="btn btn-primary">Save</button>
				<a href="<c:url value='/master/jobs'/>" class="btn btn-secondary">Cancel</a>
			</div>

		</form>
	</div>

</body>
</html>

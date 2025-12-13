<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2 class="mb-3">Add Job</h2>

<form action="${pageContext.request.contextPath}/master/jobs/save"
	method="post" class="card shadow-sm p-4">

	<!-- ID -->
	<div class="mb-3">
		<label class="form-label">Job ID</label> <input type="number"
			name="id" class="form-control" required />
	</div>

	<!-- Name -->
	<div class="mb-3">
		<label class="form-label">Job Name</label> <input type="text"
			name="name" class="form-control" required />
	</div>

	<!-- Class ID -->
	<div class="mb-3">
		<label class="form-label">Class ID</label> <input type="number"
			name="classId" class="form-control" required />
	</div>

	<!-- Class Name -->
	<div class="mb-3">
		<label class="form-label">Class Name</label> <input type="text"
			name="className" class="form-control" required />
	</div>

	<!-- Inherit -->
	<div class="mb-3">
		<label class="form-label">Inherit</label> <input type="number"
			name="inherit" class="form-control" required />
	</div>

	<!-- Required Level -->
	<div class="mb-3">
		<label class="form-label">Required Level</label> <input type="number"
			name="requiredLevel" class="form-control" required />
	</div>

	<!-- Next Classes -->
	<div class="mb-3">
		<label class="form-label">Next Classes</label>
		<div class="row">
			<c:forEach items="${allJobs}" var="j">
				<div class="col-md-3">
					<div class="form-check">
						<input class="form-check-input" type="checkbox"
							name="nextClassIds" value="${j.id}" /> <label
							class="form-check-label"> ${j.name} </label>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>

	<div class="mt-4">
		<button type="submit" class="btn btn-success">Save</button>
		<a href="${pageContext.request.contextPath}/master/jobs"
			class="btn btn-secondary"> Cancel </a>
	</div>

</form>

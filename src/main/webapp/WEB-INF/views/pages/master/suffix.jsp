<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- ================= DataTables CSS ================= -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<!-- ================= Page Header ================= -->
<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">Master Suffix</h2>
</div>

<!-- ================= Suffix Types ================= -->
<div class="card mb-4 shadow-sm">
	<div
		class="card-header d-flex justify-content-between align-items-center">
		<strong>Suffix Types</strong> <a
			href="${pageContext.request.contextPath}/master/suffix/type/add"
			class="btn btn-primary btn-sm"> + Add Suffix </a>
	</div>

	<div class="card-body">
		<table id="suffixTypeTable"
			class="table table-striped table-hover w-100">
			<thead class="table-dark">
				<tr>
					<th>ID</th>
					<th>Name</th>
					<th style="width: 160px;">Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${suffixTypes}" var="s">
					<tr>
						<td>${s.suffixId}</td>
						<td>${s.suffixName}</td>
						<td class="text-center"><a
							href="${pageContext.request.contextPath}/master/suffix/type/edit?suffixId=${s.suffixId}"
							class="btn btn-sm btn-warning me-1"> Edit </a>

							<form
								action="${pageContext.request.contextPath}/master/suffix/type/delete"
								method="post" style="display: inline;"
								onsubmit="return confirm('Delete this suffix?');">
								<input type="hidden" name="suffixId" value="${s.suffixId}" />
								<button type="submit" class="btn btn-sm btn-danger">
									Delete</button>
							</form></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<!-- ================= Suffix Groups ================= -->
<div class="card shadow-sm">
	<div
		class="card-header d-flex justify-content-between align-items-center">
		<strong>Suffix Groups</strong> <a
			href="${pageContext.request.contextPath}/master/suffix/group/add"
			class="btn btn-primary btn-sm"> + Add Group </a>
	</div>

	<div class="card-body">
		<table id="suffixGroupTable"
			class="table table-striped table-hover w-100">
			<thead class="table-dark">
				<tr>
					<th>Group ID</th>
					<th>Item Type</th>
					<th style="width: 220px;">Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${groups}" var="g">
					<tr>
						<td>${g.groupId}</td>

						<td><c:forEach items="${itemTypes}" var="it">
								<c:if test="${it.typeId == g.itemTypeId}">
									<strong>${it.typeName}</strong>
									<c:if test="${not empty it.slot}">
										&nbsp;(${it.slot})
									</c:if>
									<br />
									<small class="text-muted"> ID: ${it.typeId} </small>
								</c:if>
							</c:forEach></td>

						<td class="text-center"><a
							href="${pageContext.request.contextPath}/master/suffix/group/edit?groupId=${g.groupId}"
							class="btn btn-sm btn-warning me-1"> Edit </a> <a
							href="${pageContext.request.contextPath}/master/suffix/group/items?groupId=${g.groupId}"
							class="btn btn-sm btn-info me-1"> Items </a>

							<form
								action="${pageContext.request.contextPath}/master/suffix/group/delete"
								method="post" style="display: inline;"
								onsubmit="return confirm('Delete this group?');">
								<input type="hidden" name="groupId" value="${g.groupId}" />
								<button type="submit" class="btn btn-sm btn-danger">
									Delete</button>
							</form></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<!-- ================= JS ================= -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/dataTables.bootstrap5.min.js"></script>

<script>
	$(document).ready(function() {

		$('#suffixTypeTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			searching : true,
			ordering : true,
			columnDefs : [ {
				orderable : false,
				targets : 2
			} ]
		});

		$('#suffixGroupTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			searching : true,
			ordering : true,
			columnDefs : [ {
				orderable : false,
				targets : 2
			} ]
		});
	});
</script>

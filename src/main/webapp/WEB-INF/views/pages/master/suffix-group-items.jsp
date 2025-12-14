<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- ================= DataTables CSS ================= -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<!-- ================= Page Header ================= -->
<div class="d-flex justify-content-between align-items-start mb-3">
	<div>
		<h2 class="mb-1">Suffix Group Items</h2>

		<!-- Item Type Info -->
		<c:forEach items="${itemTypes}" var="it">
			<c:if test="${it.typeId == group.itemTypeId}">
				<div class="text-muted">
					Item Type : <strong>${it.typeName}</strong>
					<c:if test="${not empty it.slot}">
						&nbsp;(${it.slot})
					</c:if>
					&nbsp;| ID: ${it.typeId}
				</div>
			</c:if>
		</c:forEach>

		<div class="text-muted">Group ID : ${group.groupId}</div>
	</div>

	<a href="${pageContext.request.contextPath}/master/suffix"
		class="btn btn-secondary"> Back </a>
</div>

<!-- ================================================= -->
<!-- ================= ADD / EDIT FORM =============== -->
<!-- ================================================= -->
<div class="card mb-4 shadow-sm">
	<div class="card-header">
		<strong id="formTitle">Add Suffix</strong>
	</div>

	<div class="card-body">
		<form id="suffixForm"
			action="${pageContext.request.contextPath}/master/suffix/group/item/save"
			method="post" class="row g-3">

			<input type="hidden" name="id" id="itemId" /> <input type="hidden"
				name="groupId" value="${group.groupId}" /> <input type="hidden"
				name="isAdd" id="isAdd" value="true" />

			<!-- Mode -->
			<div class="col-md-3">
				<label class="form-label">Mode</label> <select name="mode"
					id="modeSelect" class="form-select" required>
					<option value="normal">Normal</option>
					<option value="pvp">PvP</option>
				</select>
			</div>

			<!-- Suffix -->
			<div class="col-md-6">
				<label class="form-label">Suffix</label> <select name="suffixId"
					id="suffixSelect" class="form-select" required>
					<option value="">-- Select Suffix --</option>

					<!-- Filter: ไม่แสดง suffix ที่ถูกใช้แล้ว -->
					<c:forEach items="${suffixTypes}" var="s">
						<c:set var="used" value="false" />
						<c:forEach items="${items}" var="i">
							<c:if test="${i.suffixId == s.suffixId}">
								<c:set var="used" value="true" />
							</c:if>
						</c:forEach>

						<c:if test="${!used}">
							<option value="${s.suffixId}">${s.suffixName} [ID:
								${s.suffixId}]</option>
						</c:if>
					</c:forEach>
				</select>
			</div>

			<!-- Submit -->
			<div class="col-md-3 d-flex align-items-end">
				<button type="submit" class="btn btn-success w-100">Save</button>
			</div>
		</form>
	</div>
</div>

<!-- ================================================= -->
<!-- ================= NORMAL MODE =================== -->
<!-- ================================================= -->
<div class="card mb-4 shadow-sm">
	<div class="card-header">
		<strong>Normal Mode</strong>
	</div>

	<div class="card-body">
		<table id="normalTable" class="table table-striped table-hover w-100">
			<thead class="table-dark">
				<tr>
					<th>Suffix</th>
					<th style="width: 180px;">Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${items}" var="item">
					<c:if test="${item.mode == 'normal'}">
						<tr>
							<td><c:forEach items="${suffixTypes}" var="s">
									<c:if test="${s.suffixId == item.suffixId}">
										${s.suffixName}
										<small class="text-muted"> (ID: ${s.suffixId}) </small>
									</c:if>
								</c:forEach></td>
							<td class="text-center">
								<button type="button" class="btn btn-sm btn-warning me-1"
									onclick="editItem('${item.id}', '${item.suffixId}', 'normal')">
									Edit</button>

								<form
									action="${pageContext.request.contextPath}/master/suffix/group/item/delete"
									method="post" style="display: inline;"
									onsubmit="return confirm('Delete this suffix?');">
									<input type="hidden" name="id" value="${item.id}" /> <input
										type="hidden" name="groupId" value="${group.groupId}" />
									<button type="submit" class="btn btn-sm btn-danger">
										Delete</button>
								</form>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<!-- ================================================= -->
<!-- ================= PVP MODE ====================== -->
<!-- ================================================= -->
<div class="card shadow-sm">
	<div class="card-header">
		<strong>PvP Mode</strong>
	</div>

	<div class="card-body">
		<table id="pvpTable" class="table table-striped table-hover w-100">
			<thead class="table-dark">
				<tr>
					<th>Suffix</th>
					<th style="width: 180px;">Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${items}" var="item">
					<c:if test="${item.mode == 'pvp'}">
						<tr>
							<td><c:forEach items="${suffixTypes}" var="s">
									<c:if test="${s.suffixId == item.suffixId}">
										${s.suffixName}
										<small class="text-muted"> (ID: ${s.suffixId}) </small>
									</c:if>
								</c:forEach></td>
							<td class="text-center">
								<button type="button" class="btn btn-sm btn-warning me-1"
									onclick="editItem('${item.id}', '${item.suffixId}', 'pvp')">
									Edit</button>

								<form
									action="${pageContext.request.contextPath}/master/suffix/group/item/delete"
									method="post" style="display: inline;"
									onsubmit="return confirm('Delete this suffix?');">
									<input type="hidden" name="id" value="${item.id}" /> <input
										type="hidden" name="groupId" value="${group.groupId}" />
									<button type="submit" class="btn btn-sm btn-danger">
										Delete</button>
								</form>
							</td>
						</tr>
					</c:if>
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

		$('#normalTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			searching : true,
			ordering : true,
			columnDefs : [ {
				orderable : false,
				targets : 1
			} ]
		});

		$('#pvpTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			searching : true,
			ordering : true,
			columnDefs : [ {
				orderable : false,
				targets : 1
			} ]
		});
	});

	function editItem(id, suffixId, mode) {
		$('#formTitle').text('Edit Suffix');
		$('#itemId').val(id);
		$('#isAdd').val('false');
		$('#modeSelect').val(mode);
		$('#suffixSelect').val(suffixId);
	}
</script>

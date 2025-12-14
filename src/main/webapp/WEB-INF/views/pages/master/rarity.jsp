<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<div class="d-flex justify-content-between align-items-center mb-3">
	<h2 class="mb-0">Master Rarities</h2>

	<a href="${pageContext.request.contextPath}/master/rarities/add"
		class="btn btn-primary"> + Add Rarity </a>
</div>

<table id="rarityTable"
	class="table table-striped table-hover shadow-sm w-100">
	<thead class="table-dark">
		<tr>
			<th style="width: 80px;">ID</th>
			<th style="width: 160px;">Name</th>
			<th style="width: 120px;">Color</th>
			<th>Allowed Categories</th>
			<th>Allowed Item Types</th>
			<th style="width: 160px;">Action</th>
		</tr>
	</thead>

	<tbody>
		<c:forEach var="r" items="${rarities}">
			<tr>
				<td>${r.rarityId}</td>
				<td>${r.rarityName}</td>

				<!-- COLOR -->
				<td><span class="badge" style="background:${r.color};">
						${r.color} </span></td>

				<!-- CATEGORIES -->
				<td><c:choose>
						<c:when test="${empty categoriesByRarity[r.rarityId]}">
							<span class="text-muted">–</span>
						</c:when>
						<c:otherwise>
							<c:forEach var="c" items="${categoriesByRarity[r.rarityId]}">
								<span class="badge bg-secondary me-1 mb-1">
									${c.categoryName} </span>
							</c:forEach>
						</c:otherwise>
					</c:choose></td>

				<!-- ITEM TYPES -->
				<td><c:choose>
						<c:when test="${empty itemTypesByRarity[r.rarityId]}">
							<span class="text-muted">–</span>
						</c:when>
						<c:otherwise>
							<c:forEach var="t" items="${itemTypesByRarity[r.rarityId]}">
								<span class="badge bg-info text-dark me-1 mb-1">
									${t.typeName} </span>
							</c:forEach>
						</c:otherwise>
					</c:choose></td>

				<!-- ACTION -->
				<td class="text-center"><a
					href="${pageContext.request.contextPath}/master/rarities/edit?id=${r.rarityId}"
					class="btn btn-sm btn-warning me-1"> Edit </a>

					<form
						action="${pageContext.request.contextPath}/master/rarities/delete"
						method="post" style="display: inline;"
						onsubmit="return confirm('Delete this rarity?');">

						<input type="hidden" name="id" value="${r.rarityId}" />

						<button type="submit" class="btn btn-sm btn-danger">
							Delete</button>
					</form></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/dataTables.bootstrap5.min.js"></script>

<script>
	$(function() {
		$('#rarityTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			columnDefs : [ {
				orderable : false,
				targets : [ 3, 4, 5 ]
			} ]
		});
	});
</script>

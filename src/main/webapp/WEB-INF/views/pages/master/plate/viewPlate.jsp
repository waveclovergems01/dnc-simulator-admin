<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<div class="d-flex align-items-center mb-3">
	<h2 class="mb-0">Plate</h2>

	<div class="ms-auto d-flex gap-2">
		<a href="${pageContext.request.contextPath}/master/plate/addPlate"
			class="btn btn-primary"> + Add Plate </a> <a
			href="${pageContext.request.contextPath}/master/plate/viewType"
			class="btn btn-primary"> >> Plate Type </a>
			<a
			href="${pageContext.request.contextPath}/master/plate/viewType"
			class="btn btn-primary"> >> Plate Name </a>
	</div>
</div>

<table id="plateTable"
	class="table table-striped table-hover shadow-sm w-100">

	<thead class="table-dark">
		<tr>
			<th style="width: 80px;">ID</th>
			<th style="width: 90px;">Icon</th>
			<th>Plate Name</th>
			<th>Type</th>
			<th style="width: 100px;">Level</th>
			<th style="width: 140px;">Rarity</th>
			<th style="width: 180px;">Action</th>
		</tr>
	</thead>

	<tbody>
		<c:if test="${not empty plateList}">
			<c:forEach items="${plateList}" var="p">
				<tr>
					<td>${p.id}</td>

					<td class="text-center"><img
						src="${pageContext.request.contextPath}/master/plate/plateIcon?id=${p.id}"
						alt="icon" class="plate-icon-img"
						style="width: 40px; height: 40px; object-fit: contain; border-radius: 6px;"
						onerror="this.style.display='none';" /></td>

					<td>${fn:escapeXml(p.plateName)}</td>

					<td>${fn:escapeXml(p.typeName)}</td>

					<td>${p.level}</td>

					<td><span class="badge"
						style="background:${fn:escapeXml(p.color)};">
							${fn:escapeXml(p.rarityName)} </span></td>

					<td class="text-center"><a
						href="${pageContext.request.contextPath}/master/plate/editPlate?id=${p.id}"
						class="btn btn-sm btn-warning me-1"> Edit </a>

						<form
							action="${pageContext.request.contextPath}/master/plate/deletePlate"
							method="post" style="display: inline;"
							onsubmit="return confirm('Are you sure you want to delete this plate?');">
							<input type="hidden" name="id" value="${p.id}" />
							<button type="submit" class="btn btn-sm btn-danger">Delete</button>
						</form></td>

				</tr>
			</c:forEach>
		</c:if>
	</tbody>

</table>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.8/js/dataTables.bootstrap5.min.js"></script>

<script>
	window.APP_CTX = "${pageContext.request.contextPath}";
</script>

<script
	src="${pageContext.request.contextPath}/resources/js/master/plate/viewPlate.js"></script>
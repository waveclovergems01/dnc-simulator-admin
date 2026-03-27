<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<div class="d-flex align-items-center mb-3">
	<h2 class="mb-0">Plate 3rd Stat</h2>

	<div class="ms-auto d-flex gap-2">
		<a href="${pageContext.request.contextPath}/master/plate/add3rdStat"
			class="btn btn-primary"> + Add 3rd Stat </a> <a
			href="${pageContext.request.contextPath}/master/plate/viewPlate"
			class="btn btn-secondary"> Back To Plate </a>
	</div>
</div>

<table id="thirdStatTable"
	class="table table-striped table-hover shadow-sm w-100">

	<thead class="table-dark">
		<tr>
			<th style="width: 80px;">ID</th>
			<th>Stat</th>
			<th style="width: 140px;">Rarity</th>
			<th style="width: 120px;">Patch Level</th>
			<th style="width: 120px;">Value</th>
			<th style="width: 180px;">Action</th>
		</tr>
	</thead>

	<tbody>
		<c:if test="${not empty thirdStatList}">
			<c:forEach items="${thirdStatList}" var="x">
				<tr>
					<td>${x.id}</td>

					<td>${fn:escapeXml(x.statDisplayName)}</td>

					<td><span class="badge"
						style="background:${fn:escapeXml(x.color)};">
							${fn:escapeXml(x.rarityName)} </span></td>

					<td>${x.level}</td>

					<td>${x.value}</td>

					<td class="text-center"><a
						href="${pageContext.request.contextPath}/master/plate/edit3rdStat?id=${x.id}"
						class="btn btn-sm btn-warning me-1"> Edit </a>

						<form
							action="${pageContext.request.contextPath}/master/plate/delete3rdStat"
							method="post" style="display: inline;"
							onsubmit="return confirm('Are you sure you want to delete this 3rd stat?');">
							<input type="hidden" name="id" value="${x.id}" />
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
	src="${pageContext.request.contextPath}/resources/js/master/plate/view3rdStat.js"></script>
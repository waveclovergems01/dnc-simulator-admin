<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<div class="d-flex align-items-center mb-3">
	<h2 class="mb-0">Plate</h2>

	<div class="ms-auto d-flex gap-2">
		<a href="${pageContext.request.contextPath}/master/plate/addPlate"
			class="btn btn-primary"> + Add Plate </a>
		<a href="${pageContext.request.contextPath}/master/plate/viewPlateName"
			class="btn btn-primary"> >> Plate Name </a>
		<a href="${pageContext.request.contextPath}/master/plate/view3rdStat"
			class="btn btn-primary"> >> Plate 3rd Stat </a>
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
			<th style="width: 220px;">Stat</th>
			<th style="width: 180px;">Action</th>
		</tr>
	</thead>

	<tbody>
		<c:if test="${not empty plateList}">
			<c:forEach items="${plateList}" var="p">
				<tr>
					<td>${p.id}</td>

					<td class="text-center">
						<div
							style="display: inline-flex; align-items: center; justify-content: center;
							width: 48px; height: 48px; border: 5px solid ${fn:escapeXml(p.color)};
							border-radius: 8px; padding: 3px; background: #fff;">
							<img
								src="${pageContext.request.contextPath}/master/plate/plateIcon?id=${p.id}"
								alt="icon" class="plate-icon-img"
								style="width: 40px; height: 40px; object-fit: contain; border-radius: 6px;"
								onerror="this.style.display='none';" />
						</div>
					</td>

					<td>${fn:escapeXml(p.plateName)}</td>

					<td>${fn:escapeXml(p.itemTypeName)}</td>

					<td>${p.level}</td>

					<td>
						<span class="badge" style="background:${fn:escapeXml(p.color)};">
							${fn:escapeXml(p.rarityName)}
						</span>
					</td>

					<td>
						<c:if test="${not empty p.statDisplayName}">
							<div>${fn:escapeXml(p.statDisplayName)}</div>
						</c:if>

						<c:if test="${p.statValue != null}">
							<div>${p.statValue}</div>
						</c:if>

						<c:if test="${p.statPercent != null}">
							<div>${p.statPercent}%</div>
						</c:if>
					</td>

					<td class="text-center">
						<a
							href="${pageContext.request.contextPath}/master/plate/editPlate?id=${p.id}"
							class="btn btn-sm btn-warning me-1"> Edit </a>

						<form
							action="${pageContext.request.contextPath}/master/plate/deletePlate"
							method="post" style="display: inline;" onsubmit="return false;">
							<input type="hidden" name="id" value="${p.id}" />
							<button type="button"
								class="btn btn-sm btn-danger btn-delete-plate" data-id="${p.id}">Delete</button>
						</form>
					</td>

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
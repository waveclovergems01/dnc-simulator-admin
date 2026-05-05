<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<style>
.th-magic {
	background: #1EFF00 !important;
	color: #000 !important;
}

.th-rare {
	background: #0070FF !important;
	color: #fff !important;
}

.th-epic {
	background: #FF8000 !important;
	color: #000 !important;
}

.th-unique {
	background: #A335EE !important;
	color: #fff !important;
}

.th-legendary {
	background: #FF0000 !important;
	color: #fff !important;
}

.td-magic {
	background: rgba(30, 255, 0, 0.12) !important;
}

.td-rare {
	background: rgba(0, 112, 255, 0.12) !important;
}

.td-epic {
	background: rgba(255, 128, 0, 0.12) !important;
}

.td-unique {
	background: rgba(163, 53, 238, 0.12) !important;
}

.td-legendary {
	background: rgba(255, 0, 0, 0.12) !important;
}

.stat-line {
	padding: 2px 0;
	white-space: nowrap;
}

.text-center {
	text-align: center;
}
</style>

<div class="d-flex align-items-center mb-3">
	<h2 class="mb-0">Card</h2>

	<div class="ms-auto d-flex gap-2">
		<a href="${pageContext.request.contextPath}/master/card/addCard"
			class="btn btn-primary"> + Add Card </a> <a
			href="${pageContext.request.contextPath}/master/card/viewCardName"
			class="btn btn-primary"> >> Card Name </a>
	</div>
</div>

<table id="cardTable"
	class="table table-striped table-hover shadow-sm w-100">
	<thead class="table-dark">
		<tr>
			<th style="width: 80px;">ID</th>
			<th style="width: 90px;">Icon</th>
			<th>Card Name</th>
			<th>Type</th>
			<th style="width: 100px;">Level</th>
			<th style="width: 100px;">Slot</th>
			<th style="width: 140px;">Rarity</th>
			<th style="width: 300px;">Stats</th>
			<th style="width: 180px;">Action</th>
		</tr>
	</thead>

	<tbody>
		<c:forEach items="${cardList}" var="p">
			<tr>
				<td>${p.id}</td>

				<td class="text-center">
					<div
						style="display:inline-flex;align-items:center;justify-content:center;width:48px;height:48px;border:5px solid ${fn:escapeXml(p.color)};border-radius:8px;padding:3px;background:#fff;">
						<img
							src="${pageContext.request.contextPath}/master/card/cardIcon?id=${p.id}"
							alt="icon"
							style="width: 40px; height: 40px; object-fit: contain; border-radius: 6px;"
							onerror="this.style.display='none';" />
					</div>
				</td>

				<td>${fn:escapeXml(p.cardName)}</td>
				<td>${fn:escapeXml(p.itemTypeName)}</td>
				<td>${p.level}</td>
				<td>${p.slotNumber}</td>

				<td><span class="badge"
					style="background:${fn:escapeXml(p.color)};">
						${fn:escapeXml(p.rarityName)} </span></td>

				<td><c:forEach items="${p.statList}" var="s">
						<div class="stat-line">
							${fn:escapeXml(s.statDisplayName)} : ${s.valueMin}
							<c:if test="${s.valueMin != s.valueMax}"> - ${s.valueMax}</c:if>
							<c:if test="${s.isPercentage == 1}">%</c:if>
						</div>
					</c:forEach></td>

				<td class="text-center"><a
					href="${pageContext.request.contextPath}/master/card/editCard?id=${p.id}"
					class="btn btn-sm btn-warning me-1">Edit</a>

					<button type="button" class="btn btn-sm btn-danger btn-delete-card"
						data-id="${p.id}">Delete</button></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<hr class="my-4" />

<h3 class="mb-3">Card Summary</h3>

<table id="cardSummaryTable"
	class="table table-striped table-hover shadow-sm w-100">

	<thead>
		<tr>
			<th style="width: 90px;">Slot</th>
			<th style="width: 90px;">Level</th>
			<th style="width: 90px;">Icon</th>
			<th>Card Name</th>
			<th class="th-magic">Magic</th>
			<th class="th-rare">Rare</th>
			<th class="th-epic">Epic</th>
			<th class="th-unique">Unique</th>
			<th class="th-legendary">Legendary</th>
		</tr>
	</thead>

	<tbody>
		<c:forEach items="${cardList}" var="p" varStatus="st">
			<c:if
				test="${st.first || cardList[st.index - 1].cardNameId != p.cardNameId}">
				<tr>
					<td>${p.slotNumber}</td>
					<td>${p.level}</td>

					<td class="text-center"><img
						src="${pageContext.request.contextPath}/master/card/cardIcon?id=${p.id}"
						alt="icon"
						style="width: 40px; height: 40px; object-fit: contain; border-radius: 6px;"
						onerror="this.style.display='none';" /></td>

					<td>${fn:escapeXml(p.cardName)}</td>

					<td class="td-magic"><c:forEach items="${cardList}" var="x">
							<c:if
								test="${x.cardNameId == p.cardNameId && x.rarityName == 'Magic'}">
								<c:forEach items="${x.statList}" var="s">
									<div class="stat-line">
										${fn:escapeXml(s.statDisplayName)}: ${s.valueMin}
										<c:if test="${s.valueMin != s.valueMax}"> - ${s.valueMax}</c:if>
										<c:if test="${s.isPercentage == 1}">%</c:if>
									</div>
								</c:forEach>
							</c:if>
						</c:forEach></td>

					<td class="td-rare"><c:forEach items="${cardList}" var="x">
							<c:if
								test="${x.cardNameId == p.cardNameId && x.rarityName == 'Rare'}">
								<c:forEach items="${x.statList}" var="s">
									<div class="stat-line">
										${fn:escapeXml(s.statDisplayName)}: ${s.valueMin}
										<c:if test="${s.valueMin != s.valueMax}"> - ${s.valueMax}</c:if>
										<c:if test="${s.isPercentage == 1}">%</c:if>
									</div>
								</c:forEach>
							</c:if>
						</c:forEach></td>

					<td class="td-epic"><c:forEach items="${cardList}" var="x">
							<c:if
								test="${x.cardNameId == p.cardNameId && x.rarityName == 'Epic'}">
								<c:forEach items="${x.statList}" var="s">
									<div class="stat-line">
										${fn:escapeXml(s.statDisplayName)}: ${s.valueMin}
										<c:if test="${s.valueMin != s.valueMax}"> - ${s.valueMax}</c:if>
										<c:if test="${s.isPercentage == 1}">%</c:if>
									</div>
								</c:forEach>
							</c:if>
						</c:forEach></td>

					<td class="td-unique"><c:forEach items="${cardList}" var="x">
							<c:if
								test="${x.cardNameId == p.cardNameId && x.rarityName == 'Unique'}">
								<c:forEach items="${x.statList}" var="s">
									<div class="stat-line">
										${fn:escapeXml(s.statDisplayName)}: ${s.valueMin}
										<c:if test="${s.valueMin != s.valueMax}"> - ${s.valueMax}</c:if>
										<c:if test="${s.isPercentage == 1}">%</c:if>
									</div>
								</c:forEach>
							</c:if>
						</c:forEach></td>

					<td class="td-legendary"><c:forEach items="${cardList}"
							var="x">
							<c:if
								test="${x.cardNameId == p.cardNameId && x.rarityName == 'Legendary'}">
								<c:forEach items="${x.statList}" var="s">
									<div class="stat-line">
										${fn:escapeXml(s.statDisplayName)}: ${s.valueMin}
										<c:if test="${s.valueMin != s.valueMax}"> - ${s.valueMax}</c:if>
										<c:if test="${s.isPercentage == 1}">%</c:if>
									</div>
								</c:forEach>
							</c:if>
						</c:forEach></td>
				</tr>
			</c:if>
		</c:forEach>
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
	src="${pageContext.request.contextPath}/resources/js/master/card/viewCard.js"></script>
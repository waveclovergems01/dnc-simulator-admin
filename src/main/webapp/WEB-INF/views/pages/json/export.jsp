<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>JSON Export</h2>

<form method="post"
	action="${pageContext.request.contextPath}/json/export/run">

	<div style="margin-bottom: 10px;">
		<button type="submit">Export Selected</button>
	</div>

	<div style="margin-bottom: 10px;">
		<label> <input type="checkbox" id="checkAll" /> Check All /
			Uncheck All
		</label>
	</div>

	<hr />

	<div style="margin-bottom: 10px;">
		<strong>Export Image Components</strong>
	</div>

	<div style="margin-bottom: 15px;">
		<label style="margin-right: 20px;"> <input type="checkbox"
			name="imageComponents" value="EQUIPMENT" /> Equipment Images
		</label> 
		<label style="margin-right: 20px;"> <input type="checkbox"
			name="imageComponents" value="PLATE" /> Plate Images
		</label> 
		<label style="margin-right: 20px;"> <input type="checkbox"
			name="imageComponents" value="CARD" /> Card Images
		</label>
	</div>

	<table border="1" cellpadding="6" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th width="60">Select</th>
				<th>Name</th>
				<th>Export File</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${configs}" var="c">
				<tr>
					<td align="center"><input type="checkbox"
						class="export-checkbox" name="exportIds" value="${c.id}" /></td>
					<td>${c.name}</td>
					<td>${c.exportFileName}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</form>

<script>
	const checkAll = document.getElementById("checkAll");
	const checkboxes = document.querySelectorAll(".export-checkbox");

	checkAll.addEventListener("change", function() {
		checkboxes.forEach(function(cb) {
			cb.checked = checkAll.checked;
		});
	});

	checkboxes.forEach(function(cb) {
		cb.addEventListener("change", function() {
			checkAll.checked = Array.from(checkboxes).every(function(c) {
				return c.checked;
			});
		});
	});
</script>
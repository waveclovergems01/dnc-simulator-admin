<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="container-fluid">

	<div class="d-flex justify-content-between align-items-center mb-3">
		<h3 class="mb-0">Item Types</h3>

		<a href="${pageContext.request.contextPath}/master/items/add"
			class="btn btn-primary"> <i class="fa fa-plus"></i> Add Item Type
		</a>
	</div>

	<div class="card">
		<div class="card-body">

			<table id="itemTypeTable"
				class="table table-striped table-bordered datatable w-100">
				<thead>
					<tr class="text-center">
						<th style="width: 80px;">ID</th>
						<th>Name</th>
						<th>Slot</th>
						<th style="width: 120px;">Category</th>
						<th style="width: 160px;">Action</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="item" items="${items}">
						<tr>
							<td class="text-center">${item.typeId}</td>
							<td>${item.typeName}</td>
							<td>${item.slot}</td>
							<td class="text-center">${item.categoryId}</td>
							<td class="text-center"><a
								href="${pageContext.request.contextPath}/master/items/edit?id=${item.typeId}"
								class="btn btn-warning btn-sm"> Edit </a>

								<form
									action="${pageContext.request.contextPath}/master/items/delete"
									method="post" style="display: inline;"
									onsubmit="return confirm('Delete this item type?');">
									<input type="hidden" name="id" value="${item.typeId}" />
									<button type="submit" class="btn btn-danger btn-sm">
										Delete</button>
								</form></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</div>
	</div>

</div>

<script>
	$(document).ready(function() {

		// ป้องกัน init ซ้ำ
		if (!$.fn.DataTable.isDataTable('#itemTypeTable')) {
			$('#itemTypeTable').DataTable({
				pageLength : 10,
				order : [ [ 0, 'asc' ] ],
				columnDefs : [ {
					orderable : false,
					targets : 4
				} ]
			});
		}

	});
</script>

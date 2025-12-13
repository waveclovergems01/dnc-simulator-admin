<%@ page contentType="text/html;charset=UTF-8"%>

<div class="d-flex justify-content-between mb-3">
	<h2>Master Items</h2>
	<button class="btn btn-primary">+ Add Item</button>
</div>

<table class="table table-striped shadow-sm">
	<thead class="table-dark">
		<tr>
			<th>ID</th>
			<th>Item Name</th>
			<th>Type</th>
			<th>Rarity</th>
			<th width="160">Action</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>10001</td>
			<td>Sea Dragon Sword</td>
			<td>Weapon</td>
			<td>Epic</td>
			<td>
				<button class="btn btn-sm btn-warning">Edit</button>
				<button class="btn btn-sm btn-danger">Delete</button>
			</td>
		</tr>
	</tbody>
</table>

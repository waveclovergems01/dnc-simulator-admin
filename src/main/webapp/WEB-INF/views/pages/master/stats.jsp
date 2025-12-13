<%@ page contentType="text/html;charset=UTF-8"%>

<div class="d-flex justify-content-between mb-3">
	<h2>Master Stats</h2>
	<button class="btn btn-primary">+ Add Stat</button>
</div>

<table class="table table-bordered shadow-sm">
	<thead class="table-dark">
		<tr>
			<th>ID</th>
			<th>Stat Code</th>
			<th>Description</th>
			<th>Is Percentage</th>
			<th width="160">Action</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>1</td>
			<td>STR</td>
			<td>Strength</td>
			<td>No</td>
			<td>
				<button class="btn btn-sm btn-warning">Edit</button>
				<button class="btn btn-sm btn-danger">Delete</button>
			</td>
		</tr>
	</tbody>
</table>

<%@ page contentType="text/html;charset=UTF-8"%>

<div class="d-flex justify-content-between mb-3">
	<h2>Master Jobs</h2>
	<button class="btn btn-primary">+ Add Job</button>
</div>

<table class="table table-hover shadow-sm">
	<thead class="table-dark">
		<tr>
			<th>ID</th>
			<th>Job Name</th>
			<th>Base Class</th>
			<th>Required Level</th>
			<th width="160">Action</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>1</td>
			<td>Warrior</td>
			<td>Base</td>
			<td>1</td>
			<td>
				<button class="btn btn-sm btn-warning">Edit</button>
				<button class="btn btn-sm btn-danger">Delete</button>
			</td>
		</tr>
	</tbody>
</table>

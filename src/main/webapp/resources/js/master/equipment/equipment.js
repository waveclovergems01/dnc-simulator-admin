(function() {

	$(document).ready(function() {

		var table = $('#equipmentTable').DataTable({
			pageLength : 10,
			lengthChange : false,
			searching : true,
			ordering : true,
			info : true,
			columnDefs : [ {
				orderable : false,
				targets : [ 1, 9 ]
			} ]
		});

		$('#jobFilter').on('change', function() {
			table.column(4).search(this.value).draw();
		});

		$('#rarityFilter').on('change', function() {
			table.column(6).search(this.value).draw();
		});

		$('#setFilter').on('change', function() {
			table.column(8).search(this.value).draw();
		});

		$('#resetFilter').on('click', function() {
			$('#jobFilter').val('');
			$('#rarityFilter').val('');
			$('#setFilter').val('');
			table.search('').columns().search('').draw();
		});
	});

})();
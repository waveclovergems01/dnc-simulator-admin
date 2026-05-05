(function() {

	$(function() {

		if ($.fn.dataTable.isDataTable("#cardNameTable")) {
			$("#cardNameTable").DataTable().destroy();
		}

		$("#cardNameTable").DataTable({
			pageLength : 10,
			lengthChange : false,
			ordering : true,
			searching : true,
			info : true,
			autoWidth : false,
			language : {
				emptyTable : "No data"
			},
			columnDefs : [ {
				orderable : false,
				targets : [ 1, 3 ]
			} ]
		});

	});

})();
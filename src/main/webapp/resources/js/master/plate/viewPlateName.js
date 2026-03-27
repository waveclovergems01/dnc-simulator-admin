(function() {

	$(function() {

		if ($.fn.dataTable.isDataTable("#plateNameTable")) {
			$("#plateNameTable").DataTable().destroy();
		}

		$("#plateNameTable").DataTable({
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
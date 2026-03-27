(function() {

	$(function() {

		if ($.fn.dataTable.isDataTable("#thirdStatTable")) {
			$("#thirdStatTable").DataTable().destroy();
		}

		$("#thirdStatTable").DataTable({
			pageLength : 10,
			lengthChange : false,
			ordering : true,
			searching : true,
			info : true,
			language : {
				emptyTable : "No data"
			},
			columnDefs : [ {
				orderable : false,
				targets : 5
			} ]
		});

	});

})();
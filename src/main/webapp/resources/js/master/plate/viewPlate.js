(function() {

	$(function() {

		// กัน init ซ้ำ
		if ($.fn.dataTable.isDataTable("#plateTable")) {
			$("#plateTable").DataTable().destroy();
		}

		$("#plateTable").DataTable({
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
				targets : 1
			}, // icon
			{
				orderable : false,
				targets : 6
			} // action
			]
		});

	});

})();
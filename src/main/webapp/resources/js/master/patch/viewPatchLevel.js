(function() {

	$(function() {
		$("#levelTable").DataTable({
			pageLength : 10,
			lengthChange : false,
			ordering : true,
			searching : true,
			info : true,
			columnDefs : [ {
				orderable : false,
				targets : 2
			} ]
		});
	});

})();
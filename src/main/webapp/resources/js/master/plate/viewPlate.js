(function() {

	$(function() {

		if ($.fn.dataTable.isDataTable("#plateTable")) {
			$("#plateTable").DataTable().destroy();
		}

		$("#plateTable").DataTable({
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
				targets : [ 1, 7 ]
			} ]
		});

		$(document)
				.on(
						"click",
						".btn-delete-plate",
						function() {
							var id = ($(this).attr("data-id") || "").trim();

							if (id === "") {
								alert("Invalid plate id");
								return;
							}

							if (!confirm("Are you sure you want to delete this plate?")) {
								return;
							}

							$
									.ajax({
										url : APP_CTX
												+ "/master/plate/deletePlate",
										type : "POST",
										data : {
											id : id
										},
										success : function(res) {
											if (res === "SUCCESS") {
												window.location.reload();
											} else {
												alert(res);
											}
										},
										error : function(xhr) {
											alert((xhr && xhr.responseText) ? xhr.responseText
													: "ERROR");
										}
									});
						});

	});

})();
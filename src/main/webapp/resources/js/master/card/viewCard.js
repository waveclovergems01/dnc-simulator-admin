(function() {

	$(function() {

		if ($.fn.dataTable.isDataTable("#cardTable")) {
			$("#cardTable").DataTable().destroy();
		}

		$("#cardTable").DataTable({
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
				targets : [ 1, 7, 8 ]
			} ]
		});

		if ($.fn.dataTable.isDataTable("#cardSummaryTable")) {
			$("#cardSummaryTable").DataTable().destroy();
		}

		$("#cardSummaryTable").DataTable({
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
				targets : [ 2, 4, 5, 6, 7, 8 ]
			} ]
		});

		$(document).on("click", ".btn-delete-card", function() {
			var id = ($(this).attr("data-id") || "").trim();

			if (id === "") {
				alert("Invalid card id");
				return;
			}

			if (!confirm("Are you sure you want to delete this card?")) {
				return;
			}

			$.ajax({
				url : APP_CTX + "/master/card/deleteCard",
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
					alert((xhr && xhr.responseText) ? xhr.responseText : "ERROR");
				}
			});
		});

	});

})();
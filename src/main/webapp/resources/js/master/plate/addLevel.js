$(document).ready(function() {

	$("#levelForm").submit(function(e) {

		e.preventDefault();

		let id = $("#id").val();
		let level = $("#level").val().trim();

		if (level === "") {
			alert("Level is required");
			return;
		}

		if (!/^\d+$/.test(level)) {
			alert("Level must be number");
			return;
		}

		let data = {
			level : level
		};

		if (id) {
			data.id = id;
		}

		$("#btnSave").prop("disabled", true);

		$.ajax({
			url : APP_CTX + "/master/plate/saveLevel",
			method : "POST",
			data : data,

			success : function(res) {

				if (res === "SUCCESS") {

					window.location.href = APP_CTX + "/master/plate/viewLevel";

				} else {

					alert(res);

				}

			},

			error : function(xhr) {

				alert(xhr.responseText || "ERROR");

			},

			complete : function() {

				$("#btnSave").prop("disabled", false);

			}

		});

	});

});
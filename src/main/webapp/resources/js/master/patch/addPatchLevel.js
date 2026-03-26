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

		let num = parseInt(level, 10);
		if (isNaN(num) || num <= 0) {
			alert("Level must be greater than 0");
			return;
		}

		let data = {
			level : level
		};
		if (id)
			data.id = id;

		$("#btnSave").prop("disabled", true);

		$.ajax({
			url : APP_CTX + "/master/patch/savePatchLevel",
			method : "POST",
			data : data,

			success : function(res) {
				if (res === "SUCCESS") {
					window.location.href = APP_CTX + "/master/patch/viewPatchLevel";
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
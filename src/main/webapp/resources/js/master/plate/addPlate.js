(function() {

	$(function() {

		function toggleStatDropdown() {
			var plateTypeId = ($("#plateTypeId").val() || "").trim();

			if (plateTypeId === "1") {
				$("#statDropdownWrapper").show();
			} else {
				$("#statDropdownWrapper").hide();
				$("#statId").val("");
				$("#statValue").val("");
				$("#statPercent").val("");
			}
		}

		function updatePlateNamePreview() {
			var selected = $("#plateNameId option:selected");
			var iconUrl = selected.attr("data-icon-url") || "";
			var plateNameId = ($("#plateNameId").val() || "").trim();

			if (plateNameId === "" || iconUrl === "") {
				$("#plateNamePreview").hide().attr("src", "");
				return;
			}

			$("#plateNamePreview").attr("src", iconUrl).show();
		}

		$(document).on("input", ".number-only-input", function() {
			var val = $(this).val() || "";
			val = val.replace(/\D/g, "");
			$(this).val(val);
		});

		$(document).on(
				"input",
				".decimal-only-input",
				function() {
					var val = $(this).val() || "";
					val = val.replace(/[^0-9.]/g, "");

					var firstDot = val.indexOf(".");
					if (firstDot !== -1) {
						val = val.substring(0, firstDot + 1)
								+ val.substring(firstDot + 1)
										.replace(/\./g, "");
					}

					$(this).val(val);
				});

		$("#plateTypeId").on("change", function() {
			toggleStatDropdown();
		});

		$("#plateNameId").on("change", function() {
			updatePlateNamePreview();
		});

		toggleStatDropdown();
		updatePlateNamePreview();

		$("#plateForm").on(
				"submit",
				function(e) {
					e.preventDefault();

					var id = ($("#id").val() || "").trim();
					var plateTypeId = ($("#plateTypeId").val() || "").trim();
					var plateNameId = ($("#plateNameId").val() || "").trim();
					var patchLevelId = ($("#patchLevelId").val() || "").trim();
					var rarityId = ($("#rarityId").val() || "").trim();
					var statId = ($("#statId").val() || "").trim();
					var statValue = ($("#statValue").val() || "").trim();
					var statPercent = ($("#statPercent").val() || "").trim();

					if (plateTypeId === "" || !/^\d+$/.test(plateTypeId)) {
						alert("Plate Type is required");
						return;
					}

					if (plateNameId === "" || !/^\d+$/.test(plateNameId)) {
						alert("Plate Name is required");
						return;
					}

					if (patchLevelId === "" || !/^\d+$/.test(patchLevelId)) {
						alert("Patch Level is required");
						return;
					}

					if (rarityId === "" || !/^\d+$/.test(rarityId)) {
						alert("Rarity is required");
						return;
					}

					if (plateTypeId === "1") {
						if (statId === "" || !/^\d+$/.test(statId)) {
							alert("Stat is required when Plate Type is 1");
							return;
						}
					}

					if (statValue !== "" && !/^\d+$/.test(statValue)) {
						alert("Value (Unit) must be numeric only");
						return;
					}

					if (statPercent !== ""
							&& !/^\d+(\.\d+)?$/.test(statPercent)) {
						alert("Value (%) must be numeric only");
						return;
					}

					var fd = new FormData();
					fd.append("plateTypeId", plateTypeId);
					fd.append("plateNameId", plateNameId);
					fd.append("patchLevelId", patchLevelId);
					fd.append("rarityId", rarityId);
					fd.append("statId", statId);
					fd.append("statValue", statValue);
					fd.append("statPercent", statPercent);

					if (id !== "") {
						fd.append("id", id);
					}

					$("#btnSave").prop("disabled", true);

					$.ajax({
						url : APP_CTX + "/master/plate/savePlate",
						type : "POST",
						data : fd,
						processData : false,
						contentType : false,
						success : function(res) {
							if (res === "SUCCESS") {
								window.location.href = APP_CTX
										+ "/master/plate/viewPlate";
							} else {
								alert(res);
							}
						},
						error : function(xhr) {
							alert((xhr && xhr.responseText) ? xhr.responseText
									: "ERROR");
						},
						complete : function() {
							$("#btnSave").prop("disabled", false);
						}
					});
				});

	});

})();
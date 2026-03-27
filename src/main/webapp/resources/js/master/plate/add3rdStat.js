(function() {

	$(function() {

		$(document).on("input", ".number-only-input", function() {
			var val = $(this).val() || "";
			val = val.replace(/\D/g, "");
			$(this).val(val);
		});

		$("#thirdStatForm").submit(
				function(e) {
					e.preventDefault();

					var id = ($("#id").val() || "").trim();
					var statId = ($("#statId").val() || "").trim();
					var rarityId = ($("#rarityId").val() || "").trim();
					var patchLevelId = ($("#patchLevelId").val() || "").trim();
					var value = ($("#value").val() || "").trim();

					if (statId === "" || !/^\d+$/.test(statId)) {
						alert("Stat is required");
						return;
					}

					if (rarityId === "" || !/^\d+$/.test(rarityId)) {
						alert("Rarity is required");
						return;
					}

					if (patchLevelId === "" || !/^\d+$/.test(patchLevelId)) {
						alert("Patch Level is required");
						return;
					}

					if (value === "" || !/^\d+$/.test(value)) {
						alert("Value must be numeric only");
						return;
					}

					$("#btnSave").prop("disabled", true);

					$.ajax({
						url : APP_CTX + "/master/plate/save3rdStat",
						type : "POST",
						data : {
							id : id,
							statId : statId,
							rarityId : rarityId,
							patchLevelId : patchLevelId,
							value : value
						},
						success : function(res) {
							if (res === "SUCCESS") {
								window.location.href = APP_CTX
										+ "/master/plate/view3rdStat";
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
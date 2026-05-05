(function() {

	$(function() {

		function updateCardNamePreview() {
			var selected = $("#cardNameId option:selected");
			var iconUrl = selected.attr("data-icon-url") || "";
			var cardNameId = ($("#cardNameId").val() || "").trim();

			if (cardNameId === "" || iconUrl === "") {
				$("#cardNamePreview").hide().attr("src", "");
				return;
			}

			$("#cardNamePreview").attr("src", iconUrl).show();
		}

		function normalizeDecimalInput($input) {
			var val = $input.val() || "";

			val = val.replace(/[^0-9.]/g, "");

			var firstDot = val.indexOf(".");
			if (firstDot !== -1) {
				val = val.substring(0, firstDot + 1)
						+ val.substring(firstDot + 1).replace(/\./g, "");
			}

			$input.val(val);
		}

		function getStatRowCount() {
			return $("#cardStatTable tbody tr.stat-row").length;
		}

		function addStatRow() {
			var $template = $("#statRowTemplate").clone();

			$template.removeAttr("id");
			$template.show();

			$template.find(".stat-id").val("");
			$template.find(".value-min").val("");
			$template.find(".value-max").val("");
			$template.find(".is-percentage").val("0");

			$("#cardStatTable tbody").append($template);
		}

		function removeStatRow($btn) {
			if (getStatRowCount() <= 1) {
				alert("At least one stat is required");
				return;
			}

			$btn.closest("tr.stat-row").remove();
		}

		function hasDuplicateStat() {
			var used = {};
			var duplicate = false;

			$("#cardStatTable tbody tr.stat-row").each(function() {
				var statId = ($(this).find(".stat-id").val() || "").trim();

				if (statId === "") {
					return true;
				}

				if (used[statId]) {
					duplicate = true;
					return false;
				}

				used[statId] = true;
			});

			return duplicate;
		}

		function validateForm() {
			var typeId = ($("#typeId").val() || "").trim();
			var cardNameId = ($("#cardNameId").val() || "").trim();
			var patchLevelId = ($("#patchLevelId").val() || "").trim();
			var rarityId = ($("#rarityId").val() || "").trim();
			var slotNumber = ($("#slotNumber").val() || "").trim();

			if (typeId === "" || !/^\d+$/.test(typeId)) {
				alert("Item Type is required");
				return false;
			}

			if (cardNameId === "" || !/^\d+$/.test(cardNameId)) {
				alert("Card Name is required");
				return false;
			}

			if (patchLevelId === "" || !/^\d+$/.test(patchLevelId)) {
				alert("Level is required");
				return false;
			}

			if (rarityId === "" || !/^\d+$/.test(rarityId)) {
				alert("Rarity is required");
				return false;
			}

			if (slotNumber === "" || !/^\d+$/.test(slotNumber)) {
				alert("Slot Number is required");
				return false;
			}

			if (parseInt(slotNumber, 10) <= 0) {
				alert("Slot Number must be greater than 0");
				return false;
			}

			if (getStatRowCount() <= 0) {
				alert("At least one stat is required");
				return false;
			}

			var validStats = true;

			$("#cardStatTable tbody tr.stat-row").each(function() {
				var statId = ($(this).find(".stat-id").val() || "").trim();
				var valueMin = ($(this).find(".value-min").val() || "").trim();
				var valueMax = ($(this).find(".value-max").val() || "").trim();
				var isPercentage = ($(this).find(".is-percentage").val() || "").trim();

				if (statId === "" || !/^\d+$/.test(statId)) {
					alert("Stat is required");
					validStats = false;
					return false;
				}

				if (valueMin === "" || !/^\d+(\.\d+)?$/.test(valueMin)) {
					alert("Min value must be numeric only");
					validStats = false;
					return false;
				}

				if (valueMax === "" || !/^\d+(\.\d+)?$/.test(valueMax)) {
					alert("Max value must be numeric only");
					validStats = false;
					return false;
				}

				if (parseFloat(valueMin) > parseFloat(valueMax)) {
					alert("Min value must be less than or equal Max value");
					validStats = false;
					return false;
				}

				if (isPercentage !== "0" && isPercentage !== "1") {
					alert("Is % must be 0 or 1");
					validStats = false;
					return false;
				}
			});

			if (!validStats) {
				return false;
			}

			if (hasDuplicateStat()) {
				alert("Duplicate stat in same card rarity");
				return false;
			}

			return true;
		}

		$(document).on("input", ".number-only-input", function() {
			var val = $(this).val() || "";
			val = val.replace(/\D/g, "");
			$(this).val(val);
		});

		$(document).on("input", ".decimal-only-input", function() {
			normalizeDecimalInput($(this));
		});

		$("#cardNameId").on("change", function() {
			updateCardNamePreview();
		});

		$("#btnAddStat").on("click", function() {
			addStatRow();
		});

		$(document).on("click", ".btn-remove-stat", function() {
			removeStatRow($(this));
		});

		updateCardNamePreview();

		$("#cardForm").on("submit", function(e) {
			e.preventDefault();

			if (!validateForm()) {
				return;
			}

			var fd = new FormData();
			var id = ($("#id").val() || "").trim();

			if (id !== "") {
				fd.append("id", id);
			}

			fd.append("typeId", ($("#typeId").val() || "").trim());
			fd.append("cardNameId", ($("#cardNameId").val() || "").trim());
			fd.append("patchLevelId", ($("#patchLevelId").val() || "").trim());
			fd.append("rarityId", ($("#rarityId").val() || "").trim());
			fd.append("slotNumber", ($("#slotNumber").val() || "").trim());

			$("#cardStatTable tbody tr.stat-row").each(function() {
				fd.append("statId", ($(this).find(".stat-id").val() || "").trim());
				fd.append("valueMin", ($(this).find(".value-min").val() || "").trim());
				fd.append("valueMax", ($(this).find(".value-max").val() || "").trim());
				fd.append("isPercentage", ($(this).find(".is-percentage").val() || "0").trim());
			});

			$("#btnSave").prop("disabled", true);

			$.ajax({
				url : APP_CTX + "/master/card/saveCard",
				type : "POST",
				data : fd,
				processData : false,
				contentType : false,
				success : function(res) {
					if (res === "SUCCESS") {
						window.location.href = APP_CTX + "/master/card/viewCard";
					} else {
						alert(res);
					}
				},
				error : function(xhr) {
					alert((xhr && xhr.responseText) ? xhr.responseText : "ERROR");
				},
				complete : function() {
					$("#btnSave").prop("disabled", false);
				}
			});
		});

	});

})();
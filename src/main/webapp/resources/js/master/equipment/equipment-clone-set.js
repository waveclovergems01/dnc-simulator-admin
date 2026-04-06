(function() {

	$(function() {

		function applyGlobalNamePrefix() {
			var prefix = ($("#globalNamePrefix").val() || "").trim();

			$("#cloneTable tbody tr").each(function() {
				var $nameInput = $(this).find(".itemName");
				var originalName = ($nameInput.attr("data-original") || "").trim();

				if (prefix === "") {
					if (originalName !== "") {
						$nameInput.val(originalName);
					}
					return;
				}

				if (originalName !== "") {
					const last = originalName.trim().split(/\s+/).pop();
					$nameInput.val(prefix + ' ' + last);
				} else {
					$nameInput.val(prefix);
				}
			});
		}

		function applyGlobalType() {
			var value = ($("#globalType").val() || "").trim();
			if (value === "") {
				return;
			}

			$("#cloneTable tbody .typeSelect").val(value);
		}

		function applyGlobalJob() {
			var value = ($("#globalJob").val() || "").trim();
			if (value === "") {
				return;
			}

			$("#cloneTable tbody .jobSelect").val(value);
		}

		function applyGlobalRarity() {
			var value = ($("#globalRarity").val() || "").trim();
			if (value === "") {
				return;
			}

			$("#cloneTable tbody .raritySelect").val(value);
		}

		function applyGlobalReqLv() {
			var value = ($("#globalReqLv").val() || "").trim();
			if (value === "") {
				return;
			}

			$("#cloneTable tbody .reqLvInput").val(value);
		}

		function buildNewRowIndex() {
			return $("#cloneTable tbody tr").length;
		}

		function renameTemplateFields($row, index) {
			var suffix = "n" + index;

			$row.find('input[name="name_0"]').attr("name", "name_" + suffix);
			$row.find('select[name="typeId_0"]').attr("name", "typeId_" + suffix);
			$row.find('select[name="jobId_0"]').attr("name", "jobId_" + suffix);
			$row.find('select[name="rarityId_0"]').attr("name", "rarityId_" + suffix);
			$row.find('input[name="requiredLevel_0"]').attr("name", "requiredLevel_" + suffix);

			$row.find(".itemName").attr("data-original", "");
		}

		function addRow() {
			var html = $("#rowTemplate").html();
			var $row = $(html);
			var index = buildNewRowIndex();

			renameTemplateFields($row, index);

			var globalType = ($("#globalType").val() || "").trim();
			var globalJob = ($("#globalJob").val() || "").trim();
			var globalRarity = ($("#globalRarity").val() || "").trim();
			var globalReqLv = ($("#globalReqLv").val() || "").trim();
			var globalNamePrefix = ($("#globalNamePrefix").val() || "").trim();

			if (globalType !== "") {
				$row.find(".typeSelect").val(globalType);
			}

			if (globalJob !== "") {
				$row.find(".jobSelect").val(globalJob);
			}

			if (globalRarity !== "") {
				$row.find(".raritySelect").val(globalRarity);
			}

			if (globalReqLv !== "") {
				$row.find(".reqLvInput").val(globalReqLv);
			}

			if (globalNamePrefix !== "") {
				$row.find(".itemName").val(globalNamePrefix);
			}

			$("#cloneTable tbody").append($row);
		}

		function validateForm() {
			var newSetId = ($("#globalSetId").val() || "").trim();
			if (newSetId === "") {
				alert("New Set ID is required");
				$("#globalSetId").focus();
				return false;
			}

			var hasAtLeastOneNewItemId = false;
			var duplicated = {};
			var duplicateFound = false;

			$("#cloneTable tbody tr").each(function() {
				var $tr = $(this);
				var newItemId = ($tr.find(".newItemId").val() || "").trim();
				var name = ($tr.find(".itemName").val() || "").trim();
				var typeId = ($tr.find(".typeSelect").val() || "").trim();

				if (newItemId !== "") {
					hasAtLeastOneNewItemId = true;

					if (!/^\d+$/.test(newItemId)) {
						alert("New Item ID must be numeric");
						$tr.find(".newItemId").focus();
						duplicateFound = true;
						return false;
					}

					if (duplicated[newItemId]) {
						alert("Duplicate New Item ID: " + newItemId);
						$tr.find(".newItemId").focus();
						duplicateFound = true;
						return false;
					}
					duplicated[newItemId] = true;
				}

				if (name === "") {
					alert("Item name is required");
					$tr.find(".itemName").focus();
					duplicateFound = true;
					return false;
				}

				if (typeId === "") {
					alert("Type is required");
					$tr.find(".typeSelect").focus();
					duplicateFound = true;
					return false;
				}
			});

			if (duplicateFound) {
				return false;
			}

			if (!hasAtLeastOneNewItemId) {
				alert("Please enter at least one New Item ID");
				return false;
			}

			return true;
		}

		$("#btnAddRow").on("click", function() {
			addRow();
		});

		$(document).on("click", ".btn-delete-row", function() {
			$(this).closest("tr").remove();
		});
		
		$('#globalSetId').on('input', function() {
			let p = $(this).val();
			let i = 1;
			$('.newItemId').each(function() {
				$(this).val(p + String(i++).padStart(4, '0'));
			});
		});

		$("#globalType").on("change", function() {
			applyGlobalType();
		});

		$("#globalJob").on("change", function() {
			applyGlobalJob();
		});

		$("#globalRarity").on("change", function() {
			applyGlobalRarity();
		});

		$("#globalReqLv").on("input", function() {
			applyGlobalReqLv();
		});

		$("#globalNamePrefix").on("input", function() {
			applyGlobalNamePrefix();
		});

		$("#cloneSetForm").on("submit", function(e) {
			if (!validateForm()) {
				e.preventDefault();
				return false;
			}
		});

	});

})();
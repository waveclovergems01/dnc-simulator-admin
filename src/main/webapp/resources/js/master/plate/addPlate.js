(function() {

	$(function() {

		// ===== Plate Name Input Control (Title Case) =====
		$(document).on("input", ".plate-name-input", function() {

			let val = $(this).val() || "";

			// allow only letters and space
			val = val.replace(/[^a-zA-Z ]/g, "");

			// remove multiple spaces
			val = val.replace(/\s{2,}/g, " ");

			// remove leading space
			val = val.replace(/^\s+/g, "");

			// Title Case
			val = val.split(" ").map(function(word) {

				if (word.length === 0) return "";

				return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();

			}).join(" ");

			$(this).val(val);

		});


		// ===== Icon Preview =====
		$("#iconFile").on("change", function() {

			var file = this.files && this.files.length > 0 ? this.files[0] : null;

			if (!file) {
				$("#iconPreview").hide().attr("src", "");
				return;
			}

			// validate type
			var allow = ["image/png", "image/jpeg", "image/webp"];
			if (allow.indexOf(file.type) === -1) {
				alert("Icon must be PNG/JPG/WEBP");
				$(this).val("");
				$("#iconPreview").hide().attr("src", "");
				return;
			}

			// validate size 2MB
			var maxBytes = 2 * 1024 * 1024;
			if (file.size > maxBytes) {
				alert("Icon file too large (max 2MB)");
				$(this).val("");
				$("#iconPreview").hide().attr("src", "");
				return;
			}

			var url = URL.createObjectURL(file);
			$("#iconPreview").attr("src", url).show();

		});


		// ===== Submit =====
		$("#plateForm").submit(function(e) {
			e.preventDefault();

			var id = $("#id").val();

			// get values
			var plateName = ($("#plateName").val() || "").trim();
			var plateTypeId = ($("#plateTypeId").val() || "").trim();
			var plateLevelId = ($("#plateLevelId").val() || "").trim();
			var rarityId = ($("#rarityId").val() || "").trim();

			// validate plate name: A-Z words separated by single spaces
			if (plateName === "") {
				alert("Plate Name is required");
				return;
			}
			if (!/^[A-Za-z]+( [A-Za-z]+)*$/.test(plateName)) {
				alert("Plate Name must contain only English words separated by spaces");
				return;
			}

			// validate dropdowns
			if (plateTypeId === "" || !/^\d+$/.test(plateTypeId)) {
				alert("Plate Type is required");
				return;
			}
			if (plateLevelId === "" || !/^\d+$/.test(plateLevelId)) {
				alert("Plate Level is required");
				return;
			}
			if (rarityId === "" || !/^\d+$/.test(rarityId)) {
				alert("Rarity is required");
				return;
			}

			// build multipart form
			var fd = new FormData();
			fd.append("plateName", plateName);
			fd.append("plateTypeId", plateTypeId);
			fd.append("plateLevelId", plateLevelId);
			fd.append("rarityId", rarityId);

			if (id) {
				fd.append("id", id);
			}

			// icon file (optional)
			var iconFile = $("#iconFile")[0].files && $("#iconFile")[0].files.length > 0
				? $("#iconFile")[0].files[0]
				: null;

			if (iconFile) {
				fd.append("iconFile", iconFile);
			}

			$("#btnSave").prop("disabled", true);

			$.ajax({
				url: APP_CTX + "/master/plate/savePlate",
				type: "POST",
				data: fd,
				processData: false,
				contentType: false,
				success: function(res) {
					if (res === "SUCCESS") {
						window.location.href = APP_CTX + "/master/plate/viewPlate";
					} else {
						alert(res);
					}
				},
				error: function(xhr) {
					alert((xhr && xhr.responseText) ? xhr.responseText : "ERROR");
				},
				complete: function() {
					$("#btnSave").prop("disabled", false);
				}
			});

		});

	});

})();
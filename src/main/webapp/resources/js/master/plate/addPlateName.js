(function() {

	$(function() {

		$(document).on(
				"input",
				".plate-name-input",
				function() {

					let val = $(this).val() || "";

					val = val.replace(/[^a-zA-Z ]/g, "");
					val = val.replace(/\s{2,}/g, " ");
					val = val.replace(/^\s+/g, "");

					val = val.split(" ").map(
							function(word) {
								if (word.length === 0) {
									return "";
								}
								return word.charAt(0).toUpperCase()
										+ word.slice(1).toLowerCase();
							}).join(" ");

					$(this).val(val);

				});

		$("#iconFile")
				.on(
						"change",
						function() {

							var file = this.files && this.files.length > 0 ? this.files[0]
									: null;

							if (!file) {
								$("#iconPreview").hide().attr("src", "");
								return;
							}

							var allow = [ "image/png", "image/jpeg",
									"image/webp" ];
							if (allow.indexOf(file.type) === -1) {
								alert("Icon must be PNG/JPG/WEBP");
								$(this).val("");
								$("#iconPreview").hide().attr("src", "");
								return;
							}

							var maxBytes = 2 * 1024 * 1024;
							if (file.size > maxBytes) {
								alert("Icon file too large (max 2MB)");
								$(this).val("");
								$("#iconPreview").hide().attr("src", "");
								return;
							}

							var url = URL.createObjectURL(file);
							$("#iconPreview").attr("src", url).show();
							$("#currentIcon").hide();

						});

		$("#plateNameForm")
				.submit(
						function(e) {
							e.preventDefault();

							var id = ($("#id").val() || "").trim();
							var name = ($("#name").val() || "").trim();

							if (name === "") {
								alert("Plate Name is required");
								return;
							}

							if (!/^[A-Za-z]+( [A-Za-z]+)*$/.test(name)) {
								alert("Plate Name must contain only English words separated by single spaces");
								return;
							}

							var fd = new FormData();
							fd.append("name", name);

							if (id !== "") {
								fd.append("id", id);
							}

							var iconFile = $("#iconFile")[0].files
									&& $("#iconFile")[0].files.length > 0 ? $("#iconFile")[0].files[0]
									: null;

							if (iconFile) {
								fd.append("iconFile", iconFile);
							}

							$("#btnSave").prop("disabled", true);

							$
									.ajax({
										url : APP_CTX
												+ "/master/plate/savePlateName",
										type : "POST",
										data : fd,
										processData : false,
										contentType : false,
										success : function(res) {
											if (res === "SUCCESS") {
												window.location.href = APP_CTX
														+ "/master/plate/viewPlateName";
											} else {
												alert(res);
											}
										},
										error : function(xhr) {
											alert((xhr && xhr.responseText) ? xhr.responseText
													: "ERROR");
										},
										complete : function() {
											$("#btnSave").prop("disabled",
													false);
										}
									});
						});

	});

})();
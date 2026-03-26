(function () {

    function keepEnglishAndSpace(value) {
        return String(value || "").replace(/[^A-Za-z ]/g, "");
    }

    function toTitleCaseWords(value) {

        var s = String(value || "");

        // allow space while typing
        s = s.replace(/\s{2,}/g, " ");

        return s.replace(/\b([A-Za-z])([A-Za-z]*)\b/g, function (match, first, rest) {
            return first.toUpperCase() + rest.toLowerCase();
        });
    }

    function normalizeForSave(value) {
        var s = String(value || "");
        s = keepEnglishAndSpace(s);
        s = s.replace(/\s{2,}/g, " ").trim();
        s = toTitleCaseWords(s);
        return s;
    }

    function saveType(id, typeName) {

        var payload = { typeName: typeName };

        if (id) {
            payload.id = id;
        }

        $.ajax({
            url: window.APP_CTX + "/master/plate/saveType",
            type: "POST",
            data: payload,
            success: function () {
                alert("Saved successfully");
                window.location.href = window.APP_CTX + "/master/plate/viewType";
            },
            error: function (xhr) {
                alert("Save failed : " + xhr.status);
            }
        });
    }

    function deleteType(id) {

        $.ajax({
            url: window.APP_CTX + "/master/plate/deleteType",
            type: "POST",
            data: { id: id },
            success: function () {
                alert("Deleted successfully");
                window.location.href = window.APP_CTX + "/master/plate/viewType";
            },
            error: function (xhr) {
                alert("Delete failed : " + xhr.status);
            }
        });
    }

    $(function () {

        $("#typeName").on("input", function () {

            var v = $(this).val();
            v = keepEnglishAndSpace(v);
            v = toTitleCaseWords(v);
            $(this).val(v);

        });

        $("#btnSaveType").on("click", function () {

            var id = $("#typeId").val();
            var name = normalizeForSave($("#typeName").val());

            $("#typeName").val(name);

            if (name.trim() === "") {
                alert("Please enter Type Name");
                return;
            }

            saveType(id, name);
        });

        $("#btnCancelType").on("click", function () {
            window.location.href = window.APP_CTX + "/master/plate/viewType";
        });

        $("#btnDeleteType").on("click", function () {

            var id = $("#typeId").val();

            if (!id) return;

            if (!confirm("Confirm delete?")) return;

            deleteType(id);
        });
        

    });

})();
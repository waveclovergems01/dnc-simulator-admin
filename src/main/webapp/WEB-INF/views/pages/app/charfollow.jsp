<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>

<%
	class TaskRow {
		public String type; // Nest / Dungeon / Event
		public String name;
		public int available; // editable
		public int max; // from DB (demo)
		public TaskRow(String type, String name, int available, int max) {
			this.type = type;
			this.name = name;
			this.available = available;
			this.max = max;
		}
	}

	class CharacterCard {
		public String charId, name, job;
		public List<TaskRow> rows = new ArrayList<>();
		public CharacterCard(String charId, String name, String job) {
			this.charId = charId;
			this.name = name;
			this.job = job;
		}
	}

	List<CharacterCard> chars = new ArrayList<>();

	CharacterCard c1 = new CharacterCard("C001", "Ariana", "Warrior");
	c1.rows.add(new TaskRow("Nest", "Manticore Nest (Hell Mode)", 0, 1));
	c1.rows.add(new TaskRow("Nest", "Archbishop Nest", 3, 4)); // green
	c1.rows.add(new TaskRow("Dungeon", "Sea Dragon Nest Core", 1, 1)); // red
	c1.rows.add(new TaskRow("Event", "Weekend Bonus Run", 0, 3));
	chars.add(c1);

	CharacterCard c2 = new CharacterCard("C002", "Borin", "Archer");
	c2.rows.add(new TaskRow("Nest", "Apocalypse Nest (Hell Mode)", 0, 1));
	c2.rows.add(new TaskRow("Nest",
			"Green Dragon Nest (Hardcore Mode) Very Very Long Name Example For Wrap Text Demonstration", 0, 1));
	c2.rows.add(new TaskRow("Dungeon", "Cerberus Arena", 2, 2)); // red
	chars.add(c2);

	CharacterCard c3 = new CharacterCard("C003", "Celia", "Sorceress");
	c3.rows.add(new TaskRow("Nest", "Archbishop Nest (Challenge Mode)", 0, 1));
	c3.rows.add(new TaskRow("Dungeon", "Sea Dragon Nest Outskirts", 0, 1));
	c3.rows.add(new TaskRow("Nest", "Gigantes Nest (Hell Mode)", 1, 1)); // red
	c3.rows.add(new TaskRow("Event", "Daily Mission Pack", 2, 5)); // green
	chars.add(c3);

	CharacterCard c4 = new CharacterCard("C004", "Dane", "Cleric");
	c4.rows.add(new TaskRow("Nest", "Manticore Nest", 1, 1)); // red
	c4.rows.add(new TaskRow("Nest", "Archbishop Nest", 2, 4)); // green
	c4.rows.add(new TaskRow("Nest", "Green Dragon Nest", 0, 1));
	c4.rows.add(new TaskRow("Dungeon", "Chaos Rift", 3, 3)); // red
	c4.rows.add(new TaskRow("Event", "Guild Boss", 0, 2));
	c4.rows.add(new TaskRow("Event", "Daily Attendance", 7, 10));
	c4.rows.add(new TaskRow("Dungeon", "Volcano Heart", 2, 5));
	c4.rows.add(new TaskRow("Dungeon", "Specter Canyon", 0, 3));
	c4.rows.add(new TaskRow("Nest", "Cerberus Nest (Challenge)", 0, 1));
	c4.rows.add(new TaskRow("Nest", "Apocalypse Nest (Challenge)", 0, 1));
	c4.rows.add(new TaskRow("Nest", "Sea Dragon Nest (Hardcore)", 0, 1)); // 11th -> scroll
	chars.add(c4);
%>

<!DOCTYPE html>
<html>
<head>
<title>Follow-up by Character (Drag + Editable + Row Colors)</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
	rel="stylesheet">

<style>
.mono {
	font-variant-numeric: tabular-nums;
}

.tiny {
	font-size: 12px;
	color: #6c757d;
}

.card-hover {
	transition: transform .12s ease, box-shadow .12s ease;
}

.card-hover:hover {
	transform: translateY(-2px);
	box-shadow: 0 0.75rem 1.5rem rgba(0, 0, 0, .08) !important;
}

/* fixed list height (~10 rows) + vertical scroll */
.task-scroll {
	height: 360px;
	overflow-y: auto;
	overflow-x: hidden;
}

/* table: wrap names, prevent horizontal scroll */
.task-table {
	table-layout: fixed;
	width: 100%;
}

.task-table th, .task-table td {
	vertical-align: top;
}

.col-name {
	white-space: normal;
	overflow-wrap: anywhere;
	word-break: break-word;
	padding-right: 8px;
}

.col-limit {
	width: 120px;
}

.limit-wrap {
	display: flex;
	justify-content: flex-end;
	gap: 6px;
	align-items: center;
}

.avail-input {
	width: 70px;
	text-align: center;
}

.slash {
	color: #6c757d;
}

/* sticky header in scroll area */
.task-table thead th {
	position: sticky;
	top: 0;
	z-index: 2;
	background: #f8f9fa;
}

/* ✅ IMPORTANT: Bootstrap paints backgrounds on TD, so paint TD */
tr.row-ok>td {
	background-color: rgba(25, 135, 84, .12) !important;
} /* green */
tr.row-full>td {
	background-color: rgba(220, 53, 69, .16) !important;
} /* red */
.badge-type {
	border: 1px solid rgba(0, 0, 0, .10);
	background: #fff;
}

/* Drag cards */
#charGrid .char-item {
	user-select: none;
}

#charGrid .char-item .card {
	cursor: grab;
}

#charGrid .char-item:active .card {
	cursor: grabbing;
}

.drag-ghost {
	opacity: .35;
}

.drag-chosen {
	cursor: grabbing !important;
}

/* ✅ ชัดๆ ว่าตรงนี้ห้ามเริ่ม drag (inputs/buttons) */
.no-drag {
	cursor: auto !important;
}
</style>
</head>

<body class="bg-light">
	<div class="container py-0">

		<div
			class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
			<div>
				<h3 class="mb-0">Follow-up by Character</h3>
				<div class="text-muted small">Drag/Drop Card ได้ • กรอก
					Available ได้ • สีแถว: เต็ม=แดง, ยังไม่เต็ม=เขียว • clamp
					ไม่ให้เกิน max</div>
			</div>
			<button class="btn btn-primary btn-sm rounded-pill no-drag"
				type="button" onclick="saveAllDemo(event)">
				<i class="bi bi-save me-1"></i>Save All (Demo)
			</button>
		</div>

		<div class="row g-4" id="charGrid">
			<%
				for (CharacterCard ch : chars) {
			%>
			<div class="col-12 col-sm-6 col-lg-3 char-item"
				data-char-id="<%=ch.charId%>">
				<div class="card card-hover shadow-sm border-0 rounded-4 h-100">
					<div class="card-body">

						<div class="mb-2">
							<div class="fw-bold">
								CharacterName : <span class="mono"><%=ch.name%></span>
							</div>
							<div class="tiny">
								Class : <span class="mono"><%=ch.job%></span> • ID: <span
									class="mono"><%=ch.charId%></span>
							</div>
						</div>

						<hr class="my-2">

						<div class="task-scroll">
							<table class="table table-sm task-table mb-0">
								<thead>
									<tr>
										<th class="col-name">Name</th>
										<th class="col-limit text-end">Limit</th>
									</tr>
								</thead>

								<tbody>
									<%
										for (int i = 0; i < ch.rows.size(); i++) {
												TaskRow r = ch.rows.get(i);

												String rowId = "row_" + ch.charId + "_" + i;
												String availId = "avail_" + ch.charId + "_" + i;

												String icon = "bi-dungeon";
												if ("Dungeon".equalsIgnoreCase(r.type))
													icon = "bi-bricks";
												if ("Event".equalsIgnoreCase(r.type))
													icon = "bi-calendar-event";

												boolean isFull = (r.max > 0 && r.available >= r.max);
												String rowClass = isFull ? "row-full" : "row-ok";
									%>
									<tr id="<%=rowId%>" class="<%=rowClass%>">
										<td class="col-name"><span
											class="badge rounded-pill badge-type text-secondary me-2"><%=r.type%></span>
											<i class="bi <%=icon%> me-1 text-secondary"></i> <%=r.name%>
										</td>

										<td class="col-limit">
											<div class="limit-wrap">
												<input id="<%=availId%>"
													class="form-control form-control-sm avail-input mono no-drag"
													type="number" min="0" value="<%=r.available%>"
													data-max="<%=r.max%>" oninput="onAvailInput(this)" />
												<span class="slash">/</span> <span class="mono"><%=r.max%></span>
											</div>
										</td>
									</tr>
									<%
										}
									%>
								</tbody>
							</table>
						</div>

					</div>

					<div class="card-footer bg-transparent border-0 pt-0 pb-3 px-3">
						<button
							class="btn btn-outline-secondary btn-sm w-100 rounded-pill no-drag"
							type="button" onclick="saveOneDemo('<%=ch.charId%>', event)">
							<i class="bi bi-check2 me-1"></i>Save (Demo)
						</button>
					</div>
				</div>
			</div>
			<%
				}
			%>
		</div>

		<div class="mt-4">
			<div class="text-muted small mb-2">ผลลัพธ์ (Demo):</div>
			<pre id="output" class="bg-white border rounded-3 p-3 small mono"
				style="min-height: 70px;">(กด Save เพื่อดูข้อมูลที่กรอก หรือ Drag/Drop เพื่อดู order)</pre>
		</div>

	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.2/Sortable.min.js"></script>

	<script>
    const output = document.getElementById('output');

    function clamp(v, min, max) {
        if (!Number.isFinite(v)) v = 0;
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }

    function updateRowColor(inputEl) {
        const max = parseInt(inputEl.dataset.max || "0", 10);
        const avail = parseInt(inputEl.value || "0", 10);
        const row = inputEl.closest('tr');

        row.classList.remove('row-ok', 'row-full');
        if (max > 0 && avail >= max) row.classList.add('row-full');
        else row.classList.add('row-ok');
    }

    // ✅ กรอก Available ได้ และ clamp ไม่ให้เกิน max
    function onAvailInput(inputEl) {
        const max = parseInt(inputEl.dataset.max || "0", 10);
        let v = parseInt(inputEl.value || "0", 10);
        if (!Number.isFinite(v)) v = 0;

        if (max > 0) v = clamp(v, 0, max);
        else v = clamp(v, 0, Number.MAX_SAFE_INTEGER);

        inputEl.value = String(v);
        updateRowColor(inputEl);
    }

    // init clamp + color
    window.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('input[id^="avail_"]').forEach(inp => onAvailInput(inp));
    });

    // ✅ Drag & Drop Cards: filter เฉพาะ ".no-drag" และ "preventOnFilter=false" เพื่อให้คลิก/พิมพ์ได้แน่นอน
    (function initSortable() {
        const grid = document.getElementById('charGrid');

        if (typeof Sortable === 'undefined') {
            output.textContent = 'ERROR: SortableJS ไม่ถูกโหลด (เช็ค CDN/CSP)';
            return;
        }

        new Sortable(grid, {
            animation: 150,
            draggable: '.char-item',
            ghostClass: 'drag-ghost',
            chosenClass: 'drag-chosen',

            // ห้ามเริ่มลากจาก element ที่มี class no-drag (input/button)
            filter: '.no-drag',
            preventOnFilter: false,   // ✅ สำคัญ: ไม่ block click/focus ของ input

            onEnd: function () {
                const order = [...grid.querySelectorAll('.char-item')].map(el => el.getAttribute('data-char-id'));
                output.textContent = JSON.stringify({ cardOrder: order }, null, 2);
                console.log('Card order:', order);
            }
        });
    })();

    function saveOneDemo(charId, evt) {
        if (evt) evt.stopPropagation();
        const inputs = document.querySelectorAll('input[id^="avail_' + charId + '_"]');
        const data = [];
        inputs.forEach(inp => data.push({
            id: inp.id,
            available: parseInt(inp.value || "0", 10),
            max: parseInt(inp.dataset.max || "0", 10)
        }));
        output.textContent = JSON.stringify({ save: "one", charId, data }, null, 2);
    }

    function saveAllDemo(evt) {
        if (evt) evt.stopPropagation();
        const inputs = document.querySelectorAll('input[id^="avail_"]');
        const data = [];
        inputs.forEach(inp => data.push({
            id: inp.id,
            available: parseInt(inp.value || "0", 10),
            max: parseInt(inp.dataset.max || "0", 10)
        }));
        output.textContent = JSON.stringify({ save: "all", data }, null, 2);
    }
</script>

</body>
</html>
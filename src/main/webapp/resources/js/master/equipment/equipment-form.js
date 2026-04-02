let statIndex = 0;

(function() {

	$(function() {

		statIndex = document.querySelectorAll('#statTableBody tr').length;

		$('#iconFile').on('change', function(e) {
			const file = e.target.files && e.target.files[0];
			const img = document.getElementById('iconPreview');
			const empty = document.getElementById('iconPreviewEmpty');
			const keepOld = document.getElementById('keepOldIcon');

			if (!file) {
				return;
			}

			const max = 2 * 1024 * 1024;
			const allowed = [ 'image/png', 'image/jpeg', 'image/webp' ];

			if (allowed.indexOf(file.type) === -1) {
				alert('Icon must be PNG/JPG/WEBP');
				e.target.value = '';
				return;
			}

			if (file.size > max) {
				alert('Icon file too large (max 2MB)');
				e.target.value = '';
				return;
			}

			const reader = new FileReader();
			reader.onload = function(evt) {
				img.src = evt.target.result;
				img.style.display = 'inline-block';

				if (empty) {
					empty.classList.add('d-none');
				}

				if (keepOld) {
					keepOld.checked = false;
				}
			};
			reader.readAsDataURL(file);
		});

	});

})();

function addStatRow() {

	const template = document.getElementById('statRowTemplate').innerHTML;
	const row = template.replace(/__INDEX__/g, statIndex);

	$('#statTableBody').append(row);
	recalcStatIndex();
}

function removeRow(btn) {
	$(btn).closest('tr').remove();
	recalcStatIndex();
}

function recalcStatIndex() {
	statIndex = document.querySelectorAll('#statTableBody tr').length;
}

function reindexStats() {
	const rows = document.querySelectorAll('#statTableBody tr');

	rows.forEach(function(row, i) {
		row.querySelectorAll('input, select').forEach(function(el) {
			if (el.name) {
				el.name = el.name.replace(/stats\[\d+\]/, 'stats[' + i + ']');
			}
		});
	});
}

function onPercentageToggle(cb) {
	const row = cb.closest('tr');
	const min = row.querySelector('.value-min');
	const max = row.querySelector('.value-max');

	if (cb.checked) {
		min.step = 'any';
		max.step = 'any';
	} else {
		min.step = '1';
		max.step = '1';

		if (min.value !== '') {
			min.value = Math.trunc(parseFloat(min.value));
		}
		if (max.value !== '') {
			max.value = Math.trunc(parseFloat(max.value));
		}
	}
}

function validateEquipmentForm() {

	reindexStats();

	const rows = document.querySelectorAll('#statTableBody tr');

	for (let i = 0; i < rows.length; i++) {

		const stat = rows[i].querySelector('select');
		const min = rows[i].querySelector('.value-min');
		const max = rows[i].querySelector('.value-max');

		if (!stat.value) {
			alert('Stat #' + (i + 1) + ' is required');
			stat.focus();
			return false;
		}

		if (min.value === '' || max.value === '') {
			alert('Stat #' + (i + 1) + ' Min/Max is required');
			return false;
		}

		if (parseFloat(min.value) > parseFloat(max.value)) {
			alert('Stat #' + (i + 1) + ' Min > Max');
			return false;
		}
	}

	return true;
}
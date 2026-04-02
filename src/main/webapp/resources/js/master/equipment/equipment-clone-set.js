(function() {

	let newIndex = 1;

	function applyGlobalValues($rows) {

		const gType = $('#globalType').val();
		const gJob = $('#globalJob').val();
		const gRarity = $('#globalRarity').val();
		const gLv = $('#globalReqLv').val();
		const prefix = $('#globalNamePrefix').val().trim();

		if (gType) {
			$rows.find('.typeSelect').val(gType);
		}
		if (gJob) {
			$rows.find('.jobSelect').val(gJob);
		}
		if (gRarity) {
			$rows.find('.raritySelect').val(gRarity).trigger('change');
		}
		if (gLv) {
			$rows.find('.reqLvInput').val(gLv);
		}

		if (prefix) {
			$rows.find('.itemName').each(function() {
				const original = $(this).data('original');
				if (!original) {
					return;
				}
				const last = original.trim().split(/\s+/).pop();
				$(this).val(prefix + ' ' + last);
			});
		}
	}

	$(function() {

		$(
				'#globalType, #globalJob, #globalRarity, #globalReqLv, #globalNamePrefix')
				.on('change input', function() {
					applyGlobalValues($('#cloneTable tbody'));
				});

		$('#globalSetId').on('input', function() {
			let prefix = $(this).val();
			let i = 1;

			$('.newItemId').each(function() {
				if (prefix) {
					$(this).val(prefix + String(i++).padStart(4, '0'));
				}
			});
		});

		$('#btnAddRow').on(
				'click',
				function() {

					let idx = 'n' + newIndex++;

					let html = $('#rowTemplate').html().replaceAll('name_0',
							'name_' + idx).replaceAll('typeId_0',
							'typeId_' + idx).replaceAll('jobId_0',
							'jobId_' + idx).replaceAll('rarityId_0',
							'rarityId_' + idx).replaceAll('requiredLevel_0',
							'requiredLevel_' + idx);

					let $row = $(html);

					applyGlobalValues($row);

					$('#cloneTable tbody').append($row);
					$('#globalSetId').trigger('input');
				});

		$(document).on('click', '.btn-delete-row', function() {
			if (!confirm('Delete this item row?')) {
				return;
			}
			$(this).closest('tr').remove();
			$('#globalSetId').trigger('input');
		});

		$(document).on('change', '.raritySelect', function() {
			const color = $(this).find(':selected').data('color');
			$(this).css('color', color || '');
		});

		$('.raritySelect').trigger('change');
	});

})();
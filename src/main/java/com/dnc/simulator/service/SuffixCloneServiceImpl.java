package com.dnc.simulator.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.SuffixItem;
import com.dnc.simulator.model.SuffixItemAbility;
import com.dnc.simulator.model.SuffixItemAbilityStat;
import com.dnc.simulator.model.SuffixItemExtraStat;
import com.dnc.simulator.repository.SuffixItemAbilityRepository;
import com.dnc.simulator.repository.SuffixItemAbilityStatRepository;
import com.dnc.simulator.repository.SuffixItemExtraStatRepository;
import com.dnc.simulator.repository.SuffixItemRepository;

@Service
public class SuffixCloneServiceImpl implements SuffixCloneService {

	private final SuffixItemRepository suffixItemRepository;
	private final SuffixItemAbilityRepository suffixItemAbilityRepository;
	private final SuffixItemExtraStatRepository suffixItemExtraStatRepository;
	private final SuffixItemAbilityStatRepository suffixItemAbilityStatRepository;

	public SuffixCloneServiceImpl(SuffixItemRepository suffixItemRepository,
			SuffixItemAbilityRepository suffixItemAbilityRepository,
			SuffixItemExtraStatRepository suffixItemExtraStatRepository,
			SuffixItemAbilityStatRepository suffixItemAbilityStatRepository) {
		this.suffixItemRepository = suffixItemRepository;
		this.suffixItemAbilityRepository = suffixItemAbilityRepository;
		this.suffixItemExtraStatRepository = suffixItemExtraStatRepository;
		this.suffixItemAbilityStatRepository = suffixItemAbilityStatRepository;
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public int cloneEquipment(Map<Long, SuffixItem> clones) {

		int success = 0;

		/*
		 * ===================================================== 0. DELETE ALL SUFFIX OF
		 * TARGET EQUIPMENT (ONCE) =====================================================
		 */
		if (!clones.isEmpty()) {

			// target equipment id (ทุก clone ใช้ itemId เดียวกัน)
			Long targetItemId = clones.values().iterator().next().getItemId();

			// หา suffix เดิมทั้งหมดของ target
			List<SuffixItem> oldSuffixes = suffixItemRepository.findByItemId(targetItemId);

			for (SuffixItem old : oldSuffixes) {

				Integer oldSuffixItemId = old.getId().intValue();

				// 1. delete ability stats
				List<SuffixItemAbility> abilities = suffixItemAbilityRepository.findBySuffixItemId(oldSuffixItemId);

				for (SuffixItemAbility ab : abilities) {
					if (ab.getAbilityId() != null) {
						suffixItemAbilityStatRepository.deleteByAbilityId(ab.getAbilityId());
					}
				}

				// 2. delete abilities
				suffixItemAbilityRepository.deleteBySuffixItemId(oldSuffixItemId);

				// 3. delete extra stats
				suffixItemExtraStatRepository.deleteBySuffixItemId(oldSuffixItemId);

				// 4. delete suffix item
				suffixItemRepository.delete(old.getId());
			}
		}

		/*
		 * ===================================================== 1. INSERT CLONED SUFFIX
		 * (NEW DATA) =====================================================
		 */
		for (SuffixItem clone : clones.values()) {

			/* ---------- insert suffix item ---------- */
			Long newSuffixItemId = suffixItemRepository.saveAndReturnId(clone);

			/* ---------- insert extra stats ---------- */
			if (clone.getExtraStats() != null && !clone.getExtraStats().isEmpty()) {
				for (SuffixItemExtraStat es : clone.getExtraStats()) {
					es.setId(null);
					es.setSuffixItemId(newSuffixItemId.intValue());
					suffixItemExtraStatRepository.insert(es);
				}
			}

			/* ---------- insert abilities + stats ---------- */
			if (clone.getAbilities() != null && !clone.getAbilities().isEmpty()) {

				for (SuffixItemAbility ability : clone.getAbilities()) {

					ability.setAbilityId(null);
					ability.setSuffixItemId(newSuffixItemId.intValue());

					// 🔑 insert ability แล้วเอา id กลับมาใช้
					Integer newAbilityId = suffixItemAbilityRepository.insert(ability);

					ability.setAbilityId(newAbilityId);

					if (ability.getAbilityStats() != null && !ability.getAbilityStats().isEmpty()) {

						for (SuffixItemAbilityStat stat : ability.getAbilityStats()) {
							stat.setId(null);
							stat.setAbilityId(newAbilityId);
							suffixItemAbilityStatRepository.insert(stat);
						}
					}
				}
			}

			success++;
		}

		return success;
	}

}

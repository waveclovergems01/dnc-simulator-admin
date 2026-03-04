package com.dnc.simulator.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.SuffixItem;
import com.dnc.simulator.model.SuffixItemAbility;
import com.dnc.simulator.model.SuffixItemAbilityStat;
import com.dnc.simulator.model.SuffixItemExtraStat;
import com.dnc.simulator.repository.SuffixItemAbilityRepository;
import com.dnc.simulator.repository.SuffixItemAbilityStatRepository;
import com.dnc.simulator.repository.SuffixItemExtraStatRepository;
import com.dnc.simulator.repository.SuffixItemRepository;

@Service
public class SuffixItemServiceImpl implements SuffixItemService {

	private final SuffixItemRepository suffixItemRepository;
	private final SuffixItemExtraStatRepository extraStatRepository;
	private final SuffixItemAbilityRepository abilityRepository;
	private final SuffixItemAbilityStatRepository abilityStatRepository;

	public SuffixItemServiceImpl(SuffixItemRepository suffixItemRepository,
			SuffixItemExtraStatRepository extraStatRepository, SuffixItemAbilityRepository abilityRepository,
			SuffixItemAbilityStatRepository abilityStatRepository) {

		this.suffixItemRepository = suffixItemRepository;
		this.extraStatRepository = extraStatRepository;
		this.abilityRepository = abilityRepository;
		this.abilityStatRepository = abilityStatRepository;
	}

	/* ================= BASIC ================= */

	@Override
	public List<SuffixItem> getAll() {
		return suffixItemRepository.findAll();
	}

	@Override
	public SuffixItem getById(Long id) {
		return suffixItemRepository.findById(id);
	}

	@Override
	public List<SuffixItem> getByItemId(Long itemId) {
		return suffixItemRepository.findByItemId(itemId);
	}

	@Override
	public void save(SuffixItem item) {
		suffixItemRepository.save(item);
	}

	@Override
	public Long saveAndReturnId(SuffixItem item) {
		return suffixItemRepository.saveAndReturnId(item);
	}

	@Override
	public void delete(Long id) {
		suffixItemRepository.delete(id);
	}

	/*
	 * ===================================================== CLONE : Tier 1 → Target
	 * Tier (WITH STATS + ABILITY)
	 * =====================================================
	 */
	@Override
	@Transactional
	public void cloneTier1WithStats(Long itemId, Integer targetTier) {

		if (itemId == null || targetTier == null || targetTier <= 1) {
			throw new IllegalArgumentException("Invalid clone parameters");
		}

		/* ===== 1. load suffix tier 1 ===== */
		List<SuffixItem> tier1Suffixes = suffixItemRepository.findByItemId(itemId).stream()
				.filter(s -> s.getTier() != null && s.getTier() == 1).collect(Collectors.toList());

		if (tier1Suffixes.isEmpty()) {
			throw new RuntimeException("No suffix found in Tier 1");
		}

		for (SuffixItem src : tier1Suffixes) {

			/*
			 * ================================================= 2. CLONE SUFFIX ITEM
			 * =================================================
			 */
			SuffixItem clone = new SuffixItem();
			clone.setItemId(src.getItemId());
			clone.setSuffixTypeId(src.getSuffixTypeId());
			clone.setTier(targetTier);
			clone.setName(buildCloneName(src.getName(), targetTier));

			Long newSuffixItemId = suffixItemRepository.saveAndReturnId(clone);

			/*
			 * ================================================= 3. CLONE EXTRA STATS
			 * =================================================
			 */
			List<SuffixItemExtraStat> extraStats = extraStatRepository.findBySuffixItemId(src.getId().intValue());

			for (SuffixItemExtraStat es : extraStats) {

				SuffixItemExtraStat cloneEs = new SuffixItemExtraStat();
				cloneEs.setSuffixItemId(newSuffixItemId.intValue());
				cloneEs.setStatId(es.getStatId());
				cloneEs.setValueMin(es.getValueMin());
				cloneEs.setValueMax(es.getValueMax());
				cloneEs.setIsPercentage(es.getIsPercentage());

				extraStatRepository.insert(cloneEs);
			}

			/*
			 * ================================================= 4. CLONE ABILITY + ABILITY
			 * STATS =================================================
			 */
			List<SuffixItemAbility> abilities = abilityRepository.findBySuffixItemId(src.getId().intValue());

			for (SuffixItemAbility ab : abilities) {

				SuffixItemAbility cloneAb = new SuffixItemAbility();
				cloneAb.setSuffixItemId(newSuffixItemId.intValue());
				cloneAb.setRawText(ab.getRawText());
				cloneAb.setType(ab.getType());

				Integer newAbilityId = abilityRepository.insert(cloneAb);

				/* ===== ability stats ===== */
				List<SuffixItemAbilityStat> abilityStats = abilityStatRepository.findByAbilityId(ab.getAbilityId());

				for (SuffixItemAbilityStat st : abilityStats) {

					SuffixItemAbilityStat cloneStat = new SuffixItemAbilityStat();
					cloneStat.setAbilityId(newAbilityId);
					cloneStat.setStatId(st.getStatId());
					cloneStat.setValueMin(st.getValueMin());
					cloneStat.setValueMax(st.getValueMax());
					cloneStat.setIsPercentage(st.getIsPercentage());

					abilityStatRepository.insert(cloneStat);
				}
			}
		}
	}

	/* ================= NAME BUILDER ================= */

	private String buildCloneName(String originalName, Integer targetTier) {

		if (originalName == null || !originalName.contains(")")) {
			return originalName;
		}

		String roman = toRoman(targetTier);

		if (roman.isEmpty()) {
			return originalName;
		}

		return originalName.replace(")", " " + roman + ")");
	}

	private String toRoman(int tier) {
		switch (tier) {
		case 2:
			return "II";
		case 3:
			return "III";
		case 4:
			return "IV";
		case 5:
			return "V";
		default:
			return "";
		}
	}
}

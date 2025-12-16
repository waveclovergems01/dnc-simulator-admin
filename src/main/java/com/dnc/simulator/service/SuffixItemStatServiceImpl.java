package com.dnc.simulator.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.SuffixItemAbility;
import com.dnc.simulator.model.SuffixItemAbilityStat;
import com.dnc.simulator.model.SuffixItemExtraStat;
import com.dnc.simulator.repository.SuffixItemAbilityRepository;
import com.dnc.simulator.repository.SuffixItemAbilityStatRepository;
import com.dnc.simulator.repository.SuffixItemExtraStatRepository;

@Service
@Transactional
public class SuffixItemStatServiceImpl implements SuffixItemStatService {

	private final SuffixItemAbilityRepository abilityRepository;
	private final SuffixItemAbilityStatRepository abilityStatRepository;
	private final SuffixItemExtraStatRepository extraStatRepository;

	public SuffixItemStatServiceImpl(SuffixItemAbilityRepository abilityRepository,
			SuffixItemAbilityStatRepository abilityStatRepository, SuffixItemExtraStatRepository extraStatRepository) {

		this.abilityRepository = abilityRepository;
		this.abilityStatRepository = abilityStatRepository;
		this.extraStatRepository = extraStatRepository;
	}

	/* ===================== Ability ===================== */

	@Override
	public List<SuffixItemAbility> getAbilities(Integer suffixItemId) {
		if (suffixItemId == null || suffixItemId <= 0) {
			return Collections.emptyList();
		}
		return abilityRepository.findBySuffixItemId(suffixItemId);
	}

	@Override
	public SuffixItemAbility getAbilityWithStats(Integer abilityId) {
		if (abilityId == null || abilityId <= 0) {
			return null;
		}

		SuffixItemAbility ability = abilityRepository.findById(abilityId);
		if (ability != null) {
			ability.setAbilityStats(abilityStatRepository.findByAbilityId(abilityId));
		}
		return ability;
	}

	@Override
	public List<SuffixItemAbility> getAbilitiesWithStatsBySuffixItemId(Integer suffixItemId) {
		if (suffixItemId == null || suffixItemId <= 0) {
			return Collections.emptyList();
		}

		List<SuffixItemAbility> abilities = abilityRepository.findBySuffixItemId(suffixItemId);

		for (SuffixItemAbility ability : abilities) {
			ability.setAbilityStats(abilityStatRepository.findByAbilityId(ability.getAbilityId()));
		}

		return abilities;
	}

	@Override
	public Integer saveAbility(SuffixItemAbility ability) {

		if (ability == null || ability.getSuffixItemId() == null) {
			throw new IllegalArgumentException("Invalid ability data");
		}

		if (ability.getAbilityId() == null) {
			return abilityRepository.insert(ability);
		} else {
			abilityRepository.update(ability);
			return ability.getAbilityId();
		}
	}

	@Override
	public void deleteAbility(Integer abilityId) {
		if (abilityId != null) {
			abilityRepository.delete(abilityId);
		}
	}

	/* ===================== Ability Stat ===================== */

	@Override
	public List<SuffixItemAbilityStat> getAbilityStats(Integer abilityId) {
		if (abilityId == null || abilityId <= 0) {
			return Collections.emptyList();
		}
		return abilityStatRepository.findByAbilityId(abilityId);
	}

	@Override
	public Integer saveAbilityStat(SuffixItemAbilityStat stat) {

		if (stat == null || stat.getAbilityId() == null) {
			throw new IllegalArgumentException("Invalid ability stat data");
		}

		if (stat.getId() == null) {
			return abilityStatRepository.insert(stat);
		} else {
			abilityStatRepository.update(stat);
			return stat.getId();
		}
	}

	@Override
	public void deleteAbilityStat(Integer id) {
		if (id != null) {
			abilityStatRepository.delete(id);
		}
	}

	/* ===================== Extra Stat ===================== */

	@Override
	public List<SuffixItemExtraStat> getExtraStats(Integer suffixItemId) {
		if (suffixItemId == null || suffixItemId <= 0) {
			return Collections.emptyList();
		}
		return extraStatRepository.findBySuffixItemId(suffixItemId);
	}

	@Override
	public Integer saveExtraStat(SuffixItemExtraStat stat) {

		if (stat == null || stat.getSuffixItemId() == null) {
			throw new IllegalArgumentException("Invalid extra stat data");
		}

		if (stat.getId() == null) {
			return extraStatRepository.insert(stat);
		} else {
			extraStatRepository.update(stat);
			return stat.getId();
		}
	}

	@Override
	public void deleteExtraStat(Integer id) {
		if (id != null) {
			extraStatRepository.delete(id);
		}
	}

	/* ===================== Extra Stat (เพิ่มใหม่) ===================== */

	@Override
	public void deleteExtraStatsBySuffixItemId(Integer suffixItemId) {
		if (suffixItemId != null && suffixItemId > 0) {
			extraStatRepository.deleteBySuffixItemId(suffixItemId);
		}
	}

	@Override
	public List<SuffixItemExtraStat> buildDefaultExtraStats(Integer suffixTypeId) {

		// ตอนนี้ยังไม่มี source default จริง
		// จึง return empty list เพื่อให้ form ไม่พัง
		if (suffixTypeId == null || suffixTypeId <= 0) {
			return Collections.emptyList();
		}

		return new ArrayList<>();
	}
}

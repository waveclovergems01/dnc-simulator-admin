package com.dnc.simulator.service;

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

	private final SuffixItemExtraStatRepository extraStatRepository;
	private final SuffixItemAbilityRepository abilityRepository;
	private final SuffixItemAbilityStatRepository abilityStatRepository;

	public SuffixItemStatServiceImpl(SuffixItemExtraStatRepository extraStatRepository,
			SuffixItemAbilityRepository abilityRepository, SuffixItemAbilityStatRepository abilityStatRepository) {

		this.extraStatRepository = extraStatRepository;
		this.abilityRepository = abilityRepository;
		this.abilityStatRepository = abilityStatRepository;
	}

	/*
	 * ===================================================== EXTRA STAT (ของเดิม)
	 * =====================================================
	 */

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

	@Override
	public void deleteExtraStatsBySuffixItemId(Integer suffixItemId) {
		if (suffixItemId != null && suffixItemId > 0) {
			extraStatRepository.deleteBySuffixItemId(suffixItemId);
		}
	}

	@Override
	public List<SuffixItemExtraStat> buildDefaultExtraStats(Integer suffixTypeId) {
		// ตอนนี้ยังไม่มี default จริง
		return Collections.emptyList();
	}

	/*
	 * ===================================================== ABILITY
	 * =====================================================
	 */

	@Override
	public List<SuffixItemAbility> getAbilities(Integer suffixItemId) {
		if (suffixItemId == null || suffixItemId <= 0) {
			return Collections.emptyList();
		}
		return abilityRepository.findBySuffixItemId(suffixItemId);
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

	/*
	 * ===================================================== ABILITY STAT
	 * =====================================================
	 */

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

	/*
	 * ===================================================== IMPORTANT
	 * =====================================================
	 */

	@Override
	public void deleteAbilityStatsByAbilityId(Integer abilityId) {
		if (abilityId != null && abilityId > 0) {
			abilityStatRepository.deleteByAbilityId(abilityId);
		}
	}
	
	@Override
	public void deleteAbilityBySuffixItemId(Integer suffixItemId) {
		if (suffixItemId != null && suffixItemId > 0) {
			List<SuffixItemAbility> ability = abilityRepository.findBySuffixItemId(suffixItemId);
			if(ability != null) {
				abilityStatRepository.deleteByAbilityId(ability.get(0).getAbilityId());
				abilityRepository.deleteBySuffixItemId(ability.get(0).getSuffixItemId());
			}
			
		}
	}
}

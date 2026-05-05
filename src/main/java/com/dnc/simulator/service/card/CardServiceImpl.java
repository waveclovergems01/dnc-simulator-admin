package com.dnc.simulator.service.card;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.card.Card;
import com.dnc.simulator.model.card.CardStat;
import com.dnc.simulator.repository.card.CardRepository;

@Service
public class CardServiceImpl implements CardService {

	private final CardRepository cardRepository;

	public CardServiceImpl(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@Override
	public List<Card> findAll() {
		return cardRepository.findAll();
	}

	@Override
	public Card findById(Long id) {
		return cardRepository.findById(id).orElse(null);
	}

	@Override
	public Card findIconById(Long id) {
		return cardRepository.findIconById(id).orElse(null);
	}

	@Override
	public List<CardStat> findStatsByCardId(Long cardId) {
		return cardRepository.findStatsByCardId(cardId);
	}

	@Override
	public boolean existsDuplicate(Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId,
			Long excludeId) {
		return cardRepository.existsDuplicate(typeId, cardLevelId, cardNameId, rarityId, excludeId);
	}

	@Override
	@Transactional
	public Long create(Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId, Integer slotNumber,
			List<CardStat> statList) {

		validate(typeId, cardLevelId, cardNameId, rarityId, slotNumber, statList);

		if (cardRepository.existsDuplicate(typeId, cardLevelId, cardNameId, rarityId, null)) {
			throw new RuntimeException("Duplicate card: same Item Type + Level + Card Name + Rarity already exists");
		}

		Long cardId = cardRepository.insert(typeId, cardLevelId, cardNameId, rarityId, slotNumber);
		cardRepository.replaceStats(cardId, statList);

		return cardId;
	}

	@Override
	@Transactional
	public void update(Long id, Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId, Integer slotNumber,
			List<CardStat> statList) {

		if (id == null) {
			throw new RuntimeException("id is required");
		}

		validate(typeId, cardLevelId, cardNameId, rarityId, slotNumber, statList);

		if (cardRepository.existsDuplicate(typeId, cardLevelId, cardNameId, rarityId, id)) {
			throw new RuntimeException("Duplicate card: same Item Type + Level + Card Name + Rarity already exists");
		}

		cardRepository.update(id, typeId, cardLevelId, cardNameId, rarityId, slotNumber);
		cardRepository.replaceStats(id, statList);
	}

	@Override
	public void delete(Long id) {
		if (id == null) {
			throw new RuntimeException("id is required");
		}

		cardRepository.delete(id);
	}

	private void validate(Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId, Integer slotNumber,
			List<CardStat> statList) {

		if (typeId == null) {
			throw new RuntimeException("typeId is required");
		}

		if (cardLevelId == null) {
			throw new RuntimeException("cardLevelId is required");
		}

		if (cardNameId == null) {
			throw new RuntimeException("cardNameId is required");
		}

		if (rarityId == null) {
			throw new RuntimeException("rarityId is required");
		}

		if (slotNumber == null) {
			throw new RuntimeException("slotNumber is required");
		}

		if (slotNumber.intValue() <= 0) {
			throw new RuntimeException("slotNumber must be greater than 0");
		}

		if (statList == null || statList.isEmpty()) {
			throw new RuntimeException("At least one stat is required");
		}

		Set<Integer> duplicateCheck = new HashSet<Integer>();

		for (CardStat stat : statList) {
			if (stat == null) {
				continue;
			}

			if (stat.getStatId() == null) {
				throw new RuntimeException("statId is required");
			}

			if (stat.getValueMin() == null) {
				throw new RuntimeException("Min value is required");
			}

			if (stat.getValueMax() == null) {
				throw new RuntimeException("Max value is required");
			}

			if (stat.getValueMin().doubleValue() < 0) {
				throw new RuntimeException("Min value must be >= 0");
			}

			if (stat.getValueMax().doubleValue() < 0) {
				throw new RuntimeException("Max value must be >= 0");
			}

			if (stat.getValueMin().doubleValue() > stat.getValueMax().doubleValue()) {
				throw new RuntimeException("Min value must be less than or equal Max value");
			}

			if (stat.getIsPercentage() == null) {
				stat.setIsPercentage(0);
			}

			if (!(stat.getIsPercentage().intValue() == 0 || stat.getIsPercentage().intValue() == 1)) {
				throw new RuntimeException("isPercentage must be 0 or 1");
			}

			if (duplicateCheck.contains(stat.getStatId())) {
				throw new RuntimeException("Duplicate stat in same card rarity");
			}

			duplicateCheck.add(stat.getStatId());
		}
	}
}
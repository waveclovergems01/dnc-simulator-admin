package com.dnc.simulator.service.card;

import java.util.List;

import com.dnc.simulator.model.card.Card;
import com.dnc.simulator.model.card.CardStat;

public interface CardService {

	List<Card> findAll();

	Card findById(Long id);

	Card findIconById(Long id);

	List<CardStat> findStatsByCardId(Long cardId);

	boolean existsDuplicate(Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId, Long excludeId);

	Long create(Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId, Integer slotNumber,
			List<CardStat> statList);

	void update(Long id, Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId, Integer slotNumber,
			List<CardStat> statList);

	void delete(Long id);
}
package com.dnc.simulator.repository.card;

import java.util.List;
import java.util.Optional;

import com.dnc.simulator.model.card.Card;
import com.dnc.simulator.model.card.CardStat;

public interface CardRepository {

	void ensureTable();

	List<Card> findAll();

	Optional<Card> findById(Long id);

	Optional<Card> findIconById(Long id);

	List<CardStat> findStatsByCardId(Long cardId);

	boolean existsDuplicate(Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId, Long excludeId);

	Long insert(Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId, Integer slotNumber);

	int update(Long id, Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId, Integer slotNumber);

	int delete(Long id);

	void deleteStatsByCardId(Long cardId);

	void insertStat(Long cardId, Integer statId, Double valueMin, Double valueMax, Integer isPercentage);

	void replaceStats(Long cardId, List<CardStat> statList);
}
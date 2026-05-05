package com.dnc.simulator.model.card;

import java.util.ArrayList;
import java.util.List;

public class Card {

	private Long id;
	private Integer typeId;
	private Long cardLevelId;
	private Long cardNameId;
	private Integer rarityId;
	private Integer slotNumber;

	private String cardName;
	private String itemTypeName;
	private Integer level;
	private String rarityName;
	private String color;

	private byte[] iconBlob;
	private String iconMime;
	private String iconName;

	private List<CardStat> statList = new ArrayList<CardStat>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Long getCardLevelId() {
		return cardLevelId;
	}

	public void setCardLevelId(Long cardLevelId) {
		this.cardLevelId = cardLevelId;
	}

	public Long getCardNameId() {
		return cardNameId;
	}

	public void setCardNameId(Long cardNameId) {
		this.cardNameId = cardNameId;
	}

	public Integer getRarityId() {
		return rarityId;
	}

	public void setRarityId(Integer rarityId) {
		this.rarityId = rarityId;
	}

	public Integer getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(Integer slotNumber) {
		this.slotNumber = slotNumber;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getItemTypeName() {
		return itemTypeName;
	}

	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getRarityName() {
		return rarityName;
	}

	public void setRarityName(String rarityName) {
		this.rarityName = rarityName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public byte[] getIconBlob() {
		return iconBlob;
	}

	public void setIconBlob(byte[] iconBlob) {
		this.iconBlob = iconBlob;
	}

	public String getIconMime() {
		return iconMime;
	}

	public void setIconMime(String iconMime) {
		this.iconMime = iconMime;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public List<CardStat> getStatList() {
		return statList;
	}

	public void setStatList(List<CardStat> statList) {
		this.statList = statList;
	}
}
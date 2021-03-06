package taki.common;

import java.io.Serializable;
import java.util.HashMap;

public class GameCard implements Serializable {

	private static final long serialVersionUID = 2284472941729695106L;

	public enum CardType {
		ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, STOP, PLUS_TWO, TAKI, SWITCH_DIRECTION, SUPER_TAKI, CHANGE_COLOR
	}

	public enum CardColor {
		BLUE, GREEN, YELLOW, RED, SPECIAL
	}

	public GameCard(CardType type, CardColor color) {
		setCardType(type);
		setCardColor(color);

		setEnumMap();
	}

	private HashMap<String, String> _enumMap;

	private CardType _cardType;

	public CardType getCardType() {
		return _cardType;
	}

	public void setCardType(CardType cardType) {
		_cardType = cardType;
	}

	private CardColor _cardColor;

	public CardColor getCardColor() {
		return _cardColor;
	}

	public void setCardColor(CardColor color) {
		_cardColor = color;
	}

	public boolean isCardSpecial() {
		return (_cardType == CardType.CHANGE_COLOR || _cardType == _cardType.SWITCH_DIRECTION
				|| _cardType == _cardType.TAKI || _cardType == _cardType.SUPER_TAKI || _cardType == _cardType.STOP
				|| _cardType == _cardType.PLUS_TWO);
	}

	private void setEnumMap() {
		_enumMap = new HashMap<>();

		_enumMap.put(CardType.ONE.name(), "1");
		_enumMap.put(CardType.TWO.name(), "2");
		_enumMap.put(CardType.THREE.name(), "3");
		_enumMap.put(CardType.FOUR.name(), "4");
		_enumMap.put(CardType.FIVE.name(), "5");
		_enumMap.put(CardType.SIX.name(), "6");
		_enumMap.put(CardType.SEVEN.name(), "7");
		_enumMap.put(CardType.EIGHT.name(), "8");
		_enumMap.put(CardType.NINE.name(), "9");
		_enumMap.put(CardType.TEN.name(), "10");
		_enumMap.put(CardType.STOP.name(), "Stop");
		_enumMap.put(CardType.PLUS_TWO.name(), "+2");
		_enumMap.put(CardType.TAKI.name(), "Taki");
		_enumMap.put(CardType.SWITCH_DIRECTION.name(), "Switch Direction");
		_enumMap.put(CardType.CHANGE_COLOR.name(), "Change Color");
		_enumMap.put(CardType.SUPER_TAKI.name(), "Super Taki");

		_enumMap.put(CardColor.BLUE.name(), "Blue");
		_enumMap.put(CardColor.GREEN.name(), "Green");
		_enumMap.put(CardColor.RED.name(), "Red");
		_enumMap.put(CardColor.YELLOW.name(), "Yellow");
		_enumMap.put(CardColor.SPECIAL.name(), "Special");
	}

	public int toInt() {
		int i = -1;
		if (!isCardSpecial()) {
			try {
				i = Integer.parseInt(_enumMap.get(_cardType.name()));
			} catch (NumberFormatException e) {
				System.out.println("Error in getting card as a number!");
			}
		}
		return i;
	}

	public String toString() {

		String strCardName = _enumMap.get(_cardType.name()) + " (" + _enumMap.get(_cardColor.name()) + ")";

		return strCardName;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof GameCard) {
			GameCard card = (GameCard) obj;
			return card.getCardColor() == this.getCardColor() &&
					card.getCardType() == this.getCardType();
		} else {
			return false;
		}
	}
	
	
}

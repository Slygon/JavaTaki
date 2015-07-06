package taki.common;

import java.io.Serializable;
import java.util.ArrayList;

public class CardList implements Serializable {
	
	private static final long serialVersionUID = 3590387990390645185L;
	
	public ArrayList<GameCard> _cards;
	
	public ArrayList<GameCard> getCards() {
		return _cards;
	}

	public void setCards(ArrayList<GameCard> cards) {
		_cards = cards;
	}

	public CardList(ArrayList<GameCard> cards) {
		_cards = cards;
	}

}

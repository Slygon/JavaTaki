package taki.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import taki.common.GameCard;
import taki.common.GameCard.CardColor;
import taki.common.GameCard.CardType;

public class GameLogic {

	final private int DEFAULT_NUM_DECKS = 2;
	final private int DECK_SIZE = 56;
	final private int INITIAL_HAND_SIZE = 8;

	private ArrayList<GameCard> _deck;
	private HashMap<String, ArrayList<GameCard>> _players;
	private String _currPlayer;

	public HashMap<String, ArrayList<GameCard>> getPlayers() {
		return _players;
	}

	public void setPlayers(String[] players) {
		_players = new HashMap<String, ArrayList<GameCard>>();
		
		for (String player : players) {
			_players.put(player, new ArrayList<>());
		}
		_currPlayer = players[0];
	}

	public ArrayList<GameCard> getDeck() {
		return _deck;
	}

	public GameLogic() {
		_deck = new ArrayList<GameCard>(Arrays.asList(generateDeck(DEFAULT_NUM_DECKS)));
		shuffleCards();
	}

	public void startGame() {
		handCardsToPlayers();
	}
	
	private void handCardsToPlayers() {
		if (_players != null) {
			String[] playerNames = new String[_players.size()];
			playerNames = _players.keySet().toArray(playerNames);
			for (String player : playerNames) {
				for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
					_players.get(player).add(_deck.get(0));
					_deck.remove(0);
				}
			}
		}
	}
	
	private void shuffleCards() {
		Collections.shuffle(_deck);
	}
	
	private GameCard[] generateDeck(int nDecks) {
		GameCard[] deck = new GameCard[DECK_SIZE * nDecks];
		CardType[] cardTypes = CardType.values();
		CardColor[] cardColors = CardColor.values();

		for (int deckIndex = 0, nCardIndex = 0; deckIndex < nDecks; deckIndex++) {
			for (int typeIndex = 0; typeIndex < cardTypes.length; typeIndex++) {
				CardType type = cardTypes[typeIndex];
				CardColor color = CardColor.SPECIAL;

				if (type != CardType.SWITCH_DIRECTION && type != CardType.CHANGE_COLOR && type != CardType.SUPER_TAKI) {
					for (int colorIndex = 0; colorIndex < cardColors.length - 1; colorIndex++, nCardIndex++) {
						color = cardColors[colorIndex];
						deck[nCardIndex] = new GameCard(type, color);
					}
				} else {
					deck[nCardIndex++] = new GameCard(type, color);
					if (type == CardType.CHANGE_COLOR) {
						deck[nCardIndex++] = new GameCard(type, color);
					}
				}
			}
		}
		return deck;
	}
}

package taki.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observer;

import taki.common.GameCard;
import taki.common.GameCard.CardColor;
import taki.common.GameCard.CardType;
import taki.common.GameState;

public class GameLogic {

	final private int DEFAULT_NUM_DECKS = 2;
	final private int DECK_SIZE = 56;
	final private int INITIAL_HAND_SIZE = 8;

	private GameState _gameState;
	private Observer _observer;

	public Observer getObserver() {
		return _observer;
	}

	public void setObserver(Observer observer) {
		_observer = observer;
	}

	public GameState getGameState() {
		return _gameState;
	}

	public GameLogic() {
		_gameState = new GameState();
		_gameState.setGameDeck(new ArrayList<GameCard>(Arrays.asList(generateDeck(DEFAULT_NUM_DECKS))));
		shuffleCards();
	}

	public void startGame() {
	}

	private void handCardsToPlayer(String playerName) {
		HashMap<String, ArrayList<GameCard>> players = _gameState.getPlayers();
		ArrayList<GameCard> gameDeck = _gameState.getGameDeck();
		if (players != null) {
			if (players.containsKey(playerName)) {
				for (int i = 0; i < INITIAL_HAND_SIZE && gameDeck.size() > 0; i++) {
					players.get(playerName).add(gameDeck.get(0));
					gameDeck.remove(0);
				}
			}
		}
	}

	private void shuffleCards() {
		Collections.shuffle(_gameState.getGameDeck());
	}

	public void playerJoined(String playerName) {
		getGameState().getPlayers().put(playerName, new ArrayList<GameCard>());
		if (getGameState().getCurrPlayer() == null)
			getGameState().setCurrPlayer(playerName);
		handCardsToPlayer(playerName);
	}

	public void playerLeft(String playerName) {
		if (_gameState.getCurrPlayer() != null &&
			_gameState.getCurrPlayer().equals(playerName)) {
			switchTurn();
		}
		ArrayList<GameCard> userCards = _gameState.getPlayers().get(playerName);
		_gameState.getPlayers().remove(playerName);
		_gameState.getGameDeck().addAll(userCards);
	}
	
	public boolean playerChoseCard(String playerName, GameCard card) {
		boolean bIsValid = true;
		int nTurnsToSkip = 0;
		String strNewColorMsg = "";
		
		if (getGameState().getCurrPlayer().equals(playerName)) {

			GameCard firstCard = getGameState().getFirstCard();
			GameCard lastCard = getGameState().getLastCard();

			switch (card.getCardType()) {
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
			case TEN: {
				// This is the first move
				if (firstCard == null) {
					_gameState.addLastCard(card);
					nTurnsToSkip++;
				}
				// Moves have been made by the player
				else {
					// If the last turn was a first card
					if (_gameState.getMoveCount() == 1) {

						// Convert SuperTaki into normal Taki of this card's color 
						if (firstCard.getCardType() == CardType.SUPER_TAKI) {

							_gameState.clearMoves();

							_gameState.addLastCard(new GameCard(CardType.TAKI, card.getCardColor()));
							_gameState.addLastCard(card);
							break;
						}
						// Just set the new card color
						else if (firstCard.getCardType() == CardType.CHANGE_COLOR) {
							_gameState.clearMoves();
							_gameState.addLastCard(card);
							strNewColorMsg = "New color Set according to " + card.toString();
							nTurnsToSkip++;
							break;
						}
					}

					if (firstCard.getCardType() == CardType.PLUS_TWO && _gameState.getMoveCount() > 2) {
						nTurnsToSkip++;
					}

					// Check if move is valid to color
					// Either special card or colors matching
					bIsValid = (lastCard.getCardType() == CardType.CHANGE_COLOR ||
								firstCard.getCardColor() == CardColor.SPECIAL ||
								firstCard.getCardColor() == card.getCardColor() ||
								lastCard.toInt() == card.toInt());

					if (bIsValid) {
						_gameState.addLastCard(card);
						
						if (firstCard.getCardType() != CardType.TAKI) {
							nTurnsToSkip++;
						}
					}
				}

				break;
			}
			case STOP: {
				nTurnsToSkip++;
				nTurnsToSkip++;
				break;
			}
			case CHANGE_COLOR:
			case TAKI:
			case SUPER_TAKI:
			case PLUS_TWO: {
				_gameState.clearMoves();
				_gameState.addLastCard(card);
				break;
			}
			case SWITCH_DIRECTION: {
				// Switch to opposite direction
				_gameState.setDirectionOpposite(!_gameState.isDirectionOpposite());
				nTurnsToSkip++;
				break;
			}
			default:
				break;
			}

		}
		// This is the wrong player's turn
		else {
			printMsg(
					"Wrong player!\n" + playerName + " tried to take " + _gameState.getCurrPlayer() + "'s turn.");
			bIsValid = false;
		}

		if (bIsValid) {
			printMsg(playerName + " played with " + card.toString());
			if (!strNewColorMsg.equals("")) {
				printMsg(strNewColorMsg);
				strNewColorMsg = "";
			}				
			
			_gameState.getPlayers().get(playerName).remove(card);
			_gameState.getBurnedCards().add(card);
			
			if (_gameState.getPlayers().get(playerName).size() == 0) {
				printMsg(playerName + " has won !");
			}
		} else {
			printMsg(playerName + ", you can't play with " + card.toString() + " now");
		}
		
		for (int i = 0; i < nTurnsToSkip; i++) {
			switchTurn();
		}

		return bIsValid;
	}

	public void switchTurn() {
		clearStatesButKeepLastColor();
		String currPlayer = _gameState.getCurrPlayer();
		String[] arrPlayerNames = new String[_gameState.getPlayers().size()];
		arrPlayerNames = _gameState.getPlayers().keySet().toArray(arrPlayerNames);

		// Find the current player
		int i = 0;
		for (; i < arrPlayerNames.length; i++) {
			if (arrPlayerNames[i].equals(currPlayer)) {
				// Move to next player (Depending on direction)
				if (!_gameState.isDirectionOpposite()) {
					i++;
				} else {
					i--;
				}
				i = (i + arrPlayerNames.length) % arrPlayerNames.length;
				break;
			}
		}
		_gameState.setCurrPlayer(arrPlayerNames[i]);
		printMsg("It's " + arrPlayerNames[i] + "'s turn");
	}
	
	public boolean takeNewCardFromDeck(String playerName) {

		boolean bIsValid = true;
		
		if(playerName.equals(_gameState.getCurrPlayer())) {
			if (_gameState.getGameDeck().size() > 0) {
				GameCard card = _gameState.getGameDeck().get(0);
				_gameState.getPlayers().get(playerName).add(card);
				_gameState.getGameDeck().remove(card);
				
				printMsg(playerName + " took a card from the deck");
			}
			switchTurn();
		}
		// This is the wrong player's turn
		else {
			printMsg(
					"Wrong player!\n" + playerName + " tried to take " + _gameState.getCurrPlayer() + "'s turn.");
			bIsValid = false;
		}
		return bIsValid;
	}
	
	public void clearStatesButKeepLastColor() {
		GameCard lastCard = _gameState.getLastCard();
		_gameState.clearMoves();
		if (lastCard != null && !lastCard.isCardSpecial()) {
			_gameState.addLastCard(lastCard);
		}
	}
	
	private void printMsg(String strMsg) {
		System.out.println(strMsg);
		
		if (_observer != null) {
			_observer.update(null, strMsg);
		}
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

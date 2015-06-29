package taki.common;

import java.io.Serializable;

public class GameMessage implements Serializable {

	private static final long serialVersionUID = -8543098553033786517L;
	
	public enum CardType {
		ZERO,
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		TEN,
		JACK,
		QUEEN,
		KING,
		ACE,
		STOP,		
		CRAZYCARD,
		PLUS,
		PLUSTWO,
		PLUSTHREE,
		SWITCHDIR,
		SWITCHCOLOR,
		TAKI,
		SUPERTAKI
	}
	
	public enum GameAction {
		CHANGE_TURN,
		MOVE
	}
	
	private CardType _cardType;
	private String _playerName;
	private GameAction _gameAction;
	
	public CardType getCardType() {
		return _cardType;
	}

	public void setCardType(CardType cardType) {
		_cardType = cardType;
	}
	
	public GameMessage() {
		
	}
	
	public void setTurn(String playerName) {
		_playerName = playerName;
		_gameAction = GameAction.CHANGE_TURN;
		
	}
	
	public void makeMove(String playerName, CardType cardType) {
		_cardType = cardType;
		_playerName = playerName;
		_gameAction = GameAction.MOVE;
	}
}

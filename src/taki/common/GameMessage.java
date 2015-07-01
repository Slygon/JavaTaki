package taki.common;

import java.io.Serializable;

public class GameMessage implements Serializable {

	private static final long serialVersionUID = -8543098553033786517L;
	
	
	public enum GameAction {
		CHANGE_TURN,
		MOVE
	}
	
	private String _playerName;
	private GameAction _gameAction;
	private GameCard _card;

	public GameMessage() {
		
	}
	
	public void setTurn(String playerName) {
		_playerName = playerName;
		_gameAction = GameAction.CHANGE_TURN;
		
	}
	
	public void makeMove(String playerName, GameCard card) {
		_card = card;
		_playerName = playerName;
		_gameAction = GameAction.MOVE;
	}
}

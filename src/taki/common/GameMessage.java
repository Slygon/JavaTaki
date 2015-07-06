package taki.common;

import java.io.Serializable;

public class GameMessage implements Serializable {

	private static final long serialVersionUID = -8543098553033786517L;
	
	public enum ClientAction {
		CHOSE_CARD,
		TAKE_CARD_FROM_DECK,
		SERVER_MESSAGE
	}
	
	private String _serverMsg = "";
	
	public String getServerMsg() {
		return _serverMsg;
	}
	public void setServerMsg(String serverMsg) {
		_serverMsg = serverMsg;
	}

	private GameState _gameState;
	
	private String _playerName;
	private ClientAction _action;
	private GameCard _card;
	
	public String getPlayerName() {
		return _playerName;
	}
	public void setPlayerName(String _playerName) {
		this._playerName = _playerName;
	}
	
	public GameCard getCard() {
		return _card;
	}
	public void setCard(GameCard _card) {
		this._card = _card;
	}
	
	public ClientAction getClientAction() {
		return _action;
	}
	public void setClientAction(ClientAction _action) {
		this._action = _action;
	}

	public GameState getGameState() {
		return _gameState;
	}
	public void setGameState(GameState _gameState) {
		this._gameState = _gameState;
	}
	
	public GameMessage(GameState state) {
		_gameState = state;
	}

	public GameMessage(ClientAction action) {
		_action = action;
	}
	
	public GameMessage(ClientAction action, GameCard card) {
		_action = action;
		_card = card;
	}
	
	public GameMessage(String serverMsg) {
		_serverMsg = serverMsg;
	}
	
	public GameMessage(String playerName, ClientAction action, GameCard card) {
		_action = action;
		_card = card;
		_playerName = playerName;
	}
}

package taki.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class GameState {

	boolean _isDirectionOpposite = false;
	private ArrayList<GameCard> _gameDeck;
	private ArrayList<GameCard> _lastMoves;
	private ArrayList<GameCard> _burnedCards;
	private HashMap<String, ArrayList<GameCard>> _players;
	private String _currPlayer;

	public ArrayList<GameCard> getBurnedCards() {
		return _burnedCards;
	}

	public GameCard getLastCard() {
		if (_lastMoves.size() > 0) {
			return _lastMoves.get(_lastMoves.size() - 1);
		}
		return null;
	}

	public GameCard getFirstCard() {
		if (_lastMoves.size() > 0) {
			return _lastMoves.get(0);
		}
		return null;
	}

	public int getMoveCount() {
		return _lastMoves.size();
	}

	public void addLastCard(GameCard lastCard) {
		_lastMoves.add(lastCard);
	}

	public void clearMoves() {
		_lastMoves.clear();
	}

	public ArrayList<GameCard> getGameDeck() {
		return _gameDeck;
	}

	public void setGameDeck(ArrayList<GameCard> _gameDeck) {
		this._gameDeck = _gameDeck;
	}

	public String getCurrPlayer() {
		return _currPlayer;
	}

	public void setCurrPlayer(String _currPlayer) {
		this._currPlayer = _currPlayer;
	}

	public HashMap<String, ArrayList<GameCard>> getPlayers() {
		return _players;
	}

	public void setPlayers(String[] players) {
		_players = new LinkedHashMap<String, ArrayList<GameCard>>();

		if (players != null) {
			for (String player : players) {
				_players.put(player, new ArrayList<>());
			}
			_currPlayer = players[0];
		}
	}

	public boolean isDirectionOpposite() {
		return _isDirectionOpposite;
	}

	public void setDirectionOpposite(boolean isDirectionOpposite) {
		if (_isDirectionOpposite = isDirectionOpposite) {
			System.out.println("Direction switched !");
		}

		_isDirectionOpposite = isDirectionOpposite;

	}

	public GameState() {
		setPlayers(null);
		_lastMoves = new ArrayList<GameCard>();
		_burnedCards = new ArrayList<GameCard>();
	}
}

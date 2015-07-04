package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taki.common.GameCard;
import taki.common.GameCard.CardColor;
import taki.common.GameCard.CardType;
import taki.server.GameLogic;

public class GameLogicTest {

	private GameLogic _gameLogic;
	private static String[] _players;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_players = new String[] { "Tal", "Moshe", "Itzik" };
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		_players = null;
	}

	@Before
	public void setUp() throws Exception {
		_gameLogic = new GameLogic();
	}

	@After
	public void tearDown() throws Exception {
		_gameLogic = null;
	}

	@Test
	public void test_Setup_SetPlayers() {
		_gameLogic.getGameState().setPlayers(_players);
	}

	@Test
	public void test_Join_3Players() {
		_gameLogic.playerJoined("Maor");
		_gameLogic.playerJoined("Omri");
		_gameLogic.playerJoined("Itai");
	}

	@Test
	public void test_Logic_SwitchTurn() {
		_gameLogic.playerJoined("Maor");
		_gameLogic.playerJoined("Omri");
		_gameLogic.playerJoined("Itai");

		_gameLogic.switchTurn();
		assertTrue(_gameLogic.getGameState().getCurrPlayer().equals("Omri"));

		_gameLogic.switchTurn();
		assertTrue(_gameLogic.getGameState().getCurrPlayer().equals("Itai"));

		_gameLogic.switchTurn();
		assertTrue(_gameLogic.getGameState().getCurrPlayer().equals("Maor"));
	}

	@Test
	public void test_Logic_SwitchDirectionAndSwitchTurn() {
		_gameLogic.playerJoined("Maor");
		_gameLogic.playerJoined("Omri");
		_gameLogic.playerJoined("Itai");

		_gameLogic.switchTurn();
		assertTrue(_gameLogic.getGameState().getCurrPlayer().equals("Omri"));

		_gameLogic.playerChoseCard("Omri", new GameCard(CardType.SWITCH_DIRECTION, CardColor.SPECIAL));
		assertTrue(_gameLogic.getGameState().getCurrPlayer().equals("Maor"));

		_gameLogic.switchTurn();
		assertTrue(_gameLogic.getGameState().getCurrPlayer().equals("Itai"));
	}

	@Test
	public void test_Play_ChooseCard() {
		_gameLogic.playerJoined("Maor");
		_gameLogic.playerChoseCard(_gameLogic.getGameState().getCurrPlayer(),
				new GameCard(CardType.EIGHT, CardColor.BLUE));
	}

	@Test
	public void test_Play_2Players_ChoseCard() {
		_gameLogic.playerJoined("Maor");
		_gameLogic.playerJoined("Omri");
	}

	@Test
	public void test_Play_InGame() {
		_gameLogic.playerJoined("Maor");
		_gameLogic.playerJoined("Omri");
		_gameLogic.playerChoseCard(_gameLogic.getGameState().getCurrPlayer(),
				new GameCard(CardType.EIGHT, CardColor.BLUE));
		_gameLogic.playerJoined("Itai");
	}

	@Test
	public void test_Card_ChangeColor() {
		_gameLogic.playerJoined("Maor");
		_gameLogic.playerJoined("Omri");
		_gameLogic.playerJoined("Itai");

		assertTrue(_gameLogic.playerChoseCard(_gameLogic.getGameState().getCurrPlayer(),
				new GameCard(CardType.EIGHT, CardColor.BLUE)));
		assertTrue(_gameLogic.playerChoseCard(_gameLogic.getGameState().getCurrPlayer(),
				new GameCard(CardType.NINE, CardColor.BLUE)));
		assertFalse(_gameLogic.playerChoseCard(_gameLogic.getGameState().getCurrPlayer(),
				new GameCard(CardType.EIGHT, CardColor.RED)));
		assertTrue(_gameLogic.playerChoseCard(_gameLogic.getGameState().getCurrPlayer(),
				new GameCard(CardType.EIGHT, CardColor.BLUE)));
	}

	@Test
	public void test_Game_Random() {
		_gameLogic.playerJoined("Maor");
		_gameLogic.playerJoined("Omri");
		_gameLogic.playerJoined("Itai");

		String strCurrentPlayer = "";
		boolean bIsValid = false;

		ArrayList<GameCard> arrPlayerCards;
		do {
			strCurrentPlayer = _gameLogic.getGameState().getCurrPlayer();
			arrPlayerCards = _gameLogic.getGameState().getPlayers().get(strCurrentPlayer);

			for (int i = 0; i < arrPlayerCards.size() && !bIsValid; i++) {
				bIsValid = _gameLogic.playerChoseCard(strCurrentPlayer, arrPlayerCards.get(i));
			}
			if (!bIsValid) {
				System.out.println(strCurrentPlayer + " doesn't have good cards left");
				_gameLogic.takeNewCardFromDeck(strCurrentPlayer);
				_gameLogic.switchTurn();
			}
			bIsValid = false;
		} while (arrPlayerCards != null && arrPlayerCards.size() > 0);
		
		if (arrPlayerCards.size() == 0) {
			System.out.println(strCurrentPlayer + " has won !!");
		}
			
	}
}

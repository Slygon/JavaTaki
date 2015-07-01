package test;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import taki.common.GameCard;
import taki.server.GameLogic;

public class GameCardTest {

	@Test
	public void testCreateDeck() {
		GameLogic logic = new GameLogic();
		printDeck(logic);
	}
	
	@Test
	public void testHandCards() {
		GameLogic logic = new GameLogic();
		String[] players = new String[] {"Tal", "Moshe", "Itzik", "Shalom", "David", "Gal", "Chen",
										 "Nir", "Assaf", "Matan", "Lior", "Maor", "Omri", "Itai"};
		logic.setPlayers(players);
		logic.startGame();
		
		for(String player : players) {
			System.out.println(player);
			for(GameCard card : logic.getPlayers().get(player)) {
				System.out.println("\t" + card);
			}
		}	

		printDeck(logic);
	}
	
	private void printDeck(GameLogic logic) {
		ArrayList<GameCard> deck = logic.getDeck();
		
		for(GameCard card : deck) {
			System.out.println(card);
		}
	}

}

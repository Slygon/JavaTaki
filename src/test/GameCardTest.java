package test;

import java.util.ArrayList;

import org.junit.Test;

import taki.common.GameCard;
import taki.server.GameLogic;

public class GameCardTest {

	@Test
	public void test() {
		GameLogic logic = new GameLogic();
		ArrayList<GameCard> deck = logic.getDeck();
		
		for(GameCard card : deck) {
			System.out.println(card);
		}
	}

}

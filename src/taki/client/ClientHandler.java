package taki.client;

import taki.common.GameMessage;

public interface ClientHandler {
	void append(String strText);
	void onConnected(String strText);
	void onConnectionFailed(String strMsg);
	void onGameStateRecieved(GameMessage gameMsg);
}

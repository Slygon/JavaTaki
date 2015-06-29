package taki.client;

import java.util.ArrayList;

public interface ClientHandler {
	void append(String strText);
	void onConnected(String strText);
	void onConnectionFailed(String strMsg);
	void onUserListRecieved(ArrayList<String> alUsers);
}

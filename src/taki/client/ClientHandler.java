package taki.client;

public interface ClientHandler {
	void append(String strText);
	void onConnected();
	void onConnectionFailed(String strMsg);
}

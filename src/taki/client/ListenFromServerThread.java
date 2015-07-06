package taki.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import taki.common.ChatMessage;
import taki.common.GameMessage;
import taki.common.ChatMessage.MsgType;
import taki.common.SystemMessage;
import taki.common.UserList;

/*
 * a class that waits for the message from the server and append them to the JTextArea
 * if we have a GUI or simply System.out.println() it in console mode
 */
public class ListenFromServerThread extends Thread {

	ObjectInputStream _sInput;
	ClientHandler _cg;
	
	public ListenFromServerThread(ClientHandler cg, ObjectInputStream sInput) {
		_sInput = sInput;
		_cg = cg;
	}

	public void run() {
		while (true) {
			try {
				Object msg = _sInput.readObject();

				if (msg instanceof GameMessage) {
					handleGameMsg(((GameMessage) msg));
				} else if (msg instanceof ChatMessage) {
					ChatMessage chatMSG = (ChatMessage) msg;
					handleChatMessage((ChatMessage)chatMSG);
					
				} else if (msg instanceof SystemMessage) {
					if (!handleSystemMsg((SystemMessage)msg)) {
						break;
					}
				} else if (msg instanceof String) {
					handleSimpleMsg(msg.toString());
				}
			} catch (IOException e) {
				if (_cg != null)
					_cg.onConnectionFailed("Connection lost !");
				break;
			}
			// can't happen with a String object but need the catch anyhow
			catch (ClassNotFoundException e2) {
			}
		}
	}
	
	private void handleSimpleMsg(String str) {
		printMsg("[Server] " + str);
	}

	private void handleChatMessage(ChatMessage chatMsg) {
		printMsg("[Chat] " + chatMsg.getMessage());
	}
	
	private void printMsg(String str) {
		// if console mode print the message and add back the prompt
		if (_cg == null) {
			System.out.println(str);
			System.out.print("> ");
		} else {
			_cg.append(str);
		}	
	}

	private boolean handleSystemMsg(SystemMessage sysMsg) {

		if (sysMsg.getMsgType() == SystemMessage.MsgType.USERNAME_TAKEN) {
			_cg.onConnectionFailed("Username already taken.");
			return false;
		}
		return true;
	}

	private void handleGameMsg(GameMessage gameMsg) {
		_cg.onGameStateRecieved(gameMsg);
	}
}
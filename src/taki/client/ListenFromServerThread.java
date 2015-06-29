package taki.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import taki.common.ChatMessage;
import taki.common.ChatMessage.MsgType;
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

				if (msg instanceof UserList) {
					ArrayList<String> alUsers = ((UserList) msg).getUsers();
					_cg.onUserListRecieved(alUsers);
				} else if (msg instanceof ChatMessage) {
					ChatMessage chatMSG = (ChatMessage) msg;
					
					if (chatMSG.getMsgType() == MsgType.USERNAME_TAKEN) {
						_cg.onConnectionFailed("Username already taken.");
						break;
					}
					
				} else if (msg instanceof String) {
					// if console mode print the message and add back the prompt
					if (_cg == null) {
						System.out.println(msg);
						System.out.print("> ");
					} else {
						_cg.append(msg.toString());
					}
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
}
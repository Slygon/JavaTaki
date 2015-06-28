package taki.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Observer;

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
		while(true) {
			try {
				String msg = (String) _sInput.readObject();
				// if console mode print the message and add back the prompt
				if(_cg == null) {
					System.out.println(msg);
					System.out.print("> ");
				}
				else {
					_cg.append(msg);
				}
			}
			catch(IOException e) {
				if(_cg != null) 
					_cg.onConnectionFailed("Connection lost !");
				break;
			}
			// can't happen with a String object but need the catch anyhow
			catch(ClassNotFoundException e2) {
			}
		}
	}
}
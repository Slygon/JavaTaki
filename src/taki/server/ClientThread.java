package taki.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import taki.common.ChatMessage;

/** One instance of this thread will run for each client */
public class ClientThread extends Thread {
	// the socket where to listen/talk
	Socket _socket;
	ObjectInputStream _sInput;
	ObjectOutputStream _sOutput;
	// my unique id (easier for deconnection)
	int _nId;
	// the Username of the Client
	String _username;
	// the only type of message a will receive
	ChatMessage _cm;
	// the date I connect
	String _date;
	Server _server;

	// Constructor
	ClientThread(Socket socket, Server server) {
		// a unique id
		this._socket = socket;
		_server = server;
		_nId = server.getUniqueIdPlusOne();
		
		/* Creating both Data Stream */
		System.out.println("Thread trying to create Object Input/Output Streams");
		try
		{
			// create output first
			_sOutput = new ObjectOutputStream(socket.getOutputStream());
			_sInput  = new ObjectInputStream(socket.getInputStream());
			// read the username
			_username = (String) _sInput.readObject();
			_server.display(_username + " just connected.");
		}
		catch (IOException e) {
			_server.display("Exception creating new Input/output Streams: " + e);
			return;
		}
		// have to catch ClassNotFoundException
		// but I read a String, I am sure it will work
		catch (ClassNotFoundException e) {
		}
        _date = new Date().toString() + "\n";
	}

	// what will run forever
	public void run() {
		// to loop until LOGOUT
		boolean keepGoing = true;
		while(keepGoing) {
			// read a String (which is an object)
			try {
				_cm = (ChatMessage) _sInput.readObject();
			}
			catch (IOException e) {
				_server.display(_username + " Exception reading Streams: " + e);
				break;				
			}
			catch(ClassNotFoundException e2) {
				break;
			}
			// the messaage part of the ChatMessage
			String message = _cm.getMessage();

			// Switch on the type of message receive
			switch(_cm.getType()) {

			case MESSAGE:
				_server.broadcast(_username + ": " + message);
				break;
			case LOGOUT:
				_server.display(_username + " disconnected with a LOGOUT message.");
				keepGoing = false;
				break;
			case WHOISIN:
				writeMsg("List of the users connected at " + _server.getDateFormat().format(new Date()) + "\n");
				// scan al the users connected
				for(int i = 0; i < _server.getClients().size(); ++i) {
					ClientThread ct = _server.getClients().get(i);
					writeMsg((i+1) + ") " + ct._username + " since " + ct._date);
				}
				break;
			}
		}
		// remove myself from the arrayList containing the list of the
		// connected Clients
		_server.remove(_nId);
		close();
	}
	
	// try to close everything
	private void close() {
		// try to close the connection
		try {
			if(_sOutput != null) _sOutput.close();
		}
		catch(Exception e) {}
		try {
			if(_sInput != null) _sInput.close();
		}
		catch(Exception e) {};
		try {
			if(_socket != null) _socket.close();
		}
		catch (Exception e) {}
	}

	/*
	 * Write a String to the Client output stream
	 */
	public boolean writeMsg(String msg) {
		// if Client is still connected send the message to it
		if(!_socket.isConnected()) {
			close();
			return false;
		}
		// write the message to the stream
		try {
			_sOutput.writeObject(msg);
		}
		// if an error occurs, do not abort just inform the user
		catch(IOException e) {
			_server.display("Error sending message to " + _username);
			_server.display(e.toString());
		}
		return true;
	}
}
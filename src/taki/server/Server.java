package taki.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import taki.common.SystemMessage;
import taki.common.UserList;

/*
 * The server that can be run both as a console application or a GUI
 */
public class Server {
	// a unique ID for each connection
	private static int _uniqueId;
	// an ArrayList to keep the list of the Client
	private ArrayList<ClientThread> _al;
	// if I am in a GUI
	private ServerGUI _sg;
	// to display time
	private SimpleDateFormat _sdf;
	// the port number to listen for connection
	private int _port;
	// the boolean that will be turned of to stop the server
	private boolean _keepGoing;
	
	public static int getUniqueIdPlusOne() {
		return ++_uniqueId;
	}

	public static void setUniqueId(int uniqueId) {
		Server._uniqueId = uniqueId;
	}
	public SimpleDateFormat getDateFormat() {
		return _sdf;
	}

	public ArrayList<ClientThread> getClients() {
		return _al;
	}
	

	/*
	 *  server constructor that receive the port to listen to for connection as parameter
	 *  in console
	 */
	public Server(int port) {
		this(port, null);
	}
	
	public Server(int port, ServerGUI sg) {
		// GUI or not
		this._sg = sg;
		// the port
		this._port = port;
		// to display hh:mm:ss
		_sdf = new SimpleDateFormat("HH:mm:ss");
		// ArrayList for the Client list
		_al = new ArrayList<ClientThread>();
	}
	
	public void start() {
		_keepGoing = true;
		/* create socket server and wait for connection requests */
		try 
		{
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(_port);

			// infinite loop to wait for connections
			while(_keepGoing) 
			{
				// format message saying we are waiting
				display("Server waiting for Clients on port " + _port + ".");
				
				Socket socket = serverSocket.accept();  	// accept connection
				// if I was asked to stop
				if(!_keepGoing)
					break;
				ClientThread t = new ClientThread(socket, this);  // make a thread of it
				
				if (t.getIsValid()) {
					t.start();
					onClientConnected(t);
				} else {
					t.writeMsg(new SystemMessage(SystemMessage.MsgType.USERNAME_TAKEN, "Username already taken"));
					t.close();
				}
			}
			// I was asked to stop
			try {
				serverSocket.close();
				for(int i = 0; i < _al.size(); ++i) {
					ClientThread tc = _al.get(i);
					try {
					tc._sInput.close();
					tc._sOutput.close();
					tc._socket.close();
					}
					catch(IOException ioE) {
						// not much I can do
					}
				}
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// something went bad
		catch (IOException e) {
            String msg = _sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}		
    /*
     * For the GUI to stop the server
     */
	public void stop() {
		_keepGoing = false;
		// connect to myself as Client to exit statement 
		// Socket socket = serverSocket.accept();
		try {
			new Socket("localhost", _port);
		}
		catch(Exception e) {
			// nothing I can really do
		}
	}
	
	public boolean checkIfNameExists(String strName) {
		for(ClientThread ct : _al) {
			if (ct.getUsername().equals(strName)) {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * Display an event (not a message) to the console or the GUI
	 */
	public void display(String msg) {
		String time = _sdf.format(new Date()) + " " + msg;
		if(_sg == null)
			System.out.println(time);
		else
			_sg.appendEvent(time + "\n");
	}
	
	public void sendTextToAll(String message) {
		// add HH:mm:ss and \n to the message
				String time = _sdf.format(new Date());
				String messageLf = time + " " + message + "\n";
				// display message on console or GUI
				if(_sg == null)
					System.out.print(messageLf);
				else
					_sg.appendRoom(messageLf);     // append in the room window
				
			broadcast(message);
	}
	
	/*
	 *  to broadcast a message to all Clients
	 */
	public synchronized void broadcast(Object msg) {
		// we loop in reverse order in case we would have to remove a Client
		// because it has disconnected
		for(int i = _al.size(); --i >= 0;) {
			ClientThread ct = _al.get(i);
			// try to write to the Client if it fails remove it from the list
			if(!ct.writeMsg(msg)) {
				onClientDisconnected(ct);
			}
		}
	}

	// for a client who logoff using the LOGOUT message
	public synchronized void remove(int id) {
		// scan the array list until we found the Id
		for(int i = 0; i < _al.size(); ++i) {
			ClientThread ct = _al.get(i);
			// found it
			if(ct._nId == id) {
				_al.remove(i);
				return;
			}
		}
	}

	public void onClientDisconnected(ClientThread ct) {
		remove(ct.get_Id());
		onClientshanged(ct);
		display("Disconnected Client " + ct.getUsername() + " removed from list.");
	}
	
	public void onClientConnected(ClientThread ct) { 
		_al.add(ct);						// save it in the ArrayList
		onClientshanged(ct);
		display(ct.getName() + " just connected.");
		
	}
	
	public void onClientshanged(ClientThread ct) {
		ArrayList<String> userList = new ArrayList<String>();
		for (ClientThread clients : _al)
			userList.add(clients.getUsername());
		broadcast(new UserList(userList));
	}

}
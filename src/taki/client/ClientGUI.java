package taki.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.eclipse.swt.widgets.Display;

import taki.common.ChatMessage;
//import taki.common.ChatMessage.CardType;
import taki.common.ChatMessage.MsgType;

/*
 * The Client with its GUI
 */
public class ClientGUI extends JFrame implements ActionListener, ClientHandler {

	private static final long serialVersionUID = 1L;
	// will first hold "Username:", later on "Enter message"
	private JLabel label;
	// to hold the Username and later on the messages
	private JTextField tf;
	// to hold the server address an the port number
	private JTextField tfServer, tfPort;
	// to Logout and get the list of the users
	private JButton login, logout, whoIsIn, chooseCard;
	// for the chat room
	private JTextArea ta;
	// if it is for connection
	private boolean connected;
	// the Client object
	private Client client;
	// the default port number
	private int defaultPort;
	private String defaultHost;
//	private JComboBox<CardType> comboBox;

	// Constructor connection receiving a socket number
	public ClientGUI(String host, int port) {

		super("Chat Client");
		defaultPort = port;
		defaultHost = host;

		// the two JTextField with default value for server address and port
		// number
		tfServer = new JTextField(host);
		tfPort = new JTextField("" + port);
		tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

		// the server name and the port number
		JPanel serverAndPort = new JPanel(new GridLayout(1, 5, 1, 3));
		serverAndPort.add(new JLabel("Server Address:  "));
		serverAndPort.add(tfServer);
		serverAndPort.add(new JLabel("Port Number:  "));
		serverAndPort.add(tfPort);
		serverAndPort.add(new JLabel(""));

		// the Label and the TextField
		label = new JLabel("Enter your username below", SwingConstants.CENTER);
		tf = new JTextField("Anonymous");
		tf.setBackground(Color.WHITE);

		// The NorthPanel with:
		JPanel northPanel = new JPanel(new GridLayout(3, 1));
		// adds the Server an port field to the GUI
		northPanel.add(serverAndPort);
		northPanel.add(label);
		northPanel.add(tf);
		add(northPanel, BorderLayout.NORTH);

		// The CenterPanel which is the chat room
		ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
		ta.setEditable(false);

		// the 3 buttons
		login = new JButton("Login");
		logout = new JButton("Logout");
		whoIsIn = new JButton("Who is in");
		chooseCard = new JButton("Choose Card");

		login.addActionListener(this);
		logout.addActionListener(this);
		whoIsIn.addActionListener(this);
		chooseCard.addActionListener(this);

		logout.setEnabled(false); // you have to login before being able to
									// logout
		whoIsIn.setEnabled(false); // you have to login before being able to Who
									// is in
		chooseCard.setEnabled(false);

//		comboBox = new JComboBox<CardType>(CardType.values());

		JPanel centerPanel = new JPanel(new GridLayout(1, 2));
		centerPanel.add(new JScrollPane(ta));
		add(centerPanel, BorderLayout.CENTER);

		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.add(new Canvas());
//		rightPanel.add(comboBox);
		centerPanel.add(rightPanel);

		JPanel southPanel = new JPanel();
		southPanel.add(login);
		southPanel.add(logout);
		southPanel.add(whoIsIn);
		add(southPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		tf.requestFocus();

	}

	// called by the Client to append text in the TextArea
	public void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}

	// called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	public void onConnectionFailed(String strMsg) {
		updateControls(false);
	}

	/*
	 * Button or JTextField clicked
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		// if it is the Logout button
		if (o == logout) {
			client.sendMessage(new ChatMessage(MsgType.LOGOUT, ""));
			return;
		}

		// if it the who is in button
		if (o == whoIsIn) {
			client.sendMessage(new ChatMessage(MsgType.WHOISIN, ""));
			return;
		}
//		if (o == chooseCard) {
//			ChatMessage chatMessage = new ChatMessage(MsgType.CARD_CHOSEN, "");
//			chatMessage.setCardType(CardType.ACE);
//			client.sendMessage(chatMessage);
//			return;
//		}

		// ok it is coming from the JTextField
		if (connected) {

			// just have to send the message
			client.sendMessage(new ChatMessage(MsgType.MESSAGE, tf.getText()));
			tf.setText("");
			return;
		}

		if (o == login) {
			// ok it is a connection request
			String username = tf.getText().trim();

			// empty username ignore it
			if (username.length() == 0)
				return;

			// empty serverAddress ignore it
			String server = tfServer.getText().trim();
			if (server.length() == 0)
				return;

			// empty or invalid port numer, ignore it
			String portNumber = tfPort.getText().trim();
			if (portNumber.length() == 0)
				return;
			int port = 0;
			try {
				port = Integer.parseInt(portNumber);
			} catch (Exception en) {
				return; // nothing I can do if port number is not valid
			}

			// try creating a new Client with GUI
			client = new Client(server, port, username, this);
			// test if we can start the Client
			if (!client.start())
				return;

			updateControls(true);
		}

	}

	private void updateControls(boolean isConnected) {
		connected = isConnected;
		// disable login button
		login.setEnabled(!isConnected);
		// enable the 2 buttons
		logout.setEnabled(isConnected);
		whoIsIn.setEnabled(isConnected);
		// disable the Server and Port JTextField
		tfServer.setEditable(!isConnected);
		tfPort.setEditable(!isConnected);
		// Action listener for when the user enter a message
		chooseCard.setEnabled(isConnected);
		if (isConnected) {
			tf.addActionListener(this);

			tf.setText("");
			label.setText("Enter your message below");
		} else {
			tf.removeActionListener(this);

			label.setText("Enter your username below");

			tf.setText("Anonymous");
			// reset port number and host name as a construction time
			tfPort.setText("" + defaultPort);
			tfServer.setText(defaultHost);
			// let the user change them
			// don't react to a <CR> after the username
		}
	}

	public void onConnected(String strText) {
	}
	
	
	public void onUserListRecieved(ArrayList<String> alUsers) {
		
	}
}
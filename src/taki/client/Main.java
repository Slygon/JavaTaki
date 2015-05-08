package taki.client;

import java.util.Arrays;
import java.util.Scanner;

import taki.common.ChatMessage;
import taki.common.ChatMessage.MsgType;

public class Main {
	/*
	 * To start the Client in console mode use one of the following command >
	 * java Client > java Client username > java Client username portNumber >
	 * java Client username portNumber serverAddress at the console prompt If
	 * the portNumber is not specified 1500 is used If the serverAddress is not
	 * specified "localHost" is used If the username is not specified
	 * "Anonymous" is used > java Client is equivalent to > java Client
	 * Anonymous 1500 localhost are eqquivalent
	 * 
	 * In console mode, if an error occurs the program simply stops when a GUI
	 * id used, the GUI is informed of the disconnection
	 */
	public static void main(String[] args) {

		if (Arrays.asList(args).contains("ui")) {
			new ClientGUI("localhost", 1500);
		} else {

			// default values
			int portNumber = 1500;
			String serverAddress = "localhost";
			String userName = "Anonymous";

			// depending of the number of arguments provided we fall through
			switch (args.length) {
			// > javac Client username portNumber serverAddr
			case 3:
				serverAddress = args[2];
				// > javac Client username portNumber
			case 2:
				try {
					portNumber = Integer.parseInt(args[1]);
				} catch (Exception e) {
					System.out.println("Invalid port number.");
					System.out
							.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
					return;
				}
				// > javac Client username
			case 1:
				userName = args[0];
				// > java Client
			case 0:
				break;
			// invalid number of arguments
			default:
				System.out
						.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
				return;
			}
			// create the Client object
			Client client = new Client(serverAddress, portNumber, userName);
			// test if we can start the connection to the Server
			// if it failed nothing we can do
			if (!client.start())
				return;

			// wait for messages from user
			Scanner scan = new Scanner(System.in);
			// loop forever for message from the user
			while (true) {
				System.out.print("> ");
				// read message from user
				String msg = scan.nextLine();
				// logout if message is LOGOUT
				if (msg.equalsIgnoreCase("LOGOUT")) {
					client.sendMessage(new ChatMessage(MsgType.LOGOUT, ""));
					// break to do the disconnect
					break;
				}
				// message WhoIsIn
				else if (msg.equalsIgnoreCase("WHOISIN")) {
					client.sendMessage(new ChatMessage(MsgType.WHOISIN, ""));
				} else { // default to ordinary message
					client.sendMessage(new ChatMessage(MsgType.MESSAGE, msg));
				}
			}
			// done disconnect
			client.disconnect();
		}
	}
}

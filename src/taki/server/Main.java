package taki.server;

import java.util.Arrays;

public class Main {

	/*
	 * To run as a console application just open a console window and: > java
	 * Server > java Server portNumber If the port number is not specified 1500
	 * is used
	 */
	public static void main(String[] args) {
		if (Arrays.asList(args).contains("ui")) {
			// start server default port 1500
			new ServerGUI(1500);

		} else {
			// start server on port 1500 unless a PortNumber is specified
			int portNumber = 1500;
			switch (args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				} catch (Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Server [portNumber]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Server [portNumber]");
				return;

			}
			// create a server object and start it
			Server server = new Server(portNumber);
			server.start();
		}
	}
}

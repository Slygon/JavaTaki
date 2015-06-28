package taki.boot;

import java.awt.EventQueue;
import java.util.Arrays;

import taki.client.ClientRunner;
import taki.client.newClientGUI;
import taki.server.ServerRunner;

public class Run {

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (Arrays.asList(args).contains("client")) {
					ClientRunner.startClient(args);
				} else if (Arrays.asList(args).contains("server")) {
					ServerRunner.startServer(args);
				} else {
					MainGUI gui = new MainGUI();
					gui.setVisible(true);
				}
			}
		});

	}

}

package taki.boot;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import taki.client.ClientRunner;
import taki.server.ServerRunner;

@SuppressWarnings("serial")
public class MainGUI extends JFrame {

	private final int SCREEN_WIDTH = 400;
	private final int SCREEN_HEIGHT = 300;
	private JButton btnClient;
	private JButton btnServer;
	private boolean isClientRunning = false;

	public boolean isClientRunning() {
		return isClientRunning;
	}

	public MainGUI() {
		initGUI();
	}

	private void initGUI() {
		setTitle("Taki Game");
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new GridLayout(1, 2, 30, 30));

		btnClient = new JButton("Client");
		btnServer = new JButton("Server");

		String[] args = new String[] { "ui" };
		JFrame frame = this;
		btnClient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				isClientRunning = true;
				ClientRunner.startClient(args);
			}
		});

		btnServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				frame.hide();
				ServerRunner.startServer(args);
			}
		});

		getContentPane().add(btnClient);
		getContentPane().add(btnServer);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				if (!isClientRunning()) {
					System.exit(0);
				}
			}
		});
	}
}

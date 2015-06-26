package taki.run;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainGUI extends JFrame {
	
	private final int SCREEN_WIDTH = 400;
	private final int SCREEN_HEIGHT = 300;
	
	public MainGUI() {
		initGUI();
	}

	private void initGUI() {
		setTitle("Taki Game");
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setLocationRelativeTo(null);
		
		JButton quitButton = new JButton("Quit");
		
		quitButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
		});
		
		createLayout(quitButton);
	}
	
	private void createLayout(JComponent arg) {

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
        		.addComponent(arg)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(arg)
        );
    }
}

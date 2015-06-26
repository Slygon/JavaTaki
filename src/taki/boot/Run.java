package taki.boot;

import java.awt.EventQueue;

import taki.run.MainGUI;

public class Run {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
		    @Override
		    public void run() {
		    	MainGUI gui = new MainGUI();
		        gui.setVisible(true);
		    }
		});
	}

}

package main;

import javax.swing.*;

import graphics.Gui;

public class Application {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Gui();
			}
		});
		
	}

}

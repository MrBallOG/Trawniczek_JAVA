package trawniczek;

import java.awt.*;
import javax.swing.*;

public class Animation extends JPanel {

	private JLabel set_speed;
	private JButton speed_x2;
	private JButton speed_x05;
	
	public Animation() {
		
		setLayout(null);
		setBackground(new Color(255, 145, 164));
		set_speed = new JLabel("Prêdkoœæ odtwarzania:");
		speed_x2 = new JButton("x2");
		speed_x05 = new JButton("x0.5");
	
		set_speed.setBounds(20, 160, 150, 30);
		speed_x2.setBounds(40, 200, 70, 30);
		speed_x05.setBounds(40, 270, 70, 30);
		
		add(set_speed);
		add(speed_x2);
		add(speed_x05);
	}
	
	@Override
	public void paintComponent(Graphics g) {    //chyba bez canvas przys³ania guziki
		super.paintComponent(g);
		g.setColor(Color.white);
		g.fillRect(300, 200, 100, 100);
		g.dispose();
	}

}

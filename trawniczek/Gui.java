package trawniczek;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Gui {

	private JFrame frame;
	private JPanel container;
	private JPanel first;                       // first screen
	private JButton next_screen;
	private JRadioButton yes;                   // sets rebounds true
	private JRadioButton no;					
	private ButtonGroup group;
	private JLabel ask_for_file_path;
	private JLabel ask_for_rebounds;
	private JLabel ask_for_num_of_iterations;
	private JLabel ask_for_period;
	private JTextField get_file_path;
	private JTextField get_num_of_iterations;
	private JTextField get_period;
	private Color background_color;
	private Input in;                             // reads input file
	private Animation anime;					  // creates animation
	private int num;							  // number of iterations
	private int period;                           // period of iteration
	private int error = 1;						  // used to determine whether switch to next screen
	private boolean set_rebounds = true;          // used to determine whether calculate rebounds of water
	
	public Gui() {
		frame = new JFrame("trawniczek");
		container = new JPanel();
		CardLayout cl = new CardLayout();
		container.setLayout(cl);
		first = new JPanel();
		background_color = new Color(255, 145, 164);
		first.setBackground(background_color);
		
		next_screen = new JButton("dalej");
		yes = new JRadioButton("tak", true);
		no = new JRadioButton("nie", false);
		ask_for_file_path = new JLabel("Podaj sciezke dostepu:");
		ask_for_rebounds = new JLabel("Czy odbicia?");
		ask_for_num_of_iterations = new JLabel("Liczba cykli:");
		ask_for_period = new JLabel("Okres cyklu(0.1s):");
		get_file_path = new JTextField(30);
		get_num_of_iterations = new JTextField(10);
		get_period = new JTextField(10);
		
		first.setLayout(null);
		ask_for_file_path.setBounds(500, 30, 150, 30);
		get_file_path.setBounds(500, 60, 350, 30);
		ask_for_rebounds.setBounds(500, 90, 150, 30);
		yes.setBounds(495, 120, 45, 30);
		yes.setBackground(background_color);
		no.setBounds(545, 120, 45, 30);
		no.setBackground(background_color);
		ask_for_num_of_iterations.setBounds(500, 150, 180, 30);
		get_num_of_iterations.setBounds(500, 180, 40, 30);
		ask_for_period.setBounds(500, 210, 180, 30);
		get_period.setBounds(500, 240, 40, 30);
		next_screen.setBounds(565, 520, 70, 30);
		
		first.add(ask_for_file_path);
		first.add(get_file_path);
		first.add(ask_for_rebounds);
		first.add(yes);
		first.add(no);
		first.add(ask_for_num_of_iterations);
		first.add(get_num_of_iterations);
		first.add(ask_for_period);
		first.add(get_period);
		first.add(next_screen);
		
		group = new ButtonGroup();
		group.add(yes);
		group.add(no);
		
		container.add(first, "1");	
		cl.show(container, "1");
		
		yes.addItemListener(new RadioButtonHandler());
		no.addItemListener(new RadioButtonHandler());
		next_screen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(get_file_path.getText().trim().isEmpty() || get_num_of_iterations.getText().trim().isEmpty() || get_period.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Puste pola", "Error", JOptionPane.PLAIN_MESSAGE);
					return;
				}
				else
					try {
						num = Integer.parseInt(get_num_of_iterations.getText().trim());
						if(num <= 0) {
							JOptionPane.showMessageDialog(null, "Liczba cykli musi byc > 0", "Error", JOptionPane.PLAIN_MESSAGE);
							return;
						}
						period = Integer.parseInt(get_period.getText().trim());
						if(period <= 0) {
							JOptionPane.showMessageDialog(null, "Okres musi byc > 0", "Error", JOptionPane.PLAIN_MESSAGE);
							return;
						}
					}catch(NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "To nie liczba naturalna", "Error", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				in = new Input(get_file_path.getText().trim());
				error = in.readFromFile();
				if(error == 0) {
					anime = new Animation(in.getLawn(), num, period, set_rebounds);
					container.add(anime, "2");      
					cl.show(container, "2");
				}
			}
		});
		
		frame.add(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	private class RadioButtonHandler implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent event) {
			if(event.getSource() == no)
				set_rebounds = false;
			else
				set_rebounds = true;
		}
		
	}

}
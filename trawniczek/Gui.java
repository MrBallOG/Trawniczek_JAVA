package trawniczek;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Gui {

	private JFrame frame;
	private JPanel container;
	private JPanel first;
	private JButton next_screen;
	private JRadioButton yes;
	private JRadioButton no;
	private ButtonGroup group;
	private JLabel file_path;
	private JLabel rebound;
	private JLabel num_of_iterations;
	private JTextField get_file_path;
	private JTextField get_num_of_iterations;
	private Color background_color;
	private Input in;
	private Animation anime;
	private int time;
	private int error = 1;
	private boolean set_rebounds = true;
	
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
		file_path = new JLabel("Podaj œcie¿kê dostêpu:");
		rebound = new JLabel("Czy odbicia?");
		num_of_iterations = new JLabel("Ile ma trwaæ podlewanie(min)?");
		get_file_path = new JTextField(30);
		get_num_of_iterations = new JTextField(10);
		
		first.setLayout(null);
		file_path.setBounds(510, 30, 150, 30);
		get_file_path.setBounds(510, 60, 150, 30);
		rebound.setBounds(510, 90, 150, 30);
		yes.setBounds(505, 120, 45, 30);
		yes.setBackground(background_color);
		no.setBounds(555, 120, 45, 30);
		no.setBackground(background_color);
		num_of_iterations.setBounds(510, 150, 180, 30);
		get_num_of_iterations.setBounds(510, 180, 40, 30);
		next_screen.setBounds(565, 520, 70, 30);
		
		first.add(file_path);
		first.add(get_file_path);
		first.add(rebound);
		first.add(yes);
		first.add(no);
		first.add(num_of_iterations);
		first.add(get_num_of_iterations);
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
				if(get_file_path.getText().trim().isEmpty() || get_num_of_iterations.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Puste pola", "Error", JOptionPane.PLAIN_MESSAGE);
					return;
				}
				else
					try {
						time = Integer.parseInt(get_num_of_iterations.getText().trim());
						if(time == 0) {
							JOptionPane.showMessageDialog(null, "Czas nie moze byc zerem", "Error", JOptionPane.PLAIN_MESSAGE);
							return;
						}
					}catch(NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "To nie liczba", "Error", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				in = new Input(get_file_path.getText().trim());
				error = in.readFromFile();
				if(error == 0) {
					anime = new Animation();
					container.add(anime, "2");      
					cl.show(container, "2");
					System.out.println(time + "  " + set_rebounds);
				}
			}
		});
		
		frame.add(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
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

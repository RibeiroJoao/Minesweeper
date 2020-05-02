/*
 * This is an opensource code. No rights.
 */
package minesweeper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Joao Ribeiro
 */
public class Minesweeper {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel buttonPanel = new JPanel();
		JPanel panel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());

		JLabel label1 = new JLabel("Choose size: ");
		panel.add(label1, BorderLayout.NORTH);

		JButton button1 = new JButton("Small");

		panel.add(button1, BorderLayout.WEST);

		panel.add(buttonPanel);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.setLocationRelativeTo(frame);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);

		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Board(10, 10);
				frame.setVisible(false);
			}
		});
	}
}
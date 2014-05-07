
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * // TODO Write specifications for your JottoGUI class.
 */
public class JottoGUI extends JFrame {

	private JButton newPuzzleButton;
	private JTextField newPuzzleNumber;
	private JLabel puzzleNumber, guessLabel;
	private JTextField guess;
	private JTable guessTable;
	private GridLayout content, gridTop, gridMid;
	public JottoGUI() {
		newPuzzleButton = new JButton();
		newPuzzleButton.setName("newPuzzleButton");
		newPuzzleNumber = new JTextField();
		newPuzzleNumber.setName("newPuzzleNumber");
		puzzleNumber = new JLabel();
		puzzleNumber.setName("puzzleNumber");
		guess = new JTextField();
		guess.setName("guess");
		guessTable = new JTable();
		guessTable.setName("guessTable");
		guessLabel = new JLabel("Type your guess here: ");
		newPuzzleButton.setText("New Puzzle");
		puzzleNumber.setText("Puzzle #");
		
		
		
		guessTable = new JTable(new String[][]{{"Guess","Count","In Order"},{"crazy", "3", "2"}}, new String[]{"Guess","Count","In Order"});
		guessTable.invalidate();
		
		
		
		
		
		content = new GridLayout(3,1);
		gridTop = new GridLayout(1,3);
		gridMid = new GridLayout(1,2);	
		JPanel mainContent = new JPanel();
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(gridTop);
		topPanel.add(puzzleNumber);
		topPanel.add(newPuzzleButton);
		topPanel.add(newPuzzleNumber);
		
		mainContent.setLayout(content);
		mainContent.add(topPanel);
		
		JPanel midPanel = new JPanel();
		midPanel.setLayout(gridMid);
		midPanel.add(guessLabel);
		midPanel.add(guess);
		mainContent.add(midPanel);
		
		
		mainContent.add(guessTable);
		this.setLocationRelativeTo(null);
		
		this.add(mainContent);
		this.setResizable(false);
		this.pack();
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				JottoGUI main = new JottoGUI();
				try {
					JottoClient client = new JottoClient("127.0.0.1", 32500, main);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				main.setVisible(true);
			}
		});
	}
}

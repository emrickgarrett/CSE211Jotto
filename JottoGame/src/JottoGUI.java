
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
	private JottoClient client;
	
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
		guess.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				sendGuess(guess.getText());
				guess.setText("");
				
			}
			
		});
		
		
		
		newPuzzleButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				newPuzzle();
				
			}
			
		});
		
		
		
		guessTable = new JTable(new DefaultTableModel(new String[]{"Column1", "Column2", "Column3"}, 0));
		this.updateGuesses(new String[]{"Test", "Test2", "Test3"});
		this.updateGuesses(new String[]{"Test4", "Test5", "Test6"});
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
		
		//topPanel.setB
		
		mainContent.add(guessTable);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.add(mainContent);
		this.setResizable(false);
		this.pack();
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				JottoGUI main = new JottoGUI();
				try {
					main.client = new JottoClient("127.0.0.1", 32500, main);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				main.setVisible(true);
			}
		});
	}
	
	public void updateGuesses(String[] input){
		((DefaultTableModel)guessTable.getModel()).addRow(input);
	}
	
	public void resetGuesses(){
		guessTable = new JTable(new String[][]{{}}, new String[]{});
		guessTable.invalidate();
	}
	
	public void wonGame(){
		
	}
	
	private void sendGuess(String s){
		client.sendGuess(s);
	}
	
	private void newPuzzle(){
		client.sendGuess("game restart");
	}
	
       public void receiveReply(String response){
		System.out.print(response);
		if(response.equalsIgnoreCase("Game Restart")){
			this.resetGuesses();
		}
		String [] guesses = response.split(" ");
		
		this.updateGuesses(new String[]{guesses[3],guesses[1],guesses[2]});
		this.invalidate();
	}
}

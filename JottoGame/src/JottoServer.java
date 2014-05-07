import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class JottoServer {

	ServerSocket serverSock;
	private final boolean listening = true;
	public static ArrayList<Socket> clientList;
	protected static ArrayList<String> wordList;
	protected static String guessWord;

	public JottoServer(int port) throws IOException {
		serverSock = new ServerSocket(port);
		clientList = new ArrayList<Socket>();
		wordList = new ArrayList<String>();
		populateWordList();
		selectNewWord();
		listenForClients();
	}

	protected void populateWordList() throws FileNotFoundException {
		FileInputStream in = new FileInputStream(new File("wordlist.txt"));
		Scanner sReader = new Scanner(in);

		while (sReader.hasNext())
			wordList.add(sReader.nextLine());
	}

	protected void selectNewWord() {
		Random r = new Random();
		guessWord = wordList.get(r.nextInt(wordList.size()));
	}

	/**
	 * This method listens for clients trying to connect. Each time a client
	 * connects, a new thread is opened up.
	 * 
	 * @throws IOException
	 */
	protected void listenForClients() throws IOException {
		try {
			while (listening) {
				new JottoServerThread(serverSock.accept()).start();
			}// end while
		} catch (IOException e) {
			System.err
					.println("Error creating new thread for connecting client.");
		}
	}// end listenForClients

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		new JottoServer(32500);
	}

}

class JottoServerThread extends Thread {
	Socket clientConnection;
	DataInputStream dis;
	DataOutputStream dos;
	private final boolean listening = true;

	public JottoServerThread(Socket sock) throws IOException {
		clientConnection = sock;
		JottoServer.clientList.add(clientConnection);
		createStreams();

	}

	protected void createStreams() throws IOException {
		dis = new DataInputStream(clientConnection.getInputStream());
		dos = new DataOutputStream(clientConnection.getOutputStream());
	}

	public void run() {
		try {
			while (listening) {
				String response = scoreWord(dis.readUTF());

				for (int i = 0; i < JottoServer.clientList.size(); i++) {
					Socket tempSoc = JottoServer.clientList.get(i);

					DataOutputStream tempDos = new DataOutputStream(
							tempSoc.getOutputStream());

					tempDos.writeUTF(response);
					tempDos.flush();
				}// end for
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// end run

	private String scoreWord(String receive) throws InterruptedException {
		receive = receive.toLowerCase();
		
		if (receive == null || receive.length() != 5)
			return "word not 5 letters";
		
		if(!JottoServer.wordList.contains(receive))
			return "not a dictionary word";
		
		if(receive.contains("*"))
			JottoServerThread.sleep(5000);
		
		if(receive.equals(JottoServer.guessWord))
			return "correct!";

		// check for all letters, no matter what position
		int inCommon = 0;

		for (int i = 0; i < receive.length(); i++) {
			if (receive.indexOf(JottoServer.guessWord.charAt(i)) != -1)
				inCommon++;
		}

		int exactPosition = 0;
		for (int i = 0; i < receive.length(); i++) {
			if (receive.charAt(i) == JottoServer.guessWord.charAt(i))
				exactPosition++;
		}

		return "response " + inCommon + " " + exactPosition;

	}

}

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class JottoClient {
	Socket tcpSocket;
	String currGuess = "";

	public JottoClient(String serverName, int port, JottoGUI gui)
			throws UnknownHostException, IOException {
		tcpSocket = new Socket(InetAddress.getByName(serverName), port);
		new JottoClientReceiveThread(tcpSocket, gui, this).start();
		new JottoClientSendThread(tcpSocket, gui, this).start();
	}

	/*public static void main(String[] args) throws NumberFormatException,
			UnknownHostException, IOException {
		new JottoClient("127.0.0.1", 32500);
	}// end main*/
	
	public void sendGuess(String s){
		this.currGuess = s;
	}
	
	String getGuess(){
		while(this.currGuess.equals("")){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return this.currGuess;
	}
	
	String readLine(){
		return getGuess();
	}
	
}// end class

class JottoClientSendThread extends Thread {
	Socket sendSocket;
	private final boolean listening = true;
	DataOutputStream dos;
	JottoGUI gui;
	JottoClient client;

	public JottoClientSendThread(Socket sendSocket, JottoGUI gui, JottoClient client) throws IOException {
		this.sendSocket = sendSocket;
		dos = new DataOutputStream(sendSocket.getOutputStream());
		this.gui = gui;
		this.client = client;
	}

	public void run() {
		try {
			while (listening) {
				String input = client.readLine();
				client.currGuess = "";
				
				dos.writeUTF(input);
			}
		} catch (IOException e) {

		}
	}//end run
}//end sendthread

class JottoClientReceiveThread extends Thread {
	Socket receiveSocket;
	private final boolean listening = true;
	DataInputStream dis;
	JottoGUI gui;
	JottoClient client;

	public JottoClientReceiveThread(Socket receiveSocket, JottoGUI gui, JottoClient client) throws IOException {
		this.receiveSocket = receiveSocket;
		dis = new DataInputStream(receiveSocket.getInputStream());
		this.gui = gui;
		this.client = client;
	}

	public void run() {
		try {
			while (listening) {
				String reply;
				reply = dis.readUTF();
				gui.receiveReply(reply);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

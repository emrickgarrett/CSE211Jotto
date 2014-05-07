import java.net.*;
import java.util.Scanner;
import java.io.*;

public class JottoClient {
	Socket tcpSocket;

	public JottoClient(String serverName, int port, JottoGUI gui)
			throws UnknownHostException, IOException {
		tcpSocket = new Socket(InetAddress.getByName(serverName), port);
		new JottoClientReceiveThread(tcpSocket, gui).start();
		new JottoClientSendThread(tcpSocket, gui).start();
	}

	/*public static void main(String[] args) throws NumberFormatException,
			UnknownHostException, IOException {
		new JottoClient("127.0.0.1", 32500);
	}// end main*/
	
	public void sendGuess(String s){
		
	}
	
}// end class

class JottoClientSendThread extends Thread {
	Socket sendSocket;
	private final boolean listening = true;
	DataOutputStream dos;
	Scanner kb;
	JottoGUI gui;

	public JottoClientSendThread(Socket sendSocket, JottoGUI gui) throws IOException {
		this.sendSocket = sendSocket;
		dos = new DataOutputStream(sendSocket.getOutputStream());
		kb = new Scanner(System.in);
		this.gui = gui;
	}

	public void run() {
		try {
			while (listening) {
				String input = kb.nextLine();
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

	public JottoClientReceiveThread(Socket receiveSocket, JottoGUI gui) throws IOException {
		this.receiveSocket = receiveSocket;
		dis = new DataInputStream(receiveSocket.getInputStream());
		this.gui = gui;
	}

	public void run() {
		try {
			while (listening) {
				String reply;
				reply = dis.readUTF();
				System.out.println(reply);
				gui.receiveReply(reply);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

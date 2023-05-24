import Laba_7.kurs_2.group_7.Maxim_Glyzno.MessageListener;

import java.awt.Component;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;

public class InstantMessenger implements MessageListener {
	private String sender;
	private List<MessageListener> listeners = new LinkedList();
	private static final int SERVER_PORT = 568;

	public InstantMessenger() {
		this.startServer();
	}

	public void sendMessage(String senderName, String destinationAddress, String message) throws IOException {
		try {
			Socket socket = new Socket(destinationAddress, 568);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(senderName);
			out.writeUTF(message);
			socket.close();
		} catch (UnknownHostException var6) {
			var6.printStackTrace();
			JOptionPane.showMessageDialog((Component)null, "Не удалось отправить сообщение: узел-адресат не найден", "Ошибка", 0);
		} catch (IOException var7) {
			var7.printStackTrace();
			JOptionPane.showMessageDialog((Component)null, "Не удалось отправить сообщение", "Ошибка", 0);
		}

	}

	public void addMessageListner(MessageListener listener) {
		synchronized(this.listeners) {
			this.listeners.add(listener);
		}
	}

	public void removeMessageListener(MessageListener listener) {
		synchronized(this.listeners) {
			this.listeners.remove(listener);
		}
	}

	private void startServer() {
		(new Thread(new Runnable() {
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(568);

					while(!Thread.interrupted()) {
						Socket socket = serverSocket.accept();
						DataInputStream in = new DataInputStream(socket.getInputStream());
						String senderName = in.readUTF();
						String message = in.readUTF();
						socket.close();
						InstantMessenger.this.notifyListeners(new Peer(senderName, (InetSocketAddress)socket.getRemoteSocketAddress()), message);
					}
				} catch (IOException var6) {
					var6.printStackTrace();
				}

			}
		})).start();
	}

	private void notifyListeners(Peer sender, String message) {
		synchronized(this.listeners) {
			Iterator var5 = this.listeners.iterator();

			while(var5.hasNext()) {
				MessageListener listener = (MessageListener)var5.next();
				listener.messageReceived(sender, message);
			}

		}
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return this.sender;
	}

	public void messageReceived(Peer sender, String message) {
	}
}
package Laba_7.kurs_2.group_7.Maxim_Glyzno;
import java.awt.Color;
import java.awt.Dimension; 
import java.awt.Font;
import java.awt.Toolkit; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.io.IOException; 
import java.net.UnknownHostException; 
import javax.swing.BorderFactory; 
import javax.swing.GroupLayout; 
import javax.swing.JButton; 
import javax.swing.JEditorPane;
import javax.swing.JFrame; 
import javax.swing.JLabel; 
import javax.swing.JOptionPane; 
import javax.swing.JPanel; 
import javax.swing.JScrollPane; 
import javax.swing.JTextArea; 
import javax.swing.JTextField; 
import javax.swing.SwingUtilities; 
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

@SuppressWarnings("serial") 
public class MainFrame extends JFrame implements MessageListener {

	private InstantMessenger instMess;
	private static final String FRAME_TITLE = "Клиент мгновенных сообщений";

	private static final int FRAME_MINIMUM_WIDTH = 500; 
	private static final int FRAME_MINIMUM_HEIGHT = 500; 
	private static final int FROM_FIELD_DEFAULT_COLUMNS = 10; 
	private static final int TO_FIELD_DEFAULT_COLUMNS = 20; 
	private static final int OUTGOING_AREA_DEFAULT_ROWS = 5; 
	private static final int SMALL_GAP = 5; 
	private static final int MEDIUM_GAP = 10; 
	private static final int LARGE_GAP = 15; 
	private final JTextField textFieldFrom; 
	private final JTextField textFieldTo; 
	//	private final JTextArea textAreaIncoming; 
	private final JEditorPane textAreaIncoming; 	
	private final JTextArea textAreaOutgoing;
	private StringBuffer incomingText ; 

	static HTMLDocument doc = null;
	static HTMLEditorKit htmlKit = null;



	public MainFrame( )
	{ 
		super(FRAME_TITLE); 
		setMinimumSize( 
				new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
		final Toolkit kit = Toolkit.getDefaultToolkit(); 
		setLocation((kit.getScreenSize().width - getWidth()) / 2, 
				(kit.getScreenSize().height - getHeight()) / 2); 


		incomingText = new StringBuffer();

		textAreaIncoming = new JEditorPane();
		textAreaIncoming.setContentType("text/html");		
		textAreaIncoming.setEditable(false); 

		final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming); 
		final JLabel labelFrom = new JLabel("Подпись");
		final JLabel labelTo = new JLabel("Получатель");


		textFieldFrom = new JTextField(FROM_FIELD_DEFAULT_COLUMNS); 
		textFieldTo = new JTextField(TO_FIELD_DEFAULT_COLUMNS); 

		textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0); 

		final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOutgoing); 


		final JPanel messagePanel = new JPanel(); 
		messagePanel.setBorder(BorderFactory.createTitledBorder("Сообщение"));

		// ������ �������� ��������� 
		final JButton sendButton = new JButton("Отправить");
		sendButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				sendMessage(); 
			} 
		}); 

		instMess = new InstantMessenger();
		instMess.addMessageListner(this);

		// ���������� ��������� ������ "���������"
		final GroupLayout layout2 = new GroupLayout(messagePanel);
		messagePanel.setLayout(layout2);
		layout2.setHorizontalGroup(layout2.createSequentialGroup()
				.addContainerGap().addGroup(
						layout2.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								layout2.createSequentialGroup()
								.addComponent(labelFrom)
								.addGap(SMALL_GAP)
								.addComponent(textFieldFrom)
								.addGap(LARGE_GAP)
								.addComponent(labelTo).addGap(
										SMALL_GAP)
										.addComponent(textFieldTo))
										.addComponent(scrollPaneOutgoing).addComponent(
												sendButton))
												.addContainerGap());
		layout2.setVerticalGroup(layout2.createSequentialGroup()
				.addContainerGap().addGroup(
						layout2.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelFrom).addComponent(
								textFieldFrom).addComponent(labelTo)
								.addComponent(textFieldTo)).addGap(MEDIUM_GAP)
								.addComponent(scrollPaneOutgoing).addGap(MEDIUM_GAP)
								.addComponent(sendButton)
								.addContainerGap());

		// ���������� ��������� ������
		final GroupLayout layout1 = new GroupLayout(getContentPane());
		setLayout(layout1);
		layout1.setHorizontalGroup(layout1.createSequentialGroup()
				.addContainerGap().addGroup(
						layout1.createParallelGroup().addComponent(
								scrollPaneIncoming).addComponent(messagePanel))
								.addContainerGap());
		layout1.setVerticalGroup(layout1.createSequentialGroup()
				.addContainerGap().addComponent(scrollPaneIncoming).addGap(
						MEDIUM_GAP).addComponent(messagePanel)
						.addContainerGap());
	}

	private void sendMessage() { 
		try { 

			final String senderName = textFieldFrom.getText(); 
			final String destinationAddress = textFieldTo.getText(); 
			final String message = textAreaOutgoing.getText(); 

			if (senderName.isEmpty()) { 
				JOptionPane.showMessageDialog(this, "Введите имя отправителя", "Ошибка",JOptionPane.ERROR_MESSAGE);
				return; 
			} 
			if (destinationAddress.isEmpty()) { 
				JOptionPane.showMessageDialog(this, "Введите адрес узла-получателя", "Ошибка",JOptionPane.ERROR_MESSAGE);
				return; 
			} 
			if (message.isEmpty()) { 
				JOptionPane.showMessageDialog(this, "Введите текст сообщения", "Ошибка",JOptionPane.ERROR_MESSAGE);
				return; 
			} 


			instMess.sendMessage(senderName, destinationAddress, message);

			// �������� ��������� � ��������� ������� ������
			//textAreaIncoming.append("� �������� -> " + destinationAddress + ": "	+ message + "\n");
			appendMessage("� -> " + destinationAddress + ": " + message);
			textAreaOutgoing.setText("");

		}

		catch (UnknownHostException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.this,"Не удалось отправить сообщение: узел-адресат не найден","Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.this,"Не удалось отправить сообщение", "Ошибка",JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final MainFrame frame = new MainFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	public void messageReceived(Peer sender, String message) {
		appendMessage(sender.getName() + " (" + sender.getAddress().getHostName() + ": " + sender.getAddress().getPort() + "): " + message);

		//textAreaIncoming.setText(message);
	}

	public synchronized void appendMessage(String message, Font font, Color color)
	{
		String html = "<span style=\"color:#"
			+ Integer.toHexString(color.getRGB()).substring(2)
			+ "; font-family:"
			+ font.getFamily()
			+ "; font-size:"
			+ font.getSize()
			+ "pt; font-weight:"
			+ (font.getStyle() == Font.BOLD ? "bold" : "")
			+ "\">"
			+ message
			+ "</span><br/>";
		incomingText.insert(0, html);
		textAreaIncoming.setText(incomingText.toString());
	}

	public synchronized void appendMessage(String message)
	{
		String smile = ":)";
		if(message.contains(smile)) {
			int pos = message.indexOf(smile);
			message = message.substring(0, pos) + "<img src=\"file:\\f:\\angel.PNG\" width=25 height=25>" + message.substring(pos+3);
		}
		String html = "<span>" + message + "</span><br/>";
		incomingText.insert(0, html);
		String text  = incomingText.toString();
		textAreaIncoming.setText(text);
	}
}
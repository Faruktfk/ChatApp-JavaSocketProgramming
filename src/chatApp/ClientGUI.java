package chatApp;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.Timer;

import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;

public class ClientGUI implements ActionListener {

	public static void main(String[] args) {
		new ClientGUI();
	}

	private Client client;
	private JTextPane chatPane, typePane;
	private final String SEND = "send";
	private final Timer timer = new Timer(500, this);

	private final static String ip = "localhost";
	private final static int port = 4444;

	public ClientGUI() {
		String username = JOptionPane.showInputDialog("Username: ");
		if (username == null || username.isBlank()) {
			JOptionPane.showMessageDialog(null, "Geben Sie einen Benutzernamen ein!");
			return;
		}
		try {
			client = new Client(new Socket(ip, port), username);
			client.listenForMessage();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "SERVER l�uft nicht!");
			return;
		}
		timer.start();
		
		JFrame frame = new JFrame("MyMessenger");
		frame.setSize(600, 590);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		chatPane = new JTextPane();
		chatPane.setFont(new Font("Cambria", Font.PLAIN, 15));
		chatPane.setBorder(new TitledBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED), new BevelBorder(BevelBorder.LOWERED)), "Chat",TitledBorder.LEADING, TitledBorder.TOP));
		chatPane.setAlignmentY(50.5f);
		chatPane.setEditable(false);
		JScrollPane scroll = new JScrollPane(chatPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(17, 20, 550, 400);
		frame.getContentPane().add(scroll);

		typePane = new JTextPane();
		typePane.setFont(new Font("Cambria", Font.PLAIN, 15));
		typePane.setBorder(new TitledBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED), new BevelBorder(BevelBorder.LOWERED)), username,TitledBorder.LEADING, TitledBorder.TOP));
		typePane.setAlignmentY(50.5f);
		typePane.setEditable(true);
		JScrollPane scroll1 = new JScrollPane(typePane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll1.setBounds(17, 450, 450, 70);
		frame.getContentPane().add(scroll1);
		
		JButton sendBtn = new JButton("SEND");
		sendBtn.setBackground(Color.LIGHT_GRAY);
		sendBtn.setFocusable(false);
		sendBtn.setFont(new Font("Calibri", Font.BOLD, 14));
		sendBtn.setBounds(477, 464, 90, 41);
		sendBtn.setActionCommand(SEND);
		sendBtn.addActionListener(this);
		frame.add(sendBtn);
		
		
		frame.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			String messageToSend = typePane.getText().strip().trim();
			if(messageToSend.isEmpty()) {
				return;
			}
			client.sendMessage(messageToSend);
			typePane.setText("");
			
		}else {
			chatPane.setText(client.getHistory());			
		}
	}
}

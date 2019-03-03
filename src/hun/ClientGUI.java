package hun;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientGUI implements ErrorListener , MsgListener , WindowListener{
	private JFrame mainFrame;
	private JLabel roundCounter , roundTitle , roundDescription , roundPrice , roundWinner;
	private JTextField newOffer;
	private JButton submitBtn , disconnectBtn , connectBtn;

	private AppClient client;
	public ClientGUI()
	{
		initApp();
		connectServer();
	}
	private void connectServer()
	{
		client = new AppClient();
		client.setErrorListener(this);
		client.setMsgListener(this);
		client.connect();
	}
	private void initApp()
	{
		mainFrame = new JFrame("Client App");
		mainFrame.addWindowListener(this);
		Container con = mainFrame.getContentPane();
		con.setLayout(new BorderLayout(10 , 10));
		JPanel mainPanel = new JPanel(new GridLayout(9 , 1 , 10 , 10));
		mainPanel.setBorder(new EmptyBorder(10 , 10 , 10 , 10));
		roundCounter = new JLabel("00:00:00");
		roundTitle = new JLabel("Shoes");
		roundDescription = new JLabel("This shoes is the best!");
		roundPrice = new JLabel("$50");
		roundWinner = new JLabel("127.0.0.1");
		newOffer = new JTextField("new offer");
		submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0)
			{
				try{
					float curPrice = Float.parseFloat(roundPrice.getText().toString().substring(1 , roundPrice.getText().toString().length()));
					float newPrice = Float.parseFloat(newOffer.getText().toString());

					String timeRemain = roundCounter.getText().toString().substring(0 , roundCounter.getText().toString().length() - 1);
					if(newPrice > curPrice)
					{
						if(!client.isClientClosed())
						{
							Command com = new Command();
							com.put("req", "NEW");
							com.put("price", "" + newPrice);
							client.send(com);
						}
					}
					else
					{
						JOptionPane.showMessageDialog(mainFrame, "New offer must be bigger than current price");
					}
				}
				catch(NumberFormatException e)
				{
					JOptionPane.showMessageDialog(mainFrame, "Please fill with number in price field");
				}
			}
		});
		disconnectBtn = new JButton("Disconnect");
		disconnectBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(!client.isClientClosed())
					client.disconnectClient();
			}

		});
		connectBtn = new JButton("Connect");
		connectBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0)
			{
				if(client.isClientClosed())
				{
					connectServer();
				}
			}
		});

		mainPanel.add(roundCounter);
		mainPanel.add(roundTitle);
		mainPanel.add(roundDescription);
		mainPanel.add(roundPrice);
		mainPanel.add(roundWinner);
		mainPanel.add(newOffer);
		mainPanel.add(submitBtn);
		mainPanel.add(disconnectBtn);
		mainPanel.add(connectBtn);

		con.add(mainPanel , BorderLayout.CENTER);


		mainFrame.setSize(300 , 350);
		mainFrame.setVisible(true);

	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientGUI client = new ClientGUI();
	}

	/*msg handler from app client*/
	@Override
	public void msgReceived(Command msg, String ip) {
		// TODO Auto-generated method stub
		System.out.println("Msg received : " + msg.toString() + " From : " + ip);

		if(msg.get("req").equals("TIME"))
		{
			roundCounter.setText(msg.get("time"));
			roundTitle.setText(msg.get("title"));
			roundDescription.setText(msg.get("description"));
			roundWinner.setText(msg.get("winner"));
			roundPrice.setText("$" + msg.get("price"));
		}

	}

	/*err handler from app client*/
	@Override
	public void errorOccured(String msg, String ip) {
		// TODO Auto-generated method stub
		System.out.println("Error occured : " + msg + " From : " + ip);

		//		int decision = JOptionPane.showConfirmDialog(mainFrame , msg + " From:" + ip , "Error" , JOptionPane.YES_NO_OPTION);
		//		int decision = JOptionPane.showConfirmDialog(null, "Do you like bacon?");
	}

	/*window state listener begin*/
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	/*window state listener end*/
}

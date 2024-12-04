package views;

import java.awt.EventQueue;
import java.security.NoSuchProviderException;

import javax.swing.JFrame;

public class Home extends JFrame {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Home() throws NoSuchProviderException {
		setTitle("Tool CKDT ");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 980, 600);
		CreateDigitalSignatureScreen mainPanel = new CreateDigitalSignatureScreen();
		setContentPane(mainPanel);
	}
}

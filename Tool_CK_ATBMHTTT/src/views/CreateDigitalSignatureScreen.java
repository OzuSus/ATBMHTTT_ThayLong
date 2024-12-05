package views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controller.Key;
import model.DS;


public class CreateDigitalSignatureScreen extends JPanel {

	private Key key = new Key();
	private JTextArea textAreaKhoaCongKhai;
	private JTextArea textAreaKhoaBiMat;
	private JPanel contentPane;
	private JTextField textField_UserName;
	private JTextField textField_Email;
	private JTextField textField_Phone;
	private JFrame frame = new JFrame();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateDigitalSignatureScreen frame = new CreateDigitalSignatureScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CreateDigitalSignatureScreen() throws NoSuchProviderException {

		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);
		setBounds(100, 100, 978, 599);
		setBackground(SystemColor.window);


		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(174, 186, 198));
		panel.setBounds(0, 0, 964, 75);
		add(panel);

		JLabel lblNewLabel = new JLabel("Logo School here");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(32, 5, 61, 61);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Trường Đại học Nông Lâm Tp.HCM");
		lblNewLabel_1.setFont(new Font("Segoe UI Light", Font.BOLD, 24));
		lblNewLabel_1.setBounds(121, 5, 466, 36);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Khoa CNTT - An toàn và bảo mật hệ thống thông tin");
		lblNewLabel_1_1.setFont(new Font("Segoe UI Light", Font.BOLD, 22));
		lblNewLabel_1_1.setBounds(121, 37, 660, 30);
		panel.add(lblNewLabel_1_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.window);
		panel_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "T\u1EA1o Kh\u00F3a",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe UI Light", Font.BOLD, 16),
				new Color(179, 179, 179)));
		panel_1.setBounds(32, 95, 438, 442);
		add(panel_1);
		panel_1.setLayout(null);

//		JLabel lblNewLabel_2 = new JLabel("Độ dài Key");
//		lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 18));
//		lblNewLabel_2.setBounds(20, 36, 94, 26);
//		panel_1.add(lblNewLabel_2);
//
//		JComboBox comboBox = new JComboBox();
//		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		comboBox.setModel(new DefaultComboBoxModel(new String[] { "1024"}));
//		comboBox.setBounds(124, 36, 94, 27);
//		panel_1.add(comboBox);

		JLabel lblNewLabel_3 = new JLabel("Khóa công khai");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblNewLabel_3.setBounds(20, 82, 123, 26);
		panel_1.add(lblNewLabel_3);

		JButton btnLoadPublicKey = new JButton("Load Public Key");
		btnLoadPublicKey.setForeground(SystemColor.text);
		btnLoadPublicKey.setBackground(new Color(52, 152, 219));
		btnLoadPublicKey.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnLoadPublicKey.setBounds(265, 75, 150, 27); // Vị trí nút bên cạnh TextArea khóa công khai
		panel_1.add(btnLoadPublicKey);
		btnLoadPublicKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try (BufferedReader br = new BufferedReader(new FileReader(file))) {
						StringBuilder content = new StringBuilder();
						String line;
						while ((line = br.readLine()) != null) {
							content.append(line).append("\n");
						}
						textAreaKhoaCongKhai.setText(content.toString().trim());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Lỗi khi đọc public key: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
		scrollPane.setBounds(20, 118, 396, 120);
		panel_1.add(scrollPane);

		textAreaKhoaCongKhai = new JTextArea();
		textAreaKhoaCongKhai.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		textAreaKhoaCongKhai.setLineWrap(true);
		scrollPane.setViewportView(textAreaKhoaCongKhai);
		textAreaKhoaCongKhai.setBorder(null);

		JLabel lblNewLabel_3_1 = new JLabel("Khóa bí mật");
		lblNewLabel_3_1.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblNewLabel_3_1.setBounds(20, 261, 123, 26);
		panel_1.add(lblNewLabel_3_1);

		JButton btnLoadPrivateKey = new JButton("Load Private Key");
		btnLoadPrivateKey.setForeground(SystemColor.text);
		btnLoadPrivateKey.setBackground(new Color(52, 152, 219));
		btnLoadPrivateKey.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnLoadPrivateKey.setBounds(265, 261, 150, 27); // Vị trí nút bên cạnh TextArea khóa bí mật
		panel_1.add(btnLoadPrivateKey);
		btnLoadPrivateKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try (BufferedReader br = new BufferedReader(new FileReader(file))) {
						StringBuilder content = new StringBuilder();
						String line;
						while ((line = br.readLine()) != null) {
							content.append(line).append("\n");
						}
						textAreaKhoaBiMat.setText(content.toString().trim());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Lỗi khi đọc private key: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new LineBorder(new Color(179, 179, 179)));
		scrollPane_1.setBounds(20, 297, 396, 120);
		panel_1.add(scrollPane_1);

		textAreaKhoaBiMat = new JTextArea();
		textAreaKhoaBiMat.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		textAreaKhoaBiMat.setLineWrap(true);
		scrollPane_1.setViewportView(textAreaKhoaBiMat);
		textAreaKhoaBiMat.setBorder(null);

//		JButton btnKhoiTao = new JButton("Khởi tạo");
//		btnKhoiTao.setForeground(SystemColor.text);
//		btnKhoiTao.setBackground(new Color(52, 152, 219));
//		btnKhoiTao.setFocusPainted(false);
//		btnKhoiTao.addActionListener(new ActionListener() {
//		    public void actionPerformed(ActionEvent e) {
//		        try {
//
//		            DS ds = new DS("RSA", "SHA256withRSA", "SUN");
//		            if (ds.genKey()) {
//
//		                String publicKeyBase64 = Base64.getEncoder().encodeToString(ds.keyPair.getPublic().getEncoded());
//		                String privateKeyBase64 = Base64.getEncoder().encodeToString(ds.keyPair.getPrivate().getEncoded());
//
//		                textAreaKhoaCongKhai.setText(publicKeyBase64);
//		                textAreaKhoaBiMat.setText(privateKeyBase64);
//
//		                JOptionPane.showMessageDialog(null, "Tạo khóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//		            } else {
//		                JOptionPane.showMessageDialog(null, "Không thể tạo cặp khóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//		            }
//		        } catch (Exception ex) {
//		            JOptionPane.showMessageDialog(null, "Lỗi khi tạo khóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//		            ex.printStackTrace();
//		        }
//		    }
//		});

//		btnKhoiTao.setFont(new Font("Times New Roman", Font.PLAIN, 16));
//		btnKhoiTao.setBounds(239, 36, 90, 27);
//		panel_1.add(btnKhoiTao);

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setLayout(null);
		panel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel_1_1.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Tạo chữ ký điện tử", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Segoe UI Light", Font.BOLD, 16), new Color(179, 179, 179)));
		panel_1_1.setBackground(Color.WHITE);
		panel_1_1.setBounds(480, 95, 458, 442);
		add(panel_1_1);

		JLabel lblNewLabel_4 = new JLabel("Plain text");
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblNewLabel_4.setBounds(46, 37, 91, 29);
		panel_1_1.add(lblNewLabel_4);

		JScrollPane scrollPaneUserName = new JScrollPane();
		scrollPaneUserName.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
		scrollPaneUserName.setBounds(46, 76, 362, 80);
		panel_1_1.add(scrollPaneUserName);

		JTextArea textArea_UserName = new JTextArea();
		textArea_UserName.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		textArea_UserName.setLineWrap(true);
		textArea_UserName.setWrapStyleWord(true);
		scrollPaneUserName.setViewportView(textArea_UserName);

		JLabel lblNewLabel_4_2 = new JLabel("Cipher text");
		lblNewLabel_4_2.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblNewLabel_4_2.setBounds(46, 202, 91, 29);
		panel_1_1.add(lblNewLabel_4_2);

		JScrollPane scrollPanePhone = new JScrollPane();
		scrollPanePhone.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
		scrollPanePhone.setBounds(46, 241, 362, 80);
		panel_1_1.add(scrollPanePhone);

		JTextArea textArea_Phone = new JTextArea();
		textArea_Phone.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		textArea_Phone.setLineWrap(true);
		textArea_Phone.setWrapStyleWord(true);
		scrollPanePhone.setViewportView(textArea_Phone);

		JButton btnKhiToFile = new JButton("Khởi tạo chữ kí");
		btnKhiToFile.setForeground(SystemColor.text);
		btnKhiToFile.setFocusPainted(false);
		btnKhiToFile.setBackground(new Color(26, 188, 156));
		btnKhiToFile.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		btnKhiToFile.setBounds(135, 371, 193, 37);
		panel_1_1.add(btnKhiToFile);
		btnKhiToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					String privateKeyBase64 = textAreaKhoaBiMat.getText();
					byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
					PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
					KeyFactory keyFactory = KeyFactory.getInstance("RSA");
					PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

					DS ds = new DS("RSA", "SHA1PRNG");
					ds.privateKey = privateKey;


					String plainText = textArea_UserName.getText();
					if (plainText.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Vui lòng nhập nội dung cần ký!", "Thông báo", JOptionPane.WARNING_MESSAGE);
						return;
					}


					String signature = ds.sign(plainText);


					textArea_Phone.setText(signature);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Lỗi khi tạo chữ ký: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});



	}
}

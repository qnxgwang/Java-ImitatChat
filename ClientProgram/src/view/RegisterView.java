package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entity.User;
import serivce.RegisterService;

public class RegisterView extends JFrame{
	
	/**
	 * ע�ᴰ�ڵ�����
	 */
	private JLabel usernameJlabel = null;
	
	private JTextField usernameJTextField = null;
	
	private JLabel realnameJlabel = null;
	
	private JTextField realnameJTextField = null;
	
	private JLabel pwdJlabel = null;
	
	private JPasswordField pwdJPasswordField = null;
	
	private JLabel confirmJlabel = null;
	
	private JPasswordField confirmJPasswordField = null;
	
	private JLabel photoJlabel = null;
	
	private JComboBox<String> photoJComboBox = null;
	
	private JPanel centerJPanel = null;
	
	private JButton submitJButton = null;
	
	private JButton resetJButton = null;
	
	private JButton backJButton = null;
	
	private JPanel southJPanel = null;
	
	/**
	 * ����ע�ᴰ��
	 */
	public void createFrame() {
		usernameJlabel = new JLabel("�û��˺�");
		usernameJTextField = new JTextField(20);
		realnameJlabel = new JLabel("�û�����");
		realnameJTextField = new JTextField(20);
		photoJlabel = new JLabel("�û�ͷ��");
        photoJComboBox=new JComboBox<String>();    
        photoJComboBox.addItem("--��ѡ��--");    
        photoJComboBox.addItem("user1.png");
        photoJComboBox.addItem("user2.png");
        photoJComboBox.addItem("user3.png");
        photoJComboBox.setPreferredSize(new Dimension(200,20));
		pwdJlabel = new JLabel("�û�����");
		pwdJPasswordField = new JPasswordField(20);
		confirmJlabel = new JLabel("ȷ������");
		confirmJPasswordField = new JPasswordField(20);
		centerJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		centerJPanel.add(usernameJlabel);
		centerJPanel.add(usernameJTextField);
		centerJPanel.add(realnameJlabel);
		centerJPanel.add(realnameJTextField);
		centerJPanel.add(photoJlabel);
		centerJPanel.add(photoJComboBox);
		centerJPanel.add(pwdJlabel);
		centerJPanel.add(pwdJPasswordField);
		centerJPanel.add(confirmJlabel);
		centerJPanel.add(confirmJPasswordField);
		this.add(centerJPanel,BorderLayout.CENTER);
			
		submitJButton = new JButton("ע��");
		submitJButton.addMouseListener(new SubmitMouseAdapter()); 

		resetJButton = new JButton("��������");
		resetJButton.addMouseListener(new ResetMouseAdapter());
		
		backJButton = new JButton("����");
		backJButton.addMouseListener(new BackMouseAdapter());
	
		southJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southJPanel.add(submitJButton);		
		southJPanel.add(backJButton);	
		southJPanel.add(resetJButton);
		this.add(southJPanel,BorderLayout.SOUTH);
		
		this.setTitle("�û�ע��");
		this.setBounds(500,500,300,300);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * ��ť������
	 * @author 14005
	 *
	 */
	class ResetMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			pwdJPasswordField.setText("");
			confirmJPasswordField.setText("");
		}
	}
	
	/**
	 * ��ť������
	 * @author 14005
	 *
	 */
	class BackMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			RegisterView.this.dispose();
            LoginView back = new LoginView();
            back.createFrame();
		}
	}
	
	/**
	 * ��ť������
	 * @author 14005
	 *
	 */
	class SubmitMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			String pwdText = pwdJPasswordField.getText();
			String confirmText = confirmJPasswordField.getText();
			System.out.println(pwdText);
			if(pwdText.equals(confirmText)) {
				User user = new User();
				user.setUsername(usernameJTextField.getText());
				user.setRealname(realnameJTextField.getText());
				user.setPhoto(photoJComboBox.getSelectedItem().toString());
				user.setPwd(pwdJPasswordField.getText());
				RegisterService registerService = new RegisterService();
				boolean registerflag = registerService.register(user);
				if(registerflag) {
					JOptionPane.showMessageDialog(RegisterView.this, "ע��ɹ�");
				}
				else {
					JOptionPane.showMessageDialog(RegisterView.this, "�û����Ѿ���ע�ᣬ������û���");
				}
			}
			else {
				JOptionPane.showMessageDialog(RegisterView.this, "�����������벻һ��");
			}
		}
	}
	
}

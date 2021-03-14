package serivce;

import java.awt.FlowLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dao.DataDao;
import dao.UserDao;
import entity.ChatContent;
import entity.Message;
import entity.User;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class UserService extends JFrame{
	public static void main(String [] args) {
		UserService serverView = new UserService();
		serverView.createFrame();
	}
	
	UserDao userDao = new UserDao(); 
	DataDao dataDao = new DataDao();  
	JLabel Label = new JLabel("�����Ѿ�����3.0");
	JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	Map<String,Socket> loginSocketMap = new HashMap<String,Socket>();
	
	public void createFrame(){
		panel.add(Label);
		this.add(panel);
		this.setTitle("�����");
		this.setBounds(500,500,500,500);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        new Thread() {
        	public void run() {
        		try {
					serverForChat();
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        }.start();
        new Thread() {
        	public void run() {
        		try {
					serverForOrder();
				} catch (Exception e) {
					e.printStackTrace();
				} 
        	}
        }.start();
	}
	/**
	 * ��¼�����غ����б���socket����map
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public void serverForChat() throws IOException, ClassNotFoundException {
		ServerSocket serverSocketForChat = new ServerSocket(IpUtil.getLoginPort());
		while(true) {
			Socket socket = serverSocketForChat.accept();
			Message requestMessage = SocketUtil.getSocketUtil().readMessage(socket);
			Message responseMessage = new Message();
			if(userDao.login(requestMessage.getUser().getUsername(),requestMessage.getUser().getPwd())!=null) {
				List<User> userList =  userDao.getFriendListByUsername(requestMessage.getUser().getUsername());//�õ������б�
				List<User> responseUserList = new ArrayList<User>();
                for(int i=0;i<userList.size();i++) {
                	User user = userList.get(i);
                	List<ChatContent> contentList = dataDao.queryData(requestMessage.getUser().getUsername(), user.getUsername());//��ѯ���ѷ�����������Ϣ
                	user.setContentList(contentList);
                	user.setOnlineFlag(loginSocketMap.containsKey(user.getUsername()));
                	responseUserList.add(user);
                }
                responseMessage.setUserList(responseUserList);
				responseMessage.setMessageType(MessageType.LOGIN_SUCCESS);	
				loginSocketMap.put(requestMessage.getUser().getUsername(), socket);				
				DataDao.deleteData(requestMessage.getUser().getUsername());//�����ѷ��͵ĵ�������Ϣ�����ݿ�ɾ��
			}
			else {
				responseMessage.setMessageType(MessageType.LOGIN_FAILURE);
			}
			SocketUtil.getSocketUtil().writeMessage(socket, responseMessage);				
		}
	}
	/**
	 * ����ָ��
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	public void serverForOrder() throws IOException, ClassNotFoundException, InterruptedException {	
		ServerSocket serverSocketForOrder = new ServerSocket(IpUtil.getOrderPort());
		while(true) {
			Socket socket = serverSocketForOrder.accept();
			Message requestMessage = SocketUtil.getSocketUtil().readMessage(socket);
			switch(requestMessage.getMessageType()) {
			    /**
			     * ����ע����Ϣ
			     */
				case MessageType.REGISTER:{
					Message responseMessage = new Message();
					if(userDao.queryUserByUsername(requestMessage.getUser().getUsername())==null) {
						userDao.register(requestMessage.getUser());
						responseMessage.setMessageType(MessageType.REGISTER_SUCCESS);
					}
					else {
						responseMessage.setMessageType(MessageType.REGISTER_FAILURE);
					}
					SocketUtil.getSocketUtil().writeMessage(socket, responseMessage);
					break;
				}
				/**
				 * �����û��������ѵ���Ϣ
				 */
				case MessageType.MESSAGE_TO_FRIEND:{
					String username = requestMessage.getUser().getChatContent().getTo();					
					if(loginSocketMap.containsKey(username)) {
						requestMessage.setMessageType(MessageType.MESSAGE_FROM_FRIEND);
						Socket loginSocket = loginSocketMap.get(username);
						SocketUtil.getSocketUtil().writeMessage(loginSocket, requestMessage);
					}
					else {
						ChatContent chatContent= requestMessage.getUser().getChatContent();
						dataDao.insertData(chatContent);
					}
					break;
				}
				/**
				 * �û��˳���¼�����û�socket��map���Ƴ�������֪����
				 */
				case MessageType.EXIT:{
					System.out.println("ִ��EXIT"+" "+"����@username="+requestMessage.getUser().getUsername());
					loginSocketMap.remove(requestMessage.getUser().getUsername());
					for(Socket loginSocket : loginSocketMap.values()) {
						requestMessage.setMessageType(MessageType.UPDATE_FRIEND_LIST_EXIT);
						SocketUtil.getSocketUtil().writeMessage(loginSocket, requestMessage);
					}
					break;
				}
				/**
				 * �û���¼,���û���¼��Ϣ���͸�����
				 */
				case MessageType.LOGIN_TO_FRIEND:{		
					for(String key : loginSocketMap.keySet()) {
						if(! key.equals(requestMessage.getUser().getUsername())) {
							requestMessage.setMessageType(MessageType.UPDATE_FRIEND_LIST_LOGIN);
							SocketUtil.getSocketUtil().writeMessage(loginSocketMap.get(key), requestMessage);
							System.out.println("ִ��LOGIN_TO_FRIEND"+" "+key);
						}
						
					}
					break;
				}
				default:break;
			}
			socket.close();
		}
	}
}

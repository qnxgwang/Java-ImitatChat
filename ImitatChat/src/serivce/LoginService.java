package serivce;

import java.net.Socket;
import java.util.List;

import entity.Message;
import entity.User;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class LoginService {

	private Socket loginSocket = null;
	private List<User> userList = null;
	public Socket getDataSocket() {
		return loginSocket;
	}
	public List<User> getUserList() {
		return userList;
	}
	/**
	 * �û���¼
	 * @param user
	 * @return
	 */
	public boolean login(User user) {	    
		try {
			Socket socket = new Socket(IpUtil.getIp(),IpUtil.getLoginPort());
			Message requestMessage = new Message();
			requestMessage.setMessageType(MessageType.LOGIN);
			requestMessage.setUser(user);
			SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);//�����˷��͵�¼����
			Message responsemessage = SocketUtil.getSocketUtil().readMessage(socket);//�ӷ���˽��շ���		
			if(responsemessage.getMessageType() == MessageType.LOGIN_SUCCESS) {		
				userList = responsemessage.getUserList();
				loginSocket = socket;
				return true;
			}
			else 
				return false;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * ����¼��Ϣ���͸�����(������ת��)
	 * @param username
	 */
	public void sendLoginMessageToFriend(String  username) {
		System.out.println("ִ��sendLoginMessageToFriend"+" "+"����@username="+username);
		try {
			Socket socket = new Socket(IpUtil.getIp(),IpUtil.getOrderPort());
			Message requestMessage = new Message();
			User user = new User();
			user.setUsername(username);
			requestMessage.setMessageType(MessageType.LOGIN_TO_FRIEND);
			requestMessage.setUser(user);
			SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);//����ѷ��͵�¼��Ϣ
			socket.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

package serivce;

import java.net.Socket;
import java.util.List;

import entity.Message;
import entity.User;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class LoginService {
	/**
	 * �û���¼
	 * @param user
	 * @return
	 */
	private Socket loginSocket = null;
	private List<User> userList = null;
	public Socket getDataSocket() {
		return loginSocket;
	}
	public List<User> getUserList() {
		return userList;
	}
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
}

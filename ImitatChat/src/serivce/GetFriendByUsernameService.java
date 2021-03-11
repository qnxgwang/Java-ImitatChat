package serivce;

import java.net.Socket;
import java.util.List;

import entity.Message;
import entity.User;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class GetFriendByUsernameService {
	/**
	 * ��ȡ�����б�
	 * @param username
	 * @return
	 */
	public List<User> getFriendByUsername(String username){
		try {
			Socket socket = new Socket(IpUtil.getIpUtil().getIp(),IpUtil.getIpUtil().getPort());
			User user = new User();
			user.setUsername(username);
			Message requestMessage = new Message();
			requestMessage.setMessageType(MessageType.GET_FRIEND_LIST);
			requestMessage.setUser(user);
			SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);//�����˷��͵�¼����
			Message responsemessage = SocketUtil.getSocketUtil().readMessage(socket);//�ӷ���˽��շ���	
			socket.close();		
			if(responsemessage.getMessageType() == MessageType.GET_FRIEND_LIST_SUCCESS) {
				return responsemessage.getUserList();
			}
			else 
				return null;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
}

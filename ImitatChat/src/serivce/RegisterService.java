package serivce;

import java.net.Socket;

import entity.Message;
import entity.User;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class RegisterService {
	/**
	 * �û�ע��
	 * @param user
	 * @return
	 */
	public boolean register(User user) {		    
		try {
			Socket socket = new Socket(IpUtil.getIpUtil().getIp(),IpUtil.getIpUtil().getPort());
			Message requestMessage = new Message();
			requestMessage.setMessageType(MessageType.REGISTER);
			requestMessage.setUser(user);
			SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);//�����˷���ע������
			Message responsemessage = SocketUtil.getSocketUtil().readMessage(socket);//�ӷ���˽��շ���	
			socket.close();			
			if(responsemessage.getMessageType() == MessageType.REGISTER_SUCCESS) {
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

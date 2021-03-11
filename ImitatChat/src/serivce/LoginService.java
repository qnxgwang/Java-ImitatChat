package serivce;

import java.net.Socket;

import entity.Message;
import entity.User;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class LoginService {
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	public boolean login(User user) {	    
		try {
			Socket socket = new Socket(IpUtil.getIpUtil().getIp(),IpUtil.getIpUtil().getPort());
			Message requestMessage = new Message();
			requestMessage.setMessageType(MessageType.LOGIN);
			requestMessage.setUser(user);
			SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);//向服务端发送登录请求
			Message responsemessage = SocketUtil.getSocketUtil().readMessage(socket);//从服务端接收反馈	
			socket.close();		
			if(responsemessage.getMessageType() == MessageType.LOGIN_SUCCESS) {
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

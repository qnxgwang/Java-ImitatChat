package serivce;

import static java.lang.System.out;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import entity.Message;
import util.IpUtil;
import util.SocketUtil;

public class StatusThread extends Thread{
	
	private Map<String,Socket> loginSocketMap = null;
	
	public StatusThread(Map<String,Socket> loginSocketMap) {
		this.loginSocketMap = loginSocketMap;
	}
	
	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(IpUtil.getLoginPort());
			while(true) {            
				Socket socket = serverSocket.accept();
				out.println("case MessageType.LOGIN");
				Message requestMessage = SocketUtil.getSocketUtil().readMessage(socket);
				//�û���¼,���غ����б�
				HandleMessageUtil.handleLogin(requestMessage, loginSocketMap, socket);										
			}
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}

}

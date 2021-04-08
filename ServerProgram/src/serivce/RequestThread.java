package serivce;

import static java.lang.System.out;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import entity.Message;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class RequestThread extends Thread{
	
	
	private Map<String,Socket> loginSocketMap = null;
	
	private ExecutorService executor = null;
	
	public RequestThread(Map<String,Socket> loginSocketMap, ExecutorService executor) {
		this.loginSocketMap = loginSocketMap;
		this.executor = executor;
	}
	
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(IpUtil.getOrderPort());
			while(true) {
				//socket������finally��ر�,��Ϊ��Щ���������ʹ��socket,socketʹ����Ϻ��ڽӿ���ر�
				Socket socket = serverSocket.accept();
				Message requestMessage = SocketUtil.getSocketUtil().readMessage(socket);
				handleMessage(socket,requestMessage);									
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void handleMessage(Socket socket, Message requestMessage) {
		switch(requestMessage.getMessageType()) {
		    /*
		     * ����ע����Ϣ
		     */
			case MessageType.REGISTER:{
				out.println("case MessageType.REGISTER");
				executor.execute(()->{
					HandleMessageUtil.handleRegister(requestMessage, socket);																
				});
				break;				
			}
			/*
			 * �����û��������ѵ���Ϣ
			 */
			case MessageType.MESSAGE_TO_FRIEND:{
				out.println("case MessageType.MESSAGE_TO_FRIEND");
				executor.execute(()->{
					HandleMessageUtil.sendMessageToFriend(requestMessage, loginSocketMap, socket);												
				});					
				break;
			}
			/*
			 * �û��˳���¼�����û�socket��map���Ƴ�������֪����
			 */
			case MessageType.EXIT:{
				out.println("case MessageType.EXIT");
				executor.execute(()->{
					HandleMessageUtil.exitLogin(requestMessage, loginSocketMap, socket);
				});					
				break;
			}
			/*
			 * �û���¼,���û���¼��Ϣ���͸�����
			 */
			case MessageType.LOGIN_TO_FRIEND:{		
				out.println("case MessageType.LOGIN_TO_FRIEND");
				executor.execute(()->{
					HandleMessageUtil.updateOnlineList(requestMessage, loginSocketMap, socket);					
				});				
				break;
			}			
			/*
			 * �ͻ��˷����ļ�����
			 */
			case MessageType.FILE_TO_FRIEND:{
				out.println("case MessageType.FILE_TO_FRIEND");
				executor.execute(()->{
					HandleMessageUtil.sendFileToFriend(requestMessage, loginSocketMap, socket);					
				});
				break;
			}
			/*
			 * �ͻ��˶�ȡ�ļ�����
			 */
			case MessageType.FILE_REQUEST:{
				out.println("case MessageType.FILE_REQUEST");
				executor.execute(()->{
					HandleMessageUtil.requestFile(requestMessage, loginSocketMap, socket);						
				});
				break;
			}
			default:break;
		}				
	}
}

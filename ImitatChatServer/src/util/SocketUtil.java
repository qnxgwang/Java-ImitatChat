package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import entity.Message;

public class SocketUtil {

	private static SocketUtil socketUtil = null;
	
	public static SocketUtil getSocketUtil() {
		if(socketUtil == null) {
			socketUtil = new SocketUtil();
		}
		
		return socketUtil;
	}
	/**
	 * socket�������ݷ���
	 * @param socket
	 * @param responseMessage
	 * @throws IOException
	 */
	public void writeMessage(Socket socket,Message responseMessage) throws IOException {
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(responseMessage);
	}
	/**
	 * socket�������ݷ���
	 * @param socket
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Message readMessage(Socket socket) throws IOException, ClassNotFoundException {
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);		
		Message requestMessage = (Message)objectInputStream.readObject();//�ֽ��������л�
		return requestMessage;
	}
}

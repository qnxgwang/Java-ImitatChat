package handle;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import entity.FileData;
import entity.Message;
import entity.User;
import util.FileUtil;
import util.MessageType;
import util.SocketUtil;
import view.ChatView;
import view.FriendListView;


public class DataThread extends Thread{
	
	/**
	 * �����̣߳������������
	 */
	
	private Socket datasocket;
	
	private FriendListView friendListView;
	
	/**
	 * ������
	 * @param datasocket �׽���
	 * @param friendListView �����б���
	 */
	public DataThread(Socket datasocket,FriendListView friendListView) {
		this.datasocket = datasocket;
		this.friendListView = friendListView;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				//������������,ά�����������ͨ��
				Message reponseMessage = SocketUtil.getSocketUtil().readMessage(datasocket);

				switch(reponseMessage.getMessageType()) {
				/*
				 * �յ���������������Ϣ"���ѷ���������Ϣ����Ҫ��ʾ�����������"
				 */
				case MessageType.MESSAGE_FROM_FRIEND:{		
	                DataHandle.updateTextArea(reponseMessage, friendListView);
	                break;
				}
				/*
				 * �յ���������������Ϣ"�к��ѵ�¼����Ҫ���������б�"
				 */
				case MessageType.UPDATE_FRIEND_LIST_LOGIN:{		
					DataHandle.updateListView(reponseMessage, "src/Image/", friendListView);
					break;
				}
				/*
				 *  �յ���������������Ϣ"�к����˳���¼����Ҫ���������б�"
				 */
				case MessageType.UPDATE_FRIEND_LIST_EXIT:{					
					DataHandle.updateListView(reponseMessage, "src/Image/gray", friendListView);
					break;
				}
				/*
				 *  �յ����Է��������ļ�������ֻ����"�ļ�����"
				 */
				case MessageType.FILE_FROM_FRIEND:{
					DataHandle.updateLocalDB(reponseMessage, friendListView);
					break;
				}
				/*
				 * �յ����Է��������ļ����������"�ļ�����"��"�ļ�����"
				 */
				case MessageType.FILE_REQUEST:{
					DataHandle.downloadFile(reponseMessage, datasocket);												
					break;					
				}				
				default:break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
}

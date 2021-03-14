package Thread;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import entity.Message;
import util.FileUtil;
import util.MessageType;
import util.SocketUtil;
import view.ChatView;
import view.FriendListView;


public class DataThread extends Thread{
	
	/**
	 * �����̣߳������������
	 */
	
	Socket datasocket;
	FriendListView friendListView;
	public DataThread(Socket datasocket,FriendListView friendListView) {
		this.datasocket = datasocket;
		this.friendListView = friendListView;
	}
	@Override
	public void run() {
		while(true) {
			try {
				Message reponseMessage = SocketUtil.getSocketUtil().readMessage(datasocket);
				switch(reponseMessage.getMessageType()) {
				/**
				 * ��������Ժ��ѵ���Ϣ���������촰���Ƿ���ڶ��û����з���
				 */
				case MessageType.MESSAGE_FROM_FRIEND:{		
	                String username = reponseMessage.getUser().getChatContent().getTo();
	                String friendname = reponseMessage.getUser().getChatContent().getFrom();
	                String time = reponseMessage.getUser().getChatContent().getTimeStamp();
	                String content = reponseMessage.getUser().getChatContent().getContent();
	                FileUtil.writeFriendMessageToFile(username, time, friendname, content);
	                JLabel[] jLabel = friendListView.jLabel;
	                ChatView chatView = friendListView.tempChatView;
	                boolean flag = false; 
	                if(chatView != null) {
	                	if(! chatView.closed && chatView.friendUsername.equals(friendname)) {
	                		Document docs = chatView.recordPanel.getDocument();//����ı�����
	        			    try {
	        			    	AttributeSet attributeSet=new javax.swing.text.SimpleAttributeSet();
	        			        docs.insertString(docs.getLength(),new Date().toString()+" "+friendname+" "+content+'\n',attributeSet);//���ı�����׷��			    
	        			    } catch (BadLocationException e) {
	        			        e.printStackTrace();
	        			    }	
	        			    flag = true;
	                	}
	                } 
	                if(! flag) {
	                	 for(int i=0;i<jLabel.length;i++) {
		                     if(jLabel[i].getText().equals(friendname)) {
		                     	jLabel[i].setForeground(Color.red);
		                     }
	                     }
	                	 friendListView.revalidate();
	                }
	                break;
				}
				/**
				 * ����к��ѵ�¼�����������б�
				 */
				case MessageType.UPDATE_FRIEND_LIST_LOGIN:{		
					String imageUrl = "";
					JLabel[] jLabel = friendListView.jLabel;
					for(int i=0;i<friendListView.userList.size();i++) {
						if(friendListView.userList.get(i).getUsername().equals(reponseMessage.getUser().getUsername())) {
							imageUrl = friendListView.userList.get(i).getPhoto();						
							ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File("src/Image/"+imageUrl)));
							jLabel[i].setIcon(imageIcon);
							break;
						}
					}	
					friendListView.revalidate();
					break;
				}
				/**
				 *  ����к����˳���¼�����������б�
				 */
				case MessageType.UPDATE_FRIEND_LIST_EXIT:{
					String imageUrl = "";
					JLabel[] jLabel = friendListView.jLabel;
					for(int i=0;i<friendListView.userList.size();i++) {
						if(friendListView.userList.get(i).getUsername().equals(reponseMessage.getUser().getUsername())) {
							imageUrl = friendListView.userList.get(i).getPhoto();
							ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File("src/Image/gray"+imageUrl)));
							jLabel[i].setIcon(imageIcon);
							break;
						}
					}	
					friendListView.revalidate();
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

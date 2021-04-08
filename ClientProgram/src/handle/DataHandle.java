package handle;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import entity.ChatContent;
import entity.FileData;
import entity.FileInformation;
import entity.Message;
import entity.User;
import util.FileUtil;
import util.SocketUtil;
import view.ChatView;
import view.FriendListView;

public class DataHandle {
	
	/**
	 * �����ı���
	 * @param reponseMessage
	 * @param friendListView
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	static void updateTextArea(Message reponseMessage,FriendListView friendListView) throws UnsupportedEncodingException, IOException {
		ChatContent cct = reponseMessage.getUser().getChatContent();
		String username = cct.getMessageTo();
        String friendname = cct.getMessageFrom();
        String time = cct.getTimeStamp();
        String content = cct.getContent();
        FileUtil.writeFriendMessageToDB(username, time, friendname, content);
        updateLabel(friendListView,friendname,content);
	}
	/**
	 * ���±����ļ���
	 * @param reponseMessage
	 * @param friendListView
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	
	static void updateLocalDB(Message reponseMessage,FriendListView friendListView) throws UnsupportedEncodingException, IOException {
		FileInformation info = reponseMessage.getFileInformation();
		String sendname = info.getSendname();
        String receivename = info.getReceivename();
        String filename = info.getFileName();
        FileUtil.writeFriendMessageToDB(receivename,"", sendname, filename);//�����ѷ��͵�"�ļ�����"д�������¼���ݿ�
        FileUtil.writeFileName(receivename, sendname, filename);//�����ѷ��͵�"�ļ�����"д���ļ����ݿ���
        updateLabel(friendListView,sendname,filename);
	}
	
	/**
	 * ���������б�
	 * @param reponseMessage
	 * @param imagePath
	 * @param friendListView
	 * @throws IOException
	 */
	
	static void updateListView(Message reponseMessage,String imagePath,FriendListView friendListView) throws IOException {
		String imageUrl = "";
		JLabel[] jLabel = friendListView.getjLabel();
		List<User> userList = friendListView.getUserList();	
		String name = reponseMessage.getUser().getUsername();
		int index = 0;
		for(User u: userList) {
			if(u.getUsername().equals(name)) {
				index = userList.indexOf(u);
				imageUrl = u.getPhoto();
				break;
			}
		}
		ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File(imagePath+imageUrl)));
		jLabel[index].setIcon(imageIcon);	
		friendListView.revalidate();		
	}
	
	/**
	 * �����ļ�
	 * @param reponseMessage
	 * @param datasocket
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	
	static void downloadFile(Message reponseMessage,Socket datasocket) throws ClassNotFoundException, IOException {
		int totalIndex = Integer.valueOf(reponseMessage.getFileInformation().getTotalIndex());
		File downloadFile = new File(reponseMessage.getFileInformation().getFileName());
		downloadFile.createNewFile();
		for(int i=0; i<totalIndex; i++) {
			FileData filedata = SocketUtil.getSocketUtil().readFileData(datasocket);										
			FileOutputStream fo = new FileOutputStream(downloadFile);
			BufferedOutputStream bo = new BufferedOutputStream(fo);
			bo.write(filedata.getByteArray());
			bo.flush();
		}
	}
	/**
	 * �����ı�����ߺ����б�
	 * @param friendListView
	 * @param friendname
	 * @param content
	 */
	
	static void updateLabel(FriendListView friendListView,String friendname,String content) {
		JLabel[] jLabel = friendListView.getjLabel();
        ChatView chatView = friendListView.getTempChatView();
        boolean flag = false; 
        if(chatView != null) {
        	if(! chatView.isClosed() && chatView.getFriendUsername().equals(friendname)) {
        		Document docs = chatView.getRecordPanel().getDocument();//����ı�����
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
	}

}

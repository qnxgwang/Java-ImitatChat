package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class FileUtil {
	
	
	
	/**
	 * ��ȡ�����¼
	 * @param username �û���
	 * @param friendname ������
	 * @return �����¼
	 */
	public static List<String> readRecord(String receivename,String sendname) {
        List<String> list = new LinkedList<String>();
        try {
            File f = new File(receivename+"+"+sendname+".txt");
            if (f.isFile() && f.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(f), "UTF-8");
                BufferedReader reader = new BufferedReader(read);             
                String line;
                while ((line = reader.readLine()) != null) {
                	list.add(line);
                }
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
	
	/**
	 * ��ȡ�����¼����ʾ�����촰��
	 * @param recordPanel �����¼����
	 * @param username �û���
	 * @param friendusername ������
	 */
	public static void readMessageFromFile(JTextPane recordPanel,String receivename,String sendname) {
		List<String> message = FileUtil.readRecord(receivename, sendname);
		String leavingMessage = "";
		for(int i=0 ;i<message.size();i++) {
			leavingMessage = leavingMessage + message.get(i) + '\n';
		}
		Document docs = recordPanel.getDocument();
		try {
	    	AttributeSet attributeSet=new javax.swing.text.SimpleAttributeSet();
	        docs.insertString(docs.getLength(),leavingMessage+'\n',attributeSet);//���ı�����׷��			    
	    } catch (BadLocationException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * �����ѷ��͵���Ϣд�������¼
	 * @param username
	 * @param time
	 * @param friendname
	 * @param content
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static void writeFriendMessageToDB(String username,String time,String friendname,String content) throws UnsupportedEncodingException, IOException {
        FileOutputStream fos = new FileOutputStream(username+"+"+friendname+".txt",true);
		fos.write(new String(time+" "+friendname+" "+content+"\n").getBytes("UTF-8"));
		fos.close();   
	}

	/**
	 * ���û����͵���Ϣд�������¼
	 * @param username
	 * @param time
	 * @param friendname
	 * @param content
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static void writeMyMessageToDB(String username,String time,String friendname,String content) throws UnsupportedEncodingException, IOException {
		FileOutputStream fos = new FileOutputStream(username+"+"+friendname+".txt",true);
		fos.write(new String(time+" "+username+" "+content+"\n").getBytes("UTF-8"));
	}
	
	/**
	 * �����ѷ��͵��ļ�����¼���ļ����ݿ�
	 * @param 
	 * @param 
	 * @param filename
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static void writeFileName(String receivename,String sendname,String filename) throws UnsupportedEncodingException, IOException {
        FileOutputStream fos = new FileOutputStream(receivename+"+"+sendname+"+file.txt",true);
		fos.write(new String(filename+"\n").getBytes("UTF-8"));
		fos.close();   
	}
	
	/**
	 * ���ļ����ݿ��ȡ�ļ���
	 * @param username
	 * @param friendname
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static List<String> readFileName(String receivename,String sendname) throws UnsupportedEncodingException, IOException {
        return readRecord(receivename,sendname+"+file");
	}

}

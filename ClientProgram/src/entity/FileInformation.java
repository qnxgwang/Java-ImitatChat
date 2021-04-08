package entity;

import java.io.Serializable;

public class FileInformation implements Serializable{
	
	/**
	 *    sendname�������ļ����û�
	 *    receivename��������ļ����û�
	 */
	private static final long serialVersionUID = 4L;

	private String fileName;
	
	private String totalIndex;
	
	private String md5;
	
	private String sendname;
	
	private String receivename;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTotalIndex() {
		return totalIndex;
	}

	public void setTotalIndex(String totalIndex) {
		this.totalIndex = totalIndex;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getSendname() {
		return sendname;
	}

	public void setSendname(String sendname) {
		this.sendname = sendname;
	}

	public String getReceivename() {
		return receivename;
	}

	public void setReceivename(String receivename) {
		this.receivename = receivename;
	}

	
}

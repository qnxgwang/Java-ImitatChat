package util;

public interface MessageType {

	/**
	 * LOGIN_SUCCESS �û���¼�ɹ�
	 * LOGIN_FAILURE �û���¼ʧ��
	 * LOGIN �û���¼
	 * EXIT �û��˳���¼
	 * REGISTER �û�ע��
	 * REGISTER_SUCCESS �û�ע��ɹ�
	 * REGISTER_FAILURE �û�ע��ʧ��
	 * GET_FRIEND_LIST ��ȡ�����б�
	 * GET_FRIEND_LIST_SUCCESS ��ȡ�����б�ɹ�
	 * GET_FRIEND_LIST_FAILURE ��ȡ�����б�ʧ��
	 * MESSAGE_TO_FRIEND �����ѷ�����Ϣ
	 * MESSAGE_FROM_FRIEND ���Ժ��ѵ���Ϣ
	 * UPDATE_FRIEND_LIST_LOGIN �û���¼�����¿ͻ��������б�
	 * UPDATE_FRIEND_LIST_EXIT �û��˳���¼�����¿ͻ��������б�
	 * LOGIN_TO_FRIEND �û���¼����֪������
	 */
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAILURE = 2;
	public static final int LOGIN = 3;
	public static final int EXIT = 4;
	public static final int REGISTER = 5;
	public static final int REGISTER_SUCCESS = 6;
	public static final int REGISTER_FAILURE = 7;
	public static final int GET_FRIEND_LIST = 8;
	public static final int GET_FRIEND_LIST_SUCCESS = 9;
	public static final int GET_FRIEND_LIST_FAILURE = 10;
	public static final int MESSAGE_TO_FRIEND =11;
	public static final int MESSAGE_FROM_FRIEND = 12;
	public static final int UPDATE_FRIEND_LIST_LOGIN = 13;
	public static final int UPDATE_FRIEND_LIST_EXIT = 14;
	public static final int LOGIN_TO_FRIEND = 15;
	/*
	 * 
	 */
	public static final int FILE_TO_FRIEND = 16;
	public static final int FILE_FROM_FRIEND = 17;
	public static final int FILE_REQUEST= 18;
	
}

package serivce;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ThreadPool.BasicThreadPool;
import ThreadPoolUtil.ThreadPool;

import static java.lang.System.*;



public class ServerService { 

	//�������������ַ�ʽ�����̳߳�
	//ThreadPoolExecutor executor1 = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5));
	
	//�����̳߳�
	private static final ThreadPool threadPool = new BasicThreadPool(2,4,6,100);
	
	//�̳߳�API
	private static final ExecutorService executor = Executors.newCachedThreadPool();
	
	//�û������׽�����Ϊ��ֵ��,ʹ���̰߳�ȫ��ConcurrentHashMap
	private static final Map<String,Socket> loginSocketMap = new ConcurrentHashMap<String,Socket>();
	
	//�������
	public static void main(String [] args) throws ClassNotFoundException, IOException, InterruptedException {
		new StatusThread(loginSocketMap).start();	
		new RequestThread(loginSocketMap,executor).start();
	}

}

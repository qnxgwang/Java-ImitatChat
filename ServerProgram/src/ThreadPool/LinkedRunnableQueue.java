package ThreadPool;

import java.util.LinkedList;

import ThreadPoolUtil.DenyPolicy;
import ThreadPoolUtil.RunnableQueue;
import ThreadPoolUtil.ThreadPool;
/**
 * ��װ���̰߳�ȫ�Ķ���
 * @author 14005
 *
 */
public class LinkedRunnableQueue implements RunnableQueue{
	
	private final int limit;//���д�С����
	
	private final DenyPolicy denyPolicy;//�ܾ��������
	
	private final LinkedList<Runnable> runnableList = new LinkedList<Runnable>();//����
	
	private final ThreadPool threadPool;

	public LinkedRunnableQueue(int limit, DenyPolicy denyPolicy, ThreadPool threadPool) {
		super();
		this.limit = limit;
		this.denyPolicy = denyPolicy;
		this.threadPool = threadPool;
	}

	/**
	 * ������ύ����
	 */
	@Override
	public void offer(Runnable runnable) {
		synchronized(runnableList) {
			if(runnableList.size() >= limit) {
				denyPolicy.reject(runnable, threadPool);
			}else {
				runnableList.addLast(runnable);
				runnableList.notifyAll();
			}
		}	
	}

	/**
	 * �Ӷ���ȡ������
	 */
	@Override
	public Runnable Take() {
		synchronized(runnableList) {
			while(runnableList.isEmpty()) {
				try {
					runnableList.wait();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return runnableList.removeFirst();
	}

	/**
	 * ���ض��д�С
	 */
	@Override
	public int size() {
		synchronized(runnableList) {
			return runnableList.size();
		}
	}

}

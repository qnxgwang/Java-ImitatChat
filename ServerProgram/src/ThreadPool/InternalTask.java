package ThreadPool;

import ThreadPoolUtil.RunnableQueue;

/**
 * �̳߳��и�����������߳�
 * @author 14005
 *
 */
public class InternalTask implements Runnable{

	private final RunnableQueue runnableQueue; //�������
	
	private volatile boolean running = true; //�߳����б�־

	public InternalTask(RunnableQueue runnableQueue) {
		this.runnableQueue = runnableQueue;
	}
	
	@Override
	public void run() {
		while(running && !Thread.currentThread().isInterrupted()) {
			try {
				Runnable task = runnableQueue.Take();
				task.run();
			} catch(Exception e) {
				running = false;
				break;
			}
		}		
	}
	
	public void stop() {
		this.running = false;
	}
	
}

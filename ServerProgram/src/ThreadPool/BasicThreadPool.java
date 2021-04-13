package ThreadPool;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import ThreadPoolUtil.DenyPolicy;
import ThreadPoolUtil.RunnableQueue;
import ThreadPoolUtil.ThreadFactory;
import ThreadPoolUtil.ThreadPool;
/**
 * ʵ����ThreadPool�ӿڵ��̳߳�
 * @author 14005
 *
 */
public class BasicThreadPool extends Thread implements ThreadPool{
	
	/**
	 * �����̵߳�һ�����
	 * @author 14005
	 *
	 */
	private static class ThreadTask{
		
		Thread thread;
		
		InternalTask internalTask;
		
		public ThreadTask(Thread thread,InternalTask internalTask) {
			this.thread = thread;
			this.internalTask = internalTask;
		}
	}
	
	/**
	 * �����������̹߳���
	 * @author 14005
	 *
	 */
	private static class DefaultThreadFactory implements ThreadFactory{

		private static final AtomicInteger GROUP_COUNTER = new AtomicInteger(1);
		
		private static final ThreadGroup group = new ThreadGroup("MyThreadPool-"+GROUP_COUNTER.getAndIncrement());
		
		private static final AtomicInteger COUNTER = new AtomicInteger(0);
		
		@Override
		public Thread createThread(Runnable runnable) {
			return new Thread(group,runnable,"thread-pool-"+COUNTER.getAndIncrement());
		}		
	}

	
	private final int initSize;//��ʼ���߳�����
	
	private final int coreSize;//�������߳�����
	
	private final int maxSize;//�����߳�����
	
	private int activeCount;//��Ծ���߳�����
	
	private final ThreadFactory threadFactory;
	
	private final RunnableQueue runnableQueue; //�������
	
	private volatile boolean isShutDown = false;
	
	private final Queue<ThreadTask> threadQueue = new ArrayDeque<>();//�����̶߳���
	
	private final static DenyPolicy DEFAULT_DENY_POLICY = new DenyPolicy.DiscardDenyPolicy();
	
	private final static ThreadFactory DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();
	
	private final long keepAliveTime;
	
	private final TimeUnit timeUnit;
	
	public BasicThreadPool(int initSize, int coreSize, int maxSize, int queueSize) {
		this(initSize, coreSize, maxSize, queueSize,
				DEFAULT_THREAD_FACTORY, DEFAULT_DENY_POLICY, 10, TimeUnit.SECONDS);
	}
	
	public BasicThreadPool(int initSize, int coreSize, int maxSize,int queueSize, ThreadFactory threadFactory,
			DenyPolicy denyPolicy, long keepAliveTime, TimeUnit timeUnit) {
		super();
		this.initSize = initSize;
		this.coreSize = coreSize;
		this.maxSize = maxSize;
		this.threadFactory = threadFactory;
		this.runnableQueue = new LinkedRunnableQueue(queueSize, denyPolicy, this);
		this.keepAliveTime = keepAliveTime;
		this.timeUnit = timeUnit;
		this.init();
	}
	
	/**
	 * ��ʼ�����ɸ��߳�
	 */
	private void init() {
		start();
		for(int i=0;i<initSize;i++) {
			newThread();
		}
	}
	
	/**
	 * �½�һ���߳�
	 */
	private void newThread() {
		InternalTask internalTask = new InternalTask(runnableQueue);
		Thread thread = this.threadFactory.createThread(internalTask);
		ThreadTask threadTask = new ThreadTask(thread, internalTask);
		threadQueue.offer(threadTask);
		this.activeCount++;
		thread.start();
	}
	
	/**
	 * ����һ���߳�
	 */
	private void removeThread() {
		ThreadTask threadTask = threadQueue.remove();
		threadTask.internalTask.stop();
		this.activeCount--;
	}

	/**
	 * �ύ���񷽷�
	 */
	@Override
	public void execute(Runnable runnable) {
		if(this.isShutDown)
			throw new IllegalStateException("The thread pool is destory");
		this.runnableQueue.offer(runnable);
		
	}

	/**
	 * �Զ������̳߳أ��������ݣ����ղ�����
	 */
	@Override
	public void run() {
		while(!isShutDown && !isInterrupted()) {
			synchronized(this){
				if(isShutDown)
					break;
				if(runnableQueue.size() > 0 && activeCount < coreSize) {
					for(int i=activeCount; i<coreSize; i++) {
						newThread();
					}
					continue;
				}
				if(runnableQueue.size() > 0 && activeCount < maxSize) {
					for(int i=activeCount; i<maxSize; i++) {
						newThread();
					}
				}
				if(runnableQueue.size() == 0 && activeCount > coreSize) {
					for(int i=coreSize; i<activeCount; i++) {
						removeThread();
					}
				}				
			}
		}
	}

	/**
	 * �����̳߳�
	 */
	@Override
	public void shutDown() {
		synchronized(this) {
			if(isShutDown) return;
			isShutDown = true;
			threadQueue.forEach(threadTask ->{
				threadTask.internalTask.stop();
				threadTask.thread.interrupt();
			});
			this.interrupt();
		}		
	}

	/**
	 * δʵ�ֵķ���
	 */
	@Override
	public int getInitSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCoreSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getQueueSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getActiveSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isShutDown() {
		// TODO Auto-generated method stub
		return false;
	}
	
}

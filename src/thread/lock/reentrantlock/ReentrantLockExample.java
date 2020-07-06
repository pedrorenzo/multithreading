package thread.lock.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Reetrant Lock is the same as the keyword synchronized applied to an object,
 * however, we need to force lock and unlock explicitly.
 * A disadvantage is that we can forget or not reach the point in the code that 
 * we unlock (if, for example, an exception occurs before reaching the unlock call).
 * When we create a ReetrantLock, we can pass the constructor the flag true 
 * and so it will decide which thread will use the resource with fairness, however,
 * this can lower the throughput of the application.
 * 
 * ReetrantLock.lockInterruptibly() can be used to detect deadlocks and
 * recover. This basically takes threads that are sleeping and forces them to
 * stop.
 * 
 * ReetrantLock.tryLock() returns true and obtains lock if available, if not,
 * returns false.
 * 
 * If we use the lock() method and thread A is blocking this piece of code,
 * thread B is waiting for the unlock. If I use the tryLock() method, I can't
 * access that code snippet too, however, I can keep running the code even in
 * this scenario.
 * 
 * In this example we can see two concurrent threads and even if the lock is active, 
 * they are able to continue with the application execution without blocking it.
 * 
 * @author pedrorenzo
 */
public class ReentrantLockExample {
	
	public static void main(String[] args) {
		final CriticalOperation criticalOperation = new CriticalOperation();
		final Thread thread1 = new Thread(criticalOperation);
		final Thread thread2 = new Thread(criticalOperation);
		
		thread1.start();
		thread2.start();
	}

	public static class CriticalOperation implements Runnable {
	    private ReentrantLock lock = new ReentrantLock();
	 
	    @Override
	    public void run() {
	    	for (int i = 0; i < 100; i++) {
	    		if (lock.tryLock()) {
		        	try {
		        		System.out.println(Thread.currentThread().getName() + " lock.");
					} finally {
						lock.unlock();
					}
		        } else {
		        	System.out.println(Thread.currentThread().getName() + " could not lock.");
		        }
			}
	    }
	}
	
}

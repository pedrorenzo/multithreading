package thread.communication;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * There are some ways to communicate between threads:
 * 
 * - Using Thread.interrupt() one thread warns another that it wants it to be stopped.
 * - If in Thread A we call the threadB.join () method, it makes Thread A wait until Thread B
 * finishes and also asks Thread B to wake up when finished
 * 
 * What we are going to see in this example is called Condition Variables. We
 * use a thread to check if login and password are filled and we take actions
 * (Thread A) and a thread (Thread B) that represents our UI. Basically,
 * Thread A checks if login and password are filled in and if not, it uses our
 * condition variable, calling the await() method, which makes this thread
 * sleep and release the lock, so that our Thread B can then perform its task,
 * that is, get the data entered and after having this, call the signal()
 * method, which wakes up our Thread A, following the login logic flow.
 * 
 * Condition variables are good because we don't need to do as in Semaphores and
 * we can wake up all threads by calling the signalAll() method.
 * 
 * @author pedrorenzo
 */
public class ConditionVariablesExample {

	private static ReentrantLock lock = new ReentrantLock();
	private static String username = null, password = null;
	private static Condition condition = lock.newCondition();

	public static void main(String[] args) {
		final Thread loginLogicThread = new Thread(new LoginLogic());
		final Thread loginUIThread = new Thread(new LoginUI());

		loginLogicThread.start();
		loginUIThread.start();
	}

	public static class LoginLogic implements Runnable {

		@Override
		public void run() {
			lock.lock();
			try {
				while (username == null || password == null) {
					System.out.println("Login logic will sleep...zzzZzzZzz");
					condition.await();
				}
			} catch (final InterruptedException e) {
				e.printStackTrace();
			} finally {
				System.out.println("Releasing logic lock");
				lock.unlock();
			}
			System.out.println("Continuing with login!");
		}
	}

	public static class LoginUI implements Runnable {

		@Override
		public void run() {
			lock.lock();
			try {
				System.out.println("Getting user data");
				Thread.sleep(10000);
				username = "username";
				password = "password";
				condition.signal();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			} finally {
				System.out.println("Releasing UI lock");
				lock.unlock();
			}
		}
	}

}

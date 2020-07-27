package thread.communication;

/**
 * We have some methods by default in the Object class and among them we have
 * wait(), notify() and notifyAll(). With that, we can use any object as a
 * condition variable and as a lock (through the synchronized keyword).
 * We can use conditions variables or use objects as if they were conditions
 * variables. The good thing about using condition variables is that we have more
 * flexibility, such as waiting until X moment to release a lock.
 * 
 * The wait() method causes the current thread to wait until another thread
 * wakes it up. This thread can be woken up if another thread containing the
 * object calls notify() or notifyAll().
 * 
 * In this simple example, we have the main thread calling a second thread,
 * which simulates login logic. The main thread waits to be notified by the
 * second thread and then follows the application flow.
 * 
 * @author pedrorenzo
 */
public class ObjectsAsConditionVariablesExample {

	public static void main(String[] args) {
		final LoginLogic loginLogic = new LoginLogic();
		loginLogic.start();

		synchronized (loginLogic) {
			try {
				System.out.println("Waiting for login logic to complete...");
				loginLogic.wait();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Time waiting for login logic was: " + loginLogic.total);
		}
	}
}

class LoginLogic extends Thread {
	int total;

	@Override
	public void run() {
		synchronized (this) {
			for (int i = 0; i < 100; i++) {
				total += i;
			}
			notify();
		}
	}
}

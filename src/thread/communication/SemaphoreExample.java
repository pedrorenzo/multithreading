package thread.communication;

import java.util.concurrent.Semaphore;

/**
 * Semaphore can be used to restrict the number of users for a particular
 * resource. Unlike the lock that allows only one user per resource, Semaphore
 * can restrict any number of users for a resource. When I start a Semaphore I
 * can pass the number of permissions as a parameter.
 * semaphore.acquire (); -> Obtain the permit. It can have the parameter number of permits. 
 * If in case it tries to obtain and no permit is available, the thread sleeps until one is
 * released.
 * semaphore.release (); -> Release the permit. It can have the parameter number of permits.
 * 
 * Using Semaphore is not very good in many cases and we must take care as we
 * can have problems of confusion between the threads when obtaining permits and
 * even when releasing those permits.
 * 
 * In this example we simulate a login where only 2 users can be logged in at
 * that moment.
 * 
 * @author pedro
 */
public class SemaphoreExample {

	public static void main(String[] args) {
		LoginQueueUsingSemaphore loginQueueUsingSemaphore = new LoginQueueUsingSemaphore(2);
		System.out.println("Could user 1 login: " + loginQueueUsingSemaphore.tryLogin());
		System.out.println("Could user 2 login: " + loginQueueUsingSemaphore.tryLogin());
		System.out.println("Available slots: " + loginQueueUsingSemaphore.availableSlots());
		System.out.println("Could user 3 login: " + loginQueueUsingSemaphore.tryLogin());

		System.out.println("One user did a logout.");
		loginQueueUsingSemaphore.logout();
		
		System.out.println("Available slots: " + loginQueueUsingSemaphore.availableSlots());
		System.out.println("Could user 3 login: " + loginQueueUsingSemaphore.tryLogin());
	}

	private static class LoginQueueUsingSemaphore {
		private Semaphore semaphore;

		public LoginQueueUsingSemaphore(final int slotLimit) {
			semaphore = new Semaphore(slotLimit);
		}

		boolean tryLogin() {
			return semaphore.tryAcquire();
		}

		void logout() {
			semaphore.release();
		}

		int availableSlots() {
			return semaphore.availablePermits();
		}

	}
}

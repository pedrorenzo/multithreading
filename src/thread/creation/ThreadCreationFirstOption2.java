package thread.creation;

/**
 * Example to how create a thread implementing Runnable interface.
 * 
 * @author pedrorenzo
 */
public class ThreadCreationFirstOption2 {
	public static void main(String[] args) throws InterruptedException {
		// Creating a new thread.
		final Thread thread = new Thread(() -> {
			System.out.println("We are now in thread: " + Thread.currentThread().getName());
			System.out.println("Current thread priority is: " + Thread.currentThread().getPriority());
		});

		thread.setName("New Worker Thread");
		// I want to say to the OS that this thread has maximum priority.
		thread.setPriority(Thread.MAX_PRIORITY);

		System.out.println("We are in thread: " + Thread.currentThread().getName() + " before starting a new thread");
		// Starting the created thread.
		thread.start();
		System.out.println("We are in thread: " + Thread.currentThread().getName() + " after starting a new thread");

		// Put the current thread to sleep. This is not a loop and does not consumes
		// CPU, it just says to the OS to not schedule the current thread.
		Thread.sleep(10000);
	}
}

package thread.creation;

/**
 * Example to how handle Uncaught Exception in the thread.
 * 
 * @author pedrorenzo
 */
public class ThreadUncaughtExceptionHandler {
	public static void main(String[] args) throws InterruptedException {
		final Thread thread = new Thread(() -> {
			throw new RuntimeException("Intentional Exception");
		});

		thread.setName("Misbehaving thread");

		// If an exception occurs and it is not caught anywhere, I handle it here the
		// way I want, otherwise it can bring down the entire thread.
		thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println(
						"A critical error happened in thread " + t.getName() + " the error is " + e.getMessage());
			}
		});

		thread.start();
	}
}

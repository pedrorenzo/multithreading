package thread.interrupt;

/**
 * Example to how interrupt a thread.
 * 
 * @author pedrorenzo
 */
public class ThreadInterruptionFirstOption {
	public static void main(String[] args) {
		final Thread thread = new Thread(new BlockingTask());
		thread.start();
		thread.interrupt();
	}

	private static class BlockingTask implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(500000);
			} catch (InterruptedException e) {
				// This exception will be thrown when this thread is interrupted externally.
				System.out.println("InterruptedException catch!");
			}
		}
	}
}

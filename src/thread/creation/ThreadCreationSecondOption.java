package thread.creation;

/**
 * Example to how create a thread extending Thread class.
 * 
 * @author pedrorenzo
 */
public class ThreadCreationSecondOption {
	public static void main(String[] args) throws InterruptedException {
		final Thread thread = new NewThread();
		thread.start();
	}

	private static class NewThread extends Thread {
		@Override
		public void run() {
			System.out.println("Hello from " + this.getName());
		}
	}
}

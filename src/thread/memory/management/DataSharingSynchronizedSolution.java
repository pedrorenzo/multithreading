package thread.memory.management;

/**
 * The synchronized keyword is used to make only one thread able to access a
 * block of code. This keyword can be used in method signature or we can create
 * a block within methods.
 * It is important to say that in the first option, if
 * we use this keyword in the signature of 2 methods of the same class, even if
 * thread A is using method A, method B will also be locked by this thread.
 * In the second option, if we make a block with synchronized (this) {}, in 2
 * different methods this will have the same effect of using the keyword in the
 * method signature. Here is the importance of creating a separate object for
 * each method that I want to lock, removing this unwanted behavior, and then
 * becoming synchronized (lock1) {} and synchronized (lock2) {}. In our case, 
 * we use the same lock, because we want to protect the 
 * read/decrement/increment access of the same variable.
 * 
 * @author pedrorenzo
 */
public class DataSharingSynchronizedSolution {
	public static void main(String[] args) throws InterruptedException {
		final InventoryCounter inventoryCounter = new InventoryCounter();
		final IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
		final DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

		incrementingThread.start();
		decrementingThread.start();

		incrementingThread.join();
		decrementingThread.join();

		System.out.println("We currently have " + inventoryCounter.getItems() + " items");
	}

	public static class DecrementingThread extends Thread {

		private InventoryCounter inventoryCounter;

		public DecrementingThread(final InventoryCounter inventoryCounter) {
			this.inventoryCounter = inventoryCounter;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10000; i++) {
				inventoryCounter.decrement();
			}
		}
	}

	public static class IncrementingThread extends Thread {

		private InventoryCounter inventoryCounter;

		public IncrementingThread(final InventoryCounter inventoryCounter) {
			this.inventoryCounter = inventoryCounter;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10000; i++) {
				inventoryCounter.increment();
			}
		}
	}

	private static class InventoryCounter {
		private int items = 0;

		final Object lock = new Object();

		public void increment() {
			synchronized (this.lock) {
				items++;
			}
		}

		public void decrement() {
			synchronized (this.lock) {
				items--;
			}
		}

		public int getItems() {
			synchronized (this.lock) {
				return items;
			}
		}
	}
}

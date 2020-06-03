package thread.memory.management;

/**
 * In this example, we can see what can happen if we incorrectly manage data
 * sharing between threads. Running the application N times, we will obtain N
 * different results. This happens because item-- and item++ are not atomic
 * operations and we are not calling the join() method before calling the
 * start() method from the decrementingThread.
 * 
 * @author pedrorenzo
 */
public class DataSharingProblemExample {
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

		public void increment() {
			items++;
		}

		public void decrement() {
			items--;
		}

		public int getItems() {
			return items;
		}
	}
}

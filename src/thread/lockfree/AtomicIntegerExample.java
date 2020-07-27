package thread.lockfree;

import java.util.concurrent.atomic.AtomicInteger;

import thread.memory.management.DataSharingProblemExample;

/**
 * Locks are very important and interesting to use, however, they have some
 * disadvantages:
 * - Deadlocks are difficult to recover and the more locks you
 * have in the application, the more chances of deadlocks happening;
 * - We can develop our application with a worse performance.
 * 
 * There are some ways to use lock free solutions, which guarantee only one hardware
 * operation, being atomic and thread safe. We have already seen some solutions
 * of this type:
 * - Read/Assignment on primitive types (except long and double);
 * - Read/Assignment in references;
 * - Read/Assignment in volatile long and volatile double.
 * 
 * Java gives us atomic classes, which are in the java.util.concurrent.atomic
 * package. The advantages of using these objects are simplicity, we don't need
 * to use locks or synchronization and it sometimes performs better than
 * the "manual" implementation of lock. The disadvantages are that there is
 * still a race condition between 2 separate atomic operations and it
 * performs worse than using a common integer, for example.
 * 
 * In this example, we will show the {@link DataSharingProblemExample} solution using AtomicInteger.
 * 
 * @author pedrorenzo
 */
public class AtomicIntegerExample {
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
		private AtomicInteger items = new AtomicInteger(0);

		public void increment() {
			items.incrementAndGet();
		}

		public void decrement() {
			items.decrementAndGet();
		}

		public int getItems() {
			return items.get();
		}
	}
}

package thread.memory.management;

/**
 * Race Condition is when many threads are accessing a shared resource and at
 * least one thread is modifying the resource which can cause incorrect data.
 * In the example of this code, it seems impossible for y to be greater than x,
 * even if running with multiple threads, since we always increment x first than
 * we increment y, however, this can happen because of something called Data
 * Race. Data Race occurs because the compiler and the CPU can execute
 * instructions out of order to optimize performance. Attention: Running out of
 * order does not interfere with the functionality of our software. If for
 * example we are going to execute a function that needs the value of x to
 * define the value of y, always the value of x will be calculated first.
 * In our example, running x++ or y++ first does not matter. Is also important to
 * remember that this would not happen if we were running this with just one
 * thread...the problem here is that one thread increases the value while the
 * other makes the comparison. To solve these problems, we can use the keyword
 * synchronized or the keyword volatile.
 * 
 * @author pedrorenzo
 */
public class RaceConditionProblemAndSolutionExample {

	public static void main(String[] args) {
		final SharedClass sharedClass = new SharedClass();
		final Thread thread1 = new Thread(() -> {
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				sharedClass.increment();
			}
		});

		final Thread thread2 = new Thread(() -> {
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				sharedClass.checkForDataRace();
			}

		});

		thread1.start();
		thread2.start();
	}

	public static class SharedClass {
		// Declaring x and y with the volatile keyword would solve our problem.
		private int x = 0;
		private int y = 0;

		public void increment() {
			x++;
			y++;
		}

		public void checkForDataRace() {
			if (y > x) {
				System.out.println("y > x - Data Race is detected.");
			}
		}
	}
}

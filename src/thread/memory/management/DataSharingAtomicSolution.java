package thread.memory.management;

import java.util.Random;

/**
 * Using the keyword synchronized is not very interesting as it is a blocking
 * operation. It would be more interesting if several threads worked together.
 * 
 * Examples of atomic operations: 
 * -We can set and obtain references to objects in an atomic way.
 * - All assignments for primitive types (except long and
 * double as they are 64 bits).
 * 
 * Using the keyword volatile, we can "transform" long and double types into atomic types.
 * 
 * With this example, our businessLogic takes a random time from 0 to 10 ms to
 * run. With that, we expect an average of 5 ms to be displayed and so we were
 * able to show, since getAverage() is not synchronized, that the threads are
 * able to work in parallel and in a correct way only with atomicity.
 * 
 * @author pedrorenzo
 */
public class DataSharingAtomicSolution {
	public static void main(String[] args) {
		final Metrics metrics = new Metrics();

		final BusinessLogic businessLogicThread1 = new BusinessLogic(metrics);
		final BusinessLogic businessLogicThread2 = new BusinessLogic(metrics);

		final MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);

		businessLogicThread1.start();
		businessLogicThread2.start();
		metricsPrinter.start();
	}

	public static class MetricsPrinter extends Thread {
		private Metrics metrics;

		public MetricsPrinter(final Metrics metrics) {
			this.metrics = metrics;
		}

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("InterruptedException" + e);
				}

				final double currentAverage = metrics.getAverage();

				System.out.println("Current Average is " + currentAverage);
			}
		}
	}

	public static class BusinessLogic extends Thread {
		private Metrics metrics;
		private Random random = new Random();

		public BusinessLogic(final Metrics metrics) {
			this.metrics = metrics;
		}

		@Override
		public void run() {
			while (true) {
				final long start = System.currentTimeMillis();

				try {
					Thread.sleep(random.nextInt(10));
				} catch (InterruptedException e) {
					System.out.println("InterruptedException" + e);
				}

				final long end = System.currentTimeMillis();

				metrics.addSample(end - start);
			}
		}
	}

	public static class Metrics {
		private long count = 0;
		private volatile double average = 0.0;

		public synchronized void addSample(final long sample) {
			double currentSum = average * count;
			count++;
			average = (currentSum + sample) / count;
		}

		// Since this is a get method, we would not need the keyword synchronized here,
		// for example, if it were a primitive type other than long or double or a
		// reference. But...since it's a double value, we need to protect it, so we
		// put the volatile keyword in the variable declaration.
		public double getAverage() {
			return average;
		}
	}
}

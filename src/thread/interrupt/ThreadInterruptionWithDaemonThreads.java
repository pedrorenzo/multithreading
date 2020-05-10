package thread.interrupt;

import java.math.BigInteger;

/**
 * Example of Daemon Threads. These are threads that run in the background and
 * do not block our application from finishing
 * 
 * @author pedrorenzo
 */
public class ThreadInterruptionWithDaemonThreads {

	public static void main(String[] args) throws InterruptedException {
		final Thread thread = new Thread(new LongComputationTask(new BigInteger("200000"), new BigInteger("100000000")));

		// This is how we set a thread as daemon.
		thread.setDaemon(true);
		thread.start();
		Thread.sleep(100);
		thread.interrupt();
	}

	private static class LongComputationTask implements Runnable {
		private BigInteger base;
		private BigInteger power;

		public LongComputationTask(final BigInteger base, final BigInteger power) {
			this.base = base;
			this.power = power;
		}

		@Override
		public void run() {
			System.out.println(base + "^" + power + " = " + pow(base, power));
		}

		private BigInteger pow(BigInteger base, BigInteger power) {
			BigInteger result = BigInteger.ONE;

			for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
				result = result.multiply(base);
			}

			return result;
		}
	}
}

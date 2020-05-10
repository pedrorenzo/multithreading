package thread.interrupt;

import java.math.BigInteger;

/**
 * Example of how to validate the execution of a thread with
 * Thread.currentThread().IsInterrupted() and then interrupt it.
 * 
 * @author pedrorenzo
 */
public class ThreadInterruptionSecondOption {

	public static void main(String[] args) {
		final Thread thread = new Thread(
				new LongComputationTask(new BigInteger("200000"), new BigInteger("100000000")));

		thread.start();
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

		private BigInteger pow(final BigInteger base, final BigInteger power) {
			BigInteger result = BigInteger.ONE;

			for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
				// As we do not have a handle for InterruptedException and this code snippet can
				// take a long time to finish its execution, when calling the
				// Thread.interrupt() method the thread continues to run.
				// Because of that, we have to validate whether the thread is running or not
				// every iteration and so, if not, we just stop our for loop.
				if (Thread.currentThread().isInterrupted()) {
					System.out.println("Prematurely interrupted computation");
					return BigInteger.ZERO;
				}
				result = result.multiply(base);
			}

			return result;
		}
	}
}

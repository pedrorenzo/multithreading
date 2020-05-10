package thread.coordination;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example to how coordinate threads using the join method.
 * 
 * @author pedrorenzo
 */
public class ThreadCoordination {
	public static void main(String[] args) {
		final List<Long> inputNumbers = Arrays.asList(100000000L, 3435L, 35435L, 2324L, 4656L, 23L, 5556L);

		final List<FactorialThread> threads = new ArrayList<>();

		for (long inputNumber : inputNumbers) {
			threads.add(new FactorialThread(inputNumber));
		}

		for (Thread thread : threads) {
			thread.start();
		}

		// The join method ensures that the main thread will only end after all other
		// threads are finished, thus ensuring that the factorial of all values ​​will
		// be displayed. In this case, I place a maximum waiting limit of 2 seconds.
		for (Thread thread : threads) {
			try {
				thread.join(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		FactorialThread factorialThread;
		for (int i = 0; i < inputNumbers.size(); i++) {
			factorialThread = threads.get(i);
			if (factorialThread.isFinished()) {
				System.out.println("Factorial of " + inputNumbers.get(i) + " is " + factorialThread.getResult());
			} else {
				System.out.println("The calculation for " + inputNumbers.get(i) + " is still in progress");
			}
		}
	}

	public static class FactorialThread extends Thread {
		private long inputNumber;
		private BigInteger result = BigInteger.ZERO;
		private boolean isFinished = false;

		public FactorialThread(final long inputNumber) {
			this.inputNumber = inputNumber;
		}

		@Override
		public void run() {
			this.result = factorial(inputNumber);
			this.isFinished = true;
		}

		public BigInteger factorial(final long n) {
			BigInteger tempResult = BigInteger.ONE;

			for (long i = n; i > 0; i--) {
				tempResult = tempResult.multiply(new BigInteger((Long.toString(i))));
			}
			return tempResult;
		}

		public BigInteger getResult() {
			return result;
		}

		public boolean isFinished() {
			return isFinished;
		}
	}
}

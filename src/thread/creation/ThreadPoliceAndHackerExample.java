package thread.creation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class exemplifies the execution of 3 simultaneous threads. 
 * If any of the 2 hackers find out the value of my vault, they will take my money.
 * They have 10 seconds to find this out, otherwise the policeman arrives and arrests
 * them.
 * We can see that as the password is generated randomly and one hacker
 * attempts in ascending order and the other in decreasing order, the result
 * will vary.
 * 
 * @author pedrorenzo
 */
public class ThreadPoliceAndHackerExample {
	
	public static final int MAX_PASSWORD = 9999;

	public static void main(String[] args) {
		final Random random = new Random();

		// Create the vault with the random password.
		final Vault vault = new Vault(random.nextInt(MAX_PASSWORD));

		// Create a list of threads and then start them.
		final List<Thread> threads = new ArrayList<>();
		threads.add(new AscendingHackerThread(vault));
		threads.add(new DescendingHackerThread(vault));
		threads.add(new PoliceThread());

		for (Thread thread : threads) {
			thread.start();
		}
	}

	private static class Vault {
		
		private int password;

		public Vault(final int password) {
			this.password = password;
		}

		// Verify if the password is correct.
		public boolean isCorrectPassword(final int guess) {
			try {
				// This is just to delay the hackers :D.
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return this.password == guess;
		}
	}

	private static abstract class HackerThread extends Thread {
		
		protected Vault vault;

		public HackerThread(final Vault vault) {
			this.vault = vault;
			this.setName(this.getClass().getSimpleName());
			this.setPriority(Thread.MAX_PRIORITY);
		}

		@Override
		public void start() {
			System.out.println("Starting thread " + this.getName());
			super.start();
		}
	}

	private static class AscendingHackerThread extends HackerThread {

		public AscendingHackerThread(final Vault vault) {
			super(vault);
		}

		@Override
		public void run() {
			for (int guess = 0; guess < MAX_PASSWORD; guess++) {
				if (vault.isCorrectPassword(guess)) {
					System.out.println(this.getName() + " guessed the password " + guess);
					System.exit(0);
				}
			}
		}
	}

	private static class DescendingHackerThread extends HackerThread {

		public DescendingHackerThread(final Vault vault) {
			super(vault);
		}

		@Override
		public void run() {
			for (int guess = MAX_PASSWORD; guess >= 0; guess--) {
				if (vault.isCorrectPassword(guess)) {
					System.out.println(this.getName() + " guessed the password " + guess);
					System.exit(0);
				}
			}
		}
	}

	private static class PoliceThread extends Thread {
		@Override
		public void run() {
			for (int i = 10; i > 0; i--) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("HURRY UP! Policeman will arrive in: " + i);
			}

			System.out.println("Game over for you hackers");
			System.exit(0);
		}
	}
}

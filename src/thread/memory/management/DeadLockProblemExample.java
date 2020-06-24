package thread.memory.management;

import java.util.Random;

/**
 * We have two lock options:
 * - Coarse-Grained Locking: It is just a lock for all resources and therefore
 * it is easy to control, the problem is that only one thread can execute 
 * one action at a time. We can do this using the keyword synchronized 
 * in our methods. 
 * - Fine-Grained Locking: It is a lock for each of the resources and that 
 * is why the threads are able to execute more things in parallel, the problem 
 * is the difficulty to control this and may even cause a deadlock.
 * 
 * An example of a deadlock is when a thread A, locking resource Y needs
 * resource X and a thread B, locking resource X needs resource Y.
 * 
 * Deadlock conditions:
 * - Mutual exclusion: Only one thread can have exclusive access to a resource.
 * - Hold and wait: At least one thread is holding a resource and waiting for another.
 * - Non-preemptive allocation: A resource is released only after the thread finishes using it.
 * - Circular wait: Thread A, locking resource Y needs resource X and a thread B, locking 
 * resource X needs resource Y.
 * 
 * The idea of ​​this example is to simulate that two trains want to lock a path
 * in order to pass.
 * We could solve it if we remove the circular wait, just by changing the lock order 
 * to be the same in both takeRoadA and takeRoadB methods, 
 * e.g. synchronized (roadA) and then synchronized (roadB) in both methods.
 * 
 * @author pedrorenzo
 *
 */
public class DeadLockProblemExample {

	public static void main(String[] args) {
		final Intersection intersection = new Intersection();
		final Thread trainAThread = new Thread(new TrainA(intersection));
		final Thread trainBThread = new Thread(new TrainB(intersection));

		trainAThread.start();
		trainBThread.start();
	}

	public static class TrainB implements Runnable {
		private Intersection intersection;
		private Random random = new Random();

		public TrainB(final Intersection intersection) {
			this.intersection = intersection;
		}

		@Override
		public void run() {
			while (true) {
				final long sleepingTime = random.nextInt(5);
				try {
					Thread.sleep(sleepingTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				intersection.takeRoadB();
			}
		}
	}

	public static class TrainA implements Runnable {
		private Intersection intersection;
		private Random random = new Random();

		public TrainA(final Intersection intersection) {
			this.intersection = intersection;
		}

		@Override
		public void run() {
			while (true) {
				final long sleepingTime = random.nextInt(5);
				try {
					Thread.sleep(sleepingTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				intersection.takeRoadA();
			}
		}
	}

	public static class Intersection {
		private Object roadA = new Object();
		private Object roadB = new Object();

		public void takeRoadA() {
			synchronized (roadA) {
				System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

				synchronized (roadB) {
					System.out.println("Train is passing through road A");
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		public void takeRoadB() {
			synchronized (roadB) {
				System.out.println("Road B is locked by thread " + Thread.currentThread().getName());

				synchronized (roadA) {
					System.out.println("Train is passing through road B");

					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

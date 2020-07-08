package thread.lock.reentrantlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * In all examples, we used a single lock, which does not care if we are doing a
 * read or write operation on our resources. This can be bad in terms
 * of performance and we can improve this by letting thread that wants to read
 * our resources access it simultaneously, while we have a lock for those who
 * want to write to our resources.
 * ReentrantReadWriteLock does what we said:
 * - Using the ReentrantReadWriteLock.getReadLock().lock() method, it allows
 * more than one thread to read this resource and even has a counter of how many
 * threads are reading.
 * - Using the ReentrantReadWriteLock.getWriteLock().lock() method, it allows
 * only one thread to write to this resource.
 * - If a thread obtains the writeLock, none can obtain the readLock.
 * - If at least one thread obtains the readLock, no thread can obtain the writeLock.
 * 
 * We also have the tryLock() method, just like the ReentrantLock object has.
 * 
 * In this example we simply add and read values ​​from a list protected by read
 * lock and write lock.
 * 
 * @author pedrorenzo
 */
public class ReentrantReadWriteLockExample<E> {

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock readLock = readWriteLock.readLock();
	private final Lock writeLock = readWriteLock.writeLock();
	private final List<E> list = new ArrayList<>();

	public void set(final E o) {
		writeLock.lock();
		try {
			list.add(o);
			System.out.println("Adding element by thread " + Thread.currentThread().getName());
		} finally {
			writeLock.unlock();
		}
	}

	public E get(final int i) {
		readLock.lock();
		try {
			System.out.println("Printing elements by thread " + Thread.currentThread().getName());
			return list.get(i);
		} finally {
			readLock.unlock();
		}
	}

	public static void main(String[] args) {
		final ReentrantReadWriteLockExample<String> threadSafeArrayList = new ReentrantReadWriteLockExample<>();
		threadSafeArrayList.set("1");
		threadSafeArrayList.set("2");
		threadSafeArrayList.set("3");

		System.out.println("Printing the First Element: " + threadSafeArrayList.get(1));
	}

}

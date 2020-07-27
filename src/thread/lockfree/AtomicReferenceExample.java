package thread.lockfree;

import java.util.concurrent.atomic.AtomicReference;

/**
 * With the AtomicReference <T> class, we can create an object with an atomic
 * reference. This class and all the other Atomics, have a very interesting
 * method called compareAndSet (expected, new), where we only set the new value,
 * if what we expected to be there really is ... otherwise, it means that some
 * other thread has already changed the value.
 * 
 * In this example, we will first see the functionality if the expected value
 * has not been changed and then the functionality if the value has been changed
 * by another thread when we try to update it.
 * 
 * @author pedrorenzo
 */
public class AtomicReferenceExample {
	public static void main(String[] args) {
		final AtomicReference<String> name = new AtomicReference<String>("Pedro");

		if (name.compareAndSet("Pedro", "Pedro Vicentin")) {
			System.out.println("Name updated: " + name.get());
		} else {
			System.out.println("Name could not be updated!");
		}

		// Let's assume that another thread has updated this name.
		name.set("Pedro Renzo Vicentin");

		if (name.compareAndSet("Pedro Vicentin", "Joao da Silva")) {
			System.out.println("Name updated: " + name.get());
		} else {
			System.out.println("Name could not be updated!");
		}

	}
}

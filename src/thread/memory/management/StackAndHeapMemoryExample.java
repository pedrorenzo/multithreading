package thread.memory.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * STACK:
 * Debugging this simple example we can understand how the stack memory works.
 * The following will occur:
 * 1) A frame will be created inside the stack memory for the main method, 
 * which will have the variables args, x = 1 and y = 2.
 * 2) A frame will be created for the sum method, which will have the
 * variables a = 1, b = 2 and s = 3.
 * 3) When the sum method is finished, the frame will be removed 
 * (along with its variables) from the stack memory.
 * 4) The result = 3 variable will be inserted into the stack within the main method frame.
 * 5) After the main method is finished, its frame and variables will be removed
 * from the stack.
 * Keeps local primitive types and local references.
 * All variables belongs to the thread executing on that stack.
 * If calling hierarchy is too deep, we may get a StackOverflowException.
 * 
 * HEAP:
 * Is a shared memory region that belong to the proccess, all the threads share the content.
 * Keeps anything created with new operator, members of classes and static variables.
 * While and object has any reference, it stays at the heap, otherwise, the garbage collector
 * remove it.
 * 
 * idToNameMap and numberOfInstances are on the heap memory.
 * x, y, result, s, args, a and b are on the stack memory;
 * The reference allNames is a local variable and is allocated on the stack. The reference refers to an object
 * of type ArrayList<String>, and that object is allocated on the heap.
 *  
 * @author pedrorenzo
 */
public class StackAndHeapMemoryExample {

	private Map<Integer, String>  idToNameMap;
	private static long numberOfInstances = 0;

	
	public static void main(String[] args) {
		final int x = 1;
		final int y = 2;
		final int result = sum(x, y);
		System.out.println("Result is:" + result);
	}

	private static int sum(final int a, final int b) {
		final List<String> allNames = new ArrayList<>();
		final int s = a + b;
		return s;
	}

}
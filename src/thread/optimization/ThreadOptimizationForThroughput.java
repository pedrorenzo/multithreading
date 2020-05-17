package thread.optimization;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Throughput is defined as the quantity of data being sent/received by unit of
 * time.
 * 
 * In this class we simulate several HTTP requests, where the idea is to search
 * for words in a VERY large book and as the answer, we will have a counter with
 * the number of times that word appears in the book.
 * 
 * It is interesting to use the Throughput strategy when we want to perform as
 * many tasks as possible as quickly as possible. Thread pooling is nothing more
 * than creating the threads once and reusing them for future tasks. The tasks
 * are distributed among the threads through a queue.
 * 
 * You can test performance and get throughput by running the test file via
 * JMeter via /resources. This test will read words from a CSV and then make a
 * request to our HTTP Server.
 * 
 * It is important to note that throughput will increase if we add more threads,
 * however, if we add more threads than the physical/virtual number of cores of
 * a CPU, we will not see an improvement. So it is interesting to keep that
 * number next from physical/virtual number of cores in a CPU.
 * 
 * @author pedrorenzo
 */
public class ThreadOptimizationForThroughput {
	private static final String INPUT_FILE = "./resources/war_and_peace.txt";

	// If you will change the numberOfThreads value, please look at the class
	// comment :)
	private static final int NUMBER_OF_THREADS = 8;

	private static final int PORT = 8000;
	// We set it to 0 because all the requests should end up in the thread pool's
	// queue.
	private static final int BACKLOG_SIZE = 0;
	private static final int BAD_REQUEST = 400;
	private static final int OK = 200;

	public static void main(String[] args) throws IOException {
		final String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
		startServer(text);
	}

	/**
	 * Starts the server.
	 * 
	 * @param text
	 * @throws IOException
	 */
	public static void startServer(final String text) throws IOException {
		final HttpServer server = HttpServer.create(new InetSocketAddress(PORT), BACKLOG_SIZE);
		server.createContext("/search", new WordCountHandler(text));
		Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		server.setExecutor(executor);
		server.start();
	}

	/**
	 * This class will handle the request and count the words.
	 * 
	 * @author pedrorenzo
	 */
	private static class WordCountHandler implements HttpHandler {
		private String text;

		public WordCountHandler(final String text) {
			this.text = text;
		}

		@Override
		public void handle(final HttpExchange httpExchange) throws IOException {
			final String query = httpExchange.getRequestURI().getQuery();
			final String[] keyValue = query.split("=");
			final String action = keyValue[0];
			final String word = keyValue[1];
			if (!action.equals("word")) {
				httpExchange.sendResponseHeaders(BAD_REQUEST, 0);
				return;
			}

			long count = countWord(word);

			final byte[] response = Long.toString(count).getBytes();
			httpExchange.sendResponseHeaders(OK, response.length);
			final OutputStream outputStream = httpExchange.getResponseBody();
			outputStream.write(response);
			outputStream.close();
		}

		/**
		 * Count the word.
		 * 
		 * @param word
		 * @return the number of times that word appears in the book.
		 */
		private long countWord(final String word) {
			long count = 0;
			int index = 0;
			while (index >= 0) {
				index = text.indexOf(word, index);

				if (index >= 0) {
					count++;
					index++;
				}
			}
			return count;
		}
	}
}

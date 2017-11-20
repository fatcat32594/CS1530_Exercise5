import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/*
 * Java Pi Approximator using threads
 */

public class Pi {

	public static void main(String[] args) {
		long numThreads = 0;
		long totalIterations = 0;
		AtomicLong insideNum = new AtomicLong(0);
		AtomicLong allNum = new AtomicLong(0);
		Thread[] threads;

		if (args.length != 2) {
			System.out.println("Please supply 2 long arguments");
			System.exit(1);
		}
		try {
			numThreads = Long.parseLong(args[0]);
			totalIterations = Long.parseLong(args[1]);
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}

		System.out.println("Threads: " + numThreads);
		System.out.println("Iterations: " + totalIterations);

		long iterationPerThread = totalIterations/numThreads;
		long leftoverIterations = totalIterations%numThreads;

		System.out.println("Per-thread Iterations: " + iterationPerThread);

		threads = new Thread[(int) numThreads];

		for (int i = 0; i < numThreads; i++) {
			threads[i] = new Thread(() -> {
				ThreadLocalRandom randomizer = ThreadLocalRandom.current();
				double x = 0;
				double y = 0;
				for (long j = 0; j < iterationPerThread; j++) {
					x = randomizer.nextDouble(1.0);
					y = randomizer.nextDouble(1.0);

					if (((x*x) + (y*y)) < 1) {
						insideNum.incrementAndGet();

					}
					allNum.incrementAndGet();
				}
			});

			threads[i].start();
		}

		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println(((double) insideNum.get())/allNum.get() * 4);
	}

}

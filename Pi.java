import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/*
 * Java Pi Approximator using threads
 * Steve Hardy -srh89
 */

public class Pi {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		long numThreads = 0;
		long totalIterations = 0;
		int insideNum = 0;
		int allNum = 0;
		PiApproximator[] threads;

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

		//System.out.println("Threads: " + numThreads);
		//System.out.println("Iterations: " + totalIterations);

		long iterationPerThread = totalIterations/numThreads;
		long leftoverIterations = totalIterations%numThreads;

		//System.out.println("Per-thread Iterations: " + iterationPerThread);

		threads = new PiApproximator[(int) numThreads];

		for (int i = 0; i < numThreads; i++) {
			threads[i] = new PiApproximator(iterationPerThread);
			threads[i].start();
		}

		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
				long[] results = threads[i].getValues();
				insideNum += results[0];
				allNum += results[1];
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		double secTime = (double) totalTime / 1000;
		double ratio = (double) insideNum/allNum;
		double pi = ratio * 4;

		String output = "Total = \t %d\n" +
						"Inside = \t %d\n" +
						"Ratio = \t %f\n" +
						"Pi = \t\t %f\n\n" +
						"Time: %f sec\n";
		System.out.format(output, allNum, insideNum, ratio, pi, secTime);

	}

}

class PiApproximator extends Thread {
	private long insideNum;
	private long allNum;
	private long iterationPerThread;
	Random randomizer;

	public PiApproximator(long iterationPerThread) {
		this.randomizer = new Random(System.currentTimeMillis());
		this.insideNum = 0;
		this.allNum = 0;
		this.iterationPerThread = iterationPerThread;
	}

	public void run() {
		double x = 0;
		double y = 0;
		for (long j = 0; j < iterationPerThread; j++) {
			x = randomizer.nextDouble();
			y = randomizer.nextDouble();

			if (((x*x) + (y*y)) < 1) {
				this.insideNum++;

			}
			this.allNum++;
		}
	}

	public long[] getValues() {
		long[] results = {insideNum, allNum};
		return results;
	}
}

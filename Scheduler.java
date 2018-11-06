import java.io.*;

public class Scheduler {

	private final int TIME_SLICE = 400;
	private int time = 1;

	/*
	 *  Our entry point
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) return; /* error */
		Scheduler s = new Scheduler(args[0]);
		s.start();
	}

	public Scheduler(String filename) throws Exception {
		queue = new HeapPriorityQueue<>();
		queueArrival = new HeapPriorityQueue<>();
		listQueue = new LinkedQueue<>();

		ProcessReader r = new ProcessReader(new File(filename));

		Process p = null;
		while (r.hasNext()) {
			p = r.next();
			queueArrival.insert(p.getArrivalTime(), p);
			listQueue.enqueue(p);
		}
	}


	private void update() {
		if (queueArrival.isEmpty()) return;
		while (time == queueArrival.min().getKey()) {
			Process x = queueArrival.min().getValue();
			queue.insert(x.getPriority(), x);
			queueArrival.removeMin();
			if (queueArrival.isEmpty()) break;
		}

	}

	public void start() throws InterruptedException {
		System.out.println("\n<BEGIN SIMULATION>\n");

		while (!(queue.isEmpty() && queueArrival.isEmpty())) {

			update();
			
			if (!queue.isEmpty())
			{
				Process p = queue.min().getValue();
				
				if (p.getLength() == 0) {
					queue.removeMin();
					continue;
				}

				p.run();
			}
						
			System.out.println("<Time=" + time++ + ">");

			Thread.sleep(TIME_SLICE);
		}


		System.out.format("\n<END SIMULATION>\n\n%s\t  %s\t  %s\t  %s", 
				"<ID>", "<LENGTH>", "<PRIORITY>", "<ARRIVAL>\n");

		Process p = null;

		while(!listQueue.isEmpty())
		{
			p = listQueue.dequeue();
			System.out.format("%s\t\t%d\t\t%d\t\t%d\n", p.getId(),
					p.getInitialLength(), 
					p.getPriority(), 
					p.getArrivalTime());
		}

	}

	private HeapPriorityQueue<Integer, Process> queue;
	private HeapPriorityQueue<Integer, Process> queueArrival;
	private LinkedQueue<Process> listQueue;

	private class Process {
		public Process(String id, int length,
				int priority, int arrivalTime) {
			this.id = id;
			this.length = length;
			this.initialLength = length;
			this.priority = priority;
			this.arrivalTime = arrivalTime;
		}

		void run() {
			System.out.print("Now running " + id + "  ");
			length--;
		}

		public String getId() {
			return id;
		}

		public int getLength() {
			return length;
		}

		public int getPriority() {
			return priority;
		}

		public int getArrivalTime() {
			return arrivalTime;
		}

		public int getInitialLength() {
			return initialLength;
		}

		private String id;
		private int length;
		private int priority;
		private int arrivalTime;
		private int initialLength;
	}

	private class ProcessReader {
		public ProcessReader(File f) throws FileNotFoundException {
			fr = new FileReader(f);
			sc = new java.util.Scanner(fr);
			sc.nextLine(); /* read header */
		}

		public Process next() throws Exception {
			String tmp = sc.nextLine();
			String[] data = tmp.split("\\t");

			if (data.length != 4) 	
				throw new Exception("Data length " + data.length);

			return new Process(data[0], 
					Integer.parseInt((data[1])),
					Integer.parseInt((data[2])), 
					Integer.parseInt((data[3])));

		}

		public boolean hasNext(){
			return sc.hasNextLine();
		}

		private FileReader fr;
		private java.util.Scanner sc;
	}
}

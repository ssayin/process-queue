import java.io.*;
public class Scheduler
{
	class Process
	{
		public Process(String id, int length,
				int priority, int arrivalTime)
		{
			this.id = id;
			this.length = length;
			this.priority = priority;
			this.arrivalTime = arrivalTime;
		}

		void run()
		{
			System.out.println("Now running " + id);
		}

		public String getId() { return id; }
		public int getLength() { return length; }
		public int getPriority() { return priority; }
		public int getArrivalTime() { return arrivalTime; }

		private String id;
		private int length;
		private int priority;
		private int arrivalTime;	
	}

	public class ProcessReader
	{
		public ProcessReader() { }
		public ProcessReader(File f) throws FileNotFoundException
		{
			fr = new FileReader(f);
		
	       	}

		public Process readNext() throws IOException
		{
			String a = new String();
			int rd = fr.read();
			while (rd != -1 && rd != '\n')
			{
				a += (char)rd;
				rd = fr.read();
			}

			String[] data = a.split("\t");
			
			if (data.length != 4) return null;
			Process p = new Process(data[0], 
					Integer.parseInt(data[1]), 
					Integer.parseInt(data[2]), 
					Integer.parseInt(data[3]));

			last = p;
			return p;
		}

		private Process last = null;


		private void readHeader() throws IOException
		{
			expect("<ID>\t<LENGTH>\t<PRIORITY>\t<ARRIVAL TIME>\n");
		}


		private boolean expect(String s) throws IOException 
		{
			int c = 0;
			int rd = fr.read();
			while (rd != -1 && c < s.length())
			{	
				if (s.charAt(c) != rd) return false;
				rd = fr.read();
				c++;
			}

			return true;
		}
	}

	/* 
	 * Each time slice spans a second.
	 */
	private final int TIME_SLICE = 1000;
	private FileReader fr;
	private HeapPriorityQueue<Integer, Process> queue;

	public Scheduler() throws IOException, FileNotFoundException
	{
		queue = new HeapPriorityQueue<>();
		ProcessReader r = new ProcessReader(new File("process.txt"));

		r.readHeader();
		Process p = r.readNext();
		while (p != null)
		{
			queue.insert(p.getPriority(), p);
			p = r.readNext();
		}
	}


	public static void main(String[] args) throws IOException, FileNotFoundException
	{
		Scheduler s = new Scheduler();
	}
}

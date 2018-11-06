import java.io.*;

public class Scheduler {

    /*
     * Each time slice spans a second.
     */
    private final int TIME_SLICE = 1000;

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
        ProcessReader r = new ProcessReader(new File(filename));
        Process p = r.next();
        while (p != null) {
            queue.insert(p.getPriority(), p);
            p = r.next();
        }
    }

    public void start() throws InterruptedException {
        while (!queue.isEmpty())
        {
            Process p = queue.min().getValue();
            if (p.getLength() == 0) {
                queue.removeMin();
                continue;
            }

            p.run();

            System.out.println("Time=" + time++);

            Thread.sleep(TIME_SLICE);
        }
    }

    private HeapPriorityQueue<Integer, Process> queue;
    private int time = 1;


    private class Process {
        public Process(String id, int length,
                       int priority, int arrivalTime) {
            this.id = id;
            this.length = length;
            this.priority = priority;
            this.arrivalTime = arrivalTime;
        }

        void run() {
            System.out.println("Now running " + id);
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

        private String id;
        private int length;
        private int priority;
        private int arrivalTime;
    }

    private class ProcessReader {
        public ProcessReader(File f) throws FileNotFoundException {
            fr = new FileReader(f);
            sc = new java.util.Scanner(fr);
            sc.nextLine(); /* read header */
        }

        public Process next() throws Exception
        {
            if (hasNext())
            {
                String tmp = sc.nextLine();
                String[] data = tmp.split("\\t");
                if (data.length != 4) throw new Exception("Data length is " + data.length);
                return new Process(data[0], Integer.parseInt((data[1])),
                        Integer.parseInt((data[2])), Integer.parseInt((data[3])));
            }

            return null;
        }

        public boolean hasNext()
        {
            return sc.hasNextLine();
        }

        private FileReader fr;
        private java.util.Scanner sc;
    }


}

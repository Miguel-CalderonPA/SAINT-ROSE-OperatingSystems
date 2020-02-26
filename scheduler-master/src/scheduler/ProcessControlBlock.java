package scheduler;

/**
 * Created by po917265 on 2/12/20.
 */
public class ProcessControlBlock {
    private int duration;
    private int cpuBurstTime;
    private int ioBurstTime; 
    private int currentBurstDuration;  //how much of the current burst is left.
    private String state; //the current state of the process.
    private int pid;  //the pid of the process.
    private int ioRequestTime; //the moment in time when an I/O request is made.

    private static int pidSource = 1;

    public static final String READY = "READY";
    public static final String NEW = "NEW";
    public static final String WAITING = "WAITING";
    public static final String TERMINATED = "TERMINATED";
    public static final String RUNNING = "RUNNING";

    /**
     * Creates a new ProcessControlBlock with the following info:
     * @param duration how long until the process terminates.
     * @param cpuBurstTime how long the process can use the CPU before it must request I/O
     * @param ioBurstTime how long it takes for an I/O request to be fulfilled.
     */
    public ProcessControlBlock(int duration, int cpuBurstTime, int ioBurstTime) {
        this.duration = duration;
        this.cpuBurstTime = cpuBurstTime;
        this.ioBurstTime = ioBurstTime;
        this.currentBurstDuration = cpuBurstTime;
        state = NEW;
        pid = pidSource++;
    }
					// time used
    public int execute(int quantum, int clock) {
        int theTime;
         state = RUNNING;
        System.out.println(this);
        if(quantum < currentBurstDuration) {  // will still exist
            System.out.println("|         Process " + pid + " will use entire quantum.");
            currentBurstDuration -= quantum;
            duration -= quantum;
            System.out.println("|         Process " + pid + " remaining duration: " + duration);
            theTime = quantum;
            state = READY;
        } else if(currentBurstDuration < duration){ // IO will happen
            System.out.println("|         Process " + pid + " will complete CPU burst.");
            int usedTime = currentBurstDuration;
            duration -= currentBurstDuration;
            currentBurstDuration = ioBurstTime;
            state = WAITING;
            ioRequestTime = clock + (usedTime - 1);
            System.out.println("|         Process " + pid + " remaining duration: " + duration);
            theTime = usedTime;
        } else {  // will finish
            System.out.println("|         Process " + pid + " will terminate.");
            int usedTime = duration; // remainder will be used time
            duration = 0;
            state = TERMINATED;
            System.out.println("|         Process " + pid + " remaining duration: " + duration);
            theTime = usedTime;
        }// end termination

        return theTime;
    }

    public void update(int clock) {
        //System.out.println("Process " + pid + " clock: " + clock + "; currentBurstDuration: " + currentBurstDuration + "; ioRequestTime: " + ioRequestTime);
        if(state.equals(READY)){}
        else if(state.equals(WAITING)) { // updates the process from waiting to ready
            if(clock - currentBurstDuration > ioRequestTime) {
                currentBurstDuration = cpuBurstTime;
                state = READY;
            }
        }
        else if(state.equals(NEW)) {
            System.out.println("---------------Initializing Process----------------(CLOCK: " + clock + ")");
            System.out.println(this);
            state=READY;
            System.out.println();
        }
    }


    public String state()   {  return state;  }


    public int pid()     {  return pid;    }

    public boolean equals(Object o) {
        if(o instanceof ProcessControlBlock) {
            return ((ProcessControlBlock) o).pid == pid;
        }
        return false;
    }

    public String toString() {
        return "|           {" + pid + ", " + state + ", " + duration + ", " + cpuBurstTime +
                    ", " + ioBurstTime + ", " + currentBurstDuration + "}";
    }
}

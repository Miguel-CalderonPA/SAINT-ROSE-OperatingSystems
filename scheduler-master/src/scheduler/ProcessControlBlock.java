package scheduler;

/**
 * Created by po917265 on 2/12/20.
 * modified by calderonm063 on 2/26/20
 */

public class ProcessControlBlock {
    private int duration; // total run-time of process
    private int cpuBurstTime; // time cpu uses per quantum if quantum large enough
    private int ioBurstTime;  // time it takes to do IO
    private int currentBurstDuration;  //how much of the current burst is left.
    private String state; //the current state of the process.
    private int pid;  //the pid of the process.
    private int ioRequestTime; //the moment in time when an I/O request is made.

    private static int pidSource = 1; // set beginning pid
    // States
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

    //--------------------------------------------------------------------------------------

    public ProcessControlBlock(int duration, int cpuBurstTime, int ioBurstTime) {
        // set PDMs
        this.duration = duration;
        this.cpuBurstTime = cpuBurstTime;
        this.ioBurstTime = ioBurstTime;
        this.currentBurstDuration = cpuBurstTime;
        state = NEW; // couldn't resist sorry paul
        pid = pidSource++; // sets then increments
    } // end non-default constructor

    //--------------------------------------------------------------------------------------

    public int execute(int quantum, int clock) {
        int theTime;
         state = RUNNING; // mainly cosmetic
        System.out.println(this);   // showing its running
        if(quantum < currentBurstDuration) {  // process uses entire time slice
            System.out.println("|         Process " + pid + " will use entire quantum.");
            currentBurstDuration -= quantum;
            duration -= quantum;
            System.out.println("|         Process " + pid + " remaining duration: " + duration);
            theTime = quantum;
            state = READY; // set state back after process "runs"
        } else if(currentBurstDuration < duration){ // IO will happen
            System.out.println("|         Process " + pid + " will complete CPU burst.");
            int usedTime = currentBurstDuration;
            duration -= currentBurstDuration;
            currentBurstDuration = ioBurstTime;
            state = WAITING;
            ioRequestTime = clock + (usedTime - 1); // needs to set to end of process time
            System.out.println("|         Process " + pid + " remaining duration: " + duration);
            theTime = usedTime;
        } else {  // will finish process
            System.out.println("|         Process " + pid + " will terminate.");
            int usedTime = duration; // remainder will be used time
            duration = 0;
            state = TERMINATED;
            System.out.println("|         Process " + pid + " remaining duration: " + duration);
            theTime = usedTime;
        }// end termination
        return theTime;
    } // end execute

    //--------------------------------------------------------------------------------------

    public void update(int clock) {
        //System.out.println("Process " + pid + " clock: " + clock + "; currentBurstDuration: " + currentBurstDuration + "; ioRequestTime: " + ioRequestTime);
        if(state.equals(READY)){}
        else if(state.equals(WAITING)) { // updates the process from waiting to ready
            if(clock - currentBurstDuration > ioRequestTime) {
                currentBurstDuration = cpuBurstTime;
                state = READY;
            } // end if IO done
        } // end if waiting for IO
        else if(state.equals(NEW)) {
            System.out.println("---------------Initializing Process----------------(CLOCK: " + clock + ")");
            System.out.println(this);
            state=READY;
            System.out.println();
        } // end if new only gets called once per process
    } // end update

    //--------------------------------------------------------------------------------------

    public String state()   {  return state;  } // end getState

    //--------------------------------------------------------------------------------------

    public int pid()     {  return pid;    } // end getPID

    //--------------------------------------------------------------------------------------

    public boolean equals(Object o) {
        if(o instanceof ProcessControlBlock) {
            return ((ProcessControlBlock) o).pid == pid;
        }
        return false;
    } // end equals

    //--------------------------------------------------------------------------------------

    public String toString() {
        return "|           {" + pid + ", " + state + ", " + duration + ", " + cpuBurstTime +
                    ", " + ioBurstTime + ", " + currentBurstDuration + "}";
    } // end override toString

} // end PCB class
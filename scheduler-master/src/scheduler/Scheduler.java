package scheduler;

/**
 * Created by po917265 on 2/12/20.
 * Modified by calderonm063 on 2/26/20
 * 13.5 27
 */

//--------------------------------------------------------------------------------------

public abstract class Scheduler implements Iterable<ProcessControlBlock> {

    protected int contextSwitchTime;  //how long a context switch takes.
    protected int clock;  //the number of cycles since the system was "started" (the current time).
    protected int idleCt;
    protected int waitTime;
    public Scheduler(int contextSwitchTime) {
        this.contextSwitchTime = contextSwitchTime; // is set by super
        clock = 0; // initialize clock commit?
        idleCt = 0;
        waitTime = 0;
    }

    //--------------------------------------------------------------------------------------

    // all done by descendant class
    public abstract void add(ProcessControlBlock pcb);
    public abstract ProcessControlBlock next();
    public abstract boolean isEmpty();
    public abstract void execute(ProcessControlBlock pcb);

    //--------------------------------------------------------------------------------------

    public void execute() { // displays
        double utilization, avgWaitTime;
        System.out.println("---------------Executing Processes----------------(CLOCK: " + clock + ")\n");
        while(!isEmpty() && clock < 100) { 
            updateProcessControlBlocks(); // update clocks
            System.out.println("\n-----------------Current Processes----------------(CLOCK: " + clock + ")");
            for(ProcessControlBlock pcb : this) { // outputs status for each
               System.out.println(pcb);
            }
            ProcessControlBlock pcb = next(); // retrieves next ready process
            clock += contextSwitchTime;  //a context switch is happening...
            if(pcb == null) { // if no processes are ready
                idle(); // if no PCB to run then wasted CPU time
                System.out.println("               -ALL Processes IDLE-                ");
                tick(); // clock continues
                continue;
            }
           // System.out.println(pcb + "@" + clock); // prints current process
            System.out.println("\n              ---Current Slice Work---            (CLOCK: " + clock + ")");
            execute(pcb); // goes to scheduler specific execution
            System.out.println();
            add(pcb); // re checks status
        } // end while process are still running
        System.out.println("------------------Ending Processes----------------(CLOCK: " + clock + ")");
        for(ProcessControlBlock pcb : this) { // outputs status for each
            System.out.println(pcb);
        } // end prints
        utilization = getUtilization(); // gets calculated CPU utilization
        avgWaitTime = getAverageWait(); // gets calculated Average Wait
        System.out.println("\nFinal Statistics: ");
        System.out.printf("CPU Utilization: %4.2f%%\n", utilization);
        System.out.printf("Average Wait-Time: %4.2f%%\n", avgWaitTime);
    } // end execute

    //--------------------------------------------------------------------------------------

    public void tick() {
        clock++; 
    } // end tick

    public void tick(int time) {
        clock += time; // add the duration of a process
    }

    //--------------------------------------------------------------------------------------

    public void idle() { idleCt++; } // end idleTime count

    //--------------------------------------------------------------------------------------

    public void setWaitTime(int usedTime) {
        waitTime += usedTime; // # of CPU units spent waiting for CPU
    } // end setWaitTime

    //--------------------------------------------------------------------------------------

    public double getUtilization() {
        int utilAmt;
        utilAmt = clock - idleCt;
        return (double)100 * ((double)utilAmt / (double)clock);
    } // end getCPU_Utilization

    //--------------------------------------------------------------------------------------

    public double getAverageWait() {
        waitTime += (double)1; // offset usedTime calculation
        return (double)100 * ((double)waitTime / (double)clock);
    } // end getAverageWait

    //--------------------------------------------------------------------------------------

    public void updateProcessControlBlocks() {
        System.out.println("               -Updating Processes-               ");
        for(ProcessControlBlock pcb : this) { // update clocks
            pcb.update(clock);
        } // end updates for each process
    } // end updateProcess

    //--------------------------------------------------------------------------------------

} // end scheduler class

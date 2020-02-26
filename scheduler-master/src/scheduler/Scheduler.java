package scheduler;

/**
 * Created by po917265 on 2/12/20.
 * 13.5 27
 */
public abstract class Scheduler implements Iterable<ProcessControlBlock> {

    protected int contextSwitchTime;  //how long a context switch takes.
    protected int clock;  //the number of cycles since the system was "started" (the current time).
    protected int idleCt;
    public Scheduler(int contextSwitchTime) {
        this.contextSwitchTime = contextSwitchTime; // is set by super
        clock = 0; // initialize clock commit?
        idleCt = 0;
    }

    public abstract void add(ProcessControlBlock pcb);
    public abstract ProcessControlBlock next();
    public abstract boolean isEmpty();
    public abstract void execute(ProcessControlBlock pcb);

    public void execute() { // displays
        int readyQCt = 0;
        double utilization, waitTime;
        System.out.println("---------------Executing Processes----------------(CLOCK: " + clock + ")\n");
        while(!isEmpty() && clock < 100) { 
            updateProcessControlBlocks(); // update clocks
            System.out.println("\n-----------------Current Processes----------------(CLOCK: " + clock + ")");
            for(ProcessControlBlock pcb : this) { // outputs status for each
               System.out.println(pcb);
            }
            ProcessControlBlock pcb = next(); // retrieves next
            // add function to get size of ready queue!!!!!!!!!!!!!!!!!!!!!!!!!
            readyQCt += calcWaitingTime();
            clock += contextSwitchTime;  //a context switch is happening...
            if(pcb == null) { // if nothing
                idle(); // if no PCB to run then wasted
                System.out.println("               -ALL Processes IDLE-                ");
                tick(); // clock continues
                continue;
            }
           // System.out.println(pcb + "@" + clock); // prints current process
            System.out.println("\n              ---Current Slice Work---            (CLOCK: " + clock + ")");
            execute(pcb); // goes to scheduler specific execution
            System.out.println();
            add(pcb); // re checks status

        }
        System.out.println("------------------Ending Processes----------------(CLOCK: " + clock + ")");
        for(ProcessControlBlock pcb : this) { // outputs status for each
            System.out.println(pcb);
        }
        utilization = getUtilization();
         waitTime = getWaitTime(readyQCt);
        System.out.println("\nFinal Statistics: ");
        System.out.printf("CPU Utilization: %4.2f%%\n", utilization);
        System.out.printf("Average Wait-Time: %4.2f%%", waitTime);
    }
    public void tick() {
        clock++; 
    }

    public void tick(int time) {
        clock += time; // add the duration of a process
    }

    public void idle() { idleCt++; }

    public int calcWaitingTime() {
        int readyCt = 0;
        for(ProcessControlBlock pcb : this) { // outputs status for each
            if (pcb.state().equals(ProcessControlBlock.READY)){
                readyCt++;
            }
        }
        if (readyCt > 1) {
            return 1;
        }
        else {
            return 0;
        }

    }

    public double getWaitTime(int readyQCt) {
        double waitTime;
        int waitAmt;
        waitAmt = clock - readyQCt;
        waitTime = (double)100 * ((double)waitAmt / (double)clock);
        return waitTime;
    }

    public double getUtilization() {
        double utilization;
        int utilAmt;
        utilAmt = clock - idleCt;
        utilization = (double)100 * ((double)utilAmt / (double)clock);
        return utilization;
    }
    public void updateProcessControlBlocks() {
        System.out.println("               -Updating Processes-               ");
        for(ProcessControlBlock pcb : this) { // update clocks
            pcb.update(clock);
        }
    }
}

package scheduler;

/**
 * Created by po917265 on 2/12/20.
 */
public abstract class Scheduler implements Iterable<ProcessControlBlock> {

    protected int contextSwitchTime;  //how long a context switch takes.
    protected int clock;  //the number of cycles since the system was "started" (the current time).

    public Scheduler(int contextSwitchTime) {
        this.contextSwitchTime = contextSwitchTime; // is set by super
        clock = 0; // initialize clock commit?
    }

    public abstract void add(ProcessControlBlock pcb);
    public abstract ProcessControlBlock next();
    public abstract boolean isEmpty();
    public abstract void execute(ProcessControlBlock pcb);

    public void execute() { // displays
        System.out.println("Executing Processes");
        while(!isEmpty() && clock < 100) { 
            updateProcessControlBlocks(); // update clocks
            System.out.println("---------Current Processes----------(CLOCK: " + clock + ")");
            for(ProcessControlBlock pcb : this) { // outputs status for each
                System.out.println(pcb);
            }
            ProcessControlBlock pcb = next(); // retrieves next
            clock += contextSwitchTime;  //a context switch is happening...
            if(pcb == null) { // if nothing 
                tick(); // clock continues
                continue;
            }
            System.out.println(pcb + "@" + clock); // prints current process
            execute(pcb); // goes to scheduler specific execution
            add(pcb); // re checks status
        }
    }
    public void tick() {
        clock++; 
    }

    public void tick(int time) {
        clock += time; // add the duration of a process
    }

    public void updateProcessControlBlocks() {
        System.out.println("Updating Processes (CLOCK: " + clock + ")");
        for(ProcessControlBlock pcb : this) { // update clocks
            pcb.update(clock);
        }
    }

}

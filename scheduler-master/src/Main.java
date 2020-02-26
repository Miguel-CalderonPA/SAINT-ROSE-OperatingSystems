import scheduler.ProcessControlBlock;
import scheduler.RoundRobin;

public class Main {

   public static void main(String[] args) {
/*
	(1) implement Round Robin Scheduling 35% // | COMPLETE |

    (2) Implement Multi-level feedback queue 35% | NONE |

    (3) For each, show cpu utilization when all processes are finished (15%) | HALF |

    (4) For each, show average wait time when all processes are finished (15%) | HALF |
  
	 */
        // dur 20 cpu2,  io 3

        // declare processes
        ProcessControlBlock a = new ProcessControlBlock(20, 5, 3);
        ProcessControlBlock b = new ProcessControlBlock(20, 10, 1);
        //ProcessControlBlock c = new ProcessControlBlock(17, 10, 1);
        // declare scheduler
        RoundRobin sched = new RoundRobin(0, 3);
        
        // add processes
        sched.add(a);
        sched.add(b);
        // sched.add(c);
        sched.execute();
    } // end main
}

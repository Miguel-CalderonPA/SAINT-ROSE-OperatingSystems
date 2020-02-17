import scheduler.ProcessControlBlock;
import scheduler.RoundRobin;

public class Main {

    public static void main(String[] args) {
	/*
	(1) implement Round Robin Scheduling 35%

    (2) Implement Multi-level feedback queue 35%

    (3) For each, show cpu utilization when all processes are finished (15%)

    (4) For each, show average wait time when all processes are finished (15%)
	 */
	ProcessControlBlock a = new ProcessControlBlock(20, 5, 3);
	ProcessControlBlock b = new ProcessControlBlock(20, 10, 1);
	ProcessControlBlock c = new ProcessControlBlock(17, 10, 1);
	RoundRobin sched = new RoundRobin(0, 5);

    sched.add(a);
    sched.add(b);
    sched.add(c);
    sched.execute();



    }
}

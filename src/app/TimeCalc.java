package app;

import java.time.Duration;
import java.time.Instant;

public class TimeCalc {
	
	Instant starttime;
	public TimeCalc() {
		startTime();
	}
	public void startTime() {
	    //Method 1: Get current instant
	    starttime = Instant.now();
	    System.out.println("Starting time " + starttime);    //2018-07-14T08:10:44.270972700Z
	}
	
	Instant endtime;
	public void endTime() {
	    //Method 1: Get current instant
	    endtime = Instant.now();
	    System.out.println("Finished at time " + endtime);    //2018-07-14T08:10:44.270972700Z
	}
	
	public void getDuration() {
		System.out.println(("Evaluation took " + 
				Duration.between(starttime, endtime)).replace("PT","").replace("S"," seconds"));
	}
}

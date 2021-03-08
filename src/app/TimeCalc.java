package app;

import java.time.Duration;
import java.time.Instant;

//Greenwich time!?
public class TimeCalc {
	
	Instant starttime, endtime;
	boolean clock_stopped = false;
	String durString;
	
	public TimeCalc() {
		startTime();
	}
	public void startTime() {
	    starttime = Instant.now();
	    System.out.println("Starting time " + starttime);    //2018-07-14T08:10:44.270972700Z
	}
	
	public void endTime() {
		if (!clock_stopped) {
			endtime = Instant.now();
			durString = ("" + Duration.between(starttime, endtime)).replace("PT","").replace("S"," sec.");
			clock_stopped = true;
			System.out.println("Finished at time " + endtime);    //2018-07-14T08:10:44.270972700Z
		}
	}
	
	public String getDuration() {
		endTime();
		return durString;
	}

	public void printDuration() {
		endTime();
		System.out.println("Evaluation took " + durString);
	}
}

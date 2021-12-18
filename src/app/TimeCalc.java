package app;

import java.time.Duration;
import java.time.Instant;

public class TimeCalc {
	
	Instant starttime, endtime;
	boolean clock_stopped = false;
	String durString;
	
	public TimeCalc() {
		startTime();
	}
	public void startTime() {
	    starttime = Instant.now();
	}
	
	public void endTime() {
		if (!clock_stopped) {
			endtime = Instant.now();
			durString = (""+ Duration.between(starttime, endtime)).replace("PT","");
			durString = durString.replace("S"," sec.");
			clock_stopped = true;
		}
	}
	
	public String getDuration() {
		endTime();
		return durString;
	}

}

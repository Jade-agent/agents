import jade.core.behaviours.OneShotBehaviour;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class PrintTimeBehaviour extends OneShotBehaviour {
		public void action(){
			Calendar cal = Calendar.getInstance();
	    	cal.getTime();
	    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	    	System.out.println( sdf.format(cal.getTime()) );
		}
}

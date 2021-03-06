import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.util.leap.Iterator;


public class GamerAgent extends Agent {
	private static final long serialVersionUID = 1L;
	public int sleepyness = 10;
	int hungryness = 51;
	int thirstyness = 51;
	boolean workIsCalling = false;

	public void setup(){
		super.setup();
		
		WakerBehaviour shouldWork = new WakerBehaviour(this, 2000) {
			@Override
			protected void onWake() {
				// TODO Auto-generated method stub
				super.onWake();
				workIsCalling = true;
				System.out.println("Got a call that I should go to work");
			}
		};
		
		FSMBehaviour fsm = new FSMBehaviour();
		
		Behaviour sleep = new TickerBehaviour(this, 100) {
			@Override
			public void onTick() {
				GamerAgent agent = (GamerAgent)this.myAgent;
				agent.sleepyness -= 10;
				System.out.println(agent.sleepyness + " sleeping");
				if (agent.sleepyness == 0){
					this.stop();
				}
			}
			public int onEnd(){
				System.out.println("Done Sleeping");
				this.reset();
				return 0;
			}
		};
		fsm.registerFirstState(sleep, "sleeping");
		
		Behaviour eat = new OneShotBehaviour() {	
			@Override
			public void action() {
				hungryness = 0;
				System.out.println("ate something");
			}
			public int onEnd(){
				return 0;
			}
		};
		fsm.registerState(eat, "eating");
		
		Behaviour drink = new OneShotBehaviour() {	
			@Override
			public void action() {
				thirstyness = 0;
				System.out.println("drank something");
			}
			public int onEnd(){
				return 0;
			}
		};
		fsm.registerState(drink, "drinking");
		
		
		Behaviour wake = new OneShotBehaviour() {		
			@Override
			public void action() {
				System.out.println("awake");				
			}
			public int onEnd(){
				System.out.println(thirstyness + " / " + hungryness + " / " + sleepyness);
				if (workIsCalling){
					return -1;
				}
				if (thirstyness > 50){
					return 0;
				}
				if (hungryness > 50){
					return 2;
				}
				if(sleepyness > 50){
					return 1;
				}
				return 3;//can game!
			}
		};
		fsm.registerState(wake, "awake");
		
		Behaviour work = new OneShotBehaviour() {
			@Override
			public void action() {
				System.out.println("Going to work");
			}
		};
		fsm.registerLastState(work, "working");
		
		Behaviour game = new OneShotBehaviour() {
			@Override
			public void action() {
				for (int i=0; i<10; i++){
					thirstyness += 25;
					hungryness += 25;
					sleepyness += 1;
					System.out.println("In the gaaming zone: "+i);
				}
			}
			public int onEnd(){
				return 0;
			}
		};
		fsm.registerState(game, "gaming");
		
		
		
		
		fsm.registerTransition("sleeping", "awake", 0);
		
		fsm.registerTransition("eating", "awake", 0);
		
		fsm.registerTransition("drinking", "awake", 0);
		
		fsm.registerTransition("gaming", "awake", 0);
		
		fsm.registerTransition("awake", "drinking", 0);
		fsm.registerTransition("awake", "sleeping", 1);
		fsm.registerTransition("awake", "eating", 2);
		fsm.registerTransition("awake", "gaming", 3);
		fsm.registerTransition("awake", "working", -1);

		
		this.addBehaviour(fsm);
		this.addBehaviour(shouldWork);
	}
	
	public void init(){
	}
}

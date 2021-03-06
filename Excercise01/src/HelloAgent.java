import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.util.leap.Iterator;


public class HelloAgent extends Agent {
	private static final long serialVersionUID = 1L;

	public void setup(){
		super.setup();
		
//		this.setDataStore(sb.getDataStore());
		/*
		System.out.println("It's alive!");
		
		System.out.println("ID: " + this.getAID());
		Object arguments = this.getArguments();
		if (arguments == null){
			System.out.println("No arguments provided");
		}
		else {
			for(Object argument: this.getArguments()){
				System.out.println("Argument: " + argument.toString());
			}
		}*/
		
	//	this.addBehaviour(new PrintAllAddressBehaviour());
			
		this.addBehaviour(new PrintTimeBehaviour());
			
		this.addBehaviour(new PrintNameBehaviour(this, 1000));
		
		SequentialBehaviour sb = new SequentialBehaviour();
		
		GetAddressesBehaviour gab = new GetAddressesBehaviour();
		gab.setDataStore(sb.getDataStore());
		
		PrintAddressesBehaviour pab = new PrintAddressesBehaviour();
		pab.setDataStore(sb.getDataStore());

		sb.addSubBehaviour(gab);
		sb.addSubBehaviour(pab);
		
		this.addBehaviour(sb);
		
	}
	
	public void init(){
	}
}

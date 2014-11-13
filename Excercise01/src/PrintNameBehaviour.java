import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;


public class PrintNameBehaviour extends TickerBehaviour{
	private Agent agent;
	
	public PrintNameBehaviour(Agent agent, int timeout){
		super(agent, timeout);
		this.agent = agent;
	}
	
	public void onTick(){
		System.out.println("Name: " + this.agent.getLocalName());
		/*if (getLocalName().equals("HelloMan")){
			System.out.println("Tick");
		}
		else {
			System.out.println("Tack");
		}*/
	}
}

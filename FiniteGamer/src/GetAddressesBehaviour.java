import jade.core.behaviours.SimpleBehaviour;


public class GetAddressesBehaviour extends SimpleBehaviour {
	boolean done = false;
	
	public void action(){
		String[] addresses = this.myAgent.getAMS().getAddressesArray();
		getDataStore().put("addresses", addresses);
		done = true;
	}
	
	public boolean done(){
		return done;
	}
}

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;


public class PrintAllAddressBehaviour extends OneShotBehaviour {

		@Override
		public void action() {
			String[] addresses = this.myAgent.getAMS().getAddressesArray();
			for (String address : addresses){
				System.out.println("Address:" + address);
			}			
		}
}

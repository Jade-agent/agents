import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ClockAgent extends Agent {
	private static final long serialVersionUID = -6824927768833968712L;

	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType("Time-Service");
		sd.setName(this.getLocalName() + "-Time-Service");

		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		this.addBehaviour(new ServeTimeRequests());
	}
	
	private class ServeTimeRequests extends CyclicBehaviour{
		private static final long serialVersionUID = -7298119436555121085L;

		public void action(){
			MessageTemplate tmp = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage m = receive(tmp);
			
			if (m != null){
				System.out.println(m);
			}
		}
	}

	public void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
}

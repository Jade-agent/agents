import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PongAgent extends Agent {
	private static final long serialVersionUID = -4436468241512863722L;

	public void setup() {
		registerAgent();
		this.addBehaviour(new AnswerPings());
	}
	
	private class AnswerPings extends CyclicBehaviour{
		private static final long serialVersionUID = -5018397038252984135L;

		public void action(){
			MessageTemplate tmp = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage m = receive(tmp);
			
			if (m != null){
				System.out.println(m);
			}
		}
	}

	private void registerAgent() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType("Reply-To-Ping-Service");
		sd.setName(this.getLocalName() + "-Reply-To-Ping-Service");

		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
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

import FIPA.DateTime;
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

	private class AnswerPings extends CyclicBehaviour {
		private static final long serialVersionUID = -5018397038252984135L;
		private long lastRequest = 0;
		private static final int ONE_SECOND = 1000;

		public void action() {
			MessageTemplate tmp = MessageTemplate
					.MatchPerformative(ACLMessage.INFORM);
			ACLMessage m = receive(tmp);

			if (m != null) {
				ACLMessage reply = m.createReply();
				String message = m.getContent();
				long rightNow = System.currentTimeMillis();

				if (message.equals("Ping")) {
					long difference = rightNow - this.lastRequest;
					if (difference < ONE_SECOND) {
						// Reply refuse
						reply.setPerformative(ACLMessage.REFUSE);
						System.out.println("Refuse");
					} else {
						System.out.println("Got a ping. Answering now");
						reply.setPerformative(ACLMessage.INFORM);
						reply.setContent("Pong");
					}
				} else {
					System.out.println("not understood");
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					// Reply with not-understood
				}
				lastRequest = rightNow;
				this.myAgent.send(reply);
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

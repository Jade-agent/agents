import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Serializable;

public class PingAgent extends Agent {

	private SendOutPingsBehaviour sopb;
	private int frequency = 100;
	private AID pongAgent;

	public void setup() {
		this.addBehaviour(new SimpleBehaviour() {

			public void action() {
				findAndSetPongAgent();
			}

			public boolean done() {
				return pongAgent == null ? false : true;
			}
		});
		sopb = new SendOutPingsBehaviour(frequency);
		this.addBehaviour(sopb);
		this.addBehaviour(new ReplyToAnswerBehaviour());
	}

	private void findAndSetPongAgent() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Reply-To-Ping-Service");
		template.addServices(sd);

		try {
			DFAgentDescription[] dfds = DFService.search(this, template);

			if (dfds.length > 0) {
				pongAgent = dfds[0].getName();
				System.out.println("PA found: " + pongAgent);
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	private void changeBehaviourToGoSlower() {
		this.removeBehaviour(sopb);
		this.frequency *= 2;
		System.out.println("New frequency in ms:" + this.frequency);
		sopb = new SendOutPingsBehaviour(this.frequency);
		this.addBehaviour(sopb);
	}

	private class SendOutPingsBehaviour extends SequentialBehaviour implements
			Serializable {
		private static final long serialVersionUID = 1L;
		private int frequency;

		public SendOutPingsBehaviour(int frequency) {
			this.frequency = frequency;
		}

		public void onStart() {
			this.addSubBehaviour(new TickerBehaviour((Agent) this.myAgent,
					frequency) {
				@Override
				protected void onTick() {
					jade.lang.acl.ACLMessage message = new jade.lang.acl.ACLMessage(
							jade.lang.acl.ACLMessage.INFORM);
					message.addReceiver(pongAgent);
					message.setContent("Ping");
					this.myAgent.send(message);
				}
			});
		}

		public void reset() {
			super.reset();
			pongAgent = null;
		}
	}

	private class ReplyToAnswerBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = -5018397038252984135L;
		private long lastRequest = 0;
		private static final int ONE_SECOND = 1000;

		public void action() {
			MessageTemplate tmp = MessageTemplate
					.MatchPerformative(ACLMessage.REFUSE);
			ACLMessage m = receive(tmp);
			if (m != null) {
				System.out.println("Oops I am going to fast. Adjusting now");
				changeBehaviourToGoSlower();
			}
		}
	}
}

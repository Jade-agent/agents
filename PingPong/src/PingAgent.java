import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.util.leap.Serializable;

public class PingAgent extends Agent {

	public void setup() {
		this.addBehaviour(new SendOutPingsBehaviour());
	}

	private class SendOutPingsBehaviour extends SequentialBehaviour implements
			Serializable {
		private static final long serialVersionUID = 1L;
		private AID pongAgent = null;

		private void findAndSetPongAgent() {
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("Reply-To-Ping-Service");
			template.addServices(sd);

			try {
				DFAgentDescription[] dfds = DFService.search(this.myAgent,
						template);

				if (dfds.length > 0) {
					pongAgent = dfds[0].getName();
					System.out.println("PA found: " + pongAgent);
				}
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}
		}

		public void onStart() {
			this.addSubBehaviour(new SimpleBehaviour() {

				public void action() {
					findAndSetPongAgent();
				}

				public boolean done() {
					return pongAgent == null ? false : true;
				}
			});
			this.addSubBehaviour(new TickerBehaviour((Agent) this.myAgent, 2000) {

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
			this.pongAgent = null;
		}
	}

}

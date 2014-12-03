import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.introspection.ACLMessage;
import jade.util.leap.Serializable;

public class UserAgent extends Agent {
	private static final long serialVersionUID = 5948766986878089120L;

	public void setup() {
		addBehaviour(new TimeRequestBehaviour());
	}

	private class TimeRequestBehaviour extends SequentialBehaviour implements Serializable {
		private static final long serialVersionUID = 1L;
		private AID clockAgent = null;

		public void onStart() {
			this.addSubBehaviour(new SimpleBehaviour() {

				private static final long serialVersionUID = -7722067965657880385L;

				public void action() {
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("Time-Service");
					template.addServices(sd);

					try {
						DFAgentDescription[] dfds = DFService.search(this.myAgent, template);

						if (dfds.length > 0) {
							clockAgent = dfds[0].getName();
							System.out.println("TS found: " + clockAgent);
						}
					} catch (FIPAException fe) {
						fe.printStackTrace();
					}
				} 
				
				public boolean done() {
					boolean ret = false;

					if (clockAgent != null) {
						ret = true;
					}
					return ret;
				}
			});
			this.addSubBehaviour(new OneShotBehaviour(){
				public void action(){
					jade.lang.acl.ACLMessage m = new jade.lang.acl.ACLMessage (jade.lang.acl.ACLMessage.REQUEST);
					m.addReceiver(clockAgent);
					m.setContent("dd.NN.yy hh:mm:ss");
					this.myAgent.send(m);				
				}
			});
		}

		public void reset() {
			super.reset();
			this.clockAgent = null;
		}
	}
}

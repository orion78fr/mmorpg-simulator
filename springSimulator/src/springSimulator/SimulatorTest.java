package springSimulator;

import org.simgrid.msg.Msg;

public class SimulatorTest {

	public static void main(String[] args) {
		/*
		 * Initialize the MSG simulation. Must be done before anything else
		 * (even logging).
		 */
		Msg.init(args);

		String platf = "platform.xml";
		String deploy = "deploy.xml";

		/* Construct the platform and deploy the application */
		Msg.createEnvironment(platf);
		Msg.deployApplication(deploy);

		/* Execute the simulation. */
		Msg.run();
	}
}
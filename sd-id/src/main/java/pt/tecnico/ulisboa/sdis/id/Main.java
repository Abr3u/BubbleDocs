package pt.tecnico.ulisboa.sdis.id;

import java.util.*;
import javax.xml.ws.Endpoint;


public class Main {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length == 0 || args.length == 2) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + Main.class.getName()
					+ " wsURL OR uddiURL wsName wsURL");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;

		SDIdImpl impl = null;
		if (args.length == 1) {
			wsURL = args[0];
			impl = new SDIdImpl(wsURL);
		} else if (args.length >= 3) {
			uddiURL = args[0];
			wsName = args[1];
			wsURL = args[2];
			impl = new SDIdImpl(uddiURL, wsName, wsURL);
		}
		impl.populate();
        try {
            
            impl.start();
    		if ("true".equalsIgnoreCase(System.getProperty("ws.test"))) {
                impl.reset();
    		}
            impl.awaitConnections();

        } finally {
            impl.stop();
            
        }
	}

}

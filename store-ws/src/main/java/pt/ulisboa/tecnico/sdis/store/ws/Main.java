package pt.ulisboa.tecnico.sdis.store.ws;


import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.store.ws.SdStoreImpl;
import pt.ulisboa.tecnico.sdis.store.ws.Main;
import  pt.ulisboa.tecnico.sdis.store.ws.uddi.UDDINaming; 


public class Main {

	private static int N = 3;
	
    public static void main(String[] args) {
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
        if (args.length == 1) {
            wsURL = args[0];
        } else if (args.length >= 3) {
            uddiURL = args[0];
            wsName = args[1];
            wsURL = args[2];
        }
        String wsTestUrl = wsURL + "/test";

        Endpoint endpoint = null;
        Endpoint testEndpoint = null;

        UDDINaming uddiNaming = null;

        try {
            SdStoreImpl impl = new SdStoreImpl();
            if (System.getProperty("store-ws.test") != null) {
                System.out.println("Populating test data...");
            }
            
            endpoint = Endpoint.create(impl);

            // publish endpoint
            System.out.printf("Starting %s%n", wsURL);
            endpoint.publish(wsURL);

            // publish to UDDI
            if (uddiURL != null) {
                System.out.printf("Publishing '%s' to UDDI at %s%n", wsName,
                        uddiURL);
                uddiNaming = new UDDINaming(uddiURL);
                uddiNaming.rebind(wsName, wsURL);
            }
            
            int i = 0;
            
            for(i =1; i <= N; i++) {
            	
            	endpoint = Endpoint.create(new SdStoreImpl());
            	endpoint.publish(wsURL + new Integer(i).toString());
            	
            	uddiNaming.rebind(wsName + new Integer(i).toString(), wsURL+new Integer(i).toString());
            	
            	System.out.printf("Publishing '%s' to UDDI at %s%n", wsName +new Integer(i).toString(), uddiURL);
            	
            }

            // wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();

        } catch (Exception e) {
            System.out.printf("Caught exception: %s%n", e);
            e.printStackTrace();

        } finally {
            try {
                if (endpoint != null) {
                    // stop endpoint
                    endpoint.stop();
                    System.out.printf("Stopped %s%n", wsURL);
                    //stop replicas endpoint
                   
                    
                }
               
            } catch (Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
            try {
                if (uddiNaming != null) {
                    // delete from UDDI
                    uddiNaming.unbind(wsName);
                    System.out.printf("Deleted '%s' from UDDI%n", wsName);
                  
                    //delete replicas from UDDI
                    for(int i =1; i <= N; i++) {
                    	 uddiNaming.unbind(wsName+ new Integer(i).toString());
                    	 System.out.printf("Deleted '%s' from UDDI%n", wsName+ new Integer(i).toString());
                    }
                
                
                }
            } catch (Exception e) {
                System.out.printf("Caught exception when deleting: %s%n", e);
            }
        }

    }

}

package pt.tecnico.ulisboa.sdis.id.it;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import pt.tecnico.ulisboa.sdis.id.cli.SDIdClient;

/**
 * Integration Test suite abstract class
 */
public abstract class AbstractIdIT {

    private static final String TEST_PROP_FILE = "/test.properties";

    private static Properties PROPS;
    protected static SDIdClient ID_CLIENT;

    @BeforeClass
    public static void oneTimeSetup() throws Exception {
//        PROPS = new Properties();
//        try {
//            PROPS.load(AbstractIdIT.class.getResourceAsStream(TEST_PROP_FILE));
//        } catch (IOException e) {
//            final String msg = String.format(
//                    "Could not load properties file {}", TEST_PROP_FILE);
//            System.out.println(msg);
//            throw e;
//        }
//        String uddiEnabled = PROPS.getProperty("uddi.enabled");
//        String uddiURL = PROPS.getProperty("uddi.url");
//        String wsName = PROPS.getProperty("ws.name");
//        String wsURL = PROPS.getProperty("ws.url");
//
    	
    	String uddiURL = "http://localhost:8081";
    	String uddiEnabled = "true";
    	String wsName = "SDId";
    	String wsURL = "http://localhost:8080/id-ws/endpoint";
        if ("true".equalsIgnoreCase(uddiEnabled)) {
            ID_CLIENT = new SDIdClient(uddiURL, wsName);
            ID_CLIENT.execute();
        } else {
            //ID_CLIENT = new SDIdClient(wsURL);
        }
        //ID_CLIENT.setVerbose(true);

        System.out.println("Resetting ID Web Service state...");
        ID_CLIENT.reset();
    }

    @AfterClass
    public static void cleanup() {
        ID_CLIENT = null;
    }

}

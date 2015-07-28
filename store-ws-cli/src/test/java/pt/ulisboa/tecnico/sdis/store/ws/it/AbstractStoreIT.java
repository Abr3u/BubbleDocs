package pt.ulisboa.tecnico.sdis.store.ws.it;

import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;




import pt.ulisboa.tecnico.sdis.store.ws.cli.SDStoreCli;

/**
 * Integration Test suite abstract class
 */
public abstract class AbstractStoreIT {

    private static final String TEST_PROP_FILE = "/test.properties";

    private static Properties PROPS;
    protected static SDStoreCli STORE_CLIENT;

    @Before
    public void oneTimeSetup() throws Exception {
       /* PROPS = new Properties();
        try {
            PROPS.load(AbstractStoreIT.class.getResourceAsStream(TEST_PROP_FILE));
        } catch (IOException e) {
            final String msg = String.format(
                    "Could not load properties file {}", TEST_PROP_FILE);
            System.out.println(msg);
            throw e;
        }
        String uddiEnabled = PROPS.getProperty("uddi.enabled");
        String uddiURL = PROPS.getProperty("uddi.url");
        String wsName = PROPS.getProperty("ws.name");
        String wsURL = PROPS.getProperty("ws.url");*/
    	
    	String uddiEnabled = "true";
        String uddiURL = "http://localhost:8081";
        String wsName = "SD-Store";
        String wsURL = "http://localhost:8082/store-ws/endpoint";

        if ("true".equalsIgnoreCase(uddiEnabled)) {
            STORE_CLIENT = new SDStoreCli(uddiURL, wsName);
            STORE_CLIENT.execute();
            STORE_CLIENT.setTesting(true);
        } 

    }

    @After
    public void cleanup() {
        STORE_CLIENT.reset();
    }

}

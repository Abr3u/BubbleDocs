package tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mockit.*;

import javax.xml.ws.WebServiceException;
import javax.xml.registry.JAXRException;

import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.cli.SDStoreCli;

public class SDStoreCliTest {


	private static DocUserPair testPair = new DocUserPair();
	private final static String userId = "alice";
	private final static String docId = "doc1";
	private final static byte[] docContent = {1, 2, 3};
	
	private static final String uddiURL = "http://localhost:8081";
	private static String wsName = "SD-Store";
	
	private static SDStoreCli client = null; 
	
    @BeforeClass
    public static void oneTimeSetUp() {
    }

    @AfterClass
    public static void oneTimeTearDown() {
    }
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testPair.setUserId(userId);
		testPair.setDocumentId(docId);
		client = new SDStoreCli(uddiURL,wsName);
		client.execute();
		client.setTesting(true);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	
	
	@Test
	public void testCreateDoc()
					throws Exception{
		
		client.createDoc(testPair);
	}
	
	@Test
	public void testStore(
			@Mocked final SDStore_Service service,
			@Mocked final SDStore port)
					throws Exception{
		port.store(testPair, docContent);
	}
	
	@Test
	public void testLoad(
			@Mocked final SDStore_Service service,
			@Mocked final SDStore port)
					throws Exception{
		port.load(testPair);
	}
	
	@Test
	public void testListDocs(
			@Mocked final SDStore_Service service,
			@Mocked final SDStore port)
					throws Exception{
		port.listDocs(userId);
	}

}



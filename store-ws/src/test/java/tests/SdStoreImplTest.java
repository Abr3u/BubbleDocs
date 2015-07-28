package tests;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.*;

public class SdStoreImplTest extends TestCase {
	
	SdStoreImpl testSdStore;
	
	private final int oversize = 10*1024+1;
	
	private final static String userId1 = "alice";
	private final static String docId1 = "Doc1";
	private final byte[] docContent1 = {1, 2, 3};
	private final byte[] docContent2 = new byte[oversize];
	
	@Before
	public void setUp(){	
	}
	
	@After
	public void tearDown(){
	}


	
	@Test
	public void testCreateDoc() throws Exception{
		testSdStore = new SdStoreImpl();
		
		DocUserPair testPair = new DocUserPair();
		testPair.setUserId(userId1);
		testPair.setDocumentId(docId1);
		
		testSdStore.createDoc(testPair);
		
		assertTrue(testSdStore._repositories.containsKey(userId1));
		assertTrue(testSdStore._repositories.get(userId1).hasDoc(docId1));
	}
	
	@Test
	public void testCreateDocAlreadyExists() throws Exception {
		testSdStore = new SdStoreImpl(); 
		
		DocUserPair testPair = new DocUserPair();
		testPair.setUserId(userId1);
		testPair.setDocumentId(docId1);
		
		try{
			testSdStore.createDoc(testPair);
			testSdStore.createDoc(testPair);	
		}catch(DocAlreadyExists_Exception e){
			assertEquals("Document already exists", e.getMessage());
		}
	}


	@Test
	public void testStoreUserDoesNotExist() throws Exception{
		testSdStore = new SdStoreImpl();
		
		DocUserPair testPair = new DocUserPair();
		testPair.setUserId(userId1);
		testPair.setDocumentId(docId1);
		
		try{
			testSdStore.store(testPair, docContent1);	
		}catch(UserDoesNotExist_Exception e){
			assertEquals("User does not exist", e.getMessage());
		}
	}
	
	@Test(expected=DocDoesNotExist_Exception.class)
	public void testStoreDocDoesNotExist() 	throws Exception{
		testSdStore = new SdStoreImpl();
		
		DocUserPair testPair = new DocUserPair();
		testPair.setUserId(userId1);
		testPair.setDocumentId(null);
		

		try{
			testSdStore.createDoc(testPair);
			testSdStore.store(testPair, docContent1);	
		}catch(DocDoesNotExist_Exception e){
			assertEquals("Document does not exist", e.getMessage());
		}
		
	}
	
	@Test
	public void testStoreCapacityExceeded() throws Exception{
		testSdStore = new SdStoreImpl();
		
		DocUserPair testPair = new DocUserPair();
		testPair.setUserId(userId1);
		testPair.setDocumentId(docId1);
		
		try{
			testSdStore.createDoc(testPair);
			testSdStore.store(testPair, docContent2);	
		}catch(CapacityExceeded_Exception e){
			assertEquals("Repository capacity exceeded", e.getMessage());
		}
	}
	
	@Test
	public void testListDocsUserDoesNotExist() throws Exception {
		testSdStore = new SdStoreImpl();
		
		try{
			testSdStore.listDocs(userId1);
		}catch(UserDoesNotExist_Exception e){
			assertEquals("User does not exist", e.getMessage());
		}
	}

	@Test
	public void testLoadUserDoesNotExist() throws Exception {
		testSdStore = new SdStoreImpl();
		
		DocUserPair testPair = new DocUserPair();
		testPair.setUserId(userId1);
		testPair.setDocumentId(docId1);

		try{
			testSdStore.load(testPair);
		}catch(UserDoesNotExist_Exception e){
			assertEquals("User does not exist", e.getMessage());
		}
	}

	@Test
	public void testLoadDocDoesNotExist() throws Exception {
		testSdStore = new SdStoreImpl();
		
		DocUserPair testPair = new DocUserPair();
		testPair.setUserId(userId1);
		testPair.setDocumentId(null);
		
		try{
			testSdStore.createDoc(testPair);
			testSdStore.load(testPair);
		}catch(DocDoesNotExist_Exception e){
			assertEquals("Document does not exist", e.getMessage());
		}
	}
}

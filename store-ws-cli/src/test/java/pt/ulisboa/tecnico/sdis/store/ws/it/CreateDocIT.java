package pt.ulisboa.tecnico.sdis.store.ws.it;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
// classes generated by wsimport from WSDL
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;


/**
 * Integration Test suite
 */
public class CreateDocIT extends AbstractStoreIT {

    // tests
    // assertEquals(expected, actual);

    // public void createDoc(CreateDoc parameters) throws
    // DocAlreadyExists_Exception

    @Test
    public void success() throws Exception {
        final DocUserPair docUser = new DocUserPair();
        docUser.setDocumentId("a1");
        docUser.setUserId("alice");

        STORE_CLIENT.createDoc(docUser);
        System.out.println("FIM DO SUCESSO...........");
    }

    @Test
    public void testCreateDocNullUser() throws Exception {
        final DocUserPair docUser = new DocUserPair();
        docUser.setDocumentId("a1");
        docUser.setUserId(null);

        STORE_CLIENT.createDoc(docUser);
    }

    @Test
    public void testCreateDocEmptyUser() throws Exception {
        final DocUserPair docUser = new DocUserPair();
        docUser.setDocumentId("a1");
        docUser.setUserId("");

        STORE_CLIENT.createDoc(docUser);
    }

    @Test
    public void testCreateNullDoc() throws Exception {
        final DocUserPair docUser = new DocUserPair();
        docUser.setDocumentId(null);
        docUser.setUserId("alice");

        STORE_CLIENT.createDoc(docUser);
    }

    @Test
    public void testCreateEmptyDoc() throws Exception {
        final DocUserPair docUser = new DocUserPair();
        docUser.setDocumentId("");
        docUser.setUserId("alice");

        STORE_CLIENT.createDoc(docUser);
    }

    @Test(expected = DocAlreadyExists_Exception.class)
    public void tesCreateDocTwice() throws Exception {
        final DocUserPair docUser = new DocUserPair();
        docUser.setDocumentId("a2");
        docUser.setUserId("alice");
        
        STORE_CLIENT.createDoc(docUser);
        // repeat
        STORE_CLIENT.createDoc(docUser);
    }

}

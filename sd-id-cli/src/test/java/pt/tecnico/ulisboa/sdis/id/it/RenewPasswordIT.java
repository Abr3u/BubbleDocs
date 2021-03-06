package pt.tecnico.ulisboa.sdis.id.it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import pt.tecnico.ulisboa.sdis.id.ws.AuthReqFailed_Exception; // classes generated by wsimport from WSDL
import pt.tecnico.ulisboa.sdis.id.ws.UserDoesNotExist_Exception;

/**
 * Test suite
 */
public class RenewPasswordIT extends AbstractIdIT {

    // tests
    // assertEquals(expected, actual);

    // public void renewPassword(RenewPassword parameters)
    // throws UserDoesNotExist {

    @Test
    public void test() throws Exception {
        // assumes user bruno exists
        final String user = "bruno";
        final byte[] pass = "Bbb2".getBytes();

        // check that user exists and can authenticate
        byte[] credentials = ID_CLIENT.requestAuthentication(user, pass);
        assertNotNull(credentials);

        // change password
        ID_CLIENT.renewPassword(user);
        // password should have changed to a new value

        // user should no longer be able to authenticate with previous password:
        try {
            credentials = ID_CLIENT.requestAuthentication(user, pass);
            fail("old password should no longer work");
        } catch (AuthReqFailed_Exception e) {
            System.out.println("Caught: " + e);
        }
    }

    @Test(expected = UserDoesNotExist_Exception.class)
    public void testUserDoesNotExist() throws Exception {
        ID_CLIENT.renewPassword("userthatdoesnotexist");
    }

    @Test(expected = UserDoesNotExist_Exception.class)
    public void testUserNull() throws Exception {
        ID_CLIENT.renewPassword(null);
    }
    
    @Test(expected = UserDoesNotExist_Exception.class)
    public void testUserEmpty() throws Exception {
        ID_CLIENT.renewPassword("");
    }

}

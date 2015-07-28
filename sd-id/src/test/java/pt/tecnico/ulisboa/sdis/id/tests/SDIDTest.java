package pt.tecnico.ulisboa.sdis.id.tests;

import java.util.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import org.junit.*;

import pt.tecnico.ulisboa.sdis.id.SDIdImpl;
import pt.tecnico.ulisboa.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.InvalidEmail_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.InvalidUser_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.UserAlreadyExists_Exception;
import mockit.*;



public class SDIDTest{
	public String wsUrl = "http://localhost:8080/id-ws/endpoint";
	
	@Before
	public void populate4Test() {
    	
    }
	
	@After
	public void tearDown(){
		SDIdImpl sdAux = new SDIdImpl(wsUrl);
		sdAux.reset();
	}


}
package xml.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import modele.Plan;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class DeserialiseurXMLTest {
	
private Plan plan;
private DeserialiseurXML deserialiseur;
	
	@Before
    public void setUp() throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		plan= new Plan();
		deserialiseur.chargerPlan(plan);
    }

	@Test
	public void testchargerIntersections(){
		
		assertEquals(69, plan.getListeIntersections().get(0).getLatitude());
		assertEquals(49, plan.getListeIntersections().get(0).getLongitude());
		
		assertEquals(173, plan.getListeIntersections().get(1).getLatitude());
		assertEquals(92, plan.getListeIntersections().get(1).getLongitude());

		assertEquals(192, plan.getListeIntersections().get(2).getLatitude());
		assertEquals(88, plan.getListeIntersections().get(2).getLongitude());

		assertEquals(280, plan.getListeIntersections().get(3).getLatitude());
		assertEquals(53, plan.getListeIntersections().get(3).getLongitude());
		
		assertEquals(1, plan.getTronconsParIntersection(0).get(0).getDestination().getId());
		assertEquals(5, plan.getTronconsParIntersection(0).get(1).getDestination().getId());

	}
	
	@Test
	public void testchargerTroncons(){
		
		assertEquals(1, plan.getTronconsParIntersection(0).get(0).getDestination().getId());
		assertEquals(5, plan.getTronconsParIntersection(0).get(1).getDestination().getId());

	}

}

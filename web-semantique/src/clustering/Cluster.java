package clustering;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	
	private List<String> elements;
	
	public Cluster(){
		elements = new ArrayList<String>();
	}
	
	public List<String> getElements(){
		return elements;
	}
	
	public void add(String element){
		elements.add(element);
	}
	
	public void fusion(Cluster autreCluster){
		elements.addAll(autreCluster.getElements());
	}
}

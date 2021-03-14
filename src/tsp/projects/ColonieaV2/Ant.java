
package tsp.projects.ColonieaV2;

//Simple Fourmi
public class Ant {
	//Le tour de la fourmi
	private final int[] tour;
	
	//La liste des villes visitées
	private final boolean[] villeVisite;
	
	private int LongueurTour;
	Ant(int n) {
		tour = new int[n + 1];
		villeVisite = new boolean[n];
		LongueurTour = 0;
	}
	
	
	public int getLongueurTour() {
		return LongueurTour;
	}
	
	
	public void setLongueurTour(int len) {
		LongueurTour = len;
	}
	
	
	public boolean getVisite(int id) {
		return villeVisite[id];
	}
	
	
	public void setVisite(int id, boolean val) {
		villeVisite[id] = val;
		
	}
	
	
	public int getTour(int id) {
		return tour[id];
	}
	
	
	public void setTour(int id, int val) {
		tour[id] = val;
		
	}
	
	
	public int[] getTour() {
		return tour;
	}
}

package tsp.projects.ColonieaV2;


//Optimisation d'un chemin
public class Opt2 {
	//Matrice des distances des villes
	double[][] dist;
	
	
	Opt2(double[][] dist) {
		this.dist = dist;
	}
	
	
	public void opt2(int[] tour) {
		int i;
		double a1, a2, a3, b1, b2, b3;
		int swap;
		for (i = 1; i < tour.length - 2; i++) {
			a1 = dist[tour[i - 1]][tour[i]];
			a2 = dist[tour[i]][tour[i + 1]];
			a3 = dist[tour[i + 1]][tour[i + 2]];
			b1 = dist[tour[i - 1]][tour[i + 1]];
			b2 = dist[tour[i + 1]][tour[i]];
			b3 = dist[tour[i]][tour[i + 2]];
			if (a1 + a2 + a3 > b1 + b2 + b3) {
				swap = tour[i];
				tour[i] = tour[i + 1];
				tour[i + 1] = swap;
			}
		}
	}
	
	
}

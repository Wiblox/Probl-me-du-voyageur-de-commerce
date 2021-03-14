package tsp.projects.ColonieaV2;



import java.util.Random;

public class AntColony {
	//Emplacement Ville
	private final double[][] matriceDistance;
	private final double[][] pheromone;
	private final double[][] matriceDecision;
	
	//Liste Fourmi
	private final Ant[] ants;
	
	//Optimisation ou NON
	private final boolean Opt2;
	
	//StagnationMax avant reset
	private final int maxStagnation;
	
	//Nb fourmi
	private final int tailleAnts;
	
	//Nb de villes
	private final int tailleNodes;
	
	//Variables
	private final double alpha;
	private final double beta;
	private final double ro;
	private double pher0;
	
	
	private int iterations;
	private int[] meilleurChemin;
	
	//Opti
	private Opt2 opt;
	
	
	private double minPheromone;
	private double maxPheromone;
	private int stagnationIterations;
	
	
	public AntColony(int noNodes, int noAnts, double alfa,
                     double beta, double ro,
                     boolean Opt2,
                     int maxStagnationIterations) {
		this.tailleNodes = noNodes;
		this.tailleAnts = noAnts;
		this.alpha = alfa;
		this.beta = beta;
		this.ro = ro;
		this.iterations = 0;
		this.stagnationIterations = 0;
		this.maxStagnation = maxStagnationIterations;
		
		matriceDistance = new double[noNodes][noNodes];
		pheromone = new double[noNodes][noNodes];
		matriceDecision = new double[noNodes][noNodes];
		
		ants = new Ant[noAnts];
		for (int i = 0; i < noAnts; i++) {
			ants[i] = new Ant(noNodes);
		}
		
		
		this.Opt2 = Opt2;
		meilleurChemin = new int[noNodes + 1];
	}
	
	
	public void setDistance(int i, int j, double d) {
		matriceDistance[i][j] = d;
	}
	
	//Recupere le meilleur Tour de toute les fourmis
	public int[] getMeilleurTour() {
		int[] meilleurTour = new int[tailleNodes + 1];
		int maxValue = Integer.MAX_VALUE;
		for (int i = 0; i < tailleAnts; i++) {
			if (ants[i].getLongueurTour() < maxValue) {
				maxValue = ants[i].getLongueurTour();
				for (int j = 0; j <= tailleNodes; j++)
					meilleurTour[j] = ants[i].getTour(j);
			}
		}
		return meilleurTour;
	}
	
	
	// Retourne le dernier tour mais sans la ville de depart à la fin
	public int[] getMeilleurTourALGO() {
		int[] truc = new int[tailleNodes];
		int[] bestTour = getMeilleurTour();
		for (int j = 0; j < tailleNodes; j++) {
			truc[j] = bestTour[j];
		}
		return truc;
	}
	
	
	public int getBestTourLength() {
		return calculLongueurTour(getMeilleurTour());
	}
	
	
	public int[] getMeilleurChemin() {
		return meilleurChemin;
	}
	
	
	public void updateMeilleurTour() {
		if (getBestTourLength() < calculLongueurTour(meilleurChemin)) {
			meilleurChemin = getMeilleurTour();
			stagnationIterations = 0;
		} else
			stagnationIterations++;
	}
	
	
	public int getIteration() {
		return iterations;
	}
	
	
	public void setIteration(int iter) {
		iterations = iter;
	}
	
	
	public void initialisation() {
		int i, j;
		for (i = 0; i < tailleNodes; i++)
			for (j = 0; j < tailleNodes; j++) {
				matriceDistance[i][j] = 0;
				pheromone[i][j] = 0.0;
				matriceDecision[i][j] = 0.0;
			}
		
	}
	
	
	public void initPheromones() {
		int i, j;
		pher0 = calculPheromoneInit();
		
		for (i = 0; i < tailleNodes; i++)
			for (j = 0; j < tailleNodes; j++)
				pheromone[i][j] = pher0;
		
		for (i = 0; i < tailleNodes; i++)
			pheromone[i][i] = 0;
		
		opt = new Opt2(matriceDistance);
		
		maxPheromone = pher0;
		minPheromone = maxPheromone * (1 - Math.pow(0.05, 1.0 / ((double) tailleNodes))) / (tailleNodes);
		
	}
	
	
	public void reinitPheromones() {
		int i, j;
		for (i = 0; i < tailleNodes; i++)
			for (j = 0; j < tailleNodes; j++)
				pheromone[i][j] = pher0;
		for (i = 0; i < tailleNodes; i++)
			pheromone[i][i] = 0;
		maxPheromone = pher0;
		minPheromone = maxPheromone * (1 - Math.pow(0.05, 1.0 / ((double) tailleNodes))) / (tailleNodes);
		
	}
	
	//Calcul avec Alpha Beta
	public void computeHeuristic() {
		double niu;
		int i, j;
		for (i = 0; i < tailleNodes; i++)
			for (j = 0; j < tailleNodes; j++) {
				if (matriceDistance[i][j] > 0)
					niu = 1.0 / matriceDistance[i][j];
				else
					niu = 1.0 / 0.0001;
				matriceDecision[i][j] = Math.pow(pheromone[i][j], alpha) * Math.pow(niu, beta);
			}
	}
	
	
	public void initAnts() {
		int i, j;
		for (i = 0; i < tailleAnts; i++) {
			ants[i].setLongueurTour(0);
			for (j = 0; j < tailleNodes; j++)
				ants[i].setVisite(j, false);
			for (j = 0; j <= tailleNodes; j++)
				ants[i].setTour(j, 0);
		}
	}
	
	
	//Tableau de decision pour une fourmi k à l'etape step
	public void decisionTableaux(int k, int step) {

		
		int c = ants[k].getTour(step - 1);
		double sumProb = 0.0;
		
		double[] selection = new double[tailleNodes];
		
		int j;
		for (j = 0; j < tailleNodes; j++) {
			if ((ants[k].getVisite(j)) || (j == c))
				selection[j] = 0.0;
			else {
				selection[j] = matriceDecision[c][j];
				sumProb += selection[j];
			}
			
		}
		double prob = Math.random() * sumProb;
		j = 0;
		double p = selection[j];
		while (p < prob) {
			j++;
			p += selection[j];
		}
		ants[k].setTour(step, j);
		ants[k].setVisite(j, true);
		
	}
	
	
	public void generateSolutions() {
		initAnts();
		
		int step = 0;
		int k;
		int r;
		
		Random rand = new Random();
		
		for (k = 0; k < tailleAnts; k++) {
			r = Math.abs(rand.nextInt()) % tailleNodes;
			
			ants[k].setTour(step, r);
			ants[k].setVisite(r, true);
		}
		while (step < tailleNodes - 1) {
			step++;
			for (k = 0; k < tailleAnts; k++)
				decisionTableaux(k, step);
		}
		for (k = 0; k < tailleAnts; k++) {
			ants[k].setTour(tailleNodes, ants[k].getTour(0));
			ants[k].setLongueurTour(calculLongueurTour(ants[k].getTour()));
		}
		updateMeilleurTour();
		
	}
	
	//On avapore les pheromones
	public void globalEvaporation() {
		int i, j;
		for (i = 0; i < tailleNodes; i++)
			for (j = 0; j < tailleNodes; j++) {
				pheromone[i][j] = (1 - ro) * pheromone[i][j];
			}
	}
	
	//Depot de pheromone
	public void globalPheromoneDepot() {
		double delta;
		if (iterations % 2 == 0)
			delta = 1.0 / ((double) getBestTourLength());
		else
			delta = 1.0 / ((double) calculLongueurTour(getMeilleurTour()));
		for (int i = 0; i < tailleNodes; i++) {
			int idx1 = meilleurChemin[i];
			int idx2 = meilleurChemin[i + 1];
			if (delta < minPheromone)
				delta = minPheromone;
			if (delta > maxPheromone)
				delta = minPheromone;
			pheromone[idx1][idx2] += delta;
			pheromone[idx2][idx1] += delta;
		}
	}
	
	//Mise à jour de pheromone
	public void updatePheromones() {
		globalEvaporation();
		globalPheromoneDepot();
		
		computeHeuristic();
		maxPheromone = 1 / (ro * ((double) calculLongueurTour(getMeilleurChemin())));
		minPheromone = maxPheromone * (1 - Math.pow(0.05, 1.0 / ((double) tailleNodes))) / (tailleNodes);
		if (stagnationIterations > maxStagnation) {
			reinitPheromones();
			stagnationIterations = 0;
		}
	}
	
	
	private int tourGourmand() {
		boolean[] visited = new boolean[tailleNodes];
		int[] tour = new int[tailleNodes + 1];
		double min;
		int node;
		int i, j;
		
		for (i = 0; i < tailleNodes; i++)
			visited[i] = false;
		
		tour[0] = 0;
		meilleurChemin[0] = 0;
		visited[0] = true;
		
		for (i = 1; i < tailleNodes; i++) {
			min = Integer.MAX_VALUE;
			node = -1;
			for (j = 0; j < tailleNodes; j++) {
				if ((!visited[j]) && (j != tour[i - 1])) {
					if (min > matriceDistance[tour[i - 1]][j]) {
						min = matriceDistance[tour[i - 1]][j];
						node = j;
					}
				}
			}
			tour[i] = node;
			meilleurChemin[i] = node;
			visited[node] = true;
		}
		tour[tailleNodes] = tour[0];
		meilleurChemin[tailleNodes] = meilleurChemin[0];
		return calculLongueurTour(tour);
		
	}
	
	
	public int calculLongueurTour(int[] tour) {
		int len = 0;
		for (int i = 0; i < tailleNodes; i++) {
			len += matriceDistance[tour[i]][tour[i + 1]];
		}
		return len;
	}
	
	
	private double calculPheromoneInit() {
		return 1.0 / (((double) tourGourmand()) * ro);
	}
	
	//Optimisation
	public void localSearch() {
		if (Opt2) {
			for (int i = 0; i < tailleAnts; i++) {
				if (i % 3 == 0)
					opt.opt2(ants[i].getTour());
			}
		}
		updateMeilleurTour();
		if (stagnationIterations > 0)
			stagnationIterations--;
	}
	
	
	public String toString() {
		String str = "";
		for (int i = 0; i < tailleAnts; i++) {
			str = str + "{" + i + "}: ";
			for (int j = 0; j < tailleNodes + 1; j++) {
				str += "" + ants[i].getTour(j) + ", ";
			}
			str += "[ " + calculLongueurTour(ants[i].getTour()) + " ]";
			str += "\n";
		}
		return str;
	}
	
	
}


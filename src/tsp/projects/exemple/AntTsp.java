package tsp.projects.exemple;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Random;

public class AntTsp extends DemoProject {
	private int debut=0;
	private double c = 1.0;
	private double alpha = 1;
	private double beta = 5;
	private double evaporation = 0.8;
	private double Q = 500;
	private double numAntFactor = 0.1;
	private double pr = 0.002;
	
	

	public int n = 0; // # towns
	public int m = 0; // # ants
	private double graph[][] = null;
	private double trails[][] = null;
	private Ant ants[] = null;
	private int rapide[] = null;
	private double distance[] = null;
	
	private Random rand = new Random();
	private double probs[] = null;
	
	private int currentIndex = 0;
	
	public int[] bestTour;
	public double bestTourLength;
	public double bestTourLengthBIS;
	
	
	public AntTsp(Evaluation evaluation) throws InvalidProjectException {
		
		super(evaluation);
		this.addAuthor ("KV & BN");
		this.setMethodName ("ANT");
	}
	
	
	/**
	 * Initialisation de l'algorithme
	 */
	@Override
	public void initialization() {

		readGraph();
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				trails[i][j] = c;
	}
	
	
	/**
	 * Boucle principale de l'algorithme
	 */
	@Override
	public void loop() {
		
		debut++;
		

			setupAnts();
			moveAnts();
			updateTrails();
			updateBest();
		if( bestTourLength!=bestTourLengthBIS){
			System.out.println("Tour = " + bestTourLength +"Iteration = " + debut);
			bestTourLengthBIS=bestTourLength;
		}

		
	}
	public void readGraph()  {
		//construire matrice d'adjacence
		
	
			
			graph = new double[problem.getLength()][problem.getLength()];
			
			for(int i=0;i<=problem.getLength()-1;i++){
			for(int j=0;j<=problem.getLength()-1;j++) {
				if(i==j){
				graph[i][j]=99999;}
				else {graph[i][j]=problem.getCoordinates(i).distance(problem.getCoordinates(j));}
			}
			
			}
		 n = problem.getLength();
		m=10;
		rapide= new int[n];
		

		// all memory allocations done here
		trails = new double[n][n];
		probs = new double[n];
		ants = new Ant[m];
		for (int j = 0; j < m; j++)
			ants[j] = new Ant();
			
			
		
}
		
		// Ant class. Maintains tour and tabu information.
	private class Ant {
		public int tour[] = new int[problem.getLength()];
		// Maintain visited list for towns, much faster
		// than checking if in tour so far.
		public boolean visited[] = new boolean[problem.getLength()];
		
		public void visitTown(int town) {
		
				
				tour[currentIndex + 1] = town;
				
				visited[town] = true;
	
		}
			
			public boolean visited(int i) {
			return visited[i];
		}
		
		public double tourLength() {
			double length = graph[tour[n - 1]][tour[0]];
			for (int i = 0; i < n - 1; i++) {
				length += graph[tour[i]][tour[i + 1]];
			}
			return length;
		}
		
		public void clear() {
			for (int i = 0; i < n; i++)
				visited[i] = false;
		}
	}
	
	
	// Approximate power function, Math.pow is quite slow and we don't need accuracy.
	// See:
	// http://martin.ankerl.com/2007/10/04/optimized-pow-approximation-for-java-and-c-c/
	// Important facts:
	// - >25 times faster
	// - Extreme cases can lead to error of 25% - but usually less.
	// - Does not harm results -- not surprising for a stochastic algorithm.
	public static double pow(final double a, final double b) {
		final int x = (int) (Double.doubleToLongBits(a) >> 32);
		final int y = (int) (b * (x - 1072632447) + 1072632447);
		return Double.longBitsToDouble(((long) y) << 32);
	}
	
	// Store in probs array the probability of moving to each town
	// [1] describes how these are calculated.
	// In short: ants like to follow stronger and shorter trails more.
	private void probTo(Ant ant) {

		
		int i = ant.tour[currentIndex];
		
		double denom = 0.0;
		for (int l = 0; l < n; l++)
			if (!ant.visited(l))
				denom += pow(trails[i][l], alpha)
						* pow(1.0 / graph[i][l], beta);
		
		
		for (int j = 0; j < n; j++) {
			if (ant.visited(j)) {
				probs[j] = 0.0;
			} else {
				double numerator = pow(trails[i][j], alpha)
						* pow(1.0 / graph[i][j], beta);
				probs[j] = numerator / denom;
			}
		}
		}
		
		// Given an ant select the next town based on the probabilities
	// we assign to each town. With pr probability chooses
	// totally randomly (taking into account tabu list).
	private int selectNextTown(Ant ant) {
		
		
			
			// sometimes just randomly select
			if (rand.nextDouble() < pr) {
				
			
				int t = rand.nextInt(n - currentIndex); // random town
				int j = -1;
				for (int i = 0; i < n; i++) {
					if (!ant.visited(i))
						j++;
					if (j == t){
						
						return i;}
				}
				
			}
			
			// calculate probabilities for each town (stored in probs)
			probTo(ant);
			// randomly select according to probs
			double r = rand.nextDouble();
			double tot = 0;
			
			for (int i = 0; i < n; i++) {
				tot += probs[i];
				if (tot >= r){
					
					return i;}
			}
		
			throw new RuntimeException("Not supposed to get here.");
	
	}
		
		// Update trails based on ants tours
	private void updateTrails() {
		

		// evaporation
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				trails[i][j] *= evaporation;
		
		// each ants contribution
		for (Ant a : ants) {
			double contribution = Q / a.tourLength();
			for (int i = 0; i < n - 1; i++) {
				trails[a.tour[i]][a.tour[i + 1]] += contribution;
			}
			trails[a.tour[n - 1]][a.tour[0]] += contribution;
		}
	}
		
		// Choose the next town for all ants
	private void moveAnts() {
	
	
			
			
			// each ant follows trails...
		while (currentIndex < n - 1) {
			for (Ant a : ants)
				a.visitTown(selectNextTown(a));
			currentIndex++;
		}
}
		
		
		// m ants with random start city
	private void setupAnts() {
		
			currentIndex = -1;
		for (int i = 0; i < m; i++) {
			ants[i].clear(); // faster than fresh allocations.
			
			int a = rand.nextInt(n );
			
			ants[i].visitTown(a);
			
		}
		currentIndex++;
			
	
		
	}

	
	private void updateBest() {
		

		if (bestTour == null) {
			bestTour = ants[0].tour;
			bestTourLength = ants[0].tourLength();
		}
			
			for (Ant a : ants) {
			if (a.tourLength() < bestTourLength) {
			
				bestTourLength = a.tourLength();
				bestTour = a.tour.clone();
				Path path = new Path (bestTour);
				
				this.evaluation.evaluate (path);
				
			}
	}}
		
	
	

	
	

}
package tsp.projects.Fusion;


import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

import java.util.Arrays;

/**
 * GeneticTest
 *
 * @author: onlylemi
 */
public class Algo extends DemoProject {
	private static GeneticAntSystemTSP gas;
	private static double eval = 0;
	private static double qeval = 0;
	private static GeneticAlgorithm ga;
	private double getBestDist;
	private int iteration;
	private int stagnationF;
	private int stagnationG;
	private boolean ant;
	private double temp;
	private double coolingRate;
	private double test;
	
	
	public Algo(Evaluation evaluation) throws InvalidProjectException {
		super(evaluation);
		this.addAuthor("KORPYS Vivien & BARILE Nicolas");
		this.setMethodName("ANT & GENETIC ");
	}
	
	
	/**
	 * Initialisation de l'algorithme
	 */
	@Override
	public void initialization() {
		iteration = 0;
		stagnationF = 0;
		stagnationG = 0;
		ant = true;
		boolean doOpt2 = true;
		boolean doOpt3 = true;
		int noAnts = 60;
		double alfa = 0.3;
		double beta = 60;
		double globalEvapRate = 0.8;
		getBestDist = 0;
		
		gas = new GeneticAntSystemTSP(problem.getLength(), noAnts, alfa, beta, globalEvapRate, doOpt2, doOpt3, 1000);
		gas.initData();
		float[][] points = new float[problem.getLength()][problem.getLength()];
		
		for (int i = 0; i <= problem.getLength() - 1; i++) {
			for (int j = 0; j <= problem.getLength() - 1; j++) {
				if (i == j) {
					points[i][j] = (float) 0;
					gas.setDistance(i, j, 0);
				} else {
					points[i][j] = (float) problem.getCoordinates(i).distance(problem.getCoordinates(j));
					
					gas.setDistance(i, j, problem.getCoordinates(i).distance(problem.getCoordinates(j)));
				}
			}
		}
		
		ga = new GeneticAlgorithm();
		int[] best = ga.tsp(points);
		gas.initPheromones();
		gas.computeHeuristic();
		gas.initAnts();
		
		test = 0;
		//Cooling rate
		

		
		// We would like to keep track if the best solution
		// Assume best solution is the current solution
	}
	
	
	/**
	 * Boucle principale de l'algorithme
	 */
	@Override
	public void loop() {
		iteration++;
		
		if (ant) {
			
			gas.constructSolutions();
			gas.localSearch();
			gas.updatePheromones();
			gas.setIteration(gas.getIteration() + 1);
			Path path = new Path(gas.getBestTourAlgo());
			this.evaluation.evaluate(path);
			if (eval != this.evaluation.getBestEvaluation()) {
				System.out.println("\n Iteration FOURMIE = " + gas.getIteration() + " best distance = " + this.evaluation.getBestEvaluation());
				eval = this.evaluation.getBestEvaluation();
				
			}
			if (eval == this.evaluation.getBestEvaluation()) {
				stagnationF++;
			} else {
				stagnationF = 0;
			}
			if (stagnationF > 100000) {
				ant = false;
				stagnationF = 0;
				ga.boost(gas.getBestTourAlgo());
				
				
				
				
			}
			
		} else {
			
			int[] best = ga.nextGeneration();
			Path path = new Path(ga.getBestIndivial());
			this.evaluation.evaluate(path);
			if (qeval != this.evaluation.getBestEvaluation()) {
				System.out.println("\n Iteration GENETIQUE = " + ga.getCurrentGeneration() + " best distance = " + this.evaluation.getBestEvaluation());
				qeval = this.evaluation.getBestEvaluation();
				
			}
			if (qeval == this.evaluation.getBestEvaluation()) {
				stagnationG++;
			} else {
				stagnationG = 0;
			}
			if(stagnationG>3000){
				ant=true;
				stagnationG=0;
				gas.boost(best);
				gas.localSearch();
				gas.updatePheromones();
			}
		}
	}
}


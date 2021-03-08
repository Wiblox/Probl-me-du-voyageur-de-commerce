package tsp.projects.Fusion;


import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

import java.util.ArrayList;

/**
 * GeneticTest
 *
 * @author: onlylemi
 */
public class Algo extends CompetitorProject {
	private static GeneticAntSystemTSP gas;
	private static double eval=0;
	private static GeneticAlgorithm ga;
	private double getBestDist;
	public Algo(Evaluation evaluation) throws InvalidProjectException {
		super(evaluation);
	}
	
	
	
	/**
	 * Initialisation de l'algorithme
	 */
	@Override
	public void initialization() {
		
		boolean doOpt2 = true;
		boolean doOpt3 = true;
		int noAnts = 60;
		double alfa = 0.2;
		double beta = 30;
		double globalEvapRate = 0.8;
		 getBestDist=0;
		
		gas = new GeneticAntSystemTSP(problem.getLength(), noAnts, alfa, beta, globalEvapRate, doOpt2, doOpt3, 1000);
		gas.initData();
		float[][] points = new float[problem.getLength()][problem.getLength()];
		
		for(int i=0;i<=problem.getLength()-1;i++){
			for(int j=0;j<=problem.getLength()-1;j++) {
				if(i==j){
					points[i][j]=(float)0;
					gas.setDistance(i, j, 0);
				}
				else {					points[i][j]=(float)problem.getCoordinates(i).distance(problem.getCoordinates(j));

					gas.setDistance(i, j, problem.getCoordinates(i).distance(problem.getCoordinates(j)));
				}
			}
		}
		
		 ga = new GeneticAlgorithm();
		int[] best = ga.tsp(points);
		gas.initPheromones();
		gas.computeHeuristic();
		gas.initAnts();
		
		
	
	}
	
	
	/**
	 * Boucle principale de l'algorithme
	 */
	@Override
	public void loop() {
		
		gas.constructSolutions();
		gas.localSearch();
		gas.updatePheromones();
		gas.setIteration(gas.getIteration()+1);
		Path path = new Path (gas.getBestTourAlgo());
		this.evaluation.evaluate (path);
		if(eval!=this.evaluation.getBestEvaluation()){
			System.out.println("\n Iteration FOURMIE = " + gas.getIteration()+" best distance = " + this.evaluation.getBestEvaluation());
			eval=this.evaluation.getBestEvaluation();
			
		}
		
		int[] best = ga.nextGeneration();
		path = new Path (ga.getBestIndivial());
		this.evaluation.evaluate (path);
		if (eval != this.evaluation.getBestEvaluation()) {
			System.out.println("\n Iteration GENETIQUE = " + ga.getMutationTimes()+ " best distance = " + this.evaluation.getBestEvaluation());
			eval = this.evaluation.getBestEvaluation();

		}
	
	}
}


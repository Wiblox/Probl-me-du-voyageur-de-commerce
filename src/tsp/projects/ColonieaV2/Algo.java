package tsp.projects.ColonieaV2;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

public class Algo extends CompetitorProject {
	
	private GeneticAntSystemTSP gas;
	private double eval=0;
	
	public Algo(Evaluation evaluation) throws InvalidProjectException {
		super(evaluation);
		this.addAuthor ("KV & BN");
		this.setMethodName ("ANT IS ");
	}
	
	
	@Override
	public void initialization() {
		boolean doOpt2 = true;
		boolean doOpt3 = false;
		int noAnts = 50;
		double alfa = 0.3;
		double beta = 20;
		double globalEvapRate = 0.5;
		gas = new GeneticAntSystemTSP(problem.getLength(), noAnts, alfa, beta, globalEvapRate, doOpt2, doOpt3, 1000, 0);
		gas.initData();
		for(int i=0;i<=problem.getLength()-1;i++){
			for(int j=0;j<=problem.getLength()-1;j++) {
				if(i==j){
					gas.setDistance(i, j, 0);
				}
				else {	gas.setDistance(i, j, problem.getCoordinates(i).distance(problem.getCoordinates(j)));
					}
			}
		}
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
			System.out.println("\n Iteration = " + gas.getIteration()+" valeur = " + this.evaluation.getBestEvaluation());
			eval=this.evaluation.getBestEvaluation();
			
		}
		
	}
}

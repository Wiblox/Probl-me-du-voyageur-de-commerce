package tsp.projects.ColonieaV2;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

public class Algo extends CompetitorProject {
	
	private AntColony colony;
	
	
	public Algo(Evaluation evaluation) throws InvalidProjectException {
		super(evaluation);
		this.addAuthor("KORPYS Vivien & BARILE Nicolas");
		this.setMethodName("ANT IS THE BEST");
	}
	
	
	@Override
	public void initialization() {
		boolean doOpt2 = true;
		int noAnts = 200;
		double alfa = 0.2;
		double beta = 30;
		double globalEvapRate = 0.5;
		colony = new AntColony(problem.getLength(), noAnts, alfa, beta, globalEvapRate, doOpt2, 1000);
		colony.initialisation();
		for (int i = 0; i <= problem.getLength() - 1; i++)
			for (int j = 0; j <= problem.getLength() - 1; j++) {
				if (i == j) {
					colony.setDistance(i, j, 0);
				} else {
					colony.setDistance(i, j, problem.getCoordinates(i).distance(problem.getCoordinates(j)));
				}
			}
		
		
		colony.initPheromones();
		colony.computeHeuristic();
		colony.initAnts();
	}
	
	

	@Override
	//Boucle Principale
	public void loop() {
		colony.generateSolutions();
		colony.localSearch();
		colony.updatePheromones();
		colony.setIteration(colony.getIteration() + 1);
		Path path = new Path(colony.getMeilleurTourALGO());
		this.evaluation.evaluate(path);

		
	}
}

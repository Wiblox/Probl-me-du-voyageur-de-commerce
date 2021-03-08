package tsp.projects.GeneticAlgorithm;


import tsp.evaluation.Problem;

import java.util.ArrayList;

/**
 * GeneticTest
 *
 * @author: onlylemi
 */
public class GeneticTest {
	
	public static void main(String[] args) {
		ArrayList<Problem> problems = Problem.getProblems();
		
		Problem test = problems.get(1);
		
		Point[] points = new Point[test.getLength()];
		
		
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point();
			points[i].x = test.getCoordinates(i).getX();
			points[i].y = test.getCoordinates(i).getY();
		}
		
		int[] best;
		
		//=======================method 1=======================
		GeneticAlgorithm ga = new GeneticAlgorithm();
		best = ga.tsp(getDist(points));
		
		long timeStart = System.currentTimeMillis();
		long time = System.currentTimeMillis();
		long timeEnd = System.currentTimeMillis() + 60000;
		float getBestDist = 0;
		
		while (time < timeEnd) {
			best = ga.nextGeneration();
			if (ga.getBestDist() != getBestDist) {
				System.out.println("best distance:" + ga.getBestDist() +
						" current generation:" + ga.getCurrentGeneration() +
						" mutation times:" + ga.getMutationTimes());
				getBestDist = ga.getBestDist();
				System.out.println("Temps écoulé : " + (time - timeStart) / 1000 + "sec");
				time = System.currentTimeMillis();
			}
		}
		
		//=======================method 2========================
        /*GeneticAlgorithm ga = GeneticAlgorithm.getInstance();

        ga.setMaxGeneration(1000);
        ga.setAutoNextGeneration(true);
        best = ga.tsp(getDist(points));
        System.out.print("best path:");
        for (int i = 0; i < best.length; i++) {
            System.out.print(best[i] + " ");
        }*/
		
		
		System.out.println("Meilleure distance = " + ga.getBestDist());
	}
	
	
	private static float[][] getDist(Point[] points) {
		float[][] dist = new float[points.length][points.length];
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points.length; j++) {
				dist[i][j] = distance(points[i], points[j]);
			}
		}
		return dist;
	}
	
	
	private static float distance(Point p1, Point p2) {
		return (float) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
	}
}


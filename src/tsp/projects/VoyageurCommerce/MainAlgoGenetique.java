package tsp.projects.VoyageurCommerce;

import tsp.evaluation.Problem;

import java.util.ArrayList;

public class MainAlgoGenetique {

    public final static int nbEvolution = 100;

    public static void main (String [] args)
    {
        ArrayList<Problem> problems = Problem.getProblems ();

        Problem test = problems.get(3);

        GestionnaireVilles gc = new GestionnaireVilles(test);

        Population pop = new Population(gc, 50, true);
        System.out.println("\nDistance initiale = " + pop.getFittest().getDistance() );

        AlgorithmeGenetique ga = new AlgorithmeGenetique(gc);

        for(long i = 0; i < nbEvolution; i++) {
            pop = ga.evoluerPopulation(pop);
        }

        /*
        long timeStart = System.currentTimeMillis();
        long time = System.currentTimeMillis();
        long timeEnd = System.currentTimeMillis() + 60000;
        while (time < timeEnd) {
            pop = ga.evoluerPopulation(pop);
            if(((time - timeStart) % 1000) == 0 )
                System.out.println("Distance a " + (time - timeStart)/1000 + "sec = " + pop.getFittest().getDistance());
            time = System.currentTimeMillis();
        }*/

        System.out.println("Distance finale = " + pop.getFittest().getDistance());


    }
}

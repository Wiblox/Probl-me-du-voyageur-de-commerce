package tsp.projects.AlgorithmeGenetique;

import tsp.evaluation.Coordinates;

import java.util.Random;

public class AlgorithmeGenetique {

    public GestionnaireVilles gestionnaireVilles;

    public final double tauxMutation = 0.015;
    public final int tailleTournoi = 5;
    public final int populationSauvegarder = 1;

    public AlgorithmeGenetique(GestionnaireVilles gestionnaireVilles) {
        this.gestionnaireVilles = gestionnaireVilles;
    }

    public Population evoluerPopulation(Population pop) {
        Population nouvellePopulation = new Population(gestionnaireVilles, pop.taillePopulation(), false);

        //nouvellePopulation.sauvegarderCircuit(0, pop.getFittest());

        for(int u = 0; u < populationSauvegarder; u++) {
            nouvellePopulation.sauvegarderCircuit(u, pop.getFittest());
        }

        for(int i = populationSauvegarder; i < nouvellePopulation.taillePopulation(); i++) {
            Circuit parent1 = selectionTournois(pop);
            Circuit parent2 = selectionTournois(pop);

            Circuit enfant = crossover(parent1, parent2);

            nouvellePopulation.sauvegarderCircuit(i, enfant);
        }

        for(int j = populationSauvegarder; j < nouvellePopulation.taillePopulation(); j++) {
            muter(nouvellePopulation.getCircuits(j));
        }

        //for(int k = nouvellePopulation.taillePopulation() - populationSauvegarder; k < nouvellePopulation.taillePopulation(); k++){
        //    nouvellePopulation.sauvegarderCircuit(k, nouvellePopulation.populationAlea());
        //}

        return nouvellePopulation;
    }

    public Circuit crossover(Circuit parent1, Circuit parent2) {
        Circuit enfant = new Circuit(gestionnaireVilles);

        int startPos = (int)(new Random().nextDouble()  * parent1.lengthCircuit());
        int endPos = (int)(new Random().nextDouble() * parent1.lengthCircuit());

        for(int i = 0; i < enfant.lengthCircuit(); i++) {
            if( startPos < endPos && i > startPos && i < endPos)
                enfant.setVille(i, parent1.getVille(i));
            else if(startPos > endPos) {
                if (!(i < startPos && i > endPos))
                    enfant.setVille(i, parent1.getVille(i));
            }
        }

        for(int j = 0; j < parent2.lengthCircuit(); j++) {
            if ( !( enfant.contientVille(parent2.getVille(j)) ) ){
                for(int k = 0; k < enfant.lengthCircuit(); k++){
                    if(enfant.getVille(k) == null) {
                        enfant.setVille(k, parent2.getVille(j));
                        break;
                    }
                }
            }
        }

        return enfant;
    }

    public void muter(Circuit circuit) {
        for(int pos1 = 0; pos1 < circuit.lengthCircuit(); pos1++){
            if( new Random().nextDouble() < tauxMutation ){
                int pos2 = (int)(circuit.lengthCircuit() * new Random().nextDouble());

                Coordinates ville1 = circuit.getVille(pos1);
                Coordinates ville2 = circuit.getVille(pos2);

                circuit.setVille(pos2, ville1);
                circuit.setVille(pos1, ville2);
            }
        }
    }

    public Circuit selectionTournois(Population pop) {
        Population tournoi = new Population(gestionnaireVilles, tailleTournoi, false);

        for(int i = 0; i < tailleTournoi; i++) {
            int randomId = (int)(new Random().nextDouble() * pop.taillePopulation());
            tournoi.sauvegarderCircuit(i, pop.getCircuits(randomId));
        }

        return tournoi.getFittest();
    }
}

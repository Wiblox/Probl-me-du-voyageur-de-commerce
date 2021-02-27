package tsp.projects.VoyageurCommerce;

import java.util.ArrayList;

public class Population {

    public ArrayList<Circuit> circuits;

    int taillePopulation;

    public Population(GestionnaireVilles gestionnaireVilles, int taillePop, boolean init) {
        this.circuits = new ArrayList<>(gestionnaireVilles.listeVilles.size());
        this.taillePopulation = taillePop;

        if (init) {
            Circuit nouveauCircuit;
            for(int i = 0; i < taillePopulation; i++) {
                nouveauCircuit = new Circuit(gestionnaireVilles);
                nouveauCircuit.generateIndividu();
                sauvegarderCircuit(i,nouveauCircuit);
            }
        }
    }

    public void sauvegarderCircuit(int index, Circuit circuit) {
        circuits.add(index,circuit);
    }

    public Circuit getCircuits(int i) {
        return circuits.get(i);
    }

    public int taillePopulation() {
        return circuits.size();
    }

    public Circuit getFittest() {
        Circuit fittest = circuits.get(0);
        for(int i = 0; i < taillePopulation(); i++) {
            if(fittest.getFitness() <= getCircuits(i).getFitness())
                fittest = getCircuits(i);
        }
        return  fittest;
    }

}

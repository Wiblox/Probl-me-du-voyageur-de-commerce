package tsp.projects.VoyageurCommerce;

import tsp.evaluation.Coordinates;

import java.util.ArrayList;

public class Population {

    public ArrayList<Circuit> circuits = new ArrayList<>();

    public GestionnaireVilles gestionnaireVilles;

    public Population(GestionnaireVilles gestionnaireVilles, int taillePop, boolean init) {

        this.gestionnaireVilles = gestionnaireVilles;

        for(int i = 0; i < taillePop; i++)
            circuits.add(null);

        if (init) {
            Circuit nouveauCircuit;
            for(int i = 0; i < taillePop; i++) {
                nouveauCircuit = new Circuit(gestionnaireVilles);
                nouveauCircuit.generateIndividu();
                sauvegarderCircuit(i,nouveauCircuit);
            }
        }
    }

    public Circuit populationAlea() {
        Circuit circuit = new Circuit(gestionnaireVilles);
        circuit.generateIndividu();

        return circuit;
    }

    public void sauvegarderCircuit(int index, Circuit circuit) {
        circuits.set(index, circuit);
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

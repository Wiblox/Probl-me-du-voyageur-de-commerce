package tsp.projects.VoyageurCommerce;

import tsp.evaluation.Coordinates;

import java.util.ArrayList;
import java.util.Collections;

public class Circuit {

    //Création de deux liste de coordonée, correspondant au position de chaque villes.
    public GestionnaireVilles gestionnaireVilles;

    public ArrayList<Coordinates> circuit;

    public double fitness;
    public int distance;

    //constructeur par défaut, qui créer une liste vide de la taille de Villes.
    public Circuit(GestionnaireVilles gestionnaireVilles) {
        this.gestionnaireVilles = gestionnaireVilles;
        this.fitness = 0.0;
        this.distance = 0;

        this.circuit = new ArrayList<>(gestionnaireVilles.listeVilles.size());
    }

    //Constructeur qui permet de récupérer le précédent circuit.
    public Circuit(GestionnaireVilles gestionnaireVilles, ArrayList<Coordinates> circuit) {
        this.gestionnaireVilles = gestionnaireVilles;
        this.fitness = 0.0;
        this.distance = 0;

        this.circuit = circuit;
    }

    public int lengthCircuit() { return circuit.size(); }

    public Coordinates getVille(int posCircuit) { return circuit.get(posCircuit); }

    public void setVilleCircuit(Coordinates ville, int i) { circuit.add(i, ville); }

    public void setVille(int posCircuit, Coordinates ville) {
        circuit.add(posCircuit, ville);
        this.fitness = 0.0;
        this.distance = 0;
    }

    public void generateIndividu() {
        for(int i = 0; i < gestionnaireVilles.nombreVilles(); i++){
            setVille(i,gestionnaireVilles.getVille(i));
        }
        Collections.shuffle(circuit);
    }

    public double getFitness() {
        if (fitness == 0)
            fitness = 1/(float)(getDistance());
        return fitness;
    }

    public boolean contientVille(Coordinates ville) {
        return circuit.contains(ville);
    }

    public int getDistance() {
        if (distance == 0) {
            int circuitDistance = 0;
            Coordinates villeOrigine = null;
            Coordinates villeArrivee = null;

            for(int i = 0; i < lengthCircuit(); i++) {
                villeOrigine = getVille(i);
                villeArrivee = null;
                if(i + 1 < lengthCircuit())
                    villeArrivee = getVille(i+1);
                else
                    villeArrivee = getVille(0);
                circuitDistance += villeOrigine.distance(villeArrivee);
            }
        }
        return distance;
    }

}

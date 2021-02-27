package tsp.projects.VoyageurCommerce;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Problem;

import java.util.ArrayList;

public class GestionnaireVilles {

    //Liste de coordonnée correspondant à la position de chaque ville
    public ArrayList<Coordinates> listeVilles = new ArrayList<>();

    public Problem problem;

    public GestionnaireVilles(Problem problem) {
        this.problem = problem;

        setGestionnaire();
    }

    public Coordinates getVille(int i){
        return listeVilles.get(i);
    }

    public void setGestionnaire() {
        for(int i = 0; i < problem.getLength(); i++) listeVilles.add(problem.getCoordinates(i));
    }

    public int nombreVilles(){
        return listeVilles.size();
    }
}

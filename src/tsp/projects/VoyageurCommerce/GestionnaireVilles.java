package tsp.projects.VoyageurCommerce;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Problem;

import java.util.ArrayList;

public class GestionnaireVilles {

    //Liste de coordonnée correspondant à la position de chaque ville
    public ArrayList<Coordinates> gestionnaireVilles;

    public Problem problem;

    public GestionnaireVilles(Problem problem) {
        this.problem = problem;
        this.gestionnaireVilles = new ArrayList<Coordinates>(0);

        setGestionnaire();
    }

    public Coordinates getVille(int i){
        return gestionnaireVilles.get(i);
    }

    public void setGestionnaire() {
        for(int i = 0; i < problem.getLength(); i++) gestionnaireVilles.add(problem.getCoordinates(i));
    }

    public int nombreVilles(){
        return gestionnaireVilles.size();
    }
}

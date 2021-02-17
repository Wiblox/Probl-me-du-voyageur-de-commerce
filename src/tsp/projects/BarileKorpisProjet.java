package tsp.projects;

import tsp.evaluation.Evaluation;

public class BarileKorpisProjet extends Project{
    /**
     * Constructeur
     *
     * @param evaluation Méthode d'évaluation de la solution
     * @throws InvalidProjectException
     */
    public BarileKorpisProjet(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor ("Nicolas Barile");
        this.addAuthor ("Vivien Korpis");
        this.setMethodName ("Premier Algo de Recherche");
    }

    @Override
    public void initialization() {

    }

    @Override
    public void loop() {

    }
}
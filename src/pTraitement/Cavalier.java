package pTraitement;
import pTraitement.Plateau;

import java.util.List;
import java.util.ArrayList;

public class Cavalier extends Piece {

    public Cavalier(Couleur couleur, Position position) {
        super(couleur, position);
    }

    @Override
    public List<Position> deplacementsPossibles(Plateau plateau) {
        List<Position> deplacements = new ArrayList<>();
        int[][] mouvements = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] mouvement : mouvements) {
            Position nouvellePosition = getPosition().decaler(mouvement[0], mouvement[1]);
            if (plateau.estCaseVide(nouvellePosition)) {
                deplacements.add(nouvellePosition);
            }
        }
        return deplacements;
    }

    @Override
    public boolean peutCapturer(Position cible, Plateau plateau) {
        int[][] mouvements = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
        
        for (int[] mouvement : mouvements) {
            Position nouvellePosition = getPosition().decaler(mouvement[0], mouvement[1]);
            if (nouvellePosition.equals(cible)) {
                Piece pieceCible = plateau.getPiece(cible);
                return pieceCible != null && pieceCible.getCouleur() != this.getCouleur();
            }
        }
        return false;
    }
}
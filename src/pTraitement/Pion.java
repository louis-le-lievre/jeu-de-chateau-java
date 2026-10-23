package pTraitement;
import java.io.Serializable;
import pTraitement.Piece;

/*
 * COUNILH QUENTIN LE LIEVRE LOUIS THOUVENIN AMELIE
 * jeu de chateau
 * rapport intermédiraire mars 2025
 */

public class Pion extends Piece implements Serializable {
    private static final long serialVersionUID = 1L;
    public Pion(Couleur couleur, Position position) {
        super(couleur, position);
    }

    public List<Position> deplacementsPossibles(Plateau plateau) {
        List<Position> deplacements = new ArrayList<>();
        int[][] mouvements = {
            {1, 0}, {1, 1}, {1, -1},
            {-1, 0}, {-1, 1}, {-1, -1}
        };

        for (int[] mouvement : mouvements) {
            Position nouvellePosition = position.decaler(mouvement[0], mouvement[1]);
            if (plateau.estCaseVide(nouvellePosition)) {
                deplacements.add(nouvellePosition);
            }
        }
        return deplacements;
    }

    public boolean peutCapturer(Position cible, Plateau plateau) {
        // Vérifier si une capture est possible en sautant par-dessus une pièce
        return plateau.estCaptureValide(this, cible);
    }
}

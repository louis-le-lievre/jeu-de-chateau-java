package pTraitement;
/*
 * COUNILH QUENTIN LE LIEVRE LOUIS THOUVENIN AMELIE
 * jeu de chateau
 * rapport intermédiraire mars 2025
 */

public abstract class Piece {
    protected Couleur couleur; // Couleur de la pièce
    protected Position position; // Position actuelle de la pièce

    public Piece(Couleur couleur, Position position) {
        this.couleur = couleur;
        this.position = position;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public abstract List<Position> deplacementsPossibles(Plateau plateau);

    public abstract boolean peutCapturer(Position cible, Plateau plateau);
}

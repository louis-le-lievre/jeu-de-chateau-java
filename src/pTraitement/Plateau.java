package pTraitement;
import pTraitement.Joueur;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/*
 * COUNILH QUENTIN LE LIEVRE LOUIS THOUVENIN AMELIE
 * jeu de chateau
 * rapport intermédiraire mars 2025
 */

public class Plateau implements Serializable {
    private static final long serialVersionUID = 1L; // 🔹 Assure la compatibilité des versions
    public static final int LIGNES = 13;
    public static final int COLONNES = 7;
    public static final char[] COLONNE_LABELS = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
    private String[][] plateau; // Rendre le tableau non statique

    // Codes ANSI pour les couleurs
    public static final String RESET = "\u001B[0m";   // Réinitialisation de couleur
    public static final String BLANC = "\u001B[47m  "; 
    public static final String GRIS = "\u001B[100m  "; 
    public static final String VIDE = "  ";            // Vide 

    // Pièces en Unicode
    public static final String CAVALIER_BLANC = "CB"; // Cavalier blanc 
    public static final String PION_BLANC = "PB";     // Pion blanc 
    public static final String CAVALIER_NOIR = "CN";  // Cavalier noir
    public static final String PION_NOIR = "PN";      // Pion noir 

    public Plateau() {
        this.plateau = new String[LIGNES][COLONNES]; // Initialiser le tableau
        genererPlateau();
        placerPieces();
    }

    public int getLIGNES() {
        return LIGNES;
    }
    
    public int getCOLONNES() {
        return COLONNES;
    }

    public static String getVIDE() {
        return VIDE;
    }

    public static String getBLANC() {
        return BLANC;
    }

    public static String getGRIS() {
        return GRIS;
    }

    public static char[] getColonneLabels() {
        return COLONNE_LABELS;
    }

    public String getCase(int ligne, int colonne) {
        // Vérifie que les coordonnées sont valides
        if (ligne >= 0 && ligne < plateau.length && colonne >= 0 && colonne < plateau[0].length) {
            return plateau[ligne][colonne];
        } else {
            throw new IllegalArgumentException("Coordonnées invalides : (" + ligne + ", " + colonne + ")");
        }
    }

    private void genererPlateau() {
        for (int i = 0; i < LIGNES; i++) {
            for (int j = 0; j < COLONNES; j++) {
                String position = COLONNE_LABELS[j] + String.valueOf(i + 1);

                // Cases spécifiques maintenant vides
                if (position.equals("A13") || position.equals("A12") || position.equals("A11") ||
                    position.equals("B13") || position.equals("B12") || position.equals("C13") ||
                    position.equals("E13") || position.equals("F13") || position.equals("G13") ||
                    position.equals("F12") || position.equals("G12") || position.equals("G11") ||
                    position.equals("A1") || position.equals("B1") || position.equals("C1") ||
                    position.equals("A2") || position.equals("A3") || position.equals("B2") ||
                    position.equals("E1") || position.equals("F1") || position.equals("G1") ||
                    position.equals("F2") || position.equals("G2") || position.equals("G3")) {
                    plateau[i][j] = VIDE;  // Anciennement blanc, maintenant vide
                } else if (position.equals("D1") || position.equals("D13")) {
                    plateau[i][j] = BLANC;   // Anciennement gris, maintenant blanc
                } else if ((i + j) % 2 == 0) {
                    plateau[i][j] = GRIS;    // Anciennement noir, maintenant gris
                } else {
                    plateau[i][j] = BLANC;    // Anciennement gris, maintenant blanc
                }
            }
        }
    }

    private void placerPieces() {
        // Pièces blanches
        plateau[3][2] = CAVALIER_BLANC; // Cavalier blanc en C4
        plateau[3][4] = CAVALIER_BLANC; // Cavalier blanc en E4
        plateau[4][1] = PION_BLANC;     // Pion blanc en B5
        plateau[4][2] = PION_BLANC;     // Pion blanc en C5
        plateau[4][3] = PION_BLANC;     // Pion blanc en D5
        plateau[4][4] = PION_BLANC;     // Pion blanc en E5
        plateau[4][5] = PION_BLANC;     // Pion blanc en F5

        // Pièces noires
        plateau[9][2] = CAVALIER_NOIR; // Cavalier noir en C10
        plateau[9][4] = CAVALIER_NOIR; // Cavalier noir en E10
        plateau[8][1] = PION_NOIR;     // Pion noir en B9
        plateau[8][2] = PION_NOIR;     // Pion noir en C9
        plateau[8][3] = PION_NOIR;     // Pion noir en D9
        plateau[8][4] = PION_NOIR;     // Pion noir en E9
        plateau[8][5] = PION_NOIR;     // Pion noir en F9
    }

    public boolean estDeplacementValide(int ligne, int colonne, int nouvelleLigne, int nouvelleColonne) {
        // Vérification des limites du plateau
        if (nouvelleLigne < 0 || nouvelleLigne >= LIGNES || nouvelleColonne < 0 || nouvelleColonne >= COLONNES) {
            return false;
        }
    
        String piece = plateau[ligne][colonne];
        if (piece.equals(VIDE)) {
            return false;
        }
    
        // Vérification du déplacement d'une case dans n'importe quelle direction
        if (Math.abs(nouvelleLigne - ligne) <= 1 && Math.abs(nouvelleColonne - colonne) <= 1 &&
            !(nouvelleLigne == ligne && nouvelleColonne == colonne)) {
            // Vérifier que la case cible n'est pas occupée par une autre pièce
            String destination = plateau[nouvelleLigne][nouvelleColonne];
            return !destination.equals("CB") && !destination.equals("PB") &&
                   !destination.equals("CN") && !destination.equals("PN");
        }
    
        // Vérification du déplacement en "L" pour un cavalier
        if (piece.equals(CAVALIER_BLANC) || piece.equals(CAVALIER_NOIR)) {
            int deltaLigne = Math.abs(nouvelleLigne - ligne);
            int deltaColonne = Math.abs(nouvelleColonne - colonne);
    
            if ((deltaLigne == 2 && deltaColonne == 1) || (deltaLigne == 1 && deltaColonne == 2)) {
                // Vérifier que la case cible n'est pas occupée par une autre pièce
                String destination = plateau[nouvelleLigne][nouvelleColonne];
                if (!destination.equals("CB") && !destination.equals("PB") &&
                    !destination.equals("CN") && !destination.equals("PN")) {
                    // Vérification des cases intermédiaires (pour le déplacement en "L")
                    if (deltaLigne == 2) {
                        // Si le cavalier se déplace de 2 cases en ligne, vérifier la case intermédiaire
                        int intermediaireLigne = (ligne + nouvelleLigne) / 2;
                        if (plateau[intermediaireLigne][colonne].equals("CB") || plateau[intermediaireLigne][colonne].equals("PB") ||
                            plateau[intermediaireLigne][colonne].equals("CN") || plateau[intermediaireLigne][colonne].equals("PN")) {
                            return false; // Si la case intermédiaire contient une pièce, mouvement invalide
                        }
                    } else if (deltaColonne == 2) {
                        // Si le cavalier se déplace de 2 cases en colonne, vérifier la case intermédiaire
                        int intermediaireColonne = (colonne + nouvelleColonne) / 2;
                        if (plateau[ligne][intermediaireColonne].equals("CB") || plateau[ligne][intermediaireColonne].equals("PB") ||
                            plateau[ligne][intermediaireColonne].equals("CN") || plateau[ligne][intermediaireColonne].equals("PN")) {
                            return false; // Si la case intermédiaire contient une pièce, mouvement invalide
                        }
                    }
                    return true; // Le mouvement est valide
                }
            }
        }
    
        return false; // Le mouvement n'est pas valide
    }                                

    public boolean estCaptureValide(int ligneDepart, int colonneDepart, int ligneArrivee, int colonneArrivee, Joueur joueurCourant) {
        // Vérifier si les coordonnées de destination sont valides
        if (ligneArrivee < 0 || ligneArrivee >= LIGNES || colonneArrivee < 0 || colonneArrivee >= COLONNES) {
            return false;
        }
    
        // Vérifier si la case d'arrivée est interdite
        String position = String.valueOf(COLONNE_LABELS[colonneArrivee]) + (ligneArrivee + 1);
        if (position.equals("A13") || position.equals("A12") || position.equals("A11") ||
            position.equals("B13") || position.equals("B12") || position.equals("C13") ||
            position.equals("E13") || position.equals("F13") || position.equals("G13") ||
            position.equals("F12") || position.equals("G12") || position.equals("G11") ||
            position.equals("A1") || position.equals("B1") || position.equals("C1") ||
            position.equals("A2") || position.equals("A3") || position.equals("B2") ||
            position.equals("E1") || position.equals("F1") || position.equals("G1") ||
            position.equals("F2") || position.equals("G2") || position.equals("G3")) {
            return false;
        }
    
        // Vérifier si la case d'arrivée est occupée par une pièce
        String caseArrivee = plateau[ligneArrivee][colonneArrivee];
        if (caseArrivee != null && (caseArrivee.equals("PB") || caseArrivee.equals("CB") || caseArrivee.equals("PN") || caseArrivee.equals("CN"))) {
            return false;
        }
    
        // Vérification du saut : trouver la pièce entre la position de départ et d'arrivée
        int intermediaireLigne = (ligneDepart + ligneArrivee) / 2;
        int intermediaireColonne = (colonneDepart + colonneArrivee) / 2;
        String pieceSautee = plateau[intermediaireLigne][intermediaireColonne];
    
        // Vérifier que la pièce sautée appartient à l'adversaire
        if ((joueurCourant.getCouleur().equals("Blanc") && (pieceSautee.equals("PN") || pieceSautee.equals("CN"))) ||
            (joueurCourant.getCouleur().equals("Noir") && (pieceSautee.equals("PB") || pieceSautee.equals("CB")))) {
            return true;
        }
        return false;
    }    
                                           
    public void deplacerPiece(int ligneDepart, int colonneDepart, int ligneArrivee, int colonneArrivee) {
        String piece = plateau[ligneDepart][colonneDepart];
        plateau[ligneDepart][colonneDepart] = obtenirCouleurOrigine(ligneDepart, colonneDepart);
        plateau[ligneArrivee][colonneArrivee] = piece;
    }   

    public void capturerPiece(int ligne, int colonne, int cibleLigne, int cibleColonne) {
        int intermediaireLigne = (ligne + cibleLigne) / 2;
        int intermediaireColonne = (colonne + cibleColonne) / 2;
    
        // Supprimer la pièce capturée et réinitialiser la case
        plateau[intermediaireLigne][intermediaireColonne] = obtenirCouleurOrigine(intermediaireLigne, intermediaireColonne);
        
        // Déplacer la pièce qui capture
        deplacerPiece(ligne, colonne, cibleLigne, cibleColonne);
    }

    public List<int[]> trouverCapturesPossibles(int ligne, int colonne, Joueur joueurCourant) { 
        List<int[]> capturesPossibles = new ArrayList<>();
        
        // Vérifier si la case contient une pièce
        String piece = this.plateau[ligne][colonne];
        if (piece == null || piece.equals(Plateau.VIDE)) {
            return capturesPossibles;
        }
    
        // Déplacements diagonaux pour captures (dames)
        int[] deltas = {-2, 2};
        
        for (int deltaLigne : deltas) {
            for (int deltaColonne : deltas) {
                int nouvelleLigne = ligne + deltaLigne;
                int nouvelleColonne = colonne + deltaColonne;
    
                if (estCaptureValide(ligne, colonne, nouvelleLigne, nouvelleColonne, joueurCourant)) {
                    capturesPossibles.add(new int[]{nouvelleLigne, nouvelleColonne});
                }
            }
        }
        
        // Vérifier aussi les captures verticales et horizontales
        int[] verticalHorizontalDeltas = {-2, 2};
        
        for (int delta : verticalHorizontalDeltas) {
            int nouvelleLigne = ligne + delta;
            if (estCaptureValide(ligne, colonne, nouvelleLigne, colonne, joueurCourant)) {
                capturesPossibles.add(new int[]{nouvelleLigne, colonne});
            }
    
            int nouvelleColonne = colonne + delta;
            if (estCaptureValide(ligne, colonne, ligne, nouvelleColonne, joueurCourant)) {
                capturesPossibles.add(new int[]{ligne, nouvelleColonne});
            }
        }
        return capturesPossibles;
    }
                    

    public List<int[]> trouverCapturesPossibles(Joueur joueur) {
        List<int[]> captures = new ArrayList<>();
        // Logique pour détecter les captures valides
        return captures;
    }

    public List<int[]> trouverDeplacementsValides(Joueur joueur) {
        List<int[]> deplacements = new ArrayList<>();
        // Logique pour détecter les déplacements valides
        return deplacements;
    }
            
    public boolean possedeCavalier(String couleur) {
        for (int i = 0; i < LIGNES; i++) {
            for (int j = 0; j < COLONNES; j++) {
                String piece = plateau[i][j];
                System.out.println("Case (" + i + ", " + j + ") contient : " + piece);
                if (piece != null && piece.equals("C" + couleur)) { // Vérifie qu'il s'agit bien d'un cavalier de la bonne couleur
                    return true;
                }
            }
        }
        return false;
    }       

    public boolean estCavalier(String couleur, int ligne, int colonne) {
        if (ligne < 0 || ligne >= LIGNES || colonne < 0 || colonne >= COLONNES) {
            return false;
        }
        String piece = plateau[ligne][colonne];
        // Vérifier si la pièce est un cavalier de la couleur du joueur
        return (couleur.equals("Blanc") && piece.equals("CB")) ||
               (couleur.equals("Noir") && piece.equals("CN"));
    }


    public void setCaseVide(int ligne, int colonne) {
        plateau[ligne][colonne] = VIDE;
    }

    public void afficherPlateau() {
        System.out.println("   │ A │ B │ C │ D │ E │ F │ G │");
        System.out.println("───┼───┼───┼───┼───┼───┼───┼───┤");
        for (int i = 0; i < LIGNES; i++) {
            String ligneAffichage = (i + 1 < 10 ? " " : "") + (i + 1) + " │ ";
            for (int j = 0; j < COLONNES; j++) {
                ligneAffichage += String.format("%-2s", plateau[i][j]) + RESET + "│ ";
            }
            System.out.println(ligneAffichage);
            if (i < LIGNES - 1) {
                System.out.println("───┼───┼───┼───┼───┼───┼───┼───┤");
            }
        }
        System.out.println("───┴───┴───┴───┴───┴───┴───┴───┘");
    }

    public static void main(String[] args) {
        Plateau p = new Plateau();
        p.afficherPlateau();
    }

    // Méthodes d'accès (getters)
    public String[][] getPlateau() {
        return plateau;
    }  

    public int getLignes() {
        return LIGNES;
    }

    public int getColonnes() {
        return COLONNES;
    }

    public String getPiece(int ligne, int colonne) {
        if (ligne < 0 || ligne >= LIGNES || colonne < 0 || colonne >= COLONNES) {
            throw new IllegalArgumentException("Position invalide sur le plateau.");
        }
        return plateau[ligne][colonne];
    }

    // Méthode pour définir l'état du plateau à partir d'une sauvegarde
    public void setPlateau(String[][] nouveauPlateau) {
        for (int i = 0; i < LIGNES; i++) {
            for (int j = 0; j < COLONNES; j++) {
                this.plateau[i][j] = nouveauPlateau[i][j];
            }
        }
    }

    // Méthode pour obtenir la couleur d'origine d'une case
    public String obtenirCouleurOrigine(int ligne, int colonne) {
        String position = COLONNE_LABELS[colonne] + String.valueOf(ligne + 1);
        String couleur;
    
        if (position.equals("A13") || position.equals("A12") || position.equals("A11") ||
            position.equals("B13") || position.equals("B12") || position.equals("C13") ||
            position.equals("E13") || position.equals("F13") || position.equals("G13") ||
            position.equals("F12") || position.equals("G12") || position.equals("G11") ||
            position.equals("A1") || position.equals("B1") || position.equals("C1") ||
            position.equals("A2") || position.equals("A3") || position.equals("B2") ||
            position.equals("E1") || position.equals("F1") || position.equals("G1") ||
            position.equals("F2") || position.equals("G2") || position.equals("G3")) {
            couleur = VIDE;
        } else if (position.equals("D1") || position.equals("D13")) {
            couleur = BLANC;
        } else if ((ligne + colonne) % 2 == 0) {
            couleur = GRIS;
        } else {
            couleur = BLANC;
        }

        return couleur;
    }              
    

    public boolean aucunePieceAdversaire(String couleur) {
        // Vérifier si aucune pièce adverse n'est présente sur le plateau
        for (int ligne = 0; ligne < LIGNES; ligne++) {
            for (int colonne = 0; colonne < COLONNES; colonne++) {
                String piece = plateau[ligne][colonne];
                if ((couleur.equals("Blanc") && (piece.equals("PN") || piece.equals("CN"))) ||
                    (couleur.equals("Noir") && (piece.equals("PB") || piece.equals("CB")))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean pieceSurPositionCritique(String couleur) {
        if (couleur.equals("Blanc")) {
            return plateau[12][3] != null &&
                   (plateau[12][3].equals("PB") || plateau[12][3].equals("CB"));
        } else if (couleur.equals("Noir")) {
            return plateau[0][3] != null &&
                   (plateau[0][3].equals("PN") || plateau[0][3].equals("CN"));
        }
        return false;
    }        
        
    public boolean joueurEstBloque(Joueur joueurCourant) {
        // Parcourir toutes les cases du plateau
        String couleur = joueurCourant.getCouleur();
        for (int ligne = 0; ligne < plateau.length; ligne++) {
            for (int colonne = 0; colonne < plateau[ligne].length; colonne++) {
                String piece = plateau[ligne][colonne];
                // Vérifier si la pièce appartient au joueur courant
                if ((couleur.equals("Blanc") && (piece.equals("CB") || piece.equals("PB"))) ||
                    (couleur.equals("Noir") && (piece.equals("CN") || piece.equals("PN")))) {
    
                    // Tester tous les déplacements possibles
                    for (int deltaLigne = -1; deltaLigne <= 1; deltaLigne++) {
                        for (int deltaColonne = -1; deltaColonne <= 1; deltaColonne++) {
                            int nouvelleLigne = ligne + deltaLigne;
                            int nouvelleColonne = colonne + deltaColonne;
    
                            if (nouvelleLigne >= 0 && nouvelleLigne < plateau.length &&
                                nouvelleColonne >= 0 && nouvelleColonne < plateau[ligne].length &&
                                estDeplacementValide(ligne, colonne, nouvelleLigne, nouvelleColonne)) {
                                return false; // Au moins un déplacement possible
                            }
                        }
                    }
    
                    // Tester tous les mouvements de capture possibles
                    for (int deltaLigne = -2; deltaLigne <= 2; deltaLigne++) {
                        for (int deltaColonne = -2; deltaColonne <= 2; deltaColonne++) {
                            int nouvelleLigne = ligne + deltaLigne;
                            int nouvelleColonne = colonne + deltaColonne;
    
                            if (nouvelleLigne >= 0 && nouvelleLigne < plateau.length &&
                                nouvelleColonne >= 0 && nouvelleColonne < plateau[ligne].length) {
                                // Vérifier la capture en utilisant la méthode unifiée
                                if (estCaptureValide(ligne, colonne, nouvelleLigne, nouvelleColonne, joueurCourant)) {
                                    return false; // Au moins une capture possible
                                }
                            }
                        }
                    }
                }
            }
        }
        return true; // Aucune action possible, le joueur est bloqué
    }                   
}
package pTraitement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.Serializable;
import pTraitement.Plateau;

/*
 * COUNILH QUENTIN LE LIEVRE LOUIS THOUVENIN AMELIE
 * jeu de chateau
 * rapport intermédiraire mars 2025
 */


public class Ordinateur extends Joueur {
    private static final long serialVersionUID = 1L;
    private int[] dernierePosition;
    private Plateau objetPlateau;

    // Constructeur par défaut
    public Ordinateur() {
        super("Ordinateur", null); // Appelle le constructeur de Joueur avec des valeurs par défaut
    }

    public Ordinateur(String nom, String couleur) {
        super("Ordinateur", couleur);
    }

    // Constructeur prenant uniquement le nom
    public Ordinateur(String nom) {
        super(nom, null); // La couleur sera définie plus tard
    }
    
    public Ordinateur(String nom, Plateau plateau) {
    super(nom); // si ça hérite de Joueur
    this.objetPlateau = plateau;
    }


    public int[] getDernierePosition() {
        return this.dernierePosition; // À maintenir lors des déplacements
    }

    // Setter pour dernierePosition
    public void setDernierePosition(int[] position) {
        this.dernierePosition = position;
    }

    public List<int[]> trouverCapturesValides(Plateau plateau, Joueur joueurCourant) {
        List<int[]> capturesValides = new ArrayList<>();
        String[][] plateauArray = plateau.getPlateau();
        int lignes = plateau.getLIGNES();
        int colonnes = plateau.getCOLONNES();
        String couleurPiece = joueurCourant.getCouleur().substring(0, 1); // "B" pour Blanc, "N" pour Noir
    
        // Directions : 4 orthogonales (haut, bas, gauche, droite) + 4 diagonales
        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Orthogonales
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonales
        };
    
        // Parcourir tout le plateau
        for (int ligne = 0; ligne < lignes; ligne++) {
            for (int colonne = 0; colonne < colonnes; colonne++) {
                String piece = plateauArray[ligne][colonne];
    
                // Vérifier si la case contient une pièce appartenant au joueur courant
                if (piece != null && piece.endsWith(couleurPiece)) {
                    
                    // Vérifier les 8 directions pour les pièces adverses
                    for (int[] direction : directions) {
                        int ligneAdverse = ligne + direction[0];
                        int colonneAdverse = colonne + direction[1];
    
                        // Vérifier que les coordonnées adjacentes sont valides
                        if (ligneAdverse >= 0 && ligneAdverse < lignes && colonneAdverse >= 0 && colonneAdverse < colonnes) {
                            String pieceAdverse = plateauArray[ligneAdverse][colonneAdverse];
    
                            // Vérifier si la case adjacente contient une pièce adverse
                            if (estPieceAdverse(pieceAdverse, couleurPiece)) {
                                
                                // Calculer la case d'arrivée pour la capture
                                int ligneArrivee = ligneAdverse + direction[0];
                                int colonneArrivee = colonneAdverse + direction[1];
    
                                // Vérifier que la case d'arrivée est valide
                                if (ligneArrivee >= 0 && ligneArrivee < lignes && colonneArrivee >= 0 && colonneArrivee < colonnes) {
                                    // Vérifier si la case d'arrivée est occupée
                                    if (!estCaseOccupee(plateauArray, ligneArrivee, colonneArrivee)) {
                                        
                                        // Vérifier si la capture est valide
                                        if (plateau.estCaptureValide(ligne, colonne, ligneArrivee, colonneArrivee, joueurCourant)) {
                                            // Ajouter la capture valide
                                            capturesValides.add(new int[]{ligne, colonne, ligneArrivee, colonneArrivee});
                                           
                                        } else {
                                            System.out.println("Capture invalide : conditions non remplies.");
                                        }
                                    } else {
                                        System.out.println("Case d'arrivée occupée : (" + ligneArrivee + ", " + colonneArrivee + ")");
                                    }
                                } 
                            }
                        } 
                    }
                }
            }
        }
        return capturesValides;
    }
    
    // Méthode pour vérifier si une pièce est adverse
    private boolean estPieceAdverse(String piece, String couleurPiece) {
        if (piece == null || piece.equals(Plateau.VIDE)) {
            return false; // Case vide ou nulle
        }
        // Vérifie que la pièce est une pièce adverse valide
        String couleurAdverse = couleurPiece.equals("B") ? "N" : "B"; // Couleur adverse
        return piece.endsWith(couleurAdverse);
    }
    
    private boolean estCaseOccupee(String[][] plateauArray, int ligne, int colonne) {
        // Vérifie si la case est valide
        if (ligne < 0 || ligne >= plateauArray.length || colonne < 0 || colonne >= plateauArray[0].length) {
            return false; // Case hors du plateau
        }
    
        // Récupère la pièce sur la case
        String piece = plateauArray[ligne][colonne];
    
        // Une case est occupée si elle contient une pièce alliée ou ennemie
        // Les pièces valides sont celles qui commencent par 'P' (Pion) ou 'C' (Cavalier)
        // et se terminent par 'B' (Blanc) ou 'N' (Noir).
        if (piece != null) {
            // Vérifie si la pièce est valide (PB, PN, CB, CN)
            return piece.matches("^[PC][BN]$");
        }
    
        // Si la pièce est nulle ou ne correspond pas au format, la case est considérée comme vide
        return false;
    }           

    public List<int[]> trouverDeplacementsValides(Plateau plateau, Joueur joueurCourant) {
        List<int[]> deplacementsValides = new ArrayList<>();
        String[][] plateauArray = plateau.getPlateau();
        int lignes = plateau.getLignes();
        int colonnes = plateau.getColonnes();
    
        // Déterminer le préfixe de la couleur du joueur
        String prefixeCouleur = joueurCourant.getCouleur().equals("Blanc") ? "B" : "N";
    
        // Lister toutes les pièces du joueur
        List<int[]> piecesDuJoueur = new ArrayList<>();
        for (int ligne = 0; ligne < lignes; ligne++) {
            for (int colonne = 0; colonne < colonnes; colonne++) {
                String piece = plateauArray[ligne][colonne];
                if (piece != null && piece.endsWith(prefixeCouleur)) { // Vérifier la couleur
                    piecesDuJoueur.add(new int[]{ligne, colonne}); // Ajouter la position de la pièce
                }
            }
        }
    
        // Choisir une pièce au hasard
        Random random = new Random();
        while (!piecesDuJoueur.isEmpty()) {
            int index = random.nextInt(piecesDuJoueur.size()); // Choisir une pièce aléatoire
            int[] positionPiece = piecesDuJoueur.get(index);
            int ligne = positionPiece[0];
            int colonne = positionPiece[1];
    
            // Vérifier les déplacements valides autour de la pièce
            for (int deltaLigne = -1; deltaLigne <= 1; deltaLigne++) {
                for (int deltaColonne = -1; deltaColonne <= 1; deltaColonne++) {
                    if (deltaLigne == 0 && deltaColonne == 0) continue; // Ignorer la case actuelle
    
                    int nouvelleLigne = ligne + deltaLigne;
                    int nouvelleColonne = colonne + deltaColonne;
    
                    // Vérifier si le déplacement est valide
                    if (plateau.estDeplacementValide(ligne, colonne, nouvelleLigne, nouvelleColonne)) {
                        deplacementsValides.add(new int[]{ligne, colonne, nouvelleLigne, nouvelleColonne});
                    }
                }
            }
    
            // Si aucun déplacement n'est valide, retirer cette pièce de la liste
            if (deplacementsValides.isEmpty()) {
                System.out.println("Aucun déplacement valide pour la pièce en (" + ligne + ", " + colonne + ")");
                piecesDuJoueur.remove(index); // Retirer la pièce de la liste
            } else {
                break; // Sortir de la boucle si des déplacements valides sont trouvés
            }
        }
        return deplacementsValides;
    }            

    public void jouerTour(Plateau plateau, Partie partie) {
        System.out.println(getNom() + " (" + getCouleur() + ") joue son tour.");

        // Vérifier les captures valides initiales
        List<int[]> capturesValides = trouverCapturesValides(plateau, this);

        // Vérifier les déplacements valides
        List<int[]> deplacementsValides = trouverDeplacementsValides(plateau, this);

        // Si aucun déplacement ni capture n'est possible
        if (capturesValides.isEmpty() && deplacementsValides.isEmpty()) {
            System.out.println(getNom() + " est bloqué. Match nul !");
            partie.setPartieTerminee(true); // Mettre à jour le statut de la partie
            return; // Terminer le tour immédiatement
        }

        // Effectuer une capture si possible
        if (!capturesValides.isEmpty()) {
            boolean captureEnCours = true;
            int[] capture = capturesValides.get(new Random().nextInt(capturesValides.size()));
            int ligneDepart = capture[0];
            int colonneDepart = capture[1];
            int ligneArrivee = capture[2];
            int colonneArrivee = capture[3];
            int nombreCaptures = 0; // Compteur pour éviter les boucles infinies

            while (captureEnCours && nombreCaptures < 10) { // Limite de 10 captures par tour
                System.out.println("Capture choisie : (" + ligneDepart + ", " + colonneDepart + ") -> (" + ligneArrivee + ", " + colonneArrivee + ")");

                // Déplacer la pièce qui capture
                plateau.deplacerPiece(ligneDepart, colonneDepart, ligneArrivee, colonneArrivee);

                // Calculer la position de la pièce intermédiaire (sautée)
                int intermediaireLigne = (ligneDepart + ligneArrivee) / 2;
                int intermediaireColonne = (colonneDepart + colonneArrivee) / 2;
            
                // Réinitialiser la case de la pièce sautée à sa couleur d'origine
                plateau.getPlateau()[intermediaireLigne][intermediaireColonne] = plateau.obtenirCouleurOrigine(intermediaireLigne, intermediaireColonne);

                System.out.println("Capture effectuée avec succès.");
                nombreCaptures++;

                // Vérifier les captures supplémentaires
                List<int[]> capturesPossibles = plateau.trouverCapturesPossibles(ligneArrivee, colonneArrivee, this);
                if (capturesPossibles.isEmpty()) {
                    captureEnCours = false; // Aucune capture supplémentaire possible
                } else {
                    // Choisir la première capture supplémentaire (ou une au hasard)
                    int[] nouvelleCapture = capturesPossibles.get(new Random().nextInt(capturesPossibles.size()));
                    ligneDepart = ligneArrivee;
                    colonneDepart = colonneArrivee;
                    ligneArrivee = nouvelleCapture[0];
                    colonneArrivee = nouvelleCapture[1];
                    System.out.println("Une capture supplémentaire est possible. L'ordinateur continue.");
                }
            }

            if (nombreCaptures >= 10) {
                System.out.println("Limite de captures atteinte (10). Le tour est terminé.");
            }
        } else {
            // Sinon, effectuer un déplacement
            if (!deplacementsValides.isEmpty()) {
                int[] deplacement = deplacementsValides.get(new Random().nextInt(deplacementsValides.size()));
                System.out.println("Déplacement choisi : (" + deplacement[0] + ", " + deplacement[1] + ") -> (" + deplacement[2] + ", " + deplacement[3] + ")");
                plateau.deplacerPiece(deplacement[0], deplacement[1], deplacement[2], deplacement[3]);
            } else {
                System.out.println("Aucun déplacement valide disponible.");
            }
        }
    }

    public void setCaseVide(int ligne, int colonne) {
    objetPlateau.getPlateau()[ligne][colonne] = objetPlateau.obtenirCouleurOrigine(ligne, colonne);
    }


    
    // Méthode pour obtenir les mouvements possibles du cavalier
    private List<int[]> obtenirMouvementsCavalier(int ligne, int colonne) {
        // Définir les positions possibles d'un cavalier (en L)
        List<int[]> mouvements = new ArrayList<>();
        int[][] directions = {
            {-2, -1}, {-2, 1}, {2, -1}, {2, 1},
            {-1, -2}, {-1, 2}, {1, -2}, {1, 2}
        };
    
        // Vérifier chaque direction pour voir si elle est valide sur le plateau
        for (int[] direction : directions) {
            int nouvelleLigne = ligne + direction[0];
            int nouvelleColonne = colonne + direction[1];
            if (nouvelleLigne >= 0 && nouvelleLigne < objetPlateau.getLignes() &&
                nouvelleColonne >= 0 && nouvelleColonne < objetPlateau.getColonnes()) {
                mouvements.add(new int[]{nouvelleLigne, nouvelleColonne});
            }

        }
        return mouvements;
    }

    public boolean aGagne(Plateau plateau) {
        // Vérifier les conditions de victoire pour le joueur
        return plateau.aucunePieceAdversaire(getCouleur()) || plateau.pieceSurPositionCritique(getCouleur());
    }
}
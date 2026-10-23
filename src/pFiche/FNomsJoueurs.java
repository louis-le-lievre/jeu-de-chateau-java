/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pFiche;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


public class FNomsJoueurs extends javax.swing.JDialog {
    
    
    public FNomsJoueurs(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initializeBoardButtons();
        configurerEcouteursPlateau();
    }
    
    public void setContreOrdi(boolean contreOrdi) {
    this.contreOrdi = contreOrdi;
    // Mettre à jour l'interface
    if (contreOrdi) {
        masquerChampOrdi();
    } else {
        afficherChampOrdi();
    }
    }

    public boolean isContreOrdi() {
        return contreOrdi;
    }
    
    public void masquerChampOrdi() {
    jLabelOrdi.setVisible(false);
    jTextFieldOrdi.setVisible(false);
    }
    
    public void afficherChampOrdi() {
    jLabel3.setVisible(true);
    jTextFieldOrdi.setVisible(true);
    }
    
    // Variables pour la gestion du plateau
    private JButton selectedPiece = null;
    private boolean isPieceSelected = false;
    private String[][] boardState = new String[13][7];
    private JButton[][] boardButtons = new JButton[13][7];
    private boolean estTourDesBlancs = true; // vrai = blancs commencent
    private boolean partieTerminee = false;
    private boolean joueursPrets = false;
    private String nomJoueurBlanc;
    private String nomJoueurNoir;
    private String nomOrdi = "Ordinateur"; // Nom par défaut pour l'IA
    private boolean sequenceCaptureEnCours = false;
    private JButton pieceEnCapture = null;
    private boolean contreOrdi = false;
    

    // Initialise le tableau des boutons du plateau
    private void initializeBoardButtons() {
        boardButtons[0][0] = jButton3;
        boardButtons[0][1] = jButton4;
        boardButtons[0][2] = jButton5;
        boardButtons[0][3] = jButton6;
        boardButtons[0][4] = jButton7;
        boardButtons[0][5] = jButton8;
        boardButtons[0][6] = jButton19;
        boardButtons[1][0] = jButton20;
        boardButtons[1][1] = jButton9;
        boardButtons[1][2] = jButton17;
        boardButtons[1][3] = jButton22;
        boardButtons[1][4] = jButton14;
        boardButtons[2][1] = jButton10;
        boardButtons[2][2] = jButton11;
        boardButtons[2][3] = jButton21;
        boardButtons[2][4] = jButton18;
        boardButtons[2][5] = jButton23;
        boardButtons[3][0] = jButton33;
        boardButtons[3][1] = jButton25;
        boardButtons[3][2] = jButton27;
        boardButtons[3][3] = jButton28;
        boardButtons[3][4] = jButton101;
        boardButtons[3][5] = jButton29;
        boardButtons[3][6] = jButton30;
        boardButtons[4][0] = jButton31;
        boardButtons[4][1] = jButton24;
        boardButtons[4][2] = jButton99;
        boardButtons[4][3] = jButton61;
        boardButtons[4][4] = jButton32;
        boardButtons[4][5] = jButton62;
        boardButtons[4][6] = jButton35;
        boardButtons[5][0] = jButton42;
        boardButtons[5][1] = jButton36;
        boardButtons[5][2] = jButton37;
        boardButtons[5][3] = jButton38;
        boardButtons[5][4] = jButton39;
        boardButtons[5][5] = jButton40;
        boardButtons[5][6] = jButton41;
        boardButtons[6][0] = jButton44;
        boardButtons[6][1] = jButton43;
        boardButtons[6][2] = jButton45;
        boardButtons[6][3] = jButton46;
        boardButtons[6][4] = jButton47;
        boardButtons[6][5] = jButton48;
        boardButtons[6][6] = jButton49;
        boardButtons[7][0] = jButton56;
        boardButtons[7][1] = jButton50;
        boardButtons[7][2] = jButton51;
        boardButtons[7][3] = jButton52;
        boardButtons[7][4] = jButton53;
        boardButtons[7][5] = jButton54;
        boardButtons[7][6] = jButton55;
        boardButtons[8][0] = jButton58;
        boardButtons[8][1] = jButton57;
        boardButtons[8][2] = jButton59;
        boardButtons[8][3] = jButton97;
        boardButtons[8][4] = jButton60;
        boardButtons[8][5] = jButton98;
        boardButtons[8][6] = jButton63;
        boardButtons[9][0] = jButton70;
        boardButtons[9][1] = jButton64;
        boardButtons[9][2] = jButton65;
        boardButtons[9][3] = jButton66;
        boardButtons[9][4] = jButton96;
        boardButtons[9][5] = jButton68;
        boardButtons[9][6] = jButton69;
        boardButtons[10][1] = jButton71;
        boardButtons[10][2] = jButton72;
        boardButtons[10][3] = jButton76;
        boardButtons[10][4] = jButton75;
        boardButtons[10][5] = jButton77;
        boardButtons[11][2] = jButton82;
        boardButtons[11][3] = jButton84;
        boardButtons[11][4] = jButton81;
        boardButtons[12][3] = jButton88;
    }

    private void configurerEcouteursPlateau() {
    // Parcourir toutes les cases du plateau (13 lignes x 7 colonnes)
    for (int ligne = 0; ligne < 13; ligne++) {
        for (int colonne = 0; colonne < 7; colonne++) {
            // Vérifier si la case existe
            if (boardButtons[ligne][colonne] != null) {
                // Sauvegarder les coordonnées pour la closure
                int ligneFinale = ligne;
                int colonneFinale = colonne;
                
                // Ajouter un écouteur de clic à la case
                boardButtons[ligne][colonne].addActionListener(e -> 
                    // Appeler la méthode de gestion du clic avec la case et ses coordonnées
                    gestionClicPlateau(boardButtons[ligneFinale][colonneFinale], ligneFinale, colonneFinale)
                );
            }
        }
    }
    }

    private void gestionClicPlateau(JButton bouton, int ligneArrivee, int colonneArrivee) {
    if (!joueursPrets || partieTerminee) return;

    if (selectedPiece == null) {
        // Sélection d'une pièce
        if (bouton.getIcon() != null) {
            String cheminIcone = bouton.getIcon().toString();
            boolean estBlanc = cheminIcone.contains("blanc");
            
            if (estBlanc == estTourDesBlancs) {
                if (sequenceCaptureEnCours && bouton != pieceEnCapture) {
                    JOptionPane.showMessageDialog(this, "Vous devez continuer avec votre pièce en capture !");
                    return;
                }
                
                selectedPiece = bouton;
                bouton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            } else {
                JOptionPane.showMessageDialog(this, "Ce n'est pas votre tour !");
            }
        }
    } else {
        boolean mouvementEffectue = false;
        boolean etaitEnSequenceCapture = sequenceCaptureEnCours;
        
        if (estCaptureValide(selectedPiece, bouton, ligneArrivee, colonneArrivee)) {
            effectuerCapture(selectedPiece, bouton, ligneArrivee, colonneArrivee);
            mouvementEffectue = true;
            sequenceCaptureEnCours = true;
            pieceEnCapture = bouton;
            
            if (!trouverCapturesPossibles(bouton).isEmpty()) {
                selectedPiece = bouton;
                bouton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                return;
            } else {
                sequenceCaptureEnCours = false;
                pieceEnCapture = null;
            }
        } 
        else if (estDeplacementValide(selectedPiece, bouton, ligneArrivee, colonneArrivee)) {
            if (sequenceCaptureEnCours) {
                JOptionPane.showMessageDialog(this, "Vous devez terminer vos captures !");
                return;
            }
            effectuerDeplacement(selectedPiece, bouton);
            mouvementEffectue = true;
        }
        else {
            JOptionPane.showMessageDialog(this, "Mouvement invalide !");
        }

        selectedPiece.setBorder(null);
        selectedPiece = null;

        if (mouvementEffectue && !partieTerminee && !sequenceCaptureEnCours) {
            estTourDesBlancs = !estTourDesBlancs;
            mettreAJourTexteTour();

            // Nouveau : vérifie si le joueur courant est l'IA (blanc ou noir)
            String joueurActuel = estTourDesBlancs ? nomJoueurBlanc : nomJoueurNoir;
            if (contreOrdi && joueurActuel.equals(nomOrdi)) {
                SwingUtilities.invokeLater(() -> jouerCoupIA());
            }
        }
    }
    }
    
    private List<int[]> trouverCapturesPossibles(JButton piece) {
    List<int[]> captures = new ArrayList<>();
    if (piece == null || piece.getIcon() == null) return captures;

    int ligne = ObtenirLignes(piece);
    int colonne = ObtenirColonnes(piece);
    
    int[][] directions = {{-2,0}, {2,0}, {0,-2}, {0,2}, {-2,-2}, {-2,2}, {2,-2}, {2,2}};

    for (int[] dir : directions) {
        int nouvelleLigne = ligne + dir[0];
        int nouvelleColonne = colonne + dir[1];
        
        // Vérifier les limites du plateau
        if (nouvelleLigne >= 0 && nouvelleLigne < boardButtons.length && 
            nouvelleColonne >= 0 && nouvelleColonne < boardButtons[0].length) {
            
            JButton destination = boardButtons[nouvelleLigne][nouvelleColonne];
            if (destination != null && estCaptureValide(piece, destination, nouvelleLigne, nouvelleColonne)) {
                captures.add(new int[]{nouvelleLigne, nouvelleColonne});
            }
        }
    }
    return captures;
    }
    
    private boolean estCaptureValide(JButton source, JButton destination, int destRow, int destCol) {
    if (source == null || destination == null || source.getIcon() == null) {
        return false;
    }

    int srcRow = ObtenirLignes(source);
    int srcCol = ObtenirColonnes(source);
    int rowDiff = Math.abs(destRow - srcRow);
    int colDiff = Math.abs(destCol - srcCol);

    // Vérifier le pattern de capture (2 cases dans n'importe quelle direction)
    if (!((rowDiff == 2 && colDiff == 0) ||  // Vertical
         (rowDiff == 0 && colDiff == 2) ||  // Horizontal
         (rowDiff == 2 && colDiff == 2))) { // Diagonal
        return false;
    }

    // Calculer la case intermédiaire
    int midRow = (srcRow + destRow) / 2;
    int midCol = (srcCol + destCol) / 2;

    // Vérifier les limites du plateau
    if (midRow < 0 || midRow >= boardButtons.length || 
        midCol < 0 || midCol >= boardButtons[0].length) {
        return false;
    }

    JButton midButton = boardButtons[midRow][midCol];
    
    // Vérifier que la case intermédiaire existe et contient une pièce
    if (midButton == null || midButton.getIcon() == null) {
        return false;
    }

    // Vérifier les couleurs
    boolean isSourceWhite = source.getIcon().toString().contains("blanc");
    boolean isMidWhite = midButton.getIcon().toString().contains("blanc");
    
    return isSourceWhite != isMidWhite && destination.getIcon() == null;
    }

    private void effectuerDeplacement(JButton source, JButton destination) {
    destination.setIcon(source.getIcon());
    source.setIcon(null);
    
    mettreAJourTexteTour();
    
    // Vérification finale (avec protection contre NullPointerException)
    try {
        verifierFinPartie();
    } catch (NullPointerException e) {
        System.err.println("Erreur lors de la vérification: " + e.getMessage());
        // On continue la partie malgré l'erreur
    }
    }
    //if (Jbutton destination == ){ 
    private void effectuerCapture(JButton source, JButton destination, int destRow, int destCol) {
    int midRow = (ObtenirLignes(source) + destRow)/2;
    int midCol = (ObtenirColonnes(source) + destCol)/2;
    JButton midButton = boardButtons[midRow][midCol];
    
    
    boolean chateauCapture = false;
    if (midButton == jButton6 || midButton == jButton88) {
        chateauCapture = true;
    }
    
    midButton.setIcon(null);
    destination.setIcon(source.getIcon());
    source.setIcon(null);
    
    if (chateauCapture) {
        verifierFinPartie();
    } else {
        mettreAJourTexteTour();
        verifierFinPartie();
    }
    }
    
    private boolean estDeplacementValide(JButton source, JButton destination, int ligneArrivee, int colonneArrivee) {
    int ligneDepart = ObtenirLignes(source);
    int colonneDepart = ObtenirColonnes(source);

    if (source.getIcon() == null) return false;

    String cheminIcone = source.getIcon().toString();
    boolean estBlanc = cheminIcone.contains("blanc");

    // Vérification tour du joueur
    if (estBlanc != estTourDesBlancs) return false;

    // Règles spéciales pour les CHATEAUX (peuvent être capturés)
    boolean estDestinationChateau = destination == jButton6 || destination == jButton88;
    
    // Règles pour le PION
    if (cheminIcone.contains("Pion")) {
        int diffLigne = Math.abs(ligneArrivee - ligneDepart);
        int diffColonne = Math.abs(colonneArrivee - colonneDepart);

        // Déplacement d'une case (horizontal, vertical ou diagonal)
        boolean deplacementValide = (diffLigne <= 1 && diffColonne <= 1);
        
        if (estDestinationChateau) {
            // Un château ne peut être capturé que par un mouvement diagonal
            return deplacementValide && (diffLigne == 1 && diffColonne == 1);
        }
        return deplacementValide && (destination.getIcon() == null);
    }

    // Règles pour le CAVALIER
    if (cheminIcone.contains("Cavalier")) {
        int diffLigne = Math.abs(ligneArrivee - ligneDepart);
        int diffColonne = Math.abs(colonneArrivee - colonneDepart);

        // Option 1 : Déplacement d'une case
        if (diffLigne <= 1 && diffColonne <= 1) {
            if (estDestinationChateau) return true; // Le cavalier peut capturer le château
            if (destination.getIcon() == null) return true;
            String destIconPath = destination.getIcon().toString();
            return cheminIcone.contains("blanc") != destIconPath.contains("blanc");
        }

        // Option 2 : Déplacement en L
        boolean estDeplacementEnL = (diffLigne == 2 && diffColonne == 1) || (diffLigne == 1 && diffColonne == 2);
        if (estDeplacementEnL) {
            // Vérification chemin libre
            if (diffLigne == 2) {
                int directionLigne = (ligneArrivee > ligneDepart) ? 1 : -1;
                if (boardButtons[ligneDepart + directionLigne][colonneDepart].getIcon() != null) {
                    return false;
                }
            } else {
                int directionColonne = (colonneArrivee > colonneDepart) ? 1 : -1;
                if (boardButtons[ligneDepart][colonneDepart + directionColonne].getIcon() != null) {
                    return false;
                }
            }
            
            if (estDestinationChateau) return true; // Le cavalier peut capturer le château
            if (destination.getIcon() == null) return true;
            String destIconPath = destination.getIcon().toString();
            return cheminIcone.contains("blanc") != destIconPath.contains("blanc");
        }
    }

    return false;
    }
    
    private boolean aucunePieceAdversaire(String couleur) {
    String couleurRecherchee = couleur.equalsIgnoreCase("Blanc") ? "blanc" : "noir";
    
    for (JButton[] ligne : boardButtons) {
        for (JButton button : ligne) {
            // Ignorer les cases null et les châteaux
            if (button == null || button == jButton6 || button == jButton88) continue;
            
            if (button.getIcon() != null 
                && button.getIcon().toString().contains(couleurRecherchee)) {
                return false; // Au moins une pièce trouvée
            }
        }
    }
    return true; // Aucune pièce trouvée
    }

    private void verifierFinPartie() {
    // Vérification capture des châteaux
    if (jButton6.getIcon() != null && jButton6.getIcon().toString().contains("noir")) {
        JOptionPane.showMessageDialog(this, nomJoueurNoir + " a gagné ! Château blanc conquis.");
        partieTerminee = true;
        this.setVisible(false); // Ferme la JDialog
        return;
    }

    if (jButton88.getIcon() != null && jButton88.getIcon().toString().contains("blanc")) {
        JOptionPane.showMessageDialog(this, nomJoueurBlanc + " a gagné ! Château noir conquis.");
        partieTerminee = true;
        this.setVisible(false);
        return;
    }

    // Vérification pièces éliminées
    if (aucunePieceAdversaire("Noir")) {
        JOptionPane.showMessageDialog(this, nomJoueurBlanc + " gagne ! Toutes les pièces noires éliminées.");
        partieTerminee = true;
        this.setVisible(false);
        return;
    }

    if (aucunePieceAdversaire("Blanc")) {
        JOptionPane.showMessageDialog(this, nomJoueurNoir + " gagne ! Toutes les pièces blanches éliminées.");
        partieTerminee = true;
        this.setVisible(false);
        return;
    }

    // Vérification blocages
    boolean blancBloque = joueurEstBloque(true);
    boolean noirBloque = joueurEstBloque(false);
    
    if (blancBloque && noirBloque) {
        JOptionPane.showMessageDialog(this, "Match nul ! Les deux joueurs sont bloqués.");
        partieTerminee = true;
        this.setVisible(false);
        return;
    }
    
    if (estTourDesBlancs && blancBloque) {
        JOptionPane.showMessageDialog(this, nomJoueurNoir + " gagne ! " + nomJoueurBlanc + " est bloqué.");
        partieTerminee = true;
        this.setVisible(false);
    } else if (!estTourDesBlancs && noirBloque) {
        JOptionPane.showMessageDialog(this, nomJoueurBlanc + " gagne ! " + nomJoueurNoir + " est bloqué.");
        partieTerminee = true;
        this.setVisible(false);
    }
    }

    private int ObtenirLignes(JButton button) {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 7; j++) {
                if (boardButtons[i][j] == button) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int ObtenirColonnes(JButton button) {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 7; j++) {
                if (boardButtons[i][j] == button) {
                    return j;
                }
            }
        }
        return -1;
    }
    
    private void mettreAJourEtatPlateau() {
    // Met à jour l'etat du tableau 
    for (int ligne = 0; ligne < 13; ligne++) {
        for (int colonne = 0; colonne < 7; colonne++) {
            // Vérifie si la case existe et contient une pièce
            if (boardButtons[ligne][colonne] != null && 
                boardButtons[ligne][colonne].getIcon() != null) {
                
                // Enregistre le chemin de l'icône dans l'état du plateau
                boardState[ligne][colonne] = boardButtons[ligne][colonne].getIcon().toString();
            } else {
                // Marque la case comme vide
                boardState[ligne][colonne] = null;
            }
        }
    }
    }

    private void mettreAJourTexteTour() {
    String joueurActuel = estTourDesBlancs ? nomJoueurBlanc : nomJoueurNoir;
    String couleur = estTourDesBlancs ? "blancs" : "noirs";
   
    if (contreOrdi && joueurActuel.equals(nomOrdi)) {
        jLabel3.setText("L'ordinateur (" + couleur + ") réfléchit...");
    } else {
        jLabel3.setText("C'est au tour de " + joueurActuel + " (" + couleur + ")");
    }
    }


    private void sauvegarderPartie() {
    try (java.io.PrintWriter writer = new java.io.PrintWriter("sauvegarde_partie.txt")) {

        writer.println("joueurBlanc=" + nomJoueurBlanc);
        writer.println("joueurNoir=" + nomJoueurNoir);
        writer.println("tour=" + (estTourDesBlancs ? "blanc" : "noir"));

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 7; j++) {
                String valeur = "null";
                if (boardButtons[i][j] != null && boardButtons[i][j].getIcon() != null) {
                    String iconStr = boardButtons[i][j].getIcon().toString();
                    int index = iconStr.lastIndexOf('/');
                    if (index != -1) {
                        valeur = iconStr.substring(index + 1); 
                    }
                }
                writer.println("cell:" + i + "," + j + "=" + valeur);
            }
        }

        JOptionPane.showMessageDialog(this, "Partie sauvegardée !");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + e.getMessage());
    }
    }
    
    public void chargerPartie() {
    try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("sauvegarde_partie.txt"))) {
        String ligne;
        while ((ligne = reader.readLine()) != null) {
            if (ligne.startsWith("joueurBlanc=")) {
                nomJoueurBlanc = ligne.substring("joueurBlanc=".length());
            } else if (ligne.startsWith("joueurNoir=")) {
                nomJoueurNoir = ligne.substring("joueurNoir=".length());
            } else if (ligne.startsWith("tour=")) {
                String tour = ligne.substring("tour=".length());
                estTourDesBlancs = tour.equals("blanc");
            } else if (ligne.startsWith("cell:")) {
                String[] parts = ligne.substring("cell:".length()).split("=");
                String[] coords = parts[0].split(",");
                int i = Integer.parseInt(coords[0]);
                int j = Integer.parseInt(coords[1]);
                String valeur = parts[1];

                if (boardButtons[i][j] != null) {
                    if (!valeur.equals("null")) {
                        // Nettoyer le nom du fichier
                        String nomFichier = valeur.replace("%20", " ");
                        java.net.URL ressource = getClass().getResource("/pImages/" + nomFichier);
                        if (ressource != null) {
                            boardButtons[i][j].setIcon(new javax.swing.ImageIcon(ressource));
                        } else {
                            boardButtons[i][j].setIcon(null);
                            System.err.println("Image introuvable : " + nomFichier);
                        }
                    } else {
                        boardButtons[i][j].setIcon(null);
                    }
                }
            }
        }

        joueursPrets = true;
        contreOrdi = nomJoueurBlanc.equals("Ordinateur") || nomJoueurNoir.equals("Ordinateur");

        // Mise à jour du texte du tour (le faire après avoir défini contreOrdi)
        mettreAJourTexteTour();

        // Si c’est à l’ordinateur de jouer, le faire immédiatement
        if (contreOrdi && ((estTourDesBlancs && nomJoueurBlanc.equals("Ordinateur")) ||
                           (!estTourDesBlancs && nomJoueurNoir.equals("Ordinateur")))) {
            SwingUtilities.invokeLater(() -> jouerCoupIA());
        }

        isPieceSelected = true;
        partieTerminee = false;

        mettreAJourEtatPlateau();
        mettreAJourTexteTour();

        JOptionPane.showMessageDialog(this, "Partie chargée avec succès !");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erreur lors du chargement : " + e.getMessage());
    }
    }
    
    public boolean joueurEstBloque(boolean estTourDesBlancs) {
    for (int ligne = 0; ligne < 13; ligne++) {
        for (int colonne = 0; colonne < 7; colonne++) {
            JButton caseCourante = boardButtons[ligne][colonne];
            
            // Vérification cruciale ajoutée ici
            if (caseCourante == null) {
                continue; // Passe à la case suivante si null
            }
            
            if (caseCourante.getIcon() != null) {
                String cheminIcone = caseCourante.getIcon().toString();
                boolean estBlanc = cheminIcone.contains("blanc");
                
                if (estBlanc == estTourDesBlancs) {
                    // Vérification des déplacements (1 case autour)
                    for (int deltaLigne = -1; deltaLigne <= 1; deltaLigne++) {
                        for (int deltaColonne = -1; deltaColonne <= 1; deltaColonne++) {
                            int nouvelleLigne = ligne + deltaLigne;
                            int nouvelleColonne = colonne + deltaColonne;
                            
                            // Correction ici : vérifiez bien 13 lignes et 7 colonnes
                            if (nouvelleLigne >= 0 && nouvelleLigne < 13 &&
                                nouvelleColonne >= 0 && nouvelleColonne < 7) {
                                
                                JButton destination = boardButtons[nouvelleLigne][nouvelleColonne];
                                if (estDeplacementValide(caseCourante, destination, nouvelleLigne, nouvelleColonne)) {
                                    return false;
                                }
                            }
                        }
                    }
                    
                    // Vérification des captures (sauts de 2 cases)
                    for (int deltaLigne = -2; deltaLigne <= 2; deltaLigne += 2) {
                        for (int deltaColonne = -2; deltaColonne <= 2; deltaColonne += 2) {
                            int nouvelleLigne = ligne + deltaLigne;
                            int nouvelleColonne = colonne + deltaColonne;
                            
                            if (nouvelleLigne >= 0 && nouvelleLigne < 13 &&
                                nouvelleColonne >= 0 && nouvelleColonne < 7) {
                                
                                JButton destination = boardButtons[nouvelleLigne][nouvelleColonne];
                                if (estCaptureValide(caseCourante, destination, nouvelleLigne, nouvelleColonne)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return true;
    }
    
    // Classe pour stocker les informations de capture
    private class CaptureInfo {
        JButton source;
        JButton destination;
        int ligneArrivee;
        int colonneArrivee;

        public CaptureInfo(JButton source, JButton destination, int ligneArrivee, int colonneArrivee) {
            this.source = source;
            this.destination = destination;
            this.ligneArrivee = ligneArrivee;
            this.colonneArrivee = colonneArrivee;
        }
    }
    
    private void jouerCoupIA() {
    String joueurActuel = estTourDesBlancs ? nomJoueurBlanc : nomJoueurNoir;
    if (partieTerminee || !contreOrdi || !joueurActuel.equals(nomOrdi)) {
        return;
    }

    try {
        Thread.sleep(500);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }

    // 1. Trouver toutes les captures possibles
    List<CaptureInfo> captures = trouverToutesCapturesPossibles();
    
    if (!captures.isEmpty()) {
        effectuerSequenceCapturesIA(captures);
    } else {
        // 2. Si aucune capture possible, faire un déplacement simple
        effectuerDeplacementSimpleIA();
    }

    // Vérifier la fin de partie et passer le tour
    verifierFinPartie();
    if (!partieTerminee) {
        estTourDesBlancs = !estTourDesBlancs;
        mettreAJourTexteTour();
    }
    }

    private List<CaptureInfo> trouverToutesCapturesPossibles() {
    List<CaptureInfo> captures = new ArrayList<>();

    for (int i = 0; i < boardButtons.length; i++) {
        for (int j = 0; j < boardButtons[i].length; j++) {
            JButton piece = boardButtons[i][j];
            if (piece != null && piece.getIcon() != null) {
                String icone = piece.getIcon().toString();

                if (icone.contains("Chateau")) continue; 

                boolean pieceEstBlanche = icone.contains("blanc");

                if ((estTourDesBlancs && pieceEstBlanche) || (!estTourDesBlancs && !pieceEstBlanche)) {
                    List<int[]> destCaptures = trouverCapturesPossibles(piece);
                    for (int[] dest : destCaptures) {
                        captures.add(new CaptureInfo(
                            piece,
                            boardButtons[dest[0]][dest[1]],
                            dest[0],
                            dest[1]
                        ));
                    }
                }
            }
        }
    }
    return captures;
    }


    private void effectuerSequenceCapturesIA(List<CaptureInfo> capturesInitiales) {
    Random rand = new Random();
    CaptureInfo capture = capturesInitiales.get(rand.nextInt(capturesInitiales.size()));
    JButton pieceCourante = capture.source;
    int nombreCaptures = 0;
    final int LIMITE_CAPTURES = 10;

    while (nombreCaptures < LIMITE_CAPTURES && !partieTerminee) {
        // Vérifier si la pièce courante est jouable (exclut les châteaux)
        if (pieceCourante == null || pieceCourante.getIcon() == null) break;

        String icone = pieceCourante.getIcon().toString();
        if (icone.contains("Chateau")) break;  

        boolean pieceEstBlanche = icone.contains("blanc");
        if ((estTourDesBlancs && pieceEstBlanche) || (!estTourDesBlancs && !pieceEstBlanche)) {
            // Effectuer la capture
            effectuerCapture(pieceCourante, capture.destination, capture.ligneArrivee, capture.colonneArrivee);
            nombreCaptures++;

            verifierFinPartie();
            if (partieTerminee) break;

            List<int[]> nouvellesCaptures = trouverCapturesPossibles(capture.destination);
            if (nouvellesCaptures.isEmpty()) break;

            int[] nouvelleDest = nouvellesCaptures.get(rand.nextInt(nouvellesCaptures.size()));
            pieceCourante = capture.destination;
            capture = new CaptureInfo(
                pieceCourante,
                boardButtons[nouvelleDest[0]][nouvelleDest[1]],
                nouvelleDest[0],
                nouvelleDest[1]
            );
        } else {
            break;
        }
    }
    }

    private void effectuerDeplacementSimpleIA() {
    List<int[]> deplacementsValides = new ArrayList<>();

    for (int i = 0; i < boardButtons.length; i++) {
        for (int j = 0; j < boardButtons[i].length; j++) {
            JButton piece = boardButtons[i][j];
            if (piece != null && piece.getIcon() != null) {
                String icone = piece.getIcon().toString();
                if (icone.contains("Chateau")) continue;  

                boolean estPieceIA = estTourDesBlancs ? icone.contains("blanc") : icone.contains("noir");
                if (!estPieceIA) continue;

                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        if (di == 0 && dj == 0) continue;

                        int ni = i + di;
                        int nj = j + dj;
                        if (ni >= 0 && ni < boardButtons.length &&
                            nj >= 0 && nj < boardButtons[ni].length) {

                            JButton dest = boardButtons[ni][nj];
                            if (dest != null && estDeplacementValide(piece, dest, ni, nj)) {
                                deplacementsValides.add(new int[]{i, j, ni, nj});
                            }
                        }
                    }
                }
            }
        }
    }

    if (!deplacementsValides.isEmpty()) {
        int[] deplacement = deplacementsValides.get(new Random().nextInt(deplacementsValides.size()));
        JButton source = boardButtons[deplacement[0]][deplacement[1]];
        JButton dest = boardButtons[deplacement[2]][deplacement[3]];
        effectuerDeplacement(source, dest);
        verifierFinPartie();
    }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton100 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabelOrdi = new javax.swing.JLabel();
        jTextFieldOrdi = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton47 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jButton96 = new javax.swing.JButton();
        jButton49 = new javax.swing.JButton();
        jButton50 = new javax.swing.JButton();
        jButton51 = new javax.swing.JButton();
        jButton52 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton97 = new javax.swing.JButton();
        jButton53 = new javax.swing.JButton();
        jButton98 = new javax.swing.JButton();
        jButton54 = new javax.swing.JButton();
        jButton60 = new javax.swing.JButton();
        jButton55 = new javax.swing.JButton();
        jButton56 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton57 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton58 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton59 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton63 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton64 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton65 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton66 = new javax.swing.JButton();
        jButton68 = new javax.swing.JButton();
        jButton69 = new javax.swing.JButton();
        jButton70 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton78 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton79 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton80 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton81 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton82 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton83 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton84 = new javax.swing.JButton();
        jButton71 = new javax.swing.JButton();
        jButton72 = new javax.swing.JButton();
        jButton73 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton74 = new javax.swing.JButton();
        jButton75 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton76 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton77 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jButton85 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jButton86 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jButton87 = new javax.swing.JButton();
        jButton88 = new javax.swing.JButton();
        jButton89 = new javax.swing.JButton();
        jButton90 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jButton91 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton92 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jButton93 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jButton94 = new javax.swing.JButton();
        jLabelTour = new javax.swing.JLabel();
        jButton61 = new javax.swing.JButton();
        jButton62 = new javax.swing.JButton();
        jButton99 = new javax.swing.JButton();
        jButton101 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        jButton100.setForeground(new java.awt.Color(255, 255, 255));
        jButton100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Cavalier blanc.png"))); // NOI18N
        jButton100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton100ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Noms des joueurs");

        jLabel2.setText("Nom du premier joueur :");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabelOrdi.setText("Nom du deuxieme joueur :");

        jButton1.setText("Retour");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Valider");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton47.setBackground(new java.awt.Color(204, 204, 204));
        jButton47.setForeground(new java.awt.Color(255, 255, 255));
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });

        jButton48.setForeground(new java.awt.Color(255, 255, 255));
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });

        jButton96.setForeground(new java.awt.Color(255, 255, 255));
        jButton96.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Cavalier noir (1).png"))); // NOI18N
        jButton96.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton96ActionPerformed(evt);
            }
        });

        jButton49.setBackground(new java.awt.Color(204, 204, 204));
        jButton49.setForeground(new java.awt.Color(255, 255, 255));
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });

        jButton50.setBackground(new java.awt.Color(204, 204, 204));
        jButton50.setForeground(new java.awt.Color(255, 255, 255));
        jButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50ActionPerformed(evt);
            }
        });

        jButton51.setForeground(new java.awt.Color(255, 255, 255));
        jButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton51ActionPerformed(evt);
            }
        });

        jButton52.setBackground(new java.awt.Color(204, 204, 204));
        jButton52.setForeground(new java.awt.Color(255, 255, 255));
        jButton52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton52ActionPerformed(evt);
            }
        });

        jButton32.setBackground(new java.awt.Color(204, 204, 204));
        jButton32.setForeground(new java.awt.Color(255, 255, 255));
        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Pion blanc.png"))); // NOI18N
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton97.setForeground(new java.awt.Color(255, 255, 255));
        jButton97.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Pion noir.png"))); // NOI18N
        jButton97.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton97ActionPerformed(evt);
            }
        });

        jButton53.setForeground(new java.awt.Color(255, 255, 255));
        jButton53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton53ActionPerformed(evt);
            }
        });

        jButton98.setForeground(new java.awt.Color(255, 255, 255));
        jButton98.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Pion noir.png"))); // NOI18N
        jButton98.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton98ActionPerformed(evt);
            }
        });

        jButton54.setBackground(new java.awt.Color(204, 204, 204));
        jButton54.setForeground(new java.awt.Color(255, 255, 255));
        jButton54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton54ActionPerformed(evt);
            }
        });

        jButton60.setBackground(new java.awt.Color(204, 204, 204));
        jButton60.setForeground(new java.awt.Color(255, 255, 255));
        jButton60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Pion noir.png"))); // NOI18N
        jButton60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton60ActionPerformed(evt);
            }
        });

        jButton55.setForeground(new java.awt.Color(255, 255, 255));
        jButton55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton55ActionPerformed(evt);
            }
        });

        jButton56.setForeground(new java.awt.Color(255, 255, 255));
        jButton56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton56ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(0, 0, 0));
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton57.setForeground(new java.awt.Color(255, 255, 255));
        jButton57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Pion noir.png"))); // NOI18N
        jButton57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton57ActionPerformed(evt);
            }
        });

        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton58.setBackground(new java.awt.Color(204, 204, 204));
        jButton58.setForeground(new java.awt.Color(255, 255, 255));
        jButton58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton58ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(204, 204, 204));
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton59.setBackground(new java.awt.Color(204, 204, 204));
        jButton59.setForeground(new java.awt.Color(255, 255, 255));
        jButton59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Pion noir.png"))); // NOI18N
        jButton59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton59ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(0, 0, 0));
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton63.setBackground(new java.awt.Color(204, 204, 204));
        jButton63.setForeground(new java.awt.Color(255, 255, 255));
        jButton63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton63ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(0, 0, 0));
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton64.setBackground(new java.awt.Color(204, 204, 204));
        jButton64.setForeground(new java.awt.Color(255, 255, 255));
        jButton64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton64ActionPerformed(evt);
            }
        });

        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton65.setForeground(new java.awt.Color(255, 255, 255));
        jButton65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Cavalier noir (1).png"))); // NOI18N
        jButton65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton65ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(0, 0, 0));
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(0, 0, 0));
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setForeground(new java.awt.Color(255, 255, 255));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setBackground(new java.awt.Color(204, 204, 204));
        jButton18.setForeground(new java.awt.Color(255, 255, 255));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 0, 0));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Chateau_blanc.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(0, 0, 0));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(0, 0, 0));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton19.setBackground(new java.awt.Color(0, 0, 0));
        jButton19.setForeground(new java.awt.Color(255, 255, 255));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(0, 0, 0));
        jButton20.setForeground(new java.awt.Color(255, 255, 255));
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton66.setBackground(new java.awt.Color(204, 204, 204));
        jButton66.setForeground(new java.awt.Color(255, 255, 255));
        jButton66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton66ActionPerformed(evt);
            }
        });

        jButton68.setBackground(new java.awt.Color(204, 204, 204));
        jButton68.setForeground(new java.awt.Color(255, 255, 255));
        jButton68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton68ActionPerformed(evt);
            }
        });

        jButton69.setForeground(new java.awt.Color(255, 255, 255));
        jButton69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton69ActionPerformed(evt);
            }
        });

        jButton70.setForeground(new java.awt.Color(255, 255, 255));
        jButton70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton70ActionPerformed(evt);
            }
        });

        jButton21.setForeground(new java.awt.Color(255, 255, 255));
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton78.setBackground(new java.awt.Color(0, 0, 0));
        jButton78.setForeground(new java.awt.Color(255, 255, 255));
        jButton78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton78ActionPerformed(evt);
            }
        });

        jButton22.setBackground(new java.awt.Color(204, 204, 204));
        jButton22.setForeground(new java.awt.Color(255, 255, 255));
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton79.setBackground(new java.awt.Color(0, 0, 0));
        jButton79.setForeground(new java.awt.Color(255, 255, 255));
        jButton79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton79ActionPerformed(evt);
            }
        });

        jButton23.setForeground(new java.awt.Color(255, 255, 255));
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton80.setBackground(new java.awt.Color(0, 0, 0));
        jButton80.setForeground(new java.awt.Color(255, 255, 255));
        jButton80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton80ActionPerformed(evt);
            }
        });

        jButton24.setForeground(new java.awt.Color(255, 255, 255));
        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Pion blanc.png"))); // NOI18N
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton81.setForeground(new java.awt.Color(255, 255, 255));
        jButton81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton81ActionPerformed(evt);
            }
        });

        jButton25.setBackground(new java.awt.Color(204, 204, 204));
        jButton25.setForeground(new java.awt.Color(255, 255, 255));
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton82.setForeground(new java.awt.Color(255, 255, 255));
        jButton82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton82ActionPerformed(evt);
            }
        });

        jButton27.setForeground(new java.awt.Color(255, 255, 255));
        jButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Cavalier blanc.png"))); // NOI18N
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton83.setBackground(new java.awt.Color(0, 0, 0));
        jButton83.setForeground(new java.awt.Color(255, 255, 255));
        jButton83.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton83ActionPerformed(evt);
            }
        });

        jButton28.setBackground(new java.awt.Color(204, 204, 204));
        jButton28.setForeground(new java.awt.Color(255, 255, 255));
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setBackground(new java.awt.Color(204, 204, 204));
        jButton29.setForeground(new java.awt.Color(255, 255, 255));
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton30.setForeground(new java.awt.Color(255, 255, 255));
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jButton31.setBackground(new java.awt.Color(204, 204, 204));
        jButton31.setForeground(new java.awt.Color(255, 255, 255));
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jButton84.setBackground(new java.awt.Color(204, 204, 204));
        jButton84.setForeground(new java.awt.Color(255, 255, 255));
        jButton84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton84ActionPerformed(evt);
            }
        });

        jButton71.setForeground(new java.awt.Color(255, 255, 255));
        jButton71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton71ActionPerformed(evt);
            }
        });

        jButton72.setBackground(new java.awt.Color(204, 204, 204));
        jButton72.setForeground(new java.awt.Color(255, 255, 255));
        jButton72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton72ActionPerformed(evt);
            }
        });

        jButton73.setBackground(new java.awt.Color(0, 0, 0));
        jButton73.setForeground(new java.awt.Color(255, 255, 255));
        jButton73.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton73ActionPerformed(evt);
            }
        });

        jButton33.setForeground(new java.awt.Color(255, 255, 255));
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton74.setBackground(new java.awt.Color(0, 0, 0));
        jButton74.setForeground(new java.awt.Color(255, 255, 255));
        jButton74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton74ActionPerformed(evt);
            }
        });

        jButton75.setBackground(new java.awt.Color(204, 204, 204));
        jButton75.setForeground(new java.awt.Color(255, 255, 255));
        jButton75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton75ActionPerformed(evt);
            }
        });

        jButton35.setBackground(new java.awt.Color(204, 204, 204));
        jButton35.setForeground(new java.awt.Color(255, 255, 255));
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jButton76.setForeground(new java.awt.Color(255, 255, 255));
        jButton76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton76ActionPerformed(evt);
            }
        });

        jButton36.setBackground(new java.awt.Color(204, 204, 204));
        jButton36.setForeground(new java.awt.Color(255, 255, 255));
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        jButton77.setForeground(new java.awt.Color(255, 255, 255));
        jButton77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton77ActionPerformed(evt);
            }
        });

        jButton37.setForeground(new java.awt.Color(255, 255, 255));
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        jButton85.setBackground(new java.awt.Color(0, 0, 0));
        jButton85.setForeground(new java.awt.Color(255, 255, 255));
        jButton85.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton85ActionPerformed(evt);
            }
        });

        jButton38.setBackground(new java.awt.Color(204, 204, 204));
        jButton38.setForeground(new java.awt.Color(255, 255, 255));
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        jButton86.setBackground(new java.awt.Color(0, 0, 0));
        jButton86.setForeground(new java.awt.Color(255, 255, 255));
        jButton86.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton86ActionPerformed(evt);
            }
        });

        jButton39.setForeground(new java.awt.Color(255, 255, 255));
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        jButton40.setBackground(new java.awt.Color(204, 204, 204));
        jButton40.setForeground(new java.awt.Color(255, 255, 255));
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        jButton41.setForeground(new java.awt.Color(255, 255, 255));
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });

        jButton42.setForeground(new java.awt.Color(255, 255, 255));
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        jButton87.setBackground(new java.awt.Color(0, 0, 0));
        jButton87.setForeground(new java.awt.Color(255, 255, 255));
        jButton87.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton87ActionPerformed(evt);
            }
        });

        jButton88.setForeground(new java.awt.Color(255, 255, 255));
        jButton88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Chateau_noir.png"))); // NOI18N
        jButton88.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton88ActionPerformed(evt);
            }
        });

        jButton89.setBackground(new java.awt.Color(0, 0, 0));
        jButton89.setForeground(new java.awt.Color(255, 255, 255));
        jButton89.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton89ActionPerformed(evt);
            }
        });

        jButton90.setBackground(new java.awt.Color(0, 0, 0));
        jButton90.setForeground(new java.awt.Color(255, 255, 255));
        jButton90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton90ActionPerformed(evt);
            }
        });

        jButton43.setForeground(new java.awt.Color(255, 255, 255));
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        jButton91.setBackground(new java.awt.Color(0, 0, 0));
        jButton91.setForeground(new java.awt.Color(255, 255, 255));
        jButton91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton91ActionPerformed(evt);
            }
        });

        jButton44.setBackground(new java.awt.Color(204, 204, 204));
        jButton44.setForeground(new java.awt.Color(255, 255, 255));
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jButton92.setText("Sauvegarder la partie");
        jButton92.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton92ActionPerformed(evt);
            }
        });

        jButton45.setBackground(new java.awt.Color(204, 204, 204));
        jButton45.setForeground(new java.awt.Color(255, 255, 255));
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });

        jButton93.setText("Effectuer un déplacement");
        jButton93.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton93ActionPerformed(evt);
            }
        });

        jButton46.setForeground(new java.awt.Color(255, 255, 255));
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        jButton94.setText("Effectuer une capture");
        jButton94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton94ActionPerformed(evt);
            }
        });

        jButton61.setForeground(new java.awt.Color(255, 255, 255));
        jButton61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Pion blanc.png"))); // NOI18N
        jButton61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton61ActionPerformed(evt);
            }
        });

        jButton62.setForeground(new java.awt.Color(255, 255, 255));
        jButton62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Pion blanc.png"))); // NOI18N
        jButton62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton62ActionPerformed(evt);
            }
        });

        jButton99.setBackground(new java.awt.Color(204, 204, 204));
        jButton99.setForeground(new java.awt.Color(255, 255, 255));
        jButton99.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Pion blanc.png"))); // NOI18N
        jButton99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton99ActionPerformed(evt);
            }
        });

        jButton101.setForeground(new java.awt.Color(255, 255, 255));
        jButton101.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pImages/Cavalier blanc.png"))); // NOI18N
        jButton101.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton101ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel3.setText("en attente de la saisie des joueurs");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelOrdi)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldOrdi, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(43, 43, 43)
                                        .addComponent(jLabelTour, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton2)
                                        .addGap(150, 150, 150)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton85, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton86, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton87, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton88, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton89, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton90, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton91, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton101, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton99, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton61, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton62, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton47, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton48, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton49, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton56, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton50, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton51, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton52, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton53, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton54, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton55, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton58, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton57, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton59, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton97, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton60, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton98, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton63, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton70, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton64, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton65, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton66, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton96, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton68, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton69, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton83, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton78, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton82, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton84, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton81, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton79, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton80, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(200, 200, 200)
                                .addComponent(jButton77, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton74, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton73, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton72, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton76, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton75, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addComponent(jButton92, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton93)
                                    .addComponent(jButton94, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(118, 118, 118)
                                .addComponent(jButton1)))))
                .addContainerGap(190, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(182, 182, 182)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelTour, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(46, 46, 46)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(jButton92, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelOrdi)
                                    .addComponent(jTextFieldOrdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(76, 76, 76)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton93, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(122, 122, 122)
                                .addComponent(jButton1))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton101, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton62, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton61, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton99, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton94, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton47, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton48, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton49, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton50, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton51, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton52, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton53, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton54, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton55, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton56, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton58, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton57, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton59, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton63, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton97, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton98, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton60, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton64, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton65, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton66, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton68, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton69, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton70, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton96, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton73, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton74, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton72, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton76, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton75, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton77, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton83, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton78, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton79, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton80, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton81, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton82, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton84, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton85, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton86, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton87, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton88, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton89, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton90, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton91, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String nom1 = jTextField1.getText().trim();
        String nom2 = contreOrdi ? "Ordinateur" : jTextFieldOrdi.getText().trim();

        if (nom1.isEmpty() || (!contreOrdi && nom2.isEmpty())) {
            JOptionPane.showMessageDialog(this, 
                contreOrdi ? "Veuillez entrer votre pseudo." 
                          : "Veuillez entrer les pseudos des deux joueurs.");
            return;
        }
        Random rand = new Random();
        boolean joueur1EstBlanc = rand.nextBoolean();

        if (joueur1EstBlanc) {
            nomJoueurBlanc = nom1;
            nomJoueurNoir = nom2;
        } else {
            nomJoueurBlanc = nom2;
            nomJoueurNoir = nom1;
        }

        estTourDesBlancs = true; // Le joueur blanc commence

        joueursPrets = true;
        mettreAJourTexteTour();

        // Si c’est contre l’ordi ET que l’ordinateur est le joueur blanc, il doit commencer à jouer immédiatement
        if (contreOrdi && nomJoueurBlanc.equals(nomOrdi)) {
            SwingUtilities.invokeLater(() -> jouerCoupIA());
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
        this.getParent().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        // TODO add your handling code here:  case E7 
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
        // TODO add your handling code here:   case F7
    }//GEN-LAST:event_jButton48ActionPerformed

    private void jButton96ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton96ActionPerformed
        // TODO add your handling code here:  case E10
    }//GEN-LAST:event_jButton96ActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        // TODO add your handling code here:   case G7
    }//GEN-LAST:event_jButton49ActionPerformed

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        // TODO add your handling code here:  case B8
    }//GEN-LAST:event_jButton50ActionPerformed

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        // TODO add your handling code here:  case C8
    }//GEN-LAST:event_jButton51ActionPerformed

    private void jButton52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton52ActionPerformed
        // TODO add your handling code here:  case D8 
    }//GEN-LAST:event_jButton52ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        // TODO add your handling code here:  case E5
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton97ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton97ActionPerformed
        // TODO add your handling code here: case D9
    }//GEN-LAST:event_jButton97ActionPerformed

    private void jButton53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton53ActionPerformed
        // TODO add your handling code here: case E8
    }//GEN-LAST:event_jButton53ActionPerformed

    private void jButton98ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton98ActionPerformed
        // TODO add your handling code here:   case F9
    }//GEN-LAST:event_jButton98ActionPerformed

    private void jButton54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton54ActionPerformed
        // TODO add your handling code here:  case F8
    }//GEN-LAST:event_jButton54ActionPerformed

    private void jButton60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton60ActionPerformed
        // TODO add your handling code here:  case E9
    }//GEN-LAST:event_jButton60ActionPerformed

    private void jButton55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton55ActionPerformed
        // TODO add your handling code here:  case G8
    }//GEN-LAST:event_jButton55ActionPerformed

    private void jButton56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton56ActionPerformed
        // TODO add your handling code here:   case A8
    }//GEN-LAST:event_jButton56ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton57ActionPerformed
        // TODO add your handling code here:  case B9
    }//GEN-LAST:event_jButton57ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:  case B3
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton58ActionPerformed
        // TODO add your handling code here:  case A9
    }//GEN-LAST:event_jButton58ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:  case C3
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton59ActionPerformed
        // TODO add your handling code here:  case C9
    }//GEN-LAST:event_jButton59ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton63ActionPerformed
        // TODO add your handling code here:   case G9
    }//GEN-LAST:event_jButton63ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton64ActionPerformed
        // TODO add your handling code here: case B10
    }//GEN-LAST:event_jButton64ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:  case E2
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton65ActionPerformed
        // TODO add your handling code here:  case C10
    }//GEN-LAST:event_jButton65ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:  case C2
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        System.out.print("bouton 3 !!");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:   case D1
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton66ActionPerformed
        // TODO add your handling code here:  case D10
    }//GEN-LAST:event_jButton66ActionPerformed

    private void jButton68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton68ActionPerformed
        // TODO add your handling code here:  case F10
    }//GEN-LAST:event_jButton68ActionPerformed

    private void jButton69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton69ActionPerformed
        // TODO add your handling code here:  case G10
    }//GEN-LAST:event_jButton69ActionPerformed

    private void jButton70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton70ActionPerformed
        // TODO add your handling code here:  case A10
    }//GEN-LAST:event_jButton70ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:  case D3
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton78ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton78ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:  case D2
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton79ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton79ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:  case F3
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton80ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton80ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:  case B5
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton81ActionPerformed
        // TODO add your handling code here:  case E12
    }//GEN-LAST:event_jButton81ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here: case B4
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton82ActionPerformed
        // TODO add your handling code here: case C12
    }//GEN-LAST:event_jButton82ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:  case C4
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton83ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton83ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton83ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:  case D4
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // TODO add your handling code here:  case F4
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:  case G4
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        // TODO add your handling code here:  case A5
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton84ActionPerformed
        // TODO add your handling code here:  CASE D12
    }//GEN-LAST:event_jButton84ActionPerformed

    private void jButton71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton71ActionPerformed
        // TODO add your handling code here:  case B11
    }//GEN-LAST:event_jButton71ActionPerformed

    private void jButton72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton72ActionPerformed
        // TODO add your handling code here:  case C11
    }//GEN-LAST:event_jButton72ActionPerformed

    private void jButton73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton73ActionPerformed
// TODO add your handling code here:

    }//GEN-LAST:event_jButton73ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        // TODO add your handling code here: case A4
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton74ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton74ActionPerformed

    private void jButton75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton75ActionPerformed
        // TODO add your handling code here: case E11
    }//GEN-LAST:event_jButton75ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        // TODO add your handling code here:   case G5
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton76ActionPerformed
        // TODO add your handling code here: case D11
    }//GEN-LAST:event_jButton76ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // TODO add your handling code here:   case B6
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton77ActionPerformed
        // TODO add your handling code here:  case F11
    }//GEN-LAST:event_jButton77ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        // TODO add your handling code here:   case C6
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton85ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton85ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton85ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        // TODO add your handling code here:  case D6
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton86ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton86ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        // TODO add your handling code here: case E6
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        // TODO add your handling code here:   case F6
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        // TODO add your handling code here:  case G6
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        // TODO add your handling code here:  case A6
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton87ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton87ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton87ActionPerformed

    private void jButton88ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton88ActionPerformed
        // TODO add your handling code here:  case D13
    }//GEN-LAST:event_jButton88ActionPerformed

    private void jButton89ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton89ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton89ActionPerformed

    private void jButton90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton90ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton90ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        // TODO add your handling code here:  case B7
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jButton91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton91ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton91ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        // TODO add your handling code here:  case A7 
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        // TODO add your handling code here:  case C7
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        // TODO add your handling code here:   case D7 
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton61ActionPerformed
        // TODO add your handling code here:  case D5
    }//GEN-LAST:event_jButton61ActionPerformed

    private void jButton62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton62ActionPerformed
        // TODO add your handling code here:   case F5
    }//GEN-LAST:event_jButton62ActionPerformed

    private void jButton99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton99ActionPerformed
        // TODO add your handling code here: case C5
    }//GEN-LAST:event_jButton99ActionPerformed

    private void jButton100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton100ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton100ActionPerformed

    private void jButton101ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton101ActionPerformed
        // TODO add your handling code here:  case E4
    }//GEN-LAST:event_jButton101ActionPerformed

    private void jButton94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton94ActionPerformed
        // TODO add your handling code here: // effectuer une capture
    }//GEN-LAST:event_jButton94ActionPerformed

    private void jButton93ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton93ActionPerformed
        // TODO add your handling code here: mettre un listener effectuer un deplacement
        if (!isPieceSelected) {
        JOptionPane.showMessageDialog(this, "Cliquez sur un pion à déplacer.");
        isPieceSelected = true;
    } else {
        JOptionPane.showMessageDialog(this, "Cliquez sur la case de destination.");
    }
    }//GEN-LAST:event_jButton93ActionPerformed

    private void jButton92ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton92ActionPerformed
        // TODO add your handling code here: sauvegarder la partie bouton sauvegarder la partie
        if (!joueursPrets) {
        JOptionPane.showMessageDialog(this, "Vous devez d'abord commencer une partie !");
        return;
    }

    sauvegarderPartie();
    }//GEN-LAST:event_jButton92ActionPerformed

    private void jTextFieldOrdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldOrdiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldOrdiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FNomsJoueurs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FNomsJoueurs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FNomsJoueurs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FNomsJoueurs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FNomsJoueurs dialog = new FNomsJoueurs(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton100;
    private javax.swing.JButton jButton101;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton52;
    private javax.swing.JButton jButton53;
    private javax.swing.JButton jButton54;
    private javax.swing.JButton jButton55;
    private javax.swing.JButton jButton56;
    private javax.swing.JButton jButton57;
    private javax.swing.JButton jButton58;
    private javax.swing.JButton jButton59;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton60;
    private javax.swing.JButton jButton61;
    private javax.swing.JButton jButton62;
    private javax.swing.JButton jButton63;
    private javax.swing.JButton jButton64;
    private javax.swing.JButton jButton65;
    private javax.swing.JButton jButton66;
    private javax.swing.JButton jButton68;
    private javax.swing.JButton jButton69;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton70;
    private javax.swing.JButton jButton71;
    private javax.swing.JButton jButton72;
    private javax.swing.JButton jButton73;
    private javax.swing.JButton jButton74;
    private javax.swing.JButton jButton75;
    private javax.swing.JButton jButton76;
    private javax.swing.JButton jButton77;
    private javax.swing.JButton jButton78;
    private javax.swing.JButton jButton79;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton80;
    private javax.swing.JButton jButton81;
    private javax.swing.JButton jButton82;
    private javax.swing.JButton jButton83;
    private javax.swing.JButton jButton84;
    private javax.swing.JButton jButton85;
    private javax.swing.JButton jButton86;
    private javax.swing.JButton jButton87;
    private javax.swing.JButton jButton88;
    private javax.swing.JButton jButton89;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButton90;
    private javax.swing.JButton jButton91;
    private javax.swing.JButton jButton92;
    private javax.swing.JButton jButton93;
    private javax.swing.JButton jButton94;
    private javax.swing.JButton jButton96;
    private javax.swing.JButton jButton97;
    private javax.swing.JButton jButton98;
    private javax.swing.JButton jButton99;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelOrdi;
    private javax.swing.JLabel jLabelTour;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextFieldOrdi;
    // End of variables declaration//GEN-END:variables
}

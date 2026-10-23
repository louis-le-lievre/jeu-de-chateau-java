package pTraitement;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import pTraitement.Plateau;

/*
 * COUNILH QUENTIN LE LIEVRE LOUIS THOUVENIN AMELIE
 * jeu de chateau
 * rapport intermédiraire mars 2025
 */

public class Joueur implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private String couleur;

    public Joueur(String nom, String couleur) {
        this.nom = nom;
        this.couleur = couleur;
    }

    // Nouveau constructeur avec seulement le nom
    public Joueur(String nom) {
        this.nom = nom;
        this.couleur = null; // La couleur sera définie plus tard
    }

    public String getNom() {
        return nom;
    }

    public String getCouleur() {
        return couleur;
    }

    // Convertir l'objet en texte
    public String toTexte() {
        return nom + "," + couleur; // Format : "nom,couleur"
    }

    // Reconstruire l'objet à partir du texte
    public static Joueur fromTexte(String texte) {
        String[] parties = texte.split(","); // Séparer le nom et la couleur
        return new Joueur(parties[0], parties[1]);
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public void jouerTour(Plateau plateau, Partie partie) {
        // Logique pour jouer un tour avec accès à la partie
    }

    public boolean aGagne(Plateau plateau) {
        // Vérifier les deux conditions de victoire
        boolean aucunePieceAdverse = plateau.aucunePieceAdversaire(getCouleur());
        boolean positionCritique = plateau.pieceSurPositionCritique(getCouleur());
    
        return aucunePieceAdverse || positionCritique;
    }    

    public boolean effectuerDeplacement(Plateau plateau, int[] caseDepart, int[] caseArrivee) {
        Scanner scanner = new Scanner(System.in);
        int ligneDepart, colonneDepart, ligneArrivee, colonneArrivee;
    
        if (caseDepart == null || caseArrivee == null) {
            // Mode non graphique : demander les coordonnées à l'utilisateur
            System.out.println(nom + ", veuillez entrer la case de départ (format : colonne ligne) : ");
            String depart = scanner.nextLine().toUpperCase(); // Convertir en majuscules
            System.out.println(nom + ", veuillez entrer la case d'arrivée (format : colonne ligne) : ");
            String arrivee = scanner.nextLine().toUpperCase(); // Convertir en majuscules
    
            // Vérification du format
            if (!verifierFormat(depart) || !verifierFormat(arrivee)) {
                System.out.println("Format invalide. Veuillez entrer une lettre suivie d'un chiffre.");
                return false;
            }
    
            ligneDepart = Integer.parseInt(depart.substring(1)) - 1;
            colonneDepart = depart.charAt(0) - 'A';
            ligneArrivee = Integer.parseInt(arrivee.substring(1)) - 1;
            colonneArrivee = arrivee.charAt(0) - 'A';
        } else {
            // Mode graphique : utiliser les coordonnées fournies
            ligneDepart = caseDepart[0];
            colonneDepart = caseDepart[1];
            ligneArrivee = caseArrivee[0];
            colonneArrivee = caseArrivee[1];
        }
    
        // Vérification supplémentaire pour s'assurer que les indices sont dans les limites du plateau
        if (ligneDepart < 0 || ligneDepart >= plateau.getLignes() || colonneDepart < 0 || colonneDepart >= plateau.getColonnes() ||
            ligneArrivee < 0 || ligneArrivee >= plateau.getLignes() || colonneArrivee < 0 || colonneArrivee >= plateau.getColonnes()) {
            System.out.println("Case invalide. Veuillez entrer une case valide sur le plateau.");
            return false;
        }
    
        // Vérification de la pièce sur la case de départ
        String piece = plateau.getPiece(ligneDepart, colonneDepart);
        if (getCouleur().equals("Blanc") && !piece.equals("CB") && !piece.equals("PB")) {
            System.out.println("La case de départ ne contient pas une de vos pièces. Veuillez réessayer.");
            return false;
        }
        if (getCouleur().equals("Noir") && !piece.equals("CN") && !piece.equals("PN")) {
            System.out.println("La case de départ ne contient pas une de vos pièces. Veuillez réessayer.");
            return false;
        }
    
        // Vérification du déplacement
        if (plateau.estDeplacementValide(ligneDepart, colonneDepart, ligneArrivee, colonneArrivee)) {
            plateau.deplacerPiece(ligneDepart, colonneDepart, ligneArrivee, colonneArrivee);
            System.out.println("Déplacement effectué avec succès.");
            return true;
        } else {
            System.out.println("Déplacement invalide. Veuillez réessayer.");
            return false;
        }
    }          
    
    public boolean verifierFormat(String position) {
        // Vérifie si la position est une lettre suivie d'un ou plusieurs chiffres
        return position.matches("[A-Za-z]\\d+");
    }        

    public boolean effectuerCapture(Plateau plateau, int[] caseDepart, int[] caseArrivee) {
   
        int ligneDepart, colonneDepart, ligneArrivee, colonneArrivee;
        Scanner scanner = new Scanner(System.in);
        boolean captureSupplementaire;
   
        // Initialisation de la case de départ
        if (caseDepart == null) {
            System.out.print("Entrez la case de départ (ex: A2) : ");
            String input = scanner.nextLine().toUpperCase();
            ligneDepart = Integer.parseInt(input.substring(1)) - 1;
            colonneDepart = input.charAt(0) - 'A';
        } else {
            ligneDepart = caseDepart[0];
            colonneDepart = caseDepart[1];
        }
   
        do {
            // Boucle pour forcer une saisie valide de la case d'arrivée
            boolean saisieValide;
            do {
                System.out.print("Entrez la case d'arrivée (ex: C4) : ");
                String inputArrivee = scanner.nextLine().toUpperCase();
                ligneArrivee = Integer.parseInt(inputArrivee.substring(1)) - 1;
                colonneArrivee = inputArrivee.charAt(0) - 'A';
   
                saisieValide = plateau.estCaptureValide(ligneDepart, colonneDepart, ligneArrivee, colonneArrivee, this);
                if (!saisieValide) {
                    System.out.println("Erreur : Capture invalide !");
                }
            } while (!saisieValide);
   
            // Effectuer la capture
            plateau.capturerPiece(ligneDepart, colonneDepart, ligneArrivee, colonneArrivee);
   
            // Vérifier les captures supplémentaires
            List<int[]> nouvellesCaptures = plateau.trouverCapturesPossibles(ligneArrivee, colonneArrivee, this);
            captureSupplementaire = !nouvellesCaptures.isEmpty();
   
            if (captureSupplementaire) {
                System.out.println("\n=== Capture supplémentaire obligatoire ===");
                caseDepart = new int[]{ligneArrivee, colonneArrivee}; // Mise à jour de la case de départ
                ligneDepart = caseDepart[0];
                colonneDepart = caseDepart[1];
            }
            plateau.afficherPlateau();
   
        } while (captureSupplementaire); // Continuer tant qu'il y a des captures supplémentaires
        return true;
    }
               
    
    public boolean possedeCavalier(Plateau plateau) {
        // Vérifie s'il y a au moins un cavalier sur le plateau
        for (int i = 0; i < plateau.getLIGNES(); i++) {
            for (int j = 0; j < plateau.getCOLONNES(); j++) {
                String piece = plateau.getPlateau()[i][j];
                if (piece != null && piece.equals("C" + getCouleur().substring(0, 1))) {
                    return true; // Au moins un cavalier trouvé
                }
            }
        }
        return false; // Aucun cavalier trouvé
    }
    
    public boolean effectuerChargeCavalier(Plateau plateau) {
        Scanner scanner = new Scanner(System.in);
        boolean actionTerminee = false;
        boolean actionValide = false;
        int ligne = -1, colonne = -1;
    
        // Demander la case de départ pour le cavalier
        while (ligne == -1 || colonne == -1) {
            System.out.print("Entrez la case de départ de votre cavalier (format : colonne ligne, ex : C4) : ");
            String depart = scanner.nextLine().toUpperCase();
    
            // Vérification du format
            if (!verifierFormat(depart)) {
                System.out.println("Format invalide. Veuillez entrer une lettre suivie d'un chiffre.");
                continue;
            }
    
            // Convertir les coordonnées de la case
            int ligneDepart = Integer.parseInt(depart.substring(1)) - 1;
            int colonneDepart = depart.charAt(0) - 'A';
    
            // Vérifier si la case contient un cavalier de la couleur correcte
            if (!plateau.estCavalier(getCouleur(), ligneDepart, colonneDepart)) {
                System.out.println("Ce n'est pas un cavalier valide ou il appartient à l'adversaire. Veuillez réessayer.");
            } else {
                ligne = ligneDepart;
                colonne = colonneDepart;
            }
        }
    
        // Boucle pour effectuer les actions de la charge du cavalier (déplacement ou capture)
        while (!actionTerminee) {
            System.out.println("Que souhaitez-vous faire ?");
            System.out.println("1. Effectuer un déplacement");
            System.out.println("2. Effectuer une capture");
            System.out.println("3. Terminer la charge");
            System.out.print("Votre choix : ");
    
            if (scanner.hasNextInt()) {
                int choix = scanner.nextInt();
                int nouvelleLigne, nouvelleColonne;
    
                switch (choix) {
                    case 1: // Déplacement
                        System.out.print("Entrez la case de destination (format : colonne ligne, ex : C4) : ");
                        String destination = scanner.next();
                        destination = destination.toUpperCase();
    
                        if (!verifierFormat(destination)) {
                            System.out.println("Format invalide. Veuillez entrer une lettre suivie d'un chiffre.");
                            break;
                        }
    
                        nouvelleLigne = Integer.parseInt(destination.substring(1)) - 1;
                        nouvelleColonne = destination.charAt(0) - 'A';
    
                        if (plateau.estDeplacementValide(ligne, colonne, nouvelleLigne, nouvelleColonne)) {
                            plateau.deplacerPiece(ligne, colonne, nouvelleLigne, nouvelleColonne);
                            ligne = nouvelleLigne;
                            colonne = nouvelleColonne;
                            actionValide = true;

                            // Actualiser l'affichage du plateau
                            plateau.afficherPlateau();
                        } else {
                            System.out.println("Déplacement invalide. Veuillez réessayer.");
                        }
                        break;
    
                    case 2: // Capture
                        System.out.print("Entrez la case de destination pour la capture (format : colonne ligne, ex : C4) : ");
                        String cible = scanner.next();
                        cible = cible.toUpperCase();
    
                        if (!verifierFormat(cible)) {
                            System.out.println("Format invalide. Veuillez entrer une lettre suivie d'un chiffre.");
                            break;
                        }
    
                        nouvelleLigne = Integer.parseInt(cible.substring(1)) - 1;
                        nouvelleColonne = cible.charAt(0) - 'A';
    
                        if (plateau.estCaptureValide(ligne, colonne, nouvelleLigne, nouvelleColonne,this)) {
                            plateau.capturerPiece(ligne, colonne, nouvelleLigne, nouvelleColonne);
                            ligne = nouvelleLigne;
                            colonne = nouvelleColonne;
                            actionValide = true;

                            // Actualiser l'affichage du plateau
                            plateau.afficherPlateau();
                        } else {
                            System.out.println("Capture invalide. Veuillez réessayer.");
                        }
                        break;
    
                    case 3: // Terminer la charge
                        actionTerminee = true;
                        break;
    
                    default:
                        System.out.println("Choix invalide. Veuillez entrer 1, 2 ou 3.");
                }
            } else {
                scanner.next(); // Consommer l'entrée invalide
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            }
        }
    
        if (!actionValide) {
            System.out.println("Aucune action n'a été effectuée.");
        }
        return actionValide; // Retourne true si au moins une action a été effectuée
    }            

    public static Joueur[] initialiserJoueurs(String nomHumain1, String nomHumain2) {
        Joueur joueur1 = new Joueur(nomHumain1, ""); // Le nom est donné, couleur à attribuer plus tard
        Joueur joueur2 = new Joueur(nomHumain2, ""); // Même chose pour le deuxième joueur
        Partie partie = new Partie(joueur1, joueur2); // Création de la partie avec les deux joueurs
        partie.attribuerCouleursEtOrdre(joueur1, joueur2); // Attribution aléatoire des couleurs et ordre
        return new Joueur[]{joueur1, joueur2};
    }
    
    public static Joueur[] initialiserJoueurs(String nomHumain1) {
        Joueur joueur1 = new Joueur(nomHumain1, ""); // Le joueur humain
        Joueur ordinateur = new Ordinateur("Ordinateur", ""); // Le joueur ordinateur avec un nom
        Partie partie = new Partie(joueur1, ordinateur); // Création de la partie avec un humain et un ordinateur
        partie.attribuerCouleursEtOrdre(joueur1, ordinateur); // Attribution aléatoire des couleurs et ordre
        return new Joueur[]{joueur1, ordinateur};
    }    
}
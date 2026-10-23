package pTraitement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.Serializable;
import pTraitement.Plateau;

/*
 * COUNILH QUENTIN LE LIEVRE LOUIS THOUVENIN AMELIE
 * jeu de chateau
 * rapport intermédiraire mars 2025
 */

public class Partie implements Serializable {
    private static final long serialVersionUID = 1L; // 🔹 Permet la compatibilité entre versions
    private Plateau plateau;
    private Joueur joueurHumain1;
    private Joueur joueurHumain2;
    private Joueur joueurOrdinateur;
    private Joueur joueur2;
    private boolean estModeGraphique;
    private Joueur joueurCourant;
    private boolean partieTerminee = false; // Variable de classe

    public boolean isPartieTerminee() {
        return partieTerminee;
    }

    public void setPartieTerminee(boolean partieTerminee) {
        this.partieTerminee = partieTerminee;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public Joueur getJoueurHumain1() {
        return joueurHumain1;
    }

    public Joueur getJoueurHumain2() {
        return joueurHumain2;
    }
    
    public boolean estTerminee() {
    return this.partieTerminee;
    }
    
    public List<Joueur> getJoueurs() {
        List<Joueur> joueurs = new ArrayList<>();
        joueurs.add(joueur1);
        joueurs.add(joueur2);
        return joueurs;
    }

    public Ordinateur getJoueurOrdinateur() {
        if (joueur2 instanceof Ordinateur) {
            return (Ordinateur) joueur2;
        }
        return null; // Retourne null si ce n'est pas un ordinateur
    }

    // Getter pour estModeGraphique
    public boolean estModeGraphique() {
        return estModeGraphique;
    }

    // Setter pour estModeGraphique
    public void setModeGraphique(boolean estModeGraphique) {
        this.estModeGraphique = estModeGraphique;
    }

    public Partie(Joueur joueur1, Joueur joueur2) {
        this.plateau = new Plateau();
        this.joueurHumain1 = joueur1;
    
        if (joueur2 instanceof Ordinateur) {
            this.joueurOrdinateur = (Ordinateur) joueur2; // Cast explicite si nécessaire
            this.joueurHumain2 = null; // Assurez-vous que joueurHumain2 est null
        } else {
            this.joueurHumain2 = joueur2;
            this.joueurOrdinateur = null; // Assurez-vous que joueurOrdinateur est null
        }
    
        // Définir le joueur courant
        this.joueurCourant = joueurHumain1;
        this.partieTerminee = false;
    
        // Appeler attribuerCouleursEtOrdre en vérifiant si joueur2 est un humain ou l'ordinateur
        attribuerCouleursEtOrdre(joueurHumain1, joueur2);
    }

    public void attribuerCouleursEtOrdre(Joueur joueur1, Joueur joueur2) {
        if (joueur1 == null) {
            throw new IllegalArgumentException("Le joueur 1 ne peut pas être null.");
        }
    
        // Si joueur2 est null, c'est que l'ordinateur est l'adversaire
        if (joueur2 == null) {
            joueur2 = joueurOrdinateur;
        }
    
        // Attribution des couleurs et de l'ordre de jeu
        if (Math.random() < 0.5) {
            joueur1.setCouleur("Blanc");
            joueur2.setCouleur("Noir");
            this.joueurCourant = joueur1;
        } else {
            joueur1.setCouleur("Noir");
            joueur2.setCouleur("Blanc");
            this.joueurCourant = joueur2;
        }
    }    
    
    private void afficherMessageBienvenue() {
        System.out.println(joueurHumain1.getNom() + " joue avec les pièces " + joueurHumain1.getCouleur());
    
        if (joueurHumain2 != null) {
            // Deux joueurs humains
            System.out.println(joueurHumain2.getNom() + " joue avec les pièces " + joueurHumain2.getCouleur());
        } else {
            // Un joueur humain et l'ordinateur
            System.out.println(joueurOrdinateur.getNom() + " joue avec les pièces " + joueurOrdinateur.getCouleur());
        }
    
        System.out.println("Le joueur qui commence est : " + joueurCourant.getNom());
    }    

    public void demarrer() {
        // Si joueurHumain2 est null, utiliser joueurOrdinateur
        if (joueurHumain2 == null) {
            attribuerCouleursEtOrdre(joueurHumain1, joueurOrdinateur);
        } else {
            attribuerCouleursEtOrdre(joueurHumain1, joueurHumain2);
        }
    
        afficherMessageBienvenue(); // Affichage des informations de début
    
        while (!partieTerminee) {
            // Afficher le plateau au début du tour
            plateau.afficherPlateau();
    
            // Vérifier si le jeu est un match nul
            if (plateau.joueurEstBloque(joueurHumain1) &&
                (joueurHumain2 != null ? plateau.joueurEstBloque(joueurHumain2) : plateau.joueurEstBloque(joueurOrdinateur))) {
                System.out.println("Aucun joueur ne peut bouger ou capturer. Match nul !");
                partieTerminee = true;
                break;
            }
    
            // Vérifier si le joueur courant est l'ordinateur
            if (joueurCourant instanceof Ordinateur) {
                joueurCourant.jouerTour(plateau, this);
            } else {
                boolean actionValide = false;
                Scanner scanner = new Scanner(System.in);
    
                while (!actionValide) {
                    System.out.println(joueurCourant.getNom() + ", que souhaitez-vous faire ?");
                    System.out.println("1. Effectuer un déplacement");
                    System.out.println("2. Effectuer une capture");
                    System.out.println("3. Effectuer une charge de cavalier");
                    System.out.println("4. Sauvegarder la partie");
                    System.out.print("Votre choix : ");
    
                    if (scanner.hasNextInt()) {
                        int choix = scanner.nextInt();
    
                        switch (choix) {
                            case 1:
                                actionValide = joueurCourant.effectuerDeplacement(plateau,null,null);
                                break;
                            case 2:
                                actionValide = joueurCourant.effectuerCapture(plateau, null, null);
                                break;
                            case 3:
                                if (joueurCourant.possedeCavalier(plateau)) {
                                    actionValide = joueurCourant.effectuerChargeCavalier(plateau);
                                    if (actionValide) plateau.afficherPlateau();
                                } else {
                                    System.out.println("Vous ne possédez pas de cavalier pour effectuer une charge.");
                                }
                                break;
                            case 4:
                                sauvegarderPartie();
                                break;
                            default:
                                System.out.println("Erreur de saisie. Veuillez choisir 1, 2, 3 ou 4.");
                        }
                    } else {
                        System.out.println("Erreur de saisie. Veuillez entrer un nombre valide (1, 2, 3 ou 4).");
                        scanner.next(); // Consommer l'entrée invalide
                    }
                }
            }
    
            // Vérifier si un joueur a gagné après chaque mouvement
            if (joueurHumain1.aGagne(plateau)) {
                System.out.println("Félicitations, " + joueurHumain1.getNom() + ", vous avez gagné !");
                partieTerminee = true;
            } else if (joueurHumain2 != null && joueurHumain2.aGagne(plateau)) {
                System.out.println("Félicitations, " + joueurHumain2.getNom() + ", vous avez gagné !");
                partieTerminee = true;
            } else if (joueurOrdinateur != null && joueurOrdinateur.aGagne(plateau)) {
                System.out.println("L'ordinateur a gagné.");
                partieTerminee = true;
            }
    
            // Changer de joueur après chaque tour
            changerJoueur();
        }
    
        System.out.println("La partie est terminée.");
    }                            

    public void changerJoueur() {
        
        if (joueurHumain2 != null) {
            // Deux joueurs humains
            joueurCourant = (joueurCourant == joueurHumain1) ? joueurHumain2 : joueurHumain1;
        } else {
            // Un joueur humain et l'ordinateur
            joueurCourant = (joueurCourant == joueurHumain1) ? joueurOrdinateur : joueurHumain1;
        }
    }

    public void sauvegarderPartie(boolean estModeGraphique) {
        String cheminSauvegarde = "sauvegarde.txt";
   
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminSauvegarde))) {
            String[][] plateauActuel = plateau.getPlateau();
            int lignes = plateauActuel.length;
            int colonnes = plateauActuel[0].length;
   
            // Sauvegarder le plateau
            for (String[] ligne : plateauActuel) {
                for (int j = 0; j < colonnes; j++) { // Commencer à la colonne 0
                    String cellule = ligne[j];
                    String cellulePropre = cellule.replaceAll("\u001B\\[[;\\d]*m", ""); // Supprimer les codes ANSI
                    writer.write(cellulePropre + ";");
                }
                writer.newLine();
            }
   
            // Sauvegarder les informations des joueurs
            writer.write("joueurHumain1:" + joueurHumain1.getNom() + "," + joueurHumain1.getCouleur());
            writer.newLine();
            writer.write("joueurHumain2:" + (joueurHumain2 != null ? joueurHumain2.getNom() + "," + joueurHumain2.getCouleur() : "null"));
            writer.newLine();
            writer.write("joueurCourant:" + joueurCourant.getNom() + "," + joueurCourant.getCouleur());
            writer.newLine();
   
            // Sauvegarde de l'état de la partie
            writer.write("partieTerminee:" + partieTerminee);
            writer.newLine();
            writer.write("estModeGraphique:" + estModeGraphique);
            writer.newLine();
   
            System.out.println("Partie sauvegardée avec succès");
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
        }
    }
   
    public void sauvegarderPartie() {
        sauvegarderPartie(false); // Sauvegarde par défaut en mode texte
    }

    public void afficherInformationsJoueurs() {
        System.out.println("Informations des joueurs :");
        System.out.println("Joueur 1 : " + this.joueurHumain1.getNom() + " (" + this.joueurHumain1.getCouleur() + ")");
        if (this.joueurHumain2 != null) {
            System.out.println("Joueur 2 : " + this.joueurHumain2.getNom() + " (" + this.joueurHumain2.getCouleur() + ")");
        }
        System.out.println("Joueur courant : " + this.joueurCourant.getNom());
    }

    public boolean chargerPartie() {
        String cheminSauvegarde = "sauvegarde.txt";
        System.out.println("Tentative de chargement de la partie depuis : " + cheminSauvegarde);
   
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminSauvegarde))) {
            String ligne;
            int ligneIndex = 0;
            String[][] plateauCharge = new String[plateau.getPlateau().length][plateau.getPlateau()[0].length];
   
            System.out.println("Début du chargement du plateau...");
   
            while ((ligne = reader.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.isEmpty()) continue;
   
                // Arrêter la lecture du plateau si on atteint les informations des joueurs
                if (ligne.startsWith("joueur")) break;
   
                String[] cellules = ligne.split(";");
   
                if (ligneIndex >= plateauCharge.length) {
                    System.out.println("Erreur : Trop de lignes dans le plateau.");
                    return false;
                }
   
                for (int j = 0; j < plateauCharge[ligneIndex].length; j++) {
                    if (j < cellules.length) {
                        plateauCharge[ligneIndex][j] = cellules[j].trim(); // Pas de décalage
                    } else {
                        plateauCharge[ligneIndex][j] = ""; // Remplir les colonnes restantes avec du vide
                    }
                }
                ligneIndex++;
            }
   
            System.out.println("Plateau chargé avec succès.");
   
            // Charger les informations des joueurs
            Map<String, String> donnees = new HashMap<>();
            do {
                ligne = ligne.trim();
                if (!ligne.isEmpty() && ligne.contains(":")) {
                    String[] parts = ligne.split(":", 2);
                    if (parts.length == 2) {
                        donnees.put(parts[0], parts[1].trim());
                    }
                }
            } while ((ligne = reader.readLine()) != null);
   
            // Vérifier et charger les joueurs
            if (!donnees.containsKey("joueurHumain1") || !donnees.containsKey("joueurCourant")) {
                System.out.println("Erreur : Données manquantes pour les joueurs.");
                return false;
            }
   
            this.joueurHumain1 = new Joueur(donnees.get("joueurHumain1").split(",")[0], donnees.get("joueurHumain1").split(",")[1]);
   
            if (donnees.containsKey("joueurHumain2") && !donnees.get("joueurHumain2").equals("null")) {
                this.joueurHumain2 = new Joueur(donnees.get("joueurHumain2").split(",")[0], donnees.get("joueurHumain2").split(",")[1]);
            } else {
                this.joueurHumain2 = null;
            }
   
            this.joueurCourant = new Joueur(donnees.get("joueurCourant").split(",")[0], donnees.get("joueurCourant").split(",")[1]);
   
            this.partieTerminee = Boolean.parseBoolean(donnees.getOrDefault("partieTerminee", "false"));
            this.estModeGraphique = Boolean.parseBoolean(donnees.getOrDefault("estModeGraphique", "false"));
   
            // Appliquer les données du plateau chargé
            for (int i = 0; i < plateauCharge.length; i++) {
                for (int j = 0; j < plateauCharge[i].length; j++) {
                    this.plateau.getPlateau()[i][j] = plateauCharge[i][j] != null ? plateauCharge[i][j] : "";
                }
            }
   
            System.out.println("Partie chargée avec succès !");
            this.plateau.afficherPlateau();
            this.afficherInformationsJoueurs();
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
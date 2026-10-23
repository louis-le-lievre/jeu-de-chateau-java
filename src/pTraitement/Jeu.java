package pTraitement;
import java.util.Scanner;

/*
 * COUNILH QUENTIN LE LIEVRE LOUIS THOUVENIN AMELIE
 * jeu de chateau
 * rapport intermédiraire mars 2025
 */


public class Jeu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenue dans le jeu de Château !");

        // Demander si l'utilisateur veut charger une partie sauvegardée
        System.out.print("Voulez-vous charger une partie sauvegardée ? (Oui/Non) : ");
        String reponseChargement = scanner.nextLine().trim().toLowerCase();

        Partie partie = null; // Déclarer la partie ici pour la réutiliser plus tard

        if (reponseChargement.equals("oui") || reponseChargement.equals("o")) {
            // Charger une partie sauvegardée
            partie = new Partie(new Joueur("Joueur1"), new Ordinateur("Ordinateur"));
            boolean chargementReussi = partie.chargerPartie();

            if (!chargementReussi) {
                System.out.println("Échec du chargement. Démarrage d'une nouvelle partie.");
                partie = creerNouvellePartie(scanner);
            }
        } else {
            // Démarrer une nouvelle partie
            partie = creerNouvellePartie(scanner);
        }

        // Démarrer la partie
        partie.demarrer();

        scanner.close();
    }

    private static Partie creerNouvellePartie(Scanner scanner) {
        System.out.print("Entrez le nom du premier joueur : ");
        String nomHumain1 = scanner.nextLine();

        String reponseHumain2;
        do {
            System.out.print("Y a-t-il un autre joueur humain ? (Oui/Non) : ");
            reponseHumain2 = scanner.nextLine().trim().toLowerCase();

            if (!reponseHumain2.equals("oui") && !reponseHumain2.equals("non")) {
                System.out.println("Veuillez répondre par 'Oui' ou 'Non'.");
            }
        } while (!reponseHumain2.equals("oui") && !reponseHumain2.equals("non"));

        Joueur joueurHumain1 = new Joueur(nomHumain1);
        Joueur joueur2;

        if (reponseHumain2.equals("oui") || reponseHumain2.equals("o")) {
            System.out.print("Veuillez entrer le nom du deuxième joueur : ");
            String nomHumain2 = scanner.nextLine();
            joueur2 = new Joueur(nomHumain2);
        } else {
            joueur2 = new Ordinateur("Ordinateur");
        }

        return new Partie(joueurHumain1, joueur2);
    }
}
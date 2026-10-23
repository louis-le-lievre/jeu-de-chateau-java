package pTraitement;
/*
 * COUNILH QUENTIN LE LIEVRE LOUIS THOUVENIN AMELIE
 * jeu de chateau
 * rapport intermédiraire mars 2025
 */

public class PartieSauvegardee {
    private String[][] plateau;
    private Joueur joueurHumain1;
    private Joueur joueurHumain2;
    private Joueur joueurCourant;
    private boolean partieTerminee;
    private boolean estModeGraphique;

    // Constructeur
    public PartieSauvegardee(String[][] plateau, Joueur joueurHumain1, Joueur joueurHumain2, Joueur joueurCourant, boolean partieTerminee, boolean estModeGraphique) {
        this.plateau = plateau;
        this.joueurHumain1 = joueurHumain1;
        this.joueurHumain2 = joueurHumain2;
        this.joueurCourant = joueurCourant;
        this.partieTerminee = partieTerminee;
        this.estModeGraphique = estModeGraphique;
    }

    // Getters
    public String[][] getPlateau() {
        return plateau;
    }

    public Joueur getJoueurHumain1() {
        return joueurHumain1;
    }

    public Joueur getJoueurHumain2() {
        return joueurHumain2;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public boolean isPartieTerminee() {
        return partieTerminee;
    }

    public boolean estModeGraphique() {
        return estModeGraphique;
    }

    // Convertir l'objet en texte
    public String toTexte() {
        StringBuilder sb = new StringBuilder();

        // Sauvegarder le plateau
        for (String[] ligne : plateau) {
            for (String cellule : ligne) {
                sb.append(cellule).append(";");
            }
            sb.append("\n");
        }

        // Sauvegarder les joueurs
        sb.append("joueurHumain1:").append(joueurHumain1.toTexte()).append("\n");
        sb.append("joueurHumain2:").append(joueurHumain2 != null ? joueurHumain2.toTexte() : "null").append("\n");
        sb.append("joueurCourant:").append(joueurCourant.toTexte()).append("\n");

        // Sauvegarder les autres champs
        sb.append("partieTerminee:").append(partieTerminee).append("\n");
        sb.append("estModeGraphique:").append(estModeGraphique).append("\n");

        return sb.toString();
    }

    // Reconstruire l'objet à partir du texte
    public static PartieSauvegardee fromTexte(String texte) {
        String[] lignes = texte.split("\n");

        // Charger le plateau
        String[][] plateau = new String[lignes.length - 5][]; // 5 lignes pour les autres champs
        for (int i = 0; i < plateau.length; i++) {
            plateau[i] = lignes[i].split(";");
        }

        // Charger les joueurs
        Joueur joueurHumain1 = Joueur.fromTexte(lignes[plateau.length].split(":")[1]);
        Joueur joueurHumain2 = "null".equals(lignes[plateau.length + 1].split(":")[1]) ? null : Joueur.fromTexte(lignes[plateau.length + 1].split(":")[1]);
        Joueur joueurCourant = Joueur.fromTexte(lignes[plateau.length + 2].split(":")[1]);

        // Charger les autres champs
        boolean partieTerminee = Boolean.parseBoolean(lignes[plateau.length + 3].split(":")[1]);
        boolean estModeGraphique = Boolean.parseBoolean(lignes[plateau.length + 4].split(":")[1]);

        return new PartieSauvegardee(plateau, joueurHumain1, joueurHumain2, joueurCourant, partieTerminee, estModeGraphique);
    }
}
package odomo;

import java.util.Scanner;

/**
 * Gestion de la partie Chauffage.
 */
class Chauffage {

    /**
     * Premier créneau du jour en mode normal.
     */
    static int[][] creneau1;

    /**
     * Deuxième créneau du jour en mode normal.
     */
    static int[][] creneau2;

    /**
     * Initialiser les données de chauffage.
     */
    /**
     * La température hors crénaux
     */
    static double temperEco;

    /**
     * La température pendant le ou les créneaux
     */
    static double temperNormal;

    /**
     * Cette fonction permet d'initialiser les attributs de température pendant
     * et hors créneaux et d'initialiser les deux tableaux pour les crénaux.
     */
    static void initialiser() {
        temperEco = 0.;
        temperNormal = 0.;
        creneau1 = new int[7][2];
        creneau2 = new int[7][2];
        for (int i = 0; i < 7; i++) {
            creneau1[i][0] = 1;
            creneau1[i][1] = 0;
        }
        for (int i = 0; i < 7; i++) {
            creneau2[i][0] = 1;
            creneau2[i][1] = 0;
        }
    }

    /**
     * Matrice des créneaux en mode normal, pour l'histogramme.
     *
     * @return la matrice des créneaux
     */
    static boolean[][] matriceCreneaux() {
        boolean[][] matrice = new boolean[8][24];
        for (int i = 0; i < matrice.length - 1; i++) {
            for (int j = 0; j < matrice[0].length - 1; j++) {
                if (creneau1[i][0] == 1 && creneau1[i][1] == 0 && creneau2[i][0] == 1 && creneau2[i][1] == 0) {
                    matrice[i][j] = false;

                } else if (creneau1[i][0] != 1 && creneau1[i][1] != 0 || creneau2[i][0] != 1 && creneau2[i][1] != 0) {
                    if (creneau1[i][0] <= j && creneau1[i][1] >= j || creneau2[i][0] <= j && creneau2[i][1] >= j) {
                        matrice[i][j] = true;
                    }
                } else {
                    matrice[i][j] = false;
                }
            }
        }
        return matrice;
    }

    /**
     * Procédure de saisie des créneaux de chauffage.
     */
    static void saisieCreneaux() {
        System.out.println("Veuillez saisir un ou deux créneaux que vous souhaitez passer en mode normal en précisant le jour sous cette forme : \"jour;h;h;h;h\"");
        String valeur;
        Scanner saisie = new Scanner(System.in);
        valeur = saisie.nextLine();
        traitementSaisieCreneaux(valeur);
    }

    /**
     * Traite la saisie de créneaux par l'utilisateur.
     *
     * @param saisie la saisie de l'utilisateur
     * @return true ssi la saisie a été correcte
     */
    static boolean traitementSaisieCreneaux(String saisie) {
        boolean correct = saisie != null;
        String[] champs = null;
        if (correct) {
            champs = saisie.split(";");
            correct &= champs.length == 3 || champs.length == 5;
            if (!correct) {
                System.err.println("Format incorrect : 3 ou 5 champs separes "
                        + "par des points-virgules sont attendus, "
                        + champs.length + " ont été saisis.");
            }
        }
        if (correct) {
            correct &= Odomo.numeroJour(champs[0]) >= 0;
            if (!correct) {
                System.err.println("Nom de jour incorrect : " + champs[0] + ".");
            }
        }
        int creneau1debut = -1;
        if (correct) {
            creneau1debut = heureCreneau(champs[1]);
            correct = creneau1debut >= 0;
        }
        int creneau1fin = -1;
        if (correct) {
            creneau1fin = heureCreneau(champs[2]);
            correct = creneau1fin >= 0;
        }
        if (correct) {
            correct &= (creneau1debut <= creneau1fin) || (creneau1debut == 1 && creneau1fin == 0);
            if (!correct) {
                System.err.println("Créneau incorrect : l'heure de début doit "
                        + "précéder (ou égaler) l'heure de fin "
                        + "(ou choisir le créneau 1h-0h pour un créneau vide).");
            }
        }
        int creneau2debut = -1;
        int creneau2fin = -1;
        if (correct && champs.length == 5) {
            creneau2debut = heureCreneau(champs[3]);
            correct = creneau2debut >= 0;
            if (correct) {
                creneau2fin = heureCreneau(champs[4]);
                correct = creneau2fin >= 0;
            }
            if (correct) {
                correct &= (creneau2debut <= creneau2fin) || (creneau2debut == 1 && creneau2fin == 0);
                if (!correct) {
                    System.err.println("Créneau incorrect : l'heure de début doit "
                            + "précéder (ou égaler) l'heure de fin "
                            + "(ou choisir le créneau 1h-0h pour un créneau vide).");
                }
            }
        }
        if (correct) {
            int numJour = Odomo.numeroJour(champs[0]);
            Chauffage.creneau1[numJour][0] = creneau1debut;
            Chauffage.creneau1[numJour][1] = creneau1fin;
            if (champs.length == 5) {
                Chauffage.creneau2[numJour][0] = creneau2debut;
                Chauffage.creneau2[numJour][1] = creneau2fin;
            } else {
                Chauffage.creneau2[numJour][0] = 1;
                Chauffage.creneau2[numJour][1] = 0;
            }
        }
        return correct;
    }

    /**
     * Récupère l'heure d'un créneau donné sous forme de chaîne.
     *
     * @param chaineHeure l'heure sous forme de chaîne
     * @return l'heure sous forme d'entier (-1 si incorrecte)
     */
    static int heureCreneau(String chaineHeure) {
        int heure;
        try {
            heure = Integer.parseInt(chaineHeure);
        } catch (NumberFormatException e) {
            System.err.println("L'heure de créneau n'est pas un entier : " + chaineHeure);
            heure = -1;
        }
        if (heure > 23) {
            System.err.println("L'heure doit être comprise entre 0 et 23 "
                    + "(inclus), au lieu de : " + chaineHeure + ".");
            heure = -1;
        }
        return heure;
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        /* Création de la liste des patients0 */
        List<int[]> patients0 = Arrays.asList(new int[]{0, 0}, new int[]{0, 0});

        System.out.println(virus(patients0, 100));
    }

    /**
     * Cette méthode permet de dérouler l'évolution des virus dans une population
     * @param patients0 liste des personnes ayant conctactées la maladie en premier
     * @param n taille de la population
     * @return liste de l'évolution du virus au sein de la population
     */
    public static List<Integer> virus(List<int[]> patients0, int n) {

        /* Vérification de la taille de la population et du nombre de patients0 */
        if(n <= 0) throw new RuntimeException("Erreur taille population");
        if(patients0.size() == 0) throw new RuntimeException("Erreur, aucun patients0");

        /* Création d'une population */
        Population population = new Population(n);

        /* Création de la liste de retour */
        List<Integer> virus = new ArrayList<>();

        /* Vérification dimentions de patients0 et ajout dans la population */
        for (int[] c: patients0) {
            if (c.length != 2) {
                throw new RuntimeException("Erreur de dimension dans patients0");
            }
            MyInt a = new MyInt(c);
            population.ajouterPatient(a);
        }

        /* Simulation de l'évolution du virus dans la population */
        while(population.getNombrePatient() != n*n) {
            virus.add(population.getNombrePatient());
            population.cycle();
        }
        virus.add(population.getNombrePatient());

        return virus;
    }
}

/**
 * Cette classe permert de simuler l'évolution d'un virus dans une population
 */
class Population {
    private final int taillePopulation;
    private final HashSet<MyInt> marquageExpansionPossible;
    private final HashSet<MyInt> marquageExpansionImpossible;

    /**
     * Ce constructeur initialise la taille de la population ainsi que les 2 ensembles qui lui sont liés.
     * @param n taille de la population
     */
    public Population(int n) {
        this.taillePopulation = n;
        this.marquageExpansionPossible = new HashSet<>();
        this.marquageExpansionImpossible = new HashSet<>();
    }

    /**
     * Ajoute un patient infecté à la population
     * @param val tableau comportant les coordonnées du patient infecté
     */
    public void ajouterPatient(MyInt val) {
        if (!(val.getIndex0() >=0 && val.getIndex0()<taillePopulation && val.getIndex1()>=0 && val.getIndex1()<this.taillePopulation)) return;
        this.marquageExpansionPossible.add(val);
    }

    /**
     * Getter permettant de récupérer le nombre de personnes contaminées
     * @return le nombre de personnes contaminées
     */
    public int getNombrePatient() {
        return (this.marquageExpansionImpossible.size() + this.marquageExpansionPossible.size());
    }

    /**
     * Permet de simuler un cycle de l'évolution du virus
     */
    public void cycle() {
        /* Création d'un ensemble temporaire */
        HashSet<MyInt> nouveauMarquagePossible = new HashSet<>(this.marquageExpansionPossible);

        /* Changement d'ensemble pour les personnes ayant déjà rependu le virus */
        this.marquageExpansionImpossible.addAll(this.marquageExpansionPossible);

        this.marquageExpansionPossible.clear();

        /* Expansion du virus par toutes les personnes fraichement contaminées */
        nouveauMarquagePossible.forEach(this::expansion);
    }

    /**
     * Répend le virus sur les cases voisines d'une personne
     * @param val coordonées de la personnes contaminée
     */
    public void expansion(MyInt val) {
        /* Test bas */
        if((this.taillePopulation-(val.getIndex0()+1) > 0) && !this.marquageExpansionImpossible.contains(new MyInt(val.getIndex0()+1, val.getIndex1()))) {
            this.marquageExpansionPossible.add(new MyInt(val.getIndex0()+1, val.getIndex1()));
        }

        /* test haut */
        if((val.getIndex0()-1 >= 0) && !this.marquageExpansionImpossible.contains(new MyInt(val.getIndex0()-1, val.getIndex1()))) {
            this.marquageExpansionPossible.add(new MyInt(val.getIndex0()-1, val.getIndex1()));
        }

        /* test droite */
        if((this.taillePopulation-(val.getIndex1()+1) > 0) && !this.marquageExpansionImpossible.contains(new MyInt(val.getIndex0(), val.getIndex1()+1))) {
            this.marquageExpansionPossible.add(new MyInt(val.getIndex0(), val.getIndex1()+1));
        }

        /* test gauche */
        if((val.getIndex1()-1 >= 0) && !this.marquageExpansionImpossible.contains(new MyInt(val.getIndex0(),val.getIndex1()-1))) {
            this.marquageExpansionPossible.add(new MyInt(val.getIndex0(),val.getIndex1()-1));
        }
    }
}

/**
 * Classe permettant de redéfinir les méthodes equals et hashCode d'un int[] d'une dimension de 2
 */
class MyInt {
    private int[] myCase;

    /**
     * Ce constructeur permet d'initialiser un int[] à 2 dimension via un int[] de même dimension
     * @param myCase tableau de sortie contenant les coordonnées
     */
    public MyInt(int[] myCase) {
        if(myCase.length == 2) this.myCase = myCase;
    }

    /**
     * Ce constructeur permet d'initialiser un int[] à 2 dimension via 2 int
     * @param x première composante
     * @param y deuxième composante
     */
    public MyInt(int x, int y) {
        this.myCase = new int[]{x,y};
    }

    /**
     * Getter permettant de récupérer la valeur du premier index du int[]
     * @return le première valeur
     */
    public int getIndex0() {
        return myCase[0];
    }

    /**
     * Getter permettant de récupérer la valeur du deuxième index du int[]
     * @return la deuxième valeur
     */
    public int getIndex1() {
        return myCase[1];
    }

    /**
     * Redéfinition de la méthode equal appliquée sur le tableau myCase et d'un deuxième objet
     * @param o objet à comparer
     * @return vrai si les objets sont indentiques
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyInt myInt = (MyInt) o;
        return Arrays.equals(myCase, myInt.myCase);
    }

    /**
     * Redéfinition de la méthode hashCode appliquée sur le tableau myCase
     * @return hash de l'objet courant
     */

    @Override
    public int hashCode() {
        return Arrays.hashCode(myCase);
    }
}
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;
import java.util.Random;

/**
 * Hlavní třída, umí spustit program
 * @author Štěpán Faragula, 27-11-2022
 */
public class Main {
    /** Vlastní implementace grafu */
    private static Graf grafMuj;
    /** Implementace grafu pomocí knihovny JGraphT */
    private static Graph<Integer, DefaultEdge> grafKnihovna;
    /** Náhodný generátor čísel */
    private static final Random random = new Random();

    /** Kolik má mít graf vrcholů */
    private static final int POCET_VRCHOLU = 10;
    /** Kolik má mít graf hran */
    private static final int POCET_HRAN = 20;

    /**
     * Vytvoří dva identické, náhodně generované orientované grafy různých implementací a změří jejich čas hledání komponent
     * @param args &lt;počet vrcholů&gt; &lt;počet hran&gt;
     */
    public static void main(String[] args) {
        // Hodnoty pro vygenerovaný graf
        int pocetVrcholu = POCET_VRCHOLU;
        int pocetHran = POCET_HRAN;

        // Vstup uživatele
        if(args.length > 2){
            System.out.println();
            System.out.println("Spusteni programu s parametry:");
            System.out.println("program.jar <pocet vrcholu> <pocet hran>");
            System.exit(-1);
        }
        if(args.length != 0) {
            try {
                pocetVrcholu = Integer.parseInt(args[0]);
                pocetHran = Integer.parseInt(args[1]);
            } catch (Exception e) {
                System.out.println();
                System.out.println("Spusteni programu s parametry:");
                System.out.println("program.jar <pocet vrcholu> <pocet hran>");
                System.exit(-1);
            }
        }
        if(pocetVrcholu <= 1 || pocetHran <= 0){
            System.out.println();
            System.out.println("Neplatny vstup");
            System.out.println("Spusteni programu s parametry:");
            System.out.println("program.jar <pocet vrcholu> <pocet hran>");
            System.exit(-1);
        }

        // Příprava grafů
        System.out.println("Generuju graf s " + pocetVrcholu + " vrcholy a " + pocetHran + " hranami");
        vytvorStejnyNahodnyGraf(pocetVrcholu, pocetHran);
        StrongConnectivityAlgorithm<Integer, DefaultEdge> inspector = new KosarajuStrongConnectivityInspector<>(grafKnihovna);

        // Výpis grafu
        System.out.println("Vygenerovaný graf:");
        grafMuj.vypisOutSousedy();
        System.out.println();

        // Měření času algoritmů
        System.out.println("Hledám komponenty grafu");
        long casSpusteni, casMuj, casKnihovny;
        casSpusteni = System.currentTimeMillis();
        List<Graph<Integer, DefaultEdge>> komponentyKnihovna = inspector.getStronglyConnectedComponents();
        casKnihovny = System.currentTimeMillis() - casSpusteni;

        casSpusteni = System.currentTimeMillis();
        grafMuj.stronglyConnectedComponents();
        casMuj = System.currentTimeMillis() - casSpusteni;

        // Výpis komponent
        System.out.println("Výpis komponent vlastní implementace:");
        grafMuj.vypisKomponenty();
        System.out.println();
        System.out.println("Výpis komponent knihovny:");
        for (Graph<Integer, DefaultEdge> komponenta : komponentyKnihovna) {
            System.out.println(komponenta);
        }
        System.out.println();
        System.out.println();

        // Výpis času
        System.out.println("Čas vlastní implementace: " + casMuj + " ms.");
        System.out.println("Čas knihovny: " + casKnihovny + " ms.");
    }

    /**
     * Nastaví do statických privátních proměnných {@code grafMuj} a {@code grafKnihovna} dva indentické, náhodně generované orientované grafy
     * @param pocetVrcholu kolik má mít graf vrcholů
     * @param pocetHran kolik má mít graf hran
     */
    public static void vytvorStejnyNahodnyGraf(int pocetVrcholu, int pocetHran){
        // Inicializace grafu
        grafMuj = new Graf();
        grafKnihovna = new DefaultDirectedGraph<>(DefaultEdge.class);

        // Přidání vrcholu
        for(int i = 0; i < pocetVrcholu; i++){
            grafMuj.pridejVrchol(i);
            grafKnihovna.addVertex(i);
        }

        // Přidání hran
        for(int i = 0; i < pocetHran; i++){
            int zacatekHrany = random.nextInt(pocetVrcholu);
            int konecHrany = random.nextInt(pocetVrcholu);
            while(zacatekHrany == konecHrany){
                zacatekHrany = random.nextInt(pocetVrcholu);
                konecHrany = random.nextInt(pocetVrcholu);
            }
            grafMuj.pridejHranu(zacatekHrany, konecHrany);
            grafKnihovna.addEdge(zacatekHrany, konecHrany);
        }
    }
}

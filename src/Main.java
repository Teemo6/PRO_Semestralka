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

    /**
     * Vytvoří dva identické, náhodně generované orientované grafy různých implementací a změří jejich čas hledání komponent
     * @param args program nepřijímá žádné argumenty
     */
    public static void main(String[] args) {
        // Příprava grafů
        vytvorStejnyNahodnyGraf(10, 20);
        StrongConnectivityAlgorithm<Integer, DefaultEdge> inspector = new KosarajuStrongConnectivityInspector<>(grafKnihovna);

        // Měření času algoritmů
        long casSpusteni, casMuj, casKnihovny;
        casSpusteni = System.currentTimeMillis();
        List<Graph<Integer, DefaultEdge>> komponentyKnihovna = inspector.getStronglyConnectedComponents();
        casKnihovny = System.currentTimeMillis() - casSpusteni;

        casSpusteni = System.currentTimeMillis();
        grafMuj.stronglyConnectedComponents();
        casMuj = System.currentTimeMillis() - casSpusteni;

        // Výpis grafu
        System.out.println("Hrany grafu:");
        grafMuj.vypisOutSousedy();
        System.out.println();
        System.out.println();

        // Výpis komponent
        System.out.println("Výpis komponent mé implementace:");
        grafMuj.vypisKomponenty();
        System.out.println();
        System.out.println("Výpis komponent knihovny:");
        for (Graph<Integer, DefaultEdge> komponenta : komponentyKnihovna) {
            System.out.println(komponenta);
        }
        System.out.println();
        System.out.println();

        // Výpis času
        System.out.println("Čas mé implementace: " + casMuj + " ms.");
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

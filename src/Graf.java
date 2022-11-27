import java.util.*;

/**
 * Třída reprezentující orientovaný graf
 * @author Štěpán Faragula, 27-11-2022
 */
public class Graf {
    /** Reprezentace grafu jako spojový seznam */
    private final Map<Integer, LinkedList<Integer>> seznamSousednosti;

    /** Jestli byl vrchol navštíven při průchodu */
    private Map<Integer, Boolean> visited;
    /** Jestli byl vrchol přiřazen komponentě */
    private Map<Integer, Boolean> assigned;
    /** V jakém pořadí se mají zpracovávat vrcholy */
    private Stack<Integer> traversal;
    /** Jednotlivé komponenty grafu spolu s jejich vrcholy */
    private Map<Integer, LinkedList<Integer>> komponenty;

    /**
     * Konstruktor, inicializuje seznam sousednosti
     */
    public Graf() {
        seznamSousednosti = new HashMap<>();
    }

    /**
     * Přidá vrchol do grafu
     * @param id cislo vrcholu
     */
    public void pridejVrchol(int id){
        seznamSousednosti.putIfAbsent(id, new LinkedList<>());
    }

    /**
     * Spojí dva vrcholy orientovanou hranou
     * @param zacatek výchozí vrchol
     * @param konec koncový vrchol
     */
    public void pridejHranu(int zacatek, int konec){
        seznamSousednosti.get(zacatek).add(konec);
    }

    /**
     * Kostarajův algoritmus na hledání všech komponent grafu
     * <p>
     * Jednotlivé kroky jsou okomentovány podle pseudokódu nalezeného na wikipedii (k 27-11-2022)
     * https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm
     * <p>
     * Využívá rekurzivní privátní metody {@code visit()} a {@code assign()}
     */
    public void stronglyConnectedComponents() {
        // For each vertex u of the graph, mark u as unvisited. Let L be empty
        visited = new HashMap<>();
        assigned = new HashMap<>();
        traversal = new Stack<>();
        komponenty = new HashMap<>();
        for (Map.Entry<Integer, LinkedList<Integer>> entry : seznamSousednosti.entrySet()) {
            visited.put(entry.getKey(), Boolean.FALSE);
            assigned.put(entry.getKey(), Boolean.FALSE);
        }

        // For each vertex u of the graph do Visit(u)
        for (Map.Entry<Integer, LinkedList<Integer>> entry : seznamSousednosti.entrySet()) {
            visit(entry.getKey());
        }

        // For each element u of L in order, do Assign(u,u)
        Collections.reverse(traversal);
        for (int vrchol : traversal) {
            assign(vrchol, vrchol);
        }
    }

    /**
     * Vypíše graf do konzole
     * <p>
     * Formát výpisu: 'výchozí vrchol' -> 'vrchol kam vede hrana' -> 'vrchol kam vede hrana' -> ...
     */
    public void vypisGraf() {
        for (Map.Entry<Integer, LinkedList<Integer>> id : seznamSousednosti.entrySet()) {
            System.out.println("Vrchol (" + id.getKey() + "):");

            System.out.print(id.getKey());
            for (Integer id2 : seznamSousednosti.get(id.getKey())) {
                System.out.print(" -> " + id2);
            }
            System.out.println();
        }
    }

    /**
     * Vypíše všechny komponenty grafu a ze kterých vrcholů se skládají
     * <p>
     * Formát výpisu: 'komponentID' -> 'vrcholy komponenty'
     */
    public void vypisKomponenty(){
        if(komponenty == null){
            System.out.println("Ještě nejsou vypočítané komponenty");
            return;
        }

        for(Map.Entry<Integer, LinkedList<Integer>> k : komponenty.entrySet()){
            System.out.println("[" + k.getKey() + "] -> " + k.getValue());
        }
    }

    //////////////////////
    //* Private metody *//
    //////////////////////

    /**
     * Rekurzivní funkce, navštíví vrchol a zařazuje je ke zpracování
     * @param vrchol jaký vrchol se má navštívit
     */
    private void visit(int vrchol){
        // If u is unvisited then
        if (visited.get(vrchol) == Boolean.FALSE) {
            // Mark u as visited
            visited.put(vrchol, Boolean.TRUE);

            // For each out-neighbour v of u do Visit(v)
            for(Integer outNeighbour : seznamSousednosti.get(vrchol)){
                visit(outNeighbour);
            }

            // Prepend u to L
            traversal.push(vrchol);
        }
    }

    /**
     * Rekurzivní funkce, přiřadí vrchol komponentě
     * @param vrchol vrchol k zařazení
     * @param root komponenta do které se zařadí
     */
    private void assign(int vrchol, int root){
        // If u has not been assigned to a component then
        if (assigned.get(vrchol) == Boolean.FALSE) {
            // Assign u as belonging to the component whose root is root
            assigned.put(vrchol, Boolean.TRUE);
            komponenty.putIfAbsent(root, new LinkedList<>());
            komponenty.get(root).push(vrchol);

            // For each in-neighbour v of u, do Assign(v,root)
            for (Map.Entry<Integer, LinkedList<Integer>> entry : seznamSousednosti.entrySet()) {
                for(Integer inNeighbour : entry.getValue()){
                    if(inNeighbour == vrchol) {
                        assign(entry.getKey(), root);
                    }
                }
            }
        }
    }
}
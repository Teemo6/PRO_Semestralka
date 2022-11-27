import java.util.*;

/**
 * Třída reprezentující orientovaný graf
 * <p>
 * Graf je reprezentován jako dva spojové seznamy, OUT-sousedé a IN-sousedé
 * @author Štěpán Faragula, 27-11-2022
 */
public class Graf {
    /** OUT-sousedé vrcholu */
    private final Map<Integer, LinkedList<Integer>> outSousedi;
    /** IN-sousedé vrcholu */
    private final Map<Integer, LinkedList<Integer>> inSousedi;

    /** Navštívené vrcholy */
    private Set<Integer> visited;
    /** Vrcholy přiřazené nějaké komponentě */
    private Set<Integer> assigned;
    /** V jakém pořadí se mají zpracovávat vrcholy */
    private Stack<Integer> traversal;
    /** Jednotlivé komponenty grafu spolu s jejich vrcholy */
    private Map<Integer, LinkedList<Integer>> komponenty;

    /**
     * Konstruktor, inicializuje seznam sousednosti
     */
    public Graf() {
        outSousedi = new HashMap<>();
        inSousedi = new HashMap<>();
    }

    /**
     * Přidá vrchol do grafu
     * @param id cislo vrcholu
     */
    public void pridejVrchol(int id){
        outSousedi.putIfAbsent(id, new LinkedList<>());
        inSousedi.putIfAbsent(id, new LinkedList<>());
    }

    /**
     * Spojí dva vrcholy orientovanou hranou
     * @param zacatek výchozí vrchol
     * @param konec koncový vrchol
     */
    public void pridejHranu(int zacatek, int konec){
        outSousedi.get(zacatek).add(konec);
        inSousedi.get(konec).add(zacatek);
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
        visited = new HashSet<>();
        assigned = new HashSet<>();
        traversal = new Stack<>();
        komponenty = new HashMap<>();

        // For each vertex u of the graph do Visit(u)
        for (Map.Entry<Integer, LinkedList<Integer>> entry : outSousedi.entrySet()) {
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
     * Formát výpisu: 'výchozí vrchol' -> 'koncový vrchol' -> 'koncový vrchol' -> ...
     */
    public void vypisOutSousedy() {
        for (Map.Entry<Integer, LinkedList<Integer>> id : outSousedi.entrySet()) {
            System.out.println("Vrchol (" + id.getKey() + "):");

            System.out.print("\t" + id.getKey());
            for (Integer id2 : outSousedi.get(id.getKey())) {
                System.out.print(" -> " + id2);
            }
            System.out.println();
        }
    }

    /**
     * Vypíše graf do konzole
     * <p>
     * Formát výpisu: 'koncový vrchol' <- 'výchozí vrchol' <- 'výchozí vrchol' <- ...
     */
    public void vypisInSousedy() {
        for (Map.Entry<Integer, LinkedList<Integer>> id : inSousedi.entrySet()) {
            System.out.println("Vrchol (" + id.getKey() + "):");

            System.out.print("\t" + id.getKey());
            for (Integer id2 : inSousedi.get(id.getKey())) {
                System.out.print(" <- " + id2);
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
        if (!visited.contains(vrchol)) {
            // Mark u as visited
            visited.add(vrchol);

            // For each out-neighbour v of u do Visit(v)
            for(Integer outNeighbour : outSousedi.get(vrchol)){
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
        if (!assigned.contains(vrchol)) {
            // Assign u as belonging to the component whose root is root
            assigned.add(vrchol);
            komponenty.putIfAbsent(root, new LinkedList<>());
            komponenty.get(root).push(vrchol);

            // For each in-neighbour v of u, do Assign(v,root)
            for(Integer inNeighbour : inSousedi.get(vrchol)){
                assign(inNeighbour, root);
            }
        }
    }
}
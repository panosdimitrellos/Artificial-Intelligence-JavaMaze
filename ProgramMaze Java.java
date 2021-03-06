import java.util.*;

public class ProjectMazeAI
{
    static int ROW = 6;
    static int COL = 5;

    //Αποθηκεύουμε τις συντεταγμένες των κελιών του πίνακα.
    static class PointerSuntetagmenes
    {
        int x;
        int y;

        public PointerSuntetagmenes(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    };

    // Δομή Δεδομένων για την ουρά που χρησιμοποιούμε στον B&B.
    static class queueNode
    {
        PointerSuntetagmenes pt; // Οι συτντεταγμένες του κελιού.
        int cost; // Η απόσταση του κελιού απο τον αρχικό κόμβο, δηλαδή το κόστος του.

        public queueNode(PointerSuntetagmenes pt, int cost)
        {
            this.pt = pt;
            this.cost = cost;
        }

    };

    // Ελέγχουμε αν το δοσμένο κελί(row, col) είναι έγκυρο ή οχι.
    static boolean isValid(int row, int col)
    {
        // Επέστρεψε true αν ο αριθμός την γραμμης και
        // της στήλης είναι στο εύρος των τιμών.
        return (row >= 0) && (row < ROW) && (col >= 0) && (col < COL);
    }

    // Αυτοί οι πίνακες χρησιμοποιούντε για να πάρουμε τον αριθμό των γραμμών
    // και στηλών των 4 γειτόνων ενός δοσθέντος κελιού.
    static int rowNum[] = {-1, 0, 0, 1};
    static int colNum[] = {0, -1, 1, 0};


    // Μέτοπο Αναζήτησης,ουρά που θα μας βοηθήσει στον αλγόριθμο B&B.
    static Queue<queueNode> MA = new LinkedList<>();

    // Μεταβλητή για το μοπάτι που θα προκύψει.
    static List<String> path = new ArrayList<String>();

    // Ανωτερο όριο.
    static int upperBound = Integer.MAX_VALUE;

    //Κλειστό σύνολο, το οποίο κρατάει ωσ ορίσματα τα κελιά που έχουμε επισκεφτεί.
    static boolean closed[][] = new boolean[ROW][COL];

    // Συνάρτηση για να βρούμε το συντομότερο μονοπάτι μεταξύ ενός δοσθέντος
    // αρχικού κελιού και του κελιού προορισμού, σύμφωνα με το αλγόριθμο B&B .
    static void bnb(int maze[][],PointerSuntetagmenes src,PointerSuntetagmenes dest)
    {
        // Μαρκάρουμε το αρχικό κελί ως κελί που το έχουμε επισκεφτεί.
        closed[src.x][src.y] = true;

        //Το κόστος/η απόσταση απο το αρχικό κελί είναι 0.
        queueNode starting_node = new queueNode(src,0);
        //Προσθέτουμε στο μέτοπο αναζήτησης τον πρώτο κόμβο.
        MA.add(starting_node);

        // Κάνουμε τυφλή αναζήτηση όσο το Μέτοπο αναζήτησης δεν είναι κενό.
        while (!MA.isEmpty())
        {
            queueNode current_node = MA.peek();
            PointerSuntetagmenes ptc = current_node.pt;

            // Γίνετε έλεγχος για αν το κόστος του τωρινού κόμβου που βρισκόμαστε
            // ειναι μικρότερο απο το ανώτερο όριο.
            if(current_node.cost < upperBound)
            {
                // Αν έχουμε φτάσει στο κελί προορισμόυ τελείωσαμε την διαδικασία.
                if (ptc.x == dest.x && ptc.y == dest.y)
                {
                    upperBound = current_node.cost;
                }

                // Αλλίως βγάζουμε το μπροστινό κελί από την ουρά και εισάγουμε σε αυτή τα γειτονικά του κελιά.
                MA.remove();

                for (int i = 0; i < 4; i++)
                {
                    int row = ptc.x + rowNum[i];
                    int col = ptc.y + colNum[i];

                    // Αν το γειτονικό κελί είναι έγκυρο , ανήκει στην διαδρομή του λαβυρίνθου
                    // και δεν έχει επσκεφτεί ακόμα, το εισάγουμε στην ουρά.
                    if (isValid(row, col) && maze[row][col] == 1 && !closed[row][col])
                    {
                        // Μαρκάρουμε το κελί ως επισκευμένο(δηλαδή το βάζουμε στο Κλειστό Σύνολο)
                        // και το εισάγουμε στο Μέτοπο Αναζήτησης.
                        closed[row][col] = true;
                        queueNode Adjcell = new queueNode(new PointerSuntetagmenes(row, col), current_node.cost + 1 );
                        MA.add(Adjcell);
                        // Προσθέτουμε στην μεταβλητή path τις συντεταγμένες του κελιού.
                        path.add(row+" "+col);

                    }

                }
            }
        }

        System.out.println("Το συντομότερο μονοπάτι από τον αρχικό κόμβο "+ src.x+ " "+ src.y+ " είναι: " );
        for(int i=0;i<11;i+=2)
        {
            System.out.println(path.get(i));
        }
        System.out.println("Το μήκος του συντομότερου μονοπατιού είναι: " + upperBound);
    }

    public static void main(String[] args)
    {
        // Ο λαβύρινθος σε μορφή διαδυκού λαβυρίνθου.
        int maze[][] = {
                { 0, 0, 0, 0, 0},
                { 0, 1, 1, 1, 0},
                { 0, 1, 0, 1, 1},
                { 0, 1, 0, 1, 0},
                { 1, 1, 1, 1, 0},
                { 0, 0, 0, 0, 0}};

        // Οι αρχικές και τελικές συντεταγμένες.
        PointerSuntetagmenes source = new PointerSuntetagmenes(4, 0);
        PointerSuntetagmenes dest = new PointerSuntetagmenes(2, 4);

        bnb(maze,source,dest);
    }
}

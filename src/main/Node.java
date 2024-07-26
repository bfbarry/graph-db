package src.main;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.io.*;


public class Node {
    private static int name_size = 20;
    private static int neighbors_max_length = 20;
    private static int int_size = 4;
    private int n_neighbors;
    private boolean initialized;
    public int ID;
    public byte[] name;
    public int[] neighbors;
    static String fn = "/Users/brianbarry/Desktop/computing/graph-db-project/data.bin";
    
    private int getRowSize() {
        int ID_size = int_size;
        int num_neighbors_size = int_size;

        return ID_size + name_size + num_neighbors_size + neighbors_max_length*int_size;

    }

    public Node() {
        // init with empty data
        // TODO check name_s size, neighbors size
        this.initialized = false;
        this.ID = -1;
        byte[] name = new byte[name_size];
        this.name = name;
        // TODO error if neighbors are don't exist
        // this.neighbors = neighbors;
    }

    public void create(String name_s, int[] neighbors) throws Exception {
        if (this.initialized) {
            throw new Exception("Cannot overwrite existing Node");
        }
        // used in POST
        //, int[] neighbors
        // copy name to bytes
        // TODO check name_s size, neighbors size
        File f = new File(fn);
        this.ID = (int) f.length() / this.getRowSize();
        byte[] name = new byte[name_size];
        byte[] inputNameBytes = name_s.getBytes(StandardCharsets.UTF_8);
        int copysize = name_size;
        if (inputNameBytes.length < name_size) {
            copysize = inputNameBytes.length;
        }
        System.arraycopy(inputNameBytes, 0, name, 0, copysize);
        this.name = name;
        if (neighbors.length > neighbors_max_length) {
            throw new Exception("too many neighbors");
        }
        this.n_neighbors = neighbors.length;
        this.neighbors = Arrays.copyOf(neighbors, neighbors_max_length);
        // TODO error if neighbors are don't exist
        this.initialized = true;
    }

    public void write() throws Exception, FileNotFoundException, IOException{
        if (!this.initialized) {
            throw new Exception("Node not initialized");
        }
        File f = new File(fn);
        if (!f.exists()) {
            System.err.println("File does not exist: " + fn);
            return;
        }

        try (
            FileOutputStream fos = new FileOutputStream(f, true);
            DataOutputStream dos = new DataOutputStream(fos);
        ) {
            dos.writeInt(this.ID);
            dos.write(this.name);
            dos.writeInt(this.n_neighbors);
            for (int e : this.neighbors) {
                dos.writeInt(e);
            }
        }
    }

    public void create_from_read(int id) throws Exception {
        //used in GET
        //TODO multithreading for non indexed queries
        // TODO should not be able to overwrite an existing object
        if (this.initialized) {
            throw new Exception("Cannot overwrite existing Node");
        }
        int s = getRowSize();
        byte[] data = new byte[s];
        try (RandomAccessFile raf = new RandomAccessFile(fn, "r")) {
            raf.seek(id*s);
            raf.readFully(data);
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
            DataInputStream ois = new DataInputStream(bais)) {
            this.ID = ois.readInt();
            ois.readFully(this.name);
            int n_neighbors = ois.readInt();
            this.n_neighbors = n_neighbors;
            int[] neighbors = new int[neighbors_max_length];
            for (int i = 0; i < n_neighbors; i++) {
                neighbors[i] = ois.readInt();
            }
            this.neighbors = neighbors;
        }
        this.initialized = true;
    }

    public void print() {
        // TODO reduce neighbors to n_neighbors
        String s = "ID: " + this.ID + "\n" +
                    "name: " + new String(this.name).trim() + "\n" +
                    "neighbors" + Arrays.toString(this.neighbors);
        System.out.println(s);

    }
}
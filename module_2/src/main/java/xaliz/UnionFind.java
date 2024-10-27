package xaliz;

import edu.princeton.cs.algs4.StdIn;

public class UnionFind {
    public static void main(String[] args) {
        int n = StdIn.readInt();
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(n);

        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();

            if (uf.connected(p, q)) {
                System.out.println(p + " " + q + " already connected");
                continue;
            }

            uf.union(p, q);
            System.out.println(p + " " + q + " connected");
        }
        System.out.println(uf.count() + " components");
    }
}

class QuickFindUF {
    private int[] id; // id[i] = component identifier of i
    private int count; // number of components

    public QuickFindUF(int N) {
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
        count = N;
    }

    public int count() {
        return count;
    }

    private void validate(int p) {
        int N = id.length;
        if (p < 0 || p >= N) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (N - 1));
        }
    }

    public int find(int p) {
        validate(p);
        return id[p];
    }

    public boolean connected(int p, int q) {
        validate(p);
        validate(q);
        return id[p] == id[q];
    }

    public void union(int p, int q) {
        validate(p);
        validate(q);
        int pID = id[p];
        int qID = id[q];

        if (pID == qID) return;

        for (int i = 0; i < id.length; i++) {
            if (id[i] == pID) {
                id[i] = qID;
            }
        }
        count--;
    }
}

class QuickUnionUF {
    private int[] parent; // parent[i] = parent of i
    private int count; // number of components

    public QuickUnionUF(int N) {
        parent = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = i;
        }
        count = N;
    }

    public int count() {
        return count;
    }

    private void validate(int p) {
        int N = parent.length;
        if (p < 0 || p >= N) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (N - 1));
        }
    }

    public int find(int p) {
        validate(p);
        while (p != parent[p]) {
            p = parent[p];
        }
        return p;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);

        if (rootP == rootQ) return;
        parent[rootP] = rootQ;

        count--;
    }
}    

// with path compression (weighted by size)
class WeightedQuickUnionUF {
    private int[] parent; // parent[i] = parent of i
    private int[] size; // size[i] = number of sites in subtree rooted at i
    private int count; // number of components

    public WeightedQuickUnionUF(int N) {
        count = N;
        parent = new int[N];
        size = new int[N];

        for (int i = 0; i < N; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int count() {
        return count;
    }

    private void validate(int p) {
        int N = parent.length;
        if (p < 0 || p >= N) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (N - 1));
        }
    }

    public int find(int p) {
        validate(p);
        while (p != parent[p]) {
            parent[p] = parent[parent[p]]; // path compression
            p = parent[p];
        }
        return p;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);

        if (rootP == rootQ) return;

        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        } else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        count--;
    }
}
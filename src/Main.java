import java.io.*;
import java.util.*;

public class Main {

    static class Edge {
        int to, rev, cap;

        Edge(int to, int rev, int cap) {
            this.to = to;
            this.rev = rev;
            this.cap = cap;
        }
    }

    static int n, m;
    static ArrayList<Edge>[] graph;

    static void addEdge(int u, int v, int cap) {
        Edge a = new Edge(v, graph[v].size(), cap);
        Edge b = new Edge(u, graph[u].size(), 0);

        graph[u].add(a);
        graph[v].add(b);
    }

    static int edmondsKarp(int s, int t) {
        int flow = 0;

        while (true) {
            Edge[] parentEdge = new Edge[n + 1];
            int[] parent = new int[n + 1];
            Arrays.fill(parent, -1);

            Queue<Integer> q = new ArrayDeque<>();
            q.add(s);
            parent[s] = s;

            while (!q.isEmpty() && parent[t] == -1) {
                int u = q.poll();

                for (Edge e : graph[u]) {
                    if (parent[e.to] == -1 && e.cap > 0) {
                        parent[e.to] = u;
                        parentEdge[e.to] = e;
                        q.add(e.to);
                    }
                }
            }

            if (parent[t] == -1) break;

            int aug = Integer.MAX_VALUE;

            for (int v = t; v != s; v = parent[v]) {
                aug = Math.min(aug, parentEdge[v].cap);
            }

            for (int v = t; v != s; v = parent[v]) {
                Edge e = parentEdge[v];
                e.cap -= aug;
                graph[e.to].get(e.rev).cap += aug;
            }

            flow += aug;
        }

        return flow;
    }
    static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = read();
            } while (c <= ' ');

            int sign = 1;
            if (c == '-') {
                sign = -1;
                c = read();
            }

            int val = 0;
            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = read();
            }

            return val * sign;
        }
    }
    public static void main(String[] args) throws Exception {

        FastScanner fs = new FastScanner();

        n = fs.nextInt();
        m = fs.nextInt();

        graph = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        int[] A = new int[m];
        int[] B = new int[m];

        for (int i = 0; i < m; i++) {
            int a = fs.nextInt();
            int b = fs.nextInt();

            A[i] = a;
            B[i] = b;

            addEdge(a, b, 1);
            addEdge(b, a, 1);
        }

        int maxFlow = edmondsKarp(1, n);

        boolean[] vis = new boolean[n + 1];
        Queue<Integer> q = new ArrayDeque<>();

        vis[1] = true;
        q.add(1);

        while (!q.isEmpty()) {
            int u = q.poll();

            for (Edge e : graph[u]) {
                if (e.cap > 0 && !vis[e.to]) {
                    vis[e.to] = true;
                    q.add(e.to);
                }
            }
        }

        StringBuilder ans = new StringBuilder();
        ans.append(maxFlow).append('\n');

        for (int i = 0; i < m; i++) {
            if (vis[A[i]] != vis[B[i]]) {
                ans.append(A[i]).append(' ')
                        .append(B[i]).append('\n');
            }
        }

        System.out.print(ans);
    }
}
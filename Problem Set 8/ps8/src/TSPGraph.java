import java.util.*;

public class TSPGraph implements IApproximateTSP {
    ArrayList<Integer> list = new ArrayList<>();
    int[] parentList;

    //depth-first search
    public void dfs(int[] list, int i) {
        for (int j = 1; j < list.length; j++) {
            if (list[j] == i) {
                this.list.add(i);
                dfs(list, i);
            }
        }
    }

    @Override
    public void MST(TSPMap map) {
        int numPoints = map.getCount();
        parentList = new int[numPoints];
        Set<Integer> visited = new HashSet<>(); // Keep track of visited vertices
        TreeMapPriorityQueue<Double, Integer> pq = new TreeMapPriorityQueue<>();

        for (int i = 0; i < numPoints; i++) {
            pq.add(i, Double.POSITIVE_INFINITY);
        }

        pq.decreasePriority(0, 0.0);

        while (!pq.isEmpty()) {
            int v = pq.extractMin();
            visited.add(v);

            for (int i = 0; i < numPoints; i++) {
                if (visited.contains(i)) continue; // Skip visited vertices
                if (pq.lookup(i) == null) continue;

                if (map.pointDistance(v, i) < pq.lookup(i)) {
                    pq.decreasePriority(i, map.pointDistance(v, i));
                    this.parentList[i] = v;
                }
            }
        }

        map.setLink(0, 0, false);

        for (int i = 1; i < numPoints; i++) {
            int parent = parentList[i];
            map.setLink(i, parent, false);
        }
        map.redraw();
    }

    @Override
    public void TSP(TSPMap map) {
        this.MST(map);
        this.list.clear();
        this.list.add(0);

        for (int i = 1; i < map.getCount(); i++) {
            if (list.get(i) == 0) {
                list.add(i);
                dfs(this.parentList,i);
            }
        }

        int size = list.size();

        for (int i = 0; i < size - 1; i++) {
            map.setLink(list.get(i), list.get(i + 1), false);
        }

        if (map.getCount() > 0) {
            map.setLink(list.get(size - 1), 0,false);
        }

        map.redraw();
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        int next = map.getLink(0);
        int parent = 0;
        int num = 1; // root
        int numPoints = map.getCount();

        if (map != null) {

            if (numPoints == 1) return true;

            while (next != parent) {
                if (next == -1) return false;
                if (numPoints == num && next == parent) return true;
                if (numPoints == num && next != parent) return false;
                num++;
                next = map.getLink(next);
            }
            return num == map.getCount();
        }
        return false;
    }

    @Override
    public double tourDistance(TSPMap map) {
        if (isValidTour(map)) return calcDist(map, 0.0, 0, 0);
        return -1;
    }
    public double calcDist(TSPMap map, double dist, int source, int start) {

        if (map.getLink(source) == start) return dist + map.pointDistance(source, start);
        double newDist = dist + map.pointDistance(map.getLink(source), source);
        return calcDist(map, newDist, map.getLink(source), start);
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "hundredpoints.txt");
        TSPGraph graph = new TSPGraph();

        graph.MST(map);
        graph.TSP(map);

    }
}

package analisis.algoritmos.ConvexHull;

import java.util.*;

class GfG {
    static class Point {
        double x, y;
        Point(double x, double y) { this.x = x; this.y = y; }
    }

    static int orientation(Point a, Point b, Point c) {
        double v = a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y);
        if (v < 0) return -1;
        if (v > 0) return  1;
        return 0;
    }

    static double distSq(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

    static int[][] findConvexHull(int[][] points) {
        int n = points.length;
        if (n < 3) return new int[][]{{-1}};

        ArrayList<Point> a = new ArrayList<>();
        for (int[] p : points) a.add(new Point(p[0], p[1]));

        // 1. Encontrar el punto más bajo (pivote p0)
        Point p0 = Collections.min(a, (p1, p2) -> {
            if (p1.y != p2.y) return Double.compare(p1.y, p2.y);
            return Double.compare(p1.x, p2.x);
        });

        // 2. Ordenar por ángulo polar respecto a p0
        a.sort((p1, p2) -> {
            int o = orientation(p0, p1, p2);
            if (o == 0) return Double.compare(distSq(p0, p1), distSq(p0, p2));
            return (o < 0) ? -1 : 1;
        });

        // 3. Recorrer y mantener giros CCW (stack)
        Stack<Point> st = new Stack<>();
        for (Point p : a) {
            while (st.size() > 1 && orientation(st.get(st.size()-2), st.peek(), p) >= 0)
                st.pop();
            st.push(p);
        }

        if (st.size() < 3) return new int[][]{{-1}};

        int[][] result = new int[st.size()][2];
        int i = 0;
        for (Point p : st) { result[i][0] = (int)p.x; result[i][1] = (int)p.y; i++; }
        return result;
    }

    public static void main(String[] args) {
        int[][] points = {
            {0,0},{1,-4},{-1,-5},{-5,-3},{-3,-1},
            {-1,-3},{-2,-2},{-1,-1},{-2,-1},{-1,1}
        };
        int[][] hull = findConvexHull(points);
        for (int[] p : hull) System.out.println(p[0] + ", " + p[1]);
    }
}
/**
 * SHORTEST PATH IN GRID
 * 
 * Problema: Dada una matriz m*n, encontrar el costo mínimo desde (0,0) hasta (m-1,n-1),
 * moviéndose solo a la derecha, en diagonal o hacia abajo.
 * 
 * RELACIÓN DE RECURRENCIA:
 * dp[i][j] = matriz[i][j] + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1])
 * 
 * Donde:
 * - dp[i][j] = costo mínimo para llegar a la posición (i,j)
 * - matriz[i][j] = costo de la celda actual
 * - Los movimientos permitidos son: arriba, izquierda, diagonal arriba-izquierda
 * 
 * OPTIMIZACIÓN DE ESPACIO A O(n):
 * En lugar de guardar la matriz completa dp[m][n] (O(m*n)), usamos un solo array
 * de tamaño n. Actualizamos el array mientras recorremos las filas.
 */

public class ShortestPathGrid {

    /**
     * Solución 1: Abordaje completo con matriz 2D (O(m*n) de espacio)
     */
    public static int minCostPath2D(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        
        // Crear tabla dp
        int[][] dp = new int[m][n];
        
        // Caso base: celda inicial
        dp[0][0] = matrix[0][0];
        
        // Llenar la primera fila (solo podemos llegar desde la izquierda)
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j-1] + matrix[0][j];
        }
        
        // Llenar la primera columna (solo podemos llegar desde arriba)
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i-1][0] + matrix[i][0];
        }
        
        // Llenar el resto de la tabla
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // Mínimo de: arriba, izquierda, diagonal
                int fromTop = dp[i-1][j];
                int fromLeft = dp[i][j-1];
                int fromDiagonal = dp[i-1][j-1];
                
                dp[i][j] = matrix[i][j] + Math.min(fromTop, Math.min(fromLeft, fromDiagonal));
            }
        }
        
        return dp[m-1][n-1];
    }

    /**
     * Solución 2: Optimizada a O(n) de espacio
     * 
     * Usamos un solo array de tamaño n que actualizamos fila por fila.
     * En cada iteración, el array contiene los resultados de la fila anterior.
     * Se necesita una variable auxiliar para guardar el valor diagonal.
     * 
     * La clave: guardamos dp[j] ANTES de actualizarlo, porque representa dp[i-1][j]
     * y será el diagonal para dp[i][j+1]
     */
    public static int minCostPathOptimized(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        
        // Usar un array 1D de tamaño n
        int[] dp = new int[n];
        
        // Caso base: primera celda
        dp[0] = matrix[0][0];
        
        // Llenar primera fila (solo desde la izquierda)
        for (int j = 1; j < n; j++) {
            dp[j] = dp[j-1] + matrix[0][j];
        }
        
        // Procesar filas restantes
        for (int i = 1; i < m; i++) {
            int prevDiagonal = 0;  // dp[i-1][-1] = 0, no usaremos esto
            
            for (int j = 0; j < n; j++) {
                int savedTop = dp[j];  // Guardar dp[i-1][j] antes de sobrescribir
                
                if (j == 0) {
                    // Primera columna: solo desde arriba
                    dp[0] = dp[0] + matrix[i][0];
                } else {
                    // Para j > 0: min de arriba, izquierda, diagonal
                    int fromTop = savedTop;           // dp[i-1][j]
                    int fromLeft = dp[j-1];           // dp[i][j-1] (ya actualizado)
                    int fromDiagonal = prevDiagonal;  // dp[i-1][j-1]
                    
                    int minCost = Math.min(fromTop, Math.min(fromLeft, fromDiagonal));
                    dp[j] = matrix[i][j] + minCost;
                }
                
                prevDiagonal = savedTop;  // Para la siguiente columna, esto será el diagonal
            }
        }
        
        return dp[n-1];
    }

    /**
     * Solución 3: Con reconstrucción del camino
     */
    public static void minCostPathWithPath(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        
        int[][] dp = new int[m][n];
        int[][] path = new int[m][n]; // 0=diagonal, 1=arriba, 2=izquierda
        
        // Inicialización
        dp[0][0] = matrix[0][0];
        
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j-1] + matrix[0][j];
            path[0][j] = 2; // vinieron de izquierda
        }
        
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i-1][0] + matrix[i][0];
            path[i][0] = 1; // vinieron de arriba
        }
        
        // Llenar tabla
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                int diagonal = dp[i-1][j-1];
                int top = dp[i-1][j];
                int left = dp[i][j-1];
                
                int minVal = Math.min(diagonal, Math.min(top, left));
                dp[i][j] = matrix[i][j] + minVal;
                
                if (minVal == diagonal) path[i][j] = 0;
                else if (minVal == top) path[i][j] = 1;
                else path[i][j] = 2;
            }
        }
        
        // Reconstruir camino
        System.out.println("Costo mínimo: " + dp[m-1][n-1]);
        System.out.print("Camino: ");
        
        int i = m - 1, j = n - 1;
        while (i > 0 || j > 0) {
            System.out.print("(" + i + "," + j + ") ");
            if (path[i][j] == 0) { i--; j--; } // diagonal
            else if (path[i][j] == 1) { i--; }  // arriba
            else { j--; }                        // izquierda
        }
        System.out.println("(0,0)");
    }

    public static void main(String[] args) {
        // Ejemplo 1: Matriz de prueba
        int[][] matrix = {
            {1, 3, 1},
            {1, 5, 1},
            {4, 2, 1}
        };
        
        System.out.println("=== SHORTEST PATH IN GRID ===\n");
        System.out.println("Matriz:");
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
        
        System.out.println("\n--- Solución 1: Con matriz 2D ---");
        int result1 = minCostPath2D(matrix);
        System.out.println("Costo mínimo: " + result1);
        
        System.out.println("\n--- Solución 2: Optimizada a O(n) ---");
        int result2 = minCostPathOptimized(matrix);
        System.out.println("Costo mínimo: " + result2);
        
        System.out.println("\n--- Solución 3: Con reconstrucción del camino ---");
        minCostPathWithPath(matrix);
        
        // Ejemplo 2: Matriz más grande
        System.out.println("\n\n=== EJEMPLO 2 ===");
        int[][] matrix2 = {
            {1, 2, 3, 4},
            {5, 1, 2, 3},
            {4, 5, 1, 2}
        };
        
        System.out.println("Matriz:");
        for (int[] row : matrix2) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
        
        System.out.println("\nCosto mínimo: " + minCostPathOptimized(matrix2));
    }
}

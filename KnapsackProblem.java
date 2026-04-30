/**
 * PROBLEMA DE LA MOCHILA (KNAPSACK - 0/1)
 * 
 * DESCRIPCIÓN:
 * Maximizar la ganancia al elegir productos que caben en la mochila sin fraccionar
 * (no podemos dividir los ítems)
 * 
 * DATOS DEL PROBLEMA:
 * Producto | Peso | Valor
 *    A     |  1   |  2
 *    B     |  3   |  5
 *    C     |  4   |  10
 *    D     |  5   |  14
 *    E     |  7   |  15
 * 
 * Capacidad máxima de la mochila: 8
 * 
 * RELACIÓN DE RECURRENCIA:
 * dp[i][w] = máximo valor usando los primeros i ítems con capacidad w
 * 
 * dp[i][w] = máx(
 *    dp[i-1][w],                           // No incluir el ítem i
 *    valor[i] + dp[i-1][w - peso[i]]     // Incluir el ítem i (si cabe)
 * )
 * 
 * COMPLEJIDAD:
 * - Tiempo: O(n * W) donde n = número de ítems, W = capacidad
 * - Espacio: O(n * W) para la tabla, pero se puede optimizar a O(W)
 * 
 * ¿POR QUÉ NO FUNCIONA EL ENFOQUE VORAZ?
 * El enfoque voraz seleccionaría los ítems con mayor valor o mejor relación valor/peso
 * de forma local sin considerar combinaciones óptimas.
 * 
 * Ejemplo: Si elegimos por relación valor/peso:
 * A: 2/1 = 2.0
 * B: 5/3 = 1.67
 * C: 10/4 = 2.5  ← Mejor
 * D: 14/5 = 2.8  ← Mejor
 * E: 15/7 = 2.14
 * 
 * Voraz: Tomar D (peso=5, valor=14), luego A (peso=1, valor=2) = Peso=6, Valor=16
 * Sobra capacidad=2, no entra nada más. Valor total = 16
 * 
 * Óptimo: Tomar D (peso=5, valor=14) + C (peso=4, valor=10) = Peso=9 > 8 ✗
 * Óptimo: Tomar C (peso=4, valor=10) + B (peso=3, valor=5) + A (peso=1, valor=2)
 *         = Peso=8, Valor=17 ✓ (MEJOR QUE VORAZ)
 */

public class KnapsackProblem {

    static class Item {
        String name;
        int weight;
        int value;

        Item(String name, int weight, int value) {
            this.name = name;
            this.weight = weight;
            this.value = value;
        }
    }

    /**
     * Solución 1: Tabla 2D completa (O(n*W) espacio)
     * Más clara para entender cómo se construye la tabla
     */
    public static int knapsack2D(Item[] items, int capacity) {
        int n = items.length;
        
        // dp[i][w] = valor máximo usando primeros i ítems con capacidad w
        int[][] dp = new int[n + 1][capacity + 1];
        
        // Base: dp[0][w] = 0 (sin ítems, valor = 0)
        // Base: dp[i][0] = 0 (capacidad 0, valor = 0)
        // Inicializadas automáticamente a 0
        
        // Llenar la tabla
        for (int i = 1; i <= n; i++) {
            Item item = items[i - 1];
            
            for (int w = 0; w <= capacity; w++) {
                // Opción 1: No incluir el ítem i
                int skipItem = dp[i - 1][w];
                
                // Opción 2: Incluir el ítem i (si cabe)
                int takeItem = 0;
                if (item.weight <= w) {
                    takeItem = item.value + dp[i - 1][w - item.weight];
                }
                
                // Tomar el máximo
                dp[i][w] = Math.max(skipItem, takeItem);
            }
        }
        
        // Imprimir la tabla para visualización
        System.out.println("\nTabla DP (primeros 5 productos y capacidades hasta 8):");
        System.out.print("    ");
        for (int w = 0; w <= capacity; w++) {
            System.out.printf("%3d ", w);
        }
        System.out.println();
        
        for (int i = 0; i <= n; i++) {
            if (i == 0) System.out.print("  0 ");
            else System.out.print(items[i-1].name + " ");
            
            for (int w = 0; w <= capacity; w++) {
                System.out.printf("%3d ", dp[i][w]);
            }
            System.out.println();
        }
        
        return dp[n][capacity];
    }

    /**
     * Solución 2: Reconstruir la solución (qué ítems incluir)
     */
    public static void knapsackWithSelection(Item[] items, int capacity) {
        int n = items.length;
        int[][] dp = new int[n + 1][capacity + 1];
        
        // Llenar tabla
        for (int i = 1; i <= n; i++) {
            Item item = items[i - 1];
            for (int w = 0; w <= capacity; w++) {
                int skipItem = dp[i - 1][w];
                int takeItem = 0;
                if (item.weight <= w) {
                    takeItem = item.value + dp[i - 1][w - item.weight];
                }
                dp[i][w] = Math.max(skipItem, takeItem);
            }
        }
        
        // Reconstruir la solución
        System.out.println("\n--- RECONSTRUCCIÓN DE LA SOLUCIÓN ---");
        int maxValue = dp[n][capacity];
        System.out.println("Valor máximo: " + maxValue);
        
        // Retrotrazar para encontrar qué ítems incluir
        int w = capacity;
        System.out.print("Ítems seleccionados: ");
        int totalWeight = 0;
        
        for (int i = n; i > 0 && w > 0; i--) {
            // Si el valor vino de incluir este ítem
            if (dp[i][w] != dp[i-1][w]) {
                Item item = items[i-1];
                System.out.print(item.name + "(w=" + item.weight + ",v=" + item.value + ") ");
                w -= item.weight;
                totalWeight += item.weight;
            }
        }
        
        System.out.println("\nPeso total usado: " + totalWeight + " / " + capacity);
    }

    /**
     * Solución 3: Optimizada a O(W) espacio (1D array)
     */
    public static int knapsackOptimized(Item[] items, int capacity) {
        // dp[w] = valor máximo con capacidad w
        int[] dp = new int[capacity + 1];
        
        // Para cada ítem
        for (Item item : items) {
            // Recorrer de atrás hacia adelante para evitar usar el mismo ítem dos veces
            for (int w = capacity; w >= item.weight; w--) {
                dp[w] = Math.max(
                    dp[w],  // No incluir el ítem
                    item.value + dp[w - item.weight]  // Incluir el ítem
                );
            }
        }
        
        return dp[capacity];
    }

    /**
     * Demostración de por qué el enfoque VORAZ NO FUNCIONA
     */
    public static void demonstrateGreedyFailure(Item[] items, int capacity) {
        System.out.println("\n\n=== ¿POR QUÉ EL ENFOQUE VORAZ NO FUNCIONA? ===\n");
        
        // Crear copia y ordenar por relación valor/peso
        Item[] sorted = items.clone();
        java.util.Arrays.sort(sorted, (a, b) -> 
            Double.compare((double)b.value/b.weight, (double)a.value/a.weight)
        );
        
        System.out.println("Ítems ordenados por relación valor/peso (descendente):");
        for (Item item : sorted) {
            System.out.printf("%s: valor/peso = %.2f\n", 
                item.name, (double)item.value/item.weight);
        }
        
        // Aplicar enfoque voraz
        System.out.println("\nEnfoque VORAZ (tomar ítems en orden hasta llenar):");
        int greedyWeight = 0;
        int greedyValue = 0;
        System.out.print("Selección: ");
        
        for (Item item : sorted) {
            if (greedyWeight + item.weight <= capacity) {
                System.out.print(item.name + " ");
                greedyWeight += item.weight;
                greedyValue += item.value;
            }
        }
        
        System.out.println("\nPeso: " + greedyWeight + ", Valor: " + greedyValue);
        
        // Enfoque óptimo con DP
        int optimalValue = knapsackOptimized(items, capacity);
        System.out.println("\nEnfoque ÓPTIMO (Programación Dinámica): Valor = " + optimalValue);
        
        System.out.println("\n❌ El voraz obtuvo: " + greedyValue);
        System.out.println("✓ El óptimo obtuvo: " + optimalValue);
        
        if (greedyValue < optimalValue) {
            System.out.println("DIFERENCIA: " + (optimalValue - greedyValue) + 
                             " unidades de valor perdidas");
        }
    }

    public static void main(String[] args) {
        // Crear los ítems del problema
        Item[] items = {
            new Item("A", 1, 2),
            new Item("B", 3, 5),
            new Item("C", 4, 10),
            new Item("D", 5, 14),
            new Item("E", 7, 15)
        };
        
        int capacity = 8;
        
        System.out.println("=== PROBLEMA DE LA MOCHILA (KNAPSACK) ===");
        System.out.println("\nDatos:");
        System.out.println("Capacidad máxima: " + capacity);
        System.out.println("\nÍtems:");
        System.out.println("Producto | Peso | Valor");
        for (Item item : items) {
            System.out.printf("   %s     |  %d   |  %d\n", 
                item.name, item.weight, item.value);
        }
        
        // Solución 1: Tabla 2D
        System.out.println("\n--- SOLUCIÓN 1: TABLA 2D ---");
        int result1 = knapsack2D(items, capacity);
        System.out.println("\nValor máximo: " + result1);
        
        // Solución 2: Con reconstrucción
        System.out.println("\n--- SOLUCIÓN 2: RECONSTRUCCIÓN ---");
        knapsackWithSelection(items, capacity);
        
        // Solución 3: Optimizada
        System.out.println("\n--- SOLUCIÓN 3: OPTIMIZADA (O(W) espacio) ---");
        int result3 = knapsackOptimized(items, capacity);
        System.out.println("Valor máximo: " + result3);
        
        // Demostración del fallo del voraz
        demonstrateGreedyFailure(items, capacity);

        // EXPLICACIÓN ADICIONAL
        System.out.println("\n\n=== EXPLICACIÓN DE LA TABLA DP ===");
        System.out.println("""
            La tabla dp[i][w] representa:
            - Filas: primeros i ítems (0 a n)
            - Columnas: capacidades de 0 a W
            
            Cómo se construye:
            1. Primera fila y columna: todos ceros (sin ítems o sin capacidad = valor 0)
            2. Para cada celda dp[i][w]:
               - Si el ítem i no cabe (peso[i] > w): dp[i][w] = dp[i-1][w]
               - Si cabe: dp[i][w] = máx(dp[i-1][w], valor[i] + dp[i-1][w-peso[i]])
            
            Valores:
            - dp[i-1][w]: valor si NO incluimos el ítem i
            - valor[i] + dp[i-1][w-peso[i]]: valor si INCLUIMOS el ítem i
            
            El óptimo global está en dp[n][W] - esquina inferior derecha.
            """);
    }
}

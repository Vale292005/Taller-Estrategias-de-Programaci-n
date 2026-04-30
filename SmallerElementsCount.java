import java.util.Arrays;

/**
 * DIVIDE Y VENCERÁS: Smaller Elements Count
 * 
 * Problema: Dado un arreglo de N elementos, para cada posición A[i], 
 * determine cuántos elementos a su derecha son menores que él.
 * 
 * Entrada:  [5, 2, 6, 1, 3]
 * Salida:   [3, 1, 2, 0, 0]
 * 
 * ENFOQUE: Merge Sort modificado
 * - Dividir el array en segmentos
 * - Conquistar recursivamente
 * - Combinar mientras contamos inversiones
 */

public class SmallerElementsCount {
    static class Pair {
        int value;
        int index;
        Pair(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }

    /**
     * Calcula para cada elemento cuántos valores menores hay a su derecha
     * Complejidad: O(n log n)
     */
    public static int[] countSmaller(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Pair[] pairs = new Pair[n];
        
        // Crear pares (valor, índice original)
        for (int i = 0; i < n; i++) {
            pairs[i] = new Pair(nums[i], i);
        }
        
        // Merge sort que cuenta inversiones
        mergeSort(pairs, 0, n - 1, result);
        return result;
    }

    private static void mergeSort(Pair[] pairs, int left, int right, int[] result) {
        if (left >= right) {
            return;
        }
        
        int mid = left + (right - left) / 2;
        mergeSort(pairs, left, mid, result);
        mergeSort(pairs, mid + 1, right, result);
        merge(pairs, left, mid, right, result);
    }

    private static void merge(Pair[] pairs, int left, int mid, int right, int[] result) {
        Pair[] temp = new Pair[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;
        int rightCount = 0;  // Cuenta de elementos del lado derecho que son menores

        // Merge manteniendo orden y contando
        while (i <= mid && j <= right) {
            if (pairs[i].value <= pairs[j].value) {
                // El elemento izquierdo es menor o igual
                // Todo lo que hemos tomado del derecho es menor que este
                result[pairs[i].index] += rightCount;
                temp[k++] = pairs[i++];
            } else {
                // El elemento derecho es menor que el izquierdo
                // Incrementar contador para el siguiente elemento izquierdo
                rightCount++;
                temp[k++] = pairs[j++];
            }
        }

        // Procesar elementos restantes del lado izquierdo
        while (i <= mid) {
            result[pairs[i].index] += rightCount;
            temp[k++] = pairs[i++];
        }

        // Procesar elementos restantes del lado derecho
        while (j <= right) {
            temp[k++] = pairs[j++];
        }

        // Copiar de vuelta al array original
        for (int x = 0; x < temp.length; x++) {
            pairs[left + x] = temp[x];
        }
    }

    /**
     * Solución alternativa usando BinarySearchTree
     */
    static class Node {
        int val;
        int count;  // Elementos en el subárbol izquierdo
        Node left, right;
        
        Node(int val) {
            this.val = val;
            this.count = 0;
        }
    }

    public static int[] countSmallerBST(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Node root = null;

        // Recorrer de derecha a izquierda
        for (int i = n - 1; i >= 0; i--) {
            root = insertAndCount(root, nums[i], 0, result, i);
        }

        return result;
    }

    private static Node insertAndCount(Node node, int val, int count, int[] result, int index) {
        if (node == null) {
            node = new Node(val);
            result[index] = count;
            return node;
        }

        if (val < node.val) {
            // Ir a la izquierda, contar este nodo
            node.count++;
            node.left = insertAndCount(node.left, val, count, result, index);
        } else {
            // Ir a la derecha, sumar this.count + 1 (el nodo actual)
            node.right = insertAndCount(node.right, val, count + node.count + 1, result, index);
        }

        return node;
    }

    public static void main(String[] args) {
        System.out.println("=== DIVIDE Y VENCERÁS: Smaller Elements Count ===\n");

        // Ejemplo 1: Caso del enunciado
        int[] nums1 = {5, 2, 6, 1, 3};
        System.out.println("Entrada:  " + Arrays.toString(nums1));
        int[] result1 = countSmaller(nums1);
        System.out.println("Salida:   " + Arrays.toString(result1));
        
        System.out.println("\nExplicación:");
        for (int i = 0; i < nums1.length; i++) {
            System.out.print("  " + nums1[i] + ": ");
            int count = 0;
            for (int j = i + 1; j < nums1.length; j++) {
                if (nums1[j] < nums1[i]) {
                    System.out.print(nums1[j] + " ");
                    count++;
                }
            }
            System.out.println("(" + count + " menores)");
        }

        // Ejemplo 2
        System.out.println("\n--- Ejemplo 2 ---");
        int[] nums2 = {8, 1, 2, 2, 3};
        System.out.println("Entrada:  " + Arrays.toString(nums2));
        int[] result2 = countSmaller(nums2);
        System.out.println("Salida:   " + Arrays.toString(result2));

        // Ejemplo 3: Usar BST
        System.out.println("\n--- Comparación con enfoque BST ---");
        int[] resultBST = countSmallerBST(nums1);
        System.out.println("BST Entrada:  " + Arrays.toString(nums1));
        System.out.println("BST Salida:   " + Arrays.toString(resultBST));

        // Ejemplo 4: Array ordenado
        System.out.println("\n--- Caso: Array ya ordenado ---");
        int[] nums3 = {1, 2, 3, 4, 5};
        System.out.println("Entrada:  " + Arrays.toString(nums3));
        int[] result3 = countSmaller(nums3);
        System.out.println("Salida:   " + Arrays.toString(result3));
        System.out.println("(Esperado: todos ceros, no hay menores a la derecha)");

        // Ejemplo 5: Array inverso
        System.out.println("\n--- Caso: Array ordenado inversamente ---");
        int[] nums4 = {5, 4, 3, 2, 1};
        System.out.println("Entrada:  " + Arrays.toString(nums4));
        int[] result4 = countSmaller(nums4);
        System.out.println("Salida:   " + Arrays.toString(result4));

        // Análisis
        System.out.println("\n\n=== ANÁLISIS DEL ALGORITMO ===");
        System.out.println("Complejidad Temporal: O(n log n)");
        System.out.println("  - Merge Sort: O(n log n)");
        System.out.println("  - Cada elemento se procesa en cada nivel");
        System.out.println();
        System.out.println("Complejidad Espacial: O(n)");
        System.out.println("  - Array temporal en merge: O(n)");
        System.out.println("  - Array de pares: O(n)");
        System.out.println();
        System.out.println("¿Por qué es Divide y Vencerás?");
        System.out.println("  1. DIVIDE: Partir el array en dos mitades");
        System.out.println("  2. VENCERÁS: Resolver recursivamente cada mitad");
        System.out.println("  3. COMBINA: Mientras mergeamos, contamos inversiones");
        System.out.println();
        System.out.println("La clave: Al hacer merge de dos arrays ordenados,");
        System.out.println("cuando un elemento de izquierda > uno de derecha,");
        System.out.println("sabemos que hay 'rightCount' elementos menores a su derecha.");
    }
}

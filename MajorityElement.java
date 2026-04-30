import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DIVIDE Y VENCERÁS: Majority Element
 * 
 * Problema: Dado un arreglo de N elementos, determine si existe un número
 * que aparece más de N/2 veces (estrictamente más de la mitad del tamaño 
 * del arreglo). Si existe tal número, retórnelo. En caso contrario, retorne -1.
 * 
 * Entrada:  [2, 2, 1, 1, 1, 2, 2]
 * Salida:   2
 * 
 * ENFOQUE: Divide y Vencerás
 * - Dividir en dos mitades
 * - Conquistar recursivamente
 * - Vencerás comparando los candidatos de cada lado
 */

public class MajorityElement {

    /**
     * Encuentra el elemento mayoritario (aparece > N/2 veces)
     * usando Divide y Vencerás
     * Complejidad: O(n log n)
     */
    public static int findMajority(int[] nums) {
        int result = findMajorityHelper(nums, 0, nums.length - 1);
        
        // Verificar que aparece más de N/2 veces
        int count = 0;
        for (int num : nums) {
            if (num == result) count++;
        }
        
        if (count > nums.length / 2) {
            return result;
        }
        return -1;
    }

    private static int findMajorityHelper(int[] nums, int left, int right) {
        // Caso base: un solo elemento
        if (left == right) {
            return nums[left];
        }

        // DIVIDE: partir en dos mitades
        int mid = left + (right - left) / 2;
        int leftMajority = findMajorityHelper(nums, left, mid);
        int rightMajority = findMajorityHelper(nums, mid + 1, right);

        // VENCERÁS: contar los candidatos en todo el rango
        int leftCount = countInRange(nums, leftMajority, left, right);
        int rightCount = countInRange(nums, rightMajority, left, right);

        // COMBINA: retornar el que aparece más
        return leftCount > rightCount ? leftMajority : rightMajority;
    }

    private static int countInRange(int[] nums, int target, int left, int right) {
        int count = 0;
        for (int i = left; i <= right; i++) {
            if (nums[i] == target) count++;
        }
        return count;
    }

    /**
     * Solución alternativa: Boyer-Moore Voting Algorithm
     * (No es D&C, pero es más eficiente)
     * Complejidad: O(n)
     */
    public static int findMajorityEfficient(int[] nums) {
        int candidate = nums[0];
        int count = 1;

        // Primera pasada: encontrar candidato
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == candidate) {
                count++;
            } else {
                count--;
                if (count == 0) {
                    candidate = nums[i];
                    count = 1;
                }
            }
        }

        // Segunda pasada: verificar que el candidato aparece más de N/2 veces
        count = 0;
        for (int num : nums) {
            if (num == candidate) count++;
        }

        if (count > nums.length / 2) {
            return candidate;
        }
        return -1;
    }

    /**
     * Solución con HashMap (enfoque trivial)
     * Complejidad: O(n) pero usa O(n) espacio
     */
    public static int findMajorityHashMap(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        int threshold = nums.length / 2;

        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > threshold) {
                return entry.getKey();
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        System.out.println("=== DIVIDE Y VENCERÁS: Majority Element ===\n");

        // Ejemplo 1: Caso del enunciado
        int[] nums1 = {2, 2, 1, 1, 1, 2, 2};
        System.out.println("Entrada:  " + Arrays.toString(nums1));
        System.out.println("Tamaño: " + nums1.length + ", Mayoría: > " + (nums1.length / 2) + " veces");
        
        int result1 = findMajority(nums1);
        System.out.println("Salida (D&C): " + result1);
        
        if (result1 != -1) {
            int count = 0;
            for (int num : nums1) {
                if (num == result1) count++;
            }
            System.out.println("Verificación: " + result1 + " aparece " + count + " veces ✓");
        }

        // Ejemplo 2: Mayoría muy clara
        System.out.println("\n--- Ejemplo 2: Mayoría muy clara ---");
        int[] nums2 = {3, 3, 4, 2, 4, 4, 2, 4, 4};
        System.out.println("Entrada:  " + Arrays.toString(nums2));
        System.out.println("Tamaño: " + nums2.length + ", Mayoría: > " + (nums2.length / 2) + " veces");
        
        int result2 = findMajority(nums2);
        System.out.println("Salida (D&C): " + result2);
        
        if (result2 != -1) {
            int count = 0;
            for (int num : nums2) {
                if (num == result2) count++;
            }
            System.out.println("Verificación: " + result2 + " aparece " + count + " veces ✓");
        }

        // Ejemplo 3: Sin mayoría
        System.out.println("\n--- Ejemplo 3: Sin elemento mayoritario ---");
        int[] nums3 = {1, 2, 3, 4, 5};
        System.out.println("Entrada:  " + Arrays.toString(nums3));
        System.out.println("Tamaño: " + nums3.length + ", Mayoría: > " + (nums3.length / 2) + " veces");
        
        int result3 = findMajority(nums3);
        System.out.println("Salida (D&C): " + result3);
        System.out.println("No existe elemento mayoritario ✓");

        // Comparación de métodos
        System.out.println("\n\n=== COMPARACIÓN DE MÉTODOS ===\n");

        System.out.println("Método 1: Divide y Vencerás (D&C)");
        int dncResult = findMajority(nums1);
        System.out.println("  Entrada: " + Arrays.toString(nums1));
        System.out.println("  Resultado: " + dncResult);
        System.out.println("  Complejidad: O(n log n) tiempo, O(log n) espacio");

        System.out.println("\nMétodo 2: Boyer-Moore Voting");
        int bmResult = findMajorityEfficient(nums1);
        System.out.println("  Entrada: " + Arrays.toString(nums1));
        System.out.println("  Resultado: " + bmResult);
        System.out.println("  Complejidad: O(n) tiempo, O(1) espacio");

        System.out.println("\nMétodo 3: HashMap");
        int hmResult = findMajorityHashMap(nums1);
        System.out.println("  Entrada: " + Arrays.toString(nums1));
        System.out.println("  Resultado: " + hmResult);
        System.out.println("  Complejidad: O(n) tiempo, O(n) espacio");

        // Análisis
        System.out.println("\n\n=== ANÁLISIS ===\n");
        System.out.println("DIVIDE Y VENCERÁS:");
        System.out.println("  1. DIVIDE: Partir el array en dos mitades");
        System.out.println("  2. VENCERÁS: Resolver recursivamente cada mitad");
        System.out.println("  3. COMBINA: Contar ambos candidatos en todo el rango");
        System.out.println("  Complejidad: O(n log n) tiempo, O(log n) espacio (stack)");
        System.out.println();
        System.out.println("BOYER-MOORE (más eficiente, NO es D&C):");
        System.out.println("  - Mantiene un candidato y un contador");
        System.out.println("  - Si contador llega a 0, cambia de candidato");
        System.out.println("  - Complejidad: O(n) tiempo, O(1) espacio");
        System.out.println();
        System.out.println("¿Cuándo usar cada uno?");
        System.out.println("  - D&C: Cuando se requiere demostrar el patrón");
        System.out.println("  - Boyer-Moore: Cuando se prioriza eficiencia");
        System.out.println("  - HashMap: Cuando hay limitaciones de tiempo/espacio trade-off");
    }
}

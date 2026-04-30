import java.util.Arrays;

class DivideYVenceras {
    static class Pair {
        int value;
        int index;
        Pair(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }

    public static int[] countSmaller(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Pair[] pairs = new Pair[n];
        for (int i = 0; i < n; i++) {
            pairs[i] = new Pair(nums[i], i);
        }
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
        int rightCount = 0;

        while (i <= mid && j <= right) {
            if (pairs[i].value <= pairs[j].value) {
                result[pairs[i].index] += rightCount;
                temp[k++] = pairs[i++];
            } else {
                rightCount++;
                temp[k++] = pairs[j++];
            }
        }

        while (i <= mid) {
            result[pairs[i].index] += rightCount;
            temp[k++] = pairs[i++];
        }

        while (j <= right) {
            temp[k++] = pairs[j++];
        }

        System.arraycopy(temp, 0, pairs, left, temp.length);
    }

    public static void main(String[] args) {
        int[] prueba1 = {5, 2, 6, 1, 3};
        int[] prueba2 = {1, 2, 3};
        int[] prueba3 = {3, 2, 1};

        System.out.println("Entrada: " + Arrays.toString(prueba1));
        System.out.println("Resultado: " + Arrays.toString(countSmaller(prueba1)));
        System.out.println("Esperado: [3, 1, 2, 0, 0]\n");

        System.out.println("Entrada: " + Arrays.toString(prueba2));
        System.out.println("Resultado: " + Arrays.toString(countSmaller(prueba2)));
        System.out.println("Esperado: [0, 0, 0]\n");

        System.out.println("Entrada: " + Arrays.toString(prueba3));
        System.out.println("Resultado: " + Arrays.toString(countSmaller(prueba3)));
        System.out.println("Esperado: [2, 1, 0]");
    }
}

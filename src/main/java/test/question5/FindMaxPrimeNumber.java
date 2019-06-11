/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.question5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author toannd4
 */
class FindMaxPrimeNumber {

    private static List<Integer> combinationArr = new ArrayList<>();
    private static Set<String> permutationArr = new HashSet<>();

    public static List<Integer> getCombinationArr() {
        return combinationArr;
    }

    public static void setCombinationArr(List<Integer> arr) {
        combinationArr = arr;
    }

    public static void clearCombinationArr() {
        combinationArr.clear();
    }

    public static Set<String> getPermutationArr() {
        return permutationArr;
    }

    public static void setPermutationArr(Set<String> arr) {
        permutationArr = arr;
    }

    public static void clearPermutationArr() {
        permutationArr.clear();
    }

    /* arr[]  ---> Input Array 
    data[] ---> Temporary array to store current combination 
    start & end ---> Staring and Ending indexes in arr[] 
    index  ---> Current index in data[] 
    r ---> Size of a combination to be printed */
    public static void combinationUtil(int arr[], int data[], int start, int end, int index, int r) {
        // return current combination
        if (index == r) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < r; j++) {
                builder.append(data[j]);
            }
            getCombinationArr().add(Integer.parseInt(builder.toString()));
            return;
        }

        // replace index with all possible elements. The condition 
        // "end-i+1 >= r-index" makes sure that including one element 
        // at index will make a combination with remaining elements 
        // at remaining positions 
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data[index] = arr[i];
            combinationUtil(arr, data, i + 1, end, index + 1, r);
        }
    }

    // The main function that prints all combinations of size r 
    // in arr[] of size n. This function mainly uses combinationUtil() 
    public static List<Integer> printCombination(int arr[], int n, int r) {

        int data[] = new int[r];
        combinationUtil(arr, data, 0, n - 1, 0, r);
        Collections.sort(getCombinationArr(), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        return getCombinationArr();
    }

    private static void permute(String str, int l, int r) {
        if (l == r) {
            getPermutationArr().add(str);
        } else {
            for (int i = l; i <= r; i++) {
                str = swap(str, l, i);
                permute(str, l + 1, r);
                str = swap(str, l, i);
            }
        }
    }

    /**
     * Swap Characters at position
     *
     * @param a string value
     * @param i position 1
     * @param j position 2
     * @return swapped string
     */
    public static String swap(String a, int i, int j) {
        char temp;
        char[] charArray = a.toCharArray();
        temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }

    public static boolean isPrimeNumber(int n) {
        if (n < 2) {
            return false;
        }

        int squareRoot = (int) Math.sqrt(n);
        for (int i = 2; i <= squareRoot; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int findMaxPrimeNumber(String number) {
        int[] arr = Arrays.stream(number.split("")).mapToInt(Integer::parseInt).toArray();
        int n = arr.length;
        for (int i = number.length()-1; i > 0; i--) {
            clearCombinationArr();
            int r = i;
            List<Integer> cArr = printCombination(arr, n, r);
            for (int j = 0; j < cArr.size(); j++) {
                clearPermutationArr();
                String pr = String.valueOf(cArr.get(j));
                permute(pr, 0, pr.length() - 1);
                int res = -1;
                for (String item: getPermutationArr()) {
                    int num = Integer.parseInt(item);
                    if (isPrimeNumber(num) && num > res) {
                        res = num;
                    }
                }
                if (res > 1) {
                    return res;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int result = findMaxPrimeNumber("137");
        if (result > 0) {
            System.out.println("The max prime number is " + result);
        } else {
            System.out.println("Can't find the max prime number");
        }
    }
}

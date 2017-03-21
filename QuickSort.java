/**
 * 15619 Cloud Computing
 * Project 1.1 Sequential Analysis
 * Author: Zhiyi (Lisa) Ding
 * Andrew ID: zhiyid
 * Date: Jan 26, 2017
 */

/** Sort key-value pairs using Quick Sort Algorithm
 * First by count_view reversely, then by page title in ascending lexicographical order.
 * The code is referenced from : Program: Implement quick sort in java
 * Link: http://www.java2novice.com/java-sorting-algorithms/quick-sort/
 */

public class QuickSort {

    private String array[][];

    public void sort(String[][] inputArr) {

        if (inputArr == null || inputArr.length == 0) {
            return;
        }

        this.array = inputArr;
        int length = inputArr.length;
        quickSort(0, length - 1);
    }

    private void quickSort(int lowIndex, int highIndex) {
        int i = lowIndex;
        int j = highIndex;
        int mid = lowIndex + (highIndex - lowIndex)/2;

        String[] pivot = {array[mid][0], array[mid][1]};

        while (i <= j) {
            while (compareI(i, pivot)) {
                i++;
            }
            while (compareJ(j, pivot)) {
                j--;
            }
            if (i <= j) {
                exchangeLine (i, j);
                i++;
                j--;
            }
        }

        if (lowIndex < j) {
            quickSort(lowIndex, j);
        }
        if (i < highIndex) {
            quickSort(i, highIndex);
        }

    }

    private boolean compareI (int index, String[] pivot) {
        // Sort word by ascending lexicographical order if frequency is the same.
        if (Integer.parseInt(array[index][1]) == Integer.parseInt(pivot[1])) {
            return array[index][0].compareTo(pivot[0]) < 0;
        } else {
            // Sort frequency in descending order.
            return Integer.parseInt(array[index][1]) > Integer.parseInt(pivot[1]);
        }
    }

    private boolean compareJ (int index, String[] pivot) {
        if (Integer.parseInt(array[index][1]) == Integer.parseInt(pivot[1])) {
            return array[index][0].compareTo(pivot[0]) > 0;
        } else {
            return Integer.parseInt(array[index][1]) < Integer.parseInt(pivot[1]);
        }
    }

    private void exchangeLine(int i, int j) {
        String[] temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

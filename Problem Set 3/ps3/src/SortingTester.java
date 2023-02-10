import java.util.Random;

public class SortingTester {
    public static boolean checkSort(ISort sorter, int size) {
        if (size < 2 || size > Integer.MAX_VALUE) {
            return false;
        }
        KeyValuePair[] array = new KeyValuePair[size];
        Random random = new Random(5);
        for (int i = 0; i < size; i ++) {
           array[i] = new KeyValuePair(random.nextInt(1000), 0);
        }
        sorter.sort(array);
        for (int i = 0; i < size - 1; i ++) {
            if (array[i + 1].getKey() < array[i].getKey()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isStable(ISort sorter, int size) {
        KeyValuePair[] array = new KeyValuePair[size];
        Random random = new Random(5);
        int increment = 0;
        for (int i = 0; i < size; i ++) {
            array[i] = new KeyValuePair(random.nextInt(20), increment);
            increment++;
        }
        sorter.sort(array);

        for (int i = 0; i < size - 1; i ++) {
            if (array[i].getKey() == array[i + 1].getKey()) {
                if (array[i].getValue() >= array[i + 1].getValue()) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void testSorter(ISort sorter) {
        System.out.println(sorter + " stable: " + isStable(sorter, 100));
        System.out.println(sorter  +  " sorted: " + checkSort(sorter, 100)); // large input to check for Dr Evil

        //check already sorted array
        KeyValuePair[] ascending = new KeyValuePair[1000];
        for (int i = 0; i < 1000; i++) {
            ascending[i] = new KeyValuePair(i, 0);
        }
        long ascendingCost = sorter.sort(ascending);
        System.out.println("Best case for " + sorter + " is: " + ascendingCost);

        //check worst case
        KeyValuePair[] descending = new KeyValuePair[1000];
        for (int i = 0; i < 1000; i++) {
            descending[1000 - i - 1] = new KeyValuePair(i, 0);
        }
        long descendingCost = sorter.sort(descending);
        System.out.println("Worst case for " + sorter + " is:" + descendingCost);


        // check partially sorted array
        // to differentiate between bubble sort and insertion sort
        KeyValuePair[] partial = new KeyValuePair[1000];
        for (int i = 0; i < 500; i++) {
            partial[1000 - i - 1] = new KeyValuePair(i, 0);
            partial[i] = new KeyValuePair(i, 0);
        }
        long partialCost = sorter.sort(partial);
        System.out.println("partially sorted case for " + sorter + " is:" + partialCost);


        //check how the algorithm scales when array is 10x the size
        //to differentiate between selection sort and quick sort
        KeyValuePair[] smallArray = new KeyValuePair[100];
        KeyValuePair[] bigArray = new KeyValuePair[1000];
        for (int i = 0; i < 100; i ++) {
            smallArray[i] = new KeyValuePair(i, 0);
        }
        for (int i = 0; i < 1000; i ++) {
            bigArray[i] = new KeyValuePair(i, 0);
        }
        long smallCost = sorter.sort(smallArray);
        long bigCost = sorter.sort(bigArray);
        System.out.println("array of size 100 for " + sorter + " costs: " + smallCost);
        System.out.println("array of size 1000 for  " + sorter + " costs: " + bigCost);

        System.out.println("\n");
    }


    public static void main(String[] args) {

        //instantiate all the sorters
        ISort sorterA = new SorterA();
        ISort sorterB = new SorterB();
        ISort sorterC = new SorterC();
        ISort sorterD = new SorterD();
        ISort sorterE = new SorterE();
        ISort sorterF = new SorterF();


        //comment out all except sorter you want to test
        testSorter(sorterA); //merge sort
        testSorter(sorterB); // dr evil
        testSorter(sorterC); // bubble sort
        testSorter(sorterD); // selection sort
        testSorter(sorterE); // quick sort
        testSorter(sorterF); // insertion sort

    }
}

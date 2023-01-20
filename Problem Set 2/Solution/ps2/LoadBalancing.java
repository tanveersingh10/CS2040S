/**
 * Contains static routines for solving the problem of balancing m jobs on p processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {

    /**
     * Checks if it is possible to assign the specified jobs to the specified number of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no processor has more than queryLoad load.
     */
    public static boolean isFeasibleLoad(int[] jobSizes, int queryLoad, int p) {
        // TODO: Implement this
        if (jobSizes.length < 1) {
            return false;
        }

        int numProcessorsNeeded = 1;
        int i = 0;
        int currentLoad = 0;

        while (i < jobSizes.length) {
            if (jobSizes[i] > queryLoad) {
                return false;
            } else if (currentLoad + jobSizes[i] <= queryLoad) {
                currentLoad += jobSizes[i];
                i++;
            } else {
                numProcessorsNeeded++;
                currentLoad = 0;

                if (i == jobSizes.length -1) { //to prevent infinite loop
                    i++;
                }
            }
        }

        if (numProcessorsNeeded <= p) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of processors.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param p the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSizes, int p) {
        // using linear search
//        for (int i = 1; i < Integer.MAX_VALUE; i++) {
//            if (isFeasibleLoad(jobSizes, i, p)) {
//                return i;
//            }
//        }
//        return -1;

        // using binary search

        if (jobSizes.length == 1) {
            return jobSizes[0];
        } else if (jobSizes.length == 0) {
            return 0;
        }
        if (p < 1) {
            System.out.println("incorrect input for p");
        }

        int minimum = 1;
        while (!isFeasibleLoad(jobSizes, minimum, p)) {
            minimum = minimum * 2;
        }

        int end = minimum;
        int begin = minimum/2;
        int mid = (end + begin)/2;

        while (begin < end) {

            if (isFeasibleLoad(jobSizes, mid, p)) {
                end = mid;
                mid = (end + begin)/2;

            } else {
               begin = mid + 1;
               mid = (end + begin)/2;
            }
        }
        return begin;


    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, 100, 80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {7}
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}



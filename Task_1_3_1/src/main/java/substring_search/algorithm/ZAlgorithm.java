package substring_search.algorithm;

public class ZAlgorithm {
    private ZAlgorithm() {}

    public static int[] zFunction(String str) {
        int n = str.length();
        if (n == 0) {
            return new int[0];
        }

        int[] z = new int[n];
        z[0] = n;

        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i <= r) {
                z[i] = Math.min(z[i-l], r-i+1);
            }

            while (i + z[i] < n && str.charAt(z[i]) == str.charAt(i + z[i])) {
                z[i]++;
            }

            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }

        return z;
    }
}

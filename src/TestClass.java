
import java.util.ArrayList;

public class TestClass {
    public static void main(String[] args) {
        System.out.println(digitCounts(0, 9));
    }

    public static int digitCounts(int k, int n) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i <= n; i++) {
            int num = i;
            while (num != 0) {
                list.add(num % 10);
                num /= 10;
            }
        }
        int count = 0;
        for (int i : list) {
            if (i == k) {
                count++;
            }
        }
        return count;
    }
}

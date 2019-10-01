package IF;

import java.util.Scanner;

public class ChooseSecond {

    // A, B, C
    // A < B = A
    // A < C = A
    // B < C = B
    // A < B = B



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String num;
        String[] arr;
        int num1, num2, num3, big, small, result = 0;

        num = scanner.nextLine();
        arr = num.split(" ");

        num1 = Integer.parseInt(arr[0]);
        num2 = Integer.parseInt(arr[1]);
        num3 = Integer.parseInt(arr[2]);

        if(num1 < num2) {
            small = num1;
            big = num2;
        } else {
            small = num2;
            big = num1;
        }

        if(small < num3 && big > num3) {
            result = num3;
        } else if(big <= num3) {
            result = big;
        } else if(small >= num3) {
            result = small;
        } else {
            result = small;
        }

        System.out.println(result);
    }

}

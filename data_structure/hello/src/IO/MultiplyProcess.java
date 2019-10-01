package IO;

import java.util.Scanner;

public class MultiplyProcess {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num1, digit = 10, result = 0;
        String num2;
        String[] arr;

        num1 = scanner.nextInt();
        num2 = scanner.next();
        arr = num2.split("");

        for(int i=arr.length; i>0; i--) {
            int value = (num1*Integer.parseInt(arr[i-1]));
            System.out.println(value);
            result += value * (int) Math.pow((double) digit, (double) (arr.length - i));// Math.pow 지수 계산, double이므로 int형으로 변경 필요
        }
        System.out.println(result);
    }
}

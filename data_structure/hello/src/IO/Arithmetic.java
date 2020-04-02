package IO;

import java.util.Scanner;

public class Arithmetic {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] arr = {"+", "-", "*", "/", "%"};
        int num1;
        int num2;

        num1 = scanner.nextInt();
        num2 = scanner.nextInt();

        for(int i=0; i<arr.length; i++) {
            if("+".equals(arr[i])) {
                System.out.println(num1 + num2);
            } else if("-".equals(arr[i])){
                System.out.println(num1 - num2);
            } else if("*".equals(arr[i])){
                System.out.println(num1 * num2);
            } else if("/".equals(arr[i])){
                System.out.println(num1 / num2);
            } else if("%".equals(arr[i])){
                System.out.println(num1 % num2);
            }
        }
    }
}

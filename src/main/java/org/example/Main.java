package org.example;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String abc = "aa     bb";
        String[] arr = abc.split("\\s+");

        for(int i=0; i<arr.length; i++){
            System.out.println(i + " :"+arr[i]);
        }
    }
}
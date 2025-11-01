package org.example;

import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        List<String> strlist = new ArrayList<>();
        strlist.add("int1");
        strlist.add("int1");
        strlist.add("int2");

        System.out.println(strlist.equals(List.of("int1", "int2", "int1")));
    }
}
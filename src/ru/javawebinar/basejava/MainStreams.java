package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainStreams {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3, 4, 4, 7, 8}));
        System.out.println(oddOrEven(new ArrayList<>(Arrays.asList(1, 2, 3, 3, 2, 3, 4, 8, 1))));
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values).sorted().distinct().reduce(0, ((left, right) -> left * 10 + right));
    }


    private static List<Integer> oddOrEven(List<Integer> integers) {
        boolean isEven = integers.stream().mapToInt(p -> p).sum() % 2 == 0;
        return integers.stream().collect(Collectors.partitioningBy((p) -> p % 2 == 0)).get(isEven);
    }
}

package com.mnm.sense;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ColorGenerator
{
    private static int[] materialColors = SenseApp.context().getResources().getIntArray(R.array.colors);

    public static int[] generateRandom(int n)
    {
        int i = 0;
        Set<Integer> visited = new HashSet<>();
        Random random = new Random();
        int[] colors = new int[n];

        while (i < n)
        {
            int colorIndex = random.nextInt(materialColors.length);

            if (visited.contains(colorIndex))
                continue;

            visited.add(colorIndex);
            colors[i++] = materialColors[colorIndex];
        }

        return colors;
    }
}

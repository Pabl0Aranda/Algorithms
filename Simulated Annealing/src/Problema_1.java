/*
Problema 1 (3 horas). Se desea fabricar un material aislante compuesto por siete materiales distintos.
Se desea encontrar cuál es el orden en que deben mezclarse dichos materiales para asegurar que sea lo más
aislante posible. Suponga la siguiente situación:
Se puede apreciar un vector en el que aparece el orden en el que se combinan los materiales y, además,
una matriz triangular para identificar posibles permutaciones de orden, es decir, posibles soluciones a
las que ir.
Así, la posición (4,5) estaría haciendo referencia a que se intercambie el orden de los materiales 4 por el 5.
Para evaluar la calidad aislante de la solución, suponga que ésta se mide por la suma de los tres
primeros componentes, esto es, sobre la figura de arriba sería 4+2+7 = 13.
Si realizamos la permutación 4 por 5, entonces, la nueva solución candidata sería [5, 2, 7, 1, 4, 6, 3],
siendo su coste 5+2+7 = 14 (>13) y por tanto se aceptaría. De esta condición se deduce que el máximo se
alcanzará cuando tengamos en las tres posiciones superiores {5, 6, 7}, en cualquier orden posible o,
en otras palabras, que el máximo global sería 5+6+7 = 18.
*/

import java.util.Random;
import java.util.Arrays;

public class Problema_1 {
    // Initial and final temperatures
    static final double T_INITIAL = 1000;
    static final double T_FINAL = 1;
    static final double COOLING_RATE = 0.9;
    static final int NUM_MATERIALS = 7;

    public static void main(String[] args)
    {
        Random random = new Random();
        int[] currentSolution = generateInitialSolution();
        int[] bestSolution = Arrays.copyOf(currentSolution, NUM_MATERIALS);
        double temperature = T_INITIAL;

        while (temperature > T_FINAL)
        {
            int[] newSolution = generateNeighbor(currentSolution);
            int delta = evaluate(newSolution) - evaluate(currentSolution);

            if (delta > 0 || Math.exp(delta / temperature) > random.nextDouble())
            {
                currentSolution = newSolution;
                if (evaluate(currentSolution) > evaluate(bestSolution))
                {
                    bestSolution = Arrays.copyOf(currentSolution, NUM_MATERIALS);
                }
            }
            temperature *= COOLING_RATE;
        }

        System.out.println("Best solution found: " + Arrays.toString(bestSolution));
        System.out.println("Best insulation quality: " + evaluate(bestSolution));
    }

    // Generate an initial random permutation of the materials
    private static int[] generateInitialSolution()
    {
        int[] materials = {1, 2, 3, 4, 5, 6, 7};
        shuffleArray(materials);
        return materials;
    }

    // Generate a neighbor solution by swapping two materials
    private static int[] generateNeighbor(int[] solution)
    {
        Random random = new Random();
        int[] newSolution = Arrays.copyOf(solution, NUM_MATERIALS);
        int i = random.nextInt(NUM_MATERIALS);
        int j = random.nextInt(NUM_MATERIALS);

        // Swap two elements
        int temp = newSolution[i];
        newSolution[i] = newSolution[j];
        newSolution[j] = temp;

        return newSolution;
    }

    // Evaluate the quality of the solution based on the sum of the first three elements
    private static int evaluate(int[] solution)
    {
        return solution[0] + solution[1] + solution[2];
    }

    // Shuffle array using Fisher-Yates algorithm
    private static void shuffleArray(int[] array)
    {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            int index = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }
}

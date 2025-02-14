/*
Ejercicio 3 (15 minutos). Modifique el ejercicio anterior para que, además de considerar el peso máximo
de la mochila, considere un beneficio asociado a cada elemento insertado. Esto es, defina un vector
de beneficios adicional, B = {b1, b2, …, b100}, con bi = [1,100], tal que cada elemento pi tenga asociado
un beneficio bi.
El objetivo ahora será maximizar el beneficio de los elementos insertados en la mochila,
sin haber exceder el peso máximo permitido.
*/

import java.util.Random;

public class Ejercicio_3 {
    // Problem constants
    static final int NUM_ITEMS = 100;
    static final int MAX_WEIGHT = 180;
    static final int T_INITIAL = 200;
    static final int T_FINAL = 5;
    static final double COOLING_RATE = 0.9;

    // Generate random weights and benefits for the items
    static int[] weights = new int[NUM_ITEMS];
    static int[] benefits = new int[NUM_ITEMS];
    static {
        Random random = new Random();
        for (int i = 0; i < NUM_ITEMS; i++) {
            weights[i] = 1 + random.nextInt(100);
            benefits[i] = 1 + random.nextInt(100);
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        boolean[] currentSolution = generateRandomSolution(random);
        boolean[] bestSolution = currentSolution.clone();
        double temperature = T_INITIAL;

        while (temperature > T_FINAL) {
            boolean[] newSolution = generateNeighbor(currentSolution, random);
            int delta = evaluate(newSolution) - evaluate(currentSolution);

            if (delta > 0 || Math.exp(delta / temperature) > random.nextDouble()) {
                currentSolution = newSolution;
                if (evaluate(currentSolution) > evaluate(bestSolution)) {
                    bestSolution = currentSolution.clone();
                }
            }

            temperature *= COOLING_RATE;
        }

        System.out.println("Best solution found: Benefit = " + evaluate(bestSolution));
    }

    // Generate a random valid solution
    private static boolean[] generateRandomSolution(Random random) {
        boolean[] solution = new boolean[NUM_ITEMS];
        int totalWeight = 0;
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (random.nextBoolean() && totalWeight + weights[i] <= MAX_WEIGHT) {
                solution[i] = true;
                totalWeight += weights[i];
            }
        }
        return solution;
    }

    // Generate a neighbor by flipping a random bit
    private static boolean[] generateNeighbor(boolean[] solution, Random random) {
        boolean[] neighbor = solution.clone();
        int index = random.nextInt(NUM_ITEMS);
        neighbor[index] = !neighbor[index];
        if (getWeight(neighbor) > MAX_WEIGHT) {
            neighbor[index] = !neighbor[index]; // Revert change if it exceeds capacity
        }
        return neighbor;
    }

    // Evaluate the solution based on total benefit
    private static int evaluate(boolean[] solution) {
        int benefit = 0;
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (solution[i]) {
                benefit += benefits[i];
            }
        }
        return benefit;
    }

    // Get the total weight of the solution
    private static int getWeight(boolean[] solution) {
        int weight = 0;
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (solution[i]) {
                weight += weights[i];
            }
        }
        return weight;
    }
}

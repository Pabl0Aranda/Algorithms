/*
Ejercicio 2 (45 minutos). Queremos resolver el problema de la mochila utilizando SA.
Para ello, suponga que la capacidad de la mochila es MAX_PESO = 180 y que puede insertar
hasta 100 elementos, con pesos aleatorios comprendidos entre [1, 100], esto es,
considere el siguiente vector P = {p1, p2, …, p100}, con pi = [1,100].
Se permite que dos elementos pesen lo mismo.

¿Qué codificación escogería para modelar el problema?
R: Un vector binario de tamaño 100, donde cada bit indica si un objeto está (1) o no está (0) en la mochila.

¿Qué temperatura inicial pondría?
R: La temperatura inicial debe ser lo suficientemente alta como para permitir la exploración de
diferentes combinaciones. Dado que los pesos están entre 1 y 100, con una capacidad de 180, establecemos:
T_INICIAL = 200

¿Cuántas combinaciones reales existen?
R: Cada objeto puede estar o no estar en la mochila, por lo que hay 2^100 combinaciones posibles.


Considere T_INICIAL = 200 y T_FINAL = 5.
*/

import java.util.Random;

public class Ejercicio_2 {
    // Problem constants
    static final int NUM_ITEMS = 100;
    static final int MAX_WEIGHT = 180;
    static final int T_INITIAL = 200;
    static final int T_FINAL = 5;
    static final double COOLING_RATE = 0.9;

    // Generate random weights for the items
    static int[] weights = new int[NUM_ITEMS];
    static {
        Random random = new Random();
        for (int i = 0; i < NUM_ITEMS; i++) {
            weights[i] = 1 + random.nextInt(100);
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

        System.out.println("Best solution found: Value = " + evaluate(bestSolution));
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

    // Evaluate the solution based on total value
    private static int evaluate(boolean[] solution) {
        int value = 0;
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (solution[i]) {
                value += weights[i];
            }
        }
        return value;
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

/*
Problema 3 (2 horas). Un modelo de coche se configura a partir de N componentes distintos.
Cada uno de esos componentes puede tomar M posibles valores, siendo vij el valor que
toma el componente i (i=1, …, N) para la posibilidad j (j=1, …, M) La afinidad de los consumidores
para cada posible valor vij es aij y el coste cij. Se entiende que el coste es el valor por la afinidad,
esto es, cij = vij * aij. Se desea encontrar una combinación de componentes que alcance la máxima
afinidad global con los gustos de los consumidores y cuyo coste no supere un umbral MAX_COSTE.

Para poder comprobar que la metaheurística diseñada funciona, suponga que N = M = 4.
Los valores aij y vij estarán comprendidos entre [1, 10] y, por tanto, cij entre [1, 100].
Por último, MAX_COSTE < 120.
*/

import java.util.Random;

public class Problema_3 {
    static final int N = 4; // Number of components
    static final int M = 4; // Number of possible values per component
    static final int MAX_COST = 120;
    static final double T_INITIAL = 100;
    static final double T_FINAL = 1;
    static final double COOLING_RATE = 0.9;

    static int[][] values = new int[N][M];
    static int[][] affinities = new int[N][M];

    public static void main(String[] args) {
        Random random = new Random();

        // Generate random values and affinities
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                values[i][j] = random.nextInt(10) + 1;
                affinities[i][j] = random.nextInt(10) + 1;
            }
        }

        // Initial random solution
        int[] currentSolution = generateRandomSolution();
        int[] bestSolution = currentSolution.clone();
        double temperature = T_INITIAL;

        while (temperature > T_FINAL) {
            int[] newSolution = generateNeighbor(currentSolution);
            int delta = evaluate(newSolution) - evaluate(currentSolution);
            int costNew = calculateCost(newSolution);
            int costCurrent = calculateCost(currentSolution);

            // Accept new solution if it's better and within the cost limit
            if ((delta > 0 && costNew <= MAX_COST) || (costNew <= MAX_COST && Math.exp(delta / temperature) > random.nextDouble())) {
                currentSolution = newSolution;
                if (evaluate(currentSolution) > evaluate(bestSolution)) {
                    bestSolution = currentSolution.clone();
                }
            }

            // Cool down the temperature
            temperature *= COOLING_RATE;
        }

        // Output the best solution
        System.out.println("Best configuration: ");
        for (int i = 0; i < N; i++) {
            System.out.println("Component " + i + " -> Option " + bestSolution[i]);
        }
        System.out.println("Maximum Affinity: " + evaluate(bestSolution));
        System.out.println("Total Cost: " + calculateCost(bestSolution));
    }

    // Generate a random initial solution
    private static int[] generateRandomSolution() {
        Random random = new Random();
        int[] solution = new int[N];
        for (int i = 0; i < N; i++) {
            solution[i] = random.nextInt(M);
        }
        return solution;
    }

    // Generate a neighboring solution by changing one component's option
    private static int[] generateNeighbor(int[] solution) {
        Random random = new Random();
        int[] newSolution = solution.clone();
        int index = random.nextInt(N);
        newSolution[index] = random.nextInt(M);
        return newSolution;
    }

    // Evaluate the affinity of a given configuration
    private static int evaluate(int[] solution) {
        int totalAffinity = 0;
        for (int i = 0; i < N; i++) {
            totalAffinity += affinities[i][solution[i]];
        }
        return totalAffinity;
    }

    // Calculate the total cost of a given configuration
    private static int calculateCost(int[] solution) {
        int totalCost = 0;
        for (int i = 0; i < N; i++) {
            totalCost += values[i][solution[i]] * affinities[i][solution[i]];
        }
        return totalCost;
    }
}
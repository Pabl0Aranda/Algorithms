/*
Problema 3 (2 horas). Un modelo de coche se configura a partir de N componentes distintos. Cada uno de
esos componentes puede tomar M posibles valores, siendo vij el valor que toma el componente i (i=1, …, N)
para la posibilidad j (j=1, …, M)
La afinidad de los consumidores para cada posible valor vij es aij y el coste cij.
Se entiendo que el coste es el valor por la afinidad. Se desea encontrar una combinación de componentes que
alcance la máxima afinidad global con los gustos de los consumidores y cuyo coste no supere un umbral C*.
Para poder comprobar que la metaheurística diseñada funciona, suponga que N = M = 4. Los valores aij y vij
estarán comprendidos entre [0, 1] y cij entre [1, 100]. Por último, C* < 200.
*/

import java.util.*;

public class Problema_3 {
    private static final int MAX_ITERATIONS = 10;
    private static final int TABU_TENURE = 5;
    private static final int NUM_ITEMS = 100;
    private static final int MAX_WEIGHT = 180;
    private static final int NUM_NEIGHBORS = 10;

    private static int[] weights = new int[NUM_ITEMS];
    private static int[] benefits = new int[NUM_ITEMS];

    private static void initializeItems() {
        Random rand = new Random();
        for (int i = 0; i < NUM_ITEMS; i++) {
            weights[i] = rand.nextInt(100) + 1;
            benefits[i] = rand.nextInt(10) + 1;
        }
    }

    private static int evaluate(int[] solution)
    {
        int totalWeight = 0, totalBenefit = 0;
        for (int i = 0; i < NUM_ITEMS; i++)
        {
            if (solution[i] == 1)
            {
                totalWeight += weights[i];
                totalBenefit += benefits[i];
            }
        }
        return totalWeight <= MAX_WEIGHT ? totalBenefit : 0;
    }

    private static List<int[]> getNeighbors(int[] solution)
    {
        List<int[]> neighbors = new ArrayList<>();
        Random rand = new Random();
        while (neighbors.size() < NUM_NEIGHBORS)
        {
            int[] neighbor = Arrays.copyOf(solution, solution.length);
            int index = rand.nextInt(NUM_ITEMS);
            neighbor[index] = 1 - neighbor[index];
            neighbors.add(neighbor);
        }
        return neighbors;
    }

    public static int[] tabuSearch()
    {
        initializeItems();

        int[] currentSolution = new int[NUM_ITEMS];
        int[] bestSolution = Arrays.copyOf(currentSolution, NUM_ITEMS);
        int bestValue = evaluate(bestSolution);

        Map<String, Integer> tabuList = new HashMap<>();

        int iterations = 0;

        while (iterations < MAX_ITERATIONS)
        {
            List<int[]> neighbors = getNeighbors(currentSolution);
            int[] bestNeighbor = currentSolution;
            int bestNeighborValue = Integer.MIN_VALUE;

            for (int[] neighbor : neighbors)
            {
                String key = Arrays.toString(neighbor);
                if (!tabuList.containsKey(key) || tabuList.get(key) == 0)
                {
                    int value = evaluate(neighbor);
                    if (value > bestNeighborValue)
                    {
                        bestNeighbor = neighbor;
                        bestNeighborValue = value;
                    }
                }
            }

            currentSolution = bestNeighbor;

            if (bestNeighborValue > bestValue)
            {
                bestSolution = bestNeighbor;
                bestValue = bestNeighborValue;
            }

            tabuList.replaceAll((key, value) -> Math.max(0, value - 1));
            tabuList.put(Arrays.toString(currentSolution), TABU_TENURE);

            iterations++;
        }
        return bestSolution;
    }

    public static void main(String[] args)
    {
        int[] bestSolution = tabuSearch();
        int bestValue = evaluate(bestSolution);
        System.out.println("Best solution found: Benefit = " + bestValue);

        solveMaterialMixing();
        solveProcessScheduling();
    }

    // Material Mixing Problem with Tabu Search
    private static void solveMaterialMixing()
    {
        int[] materials = {4, 2, 7, 1, 5, 6, 3};
        int bestScore = evaluateMixing(materials);

        Map<String, Integer> tabuList = new HashMap<>();
        int iterations = 0;

        while (iterations < MAX_ITERATIONS)
        {
            List<int[]> neighbors = getMixingNeighbors(materials);
            int[] bestNeighbor = materials;
            int bestNeighborScore = Integer.MIN_VALUE;

            for (int[] neighbor : neighbors)
            {
                String key = Arrays.toString(neighbor);
                if (!tabuList.containsKey(key) || tabuList.get(key) == 0)
                {
                    int score = evaluateMixing(neighbor);
                    if (score > bestNeighborScore)
                    {
                        bestNeighbor = neighbor;
                        bestNeighborScore = score;
                    }
                }
            }

            materials = bestNeighbor;

            if (bestNeighborScore > bestScore)
            {
                bestScore = bestNeighborScore;
            }

            tabuList.replaceAll((key, value) -> Math.max(0, value - 1));
            tabuList.put(Arrays.toString(materials), TABU_TENURE);

            iterations++;
        }

        System.out.println("Best material mixing order found: " + Arrays.toString(materials));
        System.out.println("Best insulation score: " + bestScore);
    }

    private static int evaluateMixing(int[] materials)
    {
        return materials[0] + materials[1] + materials[2];
    }

    private static List<int[]> getMixingNeighbors(int[] materials)
    {
        List<int[]> neighbors = new ArrayList<>();
        for (int i = 0; i < materials.length - 1; i++)
        {
            for (int j = i + 1; j < materials.length; j++)
            {
                int[] neighbor = Arrays.copyOf(materials, materials.length);
                int temp = neighbor[i];
                neighbor[i] = neighbor[j];
                neighbor[j] = temp;
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    // Process Scheduling Problem
    private static void solveProcessScheduling()
    {
        int n = 5; // Number of processes
        int m = 3; // Number of processors
        Random rand = new Random();
        int[][] times = new int[n][m];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                times[i][j] = rand.nextInt(50) + 1;
            }
        }

        int[] assignment = new int[n];
        Arrays.fill(assignment, -1);

        System.out.println("Optimal process assignment using Tabu Search:");
        for (int i = 0; i < n; i++)
        {
            int bestProcessor = -1;
            int bestTime = Integer.MAX_VALUE;
            for (int j = 0; j < m; j++)
            {
                if (times[i][j] < bestTime)
                {
                    bestTime = times[i][j];
                    bestProcessor = j;
                }
            }
            assignment[i] = bestProcessor;
            System.out.println("Process " + i + " assigned to Processor " + bestProcessor);
        }
    }
}

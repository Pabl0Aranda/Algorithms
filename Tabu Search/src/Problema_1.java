/*
Problema 1 (1 hora). Se desea fabricar un material aislante compuesto por siete materiales distintos.
Se desea encontrar cuál es el orden en que deben mezclarse dichos materiales para asegurar que
sea lo más aislante posible.

Suponga la siguiente situación:
Se puede apreciar un vector en el que aparece el orden en el que se combinan los materiales y, además, una
matriz triangular para identificar posibles permutaciones de orden, es decir, posibles soluciones a las que ir.

Así, la posición (4,5) estaría haciendo referencia a que se intercambie el orden de los materiales 4 por el 5.
Para evaluar la calidad aislante de la solución, suponga que ésta se mide por la suma de los tres primeros
componentes, esto es, sobre la figura de arriba sería 4+2+7 = 13.
Si realizamos la permutación 4 5, entonces, la nueva solución candidata sería [5, 2, 7, 1, 4, 6, 3],
siendo su coste 5+2+7 = 14 (>13) y por tanto se aceptaría.

De esta condición se deduce que el máximo se alcanzará cuando tengamos en las tres posiciones superiores
{5, 6, 7}, en cualquier orden posible o, en otras palabras, que el máximo global sería 5+6+7 = 18
*/

import java.util.*;

public class Problema_1
{
    private static final int MAX_ITERATIONS = 10;
    private static final int TABU_TENURE = 5;
    private static final int NUM_ITEMS = 100;
    private static final int MAX_WEIGHT = 180;
    private static final int NUM_NEIGHBORS = 10;

    private static int[] weights = new int[NUM_ITEMS];
    private static int[] benefits = new int[NUM_ITEMS];

    private static void initializeItems()
    {
        Random rand = new Random();
        for (int i = 0; i < NUM_ITEMS; i++)
        {
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
}
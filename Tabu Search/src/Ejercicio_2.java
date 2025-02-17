/*
Ejercicio 2 (45 minutos). Queremos resolver el problema de la mochila utilizando TS. Para ello, suponga que
la capacidad de la mochila es q = 180 y que puede insertar hasta 100 elementos, con pesos aleatorios
comprendidos entre [1, 100], esto es, considere el siguiente vector P = {p1, p2, …, p100}, con pi = [1,100].
Además, se dispone de un beneficio asociado a cada elemento, B = {b1, b2, …, b100}, con bi = [1,10].
Se permite que dos elementos pesen lo mismo y que tengan el mismo beneficio.
Resuelva el problema de tal modo que se maximice el beneficio y que no se incumpla la condición del peso
máximo permitido.

Suponga que:
1. La solución inicial es un vector de todo a 0 (ningún elemento incluido en la mochila).
2. La tenencia tabú es 5.
3. Se evalúan 10 vecinos en cada iteración.
4. Un vecino será la permutación de un bit escogido aleatoriamente.
5. El número de iteraciones es 10.
6. El nivel de aspiración se cumple si la solución es mejor que la mejor encontrada.

*/

import java.util.*;

public class Ejercicio_2
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
    }
}

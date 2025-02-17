/*
Ejercicio 1 (45 minutos). Queremos hallar el máximo de f(x)=x3
-60x2+900x+100 entre x=0 y x=31.
Para resolver el problema usando TS, se propone seguir la siguiente estrategia.
En primer lugar, discretizamos el rango de valores de x con vectores binarios de 5 componentes
entre 00000 y 11111.
Estos 32 vectores constituyen S las soluciones factibles del problema.
En la Tabla Auxiliar puede ver cuáles son los valores asociados a cada solución posible.
Suponga una tenencia tabú de 3 unidades de tiempo. Defina una condición de parada que considere adecuada.
*/

import java.util.*;

public class Ejercicio_1 {
    private static final int MAX_ITERATIONS = 5;
    private static final int NO_IMPROVEMENT_LIMIT = 10;
    private static final int TABU_TENURE = 3;

    private static int evaluate(int x)
    {
        return (int) (Math.pow(x, 3) - 60 * Math.pow(x, 2) + 900 * x + 100);
    }

    private static List<Integer> getNeighbors(int x)
    {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            int neighbor = x ^ (1 << i); // Flip bit at position i
            if (neighbor >= 0 && neighbor <= 31)
            {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    public static int tabuSearch()
    {
        Random rand = new Random();
        int currentSolution = rand.nextInt(32); // Random initial solution (0-31)
        int bestSolution = currentSolution;
        int bestValue = evaluate(bestSolution);

        Map<Integer, Integer> tabuList = new HashMap<>(); // Solution -> remaining tabu time
        int iterations = 0, noImprovementCount = 0;

        while (iterations < MAX_ITERATIONS && noImprovementCount < NO_IMPROVEMENT_LIMIT)
        {
            List<Integer> neighbors = getNeighbors(currentSolution);
            int bestNeighbor = currentSolution;
            int bestNeighborValue = Integer.MIN_VALUE;

            for (int neighbor : neighbors)
            {
                if (!tabuList.containsKey(neighbor) || tabuList.get(neighbor) == 0)
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

            // Update best solution found
            if (bestNeighborValue > bestValue)
            {
                bestSolution = bestNeighbor;
                bestValue = bestNeighborValue;
                noImprovementCount = 0;
            }
            else
            {
                noImprovementCount++;
            }

            // Update tabu list
            tabuList.replaceAll((key, value) -> Math.max(0, value - 1));
            tabuList.put(currentSolution, TABU_TENURE);

            iterations++;
        }
        return bestSolution;
    }

    public static void main(String[] args)
    {
        int bestX = tabuSearch();
        int bestValue = evaluate(bestX);
        System.out.println("Best solution found: x = " + bestX + ", f(x) = " + bestValue);
    }
}
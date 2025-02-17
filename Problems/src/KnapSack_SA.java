/*
Descripción:
Tienes una mochila con capacidad limitada y un conjunto de objetos, cada uno con un peso y un valor. Encuentra la combinación de objetos que maximice el valor total sin superar la capacidad de la mochila.

Requisitos:
Implementa Simulated Annealing (SA) y Tabu Search (TS) para resolver el problema.
Representa soluciones como vectores binarios, donde 1 indica que el objeto está en la mochila y 0 que no lo está.
Usa un operador de vecindad basado en flip de bits (agregar/quitar un objeto).

Entrada de ejemplo:

4 10  // Número de objetos, capacidad de la mochila
2 3   // Peso, Valor del objeto 1
3 4   // Peso, Valor del objeto 2
4 5   // Peso, Valor del objeto 3
5 8   // Peso, Valor del objeto 4

Salida esperada:

Mejor selección de objetos: [1, 0, 1, 0]  // Se escogieron los objetos 1 y 3
Valor total: 8
Peso total: 6
*/

import java.util.Random;

public class KnapSack_SA {
    static final double INITIAL_TEMP = 100;
    static final double FINAL_TEMP = 1;
    static final double COOLING_RATE = 0.85;  // Tasa de enfriamiento
    static final int MAX_WEIGHT = 12;
    static final int ITERATIONS = 100;
    static final int NUM_ITEMS = 6;

    static final int[][] KNAPSACK_ITEMS =
    {
            {4, 10}, // {peso, valor}
            {2, 3},
            {3, 4},
            {4, 5},
            {5, 8},
            {6, 13},
    };

    // Representación de los objetos seleccionados (1 si está en la mochila, 0 si no)
    int[] currentItems = {0, 0, 0, 0, 0, 0};

    public static void main(String[] args)
    {
        KnapSack_SA ks = new KnapSack_SA();
        ks.simulatedAnnealing();
    }

    // Función principal para Simulated Annealing
    private void simulatedAnnealing()
    {
        Random rand = new Random();

        // Inicialización de la solución actual
        int[] currentSolution = new int[NUM_ITEMS];
        for (int i = 0; i < NUM_ITEMS; i++)
        {
            currentSolution[i] = rand.nextInt(2); // Aleatoriamente seleccionamos algunos elementos
        }

        double currentTemp = INITIAL_TEMP;
        int[] bestSolution = currentSolution.clone();
        int bestValue = evaluateSolution(bestSolution);

        while (currentTemp > FINAL_TEMP)
        {
            for (int i = 0; i < ITERATIONS; i++)
            {
                // Generamos un vecino de la solución actual
                int[] neighbor = generateNeighbor(currentSolution);
                int neighborValue = evaluateSolution(neighbor);

                // Si el vecino es mejor, lo aceptamos
                if (neighborValue > evaluateSolution(currentSolution))
                {
                    currentSolution = neighbor.clone();

                    // Si la nueva solución es mejor que la mejor encontrada, actualizamos
                    if (neighborValue > bestValue)
                    {
                        bestSolution = neighbor.clone();
                        bestValue = neighborValue;
                    }
                }
                else
                {
                    // Si el vecino es peor, aceptamos con cierta probabilidad (enfriamiento)
                    double acceptanceProbability = Math.exp((evaluateSolution(neighbor) - evaluateSolution(currentSolution)) / currentTemp);
                    if (acceptanceProbability > rand.nextDouble())
                    {
                        currentSolution = neighbor.clone();
                    }
                }
            }
            // Reducir la temperatura
            currentTemp *= COOLING_RATE;
        }

        System.out.println("Mejor solución encontrada:");
        for (int i = 0; i < NUM_ITEMS; i++)
        {
            if (bestSolution[i] == 1)
            {
                System.out.println("Objeto " + (i + 1) + " incluido.");
            }
        }
        System.out.println("Valor total: " + bestValue);
    }

    // Función para evaluar la solución (valor total y peso)
    private int evaluateSolution(int[] solution)
    {
        int totalValue = 0;
        int totalWeight = 0;

        for (int i = 0; i < NUM_ITEMS; i++)
        {
            if (solution[i] == 1)
            {
                totalValue += KNAPSACK_ITEMS[i][1];
                totalWeight += KNAPSACK_ITEMS[i][0];
            }
        }

        // Si el peso total excede la capacidad máxima, penalizamos la solución
        if (totalWeight > MAX_WEIGHT)
        {
            return -1; // Penalización: solución inválida
        }

        return totalValue;
    }

    // Función para generar un vecino aleatorio (cambiar un elemento de la mochila)
    private int[] generateNeighbor(int[] currentSolution)
    {
        Random rand = new Random();
        int[] neighborSolution = currentSolution.clone();

        // Elegimos un índice aleatorio para cambiar el estado de ese objeto (0 -> 1 o 1 -> 0)
        int index = rand.nextInt(NUM_ITEMS);
        neighborSolution[index] = 1 - neighborSolution[index];

        return neighborSolution;
    }
}


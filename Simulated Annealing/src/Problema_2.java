/*
Problema 2 (3 horas). Se dispone de un conjunto de n procesos y un ordenador con m procesadores
(de características no necesariamente iguales). Se conoce el tiempo que requiere el procesador j-ésimo
para realizar el proceso i-ésimo, tij.
Se desea encontrar un reparto de procesos entre los m procesadores tal que el tiempo de finalización
sea lo más corto posible. Tome tantas decisiones como estime  conveniente e intente comparar distintas
soluciones con distintas configuraciones iniciales.
*/

import java.util.Random;
import java.util.Arrays;

public class Problema_2
{
    static final int T_INITIAL = 1000;
    static final int T_FINAL = 1;
    static final double COOLING_RATE = 0.95;
    static final int NUM_PROCESSES = 10;
    static final int NUM_PROCESSORS = 3;
    static final int[][] PROCESS_TIMES = new int[NUM_PROCESSES][NUM_PROCESSORS];

    public static void main(String[] args)
    {
        Random random = new Random();
        generateProcessTimes(random);
        int[] currentAssignment = generateInitialSolution(random);
        int[] bestAssignment = Arrays.copyOf(currentAssignment, currentAssignment.length);
        int bestCost = evaluate(bestAssignment);
        double temperature = T_INITIAL;

        while (temperature > T_FINAL)
        {
            int[] newAssignment = generateNeighbor(currentAssignment, random);
            int newCost = evaluate(newAssignment);
            int delta = newCost - evaluate(currentAssignment);

            if (delta < 0 || Math.exp(-delta / temperature) > random.nextDouble())
            {
                currentAssignment = newAssignment;
                if (newCost < bestCost)
                {
                    bestAssignment = Arrays.copyOf(newAssignment, newAssignment.length);
                    bestCost = newCost;
                }
            }

            temperature *= COOLING_RATE;
        }

        System.out.println("Best assignment found: " + Arrays.toString(bestAssignment));
        System.out.println("Minimum completion time: " + bestCost);
    }

    private static void generateProcessTimes(Random random)
    {
        for (int i = 0; i < NUM_PROCESSES; i++)
        {
            for (int j = 0; j < NUM_PROCESSORS; j++)
            {
                PROCESS_TIMES[i][j] = random.nextInt(100) + 1;
            }
        }
    }

    private static int[] generateInitialSolution(Random random)
    {
        int[] assignment = new int[NUM_PROCESSES];
        for (int i = 0; i < NUM_PROCESSES; i++)
        {
            assignment[i] = random.nextInt(NUM_PROCESSORS);
        }
        return assignment;
    }

    private static int[] generateNeighbor(int[] assignment, Random random)
    {
        int[] newAssignment = Arrays.copyOf(assignment, assignment.length);
        int process = random.nextInt(NUM_PROCESSES);
        newAssignment[process] = random.nextInt(NUM_PROCESSORS);
        return newAssignment;
    }

    private static int evaluate(int[] assignment)
    {
        int[] processorLoad = new int[NUM_PROCESSORS];
        for (int i = 0; i < NUM_PROCESSES; i++)
        {
            processorLoad[assignment[i]] += PROCESS_TIMES[i][assignment[i]];
        }
        return Arrays.stream(processorLoad).max().getAsInt();
    }
}

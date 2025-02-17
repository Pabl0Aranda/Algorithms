/*
Problema del TSP: Rutas por España
Un viajante debe visitar 10 ciudades en España y regresar a la ciudad de origen minimizando la
distancia total recorrida. Las ciudades y sus coordenadas aproximadas (en kilómetros) son:

Ciudad	       | X (km)	 |   Y (km) |
------------------------------------|
Madrid	       |  500	 |   500    |
Barcelona	   |  850	 |   600    |
Valencia	   |  700	 |   550    |
Sevilla	       |  300	 |   400    |
Zaragoza	   |  750	 |   650    |
Málaga	       |  320	 |   350    |
Murcia	       |  600 	 |   500    |
Bilbao	       |  600 	 |   800    |
Valladolid	   |  480	 |   700    |
Santander	   |  550	 |   750    |

Objetivo:
Encontrar la ruta más corta que pase por todas las ciudades exactamente una vez y vuelva a la ciudad
de origen (ciclo Hamiltoniano).
Implementar Simulated Annealing (SA) y Tabu Search (TS) para resolverlo.
*/

import java.util.*;

public class Spain_TSP
{
    static final int NUM_CIUDADES = 10;
    static final double TEMP_INICIAL = 100;
    static final double ENFRIAMIENTO = 0.9;
    static final int ITERACIONES = 200;
    static final int TABU_SIZE = 5;
    static final int MAX_ITERACIONES_TABU = 150;

    // Coordenadas aproximadas de ciudades en España
    static final double[][] COORDENADAS =
    {
            {500, 500}, {850, 600}, {700, 550}, {300, 400}, {750, 650},
            {320, 350}, {600, 500}, {600, 800}, {480, 700}, {550, 750}
    };

    public static void main(String[] args)
    {
        double[][] distancias = calcularMatrizDistancias(COORDENADAS);

        int[] mejorRutaSA = simulatedAnnealing(distancias);
        System.out.println("Mejor ruta encontrada con Simulated Annealing:");
        System.out.println(Arrays.toString(mejorRutaSA));
        System.out.println("Distancia total: " + calcularDistanciaTotal(mejorRutaSA, distancias));

        int[] mejorRutaTS = tabuSearch(distancias);
        System.out.println("Mejor ruta encontrada con Tabu Search:");
        System.out.println(Arrays.toString(mejorRutaTS));
        System.out.println("Distancia total: " + calcularDistanciaTotal(mejorRutaTS, distancias));
    }

    private static double calcularDistancia(double[] ciudad1, double[] ciudad2)
    {
        return Math.sqrt(Math.pow(ciudad1[0] - ciudad2[0], 2) + Math.pow(ciudad1[1] - ciudad2[1], 2));
    }

    private static double[][] calcularMatrizDistancias(double[][] coordenadas)
    {
        double[][] distancias = new double[NUM_CIUDADES][NUM_CIUDADES];
        for (int i = 0; i < NUM_CIUDADES; i++)
        {
            for (int j = 0; j < NUM_CIUDADES; j++)
            {
                distancias[i][j] = calcularDistancia(coordenadas[i], coordenadas[j]);
            }
        }
        return distancias;
    }

    private static int[] simulatedAnnealing(double[][] distancias)
    {
        Random rand = new Random();
        int[] rutaActual = generarRutaInicial();
        int[] mejorRuta = rutaActual.clone();
        double mejorDistancia = calcularDistanciaTotal(mejorRuta, distancias);
        double temperatura = TEMP_INICIAL;

        for (int iter = 0; iter < ITERACIONES; iter++)
        {
            int[] nuevaRuta = generarVecino(rutaActual);
            double nuevaDistancia = calcularDistanciaTotal(nuevaRuta, distancias);
            double delta = nuevaDistancia - mejorDistancia;

            if (delta < 0 || Math.exp(-delta / temperatura) > rand.nextDouble())
            {
                rutaActual = nuevaRuta;
                if (nuevaDistancia < mejorDistancia)
                {
                    mejorRuta = nuevaRuta.clone();
                    mejorDistancia = nuevaDistancia;
                }
            }

            temperatura *= ENFRIAMIENTO;
        }
        return mejorRuta;
    }

    private static int[] tabuSearch(double[][] distancias)
    {
        int[] mejorRuta = generarRutaInicial();
        double mejorDistancia = calcularDistanciaTotal(mejorRuta, distancias);
        LinkedList<int[]> listaTabu = new LinkedList<>();

        for (int iter = 0; iter < MAX_ITERACIONES_TABU; iter++)
        {
            List<int[]> vecinos = generarVecinos(mejorRuta);
            int[] mejorVecino = null;
            double mejorVecinoDistancia = Double.MAX_VALUE;

            for (int[] vecino : vecinos)
            {
                double distanciaVecino = calcularDistanciaTotal(vecino, distancias);
                if (!listaTabu.contains(vecino) && distanciaVecino < mejorVecinoDistancia)
                {
                    mejorVecino = vecino;
                    mejorVecinoDistancia = distanciaVecino;
                }
            }

            if (mejorVecino != null)
            {
                mejorRuta = mejorVecino;
                mejorDistancia = mejorVecinoDistancia;
                listaTabu.add(mejorVecino.clone());
                if (listaTabu.size() > TABU_SIZE)
                {
                    listaTabu.removeFirst();
                }
            }
        }
        return mejorRuta;
    }

    private static int[] generarRutaInicial()
    {
        int[] ruta = new int[NUM_CIUDADES];
        for (int i = 0; i < NUM_CIUDADES; i++) ruta[i] = i;
        Collections.shuffle(Arrays.asList(ruta));
        return ruta;
    }

    private static int[] generarVecino(int[] ruta)
    {
        Random rand = new Random();
        int[] nuevaRuta = ruta.clone();
        int i = rand.nextInt(NUM_CIUDADES);
        int j = rand.nextInt(NUM_CIUDADES);
        while (i == j) j = rand.nextInt(NUM_CIUDADES);

        int temp = nuevaRuta[i];
        nuevaRuta[i] = nuevaRuta[j];
        nuevaRuta[j] = temp;

        return nuevaRuta;
    }

    private static List<int[]> generarVecinos(int[] ruta)
    {
        List<int[]> vecinos = new ArrayList<>();
        for (int i = 0; i < NUM_CIUDADES - 1; i++)
        {
            for (int j = i + 1; j < NUM_CIUDADES; j++)
            {
                int[] nuevoVecino = ruta.clone();
                int temp = nuevoVecino[i];
                nuevoVecino[i] = nuevoVecino[j];
                nuevoVecino[j] = temp;
                vecinos.add(nuevoVecino);
            }
        }
        return vecinos;
    }

    private static double calcularDistanciaTotal(int[] ruta, double[][] distancias)
    {
        double distanciaTotal = 0;
        for (int i = 0; i < NUM_CIUDADES - 1; i++)
        {
            distanciaTotal += distancias[ruta[i]][ruta[i + 1]];
        }
        distanciaTotal += distancias[ruta[NUM_CIUDADES - 1]][ruta[0]];
        return distanciaTotal;
    }
}

/*
Ejercicio 1 (45 minutos). Queremos hallar el máximo de f(x)=x3-60x2+900x+100 entre x=0 y x=31.
Para resolver el problema usando SA, se propone seguir la siguiente estrategia.
En primer lugar, discretizamos el rango de valores de x con vectores binarios de 5 componentes entre
00000 y 11111. Estos 32 vectores constituyen S las soluciones factibles del problema.
Le damos un valor inicial a T intuitivamente, por ejemplo, T0 =100 o 500 y en cada iteración del algoritmo
lo reduciremos en 10%, es decir utilizando la estrategia de descenso geométrico: Tk = 0.9 Tk-1.

Cada iteración consiste en lo siguiente:

1. El número de vecinos queda fijado a 5, siendo éstos variaciones de un bit de la solución.
Por ejemplo, si partimos de 00011, los 5 posibles vecinos resultantes serían:
10011, 01011, 00111, 00001, 00010.

2. Para aplicar el criterio de aceptación, escogemos un vecino, buscamos su coste asociado en la
Tabla 1 que se proporciona y calculamos la diferencia con la solución actual.
Si está más cerca del óptimo se acepta, si no, se aplica Pa (δ, T) = e-δ/T.
Siendo T la temperatura actual y δ la diferencia de costes entre la solución candidata y la actual.

3. Concluya la búsqueda cuando el proceso se enfríe o cuando no se acepte ninguna solución de su vecindad.

Considere T_INICIAL = 100 y T_FI NAL = 1.
*/
import java.util.Random;

public class Ejercicio_1 {
    // Initial temperature
    static final int T_INITIAL = 100;
    // Final temperature
    static final int T_FINAL = 1;
    // Cooling rate (geometric cooling strategy)
    static final double COOLING_RATE = 0.9;

    public static void main(String[] args) {
        Random random = new Random();
        // Generate a random initial solution between 0 and 31
        int currentSolution = random.nextInt(32);
        int bestSolution = currentSolution;
        double temperature = T_INITIAL;

        // Simulated Annealing process
        while (temperature > T_FINAL) {
            // Generate a neighboring solution
            int newSolution = generateNeighbor(currentSolution);
            // Calculate the difference in function values
            int delta = evaluate(newSolution) - evaluate(currentSolution);

            // Accept the new solution if it's better or based on probability
            if (delta > 0 || Math.exp(delta / temperature) > random.nextDouble()) {
                currentSolution = newSolution;
                // Update the best solution found so far
                if (evaluate(currentSolution) > evaluate(bestSolution)) {
                    bestSolution = currentSolution;
                }
            }

            // Reduce the temperature
            temperature *= COOLING_RATE;
        }

        // Print the best solution found
        System.out.println("Best solution found: x = " + bestSolution + ", f(x) = " + evaluate(bestSolution));
    }

    // Generate a neighboring solution by flipping one random bit
    private static int generateNeighbor(int x) {
        Random random = new Random();
        int bitToFlip = 1 << random.nextInt(5); // Select a bit to flip
        return x ^ bitToFlip; // Flip the bit
    }

    // Evaluate the function f(x) = x^3 - 60x^2 + 900x + 100
    private static int evaluate(int x) {
        return x * x * x - 60 * x * x + 900 * x + 100;
    }
}

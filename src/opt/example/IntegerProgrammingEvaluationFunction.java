package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;

/**
 * An Integer Programming function
 * @author yzhuang44@gatech.edu
 * @version 1.0
 */

public class IntegerProgrammingEvaluationFunction implements EvaluationFunction {

    private int[] c;
    private int[][] A;
    private int[] b;


    /**
     * Standard Integer Programming form:
     *  Max c^T x
     *  Subject to A x less or equal to b
     *  x greater than or equal 0
     * @param c array of integers
     * @param A matrix of integers
     * @param b array of integers
     */
    public IntegerProgrammingEvaluationFunction(int[] c, int[][] A, int[] b) {
        this.c = c;
        this.A = A;
        this.b = b;
    }

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData) 
     */
    public double value(Instance d) {
        Vector data = d.getData();
        double smallNumber = 1E-12;

        // first check to make sure x > 0
        for (int i = 0; i < data.size(); i++) {
            if (((int)data.get(i)) < 0) {
                return smallNumber;
            }
        }

        // Now check to make sure all constraints are satisfied
        for (int i = 0; i < A.length; i++) {
            int constraint_sum = 0;
            for (int j = 0; j < data.size(); j++) {
                constraint_sum += A[i][j] * ((int)data.get(j));
            }
            if (constraint_sum > b[i]) {
                return smallNumber;
            }
        }

        int result = 0;
        for (int i = 0; i < data.size(); i++) {
            result += c[i] * ((int)data.get(i));
        }
        return result;
    }
}


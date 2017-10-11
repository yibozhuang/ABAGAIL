package func.nn.activation;

public class RELU extends DifferentiableActivationFunction {

    /**
     * @see nn.function.DifferentiableActivationFunction#derivative(double)
     */
    public double derivative (double value) {
        if (value < 0) {
            return 0;
        }
        else {
            return 1;
        }
    }

    /**
     * @see nn.function.ActivationFunction#activation(double)
     */
    public double value (double value) {
        if (value < 0) {
            return 0;
        }
        else {
            return value;
        }
    }

}


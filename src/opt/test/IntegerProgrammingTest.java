package opt.test;

import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

import opt.example.IntegerProgrammingEvaluationFunction;


/**
 * A test of the integer programming problem
 *
 * Generic equation of maximize c^T x
 * Subject to A x less than or equal to b
 * Where x is greater than or equal to 0
 *
 * https://en.wikipedia.org/wiki/Integer_programming
 *
 * @author yzhuang44@gatech.edu
 * @version 1.0
 */

public class IntegerProgrammingTest {
    /* Random number generator */
    private static final Random random = new Random();

    /* Number of elements in X */
    private static final int NUM_VARS = 20;

    /* Number of constraints */
    private static final int NUM_CONSTRAINTS = 50;


    /**
     * Main test code
     */
    public static void main(String[] args) {
        int c[] = new int[NUM_VARS];
        int A[][] = new int[NUM_CONSTRAINTS][NUM_VARS];
        int b[] = new int[NUM_CONSTRAINTS];

        for (int i = 0; i < NUM_VARS; i++) {
            c[i] = random.nextInt(NUM_VARS) * (random.nextBoolean() ? -1 : 1);
        }

        for (int i = 0; i < NUM_CONSTRAINTS; i++) {
            for (int j = 0; j < NUM_VARS; j++) {
                A[i][j] = random.nextInt(NUM_VARS*NUM_CONSTRAINTS) * (random.nextBoolean() ? -1 : 1);
            }
        }

        for (int i = 0; i < NUM_CONSTRAINTS; i++) {
            b[i] = random.nextInt( ((int)Math.pow(NUM_VARS, 2)) * ((int)Math.pow(NUM_CONSTRAINTS, 2)) );
        }

        int[] ranges = new int[NUM_VARS];
        Arrays.fill(ranges, NUM_VARS);
 
        EvaluationFunction ef = new IntegerProgrammingEvaluationFunction(c, A, b);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);

        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new UniformCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges);

        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);

        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 200000);
        fit.train();
        System.out.println(ef.value(rhc.getOptimal()));

        SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
        fit = new FixedIterationTrainer(sa, 200000);
        fit.train();
        System.out.println(ef.value(sa.getOptimal()));

        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 25, gap);
        fit = new FixedIterationTrainer(ga, 1000);
        fit.train();
        System.out.println(ef.value(ga.getOptimal()));

        MIMIC mimic = new MIMIC(200, 100, pop);
        fit = new FixedIterationTrainer(mimic, 1000);
        fit.train();
        System.out.println(ef.value(mimic.getOptimal()));
    }
}


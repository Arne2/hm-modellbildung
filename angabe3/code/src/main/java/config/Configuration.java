package config;

import field.use.Dijkstra;
import sun.awt.image.BufferedImageDevice;

import java.util.function.Supplier;

/**
 * Provides a configuration for the Simulation.
 *
 * @author DD00033863
 */
public class Configuration {

    /** Size of a cell. Indicates the width (and also the height) of it. */
    private final int cellSize;
    /**
     * Provides a value to use for a free flow velocity. It can be static or
     * be a new one each time it is called, based on a function.
     */
    private final Supplier<Double> velocity;

    /**
     * The used algorithm to calculate cell distances
     */
    private final AlgorithmType algorithm;
    /**
     * Create a new Configuration from a builder.
     * The Configuration has final values and cannot be changed.
     *
     * @param builder Builder used to make the Configuration.
     */
    public Configuration(Builder builder) {
        this.cellSize = builder.cellSize;
        this.velocity = builder.freeFlowVelocity;
        this.algorithm = builder.algorithm;
    }

    /**
     * Create a new Configuration with default values. Use a Builder to use
     * custom values.
     */
    public Configuration(String [] args) {
        final Builder builder = new Builder(args);
        this.cellSize = builder.cellSize;
        this.velocity = builder.freeFlowVelocity;
        this.algorithm = builder.algorithm;
    }

    /**
     * The size of the quadratic size in cm.
     * This values describes the width of the cell (and the height).
     *
     * @return the length of the edge of a cell in cm.
     */
    public int getCellSize() {
        return cellSize;
    }

    /**
     * Get a free flow velocity for a person. Depending on the configuration this value
     * could change each time this method is called. For example based on a probability distribution.
     *
     * @return the velocity in m/s
     */
    public double getVelocity() {
        return velocity.get();
    }

    public AlgorithmType getAlgorithm(){
        return this.algorithm;
    }

    public enum AlgorithmType {eDijkstra, eEuclid, eFastMarching}
    /**
     * Provides a builder for a customized Configuration.
     */
    public static class Builder {
        private int cellSize = 1;
        private AlgorithmType algorithm = AlgorithmType.eDijkstra;
        private Supplier<Double> freeFlowVelocity = () -> 1.0;

        public Builder cellSize(int cellSize) {
            this.cellSize = cellSize;
            return this;
        }

        public Builder velocity(double fixedValue) {
            this.freeFlowVelocity = () -> fixedValue;
            return this;
        }

        public Builder velocity(Supplier<Double> supplier) {
            this.freeFlowVelocity = supplier;
            return this;
        }

        public Builder(String[] args){
            parseArgs(args);
        }

        /**
         * Parse the configuration values from a command line input.
         *
         * @param args args from the command line.
         * @return this builder
         */
        public void parseArgs(final String[] args) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--cell-size")) {
                    this.cellSize(Integer.parseInt(args[i + 1]));
                }
                if (args[i].equals("--free-flow-velocity")) {
                    this.velocity(Double.parseDouble(args[i + 1]));
                }
                if (args[i].equals("--algorithm")) {
                    if(args[i + 1].equals("dijkstra")) {
                        this.algorithm = AlgorithmType.eDijkstra;
                    }
                    if(args[i + 1].equals("euclid")){
                        this.algorithm = AlgorithmType.eEuclid;
                    }
                    if(args[i + 1].equals("fast-marching")){
                        this.algorithm = AlgorithmType.eFastMarching;
                    }
                }
            }

        }

        /**
         * Finish this builder.
         *
         * @return a unmodifiable Configuration
         */
        public Configuration build() {
            return new Configuration(this);
        }
    }

}

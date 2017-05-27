package config;

import java.util.function.Supplier;

/**
 * @author DD00033863
 */
public class Configuration {

    private final int cellSize;
    private final Supplier<Double> velocity;

    public Configuration(Builder builder) {
        this.cellSize = builder.cellSize;
        this.velocity = builder.freeFlowVelocity;
    }

    public int getCellSize() {
        return cellSize;
    }

    public double getVelocity() {
        return velocity.get();
    }

    public static class Builder {
        private int cellSize = 1;
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

        public Builder parseArgs(final String[] args) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] == "--cell-size") {
                    this.cellSize(Integer.parseInt(args[i + 1]));
                }
                if (args[i] == "--free-flow-velocity") {
                    this.velocity(Double.parseDouble(args[i + 1]));
                }
            }
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }

}

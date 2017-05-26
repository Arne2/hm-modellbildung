package config;

/**
 * @author DD00033863
 */
public enum Configuration {
    INSTANCE;

	public int cellSize  = 50;
	public int fieldSizeX  = 50;
	public int fieldSizeY  = 50;
	public double freeFlowVelocity = 14.0;

    public void parseArgs(final String []args) {
        for(int i = 0; i < args.length; i++){
            if(args[i] == "--cell-size"){
                this.cellSize = Integer.parseInt(args[i+1]);
            }
            if(args[i] == "--field-size-x"){
                this.fieldSizeX = Integer.parseInt(args[i+1]);
            }

            if(args[i] == "--field-size-y"){
                this.fieldSizeY = Integer.parseInt(args[i+1]);
            }
            if(args[i] == "--free-flow-velocity"){
                this.freeFlowVelocity = Double.parseDouble(args[i+1]);
            }
        }

    }
}

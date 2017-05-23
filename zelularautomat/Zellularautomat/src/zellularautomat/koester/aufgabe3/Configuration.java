package zellularautomat.koester.aufgabe3;

public class Configuration {
	private static int CellSize  = 50;
	private static int FieldSizeX  = 50;
	private static int FieldSizeY  = 50;
	private static intfree-flow
	velocity
	
	
	public static void configureFromArguments(String[] args){
		for(int i = 0; i < args.length; i++){
			if(args[i] == "--cell-size"){
				Configuration.CellSize = Integer.parseInt(args[i+1]);
			}
			
			if(args[i] == "--field-size-x"){
				Configuration.FieldSizeX = Integer.parseInt(args[i+1]);
			}
			
			if(args[i] == "--field-size-y"){
				Configuration.FieldSizeY = Integer.parseInt(args[i+1]);
			}

		}
	}
	
	public static int getCellSize() {
		return CellSize;
	}
	public static void setCellSize(int cellSize) {
		CellSize = cellSize;
	}
	public static int getFieldSizeX() {
		return FieldSizeX;
	}
	public static void setFieldSizeX(int fieldSizeX) {
		FieldSizeX = fieldSizeX;
	}
	public static int getFieldSizeY() {
		return FieldSizeY;
	}
	public static void setFieldSizeY(int fieldSizeY) {
		FieldSizeY = fieldSizeY;
	}
	
	
}

package zellularautomat.koester.aufgabe3;

public class Field {
	private int FieldSizeX;
	private int FieldSizeY;
	private Cell Cells[][];
	
	
	public Field(int fieldSizeX, int fieldSizeY) {
		super();
		FieldSizeX = fieldSizeX;
		FieldSizeY = fieldSizeX;
		Cells = new Cell[fieldSizeX][fieldSizeY];
	}
	
	
	public Cell getCell(int X, int Y){
		if(X <= FieldSizeX && Y <= FieldSizeY)
			return Cells[X][Y];
		else return null;
	}
	
}

package field.cell;

import field.location.Location;

public class Cell {
	private final Location location;
	private boolean occupied;

	public Cell(Location location) {
		this(location,false);
	}
	public Cell(Location location, boolean occupied) {
		this.location = location;
		this.occupied = occupied;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Cell cell = (Cell) o;

		if (occupied != cell.occupied) return false;
		return location != null ? location.equals(cell.location) : cell.location == null;
	}

	@Override
	public int hashCode() {
		int result = location != null ? location.hashCode() : 0;
		result = 31 * result + (occupied ? 1 : 0);
		return result;
	}

	public Location location() {
		return location;
	}

	@Override
	public String toString() {
		return "Cell{" +
				"location=" + location +
				", occupied=" + occupied +
				'}';
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void occupy() {
		if (occupied) {
			throw new IllegalArgumentException("Cell is already occupied!");
		}
		occupied = true;
	}

	public void free() {
		if (!occupied) {
			throw new IllegalArgumentException("Cannot free Cell, it is not occupied!");
		}
		occupied = false;
	}
}

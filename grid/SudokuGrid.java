/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

 package grid;

 import java.io.*;
import java.util.HashMap;


/**
 * Abstract class representing the general interface for a Sudoku grid.
 * Both standard and Killer Sudoku extend from this abstract class.
 */
public abstract class SudokuGrid
{
	public int grid[][];
	public int gridSize;
	public int validInputs[];
	
	public int cageCount;
	public int cageConstraintsList[][][];
    /** 
     * Load the specified file and construct an initial grid from the contents
     * of the file.  See assignment specifications and sampleGames to see
     * more details about the format of the input files.
     *
     * @param filename Filename of the file containing the intial configuration
     *                  of the grid we will solve.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void initGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Write out the current values in the grid to file.  This must be implemented
     * in order for your assignment to be evaluated by our testing.
     *
     * @param filename Name of file to write output to.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void outputGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Converts grid to a String representation.  Useful for displaying to
     * output streams.
     *
     * @return String representation of the grid.
     */
    public abstract String toString();


    /**
     * Checks and validates whether the current grid satisfies the constraints
     * of the game in question (either standard or Killer Sudoku).  Override to
     * implement game specific checking.
     *
     * @return True if grid satisfies all constraints of the game in question.
     */
    public abstract boolean validate();
    
    /**
	 * Method to check if a Sudoku row is valid
	 * 
	 * @param rowIndex
	 * @return Boolean representing the validity of a row
	 */
	public boolean validateRow(int rowIndex) {
		HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();

		/* Creating a hash map from the valid inputs */
		for (int i = 0; i < this.validInputs.length; i++) {
			map.put(this.validInputs[i], false);
		}

		/* Iterating through the columns */
		for (int j = 0; j < this.gridSize; j++) {
			/* Skipping if -1 */
			if (this.grid[rowIndex][j] != -1) {
				/* Checking if it is a valid input in the grid */
				if (map.containsKey(this.grid[rowIndex][j])) {
					if (map.get(this.grid[rowIndex][j])) {
						return false;
					} else {
						map.put(this.grid[rowIndex][j], true);
					}
				} else {
					return false;
				} 
			}
		}

		return true;
	}

	public boolean validateColumn(int colIndex) {
		HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();

		/* Creating a hash map from the valid inputs */
		for (int i = 0; i < this.validInputs.length; i++) {
			map.put(this.validInputs[i], false);
		}

		/* Iterating through the columns */
		for (int i = 0; i < this.gridSize; i++) {
			/* Skipping if -1 */
			if (this.grid[i][colIndex] != -1) {
				/* Checking if it is a valid input in the grid */
				if (map.containsKey(this.grid[i][colIndex])) {
					if (map.get(this.grid[i][colIndex])) {
						return false;
					} else {
						map.put(this.grid[i][colIndex], true);
					}
				} else {
					return false;
				}
			}
		}

		return true;
	}

	public boolean validateBox(int boxNumber) {
		HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>(); 

		/* Creating a hash map from the valid inputs */
		for (int i = 0; i < this.validInputs.length; i++) {
			map.put(this.validInputs[i], false);
		}
		
		/* Calculating inner box dimensions*/
		int boxSize = (int) (Math.sqrt(this.gridSize));
		int startingRow = (boxNumber / boxSize) * boxSize;
		int startingCol = (boxNumber % boxSize) * boxSize;

		for (int i = startingRow; i < startingRow + boxSize; i++) {
			for (int j = startingCol; j < startingCol + boxSize; j++) {
				/* Skipping if -1 */
				if (this.grid[i][j] != -1) {
					/* Checking if it is a valid input in the grid */
					if (map.containsKey(this.grid[i][j])) {
						if (map.get(this.grid[i][j])) {
							return false;
						} else {
							map.put(this.grid[i][j], true);
						}
					} else {
						return false;
					}
				}
			}
		}

		return true;
	}
} // end of abstract class SudokuGrid

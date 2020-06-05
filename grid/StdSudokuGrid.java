/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class implementing the grid for standard Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task A and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class StdSudokuGrid extends SudokuGrid {
	// TODO: Add your own attributes
	

	public StdSudokuGrid() {
		super();

		/**
		 * Filling empty array spots with default -1 value
		 */
		for (int[] row : this.grid) {
			Arrays.fill(row, -1);
		}

	}

	/* ********************************************************* */

	@Override
	public void initGrid(String filename) throws FileNotFoundException, IOException {
		try {
			String content = new String(Files.readAllBytes(Paths.get(filename)));
			String[] lines = content.split("\n");

			int numberOfLines = lines.length;

			for (int i = 0; i < numberOfLines; i++) {
				if (0 == i) {
					/* Getting grid size */
					this.gridSize = Integer.parseInt(lines[i].trim());
				} else if (1 == i) {
					/* Getting valid inputs */
					this.validInputs = Arrays.stream(lines[i].trim().split(" ")).mapToInt(Integer::parseInt).toArray();
				} else {
					/* Getting the grid */
					String SplittedString[] = lines[i].trim().split(" ");
					String possArray[] = SplittedString[0].split(",");

					int xPosition = Integer.parseInt(possArray[0]);
					int yPosition = Integer.parseInt(possArray[1]);
					int value = Integer.parseInt(SplittedString[1]);

					this.grid[xPosition][yPosition] = value;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	} // end of initBoard()

	@Override
	public void outputGrid(String filename) throws FileNotFoundException, IOException {
		try {
			FileWriter myWriter = new FileWriter(filename);
			myWriter.write(this.toString());
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	} // end of outputBoard()

	@Override
	public String toString() {

		String gridString = "";

		for (int i = 0; i < this.gridSize; i++) {
			for (int j = 0; j < this.gridSize; j++) {
				gridString += (this.grid[i][j]);
				if (j != this.gridSize - 1) {
					gridString += ",";
				}
			}
			gridString += "\n";
		}

		return gridString;

	} // end of toString()

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

	@Override
	public boolean validate() {

		for (int i = 0; i < this.gridSize; i++) {
			if(this.validateRow(i)) {
				if(this.validateColumn(i)) {
					if(this.validateBox(i)) {
						continue;
					}else {
						return false;
					}
				}else {
					return false;
				}
			}else {
				return false;
			}
//			if( this.validateRow(i) && this.validateColumn(i) && this.validateBox(i)) {
//				continue;
//			}else {
//				return false;
//			}
		}

		// placeholder
		return true;
	} // end of validate()

} // end of class StdSudokuGrid



























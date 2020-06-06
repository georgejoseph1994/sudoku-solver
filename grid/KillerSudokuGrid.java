/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class implementing the grid for Killer Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task E and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid {
	// TODO: Add your own attributes

	public KillerSudokuGrid() {
		super();
	} // end of KillerSudokuGrid()

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
					this.grid = new int[this.gridSize][this.gridSize];

					/**
					 * Filling empty array spots with default -1 value
					 */
					for (int[] row : this.grid) {
						Arrays.fill(row, -1);
					}

				} else if (1 == i) {

					/* Getting valid inputs */
					this.validInputs = Arrays.stream(lines[i].trim().split(" ")).mapToInt(Integer::parseInt).toArray();

				} else if (2 == i) {

					this.cageCount = Integer.parseInt(lines[i].trim());
					this.cageConstraintsList = new int[this.cageCount][][];

				} else {

					/* Getting the grid */
					String splittedString[] = lines[i].trim().split(" ");
					int currentCageTotal = Integer.parseInt(splittedString[0]);

					int cageConstraint[][] = new int[splittedString.length][2];
					cageConstraint[0][0] = currentCageTotal;

					for (int j = 1; j < splittedString.length; j++) {
						String[] coordinatesArray = splittedString[j].split(",");
						cageConstraint[j][0] = Integer.parseInt(coordinatesArray[0]);
						cageConstraint[j][1] = Integer.parseInt(coordinatesArray[1]);
					}
					this.cageConstraintsList[i - 3] = cageConstraint;
				}
			}
		} catch (

		Exception e) {
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

	public boolean validateCage() {
		HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
		/* iterating each constraint */
		for (int i = 0; i < this.cageConstraintsList.length; i++) {

			/* Creating a hash map from the valid inputs */
			for (int m = 0; m < this.validInputs.length; m++) {
				map.put(this.validInputs[m], false);
			}

			int sum = 0;
			int j = 1;
			boolean minusOneFound = false;
			for (j = 1; j < this.cageConstraintsList[i].length; j++) {
				int posX = this.cageConstraintsList[i][j][0];
				int posY = this.cageConstraintsList[i][j][1];

				/* Skipping if -1 */
				if (this.grid[posX][posY] != -1) {

					/* Checking if it is a valid input in the grid */
					if (map.containsKey(this.grid[posX][posY])) {
						if (map.get(this.grid[posX][posY])) {
							return false;
						} else {
							map.put(this.grid[posX][posY], true);
						}
					} else {
						return false;
					}

					/* Checking the sum of cage equals cage sum */
					sum += this.grid[posX][posY];
					if (sum > this.cageConstraintsList[i][0][0]) {
						return false;
					}
				} else {
					minusOneFound = true;
				}
			}
			if (!minusOneFound) {
				if (sum != this.cageConstraintsList[i][0][0]) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean fastValidateCage(int x, int y) {

		HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
		ArrayList<int[][]> selectedCageConstraintList = new ArrayList<int[][]>();
		
		/* iterating each constraint */
		int sizeOfCageConstraintList = this.cageConstraintsList.length;
		for (int l = 0; l < sizeOfCageConstraintList; l++) {
			int constraintLength = this.cageConstraintsList[l].length;

			boolean xyFound = false;
			for (int m = 1; m < constraintLength; m++) {
				int posX = this.cageConstraintsList[l][m][0];
				int posY = this.cageConstraintsList[l][m][1];
				if (posX == x && posY == y) {
					xyFound = true;
				}
			}
			if (xyFound==true) {
				selectedCageConstraintList.add(this.cageConstraintsList[l]);
			}
		}
		

		for (int i = 0; i < selectedCageConstraintList.size(); i++) {
			/* Creating a hash map from the valid inputs */
			for (int m = 0; m < this.validInputs.length; m++) {
				map.put(this.validInputs[m], false);
			}

			int sum = 0;
			int j = 1;
			boolean minusOneFound = false;
			int constraintLength = selectedCageConstraintList.get(i).length;
			for (j = 1; j < constraintLength ; j++) {
				int posX = selectedCageConstraintList.get(i)[j][0];
				int posY = selectedCageConstraintList.get(i)[j][1];

				/* Skipping if -1 */
				if (this.grid[posX][posY] != -1) {

					/* Checking if it is a valid input in the grid */
					if (map.containsKey(this.grid[posX][posY])) {
						if (map.get(this.grid[posX][posY])) {
							return false;
						} else {
							map.put(this.grid[posX][posY], true);
						}
					} else {
						return false;
					}

					/* Checking the sum of cage equals cage sum */
					sum += this.grid[posX][posY];
					if (sum > selectedCageConstraintList.get(i)[0][0]) {
						return false;
					}
				} else {
					minusOneFound = true;
				}
			}
			if (!minusOneFound) {
				if (sum != selectedCageConstraintList.get(i)[0][0]) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean validate() {
		for (int i = 0; i < this.gridSize; i++) {
			if (this.validateRow(i)) {
				if (this.validateColumn(i)) {
					if (this.validateBox(i)) {
						continue;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		if (!this.validateCage())
			return false;

		// placeholder
		return true;
	} // end of validate()

} // end of class KillerSudokuGrid

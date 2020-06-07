/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.KillerSudokuGrid;
import grid.SudokuGrid;

/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver {
	// TODO: Add attributes as needed.

	public KillerBackTrackingSolver() {
		// TODO: any initialisation you want to implement.
	} // end of KillerBackTrackingSolver()

	/*
	 * Validates only the row, column, box and cage of the input i and j.
	 */
	public boolean fastValidate(int boxSize, SudokuGrid grid, int i, int j) {

		return grid.validateRow(i) 
				&& grid.validateColumn(j)
				&& grid.validateBox((boxSize * (i / boxSize)) + (j / boxSize))
				&& ((KillerSudokuGrid) grid).fastValidateCage(i, j);
	}

	@Override
	public boolean solve(SudokuGrid grid) {
		int boxSize = (int) (Math.sqrt(grid.gridSize));
		
		/* iterating through the grid*/
		for (int i = 0; i < grid.gridSize; i++) {
			for (int j = 0; j < grid.gridSize; j++) {

				if (grid.grid[i][j] == -1) {
					
					int sizeOfValidInputs = grid.validInputs.length;
					/* iterating through the valid inputs array*/
					for (int k = 0; k < sizeOfValidInputs; k++) {
						
						/* Assigning the grid values to value in valid inputs one by one*/
						grid.grid[i][j] = grid.validInputs[k];

						/* Checking if valid grid */
						if (fastValidate(boxSize, grid, i, j) == true) {
							
							/* if valid calling the method solve again */
							if (!solve(grid)) {
								grid.grid[i][j] = -1;
							} else
								break;
						} else {
							/* resetting the grid position*/
							grid.grid[i][j] = -1;
						}
					}
					if (grid.grid[i][j] == -1) {
						return false;
					}
				}

			}
		}
		return true;
	} // end of solve()

} // end of class KillerBackTrackingSolver()

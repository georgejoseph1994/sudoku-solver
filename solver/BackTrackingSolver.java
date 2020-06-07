/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.KillerSudokuGrid;
import grid.SudokuGrid;

/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver {

	public BackTrackingSolver() {
	} // end of BackTrackingSolver()
	
	/*
	 * Validates only the row column and box of the input i and j.
	 */
	public boolean fastValidate(int boxSize, SudokuGrid grid, int i, int j) {
		return grid.validateRow(i) && grid.validateColumn(j)
				&& grid.validateBox((boxSize * (i / boxSize)) + (j / boxSize));
	}

	@Override
	public boolean solve(SudokuGrid grid) {
		/* iterating through the grid*/
		for (int i = 0; i < grid.gridSize; i++) {
			for (int j = 0; j < grid.gridSize; j++) {
				
				if (grid.grid[i][j] == -1) {
					
					/* iterating through the valid inputs array*/
					for (int k = 0; k < grid.validInputs.length; k++) {
						
						/* Assigning the grid values to value in valid inputs one by one*/
						grid.grid[i][j] = grid.validInputs[k];
						
						/* Checking if the grid is valid */
						if (this.fastValidate(grid.gridSize, grid, i, j) == true) {
							
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

} // end of class BackTrackingSolver()

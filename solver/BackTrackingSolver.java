/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.SudokuGrid;

/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver {
	// TODO: Add attributes as needed.

	public BackTrackingSolver() { 
		// TODO: any initialisation you want to implement.
	} // end of BackTrackingSolver()

	@Override
	public boolean solve(SudokuGrid grid) {

		for (int i = 0; i < grid.gridSize; i++) {
			for (int j = 0; j < grid.gridSize; j++) {

				if (grid.grid[i][j] == -1) {
					for (int k = 0; k < grid.validInputs.length; k++) {
						grid.grid[i][j] = grid.validInputs[k];
						/* Checking if valid grid */
						if (grid.validate() == true) {
							if (!solve(grid)) {
								grid.grid[i][j] = -1;
							} else
								break;
						} else {
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

/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

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
		/**
		 * Filling empty array spots with default -1 value
		 */
		for (int[] row : this.grid) {
			Arrays.fill(row, -1);
		}
		
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
		// TODO
	} // end of outputBoard()

	@Override
	public String toString() {
		// TODO

		// placeholder
		return String.valueOf("");
	} // end of toString()

	@Override
	public boolean validate() {
		// TODO

		// placeholder
		return false;
	} // end of validate()

} // end of class KillerSudokuGrid

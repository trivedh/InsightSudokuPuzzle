
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class Sudoku 
{
	public static void main(String[] args) throws IOException
	{
		// Read a Sudoku puzzle 
		int[][] grid = readPuzzle();
		if (!isValid(grid))
			System.out.println("Invalid input");
		else if (search(grid)) 
			{
				System.out.println("Solution is found");
				printPuzzle(grid);
			}
			else
			System.out.println("No solution");
		
	}
	/** Read a Sudoku puzzle from csv file */
	public static int[][] readPuzzle() throws IOException
	{
		String input="input.csv";
		BufferedReader br = new BufferedReader(new FileReader(input));
		String lines="";
		int i=0,j=0;
		int[][] grid = new int[9][9];
		while((lines=br.readLine()) !=null)
		{
			String[] value=lines.split(","); 
			for(int k=0;k<value.length;k++)
			{
				grid[i][j++]=Integer.parseInt(value[k]);//read values into grid array( two dimensional array )
				if(j==9 && i<9)  { i++;  j=0; }
				if(i==9) break;
				
			}
			
	
		}
		br.close();
		return grid;
	}
	/** Print the values  to a csv file 
	 * @throws IOException */
	public static void printPuzzle(int[][] grid) throws IOException 
	{
		String output="output.csv";
	
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                bw.write(Integer.toString(grid[i][j]));
                bw.append(",");
    	            }
            bw.append("\n");
         }
	    bw.flush();
            bw.close();
	}
	/** Obtain a list of free cells from the puzzle */
	public static int[][] getFreeCellList(int[][] grid) 
	{
		// Determine the number of free cells
		int numberOfFreeCells = 0;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (grid[i][j] == 0)
					numberOfFreeCells++;
		// Store free cell positions into freeCellList
		int[][] freeCellList = new int[numberOfFreeCells][2];
		int count = 0;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (grid[i][j] == 0) {
					freeCellList[count][0] = i;
					freeCellList[count++][1] = j;
				}
		return freeCellList;
	}
		
	/** Search for a solution */
	public static boolean search(int[][] grid) 
	{
		int[][] freeCellList = getFreeCellList(grid); // Free cells
		if (freeCellList.length == 0)
			return true; // "No free cells");
		int k = 0; // Start from the first free cell
		while (true)
		{
			int i = freeCellList[k][0];
			int j = freeCellList[k][1];
			if (grid[i][j] == 0)
				grid[i][j] = 1; // Fill the free cell with number 1
			if (isValid(i, j, grid)) 
			{
				if (k + 1 == freeCellList.length) // No more free cells
				{ 
					return true; // A solution is found
				}
				else  // Move to the next free cell
				{ 
					k++;
				}
			}
			else if (grid[i][j] < 9) 
			{
				// Fill the free cell with the next possible value
				grid[i][j] = grid[i][j] + 1;
			}
			else  // free cell grid[i][j] is 9, backtrack
			{ 
				while (grid[i][j] == 9) 
				{
					if (k == 0) 
					{
						return false; // No possible value
					}
					grid[i][j] = 0; // Reset to free cell
					k--; // Backtrack to the preceding free cell
					i = freeCellList[k][0];
					j = freeCellList[k][1];
				}
				// Fill the free cell with the next possible value,
				// search continues from this free cell at k
				grid[i][j] = grid[i][j] + 1;
			}
		}
	}
	/** Check whether grid[i][j] is valid in the grid */
	public static boolean isValid(int i, int j, int[][] grid) 
	{
		// Check whether grid[i][j] is valid at the i's row
		for (int column = 0; column < 9; column++)
			if (column != j && grid[i][column] == grid[i][j])
				return false;
		// Check whether grid[i][j] is valid at the j's column
		for (int row = 0; row < 9; row++)
			if (row != i && grid[row][j] == grid[i][j])
				return false;
		// Check whether grid[i][j] is valid in the 3 by 3 box
		for (int row = (i / 3) * 3; row < (i / 3) * 3 + 3; row++)
			for (int col = (j / 3) * 3; col < (j / 3) * 3 + 3; col++)
				if (row != i && col != j && grid[row][col] == grid[i][j])
					return false;
		return true; // The current value at grid[i][j] is valid
	}
		/** Check whether the fixed cells are valid in the grid */
	public static boolean isValid(int[][] grid)
	{
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (grid[i][j] < 0 || grid[i][j] > 9 ||
						(grid[i][j] != 0 && !isValid(i, j, grid)))
					return false;
		return true; // The fixed cells are valid
	}
}

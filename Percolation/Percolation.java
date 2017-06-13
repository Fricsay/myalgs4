import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Percolation
{
    private boolean[][] opened;    //array to hold the open/close status for each site
    private int gridSize;          //the edge of the grid
    private int openSites = 0;     //total numbers of open sites
    private int top = 0;           //virtual site to represent the top site of the grid
    private int bottom;            //virtual site to represent the bottom node
    private WeightedQuickUnionUF wqf;
    
    //create n-by-n grid, all blocked
    public Percolation(int n)
    {
        if (n <= 0)
            throw new java.lang.IllegalArgumentException("n must be an integer larger than 0");
        
        gridSize = n;
        bottom = gridSize * gridSize + 1;
        wqf = new WeightedQuickUnionUF(gridSize * gridSize + 2); //with two more virtual sites
        opened = new boolean[gridSize][gridSize]; //boolean default is false, Boolean default is NULL
    }

    //open up a blocked site
    public void open(int row, int col)
    {//we can check if this is already opened here
     //or we can check that before passing the site into this function
     //Here I assume it is checked when randomly picking up the site
        
        checkBoundary(row, col);
                
        //set the site to be opened
        opened[row-1][col-1] = true;
        openSites++;
        
        if (row == 1)
        {//top row, must link to the virtual top site
            wqf.union(top, getSiteIndex(row, col));
        }
        
        /***********************************************************
        //this would cause backwash described in the checklists.
        //This won't impact the final result but the visual result.
        if (row == gridSize)
        {//bottom row, must link to the virtual bottom site
            wqf.union(bottom, getSiteIndex(row, col));
        }
        ***********************************************************/
        
        if (row > 1 && isOpen(row-1, col))
        {//link the upper site if it is opened
            wqf.union(getSiteIndex(row, col), getSiteIndex(row-1, col));
        }
        if (row < gridSize && isOpen(row+1, col))
        {//link the lower site if it is opened
            wqf.union(getSiteIndex(row, col), getSiteIndex(row+1, col));
        }
        if (col > 1 && isOpen(row, col-1))
        {//link the left site if it is opened
            wqf.union(getSiteIndex(row, col), getSiteIndex(row, col-1));
        }
        if (col < gridSize && isOpen(row, col+1))
        {//link the right site if it is opened
            wqf.union(getSiteIndex(row, col), getSiteIndex(row, col+1));
        }
        
        return;
    }
    
    //check if the certain site is opened
    public boolean isOpen(int row, int col)
    {
        checkBoundary(row, col);
        
        return opened[row-1][col-1];
    }
    
    //check if this is a full site, which connect to the virtual top site
    public boolean isFull(int row, int col)
    {
        checkBoundary(row, col);
        
        if (opened[row-1][col-1])
            return wqf.connected(top, getSiteIndex(row, col));
        else
            return false;
    }
    
    //return total numbers of open sites
    public int numberOfOpenSites()
    {
        return openSites;
    }
    
    //check if the system is percolated
    public boolean percolates()
    {
        //This to prevent the backwash issue
        //We union the bottom only if the bottom site is a Full site 
        for (int j = 1; j <= gridSize; j++)
        {
            //skip block sites to save some time
            if(isOpen(gridSize, j) && isFull(gridSize, j))
            {
                 wqf.union(bottom, getSiteIndex(gridSize, j));
            }
        }
        
        return wqf.connected(top, bottom);
    }
    
    //helper function to get the site index, starting from 1, 0 is the virtual top site
    private int getSiteIndex(int row, int col)
    {
        checkBoundary(row, col);
        
        return gridSize*(row-1)+col;
    }
    
    //helper function to check the boundary
    private void checkBoundary(int row, int col)
    {
        if (row < 1 || row > gridSize)
            throw new java.lang.IndexOutOfBoundsException("row index " + row + " is out of bound");
        if (col < 1 || col > gridSize)
            throw new java.lang.IndexOutOfBoundsException("column index " + col + " is out of bound");
    }
    
    public static void main(String[] args)
    {
        int n = StdIn.readInt();
        Percolation system = new Percolation(n);
        
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            
            if (!system.isOpen(row, col))
            {
                system.open(row, col);
            }
        }
        
        if (system.percolates())
            StdOut.println("percolates");
        else
            StdOut.println("not percolates");
    }
}

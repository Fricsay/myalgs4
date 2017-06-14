import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Percolation
{
    private boolean[][] opened;    //array to hold the open/close status for each site
    private int gridSize;               //the edge of the grid
    private int openSites = 0;      //total numbers of open sites
    private int top = 0;                //virtual site to represent the top site of the grid
    private int bottom;                //virtual site to represent the bottom node
    private WeightedQuickUnionUF wqf;
    private WeightedQuickUnionUF wqf2; //for solving backwash issue
    
    //create n-by-n grid, all blocked
    public Percolation(int n)
    {
        if (n <= 0)
            throw new java.lang.IllegalArgumentException("n must be an integer larger than 0");
        
        gridSize = n;
        bottom = gridSize * gridSize + 1;
        wqf = new WeightedQuickUnionUF(gridSize * gridSize + 2); //with two more virtual sites
        wqf2 = new WeightedQuickUnionUF(gridSize * gridSize + 1); //with no bottom virtual site
        opened = new boolean[gridSize][gridSize]; //boolean default is false, Boolean default is NULL
    }

    //open up a blocked site
    public void open(int row, int col)
    {        
        checkBoundary(row, col);
                
        //allow open multiple times
        if (opened[row-1][col-1])
            return;
        
        //set the site to be opened
        opened[row-1][col-1] = true;
        openSites++;
        
        if (row == 1)
        {//top row, must link to the virtual top site
            wqf.union(top, getSiteIndex(row, col));
            wqf2.union(top, getSiteIndex(row, col));
        }
        
        if (row == gridSize)
        {//bottom row, must link to the virtual bottom site
            wqf.union(bottom, getSiteIndex(row, col));
        }
        
        if (row > 1 && isOpen(row-1, col))
        {//link the upper site if it is opened
            wqf.union(getSiteIndex(row, col), getSiteIndex(row-1, col));
            wqf2.union(getSiteIndex(row, col), getSiteIndex(row-1, col));
        }
        if (row < gridSize && isOpen(row+1, col))
        {//link the lower site if it is opened
            wqf.union(getSiteIndex(row, col), getSiteIndex(row+1, col));
            wqf2.union(getSiteIndex(row, col), getSiteIndex(row+1, col));
        }
        if (col > 1 && isOpen(row, col-1))
        {//link the left site if it is opened
            wqf.union(getSiteIndex(row, col), getSiteIndex(row, col-1));
            wqf2.union(getSiteIndex(row, col), getSiteIndex(row, col-1));
        }
        if (col < gridSize && isOpen(row, col+1))
        {//link the right site if it is opened
            wqf.union(getSiteIndex(row, col), getSiteIndex(row, col+1));
            wqf2.union(getSiteIndex(row, col), getSiteIndex(row, col+1));
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
        
        //return wqf2 to prevent backwash issue
        if (opened[row-1][col-1])
            return wqf2.connected(top, getSiteIndex(row, col));
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
        Percolation perco = new Percolation(n);
        
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            
            if (!perco.isOpen(row, col))
            {
                perco.open(row, col);
            }
        }
        
        if (perco.percolates())
            StdOut.println("percolates");
        else
            StdOut.println("not percolates");
    }
}
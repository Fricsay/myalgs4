import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
    private boolean[][] opened;
    private int gridSize = 0;
    private int openSites = 0;
    private int top = 0; //virtual site to represent the top site of the grid
    private int bottom = 0; //virtual site to represent the bottom node
    private WeightedQuickUnionUF wqf;
    
    //create n-by-n grid, all blocked
    public Percolation(int n)
    {
        if (n <= 0)
            throw new java.lang.IndexOutOfBoundsException("n must be an integer larger than 0");
        
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
        
        if (row == gridSize)
        {//bottom row, must link to the virtual bottom site
            wqf.union(bottom, getSiteIndex(row, col));
        }

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
    
    private void checkBoundary(int row, int col)
    {
        if (row < 1 || row > gridSize)
            throw new java.lang.IndexOutOfBoundsException("row index out of bound");
        if (col < 1 || col > gridSize)
            throw new java.lang.IndexOutOfBoundsException("column index out of bound");
    }
    
    public static void main(String[] args)
    {
        
    }
}
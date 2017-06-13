import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
//import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats
{
    private int trials;          //hoe many trials to be run for the Monte Carlo Simulation
    private int gridSize;        //the edge of the grid
    private Percolation perco;   //percolation system
    private int run = 0;         //How many runs are done
    private double threshold[];  //array to hold the threshold value for each Monte Carlo Simulation 
        
    //create n-by-n grid percolation system and run Monte Carlo Simulation
    public PercolationStats(int n, int t)
    {
        if (n <= 0 || t <= 0)
            throw new java.lang.IllegalArgumentException("n or trials must be an integer larger than 0");
        
        gridSize = n;
        trials = t;
        threshold = new double[trials];
        
         //StdOut.println("DEBUG: Start");  //debug spew
         
        //run Monte Carlo Simulation for t runs
        for (int i = 0; i < trials; i++)
        {
            monteCarloSimulation();
        }
    }
    
    private void monteCarloSimulation()
    {
        //StdOut.println("DEBUG: Enter monteCarloSimulation");  //debug spew
   
        perco = new Percolation(gridSize);
        
        while (!perco.percolates())
        {//open up the site that has not been opened, check the percolation stats
            int row = StdRandom.uniform(gridSize) + 1;
            int col = StdRandom.uniform(gridSize) + 1;
            
            if (!perco.isOpen(row, col))
            {
                //StdOut.println("DEBUG: open (" + row + ", " + col + ")");  //debug spew
                perco.open(row, col);
            }
        }
                
        //calculate the threshold
        threshold[run] = (double) perco.numberOfOpenSites() / (gridSize*gridSize);
        //StdOut.println("DEBUG: run " + run + ": " + threshold[run]);  //debug spew
        
        run++;
        perco = null;
    }

    //calculate the mean value
    public double mean()
    {
        return StdStats.mean(threshold);
    }
    
    //calculate the standard deviation value
    public double stddev()
    {
        return StdStats.stddev(threshold);
    }
    
    //95% confidence interval low part
    public double confidenceLo()
    {      
        return mean() - (1.96*stddev());
    }
    
    //95% confidence interval high part
    public double confidenceHi()
    {
        return mean() + (1.96*stddev());
    }
    
    public static void main(String args[])
    {
        /*Stopwatch time = new Stopwatch();*/
        
        //We should check if the input is actually an integer
        //but java is going to throw the exception anyway
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        
        PercolationStats percoStats = new PercolationStats(N, T);
        
        StdOut.println("mean                    = " + percoStats.mean());
        StdOut.println("stddev                  = " + percoStats.stddev());
        StdOut.println("95% confidence interval = " + "["+ percoStats.confidenceLo() + ", " + percoStats.confidenceHi() + "]");
        /*StdOut.println("elapsed time            = " + time.elapsedTime());*/
    }   
}

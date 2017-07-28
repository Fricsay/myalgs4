import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation 
{
   public static void main(String[] args)
   {
       RandomizedQueue<String> randQueue = new RandomizedQueue<String>();
       
       int k = Integer.parseInt(args[0]);
       
       for (int i = 0; !StdIn.isEmpty(); i++)
       {
           String s = StdIn.readString();

           if (i < k)
           {
               randQueue.enqueue(s);
           }
           else
           {
               //Reservoir sampling (to get the bonus point)
               //https://en.wikipedia.org/wiki/Reservoir_sampling
               //http://www.geeksforgeeks.org/reservoir-sampling/
               if (StdRandom.uniform(i+1) < k)
               {
                   randQueue.dequeue();
                   randQueue.enqueue(s);
               }
           }
       }
       
       if (k > randQueue.size() )
        {
           //need to consider if k is greater then all elements
           throw new IllegalArgumentException("not enough elements to be dequeued");
        }
       
       for (int i = 0; i < k; i++)
       {
           StdOut.println(randQueue.dequeue());
       }
   }
}
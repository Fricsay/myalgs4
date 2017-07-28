import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    private Item[] queue; //need to use array implementation to be efficient
    private int n;        //size of the queue
    
    //constructor, create the array
    public RandomizedQueue()
    {
        //create the array size of 2, resize when needed
        queue = (Item[]) new Object[2];
        n = 0;
    }
    
    //check if the queue is empty
    public boolean isEmpty()
    {
        return n == 0;
    }
    
    //get the size of the queue
    public int size()
    {
        return n;
    }
    
    //resize array if needed
    private void resize(int capacity)
    {
        Item[] temp = (Item[]) new Object[capacity];
        
        for (int i = 0; i < n; i++)
        {
            temp[i] = queue[i];
        }
        
        queue = temp;
    }
    
    //insert the item to the last
    public void enqueue(Item item) 
    {
        //throw the exception if the item is null
        if (item == null)
        {
            throw new IllegalArgumentException("Null item");
        }
        
        //double the array size when the queue is full
        if (n == queue.length)
        {
            resize(2 * queue.length);
        }
        
        queue[n] = item;
        n++;
    }
    
    public Item dequeue()
    {
        if (isEmpty())
        {
            //no items in the queue
            throw new NoSuchElementException("Queue underflow");
        }
        
        //randomly pick an index to be dequeu
        int rand = StdRandom.uniform(n);
        Item item = queue[rand];
        n--; //get the last index in the array
        //move the last one to fill the random place
        //since it is random output, don't care about the enqueue order
        queue[rand] = queue[n]; 
        queue[n] = null;
        
        //reduce the array size when only 1/4 items
        if(n > 0 && n < queue.length/4)
        {
            resize(queue.length/2);
        }
        
        return item;
    }
    
    public Item sample() 
    {
        if (isEmpty())
        {
            //no items in the queue
            throw new NoSuchElementException("Queue underflow");
        }
        
        //randomly pick one but no remove
        int rand = StdRandom.uniform(n);
        Item item = queue[rand];
        
        return item;
    }
    
    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator();
    }
    
    //an iterator, random out
    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private int current;
        private int[] random;
        
        public RandomizedQueueIterator()
        {
            //create a random index array
            random = new int[n];

            //fill it with order
            for (int i = 0; i < n; i++)
            {
                random[i] = i;
            }
            
            //shuffle the index
            StdRandom.shuffle(random);
            current = 0;
        }
        
        public boolean hasNext()
        {
            return current != random.length;
        }
        
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        
        public Item next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }

            return queue[random[current++]];
        }
    }
    
    public static void main(String[] args) 
    {
        //unitest
    }
}
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item>
{
    private Node<Item> first;  //front of the queue
    private Node<Item> last;   //end of the queue
    private int n;             //size of the queue
    
    //linked list class 'Node'
    private static class Node<Item>
    {
        private Item item;
        private Node<Item> next;
        private Node<Item> previous; //need to add this one to make sure constant time of removeLast operation
    }
    
    //Deque class constructor
    public Deque()
    {
        first = last = null;
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
    
    //add the item to the head of the queue
    public void addFirst(Item item)
    {
        //throw the exception if the item is null
        if (item == null)
        {
            throw new IllegalArgumentException("Null item");
        }
        
        Node<Item> node = new Node<Item>();
        node.item = item;
                       
        if (isEmpty())
        {
            //handle the first item adding, first = last = null at this time
            node.next = node.previous = null;
            first = last = node;
        }
        else
        {
            first.previous = node;
            node.next = first;
            first = node;
        }
        
        //increasing the size
        n++;
    }
    
    public void addLast(Item item)
    {
        //throw the exception if the item is null
        if (item == null)
        {
            throw new IllegalArgumentException("Null item");
        }
        
        Node<Item> node = new Node<Item>();
        node.item = item;
                       
        if (isEmpty())
        {
            //handle the first item adding, first = last = null at this time
            node.next = node.previous = null;
            first = last = node;
        }
        else
        {
            last.next = node;
            node.previous = last;
            last = node;
        }
        
        //increasing the size
        n++;
    }
    
    public Item removeFirst()
    {
        if (isEmpty())
        {
            //no items in the queue
            throw new NoSuchElementException("Queue underflow");
        }
        
        Item item = first.item;
        
        if (n == 1)
        {
            //only 1 item in the queue
            first = last = null;
        }
        else
        {
            //more than one item
            first = first.next;
            first.previous = null;
        }
            
        //decreasing the size
        n--;
        
        return item;
    }
    
    public Item removeLast()
    {
        if (isEmpty())
        {
            //no items in the queue
            throw new NoSuchElementException("Stack underflow");
        }
        
        Item item = last.item;
        
        if (n == 1)
        {
            //only 1 item in the queue
            first = last = null;
        }
        else
        {
            //more than one item
            last = last.previous;
            last.next = null;
        }
        
        //decreasing the size
        n--;
        
        return item;
    }
    
    //return an iterator
    public Iterator<Item> iterator()
    {
        return new DequeIterator(first);
    }
    
    //an iterator, FIFO
    private class DequeIterator implements Iterator<Item>
    {
        private Node<Item> current;
        
        public DequeIterator(Node<Item> first)
        {
            current = first;
        }
        
        public boolean hasNext()
        {
            return current != null;
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
            
            Item item = current.item;
            current = current.next;
            
            return item;
        }
    }
    
    //unitest
    public static void main(String[] args) 
    {
        Deque<String> deque = new Deque<String>();
        
        while (!StdIn.isEmpty())
        {
            String item = StdIn.readString();
            deque.addFirst(item);
        }
        
        StdOut.println("(" + deque.size() + " on the deque)");
        
        //iterator does not remove the items
        for (String current : deque)
        {
            StdOut.println(current);
        }
    }
}

public class ArrayQueue <E> implements QueueADT <E>{  
	
	int front;
	int rear;
	int size;
	protected E[] Q; 
	
	
	@SuppressWarnings("unchecked")
	public ArrayQueue(int cap) {
		
		 Q = (E[]) new Object[cap];
		 front = 0; 
		 rear = 0; 
		 size = cap;
	}
	
	public int size() { 
		return ((size - front + rear) % size);
	}
	public boolean isEmpty(){ 
		if (front == rear) { 
			return true;
		}
		else {
			return false;
		}
	}
	
	public E front() throws EmptyQueueException { 
		
		if (isEmpty())  {
			throw new EmptyQueueException("Empty queue"); }
		return Q[front]; 
		
	}
	
	public void enqueue (E element) { 
		if (size() == (size-1)) {
			System.out.println("We're gonna need a bigger boat");
			extendQueue();
		}
		try{
			Q[rear] = element;
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println(rear + " and " + Q.length);
		}
		rear = (rear + 1) % size;
		
	}
	
	@SuppressWarnings("unchecked")
	public void extendQueue() { 
		E[] nQ = (E[]) new Object[size * 2]; 
		int top = front; 
		int i = 0;
		while (top != rear) { 
			
			nQ[i] = Q[top];
			top = (top + 1) % size;
			i++;
		}
		front = 0;
		rear = i; 
		size = 2 * size; 
		Q = nQ; 	
	}
	
	public E dequeue() throws EmptyQueueException{
		E element;
		
		if (isEmpty())  {
			throw new EmptyQueueException("Empty queue"); }
		element = Q[front]; 
		front = (front + 1)% size; 		
		return element; 
	}

}

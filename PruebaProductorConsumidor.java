import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

interface Buffer{
	public void blockingPut(int value) throws InterruptedException;
	
	public int blockingGet() throws InterruptedException;
}


class Producer implements Runnable{
	private static final SecureRandom generator = new SecureRandom();
	 private final Buffer sharedLocation;
	 
	 public Producer(Buffer sharedLocation)  {        
		 this.sharedLocation = sharedLocation;
		 }
	


	 public void run() {
	 int sum = 0;     
	 for (int count = 1; count <= 10; count++){
		 try // sleep 0 to 3 seconds, then place value in Buffer 24         
		 {  
			 Thread.sleep(generator.nextInt(3000)); 
			 sharedLocation.blockingPut(count);
			 sum += count;           
			 System.out.printf("\t%2d%n", sum); 
		} 
		 catch (InterruptedException exception) 
		 { 
			 Thread.currentThread().interrupt(); 
			 } 
		 } 
	 System.out.printf( "Producer done producing%nTerminating Producer%n"); 
	 	} 
	 } 

class Consumer implements Runnable{

	private static final SecureRandom generator = new SecureRandom();
	private final Buffer sharedLocation;
	
	public Consumer(Buffer sharedLocation) {
		this.sharedLocation= sharedLocation;
	}
	
	public void run() {
		int sum=0;
		
		for (int count = 1; count <=10; count++) {
			
			try {
				Thread.sleep(generator.nextInt(3000));
				sum+= sharedLocation.blockingGet();
				System.out.printf("\t\t\t%2d%n" , sum);
			}
				catch(InterruptedException exception) {
					Thread.currentThread().interrupt();
				}
		}
		 System.out.printf("%n%s %d%n%s%n", "Consumer read values totaling", sum, "Terminating Consumer"); 
	}
}



class UnsynchronizedBuffer implements Buffer{
	private int buffer = -1;
	
	 public void blockingPut(int value) throws InterruptedException{
		 System.out.printf("Producer writes\t%2d", value); 
		 buffer = value;
	 }
	 public int blockingGet() throws InterruptedException {
		 System.out.printf("Consumer reads\t%2d", buffer);
		 return buffer;
	 }
}



public class PruebaProductorConsumidor{
	public static void main (String [] args)  throws InterruptedException{
		 ExecutorService executorService = Executors.newCachedThreadPool(); 
		 Buffer sharedLocation = new UnsynchronizedBuffer();
		 System.out.println("Action\t\tValue\tSum of Produced\tSum of Consumed");
		 System.out.printf( "------\t\t-----\t---------------\t---------------%n%n"); 
		 executorService.execute(new Producer(sharedLocation));
		 executorService.execute(new Consumer(sharedLocation));
		 
		 executorService.shutdown();
		 executorService.awaitTermination(1, TimeUnit.MINUTES);
	}
}
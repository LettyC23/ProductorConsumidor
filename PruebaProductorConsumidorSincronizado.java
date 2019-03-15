import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



class UnsynchronizedBuffer1 implements Buffer{
	private int buffer = -1;
	
	 public void blockingPut(int value) throws InterruptedException {
		 System.out.printf("Producer writes\t%2d", value);
		 buffer = value;
	 }
	 
	 public int blockingGet() throws InterruptedException{
		 
		 System.out.printf("Consumer reads\t%2d", buffer);
		 return buffer;
	 }
}

public class PruebaProductorConsumidorSincronizado  {    
	public static void main(String[] args) throws InterruptedException   {
		
		 ExecutorService executorService = Executors.newCachedThreadPool(); 
		 Buffer sharedLocation = new UnsynchronizedBuffer1();
		 
		 System.out.println( "Action\t\tValue\tSum of Produced\tSum of Consumed"); 
		 System.out.printf("------\t\t-----\t---------------\t---------------%n%n"); 
		 
		 executorService.execute(new Producer(sharedLocation)); 
		 executorService.execute(new Consumer(sharedLocation));
		 
		 executorService.shutdown();
		 executorService.awaitTermination(1, TimeUnit.MINUTES);
	}
}
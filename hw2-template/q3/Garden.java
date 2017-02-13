// Rahul Jain, Leo Xia
//EID: rj8656, 
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Garden {
	ReentrantLock shovel = new ReentrantLock(); 
	ReentrantLock seed = new ReentrantLock(); 
	Condition nothingtoseed; 
	Condition nothingtofill; 
	Condition dontdig; 
	AtomicInteger num_filled;
	AtomicInteger num_seeded; 
	AtomicInteger num_dug; 
	
	public Garden() { 
	  this.nothingtoseed = seed.newCondition();
	  this.nothingtofill = shovel.newCondition();
	  this.dontdig = shovel.newCondition();
	  this.num_seeded = new AtomicInteger(0); 
	  this.num_filled = new AtomicInteger(0);
	  this.num_dug = new AtomicInteger(0);
	} 
	
	public void startDigging() throws InterruptedException {		//take shovel; wait if not allowed to dig
		shovel.lock();
		while((num_dug.get() - num_seeded.get()) >= 4|| (num_dug.get() - num_filled.get()) >=8 ){
			dontdig.await();
		} 
		
	} 
	
	public void doneDigging() { 	//unlock shovel and signal to seeder that there is a new hole dug
		num_dug.incrementAndGet(); 
		shovel.unlock(); 
		seed.lock();
		try{
			nothingtoseed.signal();
		}finally{
			seed.unlock();
		}
		
	} 
	
	public void startSeeding() throws InterruptedException {	//wait if there are no holes that are unseeded
		seed.lock();
		while((num_dug.get() - num_seeded.get()) <= 0 ){
			nothingtoseed.await();
		}
	}
	
	public void doneSeeding() {	//signal to both the filler and the digger that there is an update
		num_seeded.incrementAndGet(); 
		seed.unlock();
		
		shovel.lock();
		try{
			dontdig.signal();
			nothingtofill.signal();
		}finally{
			shovel.unlock(); 
		}
		
	} 
	
	public void startFilling() throws InterruptedException { //take shovel; wait if nothing to fill 
		shovel.lock();
		
		while(num_seeded.get() - num_filled.get() <= 0){
			nothingtofill.await(); 
		}
	} 
	
	public void doneFilling() {	//signal to digger that there is an update
		num_filled.incrementAndGet();
		shovel.unlock(); 
		shovel.lock();
		try{
			dontdig.signal();
		}finally{
			shovel.unlock(); 
		}
		
	} 
	 
    /*
    * The following methods return the total number of holes dug, seeded or 
    * filled by Newton, Benjamin or Mary at the time the methods' are 
    * invoked on the garden class. */
   public int totalHolesDugByNewton() {
	   return num_dug.get();
   } 
   public int totalHolesSeededByBenjamin() {
	   return num_seeded.get();
   } 
   public int totalHolesFilledByMary() {
	   return num_filled.get(); 
   }
   @Override
   public String toString() {
     return "Holes dug: " + num_dug
         + "\nHoles seeded: " + num_seeded
         + "\nHoles filled: " + num_filled;
   }
}
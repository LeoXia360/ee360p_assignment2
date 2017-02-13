//Rahul Jain, Leo Xia
//rj8656, lx939
import java.util.concurrent.Semaphore; // for implementation using Semaphores
public class CyclicBarrier {
	int parties; 
	Semaphore mutex;
	Semaphore num_allowed;
	Semaphore stop_point; 
	int thread_count;
	int index; 
	public CyclicBarrier(int parties) {
		this.parties = parties; 
		thread_count = 0; 
		index = parties ; 
		mutex = new Semaphore(1);
		num_allowed = new Semaphore(parties);
		stop_point = new Semaphore(0); //this can't be acquired at all until a release is called once 
	}
	
	public int await() throws InterruptedException {
		num_allowed.acquire();
		mutex.acquire();
		index--; 
		int i = index; 
		thread_count ++; 
		if(thread_count == parties){
			stop_point.release();		//first time a release is called
		}
		mutex.release(); 
		stop_point.acquire();			// every thread stalls here until first time release is called
		stop_point.release();
		mutex.acquire(); 
		thread_count -- ; 
		if(thread_count == 0){    //reset
			index = parties;
			thread_count = 0; 
			stop_point = new Semaphore(0);
			num_allowed.release(parties);
			
		}
		mutex.release();
		return i;
	}
}

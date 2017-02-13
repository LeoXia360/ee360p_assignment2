// Rahul Jain and Leo Xia
// eid: rj8656, lx939

public class FairReadWriteLock {
	
	//need to use wait, notify, notifyAll

	// 1) no read-write or write-write conflict
	// 2) a writer thread that invokes beginWrite will be blocked until all preceding reader/writer threads have acquired and released the lock
	// 3) a reader thread that invokes beginRead will be blocked until all preceding writer threads have acquired and released the lock
	// 4) a reader thread cannot be blocked if preceding writer threads have acquired and released the lock
	
	
	int numReaders = 0;
	int numWriters = 0; 
                        
	public synchronized void beginRead() {
		System.out.println(Thread.currentThread().getId() + " thread is trying to read");

		
		while(numWriters > 0){
			try{
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		numReaders++;
		
		System.out.println(Thread.currentThread().getId() + " thread is beginning to read");


	}
	
	public synchronized void endRead() {
		numReaders -= 1;  
		notifyAll(); 
		System.out.println(Thread.currentThread().getId() + " thread has ended reading");

	}
	
	
	public synchronized void beginWrite() {
		System.out.println(Thread.currentThread().getId() + " thread is trying to write");

		while((numWriters > 0) || numReaders > 0){
			try{
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		numWriters++;

		System.out.println(Thread.currentThread().getId() + " thread is beginning to write");

	}
	
	
	public synchronized void endWrite() {
		numWriters -= 1; 
		notifyAll(); 
		System.out.println(Thread.currentThread().getId() + " thread has ended writing");

	}
}
	



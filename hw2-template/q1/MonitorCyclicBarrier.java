//Rahul Jain, Leo Xia
//rj8656,  lx939 
public class MonitorCyclicBarrier {
  int parties;
  int thread_count;
  int index; 

  public MonitorCyclicBarrier(int parties) {
    this.parties = parties;
    thread_count = 0;
    index = parties;  
  }

  public synchronized int await() throws InterruptedException {
    while(index <= 0) wait(); 
    int i = index;
    index--; 
    while(index>0) wait(); 			// every thread stalls here until last one comes and notifiesall
    thread_count++;
    if(thread_count == parties){
        index = parties; 
        thread_count = 0;
    }
    notifyAll();
    return i;
    
  }
}
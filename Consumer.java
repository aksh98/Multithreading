package hi;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
/////////////////////////////////////////////
/////////////////////////////////////////////
class Consumer implements Runnable{
	private final List<LinkedList<Float>> sharedQueue;
	private final String function;
	
	public Consumer(List<LinkedList<Float>> sharedQueue,String s){
		this.sharedQueue = sharedQueue;
		function = s;
	}

	public void run(){
		Float mini = (float) 1000.0,maxi=(float) -1000.0,sum=(float) 0.0;
		for(int j=0;j<3;j++){
			for(int i=0;i<100;i++){
				
				if(mini > (sharedQueue.get(j).get(i)))
					mini = sharedQueue.get(j).get(i);
				if(maxi < sharedQueue.get(j).get(i))
					maxi = sharedQueue.get(j).get(i);
				sum += sharedQueue.get(j).get(i);
		}
		if(function.equals("Consumer Avg"))//return sum/100;
			System.out.println("Average : "+ sum/100);
		else if(function.equals("Consumer Min"))//return mini;
			System.out.println("Minnn : "+mini);
		if(function.equals("Consumer Max"))//return maxi;
			System.out.println("Maxx : "+maxi);
		
		}

	}
}
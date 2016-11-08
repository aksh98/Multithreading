package hi;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
/////////////////////////////////////////////
/////////////////////////////////////////////
class Producer implements Runnable{
	private List<LinkedList<Float>> sharedQueue = new LinkedList<LinkedList<Float>>();
	//private final int maxSize = 100;

	public Producer(List<LinkedList<Float>> sharedQueue){
		this.sharedQueue = sharedQueue;
	}

	public void run(){
		for (int i = 1;i<=100 ;i++) {
			try{
				produce(i);
			}catch (InterruptedException ex){
				Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);

			}
		}
	}

	private void produce(int i) throws InterruptedException{
		synchronized(sharedQueue){
			while(sharedQueue.get(0).size()== 100){
				//System.out.println("Queue is full ! Producer is waiting for consumer to consume");
				sharedQueue.wait();
			}
		}
		synchronized(sharedQueue.get(2)){
			sharedQueue.get(2).add((float) i);
			Thread.sleep((long)(Math.random()*1000));
			sharedQueue.notifyAll();
		}
		synchronized(sharedQueue.get(1)){
			sharedQueue.get(1).add((float) i);
			Thread.sleep((long)(Math.random()*1000));
			sharedQueue.notifyAll();
		}
		synchronized(sharedQueue.get(0)){
			sharedQueue.get(0).add((float) i);
			Thread.sleep((long)(Math.random()*1000));
			sharedQueue.notifyAll();
		}
	}

//=========================================================
//==========================================================
public static void main(String[] args){
	//Consumer consumer = new Consumer(, null);
	List<Float> sharedQueue1 = new LinkedList<Float>();
	List<Float> sharedQueue2 = new LinkedList<Float>();
	List<Float> sharedQueue3 = new LinkedList<Float>();
	List<LinkedList<Float>> sharedQueue = new LinkedList<LinkedList<Float>>();
	String str1 = FileRead("src/temperature");
	String val1[] = str1.split("\n");
	String str2 = FileRead("src/humidity");
	String val2[] = str2.split("\n");
	String str3 = FileRead("src/rainfall");
	String val3[] = str1.split("\n");
	for (int i=0;i<500; i++) {
		try{
		sharedQueue1.add(i,Float.parseFloat(val1[i]));
		sharedQueue2.add(i,Float.parseFloat(val2[i]));
		sharedQueue3.add(i,Float.parseFloat(val3[i]));
		}
		catch(Exception e){
			
		}
	}
	sharedQueue.add(0,(LinkedList<Float>) sharedQueue1);
	sharedQueue.add(1,(LinkedList<Float>) sharedQueue2);
	sharedQueue.add(2,(LinkedList<Float>) sharedQueue3);
	Thread Temperature = new Thread(new Producer(sharedQueue),"Temperature");
	Thread Humidity = new Thread(new Producer(sharedQueue),"Humidity");
	Thread Rainfall = new Thread(new Producer(sharedQueue),"Rainfall");
	Thread Min = new Thread(new Consumer(sharedQueue,"Consumer Min"),"Consumer Min");
	Thread Max = new Thread(new Consumer(sharedQueue,"Consumer Max"),"Consumer Max");
	Thread Average = new Thread(new Consumer(sharedQueue,"Consumer Avg"),"Consumer Avg");
	
	Temperature.start();
	Humidity.start();
	Rainfall.start();
	Max.start();
	Min.start();
	Average.start();
	try{
	Temperature.join();
	Humidity.join();
	Rainfall.join();
	Max.join();
	Min.join();
	Average.join();
	}
	catch(InterruptedException e){
		
	}
}

/////////////////////////////////////////////////////
static String FileRead(String filename){
	FileInputStream stream = null;
	try{
		stream = new FileInputStream(filename);
	} catch (FileNotFoundException ex){
		ex.printStackTrace();
	}
	BufferedReader br = new BufferedReader(new InputStreamReader(stream));
	String result = "";
	String str;
	try {
		while ((str = br.readLine())!=null){
			result += str+"\r\n";
		}
	}
	catch (IOException ex){
		ex.printStackTrace();
	}
	try {
		br.close();
	} catch (IOException ex){
		ex.printStackTrace();
	}
	return result;
}
}
///////////////////////////////////////////////////

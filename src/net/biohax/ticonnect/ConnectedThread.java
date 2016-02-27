/**
 * 
 */
package net.biohax.ticonnect;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Karel Dhondt (biohax)
 * 
 *
 * Description : //TODO
 */



public abstract class ConnectedThread
{
	Lock lock;
	Condition datareceived; 
	
	protected ConnectedThread(){
		lock = new ReentrantLock();
		datareceived = lock.newCondition();
	}
	
	abstract public void write(byte[] data);
	abstract public byte[] getBufferedData();
	abstract public void resetBufferedData();
	
	public boolean await(int millis){
		try
		{
	        lock.lock();
			return datareceived.await(millis, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public void signal(){
		
		// TODO
		// try
		lock.lock();
		datareceived.signal();
        lock.unlock();

	}
}

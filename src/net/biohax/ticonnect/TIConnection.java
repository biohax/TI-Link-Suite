package net.biohax.ticonnect;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;



import java.util.List;

import net.biohax.ticonnect.titypes.*;

public class TIConnection
{

	
	private static final int TIMEOUT = 20000;
	
	private static final byte[] SCREENREQ = new byte[] {0x73, 0x6D, 0x00, 0x00};
	private static final byte[] ACK = new byte[] { 0x73, 0x56, 0x0D, 0x00 };
	private static final byte[] EOT = new byte[] { 0x73, (byte) 0x92, 0x0D, 0x00 };
	private static final byte[] CTS = new byte[] { 0x73, 0x09, 0x00 ,0x00 };
	private static final byte[] DIR = new byte[] {0x73, (byte) 0xA2, 0x0D, 0x00, 0x00, 0x00, 0x19, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x19, 0x00	};
	
	
	private final int SEND = 0;
	private final int GET = 1;

	ConnectedThread ct;

	public TIConnection(ConnectedThread serial)
	{
		this.ct = serial;
	}

	public synchronized void send(TIVariable var) throws IOException, InterruptedException
	{

		ct.write(var.genRequest(SEND));
		waitForSerial();
		
		//GET ACK
		//GET CTS
		
		//TODO check
		ct.resetBufferedData();
		
		ct.write(ACK);
		ct.write(var.genData());
		waitForSerial();

		ct.write(EOT);
		
		
		
    	byte[] response = ct.getBufferedData();
    	ct.resetBufferedData();

		StringBuilder sb1 = new StringBuilder();

		for (byte b : response)
		{

			sb1.append(String.format("0x%02X,", b));

		}
		System.out.println(response.length);
		System.out.println(sb1.toString());	
		
	}
	
	public TIVariable get(TIVariable var){

    	ct.write(var.genRequest(GET));
    	waitForSerial();
    	
    	//Get ACK
    	//Get confirmation of header
    	
    	//TODO check if ACK
    	ct.resetBufferedData();//reset buffer
    	
    	
    	ct.write(ACK);
    	ct.write(CTS);
    	waitForSerial();

    	//Get ACK
    	//Get data
    	
    	ct.write(EOT);
    	
    	
    	byte[] response = ct.getBufferedData();
    	ct.resetBufferedData();
    	
    	StringBuilder sb = new StringBuilder();
    	for (byte b: response){
    		sb.append(String.format("0x%02X, ", b));
    	}
    	
    	System.out.println(Integer.toString(response.length));
    	System.out.println(sb.toString());
    	
    	byte[] data = Arrays.copyOfRange(response, 8, (response.length-2));

    	var.setData(data);
    	
		return var;
	}
	
	public List<TIVariable> dirList()
	{
		byte[] temp;
		byte identifier;
		List<TIVariable> list = new ArrayList<TIVariable>();
		
		ct.write(DIR);
		waitForSerial();
		
		// get ACK
		// get bytes of free memory
		ct.resetBufferedData(); //reset bufferedData
		
		ct.write(ACK);
		waitForSerial();
		// Variables are now received until EOT
		
		while(!(Arrays.equals( (temp = ct.getBufferedData() ), EOT))){
			ct.resetBufferedData();
			
	    	StringBuilder sb = new StringBuilder();
	    	for (byte b: temp){
	    		sb.append(String.format("0x%02X, ", b));
	    	}
	    	System.out.println(Integer.toString(temp.length));
	    	System.out.println(sb.toString());
	    	
	    	
	    	identifier = temp[6];
	    	TIVariable tempVar = TIVariableFactory.buildTIVariable(identifier);
	    	
	    	if(tempVar!=null){
	    		tempVar.setVarname(Arrays.copyOfRange(temp, 7, (temp.length-2)));
	    		list.add(tempVar);
	    	}
	    	
	    	ct.write(ACK);
	    	waitForSerial();
		}
    	ct.write(ACK);

		
		return list;
		
	}
	
	public TIPicture getScreenShot() throws IOException, InterruptedException{
		
		ct.write(SCREENREQ); //screenshot request
		waitForSerial();

		ct.write(ACK);
		ct.write(EOT);
		

    	byte[] response = ct.getBufferedData();
    	ct.resetBufferedData();
    	
    	StringBuilder sb2 = new StringBuilder();
    	for (byte b: response){
    		sb2.append(String.format("0x%02X, ", b));
    	}
    	
    	System.out.println(sb2.toString());
    	System.out.println(response.length);

    	byte[] data = new byte[2 + TIPicture.LENGTH];
    	data[0] = (byte) 756 % 256;
    	data[1] = (byte) 756 / 256;
    	
    	
    	byte[] pixels = Arrays.copyOfRange(response, 8, (8 + TIPicture.LENGTH));
    	
    	System.arraycopy(pixels, 0, data, 2, TIPicture.LENGTH);
    	//TODO
    	
    	
    	
    	TIPicture temp = new TIPicture(data,1);
    	
		return temp;
		
	}
	

	public void sendKeyStroke(int index){
		
		byte[] keyRequest = {0x23, (byte) 0x87, 0x00, 0x00};
		keyRequest[2] = (byte) index;
		
		ct.write(keyRequest);
	}
	

	
	private boolean waitForSerial()
	{
		synchronized (ct)
		{
			System.out.println("Waiting for serial...");
			return ct.await(TIMEOUT);
			
			/*
			try
			{
				ct.wait();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;*/
		}
	}

	/*
	 * 	try
	    {
	         Thread.sleep(TIMEOUT);
	         // here, the timeout was reached
	    } catch (InterruptedException e)
	    {
	        // here we were woken up
	    }
	die thread nemen
	en .interrupt() doen
	
	*/


}
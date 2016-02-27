/**
 * 
 */

/**
 * @author Karel Dhondt (biohax)
 * 
 *
 * Description : //TODO
 */
import jssc.*;

import java.io.ByteArrayOutputStream;
import net.biohax.ticonnect.ConnectedThread;

//TODO make delay adjustable

public class Serial extends ConnectedThread {
	
	/**
	 * @throws Exception 
	 * 
	 */
	Serial()
	{
		super();
	}


	
	private static final int READ_DELAY = 50;
	private static final int WRITE_DELAY = 5;
	
	public volatile static boolean available = false;
	
	SerialPort serialPort;
	
	private static ByteArrayOutputStream dataBuffer = new ByteArrayOutputStream();
	private byte[] temp;
		
	
	
	
	public void connect( String port) throws Exception {
		serialPort = new SerialPort(port);
		serialPort.openPort();
		serialPort.setParams( SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE );
		PortReader listener = new PortReader();
		serialPort.addEventListener(listener);
	}
	
	public void write(byte[] data){
		
		try
		{
			for (byte b : data){
				serialPort.writeInt(b);
				Thread.sleep(WRITE_DELAY);
			}
		} catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void resetBufferedData()
	{
		dataBuffer.reset();
	}
	
	
	public byte[] getBufferedData()
	{
		return dataBuffer.toByteArray();
	}
	
 
	private class PortReader implements SerialPortEventListener {

	    /**
		 * 
		 */

		@Override
	    public void serialEvent(SerialPortEvent event) {
	        if(event.isRXCHAR() && event.getEventValue() > 0) {
	            try {
	                 while((temp = serialPort.readBytes())!=null){
		                 dataBuffer.write(temp);
		                 Thread.sleep(READ_DELAY);
		                 System.out.println("recieving");
	                 }
	                 System.out.println("done");
	                 //synchronized (Serial.this){ Serial.this.notify(); }
	                 Serial.this.signal();
	                 System.out.println("done1");
	            }
	            catch (Exception ex) {
	                System.out.println("Error in receiving string from COM-port: " + ex);
	                ex.printStackTrace();
	            }
	        }
	    }

	}

	/**
	 * @return
	 */
	public String[] getAvailablePorts()
	{
		return SerialPortList.getPortNames();
	}

	

 
}
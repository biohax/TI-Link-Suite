import java.awt.EventQueue;

import net.biohax.ticonnect.TIConnection;



public class Main extends Thread {

	static Serial serial;
	
	public static void main(String[] args) throws Exception {
		
		serial = new Serial();
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ConnectionDialog cd = new ConnectionDialog();
					cd.setSerialPorts(serial.getAvailablePorts());
					cd.setVisible(true);					
					
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		
		synchronized (Main.class)
		{
			Main.class.wait();
		}
		
		//debug
		for(String s : serial.getAvailablePorts()){
			System.out.println(s);
		}
		
		TIConnection tic = new TIConnection(serial);		
		
		@SuppressWarnings("unused")
		UserInterface ui = new UserInterface(tic);
					

	}

	public synchronized static void establishConnection(String port)
	{
		try
		{
			serial.connect(port);
			Main.class.notify();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

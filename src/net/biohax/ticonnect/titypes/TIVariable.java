package net.biohax.ticonnect.titypes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public abstract class TIVariable
{
	private static final int SEND = 0;
	private static final int GET = 1;
	
	
	
	public abstract void setData(byte[] data);
	public abstract void setVarname(byte[] name);
	
	public abstract byte[] getData();
	public abstract byte[] getVarname();
	public abstract byte getType();
	public abstract String getExtension();

	public abstract void update(Object[] array);


	
	public TIVariable() {}	
	
	public TIVariable(TIVariable t) {
		this.setVarname(t.getVarname());
		this.setData(t.getData());
	}
	
	public TIVariable(byte[] varName, byte[] data){
		this.setVarname(varName);
		this.setVarname(data);
	}
	
	public byte[] genRequest(int type) {
		
		int lenData ;
		   
		byte[] dataWrapper = new byte[19];
		byte[] data = new byte[19];
		
		int varLength = 0;		
		
        //START OF REQUEST PACKET//
		
	        dataWrapper[0] = (byte) 0x23 ; // (identifier) i'm a PC
	        
	        switch(type){
		        case GET:dataWrapper[1] = (byte) 0xA2; break; // (type of connection) Silent - Request to Get Variable
		        case SEND: dataWrapper[1] = (byte) 0xC9; break; // (type of connection) Silent - Request to Send Variable
		        
	        	default: dataWrapper[1] = (byte) 0xA2; break; //get is default

	        }
	        
	        data[2] = (byte) getType(); // type of variable
		    appBytes(data,getVarname(),3,getVarname().length); //adding variable name
			
			lenData =  13; // request length is always 13
			
			dataWrapper[3] = (byte) (lenData / 256) ; //Size of data
			dataWrapper[2] = (byte) (lenData % 256) ; //Size of data
			
			if (type==SEND){
				varLength = getData().length;				
			}
			
			data[1] = (byte) (varLength / 256) ; //Size of actual variable data, in bytes
			data[0] = (byte) (varLength % 256) ; //Size of actual variable data, in bytes
			
		    appBytes(dataWrapper,data,4,15);
				   
		    int checksum = calculateChecksum(data,lenData); // get checksum as integer
		    
			dataWrapper[17] = (byte) (checksum % 256) ; // Split checksum most significant first
		    dataWrapper[18] = (byte) (checksum / 256) ; // Split checksum
	    
		// DEBUG PURPOSES
			
		    StringBuilder sb = new StringBuilder();
		    
		    for (byte b : dataWrapper) {
		        sb.append(String.format("0x%02X,", b));
		    }
		    
		    System.out.println("genRequest: "+ sb.toString());
		    
		return dataWrapper;
	}
	
	public byte[] genData() {
		 
		byte[] dataWrapper = new byte[6 + getData().length] ;
        
		
        dataWrapper[0] = 0x23 ; //(identifier) i'm a PC
        dataWrapper[1] = 0x15 ; //Silent - Request to Send Variable
         
        int lenData = getData().length+2;
		byte[] varData = new byte[lenData];

        
        
        dataWrapper[2] = (byte) ((getData().length) % 256) ; //size of actual data most significant first
        dataWrapper[3] = (byte) ((getData().length) / 256) ;
		
      //START OF DATA PACKET//

               

		appBytes(varData,getData(),0,getData().length);
		
		
		System.out.println("lenData: "+ lenData);
	   
	    int checksum = calculateChecksum(getData(),getData().length);  
	    
	    varData[1+getData().length] = (byte) (checksum / 256) ; //Size of actual variable data, in bytes
		varData[getData().length] = (byte) (checksum % 256) ; //Size of actual variable data, in bytes
		
		appBytes(dataWrapper, varData, 4, lenData);
	    
		
	    StringBuilder sb = new StringBuilder();
	    for (byte b : dataWrapper) {
	        sb.append(String.format("0x%02X,", b));
	    }
	    System.out.println("Data: "+ sb.toString());
		    
		return dataWrapper;
	    
	}
	
	public void toFile(File file){
		
		ByteArrayOutputStream data = new ByteArrayOutputStream();

		try
		{
			String newPath = changeExtension(file.getAbsolutePath(),getExtension());
			
			File newFile = new File(newPath);
			
			
			data.write(new byte[]{0x0D, 0x00});//TODO archive
			data.write(new byte[]{(byte) (getData().length % 256), (byte) (getData().length / 256)}); //length most significant first
			data.write(new byte[]{getType()}); //variable type
			data.write(getVarname()); // variable name
			
			for(int i=0; i<(8-getVarname().length); i++){
				data.write(0x00); //padding zero's
			}
			
			data.write(0x00);
			data.write(0x00);
			data.write(new byte[]{(byte) (getData().length % 256), (byte) (getData().length / 256)}); //copy of length
			data.write(getData()); //append data
			
			ByteArrayOutputStream dataWrapper = new ByteArrayOutputStream();
			
			dataWrapper.write("**TI83F*".getBytes());
			dataWrapper.write(new byte[]{(byte)0x1A, 0x0A, 0x00}); // random chain of bytes
			dataWrapper.write("Generated by TIConnect (c) Karel Dhondt   ".getBytes()); //Comment
			System.out.println(dataWrapper.size());
			dataWrapper.write(new byte[]{(byte) (data.size() % 256), (byte) (data.size() / 256)}); //copy of length
			dataWrapper.write(data.toByteArray());
			dataWrapper.write(new byte[]{(byte) (calculateChecksum(data.toByteArray(),data.toByteArray().length) % 256), (byte) (calculateChecksum(data.toByteArray(),data.toByteArray().length) / 256)});
			
			FileOutputStream fos = new FileOutputStream (newFile);
			dataWrapper.writeTo(fos);
			fos.close();
			
		} catch (IOException e)	{e.printStackTrace();}

	}
	
	protected BidiMap<Integer, Byte> genMap(){

		BidiMap<Integer, Byte> bidiMap = new DualHashBidiMap<Integer, Byte>();
		bidiMap.put( 1 , (byte) 0x00 );
		bidiMap.put( 2 , (byte) 0x01 );
		bidiMap.put( 3 , (byte) 0x02 );
		bidiMap.put( 4 , (byte) 0x03 );
		bidiMap.put( 5 , (byte) 0x04 );
		bidiMap.put( 6 , (byte) 0x05 );
		bidiMap.put( 7 , (byte) 0x06 );
		bidiMap.put( 8 , (byte) 0x07 );
		bidiMap.put( 9 , (byte) 0x08 );
		bidiMap.put( 0 , (byte) 0x09 );
		
		return bidiMap;
	}

	
	
	/**
	*  Factory method to create object from file
	*  
	**/
	public static TIVariable getVar(File file)
	{
			
	        try
			{
	        	byte [] fileData = Files.readAllBytes(file.toPath());
	        	
				byte type = fileData[55+4]; //file type
				System.out.println(type);
				int size = fileData[55+2] + (255 * fileData[55+3]); //size of data
				System.out.println(size);

				ByteArrayOutputStream temp = new ByteArrayOutputStream();
				
				temp.write( Arrays.copyOfRange(fileData, (55+5), ((55+5)+8)));
				byte[] varname = temp.toByteArray();
				
				temp.reset();
				
				temp.write(Arrays.copyOfRange(fileData, (55+17), ((55+17)+size)));
				byte[] data = temp.toByteArray();
				
				System.out.println(data[0]);
				System.out.println(data.length);
					//TODO error	
					//TODO TIVariableFactory
				switch(type){
					case 0x01:
					case 0x0C:
						break;//new TINumberbreak
					case 0x04:
						return new TIString(varname, data);
					case 0x05:
					case 0x06:
						return new TIProgram(varname, data);
					case 0x07:
						return new TIPicture(varname, data);
					case 0x09:
					

		        }
		        

			} catch (IOException e)	{e.printStackTrace();}
			return null;
	}
	
	
	private static void appBytes(byte[] data1, byte[] data2, int offset, int count)
	{
		int x ;
		
		for(x=0; x<count; x++) {
		    data1[x+offset] = data2[x];
		}
		
	}
		
	
	private static int calculateChecksum(byte[] data, int datalength)
	{
	   int checksum = 0;
	   
	   for(int x=0; x<datalength; x++) {
	      checksum = ( checksum + (short) (data[x] & 0xFF));  //overflow automatically limits to 16 bits
	   }
	   
	   return checksum;
	}
	
	private static String changeExtension(String originalName, String newExtension) {
	    int lastDot = originalName.lastIndexOf(".");
	    if (lastDot != -1) {
	        return originalName.substring(0, lastDot) + newExtension;
	    } else {
	        return originalName + newExtension;
	    }
	}
	




	
}

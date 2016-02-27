package net.biohax.ticonnect.titypes;


import java.util.Arrays;

import net.biohax.ticonnect.TIEncoder;

public class TIProgram extends TIVariable
{

	//declarations
	
		private byte[] varname = new byte[8];
		static final byte TYPE = 0x05;
		
		
		private byte[] data;
		
		//TODO edit locked		
		//private boolean locked;
		
		private final String EXT = ".8Xp";
		
		private TIEncoder t = new TIEncoder();
		
	//constructors
	
		public TIProgram(){}
	
		public TIProgram(String name,String code){
						
			this.varname=name.substring(0, Math.min(name.length(),8)).toUpperCase().getBytes();
			
			this.data = new byte[t.encode(code).length + 2];
			
			this.data[0] = (byte) (t.encode(code).length % 256);
			this.data[1] = (byte) (t.encode(code).length / 256);
		    System.arraycopy(t.encode(code), 0, data, 2, t.encode(code).length);
			
			
		}
		
		public TIProgram(byte[] varname,byte[] data){
			super(varname,data);	
		}
		
	// Getter's and setter's

		@Override
		public byte[] getData()
		{
			return data;
		}
	
		@Override
		public byte[] getVarname()
		{
			return varname;
		}
	
		@Override
		public byte getType()
		{
			return TYPE;
		}
		
		@Override
		public String getExtension()
		{
			return EXT;
		}
	
		@Override
		public void setData(byte[] data)
		{
			this.data = data;
		}
	
		@Override
		public void setVarname(byte[] varname)
		{
			this.varname = varname;
		}
	
		@Override
		public void update(Object[] obj)
		{
			// TODO Auto-generated method stub
			
		}





	// object methods
	
		@Override
		public String toString(){
			return "Program " + t.decode(varname);	//used for list
		}
		
		@Override
		public boolean equals(Object other)
		{
			if (!(other instanceof TIString))
			{
				return false;
			}
			return Arrays.equals(data, ((TIString) other).getData());
		}
	
	
		@Override
		public int hashCode()
		{
			return Arrays.hashCode(data);
		}


	
	
	
}

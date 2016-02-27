package net.biohax.ticonnect.titypes;

import java.util.Arrays;

import org.apache.commons.collections4.BidiMap;

import net.biohax.ticonnect.TIEncoder;

public class TIString extends TIVariable {
	
	// declarations
		static final byte TYPE = 0x04;
		private final String EXT = ".8Xs";
		
		private byte [] varname= new byte[8];
		private byte[] data;

		private TIEncoder t = new TIEncoder();
		private BidiMap<Integer, Byte> nameMap = genMap();

	//constructors
		
		public TIString(){}
		
		public TIString(String string, int number ){
			
			setNumber(number);
			
			this.data = new byte[t.encode(string).length + 2];
			
			this.data[0] = (byte) (t.encode(string).length % 256);
			this.data[1] = (byte) (t.encode(string).length / 256);
			
		    System.arraycopy(t.encode(string), 0, data, 2, t.encode(string).length);
			

		}
		
		public TIString(int number){
			setNumber(number);
		}
		
		public TIString(byte[] varname, byte[] data)
		{
			super(varname,data);
			
		}
		
	// getter's and setter's

		@Override
		public void setData(byte[] data)
		{
			this.data = data;
		}
		
		@Override
		public void setVarname(byte[] varname)
		{
			this.varname=varname;
		}
		
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
		
		public String getString(){
			return t.decode(Arrays.copyOfRange(data, 2, data.length));			
		}
		
		public void setString(String s){
			
			this.data = new byte[t.encode(s).length + 2];
			
			this.data[0] = (byte) (t.encode(s).length % 256);
			this.data[1] = (byte) (t.encode(s).length / 256);
		    System.arraycopy(t.encode(s), 0, data, 2, t.encode(s).length);
		}
		
		public void setNumber(int numb){
			varname[0] = (byte) 0xAA;
			varname[1] = nameMap.get(numb);
			
		}
		
		public int getNumber(){
			return nameMap.inverseBidiMap().get(varname[1]);
		}
				
	// methods
		
		@Override
		public void update(Object[] obj)
		{
			String s = (String) obj[0];
			
			this.data = new byte[t.encode(s).length + 2];
			
			this.data[0] = (byte) (t.encode(s).length % 256);
			this.data[1] = (byte) (t.encode(s).length / 256);
		    System.arraycopy(t.encode(s), 0, data, 2, t.encode(s).length);
		    
		 }
		

		

	// object methods
	
		@Override
		public String toString(){
			return "String " + getNumber();	//used for list
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

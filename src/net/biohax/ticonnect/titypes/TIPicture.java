package net.biohax.ticonnect.titypes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.Arrays;




import org.apache.commons.collections4.BidiMap;

public class TIPicture extends TIVariable
{
	final private int HEIGHT = 63;
	final private int WIDTH = 96;
	public static final int LENGTH = 756;
		
	private byte [] varname = new byte[8];
	private byte[] data = new byte[LENGTH + 2];  //Data contains length

	
	static final byte TYPE = 0x07;
	private final String EXT = ".8Xi";
	

	private BidiMap<Integer, Byte> nameMap = genMap();
	
	//Constructors
	
		public TIPicture(){}
		
		public TIPicture(int number){
			setNumber(number);
		}
		public TIPicture(byte[] data,int number){
			setNumber(number);
			this.data=data;
		}
		
		public TIPicture(byte[] varname, byte[] data)
		{
			super(varname,data);
		}
		
	// Getter's and setter's
		
		@Override
		public void setData(byte[] data)
		{
			this.data=data;		
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
		
		
		public void setNumber(int numb){
			varname[0] = (byte) 0x60;
			varname[1] = nameMap.get(numb);
			
		}
		
		public int getNumber(){
			return nameMap.inverseBidiMap().get(varname[1]);
		}
		
		
		public BufferedImage getImage(){
			
			BufferedImage img = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);			
			Graphics2D g = img.createGraphics();
			
			// making background BLACK
			g.setColor(Color.WHITE);
			g.fillRect(0,0,WIDTH,HEIGHT);
		
			
			byte[] pixels = Arrays.copyOfRange(data, 2, LENGTH+2);
	    	
	 		int x, y, pixelIndex;
	
			for (int i=0; i<LENGTH; i++){
				
				for (int j=0; j<8; j++){

					pixelIndex = i*8 + j; // -2: first 2 bytes are the data length 
					
					if( ( pixels[i] & ((byte)Math.pow(2, 7-j)) ) != 0 ){ // -7:first bit is 128
												
						x = pixelIndex % WIDTH;
						y = pixelIndex / WIDTH;
												
	            		img.setRGB(x, y, Color.BLACK.getRGB());
	            		
		            } 
				
				}
			}
						
			return img;
		}
		

		
		
		
	//Methods
		
		@Override
		public void update(Object[] array)
		{
			
			Image toolkitImage = ((BufferedImage) array[0]).getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);

				// width and height are of the toolkit image
				BufferedImage buffered = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_BYTE_BINARY);
				Graphics g = buffered.getGraphics();
				g.drawImage(toolkitImage, 0, 0, null);
				g.dispose();
			
			
			int[] pixel = new int[WIDTH*HEIGHT];
			
		    try{ new PixelGrabber(buffered, 0, 0, WIDTH, HEIGHT, pixel, 0, WIDTH).grabPixels(); } catch (InterruptedException e) { e.printStackTrace(); }

	        this.data[0] = (byte) ( LENGTH % 256);
			this.data[1] = (byte) ( LENGTH / 256);
	        
	        for(int i=2; i<LENGTH+2;i++){
	        	for (int j=0; j<8; j++){
	            	if( pixel[(i-2)*8+j] != Color.WHITE.getRGB()){
		            	data[i]+=Math.pow(2, 7-j); // +2 add after length
		            }
	            }
	        }
	        
		        
	        StringBuilder sb = new StringBuilder();
	
			for (byte b :data)
			{
				sb.append(String.format("0x%02X,", b));
			}
	
			System.out.println("Generated protocol: " + sb.toString());
			System.out.println(data.length);
	
			System.out.println("White:");
			System.out.println(Color.BLACK.getRGB());
	
		}
	

	// object methods
	
		@Override
		public String toString(){
			return "Picture " + getNumber();	//used for list
		}
		
		@Override
		public boolean equals(Object other)
		{
			if (!(other instanceof TIPicture))
			{
				return false;
			}
			return Arrays.equals(data, ((TIPicture) other).getData());
		}
	
	
		@Override
		public int hashCode()
		{
			return Arrays.hashCode(data);
		}
		
	
}

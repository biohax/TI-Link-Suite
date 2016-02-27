/**
 * 
 */
package net.biohax.ticonnect.titypes;

/**
 * @author Karel Dhondt (biohax)
 * 
 *
 * Description : //TODO
 */
public class TIVariableFactory
{

	public static TIVariable buildTIVariable(byte identifier) {
		TIVariable var = null;
        switch (identifier) {
	        case TIString.TYPE:
	        	System.out.println("string");
	            var = new TIString();
	            break;
	 
	        case TIProgram.TYPE:
	        	System.out.println("program");
	            var = new TIProgram();
	            break;
	 
	        case TIPicture.TYPE:
	        	System.out.println("picture");
	            var = new TIPicture();
	            break;
	 
	        default:
	        	System.out.println("other");
	            // throw some exception
	            break;
        }
        return var;
	}
}

package net.biohax.ticonnect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.util.Map.Entry;

public class TIEncoder
{

	private Map<String, ByteArrayWrapper> tokens = new HashMap<String, ByteArrayWrapper>();
	private Map<ByteArrayWrapper, String> tokenToString = new HashMap<ByteArrayWrapper, String>();

	public TIEncoder()
	{
		addTokens(); // adding tokens to map
		tokenToString.putAll(invert(tokens)); // inverting map
	}


	public byte[] encode(String str)
	{

		String part;
		byte[] tokenizedString;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int index = 0;
		ByteArrayWrapper tokenBAW = null;
		byte[] tokenB = null;
		while (index < str.length())
		{
			int partLength = 32; // length of largest substring + 1
			while (0 < partLength && tokenBAW == null)
			{
				partLength--;
				part = str.substring(index, Math.min(str.length(), index + partLength)); //i see... ik heb wel bij den 2de nen ofb exception gehad :s ofb? out of bound ah, hoezo, bij wat "tweede"?
				tokenBAW = tokens.get(part);
				if (tokenBAW != null)
				{
					tokenB = tokenBAW.toByteArray();
				}
			}

			try
			{

				baos.write(tokenB);
			} catch (IOException e)
			{
			}

			index = index + partLength;
			tokenBAW = null;// reset token
		}

		tokenizedString = baos.toByteArray();
		baos.reset();

		return tokenizedString;

	}

	public String decode(byte[] byteArray)
	{

		StringBuilder sb = new StringBuilder();

		int index = 0;
		String tokenString = null;
		byte[] part = new byte[byteArray.length];
		ByteArrayWrapper partBAW;

		System.out.println(byteArray.length);

		while (index < byteArray.length)
		{
			int partLength = 10; // length of largest substring + 1
			while (0 < partLength && tokenString == null)
			{
				partLength--;
				part = Arrays.copyOfRange(byteArray, index, Math.min(byteArray.length, index + partLength));
				partBAW = new ByteArrayWrapper(part);
				tokenString = tokenToString.get(partBAW);
			}

			sb.append(tokenString);
			
			tokenString = null;// reset token
			index = index + partLength;
		}

		return sb.toString();

	}

	private static <V, K> Map<V, K> invert(Map<K, V> map)
	{

		Map<V, K> inv = new HashMap<V, K>();

		for (Entry<K, V> entry : map.entrySet())
			inv.put(entry.getValue(), entry.getKey());

		return inv;
	}

	static class ByteArrayWrapper
	{
		private byte[] data;

		public ByteArrayWrapper(byte[] data)
		{
			if (data == null)
			{
				throw new NullPointerException();
			}
			this.data = data;
		}

		public byte[] toByteArray()
		{
			return data;
		}

		@Override
		public boolean equals(Object other)
		{
			if (!(other instanceof ByteArrayWrapper))
			{
				return false;
			}
			return Arrays.equals(data, ((ByteArrayWrapper) other).toByteArray());
		}

		@Override
		public int hashCode()
		{
			return Arrays.hashCode(data);
		}

	}

	private void addTokens()
	{

		tokens.put("", wrap(0x00));
		tokens.put("->DMS", wrap(0x01));
		tokens.put("->Dec", wrap(0x02));
		tokens.put("->Frac", wrap(0x03));
		tokens.put("->", wrap(0x04));
		tokens.put("Boxplot", wrap(0x05));
		tokens.put("[", wrap(0x06));
		tokens.put("]", wrap(0x07));
		tokens.put("{", wrap(0x08));
		tokens.put("}", wrap(0x09));
		tokens.put("?", wrap(0x0A));
		tokens.put("°", wrap(0x0B));
		tokens.put("¯¹", wrap(0x0C));
		tokens.put("²", wrap(0x0D));
		tokens.put("?", wrap(0x0E));
		tokens.put("³", wrap(0x0F));
		tokens.put("(", wrap(0x10));
		tokens.put(")", wrap(0x11));
		tokens.put("round(", wrap(0x12));
		tokens.put("pxl-Test(", wrap(0x13));
		tokens.put("augment(", wrap(0x14));
		tokens.put("rowSwap(", wrap(0x15));
		tokens.put("row+(", wrap(0x16));
		tokens.put("*row(", wrap(0x17));
		tokens.put("*row+(", wrap(0x18));
		tokens.put("max(", wrap(0x19));
		tokens.put("min(", wrap(0x1A));
		tokens.put("R->Pr(", wrap(0x1B));
		tokens.put("R->P(teta)(", wrap(0x1C));
		tokens.put("P->Rx(", wrap(0x1D));
		tokens.put("P->Ry(", wrap(0x1E));
		tokens.put("median(", wrap(0x1F));
		tokens.put("randM(", wrap(0x20));
		tokens.put("mean(", wrap(0x21));
		tokens.put("solve(", wrap(0x22));
		tokens.put("seq(", wrap(0x23));
		tokens.put("fnInt(", wrap(0x24));
		tokens.put("nDeriv(", wrap(0x25));
		tokens.put("fMin(", wrap(0x27));
		tokens.put("fMax(", wrap(0x28));
		tokens.put(" ", wrap(0x29));
		tokens.put("\"", wrap(0x2A));
		tokens.put(",", wrap(0x2B));
		tokens.put("[i]", wrap(0x2C));
		tokens.put("!", wrap(0x2D));
		tokens.put("CubicReg ", wrap(0x2E));
		tokens.put("QuartReg ", wrap(0x2F));
		tokens.put("0", wrap(0x30));
		tokens.put("1", wrap(0x31));
		tokens.put("2", wrap(0x32));
		tokens.put("3", wrap(0x33));
		tokens.put("4", wrap(0x34));
		tokens.put("5", wrap(0x35));
		tokens.put("6", wrap(0x36));
		tokens.put("7", wrap(0x37));
		tokens.put("8", wrap(0x38));
		tokens.put("9", wrap(0x39));
		tokens.put(".", wrap(0x3A));
		tokens.put("?", wrap(0x3B));
		tokens.put(" or ", wrap(0x3C));
		tokens.put(" xor ", wrap(0x3D));
		tokens.put(":", wrap(0x3E));

		tokens.put(" and ", wrap(0x40));
		tokens.put("A", wrap(0x41));
		tokens.put("B", wrap(0x42));
		tokens.put("C", wrap(0x43));
		tokens.put("D", wrap(0x44));
		tokens.put("E", wrap(0x45));
		tokens.put("F", wrap(0x46));
		tokens.put("G", wrap(0x47));
		tokens.put("H", wrap(0x48));
		tokens.put("I", wrap(0x49));
		tokens.put("J", wrap(0x4A));
		tokens.put("K", wrap(0x4B));
		tokens.put("L", wrap(0x4C));
		tokens.put("M", wrap(0x4D));
		tokens.put("N", wrap(0x4E));
		tokens.put("O", wrap(0x4F));
		tokens.put("P", wrap(0x50));
		tokens.put("Q", wrap(0x51));
		tokens.put("R", wrap(0x52));
		tokens.put("S", wrap(0x53));
		tokens.put("T", wrap(0x54));
		tokens.put("U", wrap(0x55));
		tokens.put("V", wrap(0x56));
		tokens.put("W", wrap(0x57));
		tokens.put("X", wrap(0x58));
		tokens.put("Y", wrap(0x59));
		tokens.put("Z", wrap(0x5A));
		tokens.put("(teta)", wrap(0x5B));

		tokens.put("[A]", wrap(0x5C, 0x00));
		tokens.put("[B]", wrap(0x5C, 0x01));
		tokens.put("[C]", wrap(0x5C, 0x02));
		tokens.put("[D]", wrap(0x5C, 0x03));
		tokens.put("[E]", wrap(0x5C, 0x04));
		tokens.put("[F]", wrap(0x5C, 0x05));
		tokens.put("[G]", wrap(0x5C, 0x06));
		tokens.put("[H]", wrap(0x5C, 0x07));
		tokens.put("[I]", wrap(0x5C, 0x08));
		tokens.put("[J]", wrap(0x5C, 0x09));

		tokens.put("L1", wrap(0x5D, 0x00));
		tokens.put("L2", wrap(0x5D, 0x01));
		tokens.put("L3", wrap(0x5D, 0x02));
		tokens.put("L4", wrap(0x5D, 0x03));
		tokens.put("L5", wrap(0x5D, 0x04));
		tokens.put("L6", wrap(0x5D, 0x05));

		tokens.put("Y1", wrap(0x5E, 0x10));
		tokens.put("Y2", wrap(0x5E, 0x11));
		tokens.put("Y3", wrap(0x5E, 0x12));
		tokens.put("Y4", wrap(0x5E, 0x13));
		tokens.put("Y5", wrap(0x5E, 0x14));
		tokens.put("Y6", wrap(0x5E, 0x15));
		tokens.put("Y7", wrap(0x5E, 0x16));
		tokens.put("Y8", wrap(0x5E, 0x17));
		tokens.put("Y9", wrap(0x5E, 0x18));
		tokens.put("Y0", wrap(0x5E, 0x19));
		tokens.put("X1T", wrap(0x5E, 0x20));
		tokens.put("Y1T", wrap(0x5E, 0x21));
		tokens.put("X2T", wrap(0x5E, 0x22));
		tokens.put("Y2T", wrap(0x5E, 0x23));
		tokens.put("X3T", wrap(0x5E, 0x24));
		tokens.put("Y3T", wrap(0x5E, 0x25));
		tokens.put("X4T", wrap(0x5E, 0x26));
		tokens.put("Y4T", wrap(0x5E, 0x27));
		tokens.put("X5T", wrap(0x5E, 0x28));
		tokens.put("Y5T", wrap(0x5E, 0x29));
		tokens.put("X6T", wrap(0x5E, 0x2A));
		tokens.put("Y6T", wrap(0x5E, 0x2B));
		tokens.put("r1", wrap(0x5E, 0x40));
		tokens.put("r2", wrap(0x5E, 0x41));
		tokens.put("r3", wrap(0x5E, 0x42));
		tokens.put("r4", wrap(0x5E, 0x43));
		tokens.put("r5", wrap(0x5E, 0x44));
		tokens.put("r6", wrap(0x5E, 0x45));
		tokens.put("|u", wrap(0x5E, 0x80));
		tokens.put("|v", wrap(0x5E, 0x81));
		tokens.put("|w", wrap(0x5E, 0x82));
		tokens.put("prgm", wrap(0x5F));

		tokens.put("Pic1", wrap(0x60, 0x00));
		tokens.put("Pic2", wrap(0x60, 0x01));
		tokens.put("Pic3", wrap(0x60, 0x02));
		tokens.put("Pic4", wrap(0x60, 0x03));
		tokens.put("Pic5", wrap(0x60, 0x04));
		tokens.put("Pic6", wrap(0x60, 0x05));
		tokens.put("Pic7", wrap(0x60, 0x06));
		tokens.put("Pic8", wrap(0x60, 0x07));
		tokens.put("Pic9", wrap(0x60, 0x08));
		tokens.put("Pic0", wrap(0x60, 0x09));

		tokens.put("GDB1", wrap(0x61, 0x00));
		tokens.put("GDB2", wrap(0x61, 0x01));
		tokens.put("GDB3", wrap(0x61, 0x02));
		tokens.put("GDB4", wrap(0x61, 0x03));
		tokens.put("GDB5", wrap(0x61, 0x04));
		tokens.put("GDB6", wrap(0x61, 0x05));
		tokens.put("GDB7", wrap(0x61, 0x06));
		tokens.put("GDB8", wrap(0x61, 0x07));
		tokens.put("GDB9", wrap(0x61, 0x08));
		tokens.put("GDB0", wrap(0x61, 0x09));

		tokens.put("[RegEQ]", wrap(0x62, 0x01));
		tokens.put("[n]", wrap(0x62, 0x02));
		tokens.put("?", wrap(0x62, 0x03));
		tokens.put("Sx", wrap(0x62, 0x04));
		tokens.put("Sx²", wrap(0x62, 0x05));
		tokens.put("[Sx]", wrap(0x62, 0x06));
		tokens.put("sx", wrap(0x62, 0x07));
		tokens.put("[minX]", wrap(0x62, 0x08));
		tokens.put("[maxX]", wrap(0x62, 0x09));
		tokens.put("[minY]", wrap(0x62, 0x0A));
		tokens.put("[maxY]", wrap(0x62, 0x0B));
		tokens.put("?", wrap(0x62, 0x0C));
		tokens.put("Sy", wrap(0x62, 0x0D));
		tokens.put("Sy²", wrap(0x62, 0x0E));
		tokens.put("[Sy]", wrap(0x62, 0x0F));
		tokens.put("sy", wrap(0x62, 0x10));
		tokens.put("Sxy", wrap(0x62, 0x11));
		tokens.put("[r]", wrap(0x62, 0x12));
		tokens.put("[Med]", wrap(0x62, 0x13));
		tokens.put("[Q1]", wrap(0x62, 0x14));
		tokens.put("[Q3]", wrap(0x62, 0x15));
		tokens.put("[|a]", wrap(0x62, 0x16));
		tokens.put("[|b]", wrap(0x62, 0x17));
		tokens.put("[|c]", wrap(0x62, 0x18));
		tokens.put("[|d]", wrap(0x62, 0x19));
		tokens.put("[|e]", wrap(0x62, 0x1A));
		tokens.put("x1", wrap(0x62, 0x1B));
		tokens.put("x2", wrap(0x62, 0x1C));
		tokens.put("x3", wrap(0x62, 0x1D));
		tokens.put("y1", wrap(0x62, 0x1E));
		tokens.put("y2", wrap(0x62, 0x1F));
		tokens.put("y3", wrap(0x62, 0x20));
		tokens.put("[recursiven]", wrap(0x62, 0x21));
		tokens.put("[p]", wrap(0x62, 0x22));
		tokens.put("[z]", wrap(0x62, 0x23));
		tokens.put("[t]", wrap(0x62, 0x24));
		tokens.put("?²", wrap(0x62, 0x25));
		tokens.put("[|F]", wrap(0x62, 0x26));
		tokens.put("[df]", wrap(0x62, 0x27));
		tokens.put("[?]", wrap(0x62, 0x28));
		tokens.put("?1", wrap(0x62, 0x29));
		tokens.put("?2", wrap(0x62, 0x2A));
		tokens.put("?1", wrap(0x62, 0x2B));
		tokens.put("Sx1", wrap(0x62, 0x2C));
		tokens.put("n1", wrap(0x62, 0x2D));
		tokens.put("?2", wrap(0x62, 0x2E));
		tokens.put("Sx2", wrap(0x62, 0x2F));
		tokens.put("n2", wrap(0x62, 0x30));
		tokens.put("[Sxp]", wrap(0x62, 0x31));
		tokens.put("[lower]", wrap(0x62, 0x32));
		tokens.put("[upper]", wrap(0x62, 0x33));
		tokens.put("[s]", wrap(0x62, 0x34));
		tokens.put("r²", wrap(0x62, 0x35));
		tokens.put("R²", wrap(0x62, 0x36));
		tokens.put("[factordf]", wrap(0x62, 0x37));
		tokens.put("[factorSS]", wrap(0x62, 0x38));
		tokens.put("[factorMS]", wrap(0x62, 0x39));
		tokens.put("[errordf]", wrap(0x62, 0x3A));
		tokens.put("[errorSS]", wrap(0x62, 0x3B));
		tokens.put("[errorMS]", wrap(0x62, 0x3C));

		tokens.put("ZXscl", wrap(0x63, 0x00));
		tokens.put("ZYscl", wrap(0x63, 0x01));
		tokens.put("Xscl", wrap(0x63, 0x02));
		tokens.put("Yscl", wrap(0x63, 0x03));
		tokens.put("u(nMin)", wrap(0x63, 0x04));
		tokens.put("v(nMin)", wrap(0x63, 0x05));
		tokens.put("Un-1", wrap(0x63, 0x06));
		tokens.put("Vn-1", wrap(0x63, 0x07));
		tokens.put("Zu(nmin)", wrap(0x63, 0x08));
		tokens.put("Zv(nmin)", wrap(0x63, 0x09));
		tokens.put("Xmin", wrap(0x63, 0x0A));
		tokens.put("Xmax", wrap(0x63, 0x0B));
		tokens.put("Ymin", wrap(0x63, 0x0C));
		tokens.put("Ymax", wrap(0x63, 0x0D));
		tokens.put("Tmin", wrap(0x63, 0x0E));
		tokens.put("Tmax", wrap(0x63, 0x0F));
		tokens.put("?Min", wrap(0x63, 0x10));
		tokens.put("?Max", wrap(0x63, 0x11));
		tokens.put("ZXmin", wrap(0x63, 0x12));
		tokens.put("ZXmax", wrap(0x63, 0x13));
		tokens.put("ZYmin", wrap(0x63, 0x14));
		tokens.put("ZYmax", wrap(0x63, 0x15));
		tokens.put("Z?min", wrap(0x63, 0x16));
		tokens.put("Z?max", wrap(0x63, 0x17));
		tokens.put("ZTmin", wrap(0x63, 0x18));
		tokens.put("ZTmax", wrap(0x63, 0x19));
		tokens.put("TblStart", wrap(0x63, 0x1A));
		tokens.put("PlotStart", wrap(0x63, 0x1B));
		tokens.put("ZPlotStart", wrap(0x63, 0x1C));
		tokens.put("nMax", wrap(0x63, 0x1D));
		tokens.put("ZnMax", wrap(0x63, 0x1E));
		tokens.put("nMin", wrap(0x63, 0x1F));
		tokens.put("ZnMin", wrap(0x63, 0x20));
		tokens.put("?Tbl", wrap(0x63, 0x21));
		tokens.put("Tstep", wrap(0x63, 0x22));
		tokens.put("?step", wrap(0x63, 0x23));
		tokens.put("ZTstep", wrap(0x63, 0x24));
		tokens.put("Z?step", wrap(0x63, 0x25));
		tokens.put("?X", wrap(0x63, 0x26));
		tokens.put("?Y", wrap(0x63, 0x27));
		tokens.put("XFact", wrap(0x63, 0x28));
		tokens.put("YFact", wrap(0x63, 0x29));
		tokens.put("TblInput", wrap(0x63, 0x2A));
		tokens.put("|N", wrap(0x63, 0x2B));
		tokens.put("I%", wrap(0x63, 0x2C));
		tokens.put("PV", wrap(0x63, 0x2D));
		tokens.put("PMT", wrap(0x63, 0x2E));
		tokens.put("FV", wrap(0x63, 0x2F));
		tokens.put("|P/Y", wrap(0x63, 0x30));
		tokens.put("|C/Y", wrap(0x63, 0x31));
		tokens.put("w(nMin)", wrap(0x63, 0x32));
		tokens.put("Zw(nMin)", wrap(0x63, 0x33));
		tokens.put("PlotStep", wrap(0x63, 0x34));
		tokens.put("ZPlotStep", wrap(0x63, 0x35));
		tokens.put("Xres", wrap(0x63, 0x36));
		tokens.put("ZXres", wrap(0x63, 0x37));
		tokens.put("Radian", wrap(0x64));
		tokens.put("Degree", wrap(0x65));
		tokens.put("Normal", wrap(0x66));
		tokens.put("Sci", wrap(0x67));
		tokens.put("Eng", wrap(0x68));
		tokens.put("Float", wrap(0x69));
		tokens.put("=", wrap(0x6A));
		tokens.put("<", wrap(0x6B));
		tokens.put(">", wrap(0x6C));
		tokens.put("=", wrap(0x6D));
		tokens.put("=", wrap(0x6E));
		tokens.put("?", wrap(0x6F));
		tokens.put("+", wrap(0x70));
		tokens.put("-", wrap(0x71));
		tokens.put("Ans", wrap(0x72));
		tokens.put("Fix ", wrap(0x73));
		tokens.put("Horiz", wrap(0x74));
		tokens.put("Full", wrap(0x75));
		tokens.put("Func", wrap(0x76));
		tokens.put("Param", wrap(0x77));
		tokens.put("Polar", wrap(0x78));
		tokens.put("Seq", wrap(0x79));
		tokens.put("IndpntAuto", wrap(0x7A));
		tokens.put("IndpntAsk", wrap(0x7B));
		tokens.put("DependAuto", wrap(0x7C));
		tokens.put("DependAsk", wrap(0x7D));

		tokens.put("Sequential", wrap(0x7E, 0x00));
		tokens.put("Simul", wrap(0x7E, 0x01));
		tokens.put("PolarGC", wrap(0x7E, 0x02));
		tokens.put("RectGC", wrap(0x7E, 0x03));
		tokens.put("CoordOn", wrap(0x7E, 0x04));
		tokens.put("CoordOff", wrap(0x7E, 0x05));
		tokens.put("Thick", wrap(0x7E, 0x06));
		tokens.put("Dot-Thick", wrap(0x7E, 0x07));
		tokens.put("AxesOn", wrap(0x7E, 0x08));
		tokens.put("AxesOff", wrap(0x7E, 0x09));
		tokens.put("GridDot ", wrap(0x7E, 0x0A));
		tokens.put("GridOff", wrap(0x7E, 0x0B));
		tokens.put("LabelOn", wrap(0x7E, 0x0C));
		tokens.put("LabelOff", wrap(0x7E, 0x0D));
		tokens.put("Web", wrap(0x7E, 0x0E));
		tokens.put("Time", wrap(0x7E, 0x0F));
		tokens.put("uvAxes", wrap(0x7E, 0x10));
		tokens.put("vwAxes", wrap(0x7E, 0x11));
		tokens.put("uwAxes", wrap(0x7E, 0x12));
		tokens.put("plotsquare", wrap(0x7F));
		tokens.put("?", wrap(0x80));
		tokens.put("·", wrap(0x81));
		tokens.put("*", wrap(0x82));
		tokens.put("/", wrap(0x83));
		tokens.put("Trace", wrap(0x84));
		tokens.put("ClrDraw", wrap(0x85));
		tokens.put("ZStandard", wrap(0x86));
		tokens.put("ZTrig", wrap(0x87));
		tokens.put("ZBox", wrap(0x88));
		tokens.put("Zoom In", wrap(0x89));
		tokens.put("Zoom Out", wrap(0x8A));
		tokens.put("ZSquare", wrap(0x8B));
		tokens.put("ZInteger", wrap(0x8C));
		tokens.put("ZPrevious", wrap(0x8D));
		tokens.put("ZDecimal", wrap(0x8E));
		tokens.put("ZoomStat", wrap(0x8F));
		tokens.put("ZoomRcl", wrap(0x90));
		tokens.put("PrintScreen", wrap(0x91));
		tokens.put("ZoomSto", wrap(0x92));
		tokens.put("Text(", wrap(0x93));
		tokens.put(" nPr ", wrap(0x94));
		tokens.put(" nCr ", wrap(0x95));
		tokens.put("FnOn ", wrap(0x96));
		tokens.put("FnOff ", wrap(0x97));
		tokens.put("StorePic ", wrap(0x98));
		tokens.put("RecallPic ", wrap(0x99));
		tokens.put("StoreGDB ", wrap(0x9A));
		tokens.put("RecallGDB ", wrap(0x9B));
		tokens.put("Line(", wrap(0x9C));
		tokens.put("Vertical ", wrap(0x9D));
		tokens.put("Pt-On(", wrap(0x9E));
		tokens.put("Pt-Off(", wrap(0x9F));
		tokens.put("Pt-Change(", wrap(0xA0));
		tokens.put("Pxl-On(", wrap(0xA1));
		tokens.put("Pxl-Off(", wrap(0xA2));
		tokens.put("Pxl-Change(", wrap(0xA3));
		tokens.put("Shade(", wrap(0xA4));
		tokens.put("Circle(", wrap(0xA5));
		tokens.put("Horizontal ", wrap(0xA6));
		tokens.put("Tangent(", wrap(0xA7));
		tokens.put("DrawInv ", wrap(0xA8));
		tokens.put("DrawF ", wrap(0xA9));

		tokens.put("Str1", wrap(0xAA, 0x00));
		tokens.put("Str2", wrap(0xAA, 0x01));
		tokens.put("Str3", wrap(0xAA, 0x02));
		tokens.put("Str4", wrap(0xAA, 0x03));
		tokens.put("Str5", wrap(0xAA, 0x04));
		tokens.put("Str6", wrap(0xAA, 0x05));
		tokens.put("Str7", wrap(0xAA, 0x06));
		tokens.put("Str8", wrap(0xAA, 0x07));
		tokens.put("Str9", wrap(0xAA, 0x08));
		tokens.put("Str0", wrap(0xAA, 0x09));
		tokens.put("rand", wrap(0xAB));
		tokens.put("p", wrap(0xAC));
		tokens.put("getKey", wrap(0xAD));
		tokens.put("'", wrap(0xAE));
		tokens.put("?", wrap(0xAF));
		tokens.put("?", wrap(0xB0));
		tokens.put("int(", wrap(0xB1));
		tokens.put("abs(", wrap(0xB2));
		tokens.put("det(", wrap(0xB3));
		tokens.put("ReadLine(", wrap(0xB3, 0x30));
		tokens.put("ReplaceLine(", wrap(0xB3, 0x31));

		tokens.put("BufSpriteSelect(", wrap(0xB3, 0x31, 0x30, 0x2B));

		tokens.put("ExecArcPrgm(", wrap(0xB3, 0x31, 0x31, 0x2B));

		tokens.put("DispColor(", wrap(0xB3, 0x31, 0x32, 0x2B));
		tokens.put("InsertLine(", wrap(0xB3, 0x32));
		tokens.put("SpecialChars(", wrap(0xB3, 0x33));
		tokens.put("CreateVar(", wrap(0xB3, 0x34));
		tokens.put("ArcUnarcVar(", wrap(0xB3, 0x35));
		tokens.put("DeleteVar(", wrap(0xB3, 0x36));
		tokens.put("DeleteLine(", wrap(0xB3, 0x37));
		tokens.put("VarStatus(", wrap(0xB3, 0x38));

		tokens.put("BufSprite(", wrap(0xB3, 0x39, 0x2B));
		tokens.put("identity(", wrap(0xB4));
		tokens.put("dim(", wrap(0xB5));
		tokens.put("sum(", wrap(0xB6));
		tokens.put("prod(", wrap(0xB7));
		tokens.put("not(", wrap(0xB8));
		tokens.put("iPart(", wrap(0xB9));
		tokens.put("fPart(", wrap(0xBA));

		tokens.put("npv(", wrap(0xBB, 0x00));
		tokens.put("irr(", wrap(0xBB, 0x01));
		tokens.put("bal(", wrap(0xBB, 0x02));
		tokens.put("SPrn(", wrap(0xBB, 0x03));
		tokens.put("SInt(", wrap(0xBB, 0x04));
		tokens.put("?Nom(", wrap(0xBB, 0x05));
		tokens.put("?Eff(", wrap(0xBB, 0x06));
		tokens.put("dbd(", wrap(0xBB, 0x07));
		tokens.put("lcm(", wrap(0xBB, 0x08));
		tokens.put("gcd(", wrap(0xBB, 0x09));
		tokens.put("randInt(", wrap(0xBB, 0x0A));
		tokens.put("randBin(", wrap(0xBB, 0x0B));
		tokens.put("sub(", wrap(0xBB, 0x0C));
		tokens.put("stdDev(", wrap(0xBB, 0x0D));
		tokens.put("variance(", wrap(0xBB, 0x0E));
		tokens.put("inString(", wrap(0xBB, 0x0F));
		tokens.put("normalcdf(", wrap(0xBB, 0x10));
		tokens.put("invNorm(", wrap(0xBB, 0x11));
		tokens.put("tcdf(", wrap(0xBB, 0x12));
		tokens.put("?²cdf(", wrap(0xBB, 0x13));
		tokens.put("Fcdf(", wrap(0xBB, 0x14));
		tokens.put("binompdf(", wrap(0xBB, 0x15));
		tokens.put("binomcdf(", wrap(0xBB, 0x16));
		tokens.put("poissonpdf(", wrap(0xBB, 0x17));
		tokens.put("poissoncdf(", wrap(0xBB, 0x18));
		tokens.put("geometpdf(", wrap(0xBB, 0x19));
		tokens.put("geometcdf(", wrap(0xBB, 0x1A));
		tokens.put("normalpdf(", wrap(0xBB, 0x1B));
		tokens.put("tpdf(", wrap(0xBB, 0x1C));
		tokens.put("?²pdf(", wrap(0xBB, 0x1D));
		tokens.put("Fpdf(", wrap(0xBB, 0x1E));
		tokens.put("randNorm(", wrap(0xBB, 0x1F));
		tokens.put("tvm_Pmt", wrap(0xBB, 0x20));
		tokens.put("tvm_I%", wrap(0xBB, 0x21));
		tokens.put("tvm_PV", wrap(0xBB, 0x22));
		tokens.put("tvm_N", wrap(0xBB, 0x23));
		tokens.put("tvm_FV", wrap(0xBB, 0x24));
		tokens.put("conj(", wrap(0xBB, 0x25));
		tokens.put("real(", wrap(0xBB, 0x26));

		tokens.put("GetxLIBVersion(", wrap(0xBB, 0x26, 0x30, 0x2B, 0x30));

		tokens.put("SetUpGraphics(", wrap(0xBB, 0x26, 0x30, 0x2B, 0x31, 0x2B));

		tokens.put("SetFullResolution(", wrap(0xBB, 0x26, 0x30, 0x2B, 0x31, 0x2B, 0x30, 0x2B));
		tokens.put("SetHalfResolution(", wrap(0xBB, 0x26, 0x30, 0x2B, 0x31, 0x2B, 0x31));

		tokens.put("ChangeSpeed(", wrap(0xBB, 0x26, 0x30, 0x2B, 0x32, 0x2B));
		tokens.put("SetSpeed6MHz(", wrap(0xBB, 0x26, 0x30, 0x2B, 0x32, 0x2B, 0x30));
		tokens.put("SetSpeed15MHz(", wrap(0xBB, 0x26, 0x30, 0x2B, 0x32, 0x2B, 0x31));

		tokens.put("SetupColorMode(", wrap(0xBB, 0x26, 0x30, 0x2B, 0x33, 0x2B));

		tokens.put("GetUservar(", wrap(0xBB, 0x26, 0x31, 0x2B, 0x30, 0x2B));

		tokens.put("SetUservar(", wrap(0xBB, 0x26, 0x31, 0x2B, 0x31, 0x2B));

		tokens.put("AddToUservar(", wrap(0xBB, 0x26, 0x31, 0x2B, 0x32, 0x2B));

		tokens.put("SubFromUservar(", wrap(0xBB, 0x26, 0x31, 0x2B, 0x33, 0x2B));

		tokens.put("GetKeyCheckList(", wrap(0xBB, 0x26, 0x32, 0x2B, 0x30, 0x2B));
		tokens.put("GetKey(", wrap(0xBB, 0x26, 0x32, 0x2B, 0x30, 0x2B, 0x30));

		tokens.put("GetKeyArrows(", wrap(0xBB, 0x26, 0x32, 0x2B, 0x31, 0x2B));

		tokens.put("GetKeyArrowsDiagonals(", wrap(0xBB, 0x26, 0x32, 0x2B, 0x32, 0x2B));

		tokens.put("GetKeyArrowsCheckTile(", wrap(0xBB, 0x26, 0x32, 0x2B, 0x33, 0x2B));

		tokens.put("GetKeyArrowsDiagonalsCheckTile(", wrap(0xBB, 0x26, 0x32, 0x2B, 0x34, 0x2B));

		tokens.put("DrawMapA(", wrap(0xBB, 0x26, 0x33, 0x2B, 0x30, 0x2B));

		tokens.put("DrawMapB(", wrap(0xBB, 0x26, 0x33, 0x2B, 0x31, 0x2B));

		tokens.put("DrawMap_GetTileA(", wrap(0xBB, 0x26, 0x33, 0x2B, 0x32, 0x2B));

		tokens.put("DrawMap_GetTileB(", wrap(0xBB, 0x26, 0x33, 0x2B, 0x33, 0x2B));

		tokens.put("DrawMap_SetTile(", wrap(0xBB, 0x26, 0x33, 0x2B, 0x34, 0x2B));

		tokens.put("DrawMap_ReplaceTile(", wrap(0xBB, 0x26, 0x33, 0x2B, 0x35, 0x2B));

		tokens.put("DrawMap_GetSectionA(", wrap(0xBB, 0x26, 0x33, 0x2B, 0x36, 0x2B));

		tokens.put("DrawMap_GetSectionB(", wrap(0xBB, 0x26, 0x33, 0x2B, 0x37, 0x2B));

		tokens.put("DrawSpriteA(", wrap(0xBB, 0x26, 0x34, 0x2B, 0x30, 0x2B));

		tokens.put("DrawSpriteB(", wrap(0xBB, 0x26, 0x34, 0x2B, 0x31, 0x2B));

		tokens.put("DrawSpriteList8x8A(", wrap(0xBB, 0x26, 0x34, 0x2B, 0x32, 0x2B));

		tokens.put("DrawSpriteList8x8B(", wrap(0xBB, 0x26, 0x34, 0x2B, 0x33, 0x2B));

		tokens.put("DrawSpriteTileBGA(", wrap(0xBB, 0x26, 0x34, 0x2B, 0x34, 0x2B));

		tokens.put("DrawSpriteTileBGB(", wrap(0xBB, 0x26, 0x34, 0x2B, 0x35, 0x2B));

		tokens.put("LoadTilePic(", wrap(0xBB, 0x26, 0x35, 0x2B, 0x30, 0x2B));

		tokens.put("LoadBGPic(", wrap(0xBB, 0x26, 0x35, 0x2B, 0x31, 0x2B));

		tokens.put("DisplayBGPic(", wrap(0xBB, 0x26, 0x35, 0x2B, 0x32, 0x2B));

		tokens.put("DrawPicSectionA(", wrap(0xBB, 0x26, 0x35, 0x2B, 0x33, 0x2B));

		tokens.put("DrawPicSectionB(", wrap(0xBB, 0x26, 0x35, 0x2B, 0x34, 0x2B));

		tokens.put("LoadSingleTile(", wrap(0xBB, 0x26, 0x35, 0x2B, 0x35, 0x2B));

		tokens.put("DrawString(", wrap(0xBB, 0x26, 0x36, 0x2B, 0x30, 0x2B));

		tokens.put("DrawStringValueA(", wrap(0xBB, 0x26, 0x36, 0x2B, 0x31, 0x2B));

		tokens.put("DrawStringValueB(", wrap(0xBB, 0x26, 0x36, 0x2B, 0x32, 0x2B));

		tokens.put("GetPixelA(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x30, 0x2B));

		tokens.put("GetPixelB(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x31, 0x2B));

		tokens.put("InvertFilledRectangle(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x31, 0x30, 0x2B));

		tokens.put("DrawCircle(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x31, 0x31, 0x2B));

		tokens.put("DrawFilledCircle(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x31, 0x32, 0x2B));

		tokens.put("SetPixelA(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x32, 0x2B));

		tokens.put("SetPixelB(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x33, 0x2B));

		tokens.put("InvertPixel(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x34, 0x2B));

		tokens.put("DrawLine(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x35, 0x2B));

		tokens.put("InvertLine(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x36, 0x2B));

		tokens.put("DrawRectangle(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x37, 0x2B));

		tokens.put("InvertRectangle(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x38, 0x2B));

		tokens.put("FillRectangle(", wrap(0xBB, 0x26, 0x37, 0x2B, 0x39, 0x2B));

		tokens.put("GetLCDBuffer(", wrap(0xBB, 0x26, 0x38, 0x2B, 0x30));

		tokens.put("SetLCDBuffer(", wrap(0xBB, 0x26, 0x38, 0x2B, 0x31, 0x2B));

		tokens.put("SetGRAMOffset(", wrap(0xBB, 0x26, 0x38, 0x2B, 0x32, 0x2B));

		tokens.put("GetRand(", wrap(0xBB, 0x26, 0x38, 0x2B, 0x33, 0x2B));
		tokens.put("UpdateLCD(", wrap(0xBB, 0x26, 0x39));
		tokens.put("imag(", wrap(0xBB, 0x27));
		tokens.put("angle(", wrap(0xBB, 0x28));
		tokens.put("cumSum(", wrap(0xBB, 0x29));
		tokens.put("expr(", wrap(0xBB, 0x2A));
		tokens.put("length(", wrap(0xBB, 0x2B));
		tokens.put("DeltaList(", wrap(0xBB, 0x2C));
		tokens.put("ref(", wrap(0xBB, 0x2D));
		tokens.put("rref(", wrap(0xBB, 0x2E));
		tokens.put("?Rect", wrap(0xBB, 0x2F));
		tokens.put("?Polar", wrap(0xBB, 0x30));
		tokens.put("[e]", wrap(0xBB, 0x31));
		tokens.put("SinReg ", wrap(0xBB, 0x32));
		tokens.put("Logistic ", wrap(0xBB, 0x33));
		tokens.put("LinRegTTest ", wrap(0xBB, 0x34));
		tokens.put("ShadeNorm(", wrap(0xBB, 0x35));
		tokens.put("Shade_t(", wrap(0xBB, 0x36));
		tokens.put("Shade?²(", wrap(0xBB, 0x37));
		tokens.put("ShadeF(", wrap(0xBB, 0x38));
		tokens.put("Matr?list(", wrap(0xBB, 0x39));
		tokens.put("List?matr(", wrap(0xBB, 0x3A));
		tokens.put("Z-Test(", wrap(0xBB, 0x3B));
		tokens.put("T-Test ", wrap(0xBB, 0x3C));
		tokens.put("2-SampZTest(", wrap(0xBB, 0x3D));
		tokens.put("1-PropZTest(", wrap(0xBB, 0x3E));
		tokens.put("2-PropZTest(", wrap(0xBB, 0x3F));
		tokens.put("?²-Test(", wrap(0xBB, 0x40));
		tokens.put("ZInterval", wrap(0xBB, 0x41));
		tokens.put("2-SampZInt(", wrap(0xBB, 0x42));
		tokens.put("1-PropZInt(", wrap(0xBB, 0x43));
		tokens.put("2-PropZInt(", wrap(0xBB, 0x44));
		tokens.put("GraphStyle(", wrap(0xBB, 0x45));
		tokens.put("2-SampTTest ", wrap(0xBB, 0x46));
		tokens.put("2-SampFTest ", wrap(0xBB, 0x47));
		tokens.put("TInterval ", wrap(0xBB, 0x48));
		tokens.put("2-SampTInt ", wrap(0xBB, 0x49));
		tokens.put("SetUpEditor ", wrap(0xBB, 0x4A));
		tokens.put("Pmt_End", wrap(0xBB, 0x4B));
		tokens.put("Pmt_Bgn", wrap(0xBB, 0x4C));
		tokens.put("Real", wrap(0xBB, 0x4D));
		tokens.put("re^?i", wrap(0xBB, 0x4E));
		tokens.put("a+bi", wrap(0xBB, 0x4F));
		tokens.put("ExprOn", wrap(0xBB, 0x50));
		tokens.put("ExprOff", wrap(0xBB, 0x51));
		tokens.put("ClrAllLists", wrap(0xBB, 0x52));
		tokens.put("GetCalc(", wrap(0xBB, 0x53));
		tokens.put("DelVar ", wrap(0xBB, 0x54));
		tokens.put("Equ?String(", wrap(0xBB, 0x55));
		tokens.put("String?Equ(", wrap(0xBB, 0x56));
		tokens.put("Clear Entries", wrap(0xBB, 0x57));
		tokens.put("Select(", wrap(0xBB, 0x58));
		tokens.put("ANOVA(", wrap(0xBB, 0x59));
		tokens.put("ModBoxPlot", wrap(0xBB, 0x5A));
		tokens.put("NormProbPlot", wrap(0xBB, 0x5B));
		tokens.put("G-T", wrap(0xBB, 0x64));
		tokens.put("ZoomFit", wrap(0xBB, 0x65));
		tokens.put("DiagnosticOn", wrap(0xBB, 0x66));
		tokens.put("DiagnosticOff", wrap(0xBB, 0x67));
		tokens.put("Archive ", wrap(0xBB, 0x68));
		tokens.put("UnArchive ", wrap(0xBB, 0x69));
		tokens.put("Asm(", wrap(0xBB, 0x6A));
		tokens.put("AsmComp(", wrap(0xBB, 0x6B));
		tokens.put("AsmPrgm", wrap(0xBB, 0x6C));
		tokens.put("Á", wrap(0xBB, 0x6E));
		tokens.put("À", wrap(0xBB, 0x6F));
		tokens.put("Â", wrap(0xBB, 0x70));
		tokens.put("Ä", wrap(0xBB, 0x71));
		tokens.put("á", wrap(0xBB, 0x72));
		tokens.put("à", wrap(0xBB, 0x73));
		tokens.put("â", wrap(0xBB, 0x74));
		tokens.put("ä", wrap(0xBB, 0x75));
		tokens.put("É", wrap(0xBB, 0x76));
		tokens.put("È", wrap(0xBB, 0x77));
		tokens.put("Ê", wrap(0xBB, 0x78));
		tokens.put("Ë", wrap(0xBB, 0x79));
		tokens.put("é", wrap(0xBB, 0x7A));
		tokens.put("è", wrap(0xBB, 0x7B));
		tokens.put("ê", wrap(0xBB, 0x7C));
		tokens.put("ë", wrap(0xBB, 0x7D));
		tokens.put("Ì", wrap(0xBB, 0x7F));
		tokens.put("Î", wrap(0xBB, 0x80));
		tokens.put("Ï", wrap(0xBB, 0x81));
		tokens.put("í", wrap(0xBB, 0x82));
		tokens.put("ì", wrap(0xBB, 0x83));
		tokens.put("î", wrap(0xBB, 0x84));
		tokens.put("ï", wrap(0xBB, 0x85));
		tokens.put("Ó", wrap(0xBB, 0x86));
		tokens.put("Ò", wrap(0xBB, 0x87));
		tokens.put("Ô", wrap(0xBB, 0x88));
		tokens.put("Ö", wrap(0xBB, 0x89));
		tokens.put("ó", wrap(0xBB, 0x8A));
		tokens.put("ò", wrap(0xBB, 0x8B));
		tokens.put("ô", wrap(0xBB, 0x8C));
		tokens.put("ö", wrap(0xBB, 0x8D));
		tokens.put("Ú", wrap(0xBB, 0x8E));
		tokens.put("Ù", wrap(0xBB, 0x8F));
		tokens.put("Û", wrap(0xBB, 0x90));
		tokens.put("Ü", wrap(0xBB, 0x91));
		tokens.put("ú", wrap(0xBB, 0x92));
		tokens.put("ù", wrap(0xBB, 0x93));
		tokens.put("û", wrap(0xBB, 0x94));
		tokens.put("ü", wrap(0xBB, 0x95));
		tokens.put("Ç", wrap(0xBB, 0x96));
		tokens.put("ç", wrap(0xBB, 0x97));
		tokens.put("Ñ", wrap(0xBB, 0x98));
		tokens.put("ñ", wrap(0xBB, 0x99));
		tokens.put("´", wrap(0xBB, 0x9A));
		tokens.put("|`", wrap(0xBB, 0x9B));
		tokens.put("¨", wrap(0xBB, 0x9C));
		tokens.put("¿", wrap(0xBB, 0x9D));
		tokens.put("¡", wrap(0xBB, 0x9E));
		tokens.put("a", wrap(0xBB, 0x9F));
		tokens.put("ß", wrap(0xBB, 0xA0));
		tokens.put("?", wrap(0xBB, 0xA1));
		tokens.put("?", wrap(0xBB, 0xA2));
		tokens.put("d", wrap(0xBB, 0xA3));
		tokens.put("e", wrap(0xBB, 0xA4));
		tokens.put("?", wrap(0xBB, 0xA5));
		tokens.put("µ", wrap(0xBB, 0xA6));
		tokens.put("|p", wrap(0xBB, 0xA7));
		tokens.put("?", wrap(0xBB, 0xA8));
		tokens.put("S", wrap(0xBB, 0xA9));
		tokens.put("F", wrap(0xBB, 0xAB));
		tokens.put("O", wrap(0xBB, 0xAC));
		tokens.put("?", wrap(0xBB, 0xAD));
		tokens.put("?", wrap(0xBB, 0xAE));
		tokens.put("|F", wrap(0xBB, 0xAF));
		tokens.put("a", wrap(0xBB, 0xB0));
		tokens.put("b", wrap(0xBB, 0xB1));
		tokens.put("c", wrap(0xBB, 0xB2));
		tokens.put("d", wrap(0xBB, 0xB3));
		tokens.put("e", wrap(0xBB, 0xB4));
		tokens.put("f", wrap(0xBB, 0xB5));
		tokens.put("g", wrap(0xBB, 0xB6));
		tokens.put("h", wrap(0xBB, 0xB7));
		tokens.put("i", wrap(0xBB, 0xB8));
		tokens.put("j", wrap(0xBB, 0xB9));
		tokens.put("k", wrap(0xBB, 0xBA));
		tokens.put("l", wrap(0xBB, 0xBC));
		tokens.put("m", wrap(0xBB, 0xBD));
		tokens.put("n", wrap(0xBB, 0xBE));
		tokens.put("o", wrap(0xBB, 0xBF));
		tokens.put("p", wrap(0xBB, 0xC0));
		tokens.put("q", wrap(0xBB, 0xC1));
		tokens.put("r", wrap(0xBB, 0xC2));
		tokens.put("s", wrap(0xBB, 0xC3));
		tokens.put("t", wrap(0xBB, 0xC4));
		tokens.put("u", wrap(0xBB, 0xC5));
		tokens.put("v", wrap(0xBB, 0xC6));
		tokens.put("w", wrap(0xBB, 0xC7));
		tokens.put("x", wrap(0xBB, 0xC8));
		tokens.put("y", wrap(0xBB, 0xC9));
		tokens.put("z", wrap(0xBB, 0xCA));
		tokens.put("s", wrap(0xBB, 0xCB));
		tokens.put("t", wrap(0xBB, 0xCC));
		tokens.put("Í", wrap(0xBB, 0xCD));
		tokens.put("GarbageCollect", wrap(0xBB, 0xCE));
		tokens.put("|~", wrap(0xBB, 0xCF));
		tokens.put("@", wrap(0xBB, 0xD1));
		tokens.put("#", wrap(0xBB, 0xD2));
		tokens.put("$", wrap(0xBB, 0xD3));
		tokens.put("&", wrap(0xBB, 0xD4));
		tokens.put("`", wrap(0xBB, 0xD5));
		tokens.put(";", wrap(0xBB, 0xD6));
		tokens.put("\\", wrap(0xBB, 0xD7));
		tokens.put("|", wrap(0xBB, 0xD8));
		tokens.put("_", wrap(0xBB, 0xD9));
		tokens.put("%", wrap(0xBB, 0xDA));
		tokens.put("…", wrap(0xBB, 0xDB));
		tokens.put("?", wrap(0xBB, 0xDC));
		tokens.put("ß", wrap(0xBB, 0xDD));
		tokens.put("?", wrap(0xBB, 0xDE));
		tokens.put("?", wrap(0xBB, 0xDF));
		
		//TODO find solution
		/*
		tokens.put("0", wrap(0xBB, 0xE0));
		tokens.put("1", wrap(0xBB, 0xE1));
		tokens.put("2", wrap(0xBB, 0xE2));
		tokens.put("3", wrap(0xBB, 0xE3));
		tokens.put("4", wrap(0xBB, 0xE4));
		tokens.put("5", wrap(0xBB, 0xE5));
		tokens.put("6", wrap(0xBB, 0xE6));
		tokens.put("7", wrap(0xBB, 0xE7));
		tokens.put("8", wrap(0xBB, 0xE8));
		tokens.put("9", wrap(0xBB, 0xE9));
		tokens.put("10", wrap(0xBB, 0xEA));
		*/
		
		tokens.put("?", wrap(0xBB, 0xEB));
		tokens.put("?", wrap(0xBB, 0xEC));
		tokens.put("?", wrap(0xBB, 0xED));
		tokens.put("?", wrap(0xBB, 0xEE));
		tokens.put("×", wrap(0xBB, 0xF0));
		tokens.put("?", wrap(0xBB, 0xF1));
		tokens.put("bolduparrow", wrap(0xBB, 0xF2));
		tokens.put("bolddownarrow", wrap(0xBB, 0xF3));
		tokens.put("v", wrap(0xBB, 0xF4));
		tokens.put("invertedequal", wrap(0xBB, 0xF5));
		tokens.put("v(", wrap(0xBC));
		tokens.put("³v(", wrap(0xBD));
		tokens.put("ln(", wrap(0xBE));
		tokens.put("e^(", wrap(0xBF));
		tokens.put("log(", wrap(0xC0));
		tokens.put("10^(", wrap(0xC1));
		tokens.put("sin(", wrap(0xC2));
		tokens.put("sin?¹(", wrap(0xC3));
		tokens.put("cos(", wrap(0xC4));
		tokens.put("cos?¹(", wrap(0xC5));
		tokens.put("tan(", wrap(0xC6));
		tokens.put("tan?¹(", wrap(0xC7));
		tokens.put("sinh(", wrap(0xC8));
		tokens.put("sinh?¹(", wrap(0xC9));
		tokens.put("cosh(", wrap(0xCA));
		tokens.put("soch?¹(", wrap(0xCB));
		tokens.put("tanh(", wrap(0xCC));
		tokens.put("tanh?¹(", wrap(0xCD));
		tokens.put("If ", wrap(0xCE));
		tokens.put("Then", wrap(0xCF));
		tokens.put("Else", wrap(0xD0));
		tokens.put("While ", wrap(0xD1));
		tokens.put("Repeat ", wrap(0xD2));
		tokens.put("For(", wrap(0xD3));
		tokens.put("End", wrap(0xD4));
		tokens.put("Return", wrap(0xD5));
		tokens.put("Lbl ", wrap(0xD6));
		tokens.put("Goto ", wrap(0xD7));
		tokens.put("Pause ", wrap(0xD8));
		tokens.put("Stop", wrap(0xD9));
		tokens.put("IS>(", wrap(0xDA));
		tokens.put("DS<(", wrap(0xDB));
		tokens.put("Input ", wrap(0xDC));
		tokens.put("Prompt ", wrap(0xDD));
		tokens.put("Disp ", wrap(0xDE));
		tokens.put("DispGraph", wrap(0xDF));
		tokens.put("Output(", wrap(0xE0));
		tokens.put("ClrHome", wrap(0xE1));
		tokens.put("Fill(", wrap(0xE2));
		tokens.put("SortA(", wrap(0xE3));
		tokens.put("SortD(", wrap(0xE4));
		tokens.put("DispTable", wrap(0xE5));
		tokens.put("Menu(", wrap(0xE6));
		tokens.put("Send(", wrap(0xE7));
		tokens.put("Get(", wrap(0xE8));
		tokens.put("PlotsOn ", wrap(0xE9));
		tokens.put("PlotsOff ", wrap(0xEA));
		tokens.put("?", wrap(0xEB));
		tokens.put("Plot1(", wrap(0xEC));
		tokens.put("Plot2(", wrap(0xED));
		tokens.put("Plot3(", wrap(0xEE));

		tokens.put("setDate(", wrap(0xEF, 0x00));
		tokens.put("setTime(", wrap(0xEF, 0x01));
		tokens.put("checkTmr(", wrap(0xEF, 0x02));
		tokens.put("setDtFmt(", wrap(0xEF, 0x03));
		tokens.put("setTmFmt(", wrap(0xEF, 0x04));
		tokens.put("timeCnv(", wrap(0xEF, 0x05));
		tokens.put("dayOfWk(", wrap(0xEF, 0x06));
		tokens.put("getDtStr", wrap(0xEF, 0x07));
		tokens.put("getTmStr(", wrap(0xEF, 0x08));
		tokens.put("getDate", wrap(0xEF, 0x09));
		tokens.put("getTime", wrap(0xEF, 0x0A));
		tokens.put("startTmr", wrap(0xEF, 0x0B));
		tokens.put("getDtFmt", wrap(0xEF, 0x0C));
		tokens.put("getTmFmt", wrap(0xEF, 0x0D));
		tokens.put("isClockOn", wrap(0xEF, 0x0E));
		tokens.put("ClockOff", wrap(0xEF, 0x0F));
		tokens.put("ClockOn", wrap(0xEF, 0x10));
		tokens.put("OpenLib(", wrap(0xEF, 0x11));
		tokens.put("ExecLib", wrap(0xEF, 0x12));
		tokens.put("invT(", wrap(0xEF, 0x13));
		tokens.put("?²GOF-Test(", wrap(0xEF, 0x14));
		tokens.put("LinRegTInt ", wrap(0xEF, 0x15));
		tokens.put("Manual-Fit ", wrap(0xEF, 0x16));
		tokens.put("ZQuadrant1", wrap(0xEF, 0x17));
		tokens.put("ZFrac1/2", wrap(0xEF, 0x18));
		tokens.put("ZFrac1/3", wrap(0xEF, 0x19));
		tokens.put("ZFrac1/4", wrap(0xEF, 0x1A));
		tokens.put("ZFrac1/5", wrap(0xEF, 0x1B));
		tokens.put("ZFrac1/8", wrap(0xEF, 0x1C));
		tokens.put("ZFrac1/10", wrap(0xEF, 0x1D));
		tokens.put("mathprintbox", wrap(0xEF, 0x1E));
		tokens.put("/", wrap(0xEF, 0x2E));
		tokens.put("?", wrap(0xEF, 0x2F));
		tokens.put("?n/d??Un/d", wrap(0xEF, 0x30));
		tokens.put("?F??D", wrap(0xEF, 0x31));
		tokens.put("remainder(", wrap(0xEF, 0x32));
		tokens.put("S(", wrap(0xEF, 0x33));
		tokens.put("logBASE(", wrap(0xEF, 0x34));
		tokens.put("randIntNoRep(", wrap(0xEF, 0x35));
		tokens.put("[MATHPRINT]", wrap(0xEF, 0x37));
		tokens.put("[CLASSIC]", wrap(0xEF, 0x38));
		tokens.put("n/d", wrap(0xEF, 0x39));
		tokens.put("Un/d", wrap(0xEF, 0x3A));
		tokens.put("[AUTO]", wrap(0xEF, 0x3B));
		tokens.put("[DEC]", wrap(0xEF, 0x3C));
		tokens.put("[FRAC]", wrap(0xEF, 0x3D));
		tokens.put("BLUE", wrap(0xEF, 0x41));
		tokens.put("RED", wrap(0xEF, 0x42));
		tokens.put("BLACK", wrap(0xEF, 0x43));
		tokens.put("MAGENTA", wrap(0xEF, 0x44));
		tokens.put("GREEN", wrap(0xEF, 0x45));
		tokens.put("ORANGE", wrap(0xEF, 0x46));
		tokens.put("BROWN", wrap(0xEF, 0x47));
		tokens.put("NAVY", wrap(0xEF, 0x48));
		tokens.put("LTBLUE", wrap(0xEF, 0x49));
		tokens.put("YELLOW", wrap(0xEF, 0x4A));
		tokens.put("WHITE", wrap(0xEF, 0x4B));
		tokens.put("LTGREY", wrap(0xEF, 0x4C));
		tokens.put("MEDGREY", wrap(0xEF, 0x4D));
		tokens.put("GREY", wrap(0xEF, 0x4E));
		tokens.put("DARKGREY", wrap(0xEF, 0x4F));
		tokens.put("GridLine ", wrap(0xEF, 0x5A));
		tokens.put("BackgroundOn ", wrap(0xEF, 0x5B));
		tokens.put("DetectAsymOn", wrap(0xEF, 0x6A));
		tokens.put("DetectAsymOff", wrap(0xEF, 0x6B));
		tokens.put("BackgroundOff", wrap(0xEF, 0x64));
		tokens.put("GraphColor(", wrap(0xEF, 0x65));
		tokens.put("TextColor(", wrap(0xEF, 0x67));
		tokens.put("Asm84CPrgm", wrap(0xEF, 0x68));
		tokens.put("BorderColor ", wrap(0xEF, 0x6C));
		tokens.put("tinydotplot", wrap(0xEF, 0x73));
		tokens.put("Thin", wrap(0xEF, 0x74));
		tokens.put("Dot-Thin", wrap(0xEF, 0x75));
		tokens.put("^", wrap(0xF0));
		tokens.put("?v", wrap(0xF1));
		tokens.put("1-Var Stats ", wrap(0xF2));
		tokens.put("2-Var Stats", wrap(0xF3));
		tokens.put("LinReg(a+bx) ", wrap(0xF4));
		tokens.put("ExpReg ", wrap(0xF5));
		tokens.put("LnReg ", wrap(0xF6));
		tokens.put("PwrReg ", wrap(0xF7));
		tokens.put("Med-Med ", wrap(0xF8));
		tokens.put("QuadReg ", wrap(0xF9));
		tokens.put("ClrList ", wrap(0xFA));
		tokens.put("ClrTable", wrap(0xFB));
		tokens.put("Histogram", wrap(0xFC));
		tokens.put("xyLine", wrap(0xFD));
		tokens.put("Scatter", wrap(0xFE));
		tokens.put("LinReg(ax+b) ", wrap(0xFF));

	}

	private ByteArrayWrapper wrap(int... is)
	{
		byte[] bs = new byte[is.length];
		for (int i = 0; i < is.length; ++i)
		{
			bs[i] = (byte) is[i];
		}
		return new ByteArrayWrapper(bs);
	}

}


public class UnicodeConverter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String toConvertTo32 = "\ud83d\ude00";
		System.out.print("16 -> 32: " + convert16to32(toConvertTo32));
		
		String toConvertTo16 = "0x1f602";
		System.out.println("32 -> 16: "+ convert32to16(toConvertTo16));
				
		//System.out.println(new String(Character.toChars(Integer.decode("0x1f600"))));

	}
	
	public static String convert16to32(String toConvert){
		for (int i = 0; i < toConvert.length(); ) {
	        int codePoint = Character.codePointAt(toConvert, i);
	        i += Character.charCount(codePoint);
	        String utf32 = String.format("0x%x%n", codePoint);
	        return utf32;
	    }
		return "Error";
	}
	
	public static String convert32to16(String utf32){
		int utf32i = Integer.decode(utf32);
		
		return new String(Character.toChars(utf32i));
	}

}

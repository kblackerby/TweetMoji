package displaytweets;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bukunmi on 3/17/2016.
 * Modified by Kenan on 4/6/2016 for GUI use.
 */
public class GUI_UnicodeEmoji {

    /**
     * The method checks the string pattern of the tweet and returns the emoji code
     * @param inputCode
     * @return ArrayList <String>
     */
    public static ArrayList<String> identifyEmojiCode(String inputCode) throws IOException {
//        System.out.println("Original Input Code: "+inputCode);

        /**
         * Regular Expression checking for emojis, considering the different ranges of unicode representation of these emojis
         * 1. Emoticons +
         * 2. Dingbats+
         * 3. Transport and map symbols+
         * 4. Enclosed characters
         * 5. Additional emoticons
         * 6. Additional transport and map symbols
         * 7.  Regional Locations
         * 8.  Fitzpatrick Type
         * 9. Other symbols
         */
        String regionalIndicator = "[\uD83C\uDDE6-\uD83C\uDDFF]"; //Regional Indicator
        String fitzpatrickSupport = "[\uD83C\uDFFB-\uD83C\uDFFF]";  //Fitzparick Type 1&2-6-6*/
        String charCombo = "\u20E3"; //Combining Diacritical
        String variationSelector = "\uFE0F"; //Variation Selector
        String heirarchy = "\u200D"; //Heirarchical

        String regexPattern = "[\uD83D\uDE01-\uD83D\uDE4F]|" + //Emoticons
                "[\u2702-\u27B0]|" + //Dingbats
                "[\uD83D\uDE80-\uD83D\uDEF3]|" + //Transport and Map
                "[\uD83C\uDF00-\uD83C\uDFFA]|" + //Miscellaneous Symbols and Pictographs1
                "[\uD83D\uDC00-\uD83D\uDDFF]|" + //Miscellaneous Symbols and Pictographs2
                "[\u2600-\u26FF]|" + //Miscellaneous Symbols
                "[\u23E9-\u23FA]|" + //Miscellaneous Technical
                "\u231A|\u231B|\u2328|" +
                "[\u25AA-\u25FE]|" + //Geometric Shapes
                "[\u2B01-\u2BD1]|" + //Miscellaneous Symbols and Arrows
                "[\u2122-\u2139]|" + //Arrows ++
                "[\u2194-\u29AB]|" + //Arrows
                "[\u0023-\u0039]"+charCombo+"|[\u0023-\u0039]"+variationSelector+charCombo+"|" + //Basic Latin
                "[\uD83C\uDD70-\uD83C\uDD9A]|\u24C2|" + //Enclosed Alphanumeric Supplement
                "[\uD83C\uDE00-\uD83C\uDE51]|\u3297|\u3299|" + //Enclosed Ideographic Supplement
                "[\uD83D\uDE00-\uD83D\uDE81]|" + //Additional emoticon
                "[\uD83E\uDD10-\uD83E\uDDE2]|" + //most recent and expected range
                regionalIndicator +"|"+ fitzpatrickSupport+"|"+ heirarchy +"|"+ variationSelector ;

        String stringIn = "";
        try {
            byte[] utf16 = inputCode.getBytes("UTF-16");
            stringIn = new String(utf16, "UTF-16");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(stringIn);

        ArrayList<String> matchList = new ArrayList<>();
        
        int addBuffer = 0; // index of current character in input string
        // separate the input text into text and emojis
        while (matcher.find()) {
            if(matcher.start() == addBuffer) {
                matchList.add(matcher.group());
                addBuffer = matcher.end();
            } else {
                matchList.add("TEXT" + stringIn.substring(addBuffer, matcher.start()));
                matchList.add(matcher.group());
                addBuffer = matcher.end();
            }
        }
        if (matchList.isEmpty()) {
            matchList.add("TEXT" + stringIn);
        }
            
        return getCorrectEmojiList(regionalIndicator, charCombo, variationSelector, heirarchy, fitzpatrickSupport, matchList);
        
        
    }

    public static ArrayList<String> getCorrectEmojiList(String regionalIndicator, String charCombo, String variationSelector, String heirarchy, String fitzpatrickSupport, ArrayList<String> matchList) throws IOException {
        /**
         * sortUnicode sorts and combine hierarchical emoji unicode
         */
        sortUnicodeList(regionalIndicator, heirarchy, charCombo, variationSelector, fitzpatrickSupport, matchList);

        for(int i=0;i<matchList.size();i++) {
            if ((!(matchList.get(i).startsWith("U+") || matchList.get(i).startsWith("TEXT")))) {
                matchList.set(i, "U+" + Long.toHexString(matchList.get(i).codePointAt(0)));
            }
        }

        /*int i=1;
        for (String uniCode : uniCodeList) {
            System.out.println(i++ + ": " + uniCode);
        }*/
//        System.out.println("There exist "+matchList.size()+" Emojis and Text Snippets in this Tweet");
        
        
//        for (int i = 0; i < matchList.size(); i++) {
//			System.out.println(matchList.get(i));
//		}

        return matchList;
    }

    /**
     * The method identifies multiple-emoji-unicode representations and resize array gotten from the inputed String
     * @param regionalIndicator
     * @param heirarchy
     * @param charCombo
     * @param variationSelector
     * @param matchListFormat
     * @return
     */
    public static ArrayList<String> sortUnicodeList(String regionalIndicator, String hierarchy, String charCombo,
                                                    String variationSelector, String fitzpatrickSupport, ArrayList<String> matchListFormat) {
        for(int i=0;i<matchListFormat.size();i++) {

            //matchlist is Character Diacritical
            if (matchListFormat.get(i).endsWith(variationSelector+charCombo)) {
                matchListFormat.set(i, "U+00" + Long.toHexString(matchListFormat.get(i).codePointAt(0))
                        + "U+" + Long.toHexString(variationSelector.codePointAt(0)) + "U+" + Long.toHexString(charCombo.codePointAt(0)));
            } else if (matchListFormat.get(i).endsWith(charCombo)) {
                matchListFormat.set(i, "U+00" + Long.toHexString(matchListFormat.get(i).codePointAt(0))
                        + "U+" + Long.toHexString(charCombo.codePointAt(0)));
            }
            //if the only value only in the array list is hierarchy
            if(matchListFormat.size() == 1 && matchListFormat.get(i).equals(hierarchy)){
                matchListFormat.remove(i);
            }
            //matchlist is of Hierarchy
            if (matchListFormat.get(i).matches(hierarchy) && !(matchListFormat.get(i+1).isEmpty())) {

                //if the previous item does not start with U+ add U+
                if (!(matchListFormat.get(i-1).startsWith("U+"))){
                    matchListFormat.set(i-1, "U+" + Long.toHexString(matchListFormat.get(i-1).codePointAt(0))
                            + "U+" + Long.toHexString(matchListFormat.get(i).codePointAt(0))
                            + "U+" + Long.toHexString(matchListFormat.get(i+1).codePointAt(0)));
                }
                else {
                    matchListFormat.set(i-1, matchListFormat.get(i-1)
                            + "U+" + Long.toHexString(matchListFormat.get(i).codePointAt(0))
                            + "U+" + Long.toHexString(matchListFormat.get(i+1).codePointAt(0)));
                }
                matchListFormat.remove(i+1);
                matchListFormat.remove(i);
                i--;

                //variation Selector combines couples
                if (matchListFormat.get(i).toUpperCase().startsWith("U+FE0F") && matchListFormat.get(i+1).equals(hierarchy)
                        && !(matchListFormat.get(i+1).isEmpty())) {
                    matchListFormat.set(i-1, matchListFormat.get(i-1) +matchListFormat.get(i));
                    matchListFormat.remove(i);
                    i--;
                }
            }

            //matchlist is related to a regional indicator
            if (matchListFormat.get(i).matches(regionalIndicator)
                    && matchListFormat.get(i+1).matches(regionalIndicator) && !(matchListFormat.get(i+1).isEmpty())) {
                matchListFormat.set(i, "U+" + Long.toHexString(matchListFormat.get(i).codePointAt(0))
                        + "U+" + Long.toHexString(matchListFormat.get(i+1).codePointAt(0)));
                matchListFormat.remove(i+1);
            }

            //Remove unnecessary variationSelector
            if (matchListFormat.get(i).matches(variationSelector) && (i == (matchListFormat.size()-1))){
                i--;
                matchListFormat.remove(i+1);
            } else if(matchListFormat.get(i).matches(variationSelector) && !(matchListFormat.get(i+1).matches(hierarchy))){
                    i--;
                    matchListFormat.remove(i+1);
            }
            
            // matchlist is fitzpatrick (NOT DISPLAYING FITZPATRICK, THE ORIGINAL IS USED FOR TIME SAKE)
            if (matchListFormat.get(i).matches(fitzpatrickSupport)) {
                i--;
                matchListFormat.remove(i+1);
            }
        }
        return matchListFormat;
    }
}

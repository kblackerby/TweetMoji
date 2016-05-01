package emojireader;

import sentimentanalysis.SentimentRankAssignment;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bukunmi on 3/17/2016.
 */
public class UnicodeEmojiSampler {

    /**
     * The method checks the string pattern of the tweet and returns the emoji code
     * @param inputCode
     * @return ArrayList<String> matchedList
     */
    public static ArrayList<String> identifyEmojiCode(String inputCode) {
        System.out.println("Original Input String to retrieve emojis: "+inputCode +"\n");

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
        String fitzpatrickSupport = "[\uD83C\uDFFB-\uD83C\uDFFF]";  //Fitzparick Type 1&2-6*/
        String charCombo = "\u20E3"; //Combining Diacritical
        String variationSelector = "\uFE0F"; //Variation Selector used in hierarchical and character combinations
        String hierarchy = "\u200D"; //Hierarchical - used in compound emoji such as Family, Kiss etc

        //Unicode Patterns to match that represent Emojis
        //Regular expression sampler idea gotten from stackoverflow -
        // http://stackoverflow.com/questions/24840667/what-is-the-regex-to-extract-all-the-emojis-from-a-string
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
                "\u203c|\u2049" +
                "[\u2194-\u29AB]|" + //Arrows
                "[\u0023-\u0039]"+charCombo+"|[\u0023-\u0039]"+variationSelector+charCombo+"|" + //Basic Latin + char combo
                "[\uD83C\uDD70-\uD83C\uDD9A]|\u24C2|" + //Enclosed Alphanumeric Supplement
                "[\uD83C\uDE00-\uD83C\uDE51]|\u3297|\u3299|" + //Enclosed Ideographic Supplement
                "[\uD83D\uDE00-\uD83D\uDE81]|" + //Additional emoticon
                "[\uD83E\uDD10-\uD83E\uDDE2]|" + //most recent and expected range
                regionalIndicator +"|"+ fitzpatrickSupport+"|"+ hierarchy +"|"+ variationSelector ;

        String string1 = null;
        try {
            byte[] utf16 = inputCode.getBytes("UTF-16"); //encoding of inputted text set
            string1 = new String(utf16, "UTF-16");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Pattern pattern = Pattern.compile(regexPattern);
        assert string1 != null;
        Matcher matcher = pattern.matcher(string1);
        ArrayList<String> matchList = new ArrayList<>();

        while (matcher.find()) {
            matchList.add(matcher.group());

        }
        //matched emojis are passed into getCorrectEmojiList(...) return
        return getCorrectEmojiList(regionalIndicator, charCombo, variationSelector, hierarchy, matchList);
    }

    /**
     * Convert all other emoji Unicodes to 32 bits from 16 bits
     * @param regionalIndicator
     * @param charCombo
     * @param variationSelector
     * @param heirarchy
     * @param emojiList
     * @return ArrayList<String> emojiList
     */
    private static ArrayList<String> getCorrectEmojiList
    (String regionalIndicator, String charCombo, String variationSelector, String heirarchy, ArrayList<String> emojiList) {

        //sortUnicodeList(...) sorts and combine hierarchical emoji unicode
        ArrayList<String> uniCodeList = sortUnicodeList(regionalIndicator, heirarchy, charCombo, variationSelector, emojiList);

        assert uniCodeList != null;
        for(int i = 0; i<uniCodeList.size(); i++) {

            //change all other input 16-bits to 32 and affix " U+" prefix
            if (!(uniCodeList.get(i).startsWith("U+"))) {
                uniCodeList.set(i, "U+" + Long.toHexString(uniCodeList.get(i).codePointAt(0)));
            }
        }
        //displays Emoji List with description from Arraylist
        //EmojiDataAccess.showAllEmojiFromList(emojiList);
        System.out.println("There exist " + emojiList.size() + " Emoji(s) Similar Unicodes in this Tweet");

        //return 32 bit codes
        return emojiList;
    }

    /**
     * The method identifies multiple-layered Unicode representations and resize ArrayList gotten from the inputted String
     * It first change these codes in the ArrayList from 16-bits to 32, affix " U+" prefix then combines those that them.
     * It leaves all other emojis for
     * @param regionalIndicator
     * @param hierarchy
     * @param charCombo
     * @param variationSelector
     * @param matchListFormat
     * @return
     */
    private static ArrayList<String> sortUnicodeList(String regionalIndicator, String hierarchy, String charCombo,
                                                     String variationSelector, ArrayList<String> matchListFormat) {
        for(int i=0;i<matchListFormat.size();i++) {

            //matchlist ends with Character Diacritical, convert the string(which at codePointAt(0)) is for numeric figures
            //append the charCombo afterwards
            //this pays no mind to codes with variationSelector
            if (matchListFormat.get(i).endsWith(charCombo)) {
                matchListFormat.set(i, "U+00" + Long.toHexString(matchListFormat.get(i).codePointAt(0))
                        + " U+" + Long.toHexString(charCombo.codePointAt(0)));
            }

            //if the only value only in the array list is hierarchy
            if(matchListFormat.size()-1 == 0 && matchListFormat.get(i).equals(hierarchy)){
                matchListFormat.remove(0);
                return null;
            }

            //matchlist is of Hierarchical
            if (matchListFormat.get(i).matches(hierarchy)
                    && matchListFormat.size() != 1
                    && !(matchListFormat.get(i+1).isEmpty())) {

                //if the previous item does not start with "U+" add U+ before combining,
                // combine items above and below and this item into the array item above the  Hierarchy
                if (!(matchListFormat.get(i-1).startsWith("U+"))){
                    matchListFormat.set(i-1, "U+" + Long.toHexString(matchListFormat.get(i-1).codePointAt(0))
                            + " U+" + Long.toHexString(matchListFormat.get(i).codePointAt(0))
                            + " U+" + Long.toHexString(matchListFormat.get(i+1).codePointAt(0)));
                }
                else {
                    //change current item and the one after it the combine said items into the previous item
                    matchListFormat.set(i-1, matchListFormat.get(i-1)
                            + " U+" + Long.toHexString(matchListFormat.get(i).codePointAt(0))
                            + " U+" + Long.toHexString(matchListFormat.get(i+1).codePointAt(0)));
                }
                //remove the array items combined into another item and set the i to that item index
                matchListFormat.remove(i+1);
                matchListFormat.remove(i);
                 i--;

                //variation Selector combines used for in the couple emojis
                if (matchListFormat.get(i).toUpperCase().startsWith("U+FE0F ") && matchListFormat.get(i+1).equals(hierarchy)
                        && !(matchListFormat.get(i+1).isEmpty())) {
                    matchListFormat.set(i-1, matchListFormat.get(i-1) + " "+matchListFormat.get(i));
                    matchListFormat.remove(i);
                    i--;
                }
            }

            //matchlist is related to a regional indicator, combine the item to the one below and remove the one below
            if (matchListFormat.get(i).matches(regionalIndicator)
                    && matchListFormat.get(i+1).matches(regionalIndicator) && !(matchListFormat.get(i+1).isEmpty())) {
                matchListFormat.set(i, "U+" + Long.toHexString(matchListFormat.get(i).codePointAt(0))
                        + " U+" + Long.toHexString(matchListFormat.get(i+1).codePointAt(0)));
                matchListFormat.remove(i+1);
            }

            //Remove unnecessary variationSelector either between Emoji codes or at the end
            if (matchListFormat.get(i).matches(variationSelector) &&
                    (i == (matchListFormat.size()-1))){
                i--;
                matchListFormat.remove(i+1);
            }
            else{
                //Yet variationSelector is not followed by the Hierarchical code
                if(matchListFormat.get(i).matches(variationSelector) && !(matchListFormat.get(i+1).matches(hierarchy))){
                    i--;
                    matchListFormat.remove(i+1);
                }
            }
        }
        return matchListFormat;
    }

}
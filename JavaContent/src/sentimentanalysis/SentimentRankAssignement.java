package sentimentanalysis;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bukunmi on 4/2/2016.
 */
public class SentimentRankAssignement {
    static StanfordCoreNLP pipeline;

    public static void init() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

        pipeline = new StanfordCoreNLP(props);
    }


    public static double findSentimentRank(String inputString) {
        System.out.println("Sentiment Value Assignment");

        int subTotalSentiment = 0;
        double mainSentiment = 0;
        if (inputString != null && inputString.length() > 0) {
            int longest = 0;
            int numIteration = 0;
            Annotation annotation = pipeline.process(inputString);
            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence
                        .get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                //remove extra punctuation
                partText =removeUrl(partText);
                if (partText.length() > longest) {
                    numIteration++;
                    subTotalSentiment += sentiment;
                    //longest = partText.length();
                    System.out.println(sentiment + " " + partText);
                }

            }
            mainSentiment =  (double)subTotalSentiment/numIteration;
        }
        System.out.println("Sentiment Value: " +mainSentiment);
        return mainSentiment;
    }

    //Due to irregularities with the removeURL in the TeetsentimentAssignmet, the remove url is now working here, it remover all string that atarts with Http or other internet protocols
    private static String removeUrl(String inputCode) {
        inputCode = inputCode.replaceAll("\\p{Punct}", "");
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http)[\\w]+)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(inputCode);
        while (m.find()) {
            inputCode = inputCode.replaceAll(m.group(0), "").trim();
        }
        return inputCode;
    }
}

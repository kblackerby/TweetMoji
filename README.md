# TweetMoji
===
TweetMoji performs sentiment analysis of tweitter contents. It performs sentiment analysis on the tweet and accompanying emojis, It then  takes these sentiment score to analysis the tweet by grouping this tweets into positive and negative sentiments.\n
These sentiments are futher grouped into a range from strong to week. \n 
Sentiment score are computed using StanfordNLP an average of this i comuputed to arrive at the sentiment either for the text, emojis and the total sentiment.
Twitter Sentiment Analysis
--
>NOTE:: THE BASH FILE IS INTENDED FOR UNIX/LINUX MACHINES ONLY. \n
1. TO Retrieve Tweets
--
- Download and Install Python 2.7 or higher (if not installed)
- Run get-pip.py
- Run "pip install oauth2" command in command line
\n>NOTE::	For Windows, the command will look like "C:\Python27\Scripts\pip.exe install oauth2".\n

- Create Twitter Developer Profile
- Create new app to get api and access keys
- In a Tweet Retriever Python file, add your keys to the appropriate variables at the top of the file

UNIX/LINUX
- Ensure that the chosen Python Tweet Retriever and the bash file are in the same directory
- Navigate to that directory using the command prompt
- Type './tweetRetrieve.sh' (without quotes)
- Enjoy all the information in the text files!! 
\n Windows
- Recommended to run the Python file in Python's IDLE interface
- Unfortunately, you must manually restart the Python script each day to continue to collect data

2. Running The Tweet Spilter and Extracting Individual Tweet into JSON files 
--

3. Running The Emoji Code Conversion
--
- Open The "<b>UnicodeEmojiSampler.java</b>" In the main method, Replace the inputString value to the new value
- Change The path to your emojilist director to your current directory (Change accordingly)
- Build Path/Open Library Setting and Import External Library (found in the JavaContent/lib) ;
	- poi-3.13-20150929.jar
	- poi-ooxml-3.13-20150929.jar
	- poi-ooxml-schemas-3.13-20150929.jar
	- poi-ooxml-scratchpad-3.13-20150929.jar
	- xmlbeans-2.6.0.jar
	 \n These Libraries enable you to Read and Write to an Excel file.

4. Running the Sentiment Ranking - Assignment
--
- Open the "<b>TweetSentimentAssignment.java</b>" In the main method, Replace the inputString value to the curent directoy with the Tweets, 
- Change The path to your individual tweet director to the actual directory (Change accordingly)
- Build Path/Open Library Setting and import the following External Library (found in the JavaContent/lib);
	- The StanfordNLP
		- ejml-0.23.jar
		- slf4j-api.jar
		- slf4j-simple.jar
		- stanford-corenlp-3.6.0.jar
		- stanford-corenlp-3.6.0-models.jar
		- xom.jar
	 \n These Libraries enable you to Use StanfordNLP for sentiment analysis
	- JSON parsing
	 - json-simple-1.1.1.jar
	 \n This Library enable you work with json object.
	
5. Running The Indexer
--

6. Running The Sample GUI
--

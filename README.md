# TweetMoji
Twitter Sentiment Analysis

>>>NOTE:: THE BASH FILE IS INTENDED FOR UNIX/LINUX MACHINES ONLY.
1. TO Retrieve Tweets
1.0. Download and Install Python 2.7 or higher (if not installed)
1.1. Run get-pip.py
1.2. Run "pip install oauth2" command in command line
>>>NOTE::
	For Windows, the command will look like "C:\Python27\Scripts\pip.exe install oauth2"
1.3. Create Twitter Developer Profile
4. Create new app to get api and access keys
5. In a Tweet Retriever Python file, add your keys to the appropriate variables at the top of the file

UNIX/LINUX
1.6. Ensure that the chosen Python Tweet Retriever and the bash file are in the same directory
1.7. Navigate to that directory using the command prompt
1.8. Type './tweetRetrieve.sh' (without quotes)
1.9. Enjoy all the information in the text files!!

Windows
1.6. Recommended to run the Python file in Python's IDLE interface
1.7. Unfortunately, you must manually restart the Python script each day to continue to collect data

2. Running The Json Parser and extracting Individual tweet in JSON files 
------

3. Running The Emoji Code Conversion
-----
3.0. Open The "<b>ConvertEmojiUTFCode.java</b>" In the main Method, Replace the inputCode value to the new value
3.1. Change The Address of your emojilist director to your current directory
3.2. Build Path/Open Library Setting and Import External Libray (found in the JavaContent/lib) ;
	- poi-3.13-20150929.jar
	- poi-ooxml-3.13-20150929.jar
	- poi-ooxml-schemas-3.13-20150929.jar
	- poi-ooxml-scratchpad-3.13-20150929.jar
	- xmlbeans-2.6.0.jar
	These Libraries enable you to Read and Write to an Excel file.

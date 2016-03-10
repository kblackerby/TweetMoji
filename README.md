# TweetMoji
===
Twitter Sentiment Analysis
--
>>>NOTE:: THE BASH FILE IS INTENDED FOR UNIX/LINUX MACHINES ONLY.>>>
1. TO Retrieve Tweets
--
- Download and Install Python 2.7 or higher (if not installed)
- Run get-pip.py
- Run "pip install oauth2" command in command line
>>>NOTE::>>>
	For Windows, the command will look like "C:\Python27\Scripts\pip.exe install oauth2"
- Create Twitter Developer Profile
- Create new app to get api and access keys
- In a Tweet Retriever Python file, add your keys to the appropriate variables at the top of the file

UNIX/LINUX
- Ensure that the chosen Python Tweet Retriever and the bash file are in the same directory
- Navigate to that directory using the command prompt
- Type './tweetRetrieve.sh' (without quotes)
- Enjoy all the information in the text files!!
Windows
- Recommended to run the Python file in Python's IDLE interface
- Unfortunately, you must manually restart the Python script each day to continue to collect data

2. Running The Json Parser and extracting Individual tweet in JSON files 
--

3. Running The Emoji Code Conversion
--
- Open The "<b>ConvertEmojiUTFCode.java</b>" In the main Method, Replace the inputCode value to the new value
- Change The Address of your emojilist director to your current directory
- Build Path/Open Library Setting and Import External Libray (found in the JavaContent/lib) ;
	- poi-3.13-20150929.jar
	- poi-ooxml-3.13-20150929.jar
	- poi-ooxml-schemas-3.13-20150929.jar
	- poi-ooxml-scratchpad-3.13-20150929.jar
	- xmlbeans-2.6.0.jar
	These Libraries enable you to Read and Write to an Excel file.

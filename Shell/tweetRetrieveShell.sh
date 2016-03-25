#!


lastDate=0

while :
do

currDate=`date +%D`

if [ $lastDate != $currDate ]
then

lastDate=$currDate
echo "File path: /home/dmedinas/Documents/Tweets/DTweets_`date +%Y%m%d`.json"
echo ""
echo "Collecting Tweets from `date +%D`..."
echo ""
python TexasTechTweetRetrieverPython.py >> /home/dmedinas/Documents/Tweets/DTweets_`date +%Y%m%d`.json

echo "Generating Individual JSON Files for file DTweets_`date +%Y%m%d -d "yesterday"`"

javac IndividualTweetGenerator.java
java IndividualTweetGenerator Tweets/DTweets_`date +%Y%m%d -d`.json

echo ""
echo ""
echo ""
echo "Generated Individual JSON Files for file DTweets_`date +%Y%m%d -d "yesterday"`"
echo ""
echo ""

mv DTweets_`date +%Y%m%d -d "yesterday"` Individual_Tweets

fi

done
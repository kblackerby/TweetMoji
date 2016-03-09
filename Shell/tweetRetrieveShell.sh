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
python TexasTechTweetRetrieverPython.py > /home/dmedinas/Documents/Tweets/DTweets_`date +%Y%m%d`.json

fi

done
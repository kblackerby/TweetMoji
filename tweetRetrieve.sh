#!/bin/bash


lastDate=0

while :
do
	if [ $lastDate != `date +%D` ]; then
		lastDate=`date +%D`
		echo "`pwd`/TweetSampleStream_`date +%Y%m%d`.txt" 1>>tweetdex.txt
		echo "Collecting Tweets for `date +%Y%m%d`"
		python sampleTweetRetrieve.py 1>>KTweets_`date +%Y%m%d`.txt
	fi
done

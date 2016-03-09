'''
Reference 
  billhowe, sachu, t101jv
  twitterstream.py
  github.com/uwescience/datasci_course_materials/blob/master/assignment1/twitterstream.py
'''

from datetime import date
import oauth2 as oauth
import urllib2 as urllib

# Credentials for me
api_key = "OwqMMuILttYttVM9w5hlpgoIq"
api_secret = "**************************************************"
access_token_key = "4870397840-aPrc7tXl6zAIS6AmSTelBcYSaVGEHo6lwulg5ag"
access_token_secret = "*********************************************"

_debug = 0

oauth_token    = oauth.Token(key=access_token_key, secret=access_token_secret)
oauth_consumer = oauth.Consumer(key=api_key, secret=api_secret)

signature_method_hmac_sha1 = oauth.SignatureMethod_HMAC_SHA1()

http_method = "POST"

http_handler  = urllib.HTTPHandler(debuglevel=_debug)
https_handler = urllib.HTTPSHandler(debuglevel=_debug)


'''
Construct, sign, and open a twitter request
using the hard-coded credentials above.
'''
def twitterreq(url, method, parameters):
  req = oauth.Request.from_consumer_and_token(oauth_consumer,
                                             token=oauth_token,
                                             http_method=http_method,
                                             http_url=url, 
                                             parameters=parameters)

  req.sign_request(signature_method_hmac_sha1, oauth_consumer, oauth_token)

  headers = req.to_header()

  if http_method == "POST":
    encoded_post_data = req.to_postdata()
  else:
    encoded_post_data = None
    url = req.to_url()

  opener = urllib.OpenerDirector()
  opener.add_handler(http_handler)
  opener.add_handler(https_handler)

  response = opener.open(url, encoded_post_data)

  return response


# Get the statuses, stopping when the day rolls over
def fetchsamples():
  url = "https://stream.twitter.com/1.1/statuses/filter.json?language=en"
  parameters = []
  response = twitterreq(url, "POST", parameters)
  for line in response:
    print (line)
    if date.today != start:
      exit(0)

# Get today's date and mine the tweets
if __name__ == '__main__':
  start = date.today()
  fetchsamples()


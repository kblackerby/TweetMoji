import datetime
import oauth2 as oauth
import urllib2 as urllib

# See assignment1.html instructions or README for how to get these credentials

api_key = "bLL3wK1fwRazntQF0ndDn3YN0"
api_secret = "sjXXWW3VBOcBEh1AMkuaur4PSKoIyDrIxLahkLWaXLo9wnzTxI"
access_token_key = "388893257-3zvYa7bgt1QNvLTnbBErB0ovVSxsxkMORnJ1dpnb"
access_token_secret = "Obl3p07dwd0UPFIzEXUYjDPiJ8Gw7IS4BO3qRLIsIEWuE"

_debug = 0

oauth_token    = oauth.Token(key=access_token_key, secret=access_token_secret)
oauth_consumer = oauth.Consumer(key=api_key, secret=api_secret)

signature_method_hmac_sha1 = oauth.SignatureMethod_HMAC_SHA1()

http_method = "GET"


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

def fetchsamples():
  url = "https://stream.twitter.com/1.1/statuses/filter.json?track=%23ttu&track=%23texastech&track=%23redraider&track=%23gunsup&track=%23wreckem&track=%23striveforhonor&track=%23raiderland"
  parameters = []
  response = twitterreq(url, "GET", parameters)
  print('{\n\tTweets:[')
  
  for line in response:
      print line
  
      if datetime.date.today() != start:
	  print ('\b\t]\n}')
	  exit(0)

if __name__ == '__main__':
  start = datetime.date.today()
  fetchsamples()

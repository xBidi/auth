#!/usr/bin/env python

'''
This script will attempt to open your webbrowser,
perform OAuth 2.0 authentication and print your access token.
To install dependencies from PyPI:
  $ pip install oauth2client
Then run this script:
  $ python get_oauth2_token.py
This is a combination of snippets from:
  https://developers.google.com/api-client-library/python/guide/aaa_oauth
  https://gist.github.com/burnash/6771295
'''
import os, sys
from oauth2client.client import OAuth2WebServerFlow
from oauth2client.tools import run_flow
from oauth2client.file import Storage
import requests

def return_token(): 
  return get_oauth2_token()


def disable_stout():
  o_stdout = sys.stdout 
  o_file = open(os.devnull, 'w')
  sys.stdout = o_file
  return (o_stdout, o_file)


def enable_stout(o_stdout, o_file):
  o_file.close()
  sys.stdout = o_stdout

def get_oauth2_token():
  CLIENT_ID = '<client_id>'
  CLIENT_SECRET = '<client_secret>'
  SCOPE ='profile email' 
 
  o_stdout, o_file = disable_stout()

  flow = OAuth2WebServerFlow(client_id=CLIENT_ID,
                             client_secret=CLIENT_SECRET,
                             scope=SCOPE)

  storage = Storage('creds.data')
  credentials = run_flow(flow, storage)
  enable_stout(o_stdout, o_file)

  print("access_token: %s" % credentials.id_token_jwt)

if __name__ == '__main__':
  return_token()

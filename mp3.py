import requests, sys, bs4, webbrowser, json, csv
import tkinter as tk
import os

#GUI

base = "https://google.com/search"

params = {
    'rlz': '1C1VDKB_enPH952PH957',
    'sxsrf': 'ALeKk03JACWm43s_IEzLdysBMBN6nmLfKw:1625232169193',
    'ei': 'KRPfYO-fC4HB3LUPuYioiAg',
    'q': '',
    'gs_lcp': 'Cgdnd3Mtd2l6EANKBAhBGAFQrkNYrkNgmURoAXAAeACAAY4BiAGOAZIBAzAuMZgBAKABAaoBB2d3cy13aXrAAQE',
    'sclient': 'gws-wiz',
    'ved': '0ahUKEwiv2PzQvcTxAhWBILcAHTkECoEQ4dUDCA4',
    'uact': '5'
}

headers = {
    'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
    #'accept-encoding': 'gzip, deflate, br',
    'accept-language': 'en-US,en;q=0.9',
    'cache-control': 'max-age=0',
    'cookie': 'CGIC=Inx0ZXh0L2h0bWwsYXBwbGljYXRpb24veGh0bWwreG1sLGFwcGxpY2F0aW9uL3htbDtxPTAuOSxpbWFnZS93ZWJwLGltYWdlL2FwbmcsKi8qO3E9MC44LGFwcGxpY2F0aW9uL3NpZ25lZC1leGNoYW5nZTt2PWIzO3E9MC45; HSID=A9DP56BPeddjtVZJ5; SSID=AL3vpYGdrGSAXBtQZ; APISID=fFWa-r1ZiFSpMVcr/ApF5Jm1Et7IHOjkmO; SAPISID=xmL8-ZtNdsvi00fS/A3fXjYnIPoXAS8t_9; __Secure-3PAPISID=xmL8-ZtNdsvi00fS/A3fXjYnIPoXAS8t_9; OGPC=19009731-1:; SID=_QfjIyT_ndmtsmsV1R4DEPDwxS-RfFhb4PQYf55LtldyD5m7fzoLS3exhGGP1HMFxwCuBQ.; __Secure-3PSID=_QfjIyT_ndmtsmsV1R4DEPDwxS-RfFhb4PQYf55LtldyD5m7FPZnMbxKJPwXUYIxBmFhgA.; 1P_JAR=2021-07-02-13; NID=218=DERFn7KeyPbDu3CmAhSeRapntqtCQiI9ns0izLqx5KoR3TPhvKuHmNOnDG0v4Fs7BC1estA7Te-pbIzC7vNUK8NfZxY9gUpK1vtzwS4Eacy7l7JHLbWjKUW5Aft3LNHCJRl1fSmKMYYu0zTf6eWTduLlI3bhhxggvqY787r8Km2EvJ8BPVqHKnfFYd6DR9Fb7pPD3s-Qtz4jTnFNO2mzxRAM5-aAyvaKbpxWRAKgKfTz3xMhnyxi9Rk; DV=I95CSFUYYhpB4B5gABCykxGFygJ1plf4WvFgpuJ_WgEAABAgbgrVr3R9hwAAAFTcWbHAZMzfLwAAAA; SIDCC=AJi4QfEcjoncYlaPWtIqImUmQS5yL8JkVM0R6pLxzhM78LqGBI9sfl9AegJfsw3FuZVzOM4iaQ; __Secure-3PSIDCC=AJi4QfFV8k4qyNSgsIR_HiyD8ebl4dOWd9szALNW7N2cGW45rakpfpkYxQowHJzRYkIwuhodAds',
    'referer': 'https://www.google.com/',
    'sec-ch-ua': '" Not;A Brand";v="99", "Microsoft Edge";v="91", "Chromium";v="91"',
    'sec-ch-ua-mobile': '?0',
    'sec-fetch-dest': 'document',
    'sec-fetch-mode': 'navigate',
    'sec-fetch-site': 'same-origin',
    'sec-fetch-user': '?1',
    'upgrade-insecure-requests': '1',
    'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36 Edg/91.0.864.59'
}

def getFirstLink(song):
    query = song
    params['q'] = query
    
    res = requests.get(base, params = params,headers = headers)
    res.raise_for_status()
    soup = bs4.BeautifulSoup(res.text, "html.parser")

    linklist = []
    for links in soup.findAll('a', href=True):
        if 'youtube' in links.attrs['href']:
            return links.attrs['href']
  
    return
with open('vars.txt', 'r') as vars:
    foldername = vars.readline().strip("\n")

with open("songs.txt",'r') as songs:
    songlist = songs.readlines()

links = []
for song in songlist:
    links.append(getFirstLink(song))
    print("Getting Links")

for link in links:
    #print('cmd.exe /k "youtube-dl -o ~/Desktop/Playlist/%(title)s.%(ext)s -f 140 ' + str(link) + '"')
    print('cmd.exe /c "youtube-dl -o ~/Desktop/'+foldername+'/%(title)s.%(ext)s -f 140 ' + str(link) + '"')
    os.system('cmd /c START /MIN  cmd /c "youtube-dl -o ~/Desktop/'+foldername+'/%(title)s.%(ext)s -f 140 ' + str(link) + '"')


# UrlShortener
Test for Scala Play Url Shortener

Simple Scala Play project.
Tech Stack

- Scala 2.13
- Play 2.8

## Run it

Have sbt installed in your host
```bash
sbt run
```

#Usage using curl

- create Url

Windows version   
```
curl -X POST -H "Content-type: application/json" --data "{\"url\" : \"https://aaa.bbb.com/eee/ddd/yyy\"}" http://localhost:9000/url
```     
Linux Version    
```
curl -X POST -H "Content-type: application/json" --data '{"url" : "https://aaa.bbb.com/eee/ddd/yyy"}' http://localhost:9000/url
```

Answer from the API => "https://RickMorty.com/VGkXBcf"   


- get back the url    

```
curl -G -d "url=https://aaa.bbb.com/eee/ddd/yyy" http://localhost:9000/url
```

Answer from the API => "https://RickMorty.com/VGkXBcf" 





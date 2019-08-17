cd ../
mvn clean install
docker build -t liken .

1. Run mongo
docker run --name mongodb --restart always -p 27017:27017 -d mongo

2. Run Liken
docker run -it -d --net=host liken
or
docker run -it -d --net=host -v /etc/liken:/etc/liken liken
to override properties
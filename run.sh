./gradlew build
docker build -t address-book .
docker run -d -p 8080:8080 --name test-addressbook address-book
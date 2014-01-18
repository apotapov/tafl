# Instructions

## Download
```
mkdir ~/game
cd ~/game
git clone git@github.com:apotapov/gdx-artemis.git
git clone git@github.com:apotapov/zaria.git
git clone git@github.com:apotapov/tafl.git
cd gdx-artemis
mvn install
cd ../zaria
mvn install
cd ../tafl
mvn install -Pdesktop
```
 
## Run
```
cd ~/game/tafl
mvn antrun:run -Pdesktop
```

## Update from repository
```
cd ~/game/tafl
git pull origin master
mvn clean install -Pdesktop
```

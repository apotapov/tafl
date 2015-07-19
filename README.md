# Deployed

* https://play.google.com/store/apps/details?id=com.captstudios.games.tafl

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
```

## Update from repository
### Updating dependencies
* Not always necessary but may be required
```
cd ~/game/gdx-artemis
git pull origin master
mvn clean install
cd ~/game/zaria
git pull origin master
mvn clean install
```
### Updating the game
```
cd ~/game/tafl
git pull origin master
```
 
## Run On Desktop
```
cd ~/game/tafl
./gradlew desktop:run
```

## Requirements for Genymotion to work

* Genymotion Version: 2.0.3
* Virtual device: Nexus 4 - 4.3 - API 18 - 768 x 1280
* Follow these instructions for the game to work correctly: [ARM Translation + GApps](http://forum.xda-developers.com/showthread.php?t=2528952)

## Run on Genymotion
* Make sure Genymotion virtual device is started.
```
cd ~/game/tafl
```




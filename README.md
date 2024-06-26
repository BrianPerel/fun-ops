# fun-ops

## Project Description

* A collection of all sorts of different cross-platform Java programs I am making that I find interesting

* Project start date: Dec 11, 2020

## Other Notes

* In the ant build script if your going to create executables make sure to download
[Launch4j](https://sourceforge.net/projects/launch4j/files/launch4j-3/3.14/) version 3.14 and paste a copy of the Launch4j folder into the root of the project workspace in your dev environment

* Optionally, if you want the apps to be digitally signed during the jar process, make sure to create a `signjar-key.properties` file and place it under the directory `Documents\keys\signjar-key.properties`. The signjar-key.properties file must have the following properties defined with values: keystore, keystore.password, key.alias, kay.password. The keystore property must point to your generated .jks file to be used in the ant sign-jar process

* List of apps in this repo: Calculator, Data encryption-decryption, Stop watch, Guessing number game, Hangman, Tic tac toe, Wiggle Mouse, Clock, Ping Pong

* To run/do development on these apps you would need to have Java `JDK 17` installed

## Author

* Owner: Brian Perel

## Contributing

* Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Sample Screenshots

![Example apps-1](res/graphics/repo_demo/demo1.png "Samples of the programs")
![Example apps-2](res/graphics/repo_demo/demo2.png "More samples of the programs")

[View the project here on my website](https://brianperel.github.io/side_projects.htm)

# Count Words

Count Words is software what it gets from a text file subtitles and this count how many times a word is repited. 

you can get the subtitules for example of a movie at https://www.opensubtitles.org.

after download the subtitles you can run this program giving the path of the text file and then get a summary of what words are the most repeated. 

I used this information to learn vocabulary and see the movies with this information =).


## Prerequisites

To build and download the dependencies we need the next programs

* Java JDK 1.8- [Download & Install Java JDK](https://www.oracle.com/cl/java/technologies/javase/javase8-archive-downloads.html). 
* MAVEN - [Download & Install Maven](https://maven.apache.org/download.cgi). 


## Installation

1. We need to build the software, we'll go to the directory where is the source code and there we'll to execute the next command

```bash
mvn install 
```

with this command we made our program and this is in the directory ./target

2. we have to move our program(jar and sh) to the bin preference directory to my case I will work on ~/.local/bin

```bash
mv ./target/<jarFile> ~/.local/bin
mv ./countWords ~/.local/bin 
```

3. We have to be sure than our bin file is in our path. if isn't we can add with the next command

```bash
echo 'export PATH="/Users/<USER_SYSTEM>/.local/bin:$PATH"' >> ~/.zprofile
```


## Usage

```bash
	countWords <PATH:FILE>
```

you should get a summary like this


```
----------------------------------------
| Word                      | Count    |
----------------------------------------
| I                         | 2        |
| Attack                    | 1        |
| Behold                    | 1        |
| Do                        | 1        |
| Open                      | 1        |
| That                      | 1        |
| The                       | 1        |
----------------------------------------
```


## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## Buy Me A Coffe

<a href="https://www.buymeacoffee.com/lroman" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>


## License

Copyright 2023 LRoman

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.




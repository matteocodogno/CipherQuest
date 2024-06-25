# CipherQuest

## Description

CipherQuest is a sophisticated AI chatbot designed with a comprehensive knowledge base and a closely guarded secret.
The bot won't reveal its secret until you ask the right questions. Can you outsmart our AI and extract the hidden truth? Test your skills and uncover the secret in CipherQuest!

## Table of Contents

- [Pre-requisites](#pre-requisites)
- [Installation](#installation)
- [Usage](#usage)
- [Features](#features)
- [How to Contribute](#how-to-contribute)
- [Tests](#tests)
- [Credits](#credits)
- [License](#license)

## Pre-requisites

- Java 21, you can use [SDKMAN](https://sdkman.io/) to install it.
- Maven, you can use [SDKMAN](https://sdkman.io/) to install it.
- [Docker](https://www.docker.com/)

## Installation

In order to install all the dependencies, you need to run the following command `./mvnw clean install`, it will download all the dependencies and build the project.

## Usage

Tu run the project, execute the following command `./mvnw spring-boot:run`, it will start the application on port
8080 and 'cause we have `spring-boot-docker-compose` dependency, it will also start a docker container with a postgres database and a large language models server (ollama).

The first time you have to download `llama3` model, you can do it by running the following command `curl http://localhost:11435/api/pull -d '{ "name": "llama3" }'`.

## Features

-

## How to Contribute

[CONTRIBUTING](CONTRIBUTING.md)

## Tests

No tests have been written yet.

## Credits

- [matteocodogno](https://github.com/matteocodogno)

## License

[License](LICENSE.md)



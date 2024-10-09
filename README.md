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

- **Java 21**, you can use [SDKMAN](https://sdkman.io/) to install it.
- Maven, you can use [SDKMAN](https://sdkman.io/) to install it.
- [Docker](https://www.docker.com/)
- Node.js
- npm
- Ensure you have created the following directory under the project base path: `.mnt/postgres/data`

## Initialization

In order to install all the dependencies, you need to:
```
cd backend
./mvnw clean install

cd ../frontend
npm install
```

## Usage

Tu run the backend, execute the following command `cd backend && ./mvnw spring-boot:run`, it will start the application on port 8080.
And then because we have `spring-boot-docker-compose` dependency, it will also start a docker container with a postgres database with pgvector extension.

To run the frontend, execute the following command `cd frontend && npm run dev`, it will start the application on port 5173.

## Features

- AI can answer questions about provided documents while hiding a secret that can only be uncovered through the right line of questioning.
- Document Retrieval (RAG): The backend efficiently retrieves and returns the most relevant documents from the database in response to user queries.
- Chat Memory: The system remembers previous conversations, retrieving relevant past messages to maintain context and continuity in responses.

## How to Contribute

[CONTRIBUTING](CONTRIBUTING.md)

## Tests

No tests have been written yet.

## Credits

- [matteocodogno](https://github.com/matteocodogno)
- [andrea rubino](https://github.com/rubin0)

## License

[License](LICENSE.md)



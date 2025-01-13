# CipherQuest

## Description

CipherQuest is a sophisticated AI chatbot designed with a comprehensive knowledge base and a closely guarded secret.
The bot won't reveal its secret until you ask the right questions. Can you outsmart our AI and extract the hidden truth? Test your skills and uncover the secret in CipherQuest!

## Table of Contents

-   [Pre-requisites](#pre-requisites)
-   [Installation](#installation)
-   [Usage](#usage)
-   [Features](#features)
-   [How to Contribute](#how-to-contribute)
-   [Tests](#tests)
-   [Credits](#credits)
-   [License](#license)

## Pre-requisites

-   **Java 21**, you can use [SDKMAN](https://sdkman.io/) to install it.
-   Maven, you can use [SDKMAN](https://sdkman.io/) to install it.
-   [Docker](https://www.docker.com/)
-   Node.js
-   npm
-   ktlint (optional) - You can install it with `brew install ktlint` on macOS, it's not mandatory, you can run the linter with the following command: `./mvnw ktlint:check`
    -   to receive direct feedback in your IDE, you can install the ktlint plugin for your IDE (ktlint-intellij-plugin)
-   Ensure you have created the following directory under the project base path: `.mnt/postgres/data`
-   Configure the following environment variables:
    -   `OPENAI_API_KEY` with the OpenAI API key, alternatively you can define it in your run configuration of your IDE.

## Initialization

In order to install all the dependencies, you need to:

-   Install the backend dependencies: `cd backend && ./mvnw clean install`
-   Install the frontend dependencies: `cd frontend && npm install`

## Usage

Now you can run the application. As you can guess, you need to run the backend and the frontend separately. Here's how you can do it:

-   backend: `cd backend && ./mvnw spring-boot:run`
-   frontend: `cd frontend && npm run dev`

At this point, you should have two services up and running. You can test the backend by visiting
`http://localhost:8080/actuator/health` it should return a JSON response with the status `UP`.
Navigating to `http://localhost:5173` you should see the frontend application. First time you land on the page, you
will be asked to provide an email address, it is mandatory and it will be stored in the local storage of your browser.

Log out to start a new session.

### IDE Configuration

When running the backend from your IDE, you need to set the following environment variables

-   `OPENAI_API_KEY` with the OpenAI API key
-   `MAIL_HOST`: SMTP email address
-   `MAIL_PORT`: SMTP email port
-   `MAIL_USERNAME`: SMTP username account
-   `MAIL_PASSWORD`: SMTP username password
-   Working directory: `${PWD}/backend`
    ![IDE Configuration](./docs/assets/working-dir.png)

## Features

-   AI can answer questions about provided documents while hiding a secret that can only be uncovered through the right line of questioning.
-   Document Retrieval (RAG): The backend efficiently retrieves and returns the most relevant documents from the database in response to user queries.
-   Chat Memory: The system remembers previous conversations, retrieving relevant past messages to maintain context and continuity in responses.

## How to Contribute

[CONTRIBUTING](CONTRIBUTING.md)

## Tests

No tests have been written yet.

## Credits

-   [matteo codogno](https://github.com/matteocodogno)
-   [andrea rubino](https://github.com/rubin0)
-   [simone cimoli](https://github.com/CimsW3llD)

## License

[License](LICENSE.md)

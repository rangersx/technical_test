# Assignment:

Your task is to create a backend service for a trello clone app.
Below is an example of a UI for the board page of the app.

| to do | in progress | review | test  | deploy | done  |
|-------|-------------|--------|-------|--------|-------|
| task1 |             |        | task3 |        |       |
|       | task 2      |        |       |        |       |
|       |             |        |       |        | task4 |

It contains the trello board with multiple columns representing task's statuses.
The user can add, modify (moving from 1 column to another), or delete task.
To access the board page, the user have to be authenticated by the system using Oauth2

# Setup

1. Fork this repository to your own GitHub account.
2. Install java 17 (temurin-17)
3. Install docker and docker-compose,
    
# Running existing tests
After all the required tools are installed. Run the following command to execute the tests:
```sh 
./mvnw verify -DskipITs=false
```
After running the tests you should see that some tests have passed and some failed.

# Your Task:
- Fix the failed tests.
- Make sure that the tests that passed will still pass.
- Pay attention to the "TODOS and NOTES" as it tells you what you need to do.
- Once done, commit the changes and notify me by creating a pull request

# Note:
- Even though we are using Java and Spring in this codebase, feel free to use any programming language or framework of your own choice. <br/>
- The integration tests are using [testcontainers](https://www.testcontainers.org/) and testcontainers have been forked in multiple programming languages:
  - [go](https://golang.testcontainers.org/)
  - [node](https://github.com/testcontainers/testcontainers-node)
  - [java]([testcontainers](https://www.testcontainers.org/))
  - [rust](https://github.com/testcontainers/testcontainers-rs)
- Feel free to rewrite the integration test in any of the above language if necessary
Please take a look at the integration tests in: src/test/java/com/trello/clone/integration/*<br/>
After reading the tests, you should have some sort of idea on how to fulfill the requirements for user stories below <br/>
 
## User Stories

### In Identity Domain, as a User I :
- can register
  - get ok status with valid payload
  - get error with invalid payload
- can login
  - get new token pair (access & refresh token) with valid payload
  - get error with invalid payload
- can refresh
  - get new token pair with valid refresh token
  - session invalidated when refresh token is invalid (simulate stolen token)
  - not able to refresh after session is invalidated
- can logout
  - session invalidated when logout endpoint is called

### In Board Domain. as a User I :
- can create new board
  - get ok status with valid payload. User becomes the board admin
  - get error with invalid payload
- can load existing board
  - get valid result when fetching existing board
  - get empty result when fetching non-existing board
- can update existing board (rename, update description, etc)
  - get ok status when updating board with valid payload
  - get error when updating with invalid payload
- can delete existing board
  - get ok status when deleting existing board
  - get error when deleting non-existing board
- can share with other user
  - get ok status when sharing board with existing user
  - get error when sharing board with non-existing user

### In Column domain, as a User I :
- Can create new column
  - get ok status with valid payload.
  - get error with invalid payload.
- can update existing column (rename, change position)
  - get ok status with valid payload.
  - get error with invalid payload.
- can remove existing column
  - get ok status with valid columnId
  - get error with invalid columnId

### In Task Domain, as a User I :
- can create new task
  - get ok status with valid payload
  - get error with invalid payload
- can update task
  - get ok status with valid payload
  - get error with invalid payload
- can delete task
  - get ok status with valid Id
  - get error with invalid Id

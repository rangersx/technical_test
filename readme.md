# Setting Up

# Assignment:

Your task is to create a backend service for a trello clone app.
Below is an example of a UI for the board page of the app.

| to do | in progress | review | test  | deploy | done  |
|-------|-------------|--------|-------|--------|-------|
| task1 |             |        | task3 |        |       |
|       | task 2      |        |       |        |       |
|       |             |        |       |        | task4 |

It contains the trello board with multiple columns representing tasks status.
Within the status column the user can add, modify, or delete tasks.

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

### In Task Domain:
As a User I :
- can create new task
    - get ok status with valid payload
    - get error with invalid payload
- can update task
    - get ok status with valid payload
    - get error with invalid payload
- can delete task
    - get ok status with valid Id
    - get error with invalid Id

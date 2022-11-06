package config

// config APP
const APP_PORT int = 8000
const APP_NAME string = "Todoapps"

// config postgres
const POSTGRES_HOST string = "localhost"
const POSTGRES_PORT string = "5432"
const POSTGRES_USERNAME string = "postgres"
const POSTGRES_PASSWORD string = "postgres"
const POSTGRES_DBNAME string = "technical_test"
const POSTGRES_IS_MIGRATE bool = true

// config mongodb
const MONGODB_HOST string = "localhost"
const MONGODB_PORT string = "27017"
const MONGODB_USERNAME string = "mongodb"
const MONGODB_PASSWORD string = "secret"
const MONGODB_DBNAME string = "todo"

// config JWT
const JWT_SECRET_KEY string = "secret"
const JWT_ACCESS_EXPIRE_MIN int = 60
const JWT_REFRESH_EXPIRE_MIN int = 60 * 24 * 3

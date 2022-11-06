package main

import (
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"github.com/gofiber/fiber/v2/middleware/logger"
	"github.com/gofiber/fiber/v2/middleware/recover"
	"github.com/rangersx/technical_test/backend/src/app/Routers"
	"github.com/rangersx/technical_test/backend/src/config"
	"github.com/rangersx/technical_test/backend/src/database"
	"log"
)

func welcome(c *fiber.Ctx) error {
	return c.JSON(fiber.Map{
		"message": "Welcome to API Golang",
	})
}

func main() {
	database.GetPostgresqlConnection()

	app := fiber.New(
		fiber.Config{
			AppName: config.APP_NAME,
		})
	app.Use(cors.New())

	app.Use(recover.New())
	app.Use(logger.New())

	app.Get("/", welcome)

	// TODO not found route

	Routers.SetupRoutes(app)

	listen := fmt.Sprintf(":%v", config.APP_PORT)
	log.Fatal(app.Listen(listen))

}

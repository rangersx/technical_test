package Routers

import (
	"github.com/gofiber/fiber/v2"
	"github.com/rangersx/technical_test/backend/src/app/Middlewares"
	controller "github.com/rangersx/technical_test/backend/src/app/controllers"
)

func SetupRoutes(app *fiber.App) {
	api := app.Group("/api")

	apiV1 := api.Group("/v1")

	apiV1.Get("/", func(c *fiber.Ctx) error {
		return c.JSON(fiber.Map{
			"message": "Welcome to API Version 1 Golang",
		})
	})

	// Auth group
	auth := apiV1.Group("/auth")
	auth.Post("/register", controller.Register)
	auth.Post("/login", controller.Login)

	auth.Get("/test", Middlewares.JWTProtected(), func(c *fiber.Ctx) error {
		return c.JSON(fiber.Map{
			"message": "Welcome to API Version 1 Golang",
		})
	})

	// Todo group
	todo := apiV1.Group("/todo")
	todo.Get("/all/:user_id", Middlewares.JWTProtected(), controller.GetAllTodo)
	todo.Get("/all/:user_id/:id", Middlewares.JWTProtected(), controller.GetAllByIdByUserIdTodo)
	todo.Post("/create", Middlewares.JWTProtected(), controller.CreateNewTodo)
	todo.Put("/update/:id", Middlewares.JWTProtected(), controller.UpdateTodo)
	todo.Delete("/delete/:id", Middlewares.JWTProtected(), controller.DeleteTodo)

	// Todo Category group
	todoCategory := apiV1.Group("/todo-category")
	todoCategory.Get("/all/user/:user_id", Middlewares.JWTProtected(), controller.GetAllTodoCategory)
	todoCategory.Get("/all/:id", Middlewares.JWTProtected(), controller.GetAllByIdTodoCategory)
	todoCategory.Post("/create", Middlewares.JWTProtected(), controller.CreateNewTodoCategory)
	todoCategory.Put("/update/:id", Middlewares.JWTProtected(), controller.UpdateTodoCategory)
	todoCategory.Delete("/delete/:id", Middlewares.JWTProtected(), controller.DeleteTodoCategory)
}

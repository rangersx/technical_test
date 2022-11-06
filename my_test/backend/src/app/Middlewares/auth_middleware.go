package Middlewares

import (
	"github.com/gofiber/fiber/v2"
	jwtware "github.com/gofiber/jwt/v3"
	"github.com/rangersx/technical_test/backend/src/app/Common"
	config2 "github.com/rangersx/technical_test/backend/src/config"
)

// https://github.com/gofiber/jwt
func JWTProtected() func(ctx *fiber.Ctx) error {
	config := jwtware.Config{
		SigningKey:   []byte(config2.JWT_SECRET_KEY),
		ContextKey:   "jwt", // used in private routes
		ErrorHandler: jwtError,
	}
	return jwtware.New(config)
}

func jwtError(c *fiber.Ctx, err error) error {
	if err.Error() == "Missing or malformed JWT" {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusBadRequest,
				Message: err.Error(),
			})
	}
	return c.JSON(Common.ErrorResponse{
		Status:  fiber.StatusUnauthorized,
		Message: err.Error(),
	})
}

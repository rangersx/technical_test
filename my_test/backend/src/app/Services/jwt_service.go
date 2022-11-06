package Services

import (
	"github.com/golang-jwt/jwt/v4"
	"github.com/rangersx/technical_test/backend/src/app/Models"
	"github.com/rangersx/technical_test/backend/src/config"
	"time"
)

func GenerateToken(user *Models.User) Models.JWTResult {
	var tokenResult Models.JWTResult

	claims := jwt.MapClaims{
		"user_id": user.ID,
		"iss":     "fiber-api",
		"iat":     time.Now().Unix(),
		"exp":     time.Now().Add(time.Duration(config.JWT_ACCESS_EXPIRE_MIN) * time.Minute).Unix(),
	}

	// Create the Claims
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)

	t, _ := token.SignedString([]byte(config.JWT_SECRET_KEY))

	tokenResult = Models.JWTResult{
		Token: t,
		Exp:   int(time.Now().Add(time.Duration(config.JWT_ACCESS_EXPIRE_MIN) * time.Minute).Unix()),
	}
	return tokenResult
}

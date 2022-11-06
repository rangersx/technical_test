package test

import (
	"context"
	"fmt"
	"github.com/rangersx/technical_test/backend/src/config"
	"github.com/testcontainers/testcontainers-go"
	"github.com/testcontainers/testcontainers-go/wait"
)

// new setup postgres in testcontainer

// example: https://github.com/thlib/testcontainerpostgres/blob/main/container.go
func New(ctx context.Context, tag, init string) (testcontainers.Container, string, error) {
	const (
		name = config.POSTGRES_DBNAME
		user = config.POSTGRES_USERNAME
		pass = config.POSTGRES_PASSWORD
	)

	// Create PostgreSQL container request
	req := testcontainers.ContainerRequest{
		Image: "postgres:" + tag,
		Env: map[string]string{
			"POSTGRES_DB":       name,
			"POSTGRES_USER":     user,
			"POSTGRES_PASSWORD": pass,
		},
		ExposedPorts: []string{"5432/tcp"},
		WaitingFor:   wait.ForListeningPort("5432/tcp"),
	}
	if init != "" {
		req.Mounts = testcontainers.Mounts(testcontainers.ContainerMount{
			Source: testcontainers.GenericBindMountSource{
				HostPath: init,
			},
			Target: testcontainers.ContainerMountTarget("/docker-entrypoint-initdb.d/init.sql"),
		})
	}

	// Start PostgreSQL container
	container, err := testcontainers.GenericContainer(ctx, testcontainers.GenericContainerRequest{
		ContainerRequest: req,
		Started:          true,
	})
	if err != nil {
		return nil, "", fmt.Errorf("failed to start postgres container: %w", err)
	}

	// Get host and port of PostgreSQL container
	host, err := container.Host(ctx)
	if err != nil {
		return nil, "", fmt.Errorf("failed to get host: %w", err)
	}

	port, err := container.MappedPort(ctx, "5432")
	if err != nil {
		return nil, "", fmt.Errorf("failed to get port: %w", err)
	}

	conn := fmt.Sprintf("postgres://%v:%v@%v:%v/%v", user, pass, host, port.Port(), name)

	// Create db connection string and connect
	return container, conn, nil
}

// Terminate terminates the container in a defer friendly way
func Terminate(ctx context.Context, c testcontainers.Container) {
	err := c.Terminate(ctx)
	if err != nil {
		panic(fmt.Sprintf("failed to terminate container: %v", err))
	}
}

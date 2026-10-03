# Development deployment with Docker Compose

This Compose setup runs the LMS services and their development dependencies.
The Config Server clones configuration from the Git repository named by
`CONFIG_GIT_URI` and the branch/tag named by `CONFIG_GIT_LABEL`. Application
profiles, service-specific settings, and Gateway routes come from that
repository. Compose environment variables override the container-specific
hostnames and local database/cache credentials so services can communicate
over the private Docker network. Compose activates the single `dev` profile;
the `*-dev.yaml` files in the Config-Server repository contain the development,
monitoring, and Kafka settings together. The project-root `dev/` directory is
only a staging folder: Config Server does not mount or load it directly.

The repository's `.env.example` selects the `dev` branch. In the Config-Server
repository, create and check out a `dev` branch, then copy the **contents** of
the project-root `dev/` directory into the Config-Server repository root,
replacing the matching configuration filenames. These names end in `-dev.yaml`
to match Spring Cloud Config's `dev` profile convention. Commit and push those
files to the `dev` branch. Do not copy the `dev/` directory as a nested
directory: Config Server expects these files at the configuration repository
root.

## Before starting

- Use an EC2 instance with enough memory for six Spring Boot processes plus
  PostgreSQL, Kafka, Redis, Zipkin, Prometheus, and Grafana. A small free-tier
  instance may run out of memory; check `free -h` before starting the full stack.
- Keep SSH restricted to your own IP. Allow inbound TCP port `8080` only if you
  want the API to be reachable from the internet.
- Grafana listens on the EC2 loopback interface only; do not open port `3000`
  in the security group.
- The API is exposed without application authentication in this development
  configuration. Do not use it for sensitive or production data.

## Start the application

From the repository root, create a private environment file and replace all
example passwords with strong values:

```bash
cp deploy/.env.example deploy/.env
nano deploy/.env
```

Confirm that `CONFIG_GIT_LABEL=dev` is set in `deploy/.env`.

Validate the Compose configuration, build the images, and start the services:

```bash
docker compose --env-file deploy/.env -f compose.dev.yml config --quiet
docker compose --parallel 1 --env-file deploy/.env -f compose.dev.yml build
docker compose --env-file deploy/.env -f compose.dev.yml up -d
docker compose --env-file deploy/.env -f compose.dev.yml ps
```

Only the API Gateway is publicly published on port `8080`. PostgreSQL, Redis,
Kafka, Prometheus, Eureka, and Config Server are reachable only on the private
Compose network. Grafana is bound to EC2 loopback and can be opened from your
Mac through an SSH tunnel:

```bash
ssh -i /path/to/lms-microservices.pem -L 3000:127.0.0.1:3000 ubuntu@<EC2-public-IP>
```

Keep that SSH session open, then visit `http://localhost:3000` in your browser.
Sign in with `GRAFANA_ADMIN_USER` and `GRAFANA_ADMIN_PASSWORD` from `deploy/.env`.
The Prometheus data source is provisioned automatically.

Prometheus scrapes the backend services, Eureka, and Config Server over the
private Compose network. It is not published to the internet. Gateway metrics
are not exposed because the Gateway is the public entry point.

If the containers are already running when you push updated files to the
Config-Server Git repository, force-recreate them to fetch the new settings:

```bash
docker compose --env-file deploy/.env -f compose.dev.yml up -d --force-recreate --no-build
```

Open the Swagger UI at:

```text
http://<EC2-public-IP>:8080/swagger-ui.html
```

Follow startup logs with:

```bash
docker compose --env-file deploy/.env -f compose.dev.yml logs -f api-gateway
```

Stop the containers while preserving database and Redis data with:

```bash
docker compose --env-file deploy/.env -f compose.dev.yml down
```

The PostgreSQL initialization script creates `bookdb` and `loandb` on the
first start of an empty data volume. It does not rerun against an existing
volume.

If the Config Server fails to clone its Git repository, verify that the
repository is reachable from EC2 and that `CONFIG_GIT_LABEL` exists. For a
private configuration repository, configure read-only Git credentials using
Docker secrets or another secret manager; do not commit a token in `.env` or
the Compose file.

The dev configuration files use environment placeholders for database and
Redis credentials. Do not copy real credentials from an existing Git branch;
rotate any real credentials that have already been committed there.

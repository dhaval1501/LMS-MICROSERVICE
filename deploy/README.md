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

The repository's `.env.example` selects the existing `master` branch. Copy the
**contents** of the project-root `dev/` directory into the root of the
Config-Server repository on `master`, replacing the matching configuration
filenames. These names end in `-dev.yaml` to match Spring Cloud Config's `dev`
profile convention; `dev` here is a Spring profile, not a Git branch. Commit
and push those files to `master`. Do not copy the `dev/` directory as a nested
directory: Config Server expects these files at the configuration repository
root.

The four application modules also have local `application-dev.yaml` files.
They select the Config-Server label from `CONFIG_GIT_LABEL`, which defaults to
`master`. The base `application.yaml` keeps the localhost default for local
runs, while Compose supplies
`CONFIG_SERVER_URL=http://config-server:8088` for the container network.

## Before starting

- Use an EC2 instance with enough memory for six Spring Boot processes plus
  PostgreSQL, Kafka, Redis, Zipkin, Prometheus, and Grafana. A small free-tier
  instance may run out of memory; check `free -h` before starting the full stack.
- Keep SSH restricted to your own IP. The API and monitoring dashboards are
  exposed publicly by this development setup; do not use it for sensitive or
  production data.
- Grafana requires login, but Prometheus and Zipkin have no authentication in
  this setup. Anyone who can reach the instance can inspect metrics and traces.
  Restrict dashboard ports to trusted IPs where possible, and use a reverse
  proxy with HTTPS and authentication for a production deployment.
- The API is exposed without application authentication in this development
  configuration. Do not use it for sensitive or production data.

## Start the application

From the repository root, create a private environment file and replace all
example passwords with strong values:

```bash
cp deploy/.env.example deploy/.env
nano deploy/.env
```

Confirm that `CONFIG_GIT_LABEL=master` is set in `deploy/.env`.

Validate the Compose configuration, build the images, and start the services:

```bash
docker compose --env-file deploy/.env -f compose.dev.yml config --quiet
docker compose --parallel 1 --env-file deploy/.env -f compose.dev.yml build
docker compose --env-file deploy/.env -f compose.dev.yml up -d
docker compose --env-file deploy/.env -f compose.dev.yml ps
```

## Start and stop Compose automatically with EC2

To have systemd start the stack during Ubuntu boot and stop (without removing)
the containers during shutdown, install the script and systemd
service on EC2. This assumes the repository is at
`/home/ubuntu/LMS-MICROSERVICE`:

```bash
sudo install -m 755 deploy/lms-compose.sh /home/ubuntu/LMS-MICROSERVICE/deploy/lms-compose.sh
sudo install -m 644 deploy/lms-compose.service /etc/systemd/system/lms-compose.service
sudo systemctl daemon-reload
sudo systemctl enable --now lms-compose.service
sudo systemctl status lms-compose.service
```

Afterward, stopping the EC2 instance through AWS stops the stack through
systemd; starting the instance runs the script to start it again. The script's
`stop` action uses `docker compose stop`, so containers remain present and all
data volumes are preserved. To disable this automation:

```bash
sudo systemctl disable --now lms-compose.service
```

The API Gateway and dashboards are published on ports `8080`, `3000`, `9090`,
and `9411`. PostgreSQL, Redis, Kafka, Eureka, and Config Server remain reachable
only on the private Compose network. Add inbound TCP rules for those four
published ports to the EC2 security group. To make them reachable from any IPv4
address, set each source to `0.0.0.0/0`; this is not recommended for dashboards.
Grafana requires the credentials configured in `deploy/.env`, but Prometheus and
Zipkin do not require login.

Open `http://<EC2-public-IP>:3000` for Grafana,
`http://<EC2-public-IP>:9090` for Prometheus, and
`http://<EC2-public-IP>:9411` for Zipkin. The Prometheus data source is
provisioned automatically in Grafana.

Prometheus scrapes the backend services, Eureka, and Config Server over the
private Compose network. Gateway metrics are not exposed because the Gateway is
the public entry point.

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
repository is reachable from EC2 and that the configured `CONFIG_GIT_LABEL`
exists. For a
private configuration repository, configure read-only Git credentials using
Docker secrets or another secret manager; do not commit a token in `.env` or
the Compose file.

The dev configuration files use environment placeholders for database and
Redis credentials. Do not copy real credentials from an existing Git branch;
rotate any real credentials that have already been committed there.

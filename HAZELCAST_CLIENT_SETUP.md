# Hazelcast Client Setup

This application now uses Hazelcast Client to connect to a remote Hazelcast server.

## Architecture

- **Hazelcast Server**: Separate project at `/HazelcastServer` - runs standalone Hazelcast instance on port 5701
- **DiceApplication**: Uses Hazelcast Client to connect to the remote server
- **Connection**: Client automatically connects to `localhost:5701` when the application starts

## Setup Instructions

### 1. Start the Hazelcast Server

First, start the standalone Hazelcast server:

```bash
cd ../HazelcastServer
./gradlew run
```

Or if you have Gradle wrapper issues:
```bash
cd ../HazelcastServer
gradle run
```

The server will start on port **5701** (default Hazelcast port).

### 2. Start the Dice Application

In a separate terminal, start the Dice Application:

```bash
cd DiceApplicaton
./gradlew bootRun
```

The application will automatically connect to the Hazelcast server running on `localhost:5701`.

## Configuration

### Server Configuration
- **Port**: 5701
- **Instance Name**: dice-roll-hazelcast
- **Map**: rollHistoryMap (TTL: 3600 seconds)

### Client Configuration
- **Server Address**: localhost:5701 (configurable in `application.properties`)
- **Cluster Name**: dice-roll-hazelcast
- Configured in: `HazelcastConfig.java`

## Changing Server Address

To connect to a different Hazelcast server, update `HazelcastConfig.java`:

```java
clientConfig.getNetworkConfig().addAddress("your-server-ip:5701");
```

Or update `application.properties`:
```properties
hazelcast.client.server.address=your-server-ip:5701
```

## Notes

- The executor service tasks (`CacheStatsAccessor`, `AllInOneCacheOperationTest`, etc.) execute on the **server side**, so they can access the server instance directly.
- Make sure the Hazelcast server is running before starting the client application.
- The client will automatically reconnect if the server restarts.


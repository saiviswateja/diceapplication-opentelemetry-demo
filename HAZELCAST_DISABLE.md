# Disabling/Enabling Hazelcast

## Current Status

**Hazelcast is currently DISABLED** by default.

## How to Enable/Disable Hazelcast

### To Disable Hazelcast (Current Setting)
In `application.properties`:
```properties
hazelcast.enabled=false
```

### To Enable Hazelcast
In `application.properties`:
```properties
hazelcast.enabled=true
```

**Important**: When enabling Hazelcast, make sure the Hazelcast server is running on `localhost:5701`.

## What Happens When Hazelcast is Disabled?

1. **HazelcastConfig bean is not created** - The `@ConditionalOnProperty` annotation prevents the Hazelcast client from being initialized.

2. **All Hazelcast-dependent services handle null gracefully**:
   - `HazelcastSerializationService` - Skips Hazelcast operations, logs debug messages
   - `CacheOperationService` - Returns error messages indicating Hazelcast is disabled
   - `CacheStatsController` - Returns error messages in response
   - `RollHistoryController` - Returns empty list for `/hazelcast` endpoint
   - `RollService2` - Continues with DB save even if Hazelcast fails (already had try-catch)

3. **Application continues to work normally** - All database operations and other features work as expected.

## Services Modified

All services that use Hazelcast now have:
- `@Autowired(required = false)` - Makes HazelcastInstance optional
- Null checks before using Hazelcast
- Graceful fallback behavior when Hazelcast is disabled

## Testing

1. **With Hazelcast disabled** (current):
   - Application starts without trying to connect to Hazelcast
   - All endpoints work but return appropriate messages
   - Database operations work normally

2. **With Hazelcast enabled**:
   - Set `hazelcast.enabled=true` in `application.properties`
   - Start Hazelcast server: `cd HazelcastServer && ./gradlew run`
   - Start DiceApplication: `./gradlew bootRun`
   - All Hazelcast features will work normally


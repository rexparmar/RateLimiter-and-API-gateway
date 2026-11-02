local key = KEYS[1]
local capacity_units = tonumber(ARGV[1])
local request_cost_units = tonumber(ARGV[2])
local ttl_ms = tonumber(ARGV[3])

local redis_time = redis.call('TIME')
local now_us = tonumber(redis_time[1]) * 1000000 + tonumber(redis_time[2])

local bucket = redis.call(
    'HMGET',
    key,
    'available_units',
    'last_refill_us'
)

local available_units = tonumber(bucket[1])
local last_refill_us = tonumber(bucket[2])

if available_units == nil or last_refill_us == nil then
    available_units = capacity_units
    last_refill_us = now_us
end

local elapsed_us = math.max(0, now_us - last_refill_us)
local refilled_units = math.min(
    capacity_units,
    available_units + elapsed_us
)

local admitted = 0
local remaining_units = refilled_units

if refilled_units >= request_cost_units then
    admitted = 1
    remaining_units = refilled_units - request_cost_units
end

redis.call(
    'HSET',
    key,
    'available_units',
    remaining_units,
    'last_refill_us',
    now_us
)
redis.call('PEXPIRE', key, ttl_ms)

return admitted

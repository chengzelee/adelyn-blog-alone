package cn.adelyn.blog.manager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Twitter的Snowflake 算法<br>
 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
 *
 * <p>
 * snowflake的结构如下(每部分用-分开):<br>
 *
 * <pre>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * </pre>
 * <p>
 * 第一位为未使用(符号位表示正数)，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * <p>
 * 并且可以通过生成的id反推出生成时间,datacenterId和workerId
 * <p>
 * 参考：http://www.cnblogs.com/relucent/p/4955340.html
 *
 * 实测这个算法线程不敏感，不管多少线程tps都在3W左右
 */
@Service
public class SnowflakeService {
    private final long twepoch;
    private final long workerIdBits = 5L;
    // 最大支持机器节点数0~31，一共32个
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final long dataCenterIdBits = 5L;
    // 最大支持数据中心节点数0~31，一共32个
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);
    // 序列号12位
    private final long sequenceBits = 12L;
    // 机器节点左移12位
    private final long workerIdShift = sequenceBits;
    // 数据中心节点左移17位
    private final long dataCenterIdShift = sequenceBits + workerIdBits;
    // 时间毫秒数左移22位
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    // 序列掩码，用于限定序列最大值不能超过4095
    @SuppressWarnings("FieldCanBeLocal")
    private final long sequenceMask = ~(-1L << sequenceBits);// 4095

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    @Value("${snowflake.workerId:1}")
    private long workerId;

    @Value("${snowflake.dataCenterId:1}")
    private long dataCenterId;

    public SnowflakeService() {
        // Thu, 04 Nov 2010 01:42:54 GMT
        // 初始时间定的早一点，这样就一直是19位了，避免用个七八年之后从18位变成19位
        this.twepoch = 1288834974657L;
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than " + maxWorkerId + " or less than 0");
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException("datacenter Id can't be greater than " + maxDataCenterId + " or less than 0");
        }
    }

    /**
     * 根据Snowflake的ID，获取机器id
     *
     * @param id snowflake算法生成的id
     * @return 所属机器的id
     */
    public long getWorkerId(long id) {
        return id >> workerIdShift & ~(-1L << workerIdBits);
    }

    /**
     * 根据Snowflake的ID，获取数据中心id
     *
     * @param id snowflake算法生成的id
     * @return 所属数据中心
     */
    public long getDataCenterId(long id) {
        return id >> dataCenterIdShift & ~(-1L << dataCenterIdBits);
    }

    /**
     * 根据Snowflake的ID，获取生成时间
     *
     * @param id snowflake算法生成的id
     * @return 生成的时间
     */
    public long getGenerateDateTime(long id) {
        return (id >> timestampLeftShift & ~(-1L << 41L)) + twepoch;
    }

    /**
     * 下一个ID
     *
     * @return ID
     */
    public synchronized long nextId() {
        long timestamp = getTime();
        if (timestamp < this.lastTimestamp) {
            if(this.lastTimestamp - timestamp < 2000){
                // 容忍2秒内的回拨，避免NTP校时造成的异常
                timestamp = lastTimestamp;
            } else{
                // 如果服务器时间有问题(时钟后退) 报错。
                throw new IllegalStateException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timestamp) + "ms");
            }
        }

        if (timestamp == this.lastTimestamp) {
            final long sequence = (this.sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
            this.sequence = sequence;
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (dataCenterId << dataCenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    /**
     * 下一个ID（字符串形式）
     *
     * @return ID 字符串形式
     */
    public String nextIdStr() {
        return Long.toString(nextId());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------ Private method start

    /**
     * 循环等待下一个时间
     *
     * @param lastTimestamp 上次记录的时间
     * @return 下一个时间
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = getTime();
        // 循环直到操作系统时间戳变化
        while (timestamp == lastTimestamp) {
            timestamp = getTime();
        }
        if (timestamp < lastTimestamp) {
            // 如果发现新的时间戳比上次记录的时间戳数值小，说明操作系统时间发生了倒退，报错
            throw new IllegalStateException(
                "Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timestamp) + "ms");
        }
        return timestamp;
    }

    /**
     * 生成时间戳
     *
     * @return 时间戳
     */
    private long getTime() {
        // https://zhuanlan.zhihu.com/p/268884637
        // 我试了，直接调用 tps 在 3W ,做时钟缓存在 2.6W
        return System.currentTimeMillis();
    }
}

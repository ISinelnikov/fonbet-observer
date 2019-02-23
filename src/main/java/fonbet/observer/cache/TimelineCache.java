package fonbet.observer.cache;

import fonbet.observer.dao.FTimelineDao;
import fonbet.observer.domain.FTimelineDto;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static java.lang.System.currentTimeMillis;

@Service
public class TimelineCache {
    private static final Logger logger = LoggerFactory.getLogger(TimelineCache.class);

    private static final long EXPIRE_CACHE = 60 * 60 * 1000;
    private static final long LIFE_TIME = TimeUnit.HOURS.toMillis(12);

    private final Map<String, TimelineData> pairToMatch = new ConcurrentHashMap<>();

    private final FTimelineDao fTimelineDao;

    public TimelineCache(FTimelineDao fTimelineDao) {
        this.fTimelineDao = Objects.requireNonNull(fTimelineDao, "Timeline dao can't be null.");
    }

    @Scheduled(fixedDelay = EXPIRE_CACHE, initialDelay = EXPIRE_CACHE)
    private void expireCache() {
        long currentTimeMillis = currentTimeMillis();
        pairToMatch.forEach((key, data) -> {
            if (currentTimeMillis - data.getTimestamp() > LIFE_TIME) {
                pairToMatch.remove(key, data);
            }
        });
    }

    public void put(FTimelineDto timelineDto) {
        pairToMatch.compute(timelineDto.getHref(), (checkedUtl, timelineData) -> {
            String pair = timelineDto.getPair();
            TimelineData data = new TimelineData(timelineDto);
            if (timelineData == null) {
                logger.debug("Add new pair: {} match dto: {}.", pair, timelineDto);
                pairToMatch.put(pair, data);
                fTimelineDao.addTimelineRow(timelineDto);
            } else {
                if (!timelineData.getTimelineDto().equals(timelineDto)) {
                    logger.debug("Update pair: {} match dto: {}.", pair, timelineDto);
                    pairToMatch.put(pair, data);
                    fTimelineDao.addTimelineRow(timelineDto);
                } else {
                    timelineData.setTimestamp(currentTimeMillis());
                }
            }
            return data;
        });
    }

    public static class TimelineData {
        private final FTimelineDto timelineDto;
        private long timestamp;

        public TimelineData(FTimelineDto timelineDto) {
            this.timelineDto = Objects.requireNonNull(timelineDto, "Timeline dto can't be null.");
            this.timestamp = currentTimeMillis();
        }

        public FTimelineDto getTimelineDto() {
            return timelineDto;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}

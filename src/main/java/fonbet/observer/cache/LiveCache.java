package fonbet.observer.cache;

import fonbet.observer.dao.FLiveDao;
import fonbet.observer.domain.FLiveTimelineDto;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LiveCache {
    private static final Logger logger = LoggerFactory.getLogger(LiveCache.class);

    private final Map<String, Map<String, MatchData>> pairToMatch = new ConcurrentHashMap<>();

    private static final long EXPIRE_VALUE = TimeUnit.HOURS.toMillis(6);

    //1 minute to milliseconds
    private static final long REFRESH_TIME = 60 * 1000;

    private final FLiveDao fLiveDao;

    public LiveCache(FLiveDao fLiveDao) {
        this.fLiveDao = Objects.requireNonNull(fLiveDao, "Live dao can't be null.");
    }

    public void put(FLiveTimelineDto matchDto) {
        Map<String, MatchData> matchDataMap = pairToMatch.get(matchDto.getPairName());

        String tabName = matchDto.getTimelineDto().getPair();
        if (matchDataMap == null) {
            Map<String, MatchData> tabToData = new ConcurrentHashMap<>();
            tabToData.put(tabName, new MatchData(matchDto));

            fLiveDao.addMatchRow(matchDto);
            logger.debug("Add live match dto to cache: {}.", matchDto);
            pairToMatch.put(matchDto.getTimelineDto().getPair(), tabToData);
        } else {
            MatchData data = matchDataMap.get(tabName);

            if (data != null) {
                if (!data.getMatchDto().equals(matchDto)) {
                    fLiveDao.addMatchRow(matchDto);
                    matchDataMap.put(tabName, new MatchData(matchDto));
                    logger.debug("Update live match dto to cache: {}.", matchDto);
                }
            } else {
                fLiveDao.addMatchRow(matchDto);
                matchDataMap.put(tabName, new MatchData(matchDto));
                logger.debug("Add live match dto to cache: {}.", matchDto);
            }
        }
    }

    @Scheduled(fixedDelay = REFRESH_TIME)
    private void clearUp() {
        long moment = System.currentTimeMillis();
        pairToMatch.forEach((pair, data) -> {
            logger.debug("Clean up for pair: {}.", pair);
            data.forEach((event, match) -> {
                if (moment - match.getTimestamp() > EXPIRE_VALUE) {
                    data.remove(event, match);
                }
            });
        });
    }

    public static class MatchData {
        private final FLiveTimelineDto matchDto;
        private final long timestamp;

        public MatchData(FLiveTimelineDto matchDto) {
            this.matchDto = Objects.requireNonNull(matchDto, "Match dto can't be null.");
            this.timestamp = System.currentTimeMillis();
        }

        public FLiveTimelineDto getMatchDto() {
            return matchDto;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}

package fonbet.observer.service;

import fonbet.observer.cache.LiveCache;
import fonbet.observer.domain.FLiveTimelineDto;

import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FLiveService {
    private static final Logger logger = LoggerFactory.getLogger(FLiveService.class);
    @Value("${path.live}")
    private String pathLive;

    private final WebDriver webDriver;
    private final LiveCache liveCache;

    private static final long TIMELINE_DELAY = 1_000;

    @Autowired
    public FLiveService(WebDriver webDriver, LiveCache liveCache) {
        this.webDriver = Objects.requireNonNull(webDriver, "Web driver can't be null.");
        this.liveCache = Objects.requireNonNull(liveCache, "Timeline cache can't be null.");
    }

    @PostConstruct
    private void getTimelinePage() {
        webDriver.get(pathLive);
    }

    @Scheduled(fixedDelay = TIMELINE_DELAY, initialDelay = TIMELINE_DELAY)
    private void parseTimelineTable() {
        //Get all matches tables
        List<WebElement> liveTables = webDriver
                .findElements(By.xpath("//*[@id=\"main\"]/div/div[3]/div/div/div/div/div[2]/section/div[1]/table/tbody"));

        liveTables.forEach(match -> {
            List<WebElement> tableRows = match.findElements(By.xpath("./tr"));
            int tableSize = tableRows.size();

            WebElement webElement = tableRows.get(0);
            List<WebElement> headerRows = webElement.findElements(By.xpath("./th"));
            String pairName = headerRows.get(0).getText();

            for (int rowIdx = 1; rowIdx < tableSize; rowIdx++) {
                WebElement tableRowElement = tableRows.get(rowIdx);

                List<WebElement> rowTds = tableRowElement.findElements(By.xpath("./td"));

                if (rowTds.size() == 12) {
                    String pair;
                    String href = "";
                    String additional;

                    List<WebElement> titleElements = rowTds.get(1).findElements(By.xpath("./div"));
                    if (titleElements.size() == 2) {
                        List<WebElement> commands = titleElements.get(0).findElements(By.xpath("./a"));
                        pair = titleElements.get(0).getText();
                        if (commands.size() == 1 && rowIdx == 1) {
                            pair = commands.get(0).getText();
                            href = commands.get(0).getAttribute("href");
                        }

                        additional = titleElements.get(1).getText();
                    } else {
                        logger.warn("Can't parse title information.");
                        continue;
                    }

                    FLiveTimelineDto fLiveTimelineDto = new FLiveTimelineDto(
                            pairName,
                            pair,
                            href,
                            additional,
                            rowTds.get(2).getText(),
                            rowTds.get(3).getText(),
                            rowTds.get(4).getText(),
                            rowTds.get(5).getText(),
                            rowTds.get(6).getText(),
                            rowTds.get(7).getText(),
                            rowTds.get(8).getText(),
                            rowTds.get(9).getText(),
                            rowTds.get(10).getText());

                    liveCache.put(fLiveTimelineDto);

                    logger.debug("Parse live match: {}.", fLiveTimelineDto);
                } else {
                    logger.warn("Can't parse tds list, list size: {}", rowTds.size());
                }
            }
        });
    }
}

package fonbet.observer.service;

import fonbet.observer.cache.TimelineCache;
import fonbet.observer.domain.FTimelineDto;

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
public class FTimelineService {
    private static final Logger logger = LoggerFactory.getLogger(FTimelineService.class);

    private static final String LIVE = "live!";

    @Value("${path.timeline}")
    private String pathTimeline;

    private final WebDriver webDriver;
    private final TimelineCache timelineCache;

    private static final long TIMELINE_DELAY = 1_000;

    @Autowired
    public FTimelineService(WebDriver webDriver, TimelineCache timelineCache) {
        this.webDriver = Objects.requireNonNull(webDriver, "Web driver can't be null.");
        this.timelineCache = Objects.requireNonNull(timelineCache, "Timeline cache can't be null.");
    }

    @PostConstruct
    private void getTimelinePage() {
        webDriver.get(pathTimeline);
    }

    @Scheduled(fixedDelay = TIMELINE_DELAY, initialDelay = TIMELINE_DELAY)
    private void parseTimelineTable() {
        //Get all matches tables
        List<WebElement> mathTables = webDriver
                .findElements(By.xpath("//*[@id=\"main\"]/div/div[3]/div/div/div/div/div[2]/section/div[1]/table/tbody"));

        mathTables.forEach(match -> {
            List<WebElement> tableRows = match.findElements(By.xpath("./tr"));
            int tableSize = tableRows.size();

            for (int rowIdx = 1; rowIdx < tableSize; rowIdx++) {
                WebElement tableRowElement = tableRows.get(rowIdx);

                List<WebElement> rowTds = tableRowElement.findElements(By.xpath("./td"));

                if (rowTds.size() == 12) {
                    String title;
                    String href;
                    String startTime;

                    List<WebElement> titleElements = rowTds.get(1).findElements(By.xpath("./div"));
                    if (titleElements.size() == 2) {
                        List<WebElement> commands = titleElements.get(0).findElements(By.xpath("./a"));
                        if (commands.size() == 1) {
                            title = commands.get(0).getText();
                            href = commands.get(0).getAttribute("href");
                        } else {
                            title = titleElements.get(0).getText();
                            href = "";
                            logger.warn("Can't found href to match: {}", title);
                        }

                        startTime = titleElements.get(1).getText();
                        if (startTime.contains(LIVE)) {
                            int beginLive = startTime.indexOf(LIVE);
                            startTime = startTime.substring(beginLive + LIVE.length()).trim();
                        }
                    } else {
                        logger.warn("Can't parse title information.");
                        continue;
                    }

                    FTimelineDto fTimelineDto = new FTimelineDto(title,
                            href,
                            startTime,
                            rowTds.get(2).getText(),
                            rowTds.get(3).getText(),
                            rowTds.get(4).getText(),
                            rowTds.get(5).getText(),
                            rowTds.get(6).getText(),
                            rowTds.get(7).getText(),
                            rowTds.get(8).getText(),
                            rowTds.get(9).getText(),
                            rowTds.get(10).getText());

                    timelineCache.put(fTimelineDto);
                } else {
                    logger.warn("Can't parse tds list, list size: {}", rowTds.size());
                }
            }
        });
    }
}

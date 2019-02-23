package fonbet.observer.configuration;

import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SeleniumConfiguration {
    @Value("${selenium.driver.chrome.path}")
    private String seleniumChromeDriverPath;

    @PostConstruct
    public void setSeleniumDriverPaths() {
        System.setProperty("webdriver.chrome.driver", seleniumChromeDriverPath);
    }

    @Bean("chromeDriver")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebDriver getChromeWebDriver() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }
}

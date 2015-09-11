package androidPages;

import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CamPreview_FBurst {
	
	private AppiumDriver driver;
	WebDriverWait wait;
	String clickableShutterBtnLocator = "description(\"Shutter button\").clickable(true).enabled(true)";

	
	public CamPreview_FBurst(AppiumDriver drive) {	        
		driver = drive;
		wait = new WebDriverWait(driver, 10);
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
	}
	
	public CamPreview_FBurst capture(Integer numberOfCaptures, Integer msTimeBetweenTaps) throws InterruptedException{
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(clickableShutterBtnLocator)));
		WebElement shutterBtn =  driver.findElementByAndroidUIAutomator(clickableShutterBtnLocator);
		for (int n = 0; n < numberOfCaptures; n++) {
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(clickableShutterBtnLocator)));
			shutterBtn.click();
			Thread.sleep(msTimeBetweenTaps);
        }
		return this;
	}
}

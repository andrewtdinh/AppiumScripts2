package androidPages;

import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CamPreview_Video {
	
	private AppiumDriver driver;
	WebDriverWait wait;
	String clickableShutterBtnLocator = "description(\"Shutter button\").clickable(true).enabled(true)";
	String modeBtnLocator = "description(\"Show switch camera mode list\")";
	String clickableModeBtnLocator = "description(\"Show switch camera mode list\").clickable(true).enabled(true)";

	
	public CamPreview_Video(AppiumDriver drive) {	        
		driver = drive;
		wait = new WebDriverWait(driver, 15);
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
	}
	
	public CamPreview_Video capture(Integer numberOfCaptures, Integer msTimeBetweenTaps) throws InterruptedException{
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(clickableShutterBtnLocator)));
		WebElement shutterBtn =  driver.findElementByAndroidUIAutomator(clickableShutterBtnLocator);
		for (int n = 0; n < numberOfCaptures; n++) {
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(modeBtnLocator)));
			shutterBtn.click();
			Thread.sleep(3000);
			//wait.until(ExpectedConditions.invisibilityOfElementLocated(MobileBy.AndroidUIAutomator(clickableModeBtnLocator)));
			shutterBtn.click();
			System.out.printf("..%d", n);
			Thread.sleep(msTimeBetweenTaps);
        }
		System.out.println();
		return this;
	}
}

package androidPages;

import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CamPreview_HDR {
	
	private AppiumDriver driver;
	WebDriverWait wait;
	String clickableShutterBtnLocator = "description(\"Shutter button\").clickable(true).enabled(true)";
	String processingTextLocator = "textStartsWith(\"Processing\")";
	String modeBtnLocator = "description(\"Show switch camera mode list\")";

	
	public CamPreview_HDR(AppiumDriver drive) {	        
		driver = drive;
		wait = new WebDriverWait(driver, 8);
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
	}
	
	public CamPreview_HDR capture(Integer numberOfCaptures, Integer msTimeBetweenTaps) throws InterruptedException{
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(clickableShutterBtnLocator)));
		WebElement shutterBtn =  driver.findElementByAndroidUIAutomator(clickableShutterBtnLocator);
		for (int n = 0; n < numberOfCaptures; n++) {
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(modeBtnLocator)));
			//wait.until(ExpectedConditions.invisibilityOfElementLocated(MobileBy.AndroidUIAutomator(processingTextLocator)));
			shutterBtn.click();
			System.out.printf("..%d", n);
			Thread.sleep(msTimeBetweenTaps);
        }
		return this;
	}
}

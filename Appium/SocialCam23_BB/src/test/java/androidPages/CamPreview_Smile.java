package androidPages;

import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CamPreview_Smile {
	
	private AppiumDriver driver;
	WebDriverWait wait;
	String clickableShutterBtnLocator = "description(\"Shutter button\").clickable(true).enabled(true)";
	String modeBtnLocator = "description(\"Show switch camera mode list\")";
	String clickableModeBtnLocator = "description(\"Show switch camera mode list\").clickable(true).enabled(true)";

	
	public CamPreview_Smile(AppiumDriver drive) {	        
		driver = drive;
		wait = new WebDriverWait(driver, 15);
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
	}
	
	public CamPreview_Smile capture(Integer numberOfCaptures, Integer msTimeBetweenTaps) throws InterruptedException{
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(clickableShutterBtnLocator)));
		WebElement shutterBtn =  driver.findElementByAndroidUIAutomator(clickableShutterBtnLocator);
		for (int n = 0; n < numberOfCaptures; n++) {
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(modeBtnLocator)));
			shutterBtn.click();
			Thread.sleep(1300);
			//wait.until(ExpectedConditions.invisibilityOfElementLocated(MobileBy.AndroidUIAutomator(clickableModeBtnLocator)));
			shutterBtn.click();
			Thread.sleep(msTimeBetweenTaps);
        }
		return this;
	}
}

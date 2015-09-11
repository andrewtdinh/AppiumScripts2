package androidPages;

import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CamPreview_Depth {
	
	private AppiumDriver driver;
	WebDriverWait wait;
	String shutterBtnLocator = "description(\"Shutter button\")";
	String clickableShutterBtnLocator = "description(\"Shutter button\").clickable(true).enabled(true)";
	String modeBtnLocator = "description(\"Show switch camera mode list\")";
	String viceCamRedBorderLocator = "resourceId(\"com.intel.camera22:id/vice_camera_highlight_border_view1\")";
	
	
	public CamPreview_Depth(AppiumDriver drive) {	        
		driver = drive;
		wait = new WebDriverWait(driver, 10);
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
	}
	
	public CamPreview_Depth capture(Integer numberOfCaptures, Integer msTimeBetweenTaps) throws InterruptedException{
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(clickableShutterBtnLocator)));
		for (int n = 0; n < numberOfCaptures; n++) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(MobileBy.AndroidUIAutomator(viceCamRedBorderLocator)));
			//wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(clickableShutterBtnLocator)));
			driver.findElementByAndroidUIAutomator(shutterBtnLocator).click();
			System.out.printf("..%d", n);
			Thread.sleep(msTimeBetweenTaps);
        }
		System.out.println();
		return this;
	}
	public CamPreview_ModeMenu tapModeItem() {
		driver.findElementByAndroidUIAutomator(modeBtnLocator).click();
		return new CamPreview_ModeMenu(driver);
	}

}

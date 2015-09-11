package androidPages;

import java.util.concurrent.TimeUnit;
import io.appium.java_client.AppiumDriver;

public class CamPreviewDepth_ModeMenu {
	
	private AppiumDriver driver;
	String shutterBtnLocator = "description(\"Shutter button\")";
	
	public CamPreviewDepth_ModeMenu(AppiumDriver driver) {	        
		this.driver = driver;
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	public CamPreviewDepth_ModeMenu scrollToRight(Integer reps) throws InterruptedException{
		Thread.sleep(500);
		for (int n = 0; n < reps; n++) {
			driver.findElementByAndroidUIAutomator(shutterBtnLocator).click();
        }
		return this;
	}

}

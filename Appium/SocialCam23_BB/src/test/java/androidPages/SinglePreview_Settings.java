package androidPages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class SinglePreview_Settings {
	
	private AppiumDriver driver;
	WebDriverWait wait;
	String depthModeSelector = "text(\"Depth\")";
	String burstSelector = "text(\"Burst\")";
	String settingNameContainer = "resourceId(\"com.intel.camera22:id/setting_item_name\").enabled(true)";

	
	public SinglePreview_Settings(AppiumDriver drive) {	        
		driver = drive;
		wait = new WebDriverWait(driver, 10);
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
	}
	
	public boolean menuIsOpen() {
		if (!driver.findElementsByAndroidUIAutomator(settingNameContainer).isEmpty())
			return true;
		else
			return false;
	}
	
	public CamPreview_Depth selectDepthMode() throws InterruptedException{
		Thread.sleep(300);
		WebElement depthIcon = driver.findElementByAndroidUIAutomator(depthModeSelector);
		new TouchAction(driver).tap(depthIcon).perform();
		return new CamPreview_Depth(driver);
	}
	
	public CamPreviewBurst_SubMenu selectBurstMode() throws InterruptedException{
		Thread.sleep(300);
		WebElement burstIcon = driver.findElementByAndroidUIAutomator(burstSelector);
		new TouchAction(driver).tap(burstIcon).perform();
		return new CamPreviewBurst_SubMenu(driver);
	}
}

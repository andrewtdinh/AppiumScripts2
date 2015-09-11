package androidPages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

public class CamPreview_Single {
	
	private AppiumDriver driver;
	WebDriverWait wait;
	String modeBtnLocator = "description(\"Show switch camera mode list\")";
	String clickableShutterBtnLocator = "description(\"Shutter button\").clickable(true).enabled(true)";
	String clickableSettingsIcon = "resourceId(\"com.intel.camera22:id/camera_settings\").clickable(true).enabled(true)";
	String settingNameContainer = "resourceId(\"com.intel.camera22:id/setting_item_name\").enabled(true)";
	
	public CamPreview_Single(AppiumDriver drive) {	        
		driver = drive;
		wait = new WebDriverWait(driver, 10);
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
	}
	
	public CamPreview_Single capture(Integer numberOfCaptures, Integer msTimeBetweenTaps) throws InterruptedException{
		Thread.sleep(1000);
		for (int n = 0; n < numberOfCaptures; n++) {
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(clickableShutterBtnLocator)));
			driver.findElementByAndroidUIAutomator(clickableShutterBtnLocator).click();
			System.out.printf("..%d", n);
			Thread.sleep(msTimeBetweenTaps);
        }
		System.out.println();
		return this;
	}
	
	public CamPreview_Depth switchToDepthMode() throws InterruptedException {
		return tapModeItem().selectDepthMode();
	}
	
	public CamPreview_FBurst switchToFBurstMode() throws InterruptedException {
		return tapModeItem().selectBurstMode().selectFBurstMode();
	}
	
	public CamPreview_SBurst switchToSBurstMode() throws InterruptedException {
		return tapModeItem().selectBurstMode().selectSBurstMode();
	}
	
	public CamPreview_ModeMenu tapModeItem() {
		driver.findElementByAndroidUIAutomator(modeBtnLocator).click();
		return new CamPreview_ModeMenu(driver);
	}
	
	public SinglePreview_Settings tapSettingsIcon() {
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(clickableSettingsIcon)));
		driver.findElementByAndroidUIAutomator(clickableSettingsIcon).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(settingNameContainer)));
		System.out.println("Single Shot settings menu is opened.");
		return new SinglePreview_Settings(driver);
	}
	
}

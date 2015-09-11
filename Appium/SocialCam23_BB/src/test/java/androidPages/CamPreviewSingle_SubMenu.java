package androidPages;

import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;
import io.appium.java_client.AppiumDriver;

public class CamPreviewSingle_SubMenu {
	
	private AppiumDriver driver;
	WebDriverWait wait;
	String hdrSelector = "textStartsWith(\"HDR\")";
	String smileSelector = "textStartsWith(\"Smile\")";

	
	public CamPreviewSingle_SubMenu(AppiumDriver drive) {	        
		driver = drive;
		wait = new WebDriverWait(driver, 10);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	public CamPreview_HDR turnHDROn() throws InterruptedException{
		Thread.sleep(300);
		driver.findElementByAndroidUIAutomator(hdrSelector).click();
		return new CamPreview_HDR(driver);
	}
	
	public CamPreview_Single turnHDROff() throws InterruptedException{
		Thread.sleep(300);
		driver.findElementByAndroidUIAutomator(hdrSelector).click();
		return new CamPreview_Single(driver);
	}
	
	public CamPreview_Smile turnSmileOn() throws InterruptedException{
		Thread.sleep(300);
		driver.findElementByAndroidUIAutomator(hdrSelector).click();
		return new CamPreview_Smile(driver);
	}
	
	public CamPreview_Single turnSmileOff() throws InterruptedException{
		Thread.sleep(300);
		driver.findElementByAndroidUIAutomator(hdrSelector).click();
		return new CamPreview_Single(driver);
	}
}

package androidPages;

import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;
import io.appium.java_client.AppiumDriver;

public class CamPreviewBurst_SubMenu {
	
	private AppiumDriver driver;
	WebDriverWait wait;
	String fastBurstSelector = "text(\"Fast\")";
	String slowBurstSelector = "text(\"Slow\")";

	
	public CamPreviewBurst_SubMenu(AppiumDriver drive) {	        
		driver = drive;
		wait = new WebDriverWait(driver, 10);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	public CamPreview_FBurst selectFBurstMode() throws InterruptedException{
		Thread.sleep(300);
		driver.findElementByAndroidUIAutomator(fastBurstSelector).click();
		return new CamPreview_FBurst(driver);
	}
	
	public CamPreview_SBurst selectSBurstMode() throws InterruptedException{
		Thread.sleep(300);
		driver.findElementByAndroidUIAutomator(slowBurstSelector).click();
		return new CamPreview_SBurst(driver);
	}
}

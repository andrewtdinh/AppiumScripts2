package androidPages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class CamPreview_ModeMenu {
	
	private AppiumDriver driver;
	WebDriverWait wait;
/*	String midModeLocator = "className(\"android.widget.FrameLayout\").clickable(true).instance(1)";
	String rightModeLocator = "className(\"android.widget.FrameLayout\").clickable(true).instance(2)";
	String fourthModeLocator = "className(\"android.widget.FrameLayout\").clickable(true).instance(3)";*/
	String depthModeSelector = "text(\"Depth\")";
	String burstSelector = "text(\"Burst\")";

	
	public CamPreview_ModeMenu(AppiumDriver drive) {	        
		driver = drive;
		wait = new WebDriverWait(driver, 10);
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
	}
	//Now not used anymore because of new design
/*	public CamPreview_ModeMenu scrollToRight(Integer reps) throws InterruptedException{
		Thread.sleep(800);
		WebElement midMode = driver.findElementByAndroidUIAutomator(midModeLocator);
		WebElement rightMode = driver.findElementByAndroidUIAutomator(rightModeLocator);
		int startX = util.getCenterXCoordinate(rightMode);
		int startY = util.getCenterYCoordinate(rightMode);
		int endX = util.getCenterXCoordinate(midMode);
		int endY = util.getCenterYCoordinate(midMode);
		//TouchAction scrollRight = new TouchAction(driver).press(rightMode).moveTo(midMode).release();
		for (int n = 0; n < reps; n++) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(MobileBy.AndroidUIAutomator(fourthModeLocator)));
			new TouchAction(driver).press(startX, startY).moveTo(endX, endY).release().perform();
			Thread.sleep(800);
        }
		return this;
	}*/

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

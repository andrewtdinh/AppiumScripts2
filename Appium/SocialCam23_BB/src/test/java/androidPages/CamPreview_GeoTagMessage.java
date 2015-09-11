package androidPages;

import androidPages.CamPreview_Single;

// import org.openqa.selenium.*;
// import org.openqa.selenium.By;
// import org.openqa.selenium.support.ui.WebDriverWait;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.interactions.HasTouchScreen;
// import org.openqa.selenium.interactions.TouchScreen;
// import org.openqa.selenium.remote.CapabilityType;
// import org.openqa.selenium.remote.DesiredCapabilities;
// import org.openqa.selenium.remote.RemoteTouchScreen;
// import org.openqa.selenium.remote.RemoteWebDriver;
// import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.WebElement;

// import java.io.File;
// import java.net.URL;
// import java.text.ParseException;
// import java.text.SimpleDateFormat;
// import java.util.regex.*;
// import java.util.*;
import java.util.concurrent.TimeUnit;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import static org.junit.Assert.*;

public class CamPreview_GeoTagMessage {
	
	private AppiumDriver driver;
	
	String cancelButtonLocator = "text(\"Cancel\")";
	String geoTagDialogLocator = "textContains(\"Enable geo location\")";
	
	public CamPreview_GeoTagMessage(AppiumDriver driver) {	        
		this.driver = driver; 
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}
	
	public CamPreview_Single tapCancel() {
		//driver.findElementByAndroidUIAutomator("resourceId(\"com.intel.camera22:id/button3\")").click();
		driver.findElementByAndroidUIAutomator(cancelButtonLocator).click();
		return new CamPreview_Single(driver);
	}
	
	public CamPreview_Single checkGeoLocationMessage() {
		//if (!driver.findElementsByAndroidUIAutomator("resourceId(\"com.intel.camera22:id/message\")").isEmpty()) {
		if (!driver.findElementsByAndroidUIAutomator(geoTagDialogLocator).isEmpty()) {
			System.out.println("Found the Geolocation diaglog!!");
			return tapCancel();
	    }
	    else {	
			System.out.println("The Geolocation dialog was not found.  Camera is launch!");
			return new CamPreview_Single(driver);
		}
	}

}

package androidPages;

// import org.openqa.selenium.*;
// import org.openqa.selenium.By;
// import org.openqa.selenium.support.ui.WebDriverWait;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.interactions.HasTouchScreen;
// import org.openqa.selenium.interactions.TouchScreen;
// import org.openqa.selenium.remote.CapabilityType;
// import org.openqa.selenium.remote.RemoteTouchScreen;
// import org.openqa.selenium.remote.RemoteWebDriver;
// import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

// import java.net.URL;
// import java.text.ParseException;
// import java.text.SimpleDateFormat;
// import java.util.regex.*;
// import java.util.*;










import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import static org.junit.Assert.*;
import static org.junit.Assert.*;

public class BB {
	private AppiumDriver driver;
	
	public BB(AppiumDriver driver) {	        
		this.driver = driver;
	}
	// Method to put the driver to sleep for a number of mili-seconds
    public static void wait(Integer milisec) {
        try {
            Thread.sleep(milisec);
        } catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
    }
    // Method to tap on a coordinate in percentage
    public void tapScreen(Double x_percent, Double y_percent) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Double> tapObject = new HashMap<String, Double>();
        tapObject.put("x", x_percent); // in percentage of the screen width from the left
        tapObject.put("y", y_percent); // in percentage of the screen height from the top
        js.executeScript("mobile: tap", tapObject);
    }
    // Method to find the number of files from a particular folder on the device.
	public int findNumberOfFiles(String dFolderPath) throws InterruptedException {
		int numOfFiles = 0;
		Thread.sleep(500);
		try {
			ProcessBuilder process = new ProcessBuilder("adb", "shell", "ls", dFolderPath, "|", "wc", "-l");
			Process p = process.start();
			//Thread.sleep(5000);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			p.waitFor();
			while ((line = br.readLine()) != null) {
				if (!line.equals(""))
					numOfFiles = Integer.parseInt(line); 
			}
		} catch (Exception e){
			System.out.println(e);
		}
		return numOfFiles;
	}
	// Method to retrieve a certain value from the camera preference xml files
	public String getValueFromXML(String fileName, String filterTerm, String searchPattern, Integer searchGroupIndex) throws InterruptedException {
		String xmlPath = "/data/data/com.intel.camera22/shared_prefs/";
		String xmlLine = "";
		Thread.sleep(500);
		try {
			ProcessBuilder process = new ProcessBuilder("adb", "shell", "cat", xmlPath + fileName, "|", "grep", filterTerm);
			Process p = process.start();
			Thread.sleep(5000);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			p.waitFor();
			while ((line = br.readLine()) != null) {
				if (!line.equals(""))
					xmlLine = line.toString(); 
			}
		} catch (Exception e){
			System.out.println(e);
		}
		Pattern compiledPattern = Pattern.compile(searchPattern);
		Matcher m = compiledPattern.matcher(xmlLine);
		String xmlValue = "";
		if (m.find()) {
            xmlValue = m.group(searchGroupIndex).toString();
            System.out.printf("The value for the '%s' filter is '%s'.\n", filterTerm, xmlValue);
        }
        else {
            System.out.println("Could not retrieve a value from the camera preference xml files!");
            assertTrue(false);
        }
		return xmlValue;
	}
	// Method to get the current camera mode
	public String getCameraMode() throws InterruptedException{
		String modeBtnLocator = "description(\"Show switch camera mode list\")";
		String hdrON = "text(\"HDR\nON\")";
		String smileON = "text(\"Smile\nON\")";
		String currentMode = "";
		Thread.sleep(500);
		String valueString = getValueFromXML("mode_selected.xml", "Mode", "value=.(\\d+).", 1);
		Integer valueInt = Integer.parseInt(valueString); 
		switch(valueInt){
			case 1:
				driver.findElementByAndroidUIAutomator(modeBtnLocator).click();
				Thread.sleep(400);
				if (!driver.findElementsByAndroidUIAutomator(hdrON).isEmpty()) {
					System.out.println("Current mode is HDR.");
					currentMode = "hdr";
					tapScreen(0.5, 0.5);
					break;
				}
				else if (!driver.findElementsByAndroidUIAutomator(smileON).isEmpty()) {
					System.out.println("Current mode is Smile.");
					currentMode = "smile";
					tapScreen(0.5, 0.5);
					break;
				}
				else {
					System.out.println("Current mode is Single Shot.");
					currentMode = "single";
					tapScreen(0.5, 0.5);
					break;
				}
			case 5:
				System.out.println("Current mode is Burst.");
				currentMode = "burst";
				break;
			case 7:
				System.out.println("Current mode is Perfectshot.");
				currentMode = "perfect";
				break;
			case 9:
				System.out.println("Current mode is Video.");
				currentMode = "video";
				break;	
			case 11:
				System.out.println("Current mode is Panorama.");
				currentMode = "pano";
				break;
			case 12:
				System.out.println("Current mode is Depth.");
				currentMode = "depth";
				break;
			default:
				System.out.println("Current camera mode is not recognized.");
				assertTrue(false);
		}
		return currentMode;
	}
	// Method to get the current camera facing direction
	public String getCameraDirection() throws InterruptedException{
		String currentDirection = "";
		Thread.sleep(500);
		String valueString = getValueFromXML("com.intel.camera22_preferences_0.xml ", "id_key", ">(\\d+)<", 1);
		Integer valueInt = Integer.parseInt(valueString);
		switch(valueInt){
			case 0:
				System.out.println("Camera is back-facing.");
				currentDirection = "back";
				break;
			case 1:
				System.out.println("Camera is front-facing.");
				currentDirection = "front";
				break;
			default:
				System.out.println("Current camera mode is not recognized.");
				assertTrue(false);
		}
		return currentDirection;
	}
	// Method to remove a folder from the device
	public void deleteDeviceFolder(String dFolderPath, int waitTime) {
		try {
			ProcessBuilder process = new ProcessBuilder("adb", "shell", "rm", "-r", dFolderPath);
			process.start();
			Thread.sleep(waitTime);
			process = new ProcessBuilder("adb", "shell", "am", "broadcast", "-a", "android.intent.action.MEDIA_MOUNTED", "-d", "file:///mnt/sdcard");
			process.start();
			Thread.sleep(waitTime);
		} catch (Exception e){}
	}
	// Method to calculate a WebElement x-coordinate
	public int getCenterXCoordinate(WebElement el) {
		//Find the item's upper left point.
		Point p = ((Locatable)el).getCoordinates().onPage();
		//Find the item's width to calculate its center
		int itemWidth = el.getSize().width;
		int xCoordinate = p.x + (itemWidth / 2);
		System.out.printf("X coordinate is %d\n", xCoordinate);
        return xCoordinate;
	}
	// Method to calculate a WebElement y-coordinate
	public int getCenterYCoordinate(WebElement el) {
		//Find the item's upper left point.
		Point p = ((Locatable)el).getCoordinates().onPage();
		System.out.printf("Y coordinate is %d\n", p.y);
		//Find the item's height to calculate its center
		int itemHeight = el.getSize().height;
		int yCoordinate = p.y + (itemHeight / 2);
		System.out.printf("Y coordinate is %d\n", yCoordinate);
        return yCoordinate;
	}
	
}

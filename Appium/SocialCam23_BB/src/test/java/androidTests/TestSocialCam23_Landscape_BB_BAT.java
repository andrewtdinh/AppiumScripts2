package androidTests;

import androidPages.CamPreview_Depth;
import androidPages.CamPreview_FBurst;
import androidPages.CamPreview_HDR;
import androidPages.CamPreview_SBurst;
import androidPages.CamPreview_Single;
import androidPages.CamPreview_GeoTagMessage;
import androidPages.BB;
import androidPages.CamPreview_Smile;
import androidPages.CamPreview_Video;
import androidPages.SinglePreview_Settings;
import static org.junit.Assert.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestSocialCam23_Landscape_BB_BAT {
    private AppiumDriver driver;
	private CamPreview_GeoTagMessage geoTagMsg;
	private CamPreview_Single singlePreview;
	private BB util;
	WebDriverWait wait;
	
	String pathTo100ANDRO = "/sdcard/DCIM/100ANDRO";
    
    
    @Before
    public void setUp() throws Exception{
        // set up appium: specify the path to the app folder from your computer.  Only if you need to install the apk
        //File app = new File("C:\\Users\\trieutdx\\Downloads\\apks\\Social Apps", "SocialGallery22_ARM.apk");
        DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "4.4");
		capabilities.setCapability("deviceName", "BB");
        capabilities.setCapability("deviceOrientation", "landscape");  //New
        //The line below is used only when the apk needs to be installed.
        //capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("appPackage", "com.intel.camera22");
        capabilities.setCapability("appActivity", ".Camera");
        
		driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		wait = new WebDriverWait(driver, 8);
		geoTagMsg = new CamPreview_GeoTagMessage(driver);
		util = new BB(driver);
		
		singlePreview = geoTagMsg.checkGeoLocationMessage();
    }

    @After
    public void tearDown() throws Exception {
		driver.closeApp();
        driver.quit();
    }

    //@Test
    public void testSingleCapture20X() throws Exception{
        System.out.println("@@@...Running testSingleCapture20X...@@@"); 
        Thread.sleep(10000);
		int filesToCapture = 1000; 
		int filesPreCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder before capture.\n", filesPreCapture);
		singlePreview.capture(filesToCapture, 800);
		Thread.sleep(2000);
		int filesAfterCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder after capture.\n", filesAfterCapture);
		int filesCaptured = filesAfterCapture - filesPreCapture;
		if (filesCaptured == filesToCapture)
			System.out.printf("'%d' Single Shot images were captured out of the specified number of '%d'!\n", filesCaptured, filesToCapture);
		else {
			System.out.printf("'%d' Single Shot images were captured out of the specified number of '%d'.  Capture failed!!\n", filesCaptured, filesToCapture);
			assertTrue(false);
		}
        System.out.println("@@@...Finished testSingleCapture20X...@@@");
    }
 
    //@Test
    public void testDepthCapture20X() throws InterruptedException{
        System.out.println("@@@...Running testDepthCapture20X...@@@"); 
		int filesToCapture = 20; 
		int filesPreCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder before depth capture.\n", filesPreCapture);
		CamPreview_Depth depthPreview = singlePreview.switchToDepthMode();
		depthPreview.capture(filesToCapture, 500);
		Thread.sleep(2000);
		int filesAfterCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder after depth capture.\n", filesAfterCapture);
		int filesCaptured = filesAfterCapture - filesPreCapture;
		if (filesCaptured == filesToCapture)
			System.out.printf("'%d' depth images were captured out of the specified number of '%d'!\n", filesCaptured, filesToCapture);
		else {
			System.out.printf("'%d' depth images were captured out of the specified number of '%d'.  Capture failed!!\n", filesCaptured, filesToCapture);
			assertTrue(false);
		}
        System.out.println("@@@...Finished testDepthCapture20X...@@@");
    }
    
    //@Test
    public void testFastBurstCapture20X() throws InterruptedException{
        System.out.println("@@@...Running testFastBurstCapture20X...@@@"); 
        int filesPerSet = 10;
		int setsToCapture = 20; 
		int filesPreCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder before fast burst capture.\n", filesPreCapture);
		CamPreview_FBurst fBurstPreview = singlePreview.switchToFBurstMode();
		fBurstPreview.capture(setsToCapture, 500);
		Thread.sleep(2000);
		int filesAfterCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder after fast burst capture.\n", filesAfterCapture);
		int filesCaptured = filesAfterCapture - filesPreCapture;
		if (filesCaptured == (setsToCapture * filesPerSet))
			System.out.printf("'%d' fast burst images were captured out of the specified number of '%d'!\n", (filesCaptured/filesPerSet), setsToCapture);
		else {
			System.out.printf("'%d' fast burst images were captured out of the specified number of '%d'.  Capture failed!!\n", (filesCaptured/filesPerSet), setsToCapture);
			assertTrue(false);
		}
        System.out.println("@@@...Finished testFastBurstCapture20X...@@@");
    }
    
    //@Test
    public void testSlowBurstCapture20X() throws InterruptedException{
        System.out.println("@@@...Running testSlowBurstCapture20X...@@@"); 
        int filesPerSet = 10;
		int setsToCapture = 20; 
		int filesPreCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder before slow burst capture.\n", filesPreCapture);
		CamPreview_SBurst sBurstPreview = singlePreview.switchToSBurstMode();
		sBurstPreview.capture(setsToCapture, 500);
		Thread.sleep(2000);
		int filesAfterCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder after slow burst capture.\n", filesAfterCapture);
		int filesCaptured = filesAfterCapture - filesPreCapture;
		if (filesCaptured == (setsToCapture * filesPerSet))
			System.out.printf("'%d' slow burst images were captured out of the specified number of '%d'!\n", (filesCaptured/filesPerSet), setsToCapture);
		else {
			System.out.printf("'%d' slow burst images were captured out of the specified number of '%d'.  Capture failed!!\n", (filesCaptured/filesPerSet), setsToCapture);
			assertTrue(false);
		}
        System.out.println("@@@...Finished testSlowBurstCapture20X...@@@");
    }
    
    //@Test
    public void testTempHDRCapture() throws Exception{
        System.out.println("@@@...Running testTempHDRCapture...@@@"); 
        Thread.sleep(8000);
		int filesToCapture = 400; 
		int filesPreCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder before capture.\n", filesPreCapture);
		CamPreview_HDR preview = new CamPreview_HDR(driver);
		preview.capture(filesToCapture, 400);
		Thread.sleep(1000);
		int filesAfterCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder after capture.\n", filesAfterCapture);
		int filesCaptured = filesAfterCapture - filesPreCapture;
		if (filesCaptured == filesToCapture)
			System.out.printf("'%d' images were captured out of the specified number of '%d'!\n", filesCaptured, filesToCapture);
		else {
			System.out.printf("'%d' images were captured out of the specified number of '%d'.  Capture failed!!\n", filesCaptured, filesToCapture);
			assertTrue(false);
		}
        System.out.println("@@@...Finished testTempHDRCapture...@@@");
    }
    
    //@Test
    public void testTempSmileCapture() throws Exception{
        System.out.println("@@@...Running testTempSmileCapture...@@@"); 
        Thread.sleep(8000);
		int filesToCapture = 500; 
		int filesPreCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder before capture.\n", filesPreCapture);
		CamPreview_Smile preview = new CamPreview_Smile(driver);
		preview.capture(filesToCapture, 400);
		Thread.sleep(1000);
		int filesAfterCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder after capture.\n", filesAfterCapture);
		int filesCaptured = filesAfterCapture - filesPreCapture;
		if (filesCaptured == filesToCapture)
			System.out.printf("'%d' images were captured out of the specified number of '%d'!\n", filesCaptured, filesToCapture);
		else {
			System.out.printf("'%d' images were captured out of the specified number of '%d'.  Capture failed!!\n", filesCaptured, filesToCapture);
			assertTrue(false);
		}
        System.out.println("@@@...Finished testMultipleCapture...@@@");
    }
    
    //@Test
    public void testTempVideoCapture() throws Exception{
        System.out.println("@@@...Running testTempVideoCapture...@@@"); 
        Thread.sleep(8000);
		int filesToCapture = 500; 
		int filesPreCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder before capture.\n", filesPreCapture);
		CamPreview_Video preview = new CamPreview_Video(driver);
		preview.capture(filesToCapture, 400);
		Thread.sleep(2000);
		int filesAfterCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder after capture.\n", filesAfterCapture);
		int filesCaptured = filesAfterCapture - filesPreCapture;
		if (filesCaptured == filesToCapture)
			System.out.printf("'%d' images were captured out of the specified number of '%d'!\n", filesCaptured, filesToCapture);
		else {
			System.out.printf("'%d' images were captured out of the specified number of '%d'.  Capture failed!!\n", filesCaptured, filesToCapture);
			assertTrue(false);
		}
        System.out.println("@@@...Finished testTempVideoCapture...@@@");
    }

    //@Test
    public void testTempDepthCapture() throws InterruptedException{
        System.out.println("@@@...Running testTempDepthCapture...@@@"); 
        Thread.sleep(8000);
		int filesToCapture = 1000; 
		int filesPreCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder before depth capture.\n", filesPreCapture);
		CamPreview_Depth depthPreview = new CamPreview_Depth(driver);
		depthPreview.capture(filesToCapture, 500);
		Thread.sleep(2000);
		int filesAfterCapture = util.findNumberOfFiles(pathTo100ANDRO);
		System.out.printf("'%d' files are found in 100ANDRO folder after depth capture.\n", filesAfterCapture);
		int filesCaptured = filesAfterCapture - filesPreCapture;
		if (filesCaptured == filesToCapture)
			System.out.printf("'%d' depth images were captured out of the specified number of '%d'!\n", filesCaptured, filesToCapture);
		else {
			System.out.printf("'%d' depth images were captured out of the specified number of '%d'.  Capture failed!!\n", filesCaptured, filesToCapture);
			assertTrue(false);
		}
        System.out.println("@@@...Finished testTempDepthCapture...@@@");
    }
    
    @Test
    public void testSingleSettingsChange() throws Exception{
    	String modeBtnLocator = "description(\"Show switch camera mode list\")";
    	String hdrON = "text(\"HDR\nON\")";
		String smileON = "text(\"Smile\nON\")";
    	Thread.sleep(8000);
    	
        System.out.println("@@@...testSingleSettingsChange...@@@");
        String cameraFacing = util.getCameraDirection();
        String cameraMode = util.getCameraMode();
        System.out.printf("Camera direction:  '%s'", cameraFacing);
        System.out.printf("Camera mode:  '%s'", cameraMode);
        
        SinglePreview_Settings singleSettingsMenu = singlePreview.tapSettingsIcon();
        if (singleSettingsMenu.menuIsOpen())
        	System.out.println("Settings menu is open!");
        else
        	System.out.println("Settings menu is not open!");
        
        util.tapScreen(.5, .5);
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(modeBtnLocator)));
        driver.findElementByAndroidUIAutomator(modeBtnLocator).click();
        Thread.sleep(4000);
        WebElement el= driver.findElementByAndroidUIAutomator(hdrON);
        String textAttribute = el.getAttribute("android:textColor");
        System.out.printf("Text color attribute:  '%s'", textAttribute);
        	
        System.out.println("@@@...testSingleSettingsChange...@@@");
    }
}
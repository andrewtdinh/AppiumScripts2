package androidTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.*;
import java.util.*;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.RemoteWebElement;


import static org.junit.Assert.*;

public class TestSocialCameraBAT_Portrait_SBY {
    private WebDriver driver;
    private WebElement menu;
    private WebDriverWait wait;
    
    @Before
    public void setUp() throws Exception {
        // set up appium: specify the path to the app folder from your computer.  Only if you need to install the apk
        //File app = new File("C:\\Users\\trieutdx\\Downloads\\apks\\Social Apps", "SocialGallery22_ARM.apk");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("device","Android");
        capabilities.setCapability("deviceOrientation", "portrait");  //New
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(CapabilityType.VERSION, "4.4");
        capabilities.setCapability(CapabilityType.PLATFORM, "Windows");
        //The line below is used only when the apk needs to be installed.
        //capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("app-package", "com.intel.camera22");
        capabilities.setCapability("app-activity", ".Camera");
        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        
        if (!driver.findElements(By.name(Data.REMEMBER_LOCATION)).isEmpty()) {
            Dut.tap(driver, "name", "Cancel", 1, 500);
        }
        if (Dut.getCamDirection(driver) == 2) {
            wait = new WebDriverWait(driver, 15);
            Dut.tap(driver, "name", "Front and back camera switch", 1, 500);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
            Dut.wait(driver, 500);
        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
    @Test
    public void checkDisplayedElementsInSingleMode(){
        wait = new WebDriverWait(driver, 15);
        Dut.wait(driver, 500);
        String changeMode = "com.intel.camera22:id/mode_button";
        String groupSingle = "com.intel.camera22:id/mode_wave_photo";
        String modeVideo = "com.intel.camera22:id/mode_wave_video";
        String groupBurst = "com.intel.camera22:id/mode_wave_burst";
        String modePerfect = "com.intel.camera22:id/mode_wave_perfectshot";
        String modePanorama = "com.intel.camera22:id/mode_wave_panorama";
        String modeHDR = "com.intel.camera22:id/mode_wave_hdr";
        String modeSmile = "com.intel.camera22:id/mode_wave_smile";
        String modeSBurst = "com.intel.camera22:id/mode_wave_burst_slow";
        String modeFBurst = "com.intel.camera22:id/mode_wave_burst_fast";
    
        System.out.println("@@@...Running checkDisplayedElementsInSingleMode...@@@"); 
        //Verify Shutter button availability
        assertTrue(Dut.verifyExist(driver, "name", "Shutter button"));
        Dut.changeMode(driver, "single");
        //Capture a single shot image, verify thumbnail existence and go to Gallery.  Then delete the image.
        Dut.imageCapture(driver, "single", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
        Dut.wait(driver, 1000);
        //Tap on the thumbnail
        Dut.tap(driver, "name", "Most recent photo", 1, 100);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Most recent photo")));
		Dut.wait(driver, 1500);
        //Delete the image folder to clean up
		Dut.goToAlbumView(driver, "Full View");
        Dut.deleteFolder(driver, "100ANDRO", 3);
        Dut.wait(driver, 700);
        Dut.sendKeyEvent(driver, 4);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Verify Change Mode icon availability
        assertTrue(Dut.verifyExist(driver, "id", changeMode));
        //Tap of the mode icon to verify other icons
        Dut.tap(driver, "id", changeMode, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(groupSingle)));
        //Verify the group icons (Single grouple, Video group, Burst group, Perfectshot group, and Panorama group
        assertTrue(Dut.verifyExist(driver, "id", groupSingle));
        assertTrue(Dut.verifyExist(driver, "id", modeVideo));
        assertTrue(Dut.verifyExist(driver, "id", groupBurst));
        assertTrue(Dut.verifyExist(driver, "id", modePerfect));
        assertTrue(Dut.verifyExist(driver, "id", modePanorama));
        //Verify shooting mode icons under the Single group by switching to Video mode first and then clicking on the Single group icon
        Dut.tap(driver, "id", modeVideo, 1, 100);
        Dut.tap(driver, "id", groupSingle, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeHDR));
        assertTrue(Dut.verifyExist(driver, "id", modeSmile));
        //Tap on the Burst group and verify the modes under it:
        Dut.tap(driver, "id", groupBurst, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeFBurst));
        assertTrue(Dut.verifyExist(driver, "id", modeSBurst));
        //Tap on the Single shooting group and then tap outside to get out 
        Dut.tap(driver, "id", groupSingle, 1, 100);
        Dut.tap(driver, 0.2, 0.3);
        //Verify icons on the left menu
        assertTrue(Dut.verifyExist(driver, "name", "Camera settings"));
        assertTrue(Dut.verifyExist(driver, "name", "Flash settings"));
        assertTrue(Dut.verifyExist(driver, "name", "Face recognition"));
        assertTrue(Dut.verifyExist(driver, "name", "Front and back camera switch"));
        //Switch to front-facing camera to verify displayed icons
        Dut.tap(driver, "name", "Front and back camera switch", 1, 500);
        //Verify icons on the left menu
        assertTrue(Dut.verifyExist(driver, "name", "Camera settings"));
        assertTrue(Dut.verifyExist(driver, "name", "Face recognition"));
        assertTrue(Dut.verifyExist(driver, "name", "Front and back camera switch"));
        //Verify the shutter button and change mode icon exists
        assertTrue(Dut.verifyExist(driver, "name", "Shutter button"));
        assertTrue(Dut.verifyExist(driver, "id", changeMode));
        //Capture a single shot image on front-facing camera, verify thumbnail existence and go to Gallery.  Then delete the image.
        Dut.imageCapture(driver, "single", 1, 800);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
        Dut.wait(driver, 1000);
        //Tap on the thumbnail
        Dut.tap(driver, "name", "Most recent photo", 1, 100);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Most recent photo")));
		Dut.wait(driver, 1500);
        //Delete the image folder to clean up
		Dut.goToAlbumView(driver, "Full View");
        Dut.deleteFolder(driver, "100ANDRO", 3);
        Dut.wait(driver, 700);
        Dut.sendKeyEvent(driver, 4);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Tap of the mode icon to verify other icons
        Dut.tap(driver, "id", changeMode, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(groupSingle)));
        //Verify the group icons (Single grouple, Video group, Burst group, Perfectshot group, and Panorama group
        assertTrue(Dut.verifyExist(driver, "id", groupSingle));
        assertTrue(Dut.verifyExist(driver, "id", modeVideo));
        //Tap outside and switch to back-facing camera
        Dut.tap(driver, 0.2, 0.2);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
		Dut.wait(driver, 500);
        Dut.tap(driver, "name", "Front and back camera switch", 1, 500);
        System.out.println("@@@...Finished checkDisplayedElementsInSingleMode...@@@");
    }
    @Test
    public void checkDisplayedElementsInSmileMode(){
        wait = new WebDriverWait(driver, 15);
        Dut.wait(driver, 500);
        String changeMode = "com.intel.camera22:id/mode_button";
        String groupSingle = "com.intel.camera22:id/mode_wave_photo";
        String modeVideo = "com.intel.camera22:id/mode_wave_video";
        String groupBurst = "com.intel.camera22:id/mode_wave_burst";
        String modePerfect = "com.intel.camera22:id/mode_wave_perfectshot";
        String modePanorama = "com.intel.camera22:id/mode_wave_panorama";
        String modeHDR = "com.intel.camera22:id/mode_wave_hdr";
        String modeSmile = "com.intel.camera22:id/mode_wave_smile";
        String modeSBurst = "com.intel.camera22:id/mode_wave_burst_slow";
        String modeFBurst = "com.intel.camera22:id/mode_wave_burst_fast";
    
        System.out.println("@@@...Running checkDisplayedElementsInSmileMode...@@@"); 
        //Change to Smile Cam mode
        Dut.changeMode(driver, "smile");
        //Verify Shutter button availability
        assertTrue(Dut.verifyExist(driver, "name", "Shutter button"));
        //Capture an image in Smile mode, verify thumbnail existence and go to Gallery.  Then delete the image.
        Dut.imageCapture(driver, "smile", 1, 300, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
        Dut.wait(driver, 1000);
        //Tap on the thumbnail
        Dut.tap(driver, "name", "Most recent photo", 1, 100);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Most recent photo")));
		Dut.wait(driver, 1500);
        //Delete the image folder to clean up
		Dut.goToAlbumView(driver, "Full View");
        Dut.deleteFolder(driver, "100ANDRO", 3);
        Dut.wait(driver, 700);
        Dut.sendKeyEvent(driver, 4);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Verify Change Mode icon availability
        assertTrue(Dut.verifyExist(driver, "id", changeMode));
        //Tap of the mode icon to verify other icons
        Dut.tap(driver, "id", changeMode, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(groupSingle)));
        //Verify the group icons (Single grouple, Video group, Burst group, Perfectshot group, and Panorama group
        assertTrue(Dut.verifyExist(driver, "id", groupSingle));
        assertTrue(Dut.verifyExist(driver, "id", modeVideo));
        assertTrue(Dut.verifyExist(driver, "id", groupBurst));
        assertTrue(Dut.verifyExist(driver, "id", modePerfect));
        assertTrue(Dut.verifyExist(driver, "id", modePanorama));
        //Verify shooting mode icons under the Single group by switching to Video mode first and then clicking on the Single group icon
        Dut.tap(driver, "id", modeVideo, 1, 100);
        Dut.tap(driver, "id", groupSingle, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeHDR));
        assertTrue(Dut.verifyExist(driver, "id", modeSmile));
        //Tap on the Burst group and verify the modes under it:
        Dut.tap(driver, "id", groupBurst, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeFBurst));
        assertTrue(Dut.verifyExist(driver, "id", modeSBurst));
        //Tap on the Single shooting group and then tap on Smile Cam
        Dut.tap(driver, "id", groupSingle, 1, 500);
        Dut.tap(driver, "id", modeSmile, 1, 300);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        //Verify icons on the left menu
        assertTrue(Dut.verifyExist(driver, "name", "Camera settings"));
        assertTrue(Dut.verifyExist(driver, "name", "Flash settings"));
        //Wait until the left menu disappear, then try pulling it out again.
        //wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Camera settings")));
        Dut.wait(driver, 5500);
        Dut.tap(driver, "id", "com.intel.camera22:id/second_menu_indicator_bar", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        //Change back to Single mode
        Dut.changeMode(driver, "single");
        System.out.println("@@@...Finished checkDisplayedElementsInSmileMode...@@@");
    }
    @Test
    public void checkDisplayedElementsInHDRMode(){
        wait = new WebDriverWait(driver, 15);
        Dut.wait(driver, 500);
        String changeMode = "com.intel.camera22:id/mode_button";
        String groupSingle = "com.intel.camera22:id/mode_wave_photo";
        String modeVideo = "com.intel.camera22:id/mode_wave_video";
        String groupBurst = "com.intel.camera22:id/mode_wave_burst";
        String modePerfect = "com.intel.camera22:id/mode_wave_perfectshot";
        String modePanorama = "com.intel.camera22:id/mode_wave_panorama";
        String modeHDR = "com.intel.camera22:id/mode_wave_hdr";
        String modeSmile = "com.intel.camera22:id/mode_wave_smile";
        String modeSBurst = "com.intel.camera22:id/mode_wave_burst_slow";
        String modeFBurst = "com.intel.camera22:id/mode_wave_burst_fast";
    
        System.out.println("@@@...Running checkDisplayedElementsInHDRMode...@@@"); 
        //Change to HDR mode
        Dut.changeMode(driver, "hdr");
        //Verify Shutter button availability
        assertTrue(Dut.verifyExist(driver, "name", "Shutter button"));
        //Capture an HDR image, verify thumbnail existence and go to Gallery.  Then delete the image.
        Dut.imageCapture(driver, "hdr", 1, 700);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
        Dut.wait(driver, 1000);
        //Tap on the thumbnail
        Dut.tap(driver, "name", "Most recent photo", 1, 100);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Most recent photo")));
		Dut.wait(driver, 1500);
        //Delete the image folder to clean up
		Dut.goToAlbumView(driver, "Full View");
        Dut.deleteFolder(driver, "100ANDRO", 3);
        Dut.wait(driver, 700);
        Dut.sendKeyEvent(driver, 4);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Verify Change Mode icon availability
        assertTrue(Dut.verifyExist(driver, "id", changeMode));
        //Tap of the mode icon to verify other icons
        Dut.tap(driver, "id", changeMode, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(groupSingle)));
        //Verify the group icons (Single grouple, Video group, Burst group, Perfectshot group, and Panorama group
        assertTrue(Dut.verifyExist(driver, "id", groupSingle));
        assertTrue(Dut.verifyExist(driver, "id", modeVideo));
        assertTrue(Dut.verifyExist(driver, "id", groupBurst));
        assertTrue(Dut.verifyExist(driver, "id", modePerfect));
        assertTrue(Dut.verifyExist(driver, "id", modePanorama));
        //Verify shooting mode icons under the Single group by switching to Video mode first and then clicking on the Single group icon
        Dut.tap(driver, "id", modeVideo, 1, 100);
        Dut.tap(driver, "id", groupSingle, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeHDR));
        assertTrue(Dut.verifyExist(driver, "id", modeSmile));
        //Tap on the Burst group and verify the modes under it:
        Dut.tap(driver, "id", groupBurst, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeFBurst));
        assertTrue(Dut.verifyExist(driver, "id", modeSBurst));
        //Tap on the Single shooting group and then tap HDR
        Dut.tap(driver, "id", groupSingle, 1, 500);
        Dut.tap(driver, "id", modeHDR, 1, 300);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        //Verify icons on the left menu
        assertTrue(Dut.verifyExist(driver, "name", "Camera settings"));
        assertTrue(Dut.verifyExist(driver, "name", "Face recognition"));
        //Wait until the left menu disappear, then try pulling it out again.
        //wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Camera settings")));
        Dut.wait(driver, 5500);
        Dut.tap(driver, "id", "com.intel.camera22:id/second_menu_indicator_bar", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        //Change back to Single mode
        Dut.changeMode(driver, "single");
        System.out.println("@@@...Finished checkDisplayedElementsInHDRMode...@@@");
    }
    @Test
    public void checkDisplayedElementsInVideoMode(){
        wait = new WebDriverWait(driver, 15);
        Dut.wait(driver, 500);
        String changeMode = "com.intel.camera22:id/mode_button";
        String groupSingle = "com.intel.camera22:id/mode_wave_photo";
        String modeVideo = "com.intel.camera22:id/mode_wave_video";
        String groupBurst = "com.intel.camera22:id/mode_wave_burst";
        String modePerfect = "com.intel.camera22:id/mode_wave_perfectshot";
        String modePanorama = "com.intel.camera22:id/mode_wave_panorama";
        String modeHDR = "com.intel.camera22:id/mode_wave_hdr";
        String modeSmile = "com.intel.camera22:id/mode_wave_smile";
        String modeSBurst = "com.intel.camera22:id/mode_wave_burst_slow";
        String modeFBurst = "com.intel.camera22:id/mode_wave_burst_fast";
    
        System.out.println("@@@...Running checkDisplayedElementsInVideoMode...@@@"); 
        //Change to Video mode
        Dut.changeMode(driver, "video");
        //Verify Shutter button availability
        assertTrue(Dut.verifyExist(driver, "name", "Shutter button"));
        //Capture a video file, verify thumbnail existence and go to Gallery.  Then delete the file.
        Dut.imageCapture(driver, "video", 1, 700);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
        Dut.wait(driver, 1000);
        //Tap on the thumbnail
        Dut.tap(driver, "name", "Most recent photo", 1, 100);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Most recent photo")));
		Dut.wait(driver, 1500);
        //Delete the image folder to clean up
		Dut.goToAlbumView(driver, "Full View");
        Dut.deleteFolder(driver, "100ANDRO", 3);
        Dut.wait(driver, 700);
        Dut.sendKeyEvent(driver, 4);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Verify Change Mode icon availability
        assertTrue(Dut.verifyExist(driver, "id", changeMode));
        //Tap of the mode icon to verify other icons
        Dut.tap(driver, "id", changeMode, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(groupSingle)));
        //Verify the group icons (Single grouple, Video group, Burst group, Perfectshot group, and Panorama group
        assertTrue(Dut.verifyExist(driver, "id", groupSingle));
        assertTrue(Dut.verifyExist(driver, "id", modeVideo));
        assertTrue(Dut.verifyExist(driver, "id", groupBurst));
        assertTrue(Dut.verifyExist(driver, "id", modePerfect));
        assertTrue(Dut.verifyExist(driver, "id", modePanorama));
        //Verify shooting mode icons under the Single group by clicking on the Single group icon
        Dut.tap(driver, "id", groupSingle, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeHDR));
        assertTrue(Dut.verifyExist(driver, "id", modeSmile));
        //Tap on the Burst group and verify the modes under it:
        Dut.tap(driver, "id", groupBurst, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeFBurst));
        assertTrue(Dut.verifyExist(driver, "id", modeSBurst));
        //Tap on the Video shooting group and then tap outside to get out 
        Dut.tap(driver, "id", modeVideo, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        assertTrue(Dut.verifyExist(driver, "name", "Camera settings"));
        assertTrue(Dut.verifyExist(driver, "name", "Flash settings"));
        assertTrue(Dut.verifyExist(driver, "name", "Front and back camera switch"));
        //Wait until the left menu disappear, then try pulling it out again.
        //wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Camera settings")));
        Dut.wait(driver, 5500);
        Dut.tap(driver, "id", "com.intel.camera22:id/second_menu_indicator_bar", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        //Switch to front-facing camera to verify displayed icons
        Dut.tap(driver, "name", "Front and back camera switch", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        //Verify icons on the left menu
        assertTrue(Dut.verifyExist(driver, "name", "Camera settings"));
        assertTrue(Dut.verifyExist(driver, "name", "Front and back camera switch"));
        //Verify the shutter button and change mode icon exists
        assertTrue(Dut.verifyExist(driver, "name", "Shutter button"));
        assertTrue(Dut.verifyExist(driver, "id", changeMode));
        //Capture a single shot image on front-facing camera, verify thumbnail existence and go to Gallery.  Then delete the image.
        Dut.imageCapture(driver, "video", 1, 700);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
        Dut.wait(driver, 1000);
        //Tap on the thumbnail
        Dut.tap(driver, "name", "Most recent photo", 1, 100);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Most recent photo")));
		Dut.wait(driver, 1500);
        //Delete the image folder to clean up
		Dut.goToAlbumView(driver, "Full View");
        Dut.deleteFolder(driver, "100ANDRO", 3);
        Dut.wait(driver, 700);
        Dut.sendKeyEvent(driver, 4);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Tap of the mode icon to verify other icons
        Dut.tap(driver, "id", changeMode, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(groupSingle)));
        //Verify the group icons (Single grouple, Video group, Burst group, Perfectshot group, and Panorama group
        assertTrue(Dut.verifyExist(driver, "id", groupSingle));
        assertTrue(Dut.verifyExist(driver, "id", modeVideo));
        //Tap outside and switch to back-facing camera
        Dut.tap(driver, 0.2, 0.2);
        //Wait until the left menu disappear, then try pulling it out again.
        //wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Camera settings")));
        Dut.wait(driver, 5300);
        Dut.tap(driver, "id", "com.intel.camera22:id/second_menu_indicator_bar", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        Dut.tap(driver, "name", "Front and back camera switch", 1, 700);
        Dut.wait(driver, 500);
        //Change back to Single mode
        Dut.changeMode(driver, "single");
        System.out.println("@@@...Finished checkDisplayedElementsInVideoMode...@@@");
    }
    @Test
    public void checkDisplayedElementsInFastBurst(){
        wait = new WebDriverWait(driver, 15);
        Dut.wait(driver, 500);
        String changeMode = "com.intel.camera22:id/mode_button";
        String groupSingle = "com.intel.camera22:id/mode_wave_photo";
        String modeVideo = "com.intel.camera22:id/mode_wave_video";
        String groupBurst = "com.intel.camera22:id/mode_wave_burst";
        String modePerfect = "com.intel.camera22:id/mode_wave_perfectshot";
        String modePanorama = "com.intel.camera22:id/mode_wave_panorama";
        String modeHDR = "com.intel.camera22:id/mode_wave_hdr";
        String modeSmile = "com.intel.camera22:id/mode_wave_smile";
        String modeSBurst = "com.intel.camera22:id/mode_wave_burst_slow";
        String modeFBurst = "com.intel.camera22:id/mode_wave_burst_fast";
    
        System.out.println("@@@...Running checkDisplayedElementsInFastBurst...@@@"); 
        //Change to Fast Burst mode
        Dut.changeMode(driver, "fburst");
        //Verify Shutter button availability
        assertTrue(Dut.verifyExist(driver, "name", "Shutter button"));
        //Capture a fast burst, verify thumbnail existence and go to Gallery.  Then delete the file.
        Dut.imageCapture(driver, "fburst", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
        Dut.wait(driver, 2000);
        //Tap on the thumbnail
        Dut.tap(driver, "name", "Most recent photo", 1, 100);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Most recent photo")));
		Dut.wait(driver, 1500);
        //Delete the image folder to clean up
		Dut.goToAlbumView(driver, "Full View");
        Dut.deleteFolder(driver, "100ANDRO", 3);
        Dut.wait(driver, 700);
        Dut.sendKeyEvent(driver, 4);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Verify Change Mode icon availability
        assertTrue(Dut.verifyExist(driver, "id", changeMode));
        //Tap of the mode icon to verify other icons
        Dut.tap(driver, "id", changeMode, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(groupSingle)));
        //Verify the group icons (Single grouple, Video group, Burst group, Perfectshot group, and Panorama group
        assertTrue(Dut.verifyExist(driver, "id", groupSingle));
        assertTrue(Dut.verifyExist(driver, "id", modeVideo));
        assertTrue(Dut.verifyExist(driver, "id", groupBurst));
        assertTrue(Dut.verifyExist(driver, "id", modePerfect));
        assertTrue(Dut.verifyExist(driver, "id", modePanorama));
        //Verify shooting mode icons under the Single group by clicking on the Single group icon
        Dut.tap(driver, "id", groupSingle, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeHDR));
        assertTrue(Dut.verifyExist(driver, "id", modeSmile));
        //Tap on the Burst group and verify the modes under it:
        Dut.tap(driver, "id", groupBurst, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeFBurst));
        assertTrue(Dut.verifyExist(driver, "id", modeSBurst));
        //Tap on the fast burst mode
        Dut.tap(driver, "id", modeFBurst, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        assertTrue(Dut.verifyExist(driver, "name", "Camera settings"));
        //Wait until the left menu disappear, then try pulling it out again.
        Dut.wait(driver, 5500);
        Dut.tap(driver, "id", "com.intel.camera22:id/second_menu_indicator_bar", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        //Change back to Single mode
        Dut.changeMode(driver, "single");
        System.out.println("@@@...Finished checkDisplayedElementsInFastBurst...@@@");
    }
    @Test
    public void checkDisplayedElementsInSlowBurst(){
        wait = new WebDriverWait(driver, 15);
        Dut.wait(driver, 500);
        String changeMode = "com.intel.camera22:id/mode_button";
        String groupSingle = "com.intel.camera22:id/mode_wave_photo";
        String modeVideo = "com.intel.camera22:id/mode_wave_video";
        String groupBurst = "com.intel.camera22:id/mode_wave_burst";
        String modePerfect = "com.intel.camera22:id/mode_wave_perfectshot";
        String modePanorama = "com.intel.camera22:id/mode_wave_panorama";
        String modeHDR = "com.intel.camera22:id/mode_wave_hdr";
        String modeSmile = "com.intel.camera22:id/mode_wave_smile";
        String modeSBurst = "com.intel.camera22:id/mode_wave_burst_slow";
        String modeFBurst = "com.intel.camera22:id/mode_wave_burst_fast";
    
        System.out.println("@@@...Running checkDisplayedElementsInSlowBurst...@@@"); 
        //Change to Slow Burst mode
        Dut.changeMode(driver, "sburst");
        //Verify Shutter button availability
        assertTrue(Dut.verifyExist(driver, "name", "Shutter button"));
        //Capture a slow burst, verify thumbnail existence and go to Gallery.  Then delete the file.
        Dut.imageCapture(driver, "sburst", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
        Dut.wait(driver, 500);
        //Tap on the thumbnail
        Dut.tap(driver, "name", "Most recent photo", 1, 100);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Most recent photo")));
		Dut.wait(driver, 1500);
        //Delete the image folder to clean up
		Dut.goToAlbumView(driver, "Full View");
        Dut.deleteFolder(driver, "100ANDRO", 3);
        Dut.wait(driver, 700);
        Dut.sendKeyEvent(driver, 4);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Verify Change Mode icon availability
        assertTrue(Dut.verifyExist(driver, "id", changeMode));
        //Tap of the mode icon to verify other icons
        Dut.tap(driver, "id", changeMode, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(groupSingle)));
        //Verify the group icons (Single grouple, Video group, Burst group, Perfectshot group, and Panorama group
        assertTrue(Dut.verifyExist(driver, "id", groupSingle));
        assertTrue(Dut.verifyExist(driver, "id", modeVideo));
        assertTrue(Dut.verifyExist(driver, "id", groupBurst));
        assertTrue(Dut.verifyExist(driver, "id", modePerfect));
        assertTrue(Dut.verifyExist(driver, "id", modePanorama));
        //Verify shooting mode icons under the Single group by clicking on the Single group icon
        Dut.tap(driver, "id", groupSingle, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeHDR));
        assertTrue(Dut.verifyExist(driver, "id", modeSmile));
        //Tap on the Burst group and verify the modes under it:
        Dut.tap(driver, "id", groupBurst, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeFBurst));
        assertTrue(Dut.verifyExist(driver, "id", modeSBurst));
        //Tap on the Slow Burst mode
        Dut.tap(driver, "id", modeSBurst, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        assertTrue(Dut.verifyExist(driver, "name", "Camera settings"));
        //Wait until the left menu disappear, then try pulling it out again.
        Dut.wait(driver, 5500);
        Dut.tap(driver, "id", "com.intel.camera22:id/second_menu_indicator_bar", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        //Change back to Single mode
        Dut.changeMode(driver, "single");
        System.out.println("@@@...Finished checkDisplayedElementsInSlowBurst...@@@");
    }
    @Test
    public void checkDisplayedElementsInPerfectshot(){
        wait = new WebDriverWait(driver, 15);
        Dut.wait(driver, 500);
        String changeMode = "com.intel.camera22:id/mode_button";
        String groupSingle = "com.intel.camera22:id/mode_wave_photo";
        String modeVideo = "com.intel.camera22:id/mode_wave_video";
        String groupBurst = "com.intel.camera22:id/mode_wave_burst";
        String modePerfect = "com.intel.camera22:id/mode_wave_perfectshot";
        String modePanorama = "com.intel.camera22:id/mode_wave_panorama";
        String modeHDR = "com.intel.camera22:id/mode_wave_hdr";
        String modeSmile = "com.intel.camera22:id/mode_wave_smile";
        String modeSBurst = "com.intel.camera22:id/mode_wave_burst_slow";
        String modeFBurst = "com.intel.camera22:id/mode_wave_burst_fast";
    
        System.out.println("@@@...Running checkDisplayedElementsInPerfectshot...@@@"); 
        //Change to Perfect Shot mode
        Dut.changeMode(driver, "perfect");
        //Verify Shutter button availability
        assertTrue(Dut.verifyExist(driver, "name", "Shutter button"));
        //Capture a Perfect Shot image, verify thumbnail existence and go to Gallery.  Then delete the file.
        Dut.imageCapture(driver, "perfect", 1, 4000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
        Dut.wait(driver, 500);
        //Tap on the thumbnail
        Dut.tap(driver, "name", "Most recent photo", 1, 100);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Most recent photo")));
		Dut.wait(driver, 1500);
        //Delete the image folder to clean up
		Dut.goToAlbumView(driver, "Full View");
        Dut.deleteFolder(driver, "100ANDRO", 3);
        Dut.wait(driver, 700);
        Dut.sendKeyEvent(driver, 4);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Verify Change Mode icon availability
        assertTrue(Dut.verifyExist(driver, "id", changeMode));
        //Tap of the mode icon to verify other icons
        Dut.tap(driver, "id", changeMode, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(groupSingle)));
        //Verify the group icons (Single grouple, Video group, Burst group, Perfectshot group, and Panorama group
        assertTrue(Dut.verifyExist(driver, "id", groupSingle));
        assertTrue(Dut.verifyExist(driver, "id", modeVideo));
        assertTrue(Dut.verifyExist(driver, "id", groupBurst));
        assertTrue(Dut.verifyExist(driver, "id", modePerfect));
        assertTrue(Dut.verifyExist(driver, "id", modePanorama));
        //Verify shooting mode icons under the Single group by clicking on the Single group icon
        Dut.tap(driver, "id", groupSingle, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeHDR));
        assertTrue(Dut.verifyExist(driver, "id", modeSmile));
        //Tap on the Burst group and verify the modes under it:
        Dut.tap(driver, "id", groupBurst, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeFBurst));
        assertTrue(Dut.verifyExist(driver, "id", modeSBurst));
        //Tap on the Perfectshot mode
        Dut.tap(driver, "id", modePerfect, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        assertTrue(Dut.verifyExist(driver, "name", "Camera settings"));
        //Wait until the left menu disappear, then try pulling it out again.
        Dut.wait(driver, 5500);
        Dut.tap(driver, "id", "com.intel.camera22:id/second_menu_indicator_bar", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        //Change back to Single mode
        Dut.changeMode(driver, "single");
        System.out.println("@@@...Finished checkDisplayedElementsInPerfectshot...@@@");
    }
    @Test
    public void checkDisplayedElementsInPanorama(){
        wait = new WebDriverWait(driver, 15);
        Dut.wait(driver, 500);
        String changeMode = "com.intel.camera22:id/mode_button";
        String groupSingle = "com.intel.camera22:id/mode_wave_photo";
        String modeVideo = "com.intel.camera22:id/mode_wave_video";
        String groupBurst = "com.intel.camera22:id/mode_wave_burst";
        String modePerfect = "com.intel.camera22:id/mode_wave_perfectshot";
        String modePanorama = "com.intel.camera22:id/mode_wave_panorama";
        String modeHDR = "com.intel.camera22:id/mode_wave_hdr";
        String modeSmile = "com.intel.camera22:id/mode_wave_smile";
        String modeSBurst = "com.intel.camera22:id/mode_wave_burst_slow";
        String modeFBurst = "com.intel.camera22:id/mode_wave_burst_fast";
    
        System.out.println("@@@...Running checkDisplayedElementsInPanorama...@@@"); 
        //Change to Panorama mode
        Dut.changeMode(driver, "panorama");
        //Verify Shutter button availability
        assertTrue(Dut.verifyExist(driver, "name", "Shutter button"));
        //Capture a Panorama image, verify thumbnail existence and go to Gallery.  Then delete the file.
        Dut.imageCapture(driver, "panorama", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
        Dut.wait(driver, 500);
        //Tap on the thumbnail
        Dut.tap(driver, "name", "Most recent photo", 1, 100);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Most recent photo")));
		Dut.wait(driver, 1500);
        //Delete the image folder to clean up
		Dut.goToAlbumView(driver, "Full View");
        Dut.deleteFolder(driver, "100ANDRO", 3);
        Dut.wait(driver, 700);
        Dut.sendKeyEvent(driver, 4);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Verify Change Mode icon availability
        assertTrue(Dut.verifyExist(driver, "id", changeMode));
        //Tap of the mode icon to verify other icons
        Dut.tap(driver, "id", changeMode, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(groupSingle)));
        //Verify the group icons (Single grouple, Video group, Burst group, Perfectshot group, and Panorama group
        assertTrue(Dut.verifyExist(driver, "id", groupSingle));
        assertTrue(Dut.verifyExist(driver, "id", modeVideo));
        assertTrue(Dut.verifyExist(driver, "id", groupBurst));
        assertTrue(Dut.verifyExist(driver, "id", modePerfect));
        assertTrue(Dut.verifyExist(driver, "id", modePanorama));
        //Verify shooting mode icons under the Single group by clicking on the Single group icon
        Dut.tap(driver, "id", groupSingle, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeHDR));
        assertTrue(Dut.verifyExist(driver, "id", modeSmile));
        //Tap on the Burst group and verify the modes under it:
        Dut.tap(driver, "id", groupBurst, 1, 500);
        assertTrue(Dut.verifyExist(driver, "id", modeFBurst));
        assertTrue(Dut.verifyExist(driver, "id", modeSBurst));
        //Tap on the Panorama mode
        Dut.tap(driver, "id", modePanorama, 1, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        assertTrue(Dut.verifyExist(driver, "name", "Camera settings"));
        //Wait until the left menu disappear, then try pulling it out again.
        Dut.wait(driver, 5500);
        Dut.tap(driver, "id", "com.intel.camera22:id/second_menu_indicator_bar", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        //Change back to Single mode
        Dut.changeMode(driver, "single");
        System.out.println("@@@...Finished checkDisplayedElementsInPanorama...@@@");
    }
    @Test
    public void testCameraModeChanging(){
        wait = new WebDriverWait(driver, 15);
        Dut.wait(driver, 500);
        String changeMode = "com.intel.camera22:id/mode_button";
        String groupSingle = "com.intel.camera22:id/mode_wave_photo";
        String modeVideo = "com.intel.camera22:id/mode_wave_video";
        String groupBurst = "com.intel.camera22:id/mode_wave_burst";
        String modePerfect = "com.intel.camera22:id/mode_wave_perfectshot";
        String modePanorama = "com.intel.camera22:id/mode_wave_panorama";
        String modeHDR = "com.intel.camera22:id/mode_wave_hdr";
        String modeSmile = "com.intel.camera22:id/mode_wave_smile";
        String modeSBurst = "com.intel.camera22:id/mode_wave_burst_slow";
        String modeFBurst = "com.intel.camera22:id/mode_wave_burst_fast";
    
        System.out.println("@@@...Running testCameraModeChanging...@@@"); 
        //Change to HDR
        Dut.changeMode(driver, "hdr");
        //Change back to Single Shot
        Dut.changeMode(driver, "single");
        //Change to Smile Cam
        Dut.changeMode(driver, "smile");
        //Change back to Single Shot
        Dut.changeMode(driver, "single");
        //Change to Video mode
        Dut.changeMode(driver, "video");
        //Change back to Single Shot
        Dut.changeMode(driver, "single");
        //Change to Slow Burst
        Dut.changeMode(driver, "sburst");
        //Change back to Single Shot
        Dut.changeMode(driver, "single");
        //Change to Fast Burst mode
        Dut.changeMode(driver, "fburst");
        //Change back to Single Shot
        Dut.changeMode(driver, "single");
        //Change to Perfectshot mode
        Dut.changeMode(driver, "perfect");
        //Change back to Single Shot
        Dut.changeMode(driver, "single");
        //Change to Panorama mode
        Dut.changeMode(driver, "panorama");
        //Change back to Single Shot
        Dut.changeMode(driver, "single");
        //Switch to Single Shot, front camera
        Dut.tap(driver, "name", "Front and back camera switch", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Change to Video mode on front camera
        Dut.changeMode(driver, "video");
        //Change back to Single Shot front camera
        Dut.changeMode(driver, "single");
        //Switch to Single Shot, back camera
        Dut.tap(driver, "name", "Front and back camera switch", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Change to HDR so that we can test switching to other modes from HDR
        Dut.changeMode(driver, "hdr");
        //Change to Smile Cam
        Dut.changeMode(driver, "smile");
        //Change back to HDR
        Dut.changeMode(driver, "hdr");
        //Change to Video mode
        Dut.changeMode(driver, "video");
        //Change back to HDR
        Dut.changeMode(driver, "hdr");
        //Change to Slow Burst
        Dut.changeMode(driver, "sburst");
        //Change back to HDR
        Dut.changeMode(driver, "hdr");
        //Change to Fast Burst mode
        Dut.changeMode(driver, "fburst");
        //Change back to HDR
        Dut.changeMode(driver, "hdr");
        //Change to Perfectshot mode
        Dut.changeMode(driver, "perfect");
        //Change back to HDR
        Dut.changeMode(driver, "hdr");
        //Change to Panorama mode
        Dut.changeMode(driver, "panorama");
        //Change back to HDR
        Dut.changeMode(driver, "hdr");
        //Change to Smile Cam mode so that we can test switching to other modes from Smile Cam
        Dut.changeMode(driver, "smile");
        //Change to Video mode
        Dut.changeMode(driver, "video");
        //Change back to Smile Cam
        Dut.changeMode(driver, "smile");
        //Change to Slow Burst
        Dut.changeMode(driver, "sburst");
        //Change back to Smile Cam
        Dut.changeMode(driver, "smile");
        //Change to Fast Burst mode
        Dut.changeMode(driver, "fburst");
        //Change back to Smile Cam
        Dut.changeMode(driver, "smile");
        //Change to Perfectshot mode
        Dut.changeMode(driver, "perfect");
        //Change back to Smile Cam
        Dut.changeMode(driver, "smile");
        //Change to Panorama mode
        Dut.changeMode(driver, "panorama");
        //Change back to Smile Cam
        Dut.changeMode(driver, "smile");
        //Change to Video mode so that we can test switching to other modes from Video
        Dut.changeMode(driver, "video");
        //Change to Slow Burst
        Dut.changeMode(driver, "sburst");
        //Change back to Video mode
        Dut.changeMode(driver, "video");
        //Change to Fast Burst mode
        Dut.changeMode(driver, "fburst");
        //Change back to Video mode
        Dut.changeMode(driver, "video");
        //Change to Perfectshot mode
        Dut.changeMode(driver, "perfect");
        //Change back to Video mode
        Dut.changeMode(driver, "video");
        //Change to Panorama mode
        Dut.changeMode(driver, "panorama");
        //Change back to Video mode
        Dut.changeMode(driver, "video");
        //Switch to Video, front camera
        Dut.tap(driver, "name", "Front and back camera switch", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Switch back to Video, back camera
        Dut.tap(driver, "name", "Front and back camera switch", 1, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Change to Slow Burst mode so that we can test switching to other modes from Slow Burst
        Dut.changeMode(driver, "sburst");
        //Change to Fast Burst mode
        Dut.changeMode(driver, "fburst");
        //Change back to Slow Burst mode
        Dut.changeMode(driver, "sburst");
        //Change to Perfectshot mode
        Dut.changeMode(driver, "perfect");
        //Change back to Slow Burst mode
        Dut.changeMode(driver, "sburst");
        //Change to Panorama mode
        Dut.changeMode(driver, "panorama");
        //Change back to Slow Burst mode
        Dut.changeMode(driver, "sburst");
        //Change to Fast Burst mode so that we can test switching to other modes from Fast Burst
        Dut.changeMode(driver, "fburst");
        //Change to Perfectshot mode
        Dut.changeMode(driver, "perfect");
        //Change back to Fast Burst mode
        Dut.changeMode(driver, "fburst");
        //Change to Panorama mode
        Dut.changeMode(driver, "panorama");
        //Change back to Fast Burst mode
        Dut.changeMode(driver, "fburst");
        //Change to Perfectshot mode so that we can test switching to other modes from Perfectshot
        Dut.changeMode(driver, "perfect");
        //Change to Panorama mode
        Dut.changeMode(driver, "panorama");
        //Change back to Perfectshot mode
        Dut.changeMode(driver, "perfect");
        //Change back to Single mode
        Dut.changeMode(driver, "single");
        System.out.println("@@@...Finished testCameraModeChanging...@@@");
    }
    //@Test
/*     public void testNewMethods(){
        wait = new WebDriverWait(driver, 10);
        //menu = driver.findElement(By.name("More options"));
        
        System.out.println("@@@...Running testNewMethods...@@@");
        Dut.wait(driver, 2000);
        WebElement element = driver.findElement(By.name("Shutter button"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Integer> tapObject = new HashMap<String, Integer>();
        tapObject.put("element", (Integer)((RemoteWebElement) element).getId());
        tapObject.put("duration", 4);
        js.executeScript("mobile: longClick", tapObject);

        // HashMap<String, Integer> tapObject = new HashMap<String, Integer>();
        // tapObject.put("element", element);
        // JavascriptExecutor js = (JavascriptExecutor) driver;
        // HashMap<String, Integer> tapObject = new HashMap<String, Integer>();
        // tapObject.put("x", 360); // in pixels from left
        // tapObject.put("y", 1190); // in pixels from top
        // tapObject.put("duration", 4); 
        // js.executeScript("mobile: complexTap", tapObject);
        
        // js.executeScript("mobile: complexTap", ["touchCount": 1, "x": 360, "y": 1190, "duration": 4, "element": element]);
        //Dut.longTap(driver, 360, 1190);
        // System.out.printf("Element id:  %s\n", ((RemoteWebElement) element).getId());
        System.out.println("@@@...Finished testNewMethods...@@@");
    } */
}
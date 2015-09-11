package androidTests;

import org.openqa.selenium.*;
import org.openqa.selenium.By;
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

import static org.junit.Assert.*;

public class Dut {

    //Method to find Camera's direction.  It returns 1 for back-facing and 2 for front facing camera.
    public static int getCamDirection(WebDriver driver){
        String modeButton = "com.intel.camera22:id/mode_button";
        String modePano = "com.intel.camera22:id/mode_wave_panorama";
        int camDirection = 1; 
        WebDriverWait wait = new WebDriverWait(driver, 15);
        
        //Wait for and click on the mode button
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
        driver.findElement(By.id(modeButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
        wait(driver, 500);
        //If we find the Pano group icon, then return 1 for back-facing camera
        if (!driver.findElements(By.id(modePano)).isEmpty()) {
            //Tap outside to exit out of the menu
            tap(driver, 0.2, 0.3);
        }
        else {
            //Tap outside to exit out of the menu
            tap(driver, 0.2, 0.3);
            camDirection = 2;
        }
        return camDirection;
    }
    //Method to check for the Camera settings icon and click on it.
    public static void tapSettingsIcon(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String singleGroup = "com.intel.camera22:id/mode_wave_photo";
        String modeButton = "com.intel.camera22:id/mode_button";
        
        //If we can find Camera settings, we will click on it.  Otherwise, we try to open the left menu and check again.
        if (!driver.findElements(By.name("Camera settings")).isEmpty()) {
            driver.findElement(By.name("Camera settings")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
            wait(driver, 500);
        }
        else {
            driver.findElement(By.id(modeButton)).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(singleGroup)));
            wait(driver, 500);
            tap(driver, 0.2, 0.3);
            if (!driver.findElements(By.name("Camera settings")).isEmpty()) {
				driver.findElement(By.name("Camera settings")).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
				wait(driver, 500);
            }
            else {
                System.out.println("The current shooting mode does not have camera setting icon!");
                assertTrue(false);
            }
        }
    }
    //Method to change Camera's shooting mode
    public static void changeMode(WebDriver driver, String camMode){
        String modeButton = "com.intel.camera22:id/mode_button";
        String modePano = "com.intel.camera22:id/mode_wave_panorama";
        String modeSingle = "com.intel.camera22:id/mode_wave_photo";
        String modeVideo = "com.intel.camera22:id/mode_wave_video";
        String modeBurst = "com.intel.camera22:id/mode_wave_burst";
        String modePerfectshot = "com.intel.camera22:id/mode_wave_perfectshot";
        String hdr = "com.intel.camera22:id/mode_wave_hdr";
        String smile = "com.intel.camera22:id/mode_wave_smile";
        String sburst = "com.intel.camera22:id/mode_wave_burst_slow";
        String fburst = "com.intel.camera22:id/mode_wave_burst_fast";
        
        int camDirection; 
        WebDriverWait wait = new WebDriverWait(driver, 15);
        
        camDirection = getCamDirection(driver);
        //If camera is back-facing
        if (camDirection == 1) {
            if (camMode.equals("single")){
                //Wait for and click on the mode button
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
                driver.findElement(By.id(modeButton)).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
                wait(driver, 700);
                //If camera is currently in Single Shot mode
                if (!driver.findElements(By.name("HDR\nOFF")).isEmpty() && !driver.findElements(By.name("Smile\nOFF")).isEmpty()) {
                    driver.findElement(By.id(modeSingle)).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
                //If camera is not in Single Shot mode
                else {
                    driver.findElement(By.id(modeSingle)).click();
                    wait(driver, 500);
                    tap(driver, 0.2, 0.2);
                    //driver.findElement(By.id(modeSingle)).click();
                    if (!driver.findElements(By.name(Data.REMEMBER_LOCATION)).isEmpty()) {
                        tap(driver, "name", "Cancel", 1, 500);
                        wait(driver, 300);
                    }
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
            }
            else if (camMode.equals("smile")){
                //Wait for and click on the mode button
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
                driver.findElement(By.id(modeButton)).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
                wait(driver, 500);
                //If camera is in Single Shot mode or in HDR mode.  Notice one element is generic while the other has a specific name.
                //if (!driver.findElements(By.name("SmileOFF")).isEmpty() && !driver.findElements(By.id(hdr)).isEmpty()) {
                if (!driver.findElements(By.name("Smile\nOFF")).isEmpty()) {
                    driver.findElement(By.name("Smile\nOFF")).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 200);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
                //If camera is already in Smile Cam mode.  Notice one element is generic while the other has a specific name.
                else if (!driver.findElements(By.name("Smile\nON")).isEmpty()) {
					tap(driver, 0.2, 0.3);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
                //If the camera is not in the above three modes:
                else {
                    driver.findElement(By.id(modeSingle)).click();
                    wait(driver, 500);
                    driver.findElement(By.id(smile)).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
            }
            else if (camMode.equals("hdr")){
                //Wait for and click on the mode button
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
                driver.findElement(By.id(modeButton)).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
                wait(driver, 500);
                //If camera is in Single Shot mode or in Smile Cam mode.  
                if (!driver.findElements(By.name("HDR\nOFF")).isEmpty()) {
                    driver.findElement(By.name("HDR\nOFF")).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
                //If camera is already in HDR mode.  Notice one element is generic while the other has a specific name.
                else if (!driver.findElements(By.name("HDR\nON")).isEmpty()) {
                    tap(driver, 0.2, 0.3); 
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
                //If the camera is not in the above three modes:
                else {
                    driver.findElement(By.id(modeSingle)).click();
                    wait(driver, 500);
                    driver.findElement(By.id(hdr)).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
            }
            else if (camMode.equals("video")){
                //Wait for and click on the mode button
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
                driver.findElement(By.id(modeButton)).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
                wait(driver, 500);
                driver.findElement(By.id(modeVideo)).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                wait(driver, 500);
                System.out.printf("Switched to '%s' mode.\n", camMode);
            }
            else if (camMode.equals("sburst")){
                //Wait for and click on the mode button
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
                driver.findElement(By.id(modeButton)).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
                wait(driver, 500);
                //If camera is in Burst mode already.
                if (!driver.findElements(By.id(fburst)).isEmpty()) {
                    driver.findElement(By.id(sburst)).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
                //If camera is in non-burst modes.
                else {
                    driver.findElement(By.id(modeBurst)).click();
                    wait(driver, 500);
                    driver.findElement(By.id(sburst)).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
            }
            else if (camMode.equals("fburst")){
                //Wait for and click on the mode button
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
                driver.findElement(By.id(modeButton)).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
                wait(driver, 500);
                //If camera is in Burst mode already.
                if (!driver.findElements(By.id(fburst)).isEmpty()) {
                    driver.findElement(By.id(fburst)).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
                //If camera is in non-burst modes.
                else {
                    driver.findElement(By.id(modeBurst)).click();
                    wait(driver, 500);
                    driver.findElement(By.id(fburst)).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
            }
            else if (camMode.equals("perfect")){
                //Wait for and click on the mode button
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
                driver.findElement(By.id(modeButton)).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
                wait(driver, 500);
                driver.findElement(By.id(modePerfectshot)).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                wait(driver, 500);
                System.out.printf("Switched to '%s' mode.\n", camMode);
            }
            else if (camMode.equals("panorama")){
                //Wait for and click on the mode button
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
                driver.findElement(By.id(modeButton)).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
                wait(driver, 500);
                driver.findElement(By.id(modePano)).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                wait(driver, 500);
                System.out.printf("Switched to '%s' mode.\n", camMode);
            }
            else {
                System.out.printf("The mode specified '%s' is not available in back-facing camera to switch to.\n", camMode);
                assertTrue(false);
            }
        }
        //Else camera is front-facing
        else {
            if (camMode.equals("single")){
                //Wait for and click on the mode button
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
                driver.findElement(By.id(modeButton)).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
                wait(driver, 500);
                //If camera is currently in Single Shot mode
                if (!driver.findElements(By.name("Smile\nOFF")).isEmpty()) {
                    driver.findElement(By.id(modeSingle)).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
                //If camera is currently in Smile Cam mode
                else if (!driver.findElements(By.name("Smile\nON")).isEmpty()) {
                    driver.findElement(By.id(modeSingle)).click();
                    wait(driver, 500);
                    driver.findElement(By.id(modeSingle)).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
                //If camera is currently not in those modes above
                else {
                    driver.findElement(By.id(modeSingle)).click();
                    wait(driver, 500);
                    tap(driver, 0.2, 0.2);
                    if (!driver.findElements(By.name(Data.REMEMBER_LOCATION)).isEmpty()) {
                        tap(driver, "name", "Cancel", 1, 500);
                    }
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                    System.out.printf("Switched to '%s' mode.\n", camMode);
                }
            }
            else if (camMode.equals("smile")){
                System.out.printf("Cannot switch to '%s' mode because it is not supported in front-facing camera.\n", camMode);
                assertTrue(false);
                /* //Wait for and click on the mode button
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
                driver.findElement(By.id(modeButton)).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
                wait(driver, 500);
                //If camera is currently in Single Shot mode
                if (!driver.findElements(By.name("SmileOFF")).isEmpty()) {
                    driver.findElement(By.name("SmileOFF")).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                }
                //If camera is currently in Smile Cam mode
                else if (!driver.findElements(By.name("SmileON")).isEmpty()) {
                    tap(driver, 0.2, 0.3);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                }
                //If camera is currently not in those modes above
                else {
                    driver.findElement(By.id(modeSingle)).click();
                    wait(driver, 500);
                    driver.findElement(By.name("SmileOFF")).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                    wait(driver, 500);
                } */
            }
            else if (camMode.equals("video")){
                //Wait for and click on the mode button
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(modeButton)));
                driver.findElement(By.id(modeButton)).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(modeButton)));
                wait(driver, 500);
                driver.findElement(By.id(modeVideo)).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
                wait(driver, 500);
                System.out.printf("Switched to '%s' mode.\n", camMode);
            }
            else {
                System.out.printf("The mode specified '%s' is not available in front-facing camera to switch to.\n", camMode);
                assertTrue(false);
            }
        }
    }
    //Method to select camera settings on 2ndary and submenu based on the indices of the choices.  
    public static void selectCamSetting(WebDriver driver, int secondItemIndex, int thirdItemIndex) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String thirdMenu = "com.intel.camera22:id/second_hori_list_view_container";
        String menuChoice = "com.intel.camera22:id/hori_list_button";
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
        wait(driver, 500);
        driver.findElements(By.id(menuChoice)).get(secondItemIndex).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
        wait(driver, 500);
        driver.findElements(By.id(menuChoice)).get(thirdItemIndex).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
        wait(driver, 500);
    }
    //Method to open the Settings menu
    public static void openSettingsMenu(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String menuHandle = "com.intel.camera22:id/second_menu_indicator_bar";
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(menuHandle)));
        driver.findElement(By.id(menuHandle)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Camera settings")));
        wait(driver, 500);
    }
    //Method to set the picture size
    public static void setPictureSize(WebDriver driver, String camMode, String pictureSize) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        int camDirection = getCamDirection(driver);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String thirdMenu = "com.intel.camera22:id/second_hori_list_view_container";
        String menuChoice = "com.intel.camera22:id/hori_list_button";
        
        tapSettingsIcon(driver);
        //If camera is back-facing
        if (camDirection == 1) {
            if (camMode.equals("single")) {
                //For wide format
                if (pictureSize.equals("wide")) {
                    selectCamSetting(driver, 3, 7);
                }
                //For standard format
                else {
                    selectCamSetting(driver, 3, 8);
                }
            }
            //Using the java.util.ArrayList.  Check if the current mode is in the array
            else if (Arrays.asList("hdr", "sburst", "fburst", "smile").contains(camMode)) {
                //For wide format on Smile Cam
                if (pictureSize.equals("wide") && camMode.equals("smile"))  {
                    selectCamSetting(driver, 1, 7);
                }
                //For standard format on Smile Cam
                else if (pictureSize.equals("standard") && camMode.equals("smile")){
                    selectCamSetting(driver, 1, 8);
                }
                //For wide format on HDR
                else if (pictureSize.equals("wide") && camMode.equals("hdr")){
                    selectCamSetting(driver, 1, 3);
                }
                //For standard format on HDR
                else if (pictureSize.equals("standard") && camMode.equals("hdr")){
                    selectCamSetting(driver, 1, 4);
                }
                //For wide format on Burst
                else if (pictureSize.equals("wide") && Arrays.asList("sburst", "fburst").contains(camMode)){
                    selectCamSetting(driver, 1, 4);
                }
                //For standard format on Burst
                else if (pictureSize.equals("standard") && Arrays.asList("sburst", "fburst").contains(camMode)){
                    selectCamSetting(driver, 1, 5);
                }
                else {
                    System.out.printf("Your specified camera mode does not support the picture size '%s'.\n", pictureSize);
                    assertTrue(false);
                }
            }
            else {
                System.out.printf("Cannot change the picture size with your current camera mode '%s'.\n", camMode);
                assertTrue(false);
            }
        }
        //If camera is front-facing
        else {
            System.out.println("Cannot change the picture size in front-facing camera!");
            assertTrue(false);
        }
    }
    //Method to set the video size
    public static void setVideoSize(WebDriver driver, String setting) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        int camDirection = getCamDirection(driver);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String thirdMenu = "com.intel.camera22:id/second_hori_list_view_container";
        String menuChoice = "com.intel.camera22:id/hori_list_button";
        
        tapSettingsIcon(driver);
        //If camera is back-facing
        if (camDirection == 1) {
            //For FullHD
            if (setting.equals("fullhd")) {
                selectCamSetting(driver, 2, 7);
            }
            //For HS size
            else if (setting.equals("hs")) {
                selectCamSetting(driver, 2, 8);
            }
            //For HD size
            else if (setting.equals("hd")) {
                selectCamSetting(driver, 2, 6);
            }
            //For SD size
            else if (setting.equals("sd")) {
                selectCamSetting(driver, 2, 5);
            }
            else {
                System.out.println("Your specified video size is not valid!");
                assertTrue(false);
            }
        }
        //If camera is front-facing
        else {
            System.out.println("Cannot change the video size in front-facing camera!");
            assertTrue(false);
        }
    }
    //Method to set Hints, which is only available in Single Shot back-camera mode:
    public static void setHints(WebDriver driver, String setting) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        int camDirection = getCamDirection(driver);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String thirdMenu = "com.intel.camera22:id/second_hori_list_view_container";
        String menuChoice = "com.intel.camera22:id/hori_list_button";
        
        tapSettingsIcon(driver);
        //If camera is back-facing
        if (camDirection == 1) {
            //For Hints OFF
            if (setting.equals("off")) {
                selectCamSetting(driver, 1, 7);
            }
            //For Hints ON
            else if (setting.equals("on")) {
                selectCamSetting(driver, 1, 8);
            }
            else {
                System.out.println("Your specified Hints setting is not valid!");
                assertTrue(false);
            }
        }
        //If camera is front-facing
        else {
            System.out.println("Cannot set Hints in front-facing camera!");
            assertTrue(false);
        }
    }
    //Method to set the Geo-Tag setting
    public static void setGeoTag(WebDriver driver, String camMode, String setting) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        int camDirection = getCamDirection(driver);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String thirdMenu = "com.intel.camera22:id/second_hori_list_view_container";
        String menuChoice = "com.intel.camera22:id/hori_list_button";
        
        driver.findElement(By.name("Camera settings")).click();
        //If camera is back-facing
        if (camDirection == 1) {
            //Using the java.util.ArrayList.  Check if the current mode is in the array
            if (Arrays.asList("single", "smile").contains(camMode)) {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                if (camMode.equals("single")) {
                    driver.findElements(By.id(menuChoice)).get(2).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                }
                else {
                    driver.findElements(By.id(menuChoice)).get(0).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                }
                //To turn GPS OFF
                if (setting.equals("off")) {
                    driver.findElements(By.id(menuChoice)).get(7).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                }
                //To turn GPS ON
                else if (setting.equals("on")) {
                    driver.findElements(By.id(menuChoice)).get(8).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                }
                else {
                    System.out.println("Your specified Geo-Tag setting is not valid!");
                    assertTrue(false);
                }
            }
            //Using the java.util.ArrayList.  Check if the current mode is in the array
            else if (Arrays.asList("hdr", "perfect", "panorama").contains(camMode)) {
                //To turn GPS OFF
                if (setting.equals("off")) {
                    selectCamSetting(driver, 0, 3);
                }
                //To turn GPS ON
                else if (setting.equals("on")) {
                    selectCamSetting(driver, 0, 4);
                }
                else {
                    System.out.println("Your specified Geo-Tag setting is not valid!");
                    assertTrue(false);
                }
            }
            //Using the java.util.ArrayList.  Check if the current mode is in the array
            else if (Arrays.asList("sburst", "fburst").contains(camMode)) {
                //To turn GPS OFF
                if (setting.equals("off")) {
                    selectCamSetting(driver, 0, 4);
                }
                //To turn GPS ON
                else if (setting.equals("on")) {
                    selectCamSetting(driver, 0, 5);
                }
                else {
                    System.out.println("Your specified Geo-Tag setting is not valid!");
                    assertTrue(false);
                }
            }
            //Else if the current mode is 'video'
            else if (camMode.equals("video")) {
                //To turn GPS OFF
                if (setting.equals("off")) {
                    selectCamSetting(driver, 1, 5);
                }
                //To turn GPS ON
                else if (setting.equals("on")) {
                    selectCamSetting(driver, 1, 6);
                }
                else {
                    System.out.println("Your specified Geo-Tag setting is not valid!");
                    assertTrue(false);
                }
            }
            else {
                System.out.printf("Cannot change the GPS setting with your current camera mode '%s'.\n", camMode);
                assertTrue(false);
            }
        }
        //If camera is front-facing
        else {
            //Using the java.util.ArrayList.  Check if the current mode is in the array
            if (Arrays.asList("video", "single", "smile").contains(camMode)) {
                //To turn GPS OFF
                if (setting.equals("off")) {
                    selectCamSetting(driver, 0, 1);
                }
                //To turn GPS ON
                else if (setting.equals("on")) {
                    selectCamSetting(driver, 0, 2);
                }
                else {
                    System.out.println("Your specified Geo-Tag setting is not valid!");
                    assertTrue(false);
                }
            }
            else {
                System.out.println("The specified current camera mode is not among the valid ones on front camera");
                assertTrue(false);
            }
        }
    }
    //Method to set flash setting
    public static void setFlash(WebDriver driver, String setting) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String menuChoice = "com.intel.camera22:id/hori_list_button";
        String flash = "com.intel.camera22:id/left_menus_flash_setting";
        String modeButton = "com.intel.camera22:id/mode_button";
        String singleGroup = "com.intel.camera22:id/mode_wave_photo";
        
        //If we can find the flash setting, we will click on it.  Otherwise, we try to open the left menu and check again.
        if (!driver.findElements(By.name("Flash settings")).isEmpty()) {
            driver.findElement(By.name("Flash settings")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
            wait(driver, 500);
        }
        else {
            driver.findElement(By.id(modeButton)).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(singleGroup)));
            wait(driver, 500);
            tap(driver, 0.2, 0.3);
            if (!driver.findElements(By.name("Flash settings")).isEmpty()) {
            driver.findElement(By.name("Flash settings")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
            wait(driver, 500);
            }
            else {
                System.out.println("The current shooting mode does not have flash setting!");
                assertTrue(false);
            }
        }
        //To set the flash setting to 'auto'
        if (setting.equals("auto")) {
            driver.findElements(By.id(menuChoice)).get(2).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
            wait(driver, 500);
        }
        //to set the flash setting to off
        else if (setting.equals("off")) {
            driver.findElements(By.id(menuChoice)).get(0).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
            wait(driver, 500);
        }
        //to set the flash setting to on
        else if (setting.equals("on")) {
            driver.findElements(By.id(menuChoice)).get(1).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
            wait(driver, 500);
        }
        else {
            System.out.println("Your specified Flash setting is not valid!");
            assertTrue(false);
        }
    }
    //Method to set Exposure setting
    public static void setExposure(WebDriver driver, String camMode, String setting) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String thirdMenu = "com.intel.camera22:id/second_hori_list_view_container";
        String menuChoice = "com.intel.camera22:id/hori_list_button";
        
        //Get camera shooting direction
        int camDirection = getCamDirection(driver);
        if (camDirection == 1) {
            //Tap on the Settings icon
            tapSettingsIcon(driver);
            //For single shooting camera
            if (camMode.equals("single")) {
                if (setting.equals("+2")) {
                    selectCamSetting(driver, 5, 11);
                }
                else if (setting.equals("+1")) {
                    selectCamSetting(driver, 5, 10);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 5, 9);
                }
                else if (setting.equals("-1")) {
                    selectCamSetting(driver, 5, 8);
                }
                else if (setting.equals("-2")) {
                    selectCamSetting(driver, 5, 7);
                }
                else {
                    System.out.println("Your Exposure setting is not valid!!");
                    assertTrue(false);
                }
            }
            //For Smile Cam mode
            else if (camMode.equals("smile")) {
                if (setting.equals("+2")) {
                    selectCamSetting(driver, 3, 10);
                }
                else if (setting.equals("+1")) {
                    selectCamSetting(driver, 3, 9);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 3, 8);
                }
                else if (setting.equals("-1")) {
                    selectCamSetting(driver, 3, 7);
                }
                else if (setting.equals("-2")) {
                    selectCamSetting(driver, 3, 6);
                }
                else {
                    System.out.println("Your Exposure setting is not valid!!");
                    assertTrue(false);
                }
            }
            //For Video mode
            else if (camMode.equals("video")) {
                if (setting.equals("+2")) {
                    selectCamSetting(driver, 3, 9);
                }
                else if (setting.equals("+1")) {
                    selectCamSetting(driver, 3, 8);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 3, 7);
                }
                else if (setting.equals("-1")) {
                    selectCamSetting(driver, 3, 6);
                }
                else if (setting.equals("-2")) {
                    selectCamSetting(driver, 3, 5);
                }
                else {
                    System.out.println("Your Exposure setting is not valid!!");
                    assertTrue(false);
                }
            }
            //For Burst modes
            else if (Arrays.asList("sburst", "fburst").contains(camMode)) {
                if (setting.equals("+2")) {
                    selectCamSetting(driver, 3, 8);
                }
                else if (setting.equals("+1")) {
                    selectCamSetting(driver, 3, 7);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 3, 6);
                }
                else if (setting.equals("-1")) {
                    selectCamSetting(driver, 3, 5);
                }
                else if (setting.equals("-2")) {
                    selectCamSetting(driver, 3, 4);
                }
                else {
                    System.out.println("Your Exposure setting is not valid!!");
                    assertTrue(false);
                }
            }
            //For Perfectshot mode
            else if (camMode.equals("perfect")) {
                if (setting.equals("+2")) {
                    selectCamSetting(driver, 2, 7);
                }
                else if (setting.equals("+1")) {
                    selectCamSetting(driver, 2, 6);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 2, 5);
                }
                else if (setting.equals("-1")) {
                    selectCamSetting(driver, 2, 4);
                }
                else if (setting.equals("-2")) {
                    selectCamSetting(driver, 2, 3);
                }
                else {
                    System.out.println("Your Exposure setting is not valid!!");
                    assertTrue(false);
                }
            }
            //For Panorama mode
            else if (camMode.equals("panorama")) {
                if (setting.equals("+2")) {
                    selectCamSetting(driver, 1, 7);
                }
                else if (setting.equals("+1")) {
                    selectCamSetting(driver, 1, 6);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 1, 5);
                }
                else if (setting.equals("-1")) {
                    selectCamSetting(driver, 1, 4);
                }
                else if (setting.equals("-2")) {
                    selectCamSetting(driver, 1, 3);
                }
                else {
                    System.out.println("Your Exposure setting is not valid!!");
                    assertTrue(false);
                }
            }
            else {
                System.out.println("Your current mode specification is not valid!!");
                assertTrue(false);
            }
        }
        else {
            System.out.println("Exposure settings are not available on front-facing camera!!");
            assertTrue(false);
        }
    }
    //Method to set White Balance setting
    public static void setWhiteBalance(WebDriver driver, String camMode, String setting) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String thirdMenu = "com.intel.camera22:id/second_hori_list_view_container";
        String menuChoice = "com.intel.camera22:id/hori_list_button";
        
        //Get camera shooting direction
        int camDirection = getCamDirection(driver);
        if (camDirection == 1) {
            //Tap on the Settings icon
            tapSettingsIcon(driver);
            //For single shooting camera
            if (camMode.equals("single")) {
                scrollCameraMenu(driver, 6, 5);
                if (setting.equals("cloudy")) {
                    selectCamSetting(driver, 5, 7);
                }
                else if (setting.equals("fluorescent")) {
                    selectCamSetting(driver, 5, 8);
                }
                else if (setting.equals("daylight")) {
                    selectCamSetting(driver, 5, 9);
                }
                else if (setting.equals("incandescent")) {
                    selectCamSetting(driver, 5, 10);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 5, 11);
                }
                else {
                    System.out.println("Your White Balance setting is not valid!!");
                    assertTrue(false);
                }
                //Returns the camera settings menu to its original position
                tapSettingsIcon(driver);
                scrollCameraMenu(driver, 5, 6);
            }
            //For Smile Cam mode
            else if (camMode.equals("smile")) {
                if (setting.equals("cloudy")) {
                    selectCamSetting(driver, 4, 6);
                }
                else if (setting.equals("fluorescent")) {
                    selectCamSetting(driver, 4, 7);
                }
                else if (setting.equals("daylight")) {
                    selectCamSetting(driver, 4, 8);
                }
                else if (setting.equals("incandescent")) {
                    selectCamSetting(driver, 4, 9);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 4, 10);
                }
                else {
                    System.out.println("Your White Balance setting is not valid!!");
                    assertTrue(false);
                }
            }
            //For Video mode
            else if (camMode.equals("video")) {
                if (setting.equals("cloudy")) {
                    selectCamSetting(driver, 4, 5);
                }
                else if (setting.equals("fluorescent")) {
                    selectCamSetting(driver, 4, 6);
                }
                else if (setting.equals("daylight")) {
                    selectCamSetting(driver, 4, 7);
                }
                else if (setting.equals("incandescent")) {
                    selectCamSetting(driver, 4, 8);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 4, 9);
                }
                else {
                    System.out.println("Your White Balance setting is not valid!!");
                    assertTrue(false);
                }
            }
            else {
                System.out.println("Your current mode does not have a White Balance setting!!");
                assertTrue(false);
            }
        }
        else {
            System.out.println("White Balance settings are not available on front-facing camera!!");
            assertTrue(false);
        }
    }
    //Method to set ISO setting
    public static void setISO(WebDriver driver, String camMode, String setting) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String thirdMenu = "com.intel.camera22:id/second_hori_list_view_container";
        String menuChoice = "com.intel.camera22:id/hori_list_button";
        
        //Get camera shooting direction
        int camDirection = getCamDirection(driver);
        if (camDirection == 1) {
            //Tap on the Settings icon
            tapSettingsIcon(driver);
            //For single shooting camera
            if (camMode.equals("single")) {
                scrollCameraMenu(driver, 6, 4);
                if (setting.equals("800")) {
                    selectCamSetting(driver, 5, 7);
                }
                else if (setting.equals("400")) {
                    selectCamSetting(driver, 5, 8);
                }
                else if (setting.equals("200")) {
                    selectCamSetting(driver, 5, 9);
                }
                else if (setting.equals("100")) {
                    selectCamSetting(driver, 5, 10);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 5, 11);
                }
                else {
                    System.out.println("Your ISO setting is not valid!!");
                    assertTrue(false);
                }
                //Returns the camera settings menu to its original position
                tapSettingsIcon(driver);
                scrollCameraMenu(driver, 4, 6);
            }
            //For Smile Cam mode
            else if (camMode.equals("smile")) {
                if (setting.equals("800")) {
                    selectCamSetting(driver, 5, 6);
                }
                else if (setting.equals("400")) {
                    selectCamSetting(driver, 5, 7);
                }
                else if (setting.equals("200")) {
                    selectCamSetting(driver, 5, 8);
                }
                else if (setting.equals("100")) {
                    selectCamSetting(driver, 5, 9);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 5, 10);
                }
                else {
                    System.out.println("Your ISO setting is not valid!!");
                    assertTrue(false);
                }
            }
            //For Panorama mode
            else if (camMode.equals("panorama")) {
                if (setting.equals("800")) {
                    selectCamSetting(driver, 2, 3);
                }
                else if (setting.equals("400")) {
                    selectCamSetting(driver, 2, 4);
                }
                else if (setting.equals("200")) {
                    selectCamSetting(driver, 2, 5);
                }
                else if (setting.equals("100")) {
                    selectCamSetting(driver, 2, 6);
                }
                else if (setting.equals("auto")) {
                    selectCamSetting(driver, 2, 7);
                }
                else {
                    System.out.println("Your ISO setting is not valid!!");
                    assertTrue(false);
                }
            }
            else {
                System.out.println("Your current mode does not have an ISO setting!!");
                assertTrue(false);
            }
        }
        else {
            System.out.println("ISO settings are not available on front-facing camera!!");
            assertTrue(false);
        }
    }
    //Method to set ISO setting
    public static void setTimer(WebDriver driver, String camMode, String setting) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String thirdMenu = "com.intel.camera22:id/second_hori_list_view_container";
        String menuChoice = "com.intel.camera22:id/hori_list_button";
        
        //Get camera shooting direction
        int camDirection = getCamDirection(driver);
        if (camDirection == 1) {
            //Tap on the Settings icon
            tapSettingsIcon(driver);
            //For Single shooting camera
            if (camMode.equals("single")) {
                scrollCameraMenu(driver, 6, 3);
                if (setting.equals("off")) {
                    selectCamSetting(driver, 6, 7);
                }
                else if (setting.equals("3")) {
                    selectCamSetting(driver, 6, 8);
                }
                else if (setting.equals("5")) {
                    selectCamSetting(driver, 6, 9);
                }
                else if (setting.equals("10")) {
                    selectCamSetting(driver, 6, 10);
                }
                else {
                    System.out.println("Your Timer setting is not valid!!");
                    assertTrue(false);
                }
                //Returns the camera settings menu to its original position
                tapSettingsIcon(driver);
                scrollCameraMenu(driver, 3, 6);
            }
            //For HDR mode
            else if (camMode.equals("hdr")) {
                if (setting.equals("off")) {
                    selectCamSetting(driver, 2, 3);
                }
                else if (setting.equals("3")) {
                    selectCamSetting(driver, 2, 4);
                }
                else if (setting.equals("5")) {
                    selectCamSetting(driver, 2, 5);
                }
                else if (setting.equals("10")) {
                    selectCamSetting(driver, 2, 6);
                }
                else {
                    System.out.println("Your Timer setting is not valid!!");
                    assertTrue(false);
                }
            }
            else {
                System.out.println("Your current mode does not have a Timer setting!!");
                assertTrue(false);
            }
        }
        else {
            System.out.println("Timer settings are not available on front-facing camera!!");
            assertTrue(false);
        }
    }
    //Method to set Scene settings
    public static void setScene(WebDriver driver, String camMode, String setting) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String secondMenu = "com.intel.camera22:id/hori_list_view_container";
        String thirdMenu = "com.intel.camera22:id/second_hori_list_view_container";
        String menuChoice = "com.intel.camera22:id/hori_list_button";
        
        //Get camera shooting direction
        int camDirection = getCamDirection(driver);
        if (camDirection == 1) {
            //Tap on the Settings icon
            tapSettingsIcon(driver);
            //For Single shooting camera
            if (camMode.equals("single")) {
                if (setting.equals("fireworks")) {
                    selectCamSetting(driver, 4, 7);
                }
                else if (setting.equals("barcode")) {
                    selectCamSetting(driver, 4, 8);
                }
                else if (setting.equals("night-portrait")) {
                    selectCamSetting(driver, 4, 9);
                }
                else if (setting.equals("portrait")) {
                    selectCamSetting(driver, 4, 10);
                }
                else if (setting.equals("landscape")) {
                    selectCamSetting(driver, 4, 11);
                }
                else if (setting.equals("night")) {
                    selectCamSetting(driver, 4, 12);
                }
                else if (setting.equals("sports")) {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(4).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 6, 3);
                    driver.findElements(By.id(menuChoice)).get(12).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    tapSettingsIcon(driver);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(4).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 3, 6);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                }
                else if (setting.equals("auto")) {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(4).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 6, 3);
                    driver.findElements(By.id(menuChoice)).get(13).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    tapSettingsIcon(driver);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(4).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 3, 6);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                }
                else {
                    System.out.println("Your Scene setting is not valid!!");
                    assertTrue(false);
                }
            }
            //For Smile Cam mode
            else if (camMode.equals("smile")) {
                if (setting.equals("fireworks")) {
                    selectCamSetting(driver, 2, 6);
                }
                else if (setting.equals("barcode")) {
                    selectCamSetting(driver, 2, 7);
                }
                else if (setting.equals("night-portrait")) {
                    selectCamSetting(driver, 2, 8);
                }
                else if (setting.equals("portrait")) {
                    selectCamSetting(driver, 2, 9);
                }
                else if (setting.equals("landscape")) {
                    selectCamSetting(driver, 2, 10);
                }
                else if (setting.equals("night")) {
                    selectCamSetting(driver, 2, 11);
                }
                else if (setting.equals("sports")) {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(2).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 6, 3);
                    driver.findElements(By.id(menuChoice)).get(11).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    tapSettingsIcon(driver);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(2).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 3, 6);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                }
                else if (setting.equals("auto")) {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(2).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 6, 3);
                    driver.findElements(By.id(menuChoice)).get(12).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    tapSettingsIcon(driver);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(2).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 3, 6);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                }
                else {
                    System.out.println("Your Scene setting is not valid!!");
                    assertTrue(false);
                }
            }
            //For Burst modes
            else if (Arrays.asList("sburst", "fburst").contains(camMode)) {
                if (setting.equals("fireworks")) {
                    selectCamSetting(driver, 2, 4);
                }
                else if (setting.equals("barcode")) {
                    selectCamSetting(driver, 2, 5);
                }
                else if (setting.equals("night-portrait")) {
                    selectCamSetting(driver, 2, 6);
                }
                else if (setting.equals("portrait")) {
                    selectCamSetting(driver, 2, 7);
                }
                else if (setting.equals("landscape")) {
                    selectCamSetting(driver, 2, 8);
                }
                else if (setting.equals("night")) {
                    selectCamSetting(driver, 2, 9);
                }
                else if (setting.equals("sports")) {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(2).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 6, 3);
                    driver.findElements(By.id(menuChoice)).get(9).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    tapSettingsIcon(driver);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(2).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 3, 6);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                }
                else if (setting.equals("auto")) {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(2).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 6, 3);
                    driver.findElements(By.id(menuChoice)).get(10).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    tapSettingsIcon(driver);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(2).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 3, 6);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                }
                else {
                    System.out.println("Your Scene setting is not valid!!");
                    assertTrue(false);
                }
            }
            //For Perfectshot mode
            else if (camMode.equals("perfect")) {
                if (setting.equals("fireworks")) {
                    selectCamSetting(driver, 1, 3);
                }
                else if (setting.equals("barcode")) {
                    selectCamSetting(driver, 1, 4);
                }
                else if (setting.equals("night-portrait")) {
                    selectCamSetting(driver, 1, 5);
                }
                else if (setting.equals("portrait")) {
                    selectCamSetting(driver, 1, 6);
                }
                else if (setting.equals("landscape")) {
                    selectCamSetting(driver, 1, 7);
                }
                else if (setting.equals("night")) {
                    selectCamSetting(driver, 1, 8);
                }
                else if (setting.equals("sports")) {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(1).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 6, 3);
                    driver.findElements(By.id(menuChoice)).get(8).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    tapSettingsIcon(driver);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(1).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 3, 6);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                }
                else if (setting.equals("auto")) {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(1).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 6, 3);
                    driver.findElements(By.id(menuChoice)).get(9).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    tapSettingsIcon(driver);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                    driver.findElements(By.id(menuChoice)).get(1).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(thirdMenu)));
                    wait(driver, 500);
                    scrollCamera2ndMenu(driver, 3, 6);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(secondMenu)));
                    wait(driver, 500);
                }
                else {
                    System.out.println("Your Scene setting is not valid!!");
                    assertTrue(false);
                }
            }
            else {
                System.out.println("Your current mode specification is not valid!!");
                assertTrue(false);
            }
        }
        else {
            System.out.println("Scene settings are not available on front-facing camera!!");
            assertTrue(false);
        }
    }
    //Method to check for the Camera Face Detection icon and toggle it.
    public static void toggleFD(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String singleGroup = "com.intel.camera22:id/mode_wave_photo";
        String modeButton = "com.intel.camera22:id/mode_button";
        
        //If we can find FD icon, we will click on it.  Otherwise, we try to reveal the camera left menu and check again.
        if (itemExists(driver, "name", "Face recognition")){
            tap(driver, "name", "Face recognition", 1, 500);
        }
        else {
            tap(driver, "id", modeButton, 1, 500);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(singleGroup)));
            tap(driver, 0.2, 0.3);
            wait(driver, 500);
            if (itemExists(driver, "name", "Face recognition")) {
                tap(driver, "name", "Face recognition", 1, 500);
                wait(driver, 500);
            }
            else {
                System.out.println("The current shooting mode does not have Face Detection capability!");
                assertTrue(false);
            }
        }
    }
    //Method to check for the Camera Flip icon and tap on it
    public static void flipCamera(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String singleGroup = "com.intel.camera22:id/mode_wave_photo";
        String modeButton = "com.intel.camera22:id/mode_button";
        
        //If we can find Camera Flip icon, we will tap on it.  Otherwise, we try to reveal the left camera menu and check again.
        if (itemExists(driver, "name", "Front and back camera switch")){
            tap(driver, "name", "Front and back camera switch", 1, 500);
        }
        else {
            tap(driver, "id", modeButton, 1, 500);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(singleGroup)));
            tap(driver, 0.2, 0.3);
            wait(driver, 500);
            if (itemExists(driver, "name", "Front and back camera switch")) {
                tap(driver, "name", "Front and back camera switch", 1, 500);
                wait(driver, 500);
            }
            else {
                System.out.println("The current shooting mode does not have the Camera Flip icon!");
                assertTrue(false);
            }
        }
    }
    // Method to capture image file(s);  numberOfCaptures is how many time capture is performed;  msHoldTime is how many msecs to hold the shutter button down.
    // msTimeBetweenTaps is the duration in msecs from one tap to the next for multiple taps, not multiple captures
    public static void imageCapture(WebDriver driver, String camMode, int numberOfCaptures, int msHoldTime, int msTimeBetweenTaps, int msWaitTime) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
        for (int n = 0; n < numberOfCaptures; n++) {
            if (n != 0) {
                if (Arrays.asList("sburst", "fburst", "perfect").contains(camMode)){
                    waitUntil(driver, "name", "Shutter button");
					wait(driver, 500);
                }
                else if (camMode.equals("hdr")) {
                    waitUntil(driver, "name", "Shutter button");
					wait(driver, 500);
                }
                else {
                    waitUntil(driver, "name", "Shutter button");
					wait(driver, 400);
                }
            }
            if (Arrays.asList("single", "hdr").contains(camMode)) {
                tap(driver, "name", "Shutter button", 1, 100);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
            }
			else if (Arrays.asList("sburst", "fburst", "perfect").contains(camMode)) {
                tap(driver, "name", "Shutter button", 1, 100);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
				wait(driver, 500);
            }
            else if (Arrays.asList("smile", "video", "panorama").contains(camMode)) {
                tap(driver, "name", "Shutter button", 1, 100);
                wait(driver, msTimeBetweenTaps);
				tap(driver, "name", "Shutter button", 1, 100);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
            }
            else if (camMode.equals("continuous")) {
                longTap(driver, "name", "Shutter button");
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
            }
            else {
                System.out.println("Sorry, the camera mode specified is not valid!!");
                assertTrue(false);
            }
        }
        wait(driver, 1000);
    } 
    // Method to capture image file(s);  numberOfCaptures is how many time capture is performed;  
    public static void imageCapture(WebDriver driver, String camMode, int numberOfCaptures, int msWaitTime) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
        for (int n = 0; n < numberOfCaptures; n++) {
            if (n != 0) {
                if (Arrays.asList("sburst", "fburst", "perfect").contains(camMode)){
                    waitUntil(driver, "name", "Shutter button");
					wait(driver, 500);
                }
                else if (camMode.equals("hdr")) {
                    waitUntil(driver, "name", "Shutter button");
					wait(driver, 500);
                }
                else {
                    waitUntil(driver, "name", "Shutter button");
					wait(driver, 400);
                }
            }
            if (Arrays.asList("single", "hdr").contains(camMode)) {
                tap(driver, "name", "Shutter button", 1, 100);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
            }
			else if (Arrays.asList("sburst", "fburst", "perfect").contains(camMode)) {
                tap(driver, "name", "Shutter button", 1, 100);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
				wait(driver, 500);
            }
            else if (Arrays.asList("smile", "video", "panorama").contains(camMode)) {
                tap(driver, "name", "Shutter button", 1, 100);
                wait(driver, 3000);
				tap(driver, "name", "Shutter button", 1, 100);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
            }
            else if (camMode.equals("continuous")) {
                longTap(driver, "name", "Shutter button");
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
            }
            else {
                System.out.println("Sorry, the camera mode specified is not valid!!");
                assertTrue(false);
            }
        }
        wait(driver, msWaitTime);
    }
    // Method to capture image file(s);  numberOfCaptures is how many time capture is performed; msTimeBetweenTaps is the time in msecs between each consecutive tap for 
    // modes that require more than one touch of the shutter button to capture
    public static void imageCapture(WebDriver driver, String camMode, int numberOfCaptures, int msTimeBetweenTaps, int msWaitTime) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
        for (int n = 0; n < numberOfCaptures; n++) {
            if (n != 0) {
                if (Arrays.asList("sburst", "fburst", "perfect").contains(camMode)){
                    waitUntil(driver, "name", "Shutter button");
					wait(driver, 500);
                }
                else if (camMode.equals("hdr")) {
                    waitUntil(driver, "name", "Shutter button");
					wait(driver, 500);
                }
                else {
                    waitUntil(driver, "name", "Shutter button");
					wait(driver, 400);
                }
            }
            if (Arrays.asList("single", "hdr").contains(camMode)) {
                tap(driver, "name", "Shutter button", 1, 100);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
            }
			else if (Arrays.asList("sburst", "fburst", "perfect").contains(camMode)) {
                tap(driver, "name", "Shutter button", 1, 100);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
				wait(driver, 500);
            }
            else if (Arrays.asList("smile", "video", "panorama").contains(camMode)) {
                tap(driver, "name", "Shutter button", 1, 100);
                wait(driver, msTimeBetweenTaps);
				tap(driver, "name", "Shutter button", 1, 100);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
            }
            else if (camMode.equals("continuous")) {
                longTap(driver, "name", "Shutter button");
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
            }
            else {
                System.out.println("Sorry, the camera mode specified is not valid!!");
                assertTrue(false);
            }
        }
        wait(driver, msWaitTime);
    }
    // Method to check if an element exists
    public static boolean itemExists(WebDriver driver, String locator, String elementName) {
        if (locator.equals("name")) {
            if (!driver.findElements(By.name(elementName)).isEmpty()) {
                return true;
            }
            else {
                System.out.printf("Cannot find the item '%s' using the locator '%s'! \n", elementName, locator);
                return false;
            }
        }
        else if (locator.equals("id")) {
            if (!driver.findElements(By.id(elementName)).isEmpty()) {
                return true;
            }
            else {
                System.out.printf("Cannot find the item '%s' using the locator '%s'! \n", elementName, locator);
                return false;
            }
        }
        else if (locator.equals("tag")) {
            if (!driver.findElements(By.tagName(elementName)).isEmpty()) {
                return true;
            }
            else {
                System.out.printf("Cannot find the item '%s' using the locator '%s'! \n", elementName, locator);
                return false;
            }
        }
        else {
            System.out.println("Sorry, the locator specified is not valid!!");
            return false;
        }
    }
    //Method to verify to see if a label exist.  The locators are "name, tagName, and id"
    public static boolean verifyExist(WebDriver driver, String locator, String label){
        // If locating by id:
        if (locator == "id") {
            if (itemExists(driver, "id", label)) {
                System.out.printf("Found the element by the %s '%s' on the screen!\n", locator, label);
                return true;
            }
            else {
                System.out.printf("The specified element by the %s '%s' was not found on the screen!\n", locator, label);
                return false;
            }
        }
        // If locating by name:
        else if (locator == "name") {
            if (itemExists(driver, "name", label)) {
                System.out.printf("Found the element by the %s '%s' on the screen!\n", locator, label);
                return true;
            }
            else {
                System.out.printf("The specified element by the %s '%s' was not found on the screen!\n", locator, label);
                return false;
            }
        }
        // If locating by tagName:
        else if (locator == "tagName") {
            if (itemExists(driver, "tag", label)) {
                System.out.printf("Found the element by the %s '%s' on the screen!\n", locator, label);
                return true;
            }
            else {
                System.out.printf("The specified element by the %s '%s' was not found on the screen!\n", locator, label);
                return false;
            }
        }
        // If locating by a locator not defined here, print a message and return 'false'.
        else {
            System.out.printf("Unfortunately, the '%s' locator was not defined.\n", locator);
            return false;
        }
    }
    //  Method to send a key-event
    public static void sendKeyEvent(WebDriver driver, Integer code) { 
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Integer> keycode = new HashMap<String, Integer>(); 
        keycode.put("keycode", code); 
        js.executeScript("mobile: keyevent", keycode); 
    } 
    // Method to tap on a coordinate in pixels
    public static void tap(WebDriver driver, Integer x_coordinate, Integer y_coordinate) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Integer> tapObject = new HashMap<String, Integer>();
        tapObject.put("x", x_coordinate); // in pixels from left
        tapObject.put("y", y_coordinate); // in pixels from top
        js.executeScript("mobile: tap", tapObject);
    }
    // Method to tap on a coordinate in percentage
    public static void tap(WebDriver driver, Double x_percent, Double y_percent) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Double> tapObject = new HashMap<String, Double>();
        tapObject.put("x", x_percent); // in percentage of the screen width from the left
        tapObject.put("y", y_percent); // in percentage of the screen height from the top
        js.executeScript("mobile: tap", tapObject);
    }
    // Method to tap on an element based on name, id, or tagName for a number of taps with the interval in between each tap specified in miliseconds
    public static void tap(WebDriver driver, String locator, String elementName, int numberOfTaps, int msInterval) {
        for (int n = 0; n < numberOfTaps; n++) {
            if (locator.equals("name")) {
                driver.findElement(By.name(elementName)).click();
            }
            else if (locator.equals("id")) {
                driver.findElement(By.id(elementName)).click();
            }
            else if (locator.equals("tag")) {
                driver.findElement(By.tagName(elementName)).click();
            }
            else {
                System.out.println("Sorry, the locator specified is not valid!!");
                assertTrue(false);
            }
            wait(driver, msInterval);
        }
    }
	// Method to long-tap on a coordinate in pixels
    public static void longTap(WebDriver driver, Integer x_coordinate, Integer y_coordinate) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Integer> longTapObject = new HashMap<String, Integer>();
        longTapObject.put("x", x_coordinate); // in pixels from left
        longTapObject.put("y", y_coordinate); // in pixels from top
		//longTapObject.put("duration", duration);
        js.executeScript("mobile: longClick", longTapObject);
		wait(driver, 400);
    }
	// Method to long-tap on a coordinate in percentage
    public static void longTap(WebDriver driver, Double x_coordinate, Double y_coordinate) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Double> longTapObject = new HashMap<String, Double>();
        longTapObject.put("x", x_coordinate); // in percentage from left
        longTapObject.put("y", y_coordinate); // in percentage from top
		//longTapObject.put("duration", duration);
        js.executeScript("mobile: longClick", longTapObject);
		wait(driver, 400);
    }
	// Method to long-tap on an item given its locator and identifier
    public static void longTap(WebDriver driver, String locator, String item) {
        longTap(driver, getItemXCoord(driver, locator, item), getItemYCoord(driver, locator, item));
    }
    //Method to calculate and return the integer x-coordinate of the center of an item given its locator and identifier
	public static int getItemXCoord(WebDriver driver, String locator, String item) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement el = null;
		int xCoordinate = 0;
		int itemWidth = 0;
		
		//Wait and find the item on screen.  Then assign it to 'el'
		waitUntil(driver, locator, item);
		wait(driver, 400);
		if (locator.equals("name")) {
			el = driver.findElement(By.name(item));
		}
		else if (locator.equals("id")) {
			el = driver.findElement(By.id(item));
		}
		else if (locator.equals("tag")) {
			el = driver.findElement(By.tagName(item));
		}
		else {
			System.out.println("Sorry, the locator specified is not valid!!");
			assertTrue(false);
        }
		//Find the item's upper left point.
		Point p = ((Locatable)el).getCoordinates().onPage();
		//Find the item's width to calculate its center
		itemWidth = el.getSize().width;
		xCoordinate = p.x + (itemWidth / 2);
        return xCoordinate;
    }
    //Method to calculate and return the integer y-coordinate of the center of an item given its locator and identifier
    public static int getItemYCoord(WebDriver driver, String locator, String item) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement el = null;
		int yCoordinate = 0;
		int itemHeight = 0;
		
		//Wait and find the item on screen.  Then assign it to 'el'
		waitUntil(driver, locator, item);
		wait(driver, 400);
		if (locator.equals("name")) {
			el = driver.findElement(By.name(item));
		}
		else if (locator.equals("id")) {
			el = driver.findElement(By.id(item));
		}
		else if (locator.equals("tag")) {
			el = driver.findElement(By.tagName(item));
		}
		else {
			System.out.println("Sorry, the locator specified is not valid!!");
			assertTrue(false);
        }
		//Find the item's upper left point.
		Point p = ((Locatable)el).getCoordinates().onPage();
		//Find the item's width to calculate its center
		itemHeight = el.getSize().height;
		yCoordinate = p.y + (itemHeight / 2);
        return yCoordinate;
    }
    // Method to tap on the Done button on the soft keyboard.  The wait time after the tap is specified by msWait.  
    public static void tapDone(WebDriver driver, String deviceType, int msWait) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        int sectionWidth = (getFrameWidth(driver) / 10);
        int sectionHeight = (getFrameHeight(driver) / 10);
        
        if (Arrays.asList("saltbay", "moorfield").contains(deviceType)) {
            int xCoord = getFrameWidth(driver) - sectionWidth;
            int yCoord = getFrameHeight(driver) - (sectionHeight / 2);
            tap(driver, xCoord, yCoord);
        }
        else if (deviceType.equals("baytrail")) {
            int xCoord = getFrameWidth(driver) - sectionWidth;
            int yCoord = getFrameHeight(driver) - (7 * sectionHeight / 2);
            tap(driver, xCoord, yCoord);
        }
        wait(driver, msWait);
    }
    // Method to swipe the screen given the starting and ending coordinates in percentage of the screen
    public static void swipe(WebDriver driver, Double startX, Double startY, Double endX, Double endY) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Double> swipeObject = new HashMap<String, Double>();
        swipeObject.put("startX", startX);
        swipeObject.put("startY", startY);
        swipeObject.put("endX", endX);
        swipeObject.put("endY", endY);
        swipeObject.put("duration", 0.4);
        js.executeScript("mobile: swipe", swipeObject);
    }
    // Method to swipe the screen given the starting and ending coordinates in percentage of the screen
    // This is a hack and it seemed to work, except the test failed out at the end.  Use this in test case:  Dut.swipe(driver, 360, 1190, 360, 1190, 1000);
    public static void swipe(WebDriver driver, int startX, int startY, int endX, int endY, int msDuration) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Integer> swipeObject = new HashMap<String, Integer>();
        swipeObject.put("startX", startX);
        swipeObject.put("startY", startY);
        swipeObject.put("endX", endX);
        swipeObject.put("endY", endY);
        swipeObject.put("duration", msDuration);
        js.executeScript("mobile: swipe", swipeObject);
    }
    // Method to scroll a list or menu from the middle of the start element to the middle of the end element
    public static void scrollMenu(WebDriver driver, String startItemLocator, String startItem, String endItemLocator, String endItem) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        WebElement menuContainer = null;
        waitUntil(driver, startItemLocator, startItem);
        if (startItemLocator.equals("name")) {
            menuContainer = driver.findElement(By.name(startItem));
        }
        else if (startItemLocator.equals("id")) {
            menuContainer = driver.findElement(By.id(startItem));
        }
        else if (startItemLocator.equals("tag")) {
            menuContainer = driver.findElement(By.tagName(startItem));
        }
        Point p = ((Locatable)menuContainer).getCoordinates().onPage();
        int containerWidth = menuContainer.getSize().width;
        int containerHeight = menuContainer.getSize().height;
        double startXCoord = p.x + (containerWidth / 2);
        double startYCoord = p.y + (containerHeight / 2);
        waitUntil(driver, endItemLocator, endItem);
        if (endItemLocator.equals("name")) {
            menuContainer = driver.findElement(By.name(endItem));
        }
        else if (endItemLocator.equals("id")) {
            menuContainer = driver.findElement(By.id(endItem));
        }
        else if (endItemLocator.equals("tag")) {
            menuContainer = driver.findElement(By.tagName(endItem));
        }
        p = ((Locatable)menuContainer).getCoordinates().onPage();
        containerWidth = menuContainer.getSize().width;
        containerHeight = menuContainer.getSize().height;
        double endXCoord = p.x + (containerWidth / 2);
        double endYCoord = p.y + (containerHeight / 2);
        swipe(driver, startXCoord, startYCoord, endXCoord, endYCoord);
        wait(driver, 500);
    }
    // Method to scroll menu a number of menu choices.  Starts at the center of the menu choice and ends at the center of another menu choice.
    public static void scrollCameraMenu(WebDriver driver, int startElement, int endElement) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        Dut.wait(driver, 500);
        List<WebElement> menuList = driver.findElements(By.tagName("FrameLayout"));
        WebElement menuContainer = menuList.get(8);
        Point p = ((Locatable)menuContainer).getCoordinates().onPage();
        int containerWidth = menuContainer.getSize().width;
        double yCoord = p.y + (menuContainer.getSize().height / 2);
        double startXCoord = (startElement * containerWidth) - (containerWidth / 2);
        double endXCoord = (endElement * containerWidth) - (containerWidth / 2);
        swipe(driver, startXCoord, yCoord, endXCoord, yCoord);
    }
    // Method to scroll the 2ndary menu a number of menu choices.  Starts at the center of the menu choice and ends at the center of another menu choice.
    public static void scrollCamera2ndMenu(WebDriver driver, int startElement, int endElement) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        Dut.wait(driver, 500);
        List<WebElement> menuList = driver.findElements(By.tagName("FrameLayout"));
        WebElement menuContainer = menuList.get(8);
        Point p = ((Locatable)menuContainer).getCoordinates().onPage();
        int containerWidth = menuContainer.getSize().width;
        double yCoord = p.y + (3 * menuContainer.getSize().height / 2);
        double startXCoord = (startElement * containerWidth) - (containerWidth / 2);
        double endXCoord = (endElement * containerWidth) - (containerWidth / 2);
        swipe(driver, startXCoord, yCoord, endXCoord, yCoord);
    }
    // Method to put the driver to sleep for a number of mili-seconds
    public static void wait(WebDriver driver, Integer milisec) {
        try {
            Thread.sleep(milisec);
        } catch(InterruptedException e) {}
    }
    // Method to wait until something appears
    public static void waitUntil(WebDriver driver, String itemLocator, String item) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        if (itemLocator.equals("name")) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name(item)));
        }
        else if (itemLocator.equals("id")) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(item)));
        }
        else if (itemLocator.equals("tag")) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(item)));
        }
        else {
            System.out.printf("waitUntil method error:  The specified locator '%s' is not a valid one!\n", itemLocator);
            assertTrue(false);
        }
    }
    // Method to select a choice from a dropdown menu 
    public static void selectMenuChoice(WebDriver driver, String menuName, String menuChoice) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait(driver, 500);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(menuName)));
        WebElement menu = driver.findElement(By.name(menuName));
        //Click on the Extras Menu.
        menu.click();
        //Find and click on the 'Details' selection
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(menuChoice)));
        driver.findElement(By.name(menuChoice)).click();
        wait(driver, 300);
    }
    // Method to go back to Albumset View
    public static void goToAlbumView(WebDriver driver, String view) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
        if (view == "Full View") {
            if (itemExists(driver, "name", "More options")) {
				tap(driver, "id", "android:id/home", 1, 500);
            }
            else {
                //Tap on the middle of the screen to reveal the actionbar
                tap(driver, 0.2, 0.2);
                wait(driver, 500);
                // Tap on the Gallery icon
				tap(driver, "id", "android:id/home", 1, 500);
            }
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("android:id/home")));
            // Tap on the Gallery icon again
            tap(driver, "id", "android:id/home", 1, 500);
        }
        else if (view == "Grid View") {
        // Tap on the Gallery icon
        tap(driver, "id", "android:id/home", 1, 500);
        }
    }
    // Method to select a folder at a given coordinates
    public static void selectFolderAt(WebDriver driver, int xCoord, int yCoord) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        /* WebElement menu = driver.findElement(By.name("More options"));
        //Pick the 'Select album' choice from the Extras Menu
        selectMenuChoice(driver, "More options", "Select album");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("0 selected"))); */
        //Long-tap on the folder to select it.
        longTap(driver, xCoord, yCoord);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("1 selected")));
    }
    // Method to select a folder at a given x and y percentage
    public static void selectFolderAt(WebDriver driver, Double xCoord, Double yCoord) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        /* WebElement menu = driver.findElement(By.name("More options"));
        //Pick the 'Select album' choice from the Extras Menu
        selectMenuChoice(driver, "More options", "Select album");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("0 selected"))); */
        //Long-tap on the folder to select it.
        longTap(driver, xCoord, yCoord);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("1 selected")));
    }
    // Method to find a folder's name at a given coordinates
    public static String findFolderTitleAt(WebDriver driver, int xCoord, int yCoord) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        selectFolderAt(driver, xCoord, yCoord);
		//Select the Details choice from the Overflow menu
		selectMenuChoice(driver, "More options", "Details");
        //Click on the Extras Menu.
		Dut.wait(driver, 400);
        List<WebElement> textViews = driver.findElements(By.tagName("TextView"));
        String titleText = textViews.get(1).getText();
        Pattern titlePattern = Pattern.compile("Title : (.+)");
        //Searching for pattern titlePattern in titleText
        Matcher m = titlePattern.matcher(titleText);
        String albumTitle = "";
        if (m.find()) {
            albumTitle = m.group(1).toString();
            System.out.printf("The album at coordinates (%d, %d) is titled '%s'.\n", xCoord, yCoord, albumTitle);
        }
        else {
            System.out.println("Could not find the album's title!");
            assertTrue(false);
        }        
        driver.findElement(By.name("Close")).click();
        return albumTitle;
    }  
    // Method to find a folder's name at a given x and y percentage.  Assumes user is on Albumset View.
    public static String findFolderTitleAt(WebDriver driver, Double xCoord, Double yCoord) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        selectFolderAt(driver, xCoord, yCoord);
		//Select the Details choice from the Overflow menu
		selectMenuChoice(driver, "More options", "Details");
        //Click on the Extras Menu.
		Dut.wait(driver, 400);
        List<WebElement> textViews = driver.findElements(By.tagName("TextView"));
        String titleText = textViews.get(1).getText();
        Pattern titlePattern = Pattern.compile("Title : (.+)");
        //Searching for pattern titlePattern in titleText
        Matcher m = titlePattern.matcher(titleText);
        String albumTitle = "";
        if (m.find()) {
            albumTitle = m.group(1).toString();
            System.out.printf("The album at coordinates (%d, %d) is titled '%s'.\n", xCoord, yCoord, albumTitle);
        }
        else {
            System.out.println("Could not find the album's title!");
            assertTrue(false);
        }        
        driver.findElement(By.name("Close")).click();
        return albumTitle;
    }
    // Method to take a certain number of pictures from the Gallery, assuming user is on Albumset View 
    public static void takePicturesFromGallery(WebDriver driver, int numberOfShots) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //Switch to camera and perform one capture.
        driver.findElement(By.name("Switch to camera")).click();
        //If the 'Complete action' dialog pops up and we see the 'Just once' button already, select the Social Camera application
        if (itemExists(driver, "name", "Complete action using")) {
            driver.findElement(By.name("com.android.camera2")).click();
            driver.findElement(By.name("com.intel.camera22")).click();
        }
        if (itemExists(driver, "name", "Just once")){
            driver.findElement(By.name("Just once")).click();
        }
        if (itemExists(driver, "name", Data.REMEMBER_LOCATION)) {
            tap(driver, "name", "Cancel", 1, 500);
            wait(driver, 300);
        }
        else {
            System.out.println("The Remember Location diaglog was not seen!");
        }
        //wait(driver, 1000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Find and click on the 'Shutter' button however number of times specified
        WebElement shutter = driver.findElement(By.name("Shutter button"));
        for (int counter = 0; counter < numberOfShots; counter++) {
            shutter.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
            wait(driver, 1000);
        }
        wait(driver, 1000);
        //Return to SGallery by sending a keyevent using keycode '4' for the Back button
        Dut.sendKeyEvent(driver, 4);
        //Wait until we find the camera icon to be present. 
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Switch to camera")));
    }
    // Method to take and open a picture in Social Gallery Full View, assuming user is on Albumset View in Gallery at first
    public static void takeAndOpenPicture(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        //Switch to camera and perform one capture.
        driver.findElement(By.name("Switch to camera")).click();
        //If the 'Complete action' dialog pops up and we see the 'Just once' button already, select the Social Camera application
        if (itemExists(driver, "name", "Complete action using")) {
            driver.findElement(By.name("com.android.camera2")).click();
            driver.findElement(By.name("com.intel.camera22")).click();
        }
        if (itemExists(driver, "name", "Just once")){
            driver.findElement(By.name("Just once")).click();
        }
        if (itemExists(driver, "name", Data.REMEMBER_LOCATION)) {
            tap(driver, "name", "Cancel", 1, 500);
            wait(driver, 300);
        }
        else {
            System.out.println("The Remember Location diaglog was not seen!");
        }
        //wait(driver, 1000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Shutter button")));
        //Find and click on the 'Shutter' button 
        WebElement shutter = driver.findElement(By.name("Shutter button"));
        shutter.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Most recent photo")));
        wait(driver, 1000);
        //Tap on the thumbnail
        driver.findElement(By.name("Most recent photo")).click();
        wait(driver, 1500);
        //Tap on the screen to expose the actionbar
        tap(driver, 0.3, 0.3);
        wait(driver, 1000);
    }
    // Method to delete a file in Full View
    public static void deleteFileOnFullView(WebDriver driver, int numberOfDeletion) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        for (int counter = 0; counter < numberOfDeletion; counter++) {
            if (!driver.findElements(By.name("More options")).isEmpty()) {
                if (!driver.findElements(By.name("Delete")).isEmpty()) {
                    driver.findElement(By.name("Delete")).click();
                    driver.findElement(By.name("Delete")).click();
                }
                else {
                    driver.findElement(By.name("More options")).click();
                    driver.findElement(By.name("Delete")).click();
                    driver.findElement(By.name("Delete")).click();
                }
            }
            else {
                tap(driver, 0.2, 0.2);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.name("More options")));
                if (!driver.findElements(By.name("Delete")).isEmpty()) {
                    driver.findElement(By.name("Delete")).click();
                    driver.findElement(By.name("Delete")).click();
                }
                else {
                    driver.findElement(By.name("More options")).click();
                    driver.findElement(By.name("Delete")).click();
                    driver.findElement(By.name("Delete")).click();
                }
            }
            wait(driver, 500);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("android:id/progress_circular")));
        }
    }
    // Method to tag a folder at the specified coordinates with the specified tag term 
    public static void tagFolderAtWith(WebDriver driver, int xCoord, int yCoord, String tagTerm) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        selectFolderAt(driver, xCoord, yCoord);
        //Pick the 'Add a keyword' selection from the Extras Menu 
        selectMenuChoice(driver, "More options", "Add a keyword");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Enter new keyword")));
        //Find and type the text "Before image capture tag" into the Edit Text view
        driver.findElement(By.name("Enter new keyword")).sendKeys(tagTerm);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(tagTerm)));
        //Enter the text by tapping on the Done button on the soft keyboard using coordinates.
        tap(driver, 715, 1120);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Search")));
    }
    // Method to tag a folder at the specified x and y percentage with the specified tag term 
    public static void tagFolderAtWith(WebDriver driver, Double xCoord, Double yCoord, String tagTerm) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        selectFolderAt(driver, xCoord, yCoord);
        //Pick the 'Add a keyword' selection from the Extras Menu 
        selectMenuChoice(driver, "More options", "Add a keyword");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Enter new keyword")));
        //Find and type the text "Before image capture tag" into the Edit Text view
        driver.findElement(By.name("Enter new keyword")).sendKeys(tagTerm);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(tagTerm)));
        //Enter the text by tapping on the Done button on the soft keyboard using coordinates.
        tap(driver, 715, 1120);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Search")));
    }
    // Method to find the number of files tag with a certain keyword
    public static int findNumberOfTaggedFiles(WebDriver driver, String tagTerm) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        if (driver.findElement(By.name("Search")).isDisplayed()) {
            driver.findElement(By.name("Search")).click();
        }
        else {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Search")));
            driver.findElement(By.name("Search")).click();
        }
        driver.findElements(By.tagName("EditText")).get(0).sendKeys(tagTerm);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("TextView")));
        String resultText = driver.findElement(By.tagName("TextView")).getText();
        Pattern p = Pattern.compile(tagTerm + " \\(" + "(\\d+)(\\))");
        Matcher m = p.matcher(resultText);
        int numberOfFilesTagged = 0;
        if (m.find()) {
            Dut.sendKeyEvent(driver, 4);
            Dut.sendKeyEvent(driver, 4);
            numberOfFilesTagged = Integer.parseInt(m.group(1));
            System.out.printf("There are %d files tagged with the term '%s'.\n", numberOfFilesTagged, tagTerm);
            return numberOfFilesTagged;
        }
        else {
            Dut.sendKeyEvent(driver, 4);
            Dut.sendKeyEvent(driver, 4);
            System.out.printf("There are %d files tagged with the term '%s'.\n", numberOfFilesTagged, tagTerm);
            return numberOfFilesTagged;
        }
    }
    //Method to extract file's name from the Full View's actionbar display
    public static String getFileNameFromActionbar(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        String fileName = "";
        if (driver.findElement(By.name("More options")).isDisplayed()) {
            List<WebElement> textViews = driver.findElements(By.tagName("TextView"));
            fileName = textViews.get(1).getText();
        }
        else {
            tap(driver, 0.5, 0.25);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("More options")));
            List<WebElement> textViews = driver.findElements(By.tagName("TextView"));
            fileName = textViews.get(1).getText();
        }
        System.out.printf("The file name extracted from the Gallery actionbar is '%s'.\n", fileName);
        return fileName;
    }
    //Method to calculate the x-coordinate of the center of Gallery thumbnails or tiles
    public static int calcTileXCoord(WebDriver driver, int tileOrder, int totalTiles) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        int orientation = 1;
        int totalColumns = 1;
        int tilesPerColumn = 4;
        int tileColumnLocation = 1;
        int xCoordinate = 0; 
        
        List<WebElement> frames = driver.findElements(By.tagName("FrameLayout"));
        int frameHeight = frames.get(0).getSize().height;
        int frameWidth = frames.get(0).getSize().width;
        int centerXCoord = frameWidth / 2;
        //Determine the device's orientation:
        if (frameWidth > frameHeight) {
            orientation = 2;
            tilesPerColumn = 2;
        }
        //Determine the thumbnail width and height
        WebElement el = driver.findElement(By.id("android:id/action_bar_container"));
        Point p = ((Locatable)el).getCoordinates().onPage();
        int galleryDisplayHeight = (frameHeight - p.y - el.getSize().height);
        int tileHeight = galleryDisplayHeight / tilesPerColumn;
        int tileWidth = tileHeight;
        //Calculate the total number of columns 
        if ((totalTiles % tilesPerColumn) > 0) {
            totalColumns = totalTiles / tilesPerColumn + 1;
        }
        else {
            totalColumns = totalTiles / tilesPerColumn;
        }
        //Calculate the column in which the thumbnail is located
        if ((tileOrder % tilesPerColumn) > 0) {
            tileColumnLocation = tileOrder / tilesPerColumn + 1;
        }
        else {
            tileColumnLocation = tileOrder / tilesPerColumn;
        }
        //Calculate and return the x-coordinate of the column in which the tile is located
        switch (orientation) {
            //For portrait orientation
            case 1:
                //When there is only one column
                if (totalColumns == 1) {
                    xCoordinate = frameWidth / 2;
                }
                //When there are two columns
                else if (totalColumns == 2) {
                    if (tileColumnLocation == 1) {
                        xCoordinate = centerXCoord - tileWidth / 2;
                    }
                    else if (tileColumnLocation == 2) {
                        xCoordinate = centerXCoord + tileWidth / 2;
                    }
                }
                //When there are three columns or more
                else {
                    if (tileColumnLocation == 1) {
                        xCoordinate = tileWidth / 2;
                    }
                    else if (tileColumnLocation == 2) {
                        xCoordinate = (3 * tileWidth / 2);
                    }
                    else if (tileColumnLocation == 3) {
                        xCoordinate = (5 * tileWidth / 2); 
                    }
                }
                break;
            //For landscape orientation
            case 2:
                //When there is only one column
                if (totalColumns == 1) {
                    xCoordinate = frameWidth / 2;
                }
                //When there are two columns
                else if (totalColumns == 2) {
                    if (tileColumnLocation == 1) {
                        xCoordinate = centerXCoord - tileWidth / 2;
                    }
                    else if (tileColumnLocation == 2) {
                        xCoordinate = centerXCoord + tileWidth / 2;
                    }
                }
                //When there are three columns
                else if (totalColumns == 3) {
                    if (tileColumnLocation == 1) {
                        xCoordinate = centerXCoord - tileWidth;
                    }
                    else if (tileColumnLocation == 2) {
                        xCoordinate = centerXCoord;
                    }
                    else if (tileColumnLocation == 3) {
                        xCoordinate = centerXCoord + tileWidth; 
                    }
                }
                //When there are four columns
                else if (totalColumns == 4) {
                    if (tileColumnLocation == 1) {
                        xCoordinate = centerXCoord - (3 * tileWidth / 2);
                    }
                    else if (tileColumnLocation == 2) {
                        xCoordinate = centerXCoord - (tileWidth / 2);
                    }
                    else if (tileColumnLocation == 3) {
                        xCoordinate = centerXCoord + (tileWidth / 2); 
                    }
                    else {
                        xCoordinate = centerXCoord + (3 * tileWidth / 2);
                    }
                }
                //When there are more than four columns
                else {
                    if (tileColumnLocation == 1) {
                        xCoordinate = (tileWidth / 2);
                    }
                    else if (tileColumnLocation == 2) {
                        xCoordinate = (3 * tileWidth / 2);
                    }
                    else if (tileColumnLocation == 3) {
                        xCoordinate = (5 * tileWidth / 2); 
                    }
                    else if (tileColumnLocation == 4) {
                        xCoordinate = (7 * tileWidth / 2);
                    }
                    else {
                        xCoordinate = frameWidth - 10;
                    }
                }
                break;
        }
        return xCoordinate;
    }
    //Method to calculate the y-coordinate of a Gallery thumbnail or tile on the first screen
    public static int calcTileYCoord(WebDriver driver, int tileOrder, int totalTiles) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        int orientation = 1;
        int totalColumns = 1;
        int tilesPerColumn = 4;
        int tileColumnLocation = 1;
        int yCoordinate = 0; 
        
        List<WebElement> frames = driver.findElements(By.tagName("FrameLayout"));
        int frameHeight = frames.get(0).getSize().height;
        int frameWidth = frames.get(0).getSize().width;
        int bottomYCoord = frameHeight;
        //Determine the device's orientation:
        if (frameWidth > frameHeight) {
            orientation = 2;
            tilesPerColumn = 2;
        }
        //Determine the thumbnail height
        WebElement el = driver.findElement(By.id("android:id/action_bar_container"));
        Point p = ((Locatable)el).getCoordinates().onPage();
        int galleryDisplayHeight = (frameHeight - p.y - el.getSize().height);
        int tileHeight = galleryDisplayHeight / tilesPerColumn;
        int centerYCoord = frameHeight - (galleryDisplayHeight / 2);
        //Calculate the total number of columns 
        if ((totalTiles % tilesPerColumn) > 0) {
            totalColumns = totalTiles / tilesPerColumn + 1;
        }
        else {
            totalColumns = totalTiles / tilesPerColumn;
        }
        //Calculate the column in which the thumbnail is located
        if ((tileOrder % tilesPerColumn) > 0) {
            tileColumnLocation = tileOrder / tilesPerColumn + 1;
        }
        else {
            tileColumnLocation = tileOrder / tilesPerColumn;
        }
        //Calculate the y-coordinate of the tile
        switch (orientation) {
            //For portrait orientation
            case 1:
                //When there is only one tile
                if (totalTiles == 1) {
                    yCoordinate = centerYCoord;
                }
                //When there are only two tiles
                else if (totalTiles == 2) {
                    if (tileOrder == 1) {
                        yCoordinate = centerYCoord - tileHeight / 2;
                    }
                    else if (tileOrder == 2) {
                        yCoordinate = centerYCoord + tileHeight / 2;
                    }
                }
                //When there are only three tiles
                else if (totalTiles == 3) {
                    if (tileOrder == 1) {
                        yCoordinate = centerYCoord - tileHeight;
                    }
                    else if (tileOrder == 2) {
                        yCoordinate = centerYCoord;
                    }
                    else {
                        yCoordinate = centerYCoord + tileHeight; 
                    }
                }
                //When there are four tiles or more
                else {
                    //If the column in which the tile is in is not among the first three columns, we do nothing and the y-coordinate still remains at 0.
                    if (tileColumnLocation > 3) {
                        ;
                    }
                    else {
                        //Checking to see what row the tile is located on
                        switch (tileOrder % tilesPerColumn) {
                            //When the tile is in the first row
                            case 1:
                                yCoordinate = bottomYCoord - (7 * tileHeight / 2);
                                break;
                            //When the tile is in the second row
                            case 2:
                                yCoordinate = bottomYCoord - (5 * tileHeight / 2);
                                break;
                            //When the tile is in the third row
                            case 3:
                                yCoordinate = bottomYCoord - (3 * tileHeight / 2);
                                break;
                            //When the tile is in the bottom row
                            default:
                                yCoordinate = bottomYCoord - (tileHeight / 2);
                                break;
                        }
                    }
                }
                break;
            //For landscape orientation
            case 2:
                //When there is only one tile
                if (totalTiles == 1) {
                    yCoordinate = centerYCoord;
                }
                //When there are two tiles or more
                else {
                    //If the column in which the tile is in is not among the first four columns, we do nothing and the y-coordinate still remains at 0.
                    if (tileColumnLocation > 4) {
                        ;
                    }
                    else {
                        //Checking to see what row the tile is located on
                        switch (tileOrder % tilesPerColumn) {
                            //When the tile is in the first row
                            case 1:
                                yCoordinate = bottomYCoord - (3 * tileHeight / 2);
                                break;
                            //When the tile is in the bottom row
                            case 0:
                                yCoordinate = bottomYCoord - (tileHeight / 2);
                                break;
                        }
                    }
                }
                break;
        }
        return yCoordinate;
    }
    //Method to tap on a specified thumbnail in Gallery
    public static void tapOnTile(WebDriver driver, int tileOrder, int totalTiles) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        // Wait until we find the 'More options' icon before proceeding further.
        // wait.until(ExpectedConditions.presenceOfElementLocated(By.name("More options")));
        tap(driver, calcTileXCoord(driver, tileOrder, totalTiles), calcTileYCoord(driver, tileOrder, totalTiles));
        wait(driver, 1500);
    }
	//Method to long-tap on a specified thumbnail in Gallery
    public static void longTapOnTile(WebDriver driver, int tileOrder, int totalTiles) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        // Wait until we find the 'More options' icon before proceeding further.
        // wait.until(ExpectedConditions.presenceOfElementLocated(By.name("More options")));
        longTap(driver, calcTileXCoord(driver, tileOrder, totalTiles), calcTileYCoord(driver, tileOrder, totalTiles));
        wait(driver, 400);
    }
    // Method to open a folder providing the folder's name and the number of total tiles.  This only works for folders that appear on the first page of Albumset View
    public static void openFolder(WebDriver driver, String folderName, int totalTiles) {
        //Determine the total number of tiles on the first screen of the View.  If totalTiles is more than that, then we can't delete the folder
        int columnsPerScreen = (int) Math.round(calcDisplayWidth(driver) / (double) calcTileHeight(driver));
        int tilesPerScreen = columnsPerScreen * getTilesPerColumn(driver);
        if (totalTiles > tilesPerScreen) {
            System.out.println("Sorry, we can't open the folder based on the number of totalTiles given.");
            System.out.printf("Please fix the number of totalTiles to be equal to or smaller than '%d' for this orientation.\n", tilesPerScreen);
            assertTrue(false);
        }
        //Loop through all the tiles to look for the one with the provided folder's name
        for (int currentTile = 1; currentTile <= totalTiles; currentTile++) {
            if (findFolderTitle(driver, currentTile, totalTiles).equals(folderName)) {
                tapOnTile(driver, currentTile, totalTiles);
                break;
            }
            //If we are on the last tile and we could not find a match, then we notify the user and break.
            if (currentTile == totalTiles) {
                System.out.println("Sorry, we can't find a folder with the provided name among the thumbnails on the first screen!  Nothing is done.");
                assertTrue(false);
            }
        }
    }
    // Method to select a folder providing the tile (thumbnail) order and the number of total tiles.  This is more powerful than the selectFolderAt methods.
    public static void selectFolder(WebDriver driver, int tileOrder, int totalTiles) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        longTapOnTile(driver, tileOrder, totalTiles);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("1 selected")));
    }
	// Method to select a folder providing the folder's name and the number of total tiles.  This only works for folders that appear on the first page of the View
    public static void selectFolder(WebDriver driver, String folderName, int totalTiles) {
        //Determine the total number of tiles on the first screen of the View.  If totalTiles is more than that, then we can't delete the folder
        int columnsPerScreen = (int) Math.round(calcDisplayWidth(driver) / (double) calcTileHeight(driver));
        int tilesPerScreen = columnsPerScreen * getTilesPerColumn(driver);
        if (totalTiles > tilesPerScreen) {
            System.out.println("Sorry, we can't select the folder based on the number of totalTiles given.");
            System.out.printf("Please fix the number of totalTiles to be equal to or smaller than '%d' for this orientation.\n", tilesPerScreen);
            assertTrue(false);
        }
        //Loop through all the tiles to look for the one with the provided folder's name
        for (int currentTile = 1; currentTile <= totalTiles; currentTile++) {
            if (findFolderTitle(driver, currentTile, totalTiles).equals(folderName)) {
                selectFolder(driver, currentTile, totalTiles);
                break;
            }
            //If we are on the last tile and we could not find a match, then we notify the user and break.
            if (currentTile == totalTiles) {
                System.out.println("Sorry, we can't find a folder with the provided name among the thumbnails on the first screen!  Nothing is selected.");
                break;
            }
        }
    }
    // Method to delete a folder providing the tile (thumbnail) order and the number of total tiles.  This is more powerful than the deleteFolderAt methods.
    public static void deleteFolder(WebDriver driver, int tileOrder, int totalTiles) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        selectFolder(driver, tileOrder, totalTiles);
        //Tap on the trash icon
        driver.findElement(By.name("Delete")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Cancel")));
        //Tap on 'Delete' to confirm deletion
        driver.findElement(By.name("Delete")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Switch to camera")));
		wait(driver, 400);
    }
    // Method to delete a folder providing the folder's name and the number of total tiles.  This only works for folders that appear on the first page of the View
    public static void deleteFolder(WebDriver driver, String folderName, int totalTiles) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        selectFolder(driver, folderName, totalTiles);
        //Tap on the trash icon
        driver.findElement(By.name("Delete")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Cancel")));
        //Tap on 'Delete' to confirm deletion
        driver.findElement(By.name("Delete")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Switch to camera")));
		wait(driver, 400);
    }
    //Method to extract the test device's orientation.  It will return 1 for 'portrait' and 2 for 'landscape'. 
    public static int getOrientation(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        int orientation = 1; 
        
        List<WebElement> frames = driver.findElements(By.tagName("FrameLayout"));
        int frameHeight = frames.get(0).getSize().height;
        int frameWidth = frames.get(0).getSize().width;
        //Determine the device's orientation:
        if (frameWidth > frameHeight) {
            orientation = 2;
        }
        return orientation;
    }
    //Calculate Gallery's displayed height.  The display is the area minus the notification bar and actionbar.
    public static int calcDisplayHeight(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 15);

        List<WebElement> frames = driver.findElements(By.tagName("FrameLayout"));
        int frameHeight = frames.get(0).getSize().height;
        //Determine the Gallery's display height
        WebElement actionbar = driver.findElement(By.id("android:id/action_bar_container"));
        Point p = ((Locatable)actionbar).getCoordinates().onPage();
        int galleryDisplayHeight = (frameHeight - p.y - actionbar.getSize().height);
        return galleryDisplayHeight;
    }
    //Calculate Gallery's display width, which is the same as the device's width
    public static int calcDisplayWidth(WebDriver driver) {
        List<WebElement> frames = driver.findElements(By.tagName("FrameLayout"));
        int frameWidth = frames.get(0).getSize().width;
        return frameWidth;
    }
    //Retrieve the height of the first Frame Layout.  This is used to divide the screen into 10x10 sections to tap
    public static int getFrameHeight(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 15);

        List<WebElement> frames = driver.findElements(By.tagName("FrameLayout"));
        int frameHeight = frames.get(0).getSize().height;
        return frameHeight;
    }
    //Retrieve the width of the first Frame Layout.  This is used to divide the screen into 10x10 sections to tap
    public static int getFrameWidth(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        List<WebElement> frames = driver.findElements(By.tagName("FrameLayout"));
        int frameWidth = frames.get(0).getSize().width;
        return frameWidth;
    }
    //Calculate Gallery thumbnail's height and width.  This is approximate.
    public static int calcTileHeight(WebDriver driver) {
        int orientation = getOrientation(driver);
        int tilesPerColumn = 4;
        //Determine the device's orientation:
        if (orientation == 2) {
            tilesPerColumn = 2;
        }
        int tileHeight = calcDisplayHeight(driver) / tilesPerColumn;
        return tileHeight;
    }
    //Returns the number of tiles per column.  This number is assigned based on RHB device.  When we have other devices, we need to revise this method.
    public static int getTilesPerColumn(WebDriver driver) {
        int orientation = getOrientation(driver);
        int tilesPerColumn = 4;
        //Determine the device's orientation:
        if (orientation == 2) {
            tilesPerColumn = 2;
        }
        return tilesPerColumn;
    }
    // Method to keyword tag a folder provided with the tile (thumbnail) order, the total number of tiles, and the tag term. 
    public static void kwTagFolder(WebDriver driver, int tileOrder, int totalTiles, String tagTerm) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        selectFolder(driver, tileOrder, totalTiles);
        //Pick the 'Add a keyword' selection from the Extras Menu 
        selectMenuChoice(driver, "More options", "Add a keyword");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Enter new keyword")));
        //Find and type the tag term into the Edit Text view
        driver.findElement(By.name("Enter new keyword")).sendKeys(tagTerm);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(tagTerm)));
        //Enter the text by tapping on the Done button on the soft keyboard using coordinates.
        tapDone(driver, "saltbay", 300);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Search")));
    }
    // Method to keyword tag a folder providing the folder's name and the number of total tiles.  This only works for folders that appear on the first page of the View
    public static void kwTagFolder(WebDriver driver, String folderName, int totalTiles, String tagTerm) {
        //Determine the total number of tiles on the first screen of the View.  If totalTiles is more than that, then we can't id and tag the folder
        int columnsPerScreen = (int) Math.round(calcDisplayWidth(driver) / (double) calcTileHeight(driver));
        int tilesPerScreen = columnsPerScreen * getTilesPerColumn(driver);
        if (totalTiles > tilesPerScreen) {
            System.out.println("Sorry, we can't tag the folder based on the number of totalTiles given.");
            System.out.printf("Please fix the number of totalTiles to be equal to or smaller than '%d' for this orientation.\n", tilesPerScreen);
            assertTrue(false);
        }
        //Loop through all the tiles to look for the one with the provided folder's name
        for (int currentTile = 1; currentTile <= totalTiles; currentTile++) {
            if (findFolderTitle(driver, currentTile, totalTiles).equals(folderName)) {
                kwTagFolder(driver, currentTile, totalTiles, tagTerm);
                break;
            }
            //If we are on the last tile and we could not find a match, then we notify the user and break.
            if (currentTile == totalTiles) {
                System.out.println("Sorry, we can't find a folder with the provided name among the thumbnails on the first screen!  Nothing is tagged.");
                break;
            }
        }
    }
    // Method to geo-tag a folder provided with the tile (thumbnail) order, the total number of tiles, and the tag term. 
    public static void geoTagFolder(WebDriver driver, int tileOrder, int totalTiles, String tagTerm) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        selectFolder(driver, tileOrder, totalTiles);
        //Pick the 'Edit Place' selection from the Extras Menu 
        selectMenuChoice(driver, "More options", "Edit Place");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Enter new venue")));
        //Find and type the location into the Edit Text view
        driver.findElement(By.name("Enter new venue")).click();
        wait(driver, 500);
        driver.findElement(By.name("Enter new venue")).sendKeys(tagTerm);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(tagTerm)));
        //Enter the text by tapping on the Done button on the soft keyboard using coordinates.
        tapDone(driver, "saltbay", 300);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Search")));
    }
    // Method to geo-tag a folder providing the folder's name and the number of total tiles.  This only works for folders that appear on the first page of the View
    public static void geoTagFolder(WebDriver driver, String folderName, int totalTiles, String tagTerm) {
        //Determine the total number of tiles on the first screen of the View.  If totalTiles is more than that, then we can't id and tag the folder
        int columnsPerScreen = (int) Math.round(calcDisplayWidth(driver) / (double) calcTileHeight(driver));
        int tilesPerScreen = columnsPerScreen * getTilesPerColumn(driver);
        if (totalTiles > tilesPerScreen) {
            System.out.println("Sorry, we can't tag the folder based on the number of totalTiles given.");
            System.out.printf("Please fix the number of totalTiles to be equal to or smaller than '%d' for this orientation.\n", tilesPerScreen);
            assertTrue(false);
        }
        //Loop through all the tiles to look for the one with the provided folder's name
        for (int currentTile = 1; currentTile <= totalTiles; currentTile++) {
            if (findFolderTitle(driver, currentTile, totalTiles).equals(folderName)) {
                geoTagFolder(driver, currentTile, totalTiles, tagTerm);
                break;
            }
            //If we are on the last tile and we could not find a match, then we notify the user and break.
            if (currentTile == totalTiles) {
                System.out.println("Sorry, we can't find a folder with the provided name among the thumbnails on the first screen!  Nothing is tagged.");
                assertTrue(false);
            }
        }
    }
    // Method to event-tag a folder provided with the tile (thumbnail) order, the total number of tiles, and the tag term. 
    public static void eventTagFolder(WebDriver driver, int tileOrder, int totalTiles, String tagTerm) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        selectFolder(driver, tileOrder, totalTiles);
        //Pick the 'Edit Event' selection from the Extras Menu 
        selectMenuChoice(driver, "More options", "Edit Event");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Enter new event")));
        //Find and type the location into the Edit Text view
        driver.findElement(By.name("Enter new event")).click();
        wait(driver, 500);
        driver.findElement(By.name("Enter new event")).sendKeys(tagTerm);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(tagTerm)));
        //Enter the text by tapping on the Done button on the soft keyboard using coordinates.
        tapDone(driver, "saltbay", 300);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Search")));
    }
    // Method to event-tag a folder providing the folder's name and the number of total tiles.  This only works for folders that appear on the first page of the View
    public static void eventTagFolder(WebDriver driver, String folderName, int totalTiles, String tagTerm) {
        //Determine the total number of tiles on the first screen of the View.  If totalTiles is more than that, then we can't id and tag the folder
        int columnsPerScreen = (int) Math.round(calcDisplayWidth(driver) / (double) calcTileHeight(driver));
        int tilesPerScreen = columnsPerScreen * getTilesPerColumn(driver);
        if (totalTiles > tilesPerScreen) {
            System.out.println("Sorry, we can't tag the folder based on the number of totalTiles given.");
            System.out.printf("Please fix the number of totalTiles to be equal to or smaller than '%d' for this orientation.\n", tilesPerScreen);
            assertTrue(false);
        }
        //Loop through all the tiles to look for the one with the provided folder's name
        for (int currentTile = 1; currentTile <= totalTiles; currentTile++) {
            if (findFolderTitle(driver, currentTile, totalTiles).equals(folderName)) {
                eventTagFolder(driver, currentTile, totalTiles, tagTerm);
                break;
            }
            //If we are on the last tile and we could not find a match, then we notify the user and break.
            if (currentTile == totalTiles) {
                System.out.println("Sorry, we can't find a folder with the provided name among the thumbnails on the first screen!  Nothing is tagged.");
                assertTrue(false);
            }
        }
    }
    //Need to work on this Method to search for and access the files from the search result
    public static void searchAndOpenResult(WebDriver driver, String tagTerm) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        if (driver.findElement(By.name("Search")).isDisplayed()) {
            driver.findElement(By.name("Search")).click();
        }
        else {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Search")));
            driver.findElement(By.name("Search")).click();
        }
        driver.findElements(By.tagName("EditText")).get(0).sendKeys(tagTerm);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("TextView")));
        WebElement resultTextView = driver.findElement(By.tagName("TextView"));
        String resultText = resultTextView.getText();
        Pattern p = Pattern.compile(tagTerm + " \\(" + "(\\d+)(\\))");
        Matcher m = p.matcher(resultText);
        int numberOfFilesTagged = 0;
        if (m.find()) {
            numberOfFilesTagged = Integer.parseInt(m.group(1));
            System.out.printf("There are %d files tagged with the term '%s'.  Accessing search results...\n", numberOfFilesTagged, tagTerm);
            driver.findElement(By.name(resultText)).click();
        }
        else {
            Dut.sendKeyEvent(driver, 4);
            Dut.sendKeyEvent(driver, 4);
            System.out.printf("There are %d files tagged with the term '%s'.\n", numberOfFilesTagged, tagTerm);
        }
    }
    // Method to find a folder's name given the tile's (thumbnail) order and the number of thumbnails on the view.
    public static String findFolderTitle(WebDriver driver, int tileOrder, int totalTiles) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        WebElement menu = driver.findElement(By.name("More options"));
        selectFolder(driver, tileOrder, totalTiles);
        //Select folder Details from the Extras menu
        selectMenuChoice(driver, "More options", "Details");
        List<WebElement> textViews = driver.findElements(By.tagName("TextView"));
        String titleText = textViews.get(1).getText();
        Pattern titlePattern = Pattern.compile("Title : (.+)");
        //Searching for pattern titlePattern in titleText
        Matcher m = titlePattern.matcher(titleText);
        String albumTitle = "";
        if (m.find()) {
            albumTitle = m.group(1).toString();
            System.out.printf("The folder with the tile order of '%d' is titled '%s'.\n", tileOrder, albumTitle);
        }
        else {
            System.out.println("Could not find the album's title!");
            assertTrue(false);
        }    
        wait(driver, 500);
        driver.findElement(By.name("Close")).click();
        wait(driver, 500);
        return albumTitle;
    }
}

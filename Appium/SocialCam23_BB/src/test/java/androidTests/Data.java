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
import java.util.*;

import static org.junit.Assert.*;

public class Data {
    public static final String PHOTO_DATA_INFO = "When you turn this option on, photo data is attached "
        + "to your photo files. This means the data moves with your photo files when you move them "
        + "out of your phone to your computer. It also means that other apps on the phone may be able "
        + "to see the data as well.\n\n"
        + "When you turn this option off, photo data is not attached to your photo files, but standard "
        + "technical photo data (EXIF) is still kept. If you had previously attached this data to your "
        + "photos and then decide to turn this option off, that data remains with those photos but is "
        + "not added to any new photo you take. Note that the Gallery app always has access to this data. "
        + "For example, if you choose to add an event name to a photo in Gallery, this information is kept "
        + "in a secure area of your phone and Gallery uses it to organize your photos around events. "
        + "If you remove the event name from that photo, the event is then removed from Gallery also.";
    public static final String TURN_ON_BLUETOOTH_PROMPT = "To use Bluetooth services, you must first turn on Bluetooth.\n\nTurn on Bluetooth now?\n\n";

    public static final String REMEMBER_LOCATION = "Remember location? Enable geo location to save the "
        + "location the photo was taken. Other applications will have access to this information.";
}

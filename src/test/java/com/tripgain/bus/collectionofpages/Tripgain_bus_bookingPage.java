package com.tripgain.bus.collectionofpages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class Tripgain_bus_bookingPage {


WebDriver driver;
	

	public Tripgain_bus_bookingPage(WebDriver driver) {

        PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	public void validateReviewYourTripPage(Log Log, ScreenShots ScreenShots)
	{
		WebElement reviewPage=driver.findElement(By.xpath("//*[text()='Review Your Trip']"));
	
		if(reviewPage.isDisplayed())
		{
			 Log.ReportEvent("PASS", "Review Page is dispalyed");
		}
		else
		{
			 Log.ReportEvent("FAIL", "Review Page is dispalyed");
		}
		ScreenShots.takeScreenShot1();
	}
	
	public void validateBookingPage(String[] userEnterData, String[] busDetail, String boarding, String drop, Log Log, ScreenShots ScreenShots) {
	    try {
	        String fromLocation = userEnterData[0];
	        String toLocation = userEnterData[1];
	        String userEnterDate = userEnterData[2];

	        String busOperatorName = busDetail[0];
	        String durations = busDetail[1];
	        String departureTime = busDetail[2];

	        // Actual values from Booking Page
	        String origin = driver.findElement(By.xpath("//*[contains(@class,'tg-bsbk-origin')]")).getText();
	        String destination = driver.findElement(By.xpath("//*[contains(@class,'tg-bsbk-destination ')]")).getText();
	        String travelDate = driver.findElement(By.xpath("//*[contains(@class,'tg-bsbk-traveldate')]")).getText();
	       // String travelDate = driver.findElement(By.xpath("//*[contains(@class,'tg-bsbk-traveldate')]")).getText();
	     // travelDate = "13th Jul, 2025"

	     // Step 1: Remove the comma
	     travelDate = travelDate.replace(",", "").trim();  // "13th Jul 2025"

	     // Step 2: Replace spaces with hyphens
	     travelDate = travelDate.replace(" ", "-");  // "13th-Jul-2025"

	     System.out.println(travelDate);  // prints "13th-Jul-2025"

	        String duration = driver.findElement(By.xpath("//*[contains(@class,'tg-bsbk-duration')]")).getText();
	        String boardingPoint = driver.findElement(By.xpath("//*[contains(@class,'tg-bsbk-boardingpoint')]")).getText().trim();
	        String droppingPoint = driver.findElement(By.xpath("//*[contains(@class,'tg-bsbk-droppingpoint')]")).getText().trim();
	        String boardingTime = driver.findElement(By.xpath("//*[contains(@class,'tg-bsbk-boardingtime')]")).getText();

	        boolean allMatch =
	                origin.equals(fromLocation) &&
	                destination.equals(toLocation) &&
	                travelDate.equals(userEnterDate) &&
	                duration.equals(durations) &&
	                boardingPoint.equals(boarding) &&
	                droppingPoint.equals(drop) &&
	                boardingTime.equals(departureTime);

	        if (allMatch) {
	            String matchedDetails = String.format(
	                "Booking page details matched:\n" +
	                "- From: %s\n" +
	                "- To: %s\n" +
	                "- Travel Date: %s\n" +
	                "- Duration: %s\n" +
	                "- Boarding Point: %s\n" +
	                "- Dropping Point: %s\n" +
	                "- Departure Time: %s",
	                origin, destination, travelDate, duration, boardingPoint, droppingPoint, boardingTime
	            );
	            Log.ReportEvent("PASS", matchedDetails);
	        } else {
	            String mismatchDetails = String.format(
	                "Booking page details do NOT match:\n" +
	                "- From: expected '%s', actual '%s'\n" +
	                "- To: expected '%s', actual '%s'\n" +
	                "- Travel Date: expected '%s', actual '%s'\n" +
	                "- Duration: expected '%s', actual '%s'\n" +
	                "- Boarding Point: expected '%s', actual '%s'\n" +
	                "- Dropping Point: expected '%s', actual '%s'\n" +
	                "- Departure Time: expected '%s', actual '%s'",
	                fromLocation, origin,
	                toLocation, destination,
	                userEnterDate, travelDate,
	                durations, duration,
	                boarding, boardingPoint,
	                drop, droppingPoint,
	                departureTime, boardingTime
	            );
	            Log.ReportEvent("FAIL", mismatchDetails);
	            ScreenShots.takeScreenShot1();
	        }

	       

	    } catch (Exception e) {
	        Log.ReportEvent("ERROR", "Exception while validating booking page: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}

	public void validateBookingPageSeat(List<String> selectedSeat, Log Log, ScreenShots ScreenShots) {
	    try {
	    	System.out.println(selectedSeat.size());
	    	
	        List<WebElement> seatElements = driver.findElements(By.xpath("//*[contains(@class,'tg-bsbk-seats')]/span"));
	        List<String> actualSeats = new ArrayList<>();
	        System.out.println(seatElements.size());
	        for (WebElement seatElement : seatElements) {
	            String fullText = seatElement.getText(); // e.g., "U3 - ₹ 1,418"
	            if (fullText != null) {
	                String seatNumber = fullText.split(",")[0].trim(); // Extract "U3"
	                actualSeats.add(seatNumber);
	            }
	        }

	        // Sort both lists to ignore order mismatch
	        List<String> sortedExpected = new ArrayList<>(selectedSeat);
	        List<String> sortedActual = new ArrayList<>(actualSeats);
	        Collections.sort(sortedExpected);
	        System.out.println(sortedExpected);
	        Collections.sort(sortedActual);
	        System.out.println(sortedActual);

	        if (sortedExpected.equals(sortedActual)) {
	            Log.ReportEvent("PASS", "Selected seats match.\nExpected: " + sortedExpected + "\nActual: " + sortedActual);
	        } else {
	            Log.ReportEvent("FAIL", "Selected seats do NOT match.\nExpected: " + sortedExpected + "\nActual: " + sortedActual);
	        }

	        ScreenShots.takeScreenShot1();

	    } catch (Exception e) {
	        Log.ReportEvent("ERROR", "Exception while validating seats: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}


	public void validatePriceInBookingPage(String price, Log Log, ScreenShots ScreenShots) {
	    try {
	        // Get actual price text from the page and trim it
	        String priceText = driver.findElement(By.xpath("//*[contains(@class,'tg-bsbk-totalprice')]")).getText().trim();
	        price = price.trim();  // Trim expected price

	        if (priceText.equals(price)) {
	            Log.ReportEvent("PASS", "Price is matching: " + priceText);
	        } else {
	            Log.ReportEvent("FAIL", "Price is not matching: Expected '" + price + "', Actual '" + priceText + "'");
	            ScreenShots.takeScreenShot1();
	        }

	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "Exception occurred while validating price: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	    }
	}

	

}

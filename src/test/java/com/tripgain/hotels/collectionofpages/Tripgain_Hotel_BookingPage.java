package com.tripgain.hotels.collectionofpages;

import java.awt.AWTException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import com.tripgain.common.TestExecutionNotifier;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class Tripgain_Hotel_BookingPage {

	WebDriver driver;


	public Tripgain_Hotel_BookingPage(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	/*
	public void validateBookingPage(String userEnteredData[],Map<String, Object> hotelDetail,Map<String, Object> hotelCardDetails,Log Log, ScreenShots ScreenShots)
	{
		String checkInDates =userEnteredData[0];
		String checkOutDates =userEnteredData[1];

		 String expectedHotelName = (String) hotelDetail.get("hotelName");
	        String expectedHotelAddress = (String) hotelDetail.get("hotelAddress");
	        int expectedStarRating = (int) hotelDetail.get("starRatingCount");
	        String expectedPolicy = (String) hotelDetail.get("policy");
	        String expectedPrice = (String) hotelDetail.get("actualPrice");

	     String hotelFareType=(String)hotelCardDetails.get("hotelFareType");
	       String fare = (String) hotelCardDetails.get("fare");
	       String cancellationPolicy = (String) hotelCardDetails.get("cancellationPolicy");
	       String meal= (String) hotelCardDetails.get("meals");

		 Map<String, Object> hotelDetails = new HashMap<>();


		String hotelName=driver.findElement(By.xpath("//*[contains(@class,'tg-hb-hotelname')]")).getText();
		String hotelAddress=driver.findElement(By.xpath("//*[contains(@class,'tg-hb-hoteladdress')]")).getText();
		List<WebElement> starRating=driver.findElements(By.xpath("//*[contains(@class,'tg-hb-hotelstars')]/*[@data-testid='StarIcon']"));
		int starRatings=starRating.size();
		String fares=driver.findElement(By.xpath("//*[contains(@class,'tg-hb-refundable')]")).getText().trim();
		String checkInDate=driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkindate')]")).getText();
		String checkOutDate=driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkoutdate')]")).getText();
		String price=driver.findElement(By.xpath("(//*[contains(@class,'tg-hb-price')])[1]")).getText();
		String meals=driver.findElement(By.xpath("//*[contains(@class,'tg-hb-meals')]")).getText();
		String checkInTime=driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkintime')]")).getText();
		String checkOutTime=driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkouttime')]")).getText();
		String fareType=driver.findElement(By.xpath("//*[contains(@class,'tg-hb-fatetype')]")).getText();
		String roomAndNights=driver.findElement(By.xpath("//*[contains(@class,'tg-hb-numroomsnights')]")).getText();
		String policy =driver.findElement(By.xpath("//*[contains(@class,'tg-policy')]")).getText().trim();

		if(checkInDate.equals(checkInDates) && checkOutDate.equals(checkOutDates) && hotelName.equals(expectedHotelName)
				&& hotelAddress.equals(expectedHotelAddress) && starRatings==expectedStarRating && policy.equals(expectedPolicy)
				&& price.equals(expectedPrice) && hotelFareType.equals(fareType) && fare.equals(fares) && meal.equals(meals)
				)
		{
			 Log.ReportEvent("FAIL", "❌ Element not found: " + e.getMessage());
		        ScreenShots.takeScreenShot1();
		}
	}
	 */
	/*
	public void validateBookingPage(String userEnteredData[], Map<String, Object> hotelDetail,
			Map<String, Object> hotelCardDetails, Log Log, ScreenShots ScreenShots) {
		try {
			String checkInDates = userEnteredData[0];
			String checkOutDates = userEnteredData[1];

			// Expected values from backend/API
			String expectedHotelName = (String) hotelDetail.get("actualHotelName");
			String expectedHotelAddress = (String) hotelDetail.get("actualHotelAddress");
			int expectedStarRating = (int) hotelDetail.get("actualStarRating");
			String expectedPolicy = (String) hotelDetail.get("policy");
			String expectedPrice = (String) hotelDetail.get("actualPrice");

			String hotelFareType = (String) hotelCardDetails.get("hotelFareType");
			String fare = (String) hotelCardDetails.get("fare");
			String cancellationPolicy = (String) hotelCardDetails.get("cancellationPolicy");
			String meal = (String) hotelCardDetails.get("meals");

			// Actual values from UI
			String hotelName = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-hotelname')]")).getText();
			String hotelAddress = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-hoteladdress')]")).getText();
			List<WebElement> starRating = driver.findElements(By.xpath("//*[contains(@class,'tg-hb-hotelstars')]/*[@data-testid='StarIcon']"));
			int starRatings = starRating.size();
			String fares = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-refundable')]")).getText().trim();
			String checkInDate = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkindate')]")).getText();
			String checkOutDate = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkoutdate')]")).getText();
			String price = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-totalnet')]")).getText();
			String meals = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-meals')]")).getText();
			String checkInTime = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkintime')]")).getText();
			String checkOutTime = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkouttime')]")).getText();
			String fareType = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-fatetype')]")).getText();
			String roomAndNights = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-numroomsnights')]")).getText();
			String policy = driver.findElement(By.xpath("//*[contains(@class,'tg-policy')]")).getText().trim();

			// Print all values for debugging
			System.out.println("======== DEBUG LOG ========");
			System.out.println("Expected Hotel Name: " + expectedHotelName + " | Actual: " + hotelName);
			System.out.println("Expected Address: " + expectedHotelAddress + " | Actual: " + hotelAddress);
			System.out.println("Expected Stars: " + expectedStarRating + " | Actual: " + starRatings);
			System.out.println("Expected Check-In Date: " + checkInDates + " | Actual: " + checkInDate);
			System.out.println("Expected Check-Out Date: " + checkOutDates + " | Actual: " + checkOutDate);
			System.out.println("Expected Price: " + expectedPrice + " | Actual: " + price);
			System.out.println("Expected Policy: " + expectedPolicy + " | Actual: " + policy);
			System.out.println("Expected Fare Type: " + hotelFareType + " | Actual: " + fareType);
			System.out.println("Expected Fare: " + fare + " | Actual: " + fares);
			System.out.println("Expected Meal: " + meal + " | Actual: " + meals);
			System.out.println("===========================");

			// Validation
			boolean isValid = checkInDate.equals(checkInDates)
					&& checkOutDate.equals(checkOutDates)
					&& hotelName.equals(expectedHotelName)
					&& hotelAddress.equals(expectedHotelAddress)
					&& starRatings == expectedStarRating
					&& policy.equals(expectedPolicy)
					&& price.equals(expectedPrice)
					&& fareType.equals(hotelFareType)
					&& fares.equals(fare)
					&& meals.equals(meal);

			if (!isValid) {
				Log.ReportEvent("FAIL", "❌ Booking Page Validation Failed. Check console for mismatched values.");
				ScreenShots.takeScreenShot1();
			} else {
				Log.ReportEvent("PASS", "✅ Booking Page Validation Passed Successfully.");
			}

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "❌ Exception occurred during validation: " + e.getMessage());
			ScreenShots.takeScreenShot1();
			e.printStackTrace();
		}
	}
	 */
	/*
	public void validateBookingPage(String userEnteredData[], Map<String, Object> hotelDetail,
			Map<String, Object> hotelCardDetails, Log Log, ScreenShots ScreenShots) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, 0);");
 Thread.sleep(3000);

			String checkInDates = userEnteredData[0];   // e.g. "24th-Jul-2025"
			String checkOutDates = userEnteredData[1];  // e.g. "27th-Jul-2025"

			// Expected values from backend/API
			String expectedHotelName = ((String) hotelDetail.get("actualHotelName")).trim();
			String expectedHotelAddress = ((String) hotelDetail.get("actualHotelAddress")).trim();
			int expectedStarRating = (int) hotelDetail.get("actualStarRating");
			String expectedPolicy = ((String) hotelDetail.get("policy")).trim();
			String expectedPrice = ((String) hotelDetail.get("actualPrice")).trim();

			String hotelFareType = ((String) hotelCardDetails.get("hotelFareType")).trim();
			String fare = ((String) hotelCardDetails.get("fare")).trim();
			String meal = ((String) hotelCardDetails.get("meals")).trim();

			// Wait helper (5 seconds max)
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));

			// Actual values from UI - wait for hotel name element to be visible
			WebElement hotelNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'tg-hb-hotelname')]")));
			String hotelName = hotelNameElement.getText().trim();

			String hotelAddress = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-hoteladdress')]")).getText().trim();

			int starRatings = driver.findElements(By.xpath("//*[contains(@class,'tg-hb-hotelstars')]/*[@data-testid='StarIcon']")).size();

			String fares = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-refundable')]")).getText().trim();
			String checkInDate = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkindate')]")).getText().trim();
			String checkOutDate = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkoutdate')]")).getText().trim();
			String price = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-totalnet')]")).getText().trim();
			String meals = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-meals')]")).getText().trim();
			String fareType = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-fatetype')]")).getText().trim();
			String policy = driver.findElement(By.xpath("//*[contains(@class,'tg-policy')]")).getText().trim();

			// --- Normalize dates ---
			// Remove ordinal suffixes from date strings ("st", "nd", "rd", "th") and parse to LocalDate
			LocalDate expectedCheckInDate = null;
			LocalDate expectedCheckOutDate = null;
			LocalDate actualCheckInDate = null;
			LocalDate actualCheckOutDate = null;

			try {
				expectedCheckInDate = LocalDate.parse(checkInDates.replaceAll("(st|nd|rd|th)", ""), DateTimeFormatter.ofPattern("d-MMM-yyyy"));
				expectedCheckOutDate = LocalDate.parse(checkOutDates.replaceAll("(st|nd|rd|th)", ""), DateTimeFormatter.ofPattern("d-MMM-yyyy"));

				actualCheckInDate = LocalDate.parse(checkInDate.replaceAll("(st|nd|rd|th)", "").replace(",", ""), DateTimeFormatter.ofPattern("d MMM yyyy"));
				actualCheckOutDate = LocalDate.parse(checkOutDate.replaceAll("(st|nd|rd|th)", "").replace(",", ""), DateTimeFormatter.ofPattern("d MMM yyyy"));
			} catch (DateTimeParseException dtpe) {
				System.out.println("Date parsing failed: " + dtpe.getMessage());
			}

			// --- Normalize price ---
			double expectedPriceVal = Double.parseDouble(expectedPrice.replaceAll("[^0-9.]", ""));
			double actualPriceVal = Double.parseDouble(price.replaceAll("[^0-9.]", ""));

			// --- Normalize fare strings by removing spaces and lowercasing ---
			String normalizedExpectedFare = fare.replaceAll("\\s+", "").toLowerCase();
			String normalizedActualFare = fares.replaceAll("\\s+", "").toLowerCase();

			// --- Debug print ---
			System.out.println("======== DEBUG LOG ========");
			System.out.println("Expected Hotel Name: '" + expectedHotelName + "' | Actual: '" + hotelName + "'");
			System.out.println("Expected Address: '" + expectedHotelAddress + "' | Actual: '" + hotelAddress + "'");
			System.out.println("Expected Stars: " + expectedStarRating + " | Actual: " + starRatings);
			System.out.println("Expected Check-In Date: " + expectedCheckInDate + " | Actual: " + actualCheckInDate);
			System.out.println("Expected Check-Out Date: " + expectedCheckOutDate + " | Actual: " + actualCheckOutDate);
			System.out.println("Expected Price: " + expectedPriceVal + " | Actual: " + actualPriceVal);
			System.out.println("Expected Policy: '" + expectedPolicy + "' | Actual: '" + policy + "'");
			System.out.println("Expected Fare Type: '" + hotelFareType + "' | Actual: '" + fareType + "'");
			System.out.println("Expected Fare: '" + normalizedExpectedFare + "' | Actual: '" + normalizedActualFare + "'");
			System.out.println("Expected Meal: '" + meal + "' | Actual: '" + meals + "'");
			System.out.println("===========================");

			// --- Validation ---
			boolean isValid =
					hotelName.equalsIgnoreCase(expectedHotelName) &&
					hotelAddress.equalsIgnoreCase(expectedHotelAddress) &&
					starRatings == expectedStarRating &&
					expectedCheckInDate != null && expectedCheckInDate.equals(actualCheckInDate) &&
					expectedCheckOutDate != null && expectedCheckOutDate.equals(actualCheckOutDate) &&
					Math.abs(expectedPriceVal - actualPriceVal) < 0.01 &&
					policy.equalsIgnoreCase(expectedPolicy) &&
					fareType.equalsIgnoreCase(hotelFareType) &&
					normalizedExpectedFare.equals(normalizedActualFare) &&
					meals.equalsIgnoreCase(meal);

			// --- Prepare detailed report string ---
			StringBuilder reportDetails = new StringBuilder();
			reportDetails.append("===== Booking Page Validation Details =====\n");
			reportDetails.append(String.format("Hotel Name       : Expected = '%s', Actual = '%s'%n", expectedHotelName, hotelName));
			reportDetails.append(String.format("Hotel Address    : Expected = '%s', Actual = '%s'%n", expectedHotelAddress, hotelAddress));
			reportDetails.append(String.format("Star Rating      : Expected = %d, Actual = %d%n", expectedStarRating, starRatings));
			reportDetails.append(String.format("Check-In Date    : Expected = %s, Actual = %s%n", expectedCheckInDate, actualCheckInDate));
			reportDetails.append(String.format("Check-Out Date   : Expected = %s, Actual = %s%n", expectedCheckOutDate, actualCheckOutDate));
			reportDetails.append(String.format("Price            : Expected = %s, Actual = %s%n", expectedPrice, price));
			reportDetails.append(String.format("Policy           : Expected = '%s', Actual = '%s'%n", expectedPolicy, policy));
			reportDetails.append(String.format("Fare Type        : Expected = '%s', Actual = '%s'%n", hotelFareType, fareType));
			reportDetails.append(String.format("Fare             : Expected = '%s', Actual = '%s'%n", fare, fares));
			reportDetails.append(String.format("Meal             : Expected = '%s', Actual = '%s'%n", meal, meals));
			reportDetails.append("============================================");

			if (!isValid) {
				Log.ReportEvent("FAIL", reportDetails.toString());
				ScreenShots.takeScreenShot1();
			} else {
				Log.ReportEvent("PASS", reportDetails.toString());
			}

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "❌ Exception occurred during validation: " + e.getMessage());
			ScreenShots.takeScreenShot1();
			e.printStackTrace();
		}
	}
	 */
	
	//Method to validate Booking Page of user search results
	public void validateBookingPage(String[] userEnteredData, Map<String, Object> hotelDetail,
			Map<String, Object> hotelCardDetails, Log Log, ScreenShots ScreenShots) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(3000);

			String checkInDates = userEnteredData[0];   // e.g. "24th-Jul-2025"
			String checkOutDates = userEnteredData[1];  // e.g. "27th-Jul-2025"

			// Expected values from backend/API
			String expectedHotelName = ((String) hotelDetail.get("actualHotelName")).trim();
			String expectedHotelAddress = ((String) hotelDetail.get("actualHotelAddress")).trim();
			int expectedStarRating = (int) hotelDetail.get("actualStarRating");
			String expectedPolicy = ((String) hotelDetail.get("policy")).trim();
			String expectedPrice = ((String) hotelDetail.get("actualPrice")).trim();

			String hotelFareType = ((String) hotelCardDetails.get("hotelFareType")).trim();
			String fare = ((String) hotelCardDetails.get("fare")).trim();
			String meal = ((String) hotelCardDetails.get("meals")).trim();

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

			// Actual values from UI
			WebElement hotelNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'tg-hb-hotelname')]")));
			String hotelName = hotelNameElement.getText().trim();

			String hotelAddress = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-hoteladdress')]")).getText().trim();
			int starRatings = driver.findElements(By.xpath("//*[contains(@class,'tg-hb-hotelstars')]/*[@data-testid='StarIcon']")).size();
			String fares = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-refundable')]")).getText().trim();
			String checkInDate = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkindate')]")).getText().trim();
			String checkOutDate = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-checkoutdate')]")).getText().trim();
			String price = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-totalnet')]")).getText().trim();
			String meals = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-meals')]")).getText().trim();
			String fareType = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-fatetype')]")).getText().trim();
			String policy = driver.findElement(By.xpath("//*[contains(@class,'tg-policy')]")).getText().trim();

			// Normalize dates
			LocalDate expectedCheckInDate = parseDate(checkInDates, "d-MMM-yyyy");
			LocalDate expectedCheckOutDate = parseDate(checkOutDates, "d-MMM-yyyy");
			LocalDate actualCheckInDate = parseDate(checkInDate.replace(",", ""), "d MMM yyyy");
			LocalDate actualCheckOutDate = parseDate(checkOutDate.replace(",", ""), "d MMM yyyy");

			// Normalize price
			long expectedPriceVal = Math.round(Double.parseDouble(expectedPrice.replaceAll("[^0-9.]", "")));
			long actualPriceVal = Math.round(Double.parseDouble(price.replaceAll("[^0-9.]", "")));

			// Normalize fare strings
			String normalizedExpectedFare = fare.replaceAll("\\s+", "").toLowerCase();
			String normalizedActualFare = fares.replaceAll("\\s+", "").toLowerCase();

			// Prepare validation report
			StringBuilder reportDetails = new StringBuilder();
			reportDetails.append("===== Booking Page Validation Details =====\n");
			reportDetails.append(String.format("Hotel Name       : Expected = '%s', Actual = '%s'%n", expectedHotelName, hotelName));
			reportDetails.append(String.format("Hotel Address    : Expected = '%s', Actual = '%s'%n", expectedHotelAddress, hotelAddress));
			reportDetails.append(String.format("Star Rating      : Expected = %d, Actual = %d%n", expectedStarRating, starRatings));
			reportDetails.append(String.format("Check-In Date    : Expected = %s, Actual = %s%n", expectedCheckInDate, actualCheckInDate));
			reportDetails.append(String.format("Check-Out Date   : Expected = %s, Actual = %s%n", expectedCheckOutDate, actualCheckOutDate));
			reportDetails.append(String.format("Price            : Expected = ₹%d, Actual = ₹%d%n", expectedPriceVal, actualPriceVal));
			reportDetails.append(String.format("Policy           : Expected = '%s', Actual = '%s'%n", expectedPolicy, policy));
			reportDetails.append(String.format("Fare Type        : Expected = '%s', Actual = '%s'%n", hotelFareType, fareType));
			reportDetails.append(String.format("Fare             : Expected = '%s', Actual = '%s'%n", fare, fares));
			reportDetails.append(String.format("Meal             : Expected = '%s', Actual = '%s'%n", meal, meals));
			reportDetails.append("============================================");

			// Field-wise validation
			boolean isValid = true;

			if (!hotelName.equalsIgnoreCase(expectedHotelName)) {
				isValid = false;
				Log.ReportEvent("WARN", "❌ Hotel name mismatch.");
			}
			if (!hotelAddress.equalsIgnoreCase(expectedHotelAddress)) {
				isValid = false;
				Log.ReportEvent("WARN", "❌ Hotel address mismatch.");
			}
			if (starRatings != expectedStarRating) {
				isValid = false;
				Log.ReportEvent("WARN", "❌ Star rating mismatch.");
			}
			if (!expectedCheckInDate.equals(actualCheckInDate)) {
				isValid = false;
				Log.ReportEvent("WARN", "❌ Check-in date mismatch.");
			}
			if (!expectedCheckOutDate.equals(actualCheckOutDate)) {
				isValid = false;
				Log.ReportEvent("WARN", "❌ Check-out date mismatch.");
			}
			if (expectedPriceVal != actualPriceVal) {
				isValid = false;
				Log.ReportEvent("WARN", "❌ Price mismatch.");
			}
			if (!policy.equalsIgnoreCase(expectedPolicy)) {
				isValid = false;
				Log.ReportEvent("WARN", "❌ Policy mismatch.");
			}
			if (!fareType.equalsIgnoreCase(hotelFareType)) {
				isValid = false;
				Log.ReportEvent("WARN", "❌ Fare type mismatch.");
			}
			if (!normalizedExpectedFare.equals(normalizedActualFare)) {
				isValid = false;
				Log.ReportEvent("WARN", "❌ Fare description mismatch (possible spacing issue).");
			}
			if (!meal.equalsIgnoreCase(meals)) {
				isValid = false;
				Log.ReportEvent("WARN", "❌ Meal info mismatch.");
			}

			// Final report
			if (isValid) {
				Log.ReportEvent("PASS", reportDetails.toString());
			} else {
				Log.ReportEvent("FAIL", reportDetails.toString());
				ScreenShots.takeScreenShot1();
			}

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "❌ Exception occurred during validation: " + e.getMessage());
			ScreenShots.takeScreenShot1();
			e.printStackTrace();
		}
	}

	//Helper method to parse date
	private LocalDate parseDate(String inputDate, String format) {
		try {
			return LocalDate.parse(inputDate.replaceAll("(st|nd|rd|th)", ""), DateTimeFormatter.ofPattern(format, Locale.ENGLISH));
		} catch (DateTimeParseException e) {
			return null;
		}
	}
	
	//Method to validate currency In Booking Page
	public void currencyInBookingPage(String expectedCurrency, Log Log, ScreenShots ScreenShots) {
	    try {
	        String actualCurrency = driver.findElement(By.xpath("//*[contains(@class,'tg-hb-totalothercurrency')]")).getText().trim();

	        if (actualCurrency.startsWith(expectedCurrency)) {
	            Log.ReportEvent("PASS", "✅ Currency on booking page matches expected: " + expectedCurrency);
	        } else {
	            Log.ReportEvent("FAIL", "❌ Currency mismatch. Expected: '" + expectedCurrency + "', but found: '" + actualCurrency + "'");
	            ScreenShots.takeScreenShot1();
	        }

	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "❌ Exception occurred during currency validation: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}


}

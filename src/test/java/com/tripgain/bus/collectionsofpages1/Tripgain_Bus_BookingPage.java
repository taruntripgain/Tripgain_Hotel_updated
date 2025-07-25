package com.tripgain.bus.collectionsofpages1;

import static org.testng.Assert.fail;

import java.awt.AWTException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import com.tripgain.common.TestExecutionNotifier;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class Tripgain_Bus_BookingPage {

	WebDriver driver;


	public Tripgain_Bus_BookingPage(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver=driver;
	}

	//Method to get all the seat in booking page
	public List<String> fetchSeatInBookingPage(Log Log, ScreenShots ScreenShots) {
		List<String> bookingPageSeatList = new ArrayList<>();

		try {
			List<WebElement> seatElements = driver.findElements(By.xpath("//*[contains(normalize-space(@class), 'tg-bsbk-seats')]//span"));

			if (seatElements.isEmpty()) {
				Log.ReportEvent("INFO", "No seats found on booking page.");
			}

			for (WebElement seat : seatElements) {
				String seatText = seat.getText().replace(",", "").trim();
				if (!seatText.isEmpty()) {
					bookingPageSeatList.add(seatText);
				}
			}

			Log.ReportEvent("PASS", "Selected seats found on booking page are: " + bookingPageSeatList);
		} catch (NoSuchElementException | WebDriverException e) {
			Log.ReportEvent("FAIL", "Error fetching seats: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Failed to fetch seat information due to: " + e.getMessage());
		}

		return bookingPageSeatList;
	}



	//Method to get the text of data in booking page
	public String[] getTextInBookingPage() {
		try {
			String boardingCity = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsbk-origin')]")).getText();
			System.out.println("Boarding City: " + boardingCity);

			String dropingCity = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsbk-destination')]")).getText();
			System.out.println("Dropping City: " + dropingCity);

			String travellingDate = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsbk-traveldate')]")).getText();
			String bookingPageTravellingDateText = travellingDate.replace(" ", "-").replace(",", "");
			System.out.println("Traveling Date: " + bookingPageTravellingDateText);

			String travellingTime = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsbk-duration')]")).getText();
			System.out.println("Traveling Time: " + travellingTime);

			String boardingPointText = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsbk-boardingpoint')]")).getText();
			System.out.println("Boarding Point: " + boardingPointText);

			String dropingPointText = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsbk-droppingpoint')]")).getText();
			System.out.println("Dropping Point: " + dropingPointText);

			String price = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsbk-totalprice')]")).getText();
			System.out.println("Price: " + price);

			String operatorName = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsbk-operator')]")).getText();
			System.out.println("Operator: " + operatorName);

			String boardingTimeInBookingPage = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsbk-boardingtime')]")).getText();
			System.out.println("Boarding Time: " + boardingTimeInBookingPage);

			return new String[] {
					boardingCity,
					dropingCity,
					bookingPageTravellingDateText,
					travellingTime,
					boardingPointText,
					dropingPointText,
					price,
					operatorName,
					boardingTimeInBookingPage
			};

		} catch (NoSuchElementException | WebDriverException e) {
			System.err.println("Error in getTextInBookingPage(): " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Failed to extract booking page details: " + e.getMessage());
			return new String[0];  // Return empty array in case of failure
		}
	}


	//Method to validate result page data with booking page
	public void validateResultAndBookingPageData(
			String[] getTextInResultPage, String[] getTextInBookingPage,
			List<String> getSeatNames, List<String> fetchSeatInBookingPage,
			Log Log, ScreenShots ScreenShots) {

		try {
			// Sort and compare seats
			List<String> sortedExpected = new ArrayList<>(getSeatNames);
			List<String> sortedActual = new ArrayList<>(fetchSeatInBookingPage);
			Collections.sort(sortedExpected);
			Collections.sort(sortedActual);

			// Parse result page data
			String boardingPoint = getTextInResultPage[0];
			String dropingPoint = getTextInResultPage[1];
			String Price = getTextInResultPage[2];
			String boardingTime = getTextInResultPage[3];
			String arrivalTime = getTextInResultPage[4];
			String journeyDuration = getTextInResultPage[5];
			String operatorNameInResultPage = getTextInResultPage[6];
			String noOfSeatsAvailable = getTextInResultPage[7];

			// Parse booking page data
			String boardingCity = getTextInBookingPage[0];
			String dropingCity = getTextInBookingPage[1];
			String travellingDate = getTextInBookingPage[2];
			String travellingTime = getTextInBookingPage[3];
			String boardingPointText = getTextInBookingPage[4];
			String dropingPointText = getTextInBookingPage[5];
			String price = getTextInBookingPage[6];
			String operatorName = getTextInBookingPage[7];
			String boardingTimeInBookingPage = getTextInBookingPage[8];

			// Compare booking vs result page
			if (boardingPoint.equals(boardingPointText)
					&& dropingPoint.equals(dropingPointText)
					&& Price.equals(price)
					&& journeyDuration.equals(travellingTime)
					&& operatorNameInResultPage.equals(operatorName)) {

				String matchedDetails = String.format(
						"Booking page details matched with result page details:\n" +
								"- boardingPoint: %s\n" +
								"- dropingPoint: %s\n" +
								"- Price: %s\n" +
								"- journeyDuration: %s\n" +
								"- operatorName: %s",
								boardingPoint, dropingPoint, Price, journeyDuration, operatorNameInResultPage);

				Log.ReportEvent("PASS", matchedDetails);
				System.out.println(matchedDetails);
			} else {
				String mismatchDetails = String.format(
						"Booking page vs Result page comparison:\n" +
								"- boardingPoint: expected [%s] | actual [%s]\n" +
								"- dropingPoint: expected [%s] | actual [%s]\n" +
								"- Price:         expected [%s] | actual [%s]\n" +
								"- journeyDuration: expected [%s] | actual [%s]\n" +
								"- operatorName: expected [%s] | actual [%s]",
								boardingPointText, boardingPoint,
								dropingPointText, dropingPoint,
								price, Price,
								travellingTime, journeyDuration,
								operatorName, operatorNameInResultPage);

				Log.ReportEvent("FAIL", mismatchDetails);
				System.out.println(mismatchDetails);
			}

			// Seat list comparison
			if (sortedExpected.equals(sortedActual)) {
				Log.ReportEvent("PASS", "Seat List Matched: " + sortedExpected + " with " + sortedActual);
				System.out.println("Seat List Matched: " + sortedExpected + " with " + sortedActual);
			} else {
				Log.ReportEvent("FAIL", "Seat List mismatch!: " + sortedExpected + " with " + sortedActual);
				System.out.println("Seat List mismatch!: " + sortedExpected + " with " + sortedActual);
			}

		} catch (Exception e) {
			String errorMessage = "Exception in validateResultAndBookingPageData: " + e.getMessage();
			Log.ReportEvent("FAIL", errorMessage);
			System.err.println(errorMessage);
			e.printStackTrace();
			Assert.fail(errorMessage);
		}
	}


	//Method to validate user search data with booking page data
	public void validateUserSearchDataAndBookingPageData(String[] userInput, String[] getTextInBookingPage, Log Log, ScreenShots ScreenShots) {
		try {
			// Extract user input
			String boardingCityAsPerUserInput = userInput[0];
			String dropingCityAsPerUserInput = userInput[1];
			String journeyDate = userInput[2];

			// Extract booking page data
			String boardingCity = getTextInBookingPage[0];
			String dropingCity = getTextInBookingPage[1];
			String travellingDate = getTextInBookingPage[2];
			String travellingTime = getTextInBookingPage[3];
			String boardingPointText = getTextInBookingPage[4];
			String dropingPointText = getTextInBookingPage[5];
			String price = getTextInBookingPage[6];
			String operatorName = getTextInBookingPage[7];

			// Log raw values for debugging (optional)
			System.out.println("User Input: " + Arrays.toString(userInput));
			System.out.println("Booking Page: " + Arrays.toString(getTextInBookingPage));

			// Validation
			if (boardingCityAsPerUserInput.equals(boardingCity)
					&& dropingCityAsPerUserInput.equals(dropingCity)
					&& journeyDate.equals(travellingDate)) {

				String matchedDetails = String.format(
						"Booking page details matched with user entered data:\n" +
								"- boardingCity: %s\n" +
								"- dropingCity: %s\n" +
								"- travellingDate: %s",
								boardingCity, dropingCity, travellingDate
						);

				Log.ReportEvent("PASS", matchedDetails);

			} else {
				String mismatchDetails = String.format(
						"Booking page vs user data mismatch:\n" +
								"- boardingCity: expected [%s] | actual [%s]\n" +
								"- dropingCity: expected [%s] | actual [%s]\n" +
								"- travellingDate: expected [%s] | actual [%s]",
								boardingCityAsPerUserInput, boardingCity,
								dropingCityAsPerUserInput, dropingCity,
								journeyDate, travellingDate
						);

				Log.ReportEvent("FAIL", mismatchDetails);
				System.out.println(mismatchDetails);
			}

		} catch (Exception e) {
			String errorMessage = "Exception in validateUserSearchDataAndBookingPageData: " + e.getMessage();
			Log.ReportEvent("FAIL", errorMessage);
			System.err.println(errorMessage);
			e.printStackTrace();
			Assert.fail(errorMessage);
		}
	}
	//Method to check whether review your trip page is displayed
	public void validateReviewYourTripPage(Log Log, ScreenShots ScreenShots) {
		try {
			Thread.sleep(2000);
			WebElement reviewPage = driver.findElement(By.xpath("//*[text()='Review Your Trip']"));

			if (reviewPage.isDisplayed()) {
				Log.ReportEvent("PASS", "Review Page is displayed");
			} else {
				Log.ReportEvent("FAIL", "Review Page is not displayed");
			}
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception occurred while validating Review Your Trip page: " + e.getMessage());

		}
	}

	//Method to get and validate price in booking page
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




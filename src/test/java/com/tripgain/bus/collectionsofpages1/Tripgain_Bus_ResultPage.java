package com.tripgain.bus.collectionsofpages1;

import static org.testng.Assert.fail;

import java.awt.AWTException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

public class Tripgain_Bus_ResultPage {

	WebDriver driver;


	public Tripgain_Bus_ResultPage(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	//Method to get all operators name on the screen.
	public void getOperatorsName(Log Log, ScreenShots ScreenShots) {
		List<String> names = new ArrayList<>();

		try {
			List<WebElement> listOfOperators = driver.findElements(
					By.xpath("//h6[text()='OPERATOR NAME']/parent::div//div//span")
					);
			for (WebElement operator : listOfOperators) {
				String operatorName = operator.getText();
				names.add(operatorName);
				Log.ReportEvent("INFO", "Operator Name Found: " + operatorName);  

			}
			Log.ReportEvent("PASS", "Operator name found on the screen.");
			System.out.println("List Of Operator Names Found On the Screen After Search: " + names);

		} catch (NoSuchElementException nse) {
			System.err.println("No operator name elements found on the page: " + nse.getMessage());
		} catch (WebDriverException wde) {
			System.err.println("WebDriver encountered an issue while retrieving operator names: " + wde.getMessage());
		} catch (Exception e) {
			System.err.println("Unexpected error in getOperatorsName(): " + e.getMessage());
			Log.ReportEvent("FAIL", "Operator name not found on the screen.");
			e.printStackTrace();
			ScreenShots.takeScreenShot();
			Assert.fail();

		} 

	}
	//Method to click on view seat button
	public void clickOnViewSeatButton(Log Log, ScreenShots ScreenShots,int viewBus)
	{
		try
		{
			driver.findElement(By.xpath("(//button[text()='View Seats'])["+ viewBus +"]")).click();
			Log.ReportEvent("PASS", "Clicked on view seat button successfully.");

		}

		catch (Exception e) {
			System.err.println("Unexpected error in clickOnViewSeatButton(): " + e.getMessage());
			e.printStackTrace();
			Log.ReportEvent("FAIL", "Failed to click on view seat button.");
			ScreenShots.takeScreenShot();

			Assert.fail();

		}
	}
	//Method to click on boarding point dropdown
	public void clickOnBoardingPoint(int boardingPointIndex,Log Log, ScreenShots ScreenShots) throws InterruptedException
	{
		try
		{
			Thread.sleep(2000);
			driver.findElement(By.xpath("(//div[@aria-haspopup='listbox'])[1]")).click();
			List<WebElement> listOfBoardingPoints = driver.findElements(By.xpath("//ul//div//span"));
			WebElement indexWiseClickOnBoardingPoint = listOfBoardingPoints.get(boardingPointIndex);
			Thread.sleep(3000);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", indexWiseClickOnBoardingPoint);

			String boardingPointText = driver.findElement(By.xpath("(//*[contains(normalize-space(@class), 'tg-bsrs-boardingpoint')]//span)[1]")).getText();
			Log.ReportEvent("PASS", "boarding point selected successfully. " );
			Log.ReportEvent("INFO", "Selected boarding point: "+ boardingPointText);

		}
		catch (Exception e) {
			System.err.println("Unexpected error when clicking on boarding point dropdown: " + e.getMessage());
			e.printStackTrace();	
			Log.ReportEvent("FAIL", "Failed to select the boarding point." );
			ScreenShots.takeScreenShot();
			Assert.fail();
		}

	}
	//Method to click on droping point dropdown
	public void clickOnDropingPoint(int dropingPointIndex,Log Log, ScreenShots ScreenShots) throws InterruptedException
	{
		try
		{
			Thread.sleep(2000);
			driver.findElement(By.xpath("(//div[@aria-haspopup='listbox'])[2]")).click();
			List<WebElement> listOfBoardingPoints = driver.findElements(By.xpath("//ul//div//span"));
			WebElement indexWiseClickOndropingPoint = listOfBoardingPoints.get(dropingPointIndex);
			Thread.sleep(3000);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", indexWiseClickOndropingPoint);

			String dropingPointText = driver.findElement(By.xpath("(//*[contains(normalize-space(@class), 'tg-bsrs-droppingpoint')]//span)[1]")).getText();

			Log.ReportEvent("PASS", "Droping point selected successfully. " );
			Log.ReportEvent("INFO", "Selected Droping point: "+ dropingPointText);
		}
		catch (Exception e) {
			System.err.println("Unexpected error when clicking on droping point dropdown: " + e.getMessage());
			e.printStackTrace();	
			Log.ReportEvent("FAIL", "Failed to select the droping point." );
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
	}
	//Method to pick the seat
	public void pickSeat(int seatCount,Log Log, ScreenShots ScreenShots)
	{
		try
		{
			List<WebElement> availableSeat = driver.findElements(By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-seatavailable')]"));
			int seat = availableSeat.size();
			if(seat < seatCount)
			{
				System.out.println("User needs "+ seatCount +"No of seats,But in the bus found only "+seat);

			}
			for(int i=0;i<seatCount;i++)
			{
				WebElement clickOnSeat = availableSeat.get(i);
				System.out.println(clickOnSeat);
				clickOnSeat.click();
			}
			Log.ReportEvent("PASS", "Seat selected succesfully " );

		}
		catch (Exception e) {
			System.err.println("Unexpected error when picking the seat: " + e.getMessage());
			e.printStackTrace();	
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
	}


	//Method to get seat names in result page
	public List<String> getSeatNames(Log Log, ScreenShots ScreenShots) {
		List<String> listOfSeat = new ArrayList<>();

		try {
			List<WebElement> seat = driver.findElements(By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-seatandprice')]"));
			for (WebElement seat1 : seat) {
				String seatText = seat1.getText();
				String[] seatTextSplit = seatText.split("-");
				String seatFound = seatTextSplit[0].trim();
				listOfSeat.add(seatFound);
				System.out.println(listOfSeat);

				Log.ReportEvent("INFO", "Selected seats are :"+ seatFound); 

			}
		} catch (Exception e) {
			System.err.println("Unexpected error while getting SeatNames in result page: " + e.getMessage());
			e.printStackTrace();
			ScreenShots.takeScreenShot();
			Assert.fail();

		}

		return listOfSeat;
	}

	//Method to getText of data in result page
	public String[] getTextInResultPage() {
		try {
			String boardingPointText = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-boardingpoint')]//p")).getText();
			System.out.println("Boarding Point: " + boardingPointText);

			String dropingPointText = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-droppingpoint')]//p")).getText();
			System.out.println("Dropping Point: " + dropingPointText);

			String price = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-totalseatprice')]")).getText();
			System.out.println("Price: " + price);

			String boardingTime = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-deptime')]")).getText();
			System.out.println("Boarding Time: " + boardingTime);

			String arrivalTime = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-arrtime')]")).getText();
			System.out.println("Arrival Time: " + arrivalTime);

			String journeyDuration = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-duration')]")).getText();
			System.out.println("Journey Duration: " + journeyDuration);

			String operatorName = driver.findElement(By.xpath("(//*[contains(normalize-space(@class), 'tg-bsrs-operatorname')])[1]")).getText();
			System.out.println("Operator Name: " + operatorName);

			String noOfSeatsAvailable = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-availableseats')]")).getText();
			System.out.println("Seats Available: " + noOfSeatsAvailable);

			return new String[] {
					boardingPointText,
					dropingPointText,
					price,
					boardingTime,
					arrivalTime,
					journeyDuration,
					operatorName,
					noOfSeatsAvailable
			};

		} catch (Exception e) {
			String errorMessage = "Exception in getTextInResultPage(): " + e.getMessage();
			System.err.println(errorMessage);
			e.printStackTrace();
			Assert.fail(errorMessage);
			return new String[0];  // Safe fallback if failure occurs
		}
	}

	//Method to click on confirm seat button
	public void clickOnConfirmSeat(Log Log, ScreenShots ScreenShots) throws InterruptedException
	{
		try
		{
			Thread.sleep(2000);

			driver.findElement(By.xpath("//button[text()='Confirm Seat']")).click();
			Log.ReportEvent("INFO", "Clicked on confirm seat button successfully"); 

		}
		catch (Exception e) {
			e.printStackTrace(); 
			System.err.println("Unexpected error when clicking on confirm seat button in result page : " + e.getMessage());
			Log.ReportEvent("FAIL", "Unexpected error when clicking on confirm seat button in result page :"+ e.getMessage()); 
			ScreenShots.takeScreenShot();
			Assert.fail();
		}

	}
	//Method to close reason For Selection PopUp
	public void reasonForSelectionPopUp1(Log Log, ScreenShots ScreenShots) throws InterruptedException {
		{
			try
			{
				String value = "Personal Preference";

				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
				Thread.sleep(8000);
				WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//*[@id='alert-dialog-title']")
						));

				if (popup.isDisplayed()) {
					WebElement reasonOption = driver.findElement(
							By.xpath("//*[text()='" + value + "']//parent::label")
							);
					reasonOption.click();
					driver.findElement(By.xpath("//button[text()='Proceed to Booking']")).click();

				}
			}
			catch (Exception e) {
				System.err.println("Unable to find the reason pop-up: " + e.getMessage());
				Log.ReportEvent("FAIL", "Unable to find the reason pop-up.");
				e.printStackTrace();
				ScreenShots.takeScreenShot();
				Assert.fail();
			}
		}
	}
	//Method to validate result page data with booking page
	public void validateResultAndBookingPageData(String[] getTextInResultPage, String[] getTextInBookingPage,
			List<String> getSeatNames, List<String> fetchSeatInBookingPage,
			Log Log, ScreenShots ScreenShots) {

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
		String boardingTimeInbookingPage = getTextInBookingPage[8];

		// Compare booking vs result page
		if (boardingPoint.equals(boardingPointText)
				&& dropingPoint.equals(dropingPointText)
				&& Price.equals(price)
				&& journeyDuration.equals(travellingTime)
				&& operatorNameInResultPage.equals(operatorName)) {

			String matchedDetails = String.format(
					"Booking page details matched:\n" +
							"- boardingPoint: %s\n" +
							"- dropingPoint: %s\n" +
							"- Price: %s\n" +
							"- journeyDuration: %s\n" +
							"- operatorName: %s",
							boardingPoint, dropingPoint, Price, journeyDuration, operatorNameInResultPage
					);

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
							operatorName, operatorNameInResultPage
					);

			Log.ReportEvent("FAIL", mismatchDetails);
			System.out.println(mismatchDetails);
		}

		//// Seat list comparison
		if (sortedExpected.equals(sortedActual)) {
			Log.ReportEvent("PASS", "Seat List Matched: " + sortedExpected + " with " + sortedActual);
			System.out.println("Seat List Matched: " + sortedExpected + " with " + sortedActual);
		} else {
			Log.ReportEvent("FAIL", "Seat List mismatch!: " + sortedExpected + " with " + sortedActual);
			System.out.println("Seat List mismatch!: " + sortedExpected + " with " + sortedActual);
		}

	}

	//Method to validate user search data with booking page data
	public void validateUserSearchDataAndBookingPageData(String[] userInput,String[] getTextInBookingPage,Log Log, ScreenShots ScreenShots)
	{
		String boardingCityAsPerUserInput = userInput[0];
		String dropingCityAsPerUserInput = userInput[1];
		String JourneyDate = userInput[2];
		System.out.println(boardingCityAsPerUserInput);
		System.out.println(dropingCityAsPerUserInput);
		System.out.println(JourneyDate);
		String boardingCity = getTextInBookingPage[0];
		String dropingCity = getTextInBookingPage[1];
		String travellingDate = getTextInBookingPage[2];
		String travellingTime = getTextInBookingPage[3];
		String boardingPointText = getTextInBookingPage[4];
		String dropingPointText = getTextInBookingPage[5];
		String price = getTextInBookingPage[6];
		String operatorName = getTextInBookingPage[7];
		System.out.println(boardingCity);
		System.out.println(dropingCity);
		System.out.println(travellingDate);
		System.out.println(travellingTime);
		System.out.println(boardingPointText);
		System.out.println(dropingPointText);
		System.out.println(price);
		System.out.println(operatorName);

		if(boardingCityAsPerUserInput.equals(boardingCity)&&dropingCityAsPerUserInput.equals(dropingCity)&&JourneyDate.equals(travellingDate))
		{
			String matchedDetails = String.format(
					"Booking page details matched:\n" +
							"- boardingCity: %s\n" +
							"- dropingCity: %s\n" +
							"- travellingDate: %s\n",

							boardingCity, dropingCity, travellingDate
					);

			Log.ReportEvent("PASS", matchedDetails);

			//System.out.println(" Booking page validation passed:boarding, droping city and boarding date matched with user entered data with booking page data");
		}
		else
		{
			//System.out.println("boarding, droping city and boarding date not matched");
			String mismatchDetails = String.format(
					"Booking page vs user data comparison:\n" +
							"- boardingCity: expected [%s] | actual [%s]\n" +
							"- dropingCity: expected [%s] | actual [%s]\n" +
							"- travellingDate: expected [%s] | actual [%s]\n",

							boardingCityAsPerUserInput, boardingCity,
							dropingCityAsPerUserInput, dropingCity,
							JourneyDate, travellingDate

					);

			Log.ReportEvent("FAIL", mismatchDetails);
			System.out.println(mismatchDetails);
		}

	}
	public void validateResultPageIsDisplayed(Log Log, ScreenShots ScreenShots) {
		try {
			Thread.sleep(2000);
			//  WebElement resultPageGrid = driver.findElement(By.xpath("//*[contains(@class, 'MuiGrid2-grid-md-9')]"));
			WebElement resultPageGrid = driver.findElement(By.xpath("(//button[text()='View Seats'])[1]"));

			if (resultPageGrid.isDisplayed()) {
				Log.ReportEvent("PASS", "Result page is getting displayed successfully.");
				System.out.println("Result page is getting displayed successfully.");
			} else {

				Log.ReportEvent("FAIL", "Result page is not displayed.");
				System.out.println("Result page is not displayed.");
			}

		} catch (NoSuchElementException e) {
			Log.ReportEvent("FAIL", "Result page element not found.");
			System.out.println("Result page element not found.");
			ScreenShots.takeScreenShot1();
			Assert.fail();
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "An unexpected error occurred: " + e.getMessage());
			System.out.println("Unexpected error: " + e.getMessage());
			ScreenShots.takeScreenShot1();
			Assert.fail();

		}
	}
	public String selectedTotalAmountPrice() {
		try {
			String totalAmount = driver.findElement(
					By.xpath("//*[contains(@class,'tg-bsrs-totalseatprice')]")
					).getText();
			return totalAmount;
		} catch (Exception e) {
			System.out.println("Exception while fetching total amount: " + e.getMessage());
			e.printStackTrace();
			return null; // or return "0" or "N/A" based on how you want to handle it
		}
	}

	public List<String> clickOnDepartTime(List<String> userInputDepartTime) {
	    List<String> selectedDepartTime = new ArrayList<>();
 
	    // Find all bus type filter options
	    List<WebElement> selectedDepartTimeOptions = driver.findElements(
	        By.xpath("//*[contains(normalize-space(@class), 'tg-bsflt-deptime')]//div//span")
	    );
 
	    // Loop through each option and click if it matches any user input
	    for (WebElement getDepartTime : selectedDepartTimeOptions) {
	        String getDepartTimeText = getDepartTime.getText().trim();
 
	        for (String userInput : userInputDepartTime) {
	            if (getDepartTimeText.equalsIgnoreCase(userInput)) {
	            	getDepartTime.click();
	                // Optional: add a small wait here to ensure UI updates before next click
	                // Thread.sleep(500); or use WebDriverWait until clickable/visible if needed
	                break;  // Break inner loop after clicking current option
	            }
	        }
	    }
 
	    // Now get all checkboxes + labels after clicking filters
	    List<WebElement> departTimes = driver.findElements(
	        By.xpath("//*[contains(@class, 'tg-bsflt-deptime')]//li")
	    );
 
	    for (WebElement departTime : departTimes) {
	        WebElement checkbox = departTime.findElement(By.xpath(".//input[@type='checkbox']"));
	        WebElement label = departTime.findElement(By.xpath(".//span[contains(@class, 'MuiListItemText-primary')]"));
 
	        if (checkbox.isSelected()) {
	            String clickedDepartTimeText = label.getText().trim();
	            selectedDepartTime.add(clickedDepartTimeText);
	            System.out.println("Selected Depart Time: " + selectedDepartTime);
	        }
	    }
 
	    return selectedDepartTime;
	}
	/*
	 public void ValidateDepartTime(List<String> selectedDepartTime,Log Log, ScreenShots ScreenShots) {
	        System.out.println("Selected time filters: " + selectedDepartTime);

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

	        // Extract all visible departure times and convert to minutes
	        List<WebElement> departElements = driver.findElements(
	            By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-deptime')]")
	        );

	        List<Integer> departInMinutes = new ArrayList<>();

	        for (WebElement depart : departElements) {
	            String departTimeText = depart.getText().trim();
	            try {
	                LocalTime time = LocalTime.parse(departTimeText, formatter);
	                int minutes = time.getHour() * 60 + time.getMinute();
	                departInMinutes.add(minutes);
	                System.out.println("Visible depart time: " + departTimeText + " = " + minutes + " mins");
	            } catch (DateTimeParseException e) {
	                System.err.println("Invalid displayed time format: " + departTimeText);
	            }
	        }

	        // Validate each time against the corresponding filter
	        for (String filter : selectedDepartTime) {
	            int[] range = getFilterRangeInMinutes(filter);
	            int start = range[0];
	            int end = range[1];

	            System.out.println("\n🔍 Validating filter: \"" + filter + "\" (Expected between " + start + " and " + end + " mins)");
	          
	            for (int time : departInMinutes) {
	                if (time >= start && time <= end) {
	                    System.out.println("✅ MATCH: " + time + " mins");
	                } else {
	                    System.err.println("❌ INVALID: " + time + " mins (Expected between " + start + " and " + end + ")");
	                }
	            }
	        }
	    }

	    public static int[] getFilterRangeInMinutes(String filterText) {
	        filterText = filterText.toLowerCase().trim();

	        if (filterText.startsWith("before")) {
	            int end = convertToMinutes(filterText.replace("before", "").trim());
	            return new int[]{0, end - 1};  // before 'end' minute

	        } else if (filterText.startsWith("after")) {
	            int start = convertToMinutes(filterText.replace("after", "").trim());
	            return new int[]{start + 1, 1439};  // after 'start' minute

	        } else if (filterText.contains("to")) {
	            String[] parts = filterText.split("to");
	            if (parts.length != 2) {
	                throw new IllegalArgumentException("Invalid time range format: " + filterText);
	            }

	            int start = convertToMinutes(parts[0].trim());
	            int end = convertToMinutes(parts[1].trim());
	            return new int[]{start, end};
	        }

	        throw new IllegalArgumentException("Unsupported filter format: " + filterText);
	    }

	    public static int convertToMinutes(String timeText) {
	        timeText = timeText.toLowerCase().trim();

	        if (timeText.contains("am") || timeText.contains("pm")) {
	            String[] parts = timeText.split(" ");
	            if (parts.length != 2) {
	                throw new IllegalArgumentException("Invalid time format: " + timeText);
	            }

	            int hour = Integer.parseInt(parts[0]);
	            String meridian = parts[1];

	            if (meridian.equals("am") && hour == 12) {
	                hour = 0;
	            } else if (meridian.equals("pm") && hour != 12) {
	                hour += 12;
	            }

	            return hour * 60;
	        }

	        // fallback for "HH:mm" format
	        try {
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	            LocalTime time = LocalTime.parse(timeText, formatter);
	            return time.getHour() * 60 + time.getMinute();
	        } catch (DateTimeParseException e) {
	            throw new IllegalArgumentException("Unexpected time format: " + timeText);
	        }
	    }
*/
	/*
	public void ValidateDepartTime(List<String> selectedFilters, Log Log, ScreenShots ScreenShots) {
	    System.out.println("Selected Filters: " + selectedFilters);

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

	    // Get all departure time elements from page
	    List<WebElement> departElements = driver.findElements(
	        By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-deptime')]")
	    );

	    if (departElements.isEmpty()) {
	        Log.ReportEvent("FAIL", "❌ Result page element not found (no departure times).");
	        ScreenShots.takeScreenShot1();
	        return;
	    }

	    // Convert visible departure times to minutes
	    List<Integer> departMinutes = new ArrayList<>();
	    List<String> departTextList = new ArrayList<>();

	    for (WebElement element : departElements) {
	        String timeText = element.getText().trim();
	        try {
	            LocalTime time = LocalTime.parse(timeText, formatter);
	            int minutes = time.getHour() * 60 + time.getMinute();
	            departMinutes.add(minutes);
	            departTextList.add(timeText);
	        } catch (DateTimeParseException e) {
	            Log.ReportEvent("FAIL", "❌ Invalid time format: " + timeText);
	            ScreenShots.takeScreenShot1();
	        }
	    }

	    // Validate each selected filter
	    for (String filter : selectedFilters) {
	        int[] range = getFilterRangeInMinutes(filter);
	        int start = range[0];
	        int end = range[1];

	        boolean isFilterValid = true;

	        Log.ReportEvent("INFO", "🔍 Validating filter: \"" + filter + "\" (" + start + " to " + end + " mins)");

	        for (int i = 0; i < departMinutes.size(); i++) {
	            int time = departMinutes.get(i);
	            String timeText = departTextList.get(i);

	            if (time >= start && time <= end) {
	                Log.ReportEvent("PASS", "✅ " + timeText + " is valid for filter \"" + filter + "\"");
	            } else {
	                isFilterValid = false;
	                Log.ReportEvent("FAIL", "❌ " + timeText + " is NOT valid for filter \"" + filter + "\" (Expected: " + start + " - " + end + ")");
	            }
	        }

	        if (!isFilterValid) {
	            ScreenShots.takeScreenShot1();
	        }
	    }
	}
	
	public static int[] getFilterRangeInMinutes(String filter) {
	    filter = filter.toLowerCase().trim();

	    if (filter.startsWith("before")) {
	        int end = convertToMinutes(filter.replace("before", "").trim());
	        return new int[]{0, end - 1};
	    } else if (filter.startsWith("after")) {
	        int start = convertToMinutes(filter.replace("after", "").trim());
	        return new int[]{start + 1, 1439};
	    } else if (filter.contains("to")) {
	        String[] parts = filter.split("to");
	        int start = convertToMinutes(parts[0].trim());
	        int end = convertToMinutes(parts[1].trim());
	        return new int[]{start, end};
	    }

	    throw new IllegalArgumentException("Invalid filter: " + filter);
	}
	public static int convertToMinutes(String timeText) {
	    timeText = timeText.toLowerCase().trim();
	    String[] parts = timeText.split(" ");

	    int hour = Integer.parseInt(parts[0]);
	    String meridian = parts[1];

	    if (meridian.equals("am") && hour == 12) hour = 0;
	    else if (meridian.equals("pm") && hour != 12) hour += 12;

	    return hour * 60;
	}
	*/
	/*
	public void ValidateDepartTime(List<String> selectedFilters, Log Log, ScreenShots ScreenShots) {
	    System.out.println("Selected Filters: " + selectedFilters);

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

	    // Get all departure time elements from the page
	    List<WebElement> departElements = driver.findElements(
	        By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-deptime')]")
	    );

	    if (departElements.isEmpty()) {
	        Log.ReportEvent("FAIL", "❌ No departure times found on the result page.");
	        ScreenShots.takeScreenShot1();
	        return;
	    }

	    // Convert visible departure times to minutes
	    List<Integer> departMinutes = new ArrayList<>();
	    List<String> departTextList = new ArrayList<>();

	    for (WebElement element : departElements) {
	        String timeText = element.getText().trim();
	        try {
	            LocalTime time = LocalTime.parse(timeText, formatter);
	            int minutes = time.getHour() * 60 + time.getMinute();
	            departMinutes.add(minutes);
	            departTextList.add(timeText);
	        } catch (DateTimeParseException e) {
	            Log.ReportEvent("FAIL", "❌ Invalid time format: " + timeText);
	            ScreenShots.takeScreenShot1();
	        }
	    }

	    // Validate each selected filter
	    for (String filter : selectedFilters) {
	        int[] range = getFilterRangeInMinutes(filter);
	        int start = range[0];
	        int end = range[1];

	        boolean isFilterValid = true;

	        Log.ReportEvent("INFO", "🔍 Validating filter: \"" + filter + "\" (Expected range: " + start + " - " + end + " mins)");

	        for (int i = 0; i < departMinutes.size(); i++) {
	            int time = departMinutes.get(i);
	            String text = departTextList.get(i);

	            if (time >= start && time <= end) {
	              //  Log.ReportEvent("PASS", "✅ " + text + " is valid for \"" + filter + "\"");
	                System.out.println( "✅ " + text + " is valid for \"" + filter + "\"");
	            } else {
	                isFilterValid = false;
	                Log.ReportEvent("FAIL", "❌ " + text + " is outside range for \"" + filter + "\" (Expected: " + start + " - " + end + ")");
	                System.out.println("❌ " + text + " is outside range for \"" + filter + "\" (Expected: " + start + " - " + end + ")");
	            }
	        }
	        if(isFilterValid)
	        {
	        	Log.ReportEvent("PASS", "✅ " + text + " is valid for \"" + filter + "\"");
	        }

	        else  if (!isFilterValid) {
	        	Log.ReportEvent("FAIL", "❌ " + text + " is outside range for \"" + filter + "\" (Expected: " + start + " - " + end + ")");
	            ScreenShots.takeScreenShot1();
	        }
	    }
	}
	*/
	public void ValidateDepartTime(List<String> selectedFilters, Log Log, ScreenShots ScreenShots) {
	    System.out.println("Selected Filters: " + selectedFilters);

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

	    List<WebElement> departElements = driver.findElements(
	        By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-deptime')]")
	    );

	    if (departElements.isEmpty()) {
	        Log.ReportEvent("FAIL", "❌ No departure times found on the result page.");
	        ScreenShots.takeScreenShot1();
	        return;
	    }

	    // Extract all departure times from the page and convert them to minutes
	    List<Integer> departMinutes = new ArrayList<>();
	    List<String> departTextList = new ArrayList<>();

	    for (WebElement element : departElements) {
	        String timeText = element.getText().trim();
	        try {
	            LocalTime time = LocalTime.parse(timeText, formatter);
	            int minutes = time.getHour() * 60 + time.getMinute();
	            departMinutes.add(minutes);
	            departTextList.add(timeText);
	        } catch (DateTimeParseException e) {
	            Log.ReportEvent("FAIL", "❌ Invalid time format: " + timeText);
	            ScreenShots.takeScreenShot1();
	        }
	    }

	    // Loop through each selected filter
	    for (String filter : selectedFilters) {
	        int[] range = getFilterRangeInMinutes(filter);
	        int start = range[0];
	        int end = range[1];

	        boolean isFilterValid = true;

	        Log.ReportEvent("INFO", "🔍 Validating filter: \"" + filter + "\" (Expected range: " + start + " - " + end + " mins)");

	        // Check each departure time against the current filter range
	        for (int i = 0; i < departMinutes.size(); i++) {
	            int time = departMinutes.get(i);
	            String timeText = departTextList.get(i);

	            if (time >= start && time <= end) {
	                System.out.println("✅ " + timeText + " is valid for \"" + filter + "\"");
	            } else {
	                isFilterValid = false;
	         //       Log.ReportEvent("FAIL", "❌ " + timeText + " is outside range for \"" + filter + "\" (Expected: " + start + " - " + end + ")");
	                System.out.println("❌ " + timeText + " is outside range for \"" + filter + "\" (Expected: " + start + " - " + end + ")");
	            }
	        }

	        // Final pass/fail log for each filter
	        if (isFilterValid) {
	            Log.ReportEvent("PASS", "✅ All visible departure times matched filter: \"" + filter + "\"");
	        } else {
	            Log.ReportEvent("FAIL", "❌ Some departure times did not match filter: \"" + filter + "\"");
	            ScreenShots.takeScreenShot1();
	        }
	    }
	}

	public static int[] getFilterRangeInMinutes(String filter) {
	    filter = filter.toLowerCase().trim();

	    if (filter.startsWith("before")) {
	        int end = convertToMinutes(filter.replace("before", "").trim());
	        return new int[]{0, end - 1};

	    } else if (filter.startsWith("after")) {
	        int start = convertToMinutes(filter.replace("after", "").trim());
	        return new int[]{start + 1, 1439};

	    } else if (filter.contains("to")) {
	        String[] parts = filter.split("to");
	        int start = convertToMinutes(parts[0].trim());
	        int end = convertToMinutes(parts[1].trim());
	        return new int[]{start, end};
	    }

	    throw new IllegalArgumentException("Invalid filter: " + filter);
	}
	public static int convertToMinutes(String timeText) {
	    timeText = timeText.toLowerCase().trim();
	    String[] parts = timeText.split(" ");

	    int hour = Integer.parseInt(parts[0]);
	    String meridian = parts[1];

	    if (meridian.equals("am") && hour == 12) hour = 0;
	    else if (meridian.equals("pm") && hour != 12) hour += 12;

	    return hour * 60;
	}


}



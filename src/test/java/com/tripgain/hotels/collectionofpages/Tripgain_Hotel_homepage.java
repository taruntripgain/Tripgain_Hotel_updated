package com.tripgain.hotels.collectionofpages;

import java.awt.AWTException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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

public class Tripgain_Hotel_homepage {

	WebDriver driver;


	public Tripgain_Hotel_homepage(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	@FindBy(xpath = "(//input[@class='DayPickerInput input'])[1]")
	WebElement datePickerInput;

	@FindBy(xpath = "//input[@id='tg-hl-search-input']")
	private WebElement enterLocation;

	//Method to click on Hotel dropdown
	public void hotelClick()
	{
		driver.findElement(By.xpath("(//*[@data-testid='KeyboardArrowDownIcon'])[1]")).click();
		driver.findElement(By.xpath("//a[text()='Hotels']")).click();
	}
	//Method to check whether Home Page is displayed
	public void validateHomePgaeIsDisplayed(Log Log, ScreenShots ScreenShots) {
		try {
			Thread.sleep(3000);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));
			// Wait for dropdown container to appear
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//span[text()='Search']")));
			WebElement homePage = driver.findElement(By.xpath("//span[text()='Search']"));
			if (homePage.isDisplayed()) {
				System.out.println("Home page is getting displayed successfully.");
				ScreenShots.takeScreenShot();

				Log.ReportEvent("PASS", "Home page is getting displayed successfully.");
			} else {
				System.out.println("Failed to load the Home Page: Element found but not visible.");
				Log.ReportEvent("FAIL", "Failed to load the Home Page: Element not visible.");
				ScreenShots.takeScreenShot();
				Assert.fail();
			}

		} catch (Exception e) {
			System.out.println("Exception while verifying Home Page: " + e.getMessage());
			Log.ReportEvent("FAIL", "Exception while verifying Home Page: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail();

		}
	}

	//Method to select City.
	public void selectCityOrHotelName(String location) throws TimeoutException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Wait for dropdown container to appear
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[@role='listbox']/parent::div")));

			// Wait until options are loaded
			wait.until(driver -> driver.findElements(By.xpath("//div[@role='option']")).size() > 0);

			List<WebElement> initialOptions = driver.findElements(By.xpath("//div[@role='option']"));
			int bestScore = Integer.MAX_VALUE;
			String bestMatchText = null;

			String input = location.trim().toLowerCase();

			for (int i = 0; i < initialOptions.size(); i++) {
				try {
					WebElement option = driver.findElements(By.xpath("//div[@role='option']")).get(i);
					String suggestion = option.getText().trim().toLowerCase();
					int score = levenshteinDistance(input, suggestion);

					if (score < bestScore) {
						bestScore = score;
						bestMatchText = option.getText().trim();
					}
				} catch (StaleElementReferenceException e) {
					System.out.println("Stale element at index " + i + ", skipping.");
				}
			}

			if (bestMatchText != null) {
				// Retry clicking best match up to 3 times
				int attempts = 0;
				boolean clicked = false;
				while (attempts < 3 && !clicked) {
					try {
						WebElement bestMatch = wait.until(ExpectedConditions.elementToBeClickable(
								By.xpath("//div[@role='option' and normalize-space(text())='" + bestMatchText + "']")));
						bestMatch.click();
						System.out.println("Selected best match: " + bestMatchText);
						clicked = true;
					} catch (StaleElementReferenceException e) {
						System.out.println("Stale element on click attempt " + (attempts + 1) + ", retrying...");
					}
					attempts++;
				}

				if (!clicked) {
					System.out.println("Failed to click the best match after retries.");
				}

			} else {
				System.out.println("No suitable match found for input: " + location);
			}

		} catch (NoSuchElementException e) {
			System.out.println("Input or dropdown not found: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Unexpected error while selecting city or hotel: " + e.getMessage());
		}
	}
	
	//Method to enter City Or Hotel Name
	public void enterCityOrHotelName(String location) {
		try {
			enterLocation.clear();
			enterLocation.sendKeys(location);
			selectCityOrHotelName(location);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int levenshteinDistance(String a, String b) {
		int[][] dp = new int[a.length() + 1][b.length() + 1];

		for (int i = 0; i <= a.length(); i++) {
			for (int j = 0; j <= b.length(); j++) {
				if (i == 0) {
					dp[i][j] = j;
				} else if (j == 0) {
					dp[i][j] = i;
				} else {
					int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
					dp[i][j] = Math.min(
							Math.min(dp[i - 1][j] + 1,     // deletion
									dp[i][j - 1] + 1),    // insertion
							dp[i - 1][j - 1] + cost); // substitution
				}
			}
		}
		return dp[a.length()][b.length()];
	}


	//Method to click on City dropdown
	public void clickOnPropertySearchfield(String city) throws InterruptedException
	{
		try {
			Thread.sleep(3000);
			WebElement seachFieldClick = driver.findElement(By.xpath("//input[@aria-describedby='react-select-4-placeholder']"));
			//seachFieldClick.click();
			seachFieldClick.sendKeys(city);
			Thread.sleep(2000);
			List<WebElement> listOfProperty = driver.findElements(By.xpath("//div[@role='option']"));
			WebElement firstProperty = listOfProperty.get(0);
			firstProperty.click();
		}
		catch (Exception e) {
			System.out.println("Failed to select a property from the list: " + e.getMessage());
			return;
		}
	}
	//Method to Click on Check-Out  Date
	public void clickOnReturnDate()
	{
		driver.findElement(By.xpath("(//input[@class='DayPickerInput input'])[2]")).click();
	}
	//Method to Select Return Date By Passing Two Paramenters(Date and MounthYear)
	public void selectReturnDate(String returnDate, String returnMonthAndYear) throws InterruptedException
	{
		clickOnReturnDate();
		String Date=driver.findElement(By.xpath("(//div[@class='react-datepicker__header ']/child::h2)[1]")).getText();
		if(Date.contentEquals(returnMonthAndYear))
		{
			driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='"+returnDate+"' and @aria-disabled='false']")).click();
			Thread.sleep(4000);
		}else {
			while(!Date.contentEquals(returnMonthAndYear))
			{
				Thread.sleep(500);
				driver.findElement(By.xpath("//button[@aria-label='Next Month']")).click();
				if(driver.findElement(By.xpath("(//div[@class='react-datepicker__header ']/child::h2)[1]")).getText().contentEquals(returnMonthAndYear))
				{
					driver.findElement(By.xpath("//div[text()='"+returnDate+"']")).click();
					break;
				}

			}
		}
	}



	// Method to click on search button
	public void clickOnSearch() {
		try {
			System.out.println("Attempting to click on the 'Search' button...");
			driver.findElement(By.xpath("//button[text()='Search']")).click();
			System.out.println("'Search' button clicked successfully.");
		} catch (Exception e) {
			System.out.println("Failed to click on the 'Search' button: " + e.getMessage());
		}
	}


	// Method to get flight count on result page

	public void getFlightsCount(Log Log, ScreenShots ScreenShots) {
		try {
			WebElement Flightscount = driver.findElement(By.className("total-hotels-count"));
			String countText = Flightscount.getText();
			Log.ReportEvent("PASS", "Flight count is: " + countText);
			System.out.println("Flight count is: " + countText);
			ScreenShots.takeScreenShot1();
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to retrieve flight count: " + e.getMessage());
			ScreenShots.takeScreenShot1();
		}
	}

	//------------------------------------------------------------------------------------------------------




	//Method to get the hotels name text for validating result screen is displayed or not

	public void validateResultScreen(Log Log, ScreenShots ScreenShots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
			WebElement allHotelsGrid = wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//div[@class='MuiGrid2-root MuiGrid2-direction-xs-row MuiGrid2-grid-lg-9 MuiGrid2-grid-md-8 MuiGrid2-grid-sm-12 MuiGrid2-grid-xs-12 css-lgz2hl']")));

			// Check if the hotel grid is displayed
			if (allHotelsGrid.isDisplayed()) {
				System.out.println("Hotel grid is displayed.");
				Log.ReportEvent("PASS", "Hotel grid is displayed.");
			} else {
				Log.ReportEvent("FAIL", "Hotel grid is not displayed.");
			}

			ScreenShots.takeScreenShot1();	      
			// Get the total count of hotels
			WebElement countElement = driver.findElement(By.className("total-hotels-count"));
			String countText = countElement.getText();
			String totalCountStr = countText.replaceAll("[^0-9]", ""); // Extract digits
			int totalCount = Integer.parseInt(totalCountStr);

			//Get the list of hotel names
			List<WebElement> hotelNames = driver.findElements(By.xpath("//h6[@title]"));

			// Validate if at least 20 hotel names are displayed
			if (hotelNames.size() >= 20) {
				// Get the first 20 names
				List<String> first20Names = new ArrayList<>();
				for (int i = 0; i < 20; i++) {
					first20Names.add(hotelNames.get(i).getText());
				}
				System.out.println("Total Hotels Count: " + totalCount + " | First 20 Hotels Displayed: " + first20Names);
				Log.ReportEvent("PASS", "Total Hotels Count: " + totalCount + " | First 20 Hotels Displayed: " + first20Names);
				ScreenShots.takeScreenShot1();
			} else {
				Log.ReportEvent("FAIL", "Only " + hotelNames.size() + " hotel names are displayed. Expected at least 20.");
				ScreenShots.takeScreenShot1();
			}

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception during validation: " + e.getMessage());
			ScreenShots.takeScreenShot1();
		}
	}


	//Method to click on save and continue button
	public void clickOnSaveAndContinueButton(Log Log, ScreenShots ScreenShots) {
		try {
			Log.ReportEvent("INFO", "Attempting to click Save and Continue button");
			ScreenShots.takeScreenShot1();

			driver.findElement(By.xpath("(//*[contains(normalize-space(@class), 'tg-hl-continue-btn')])[1]"))
			.click();

			WebElement reviewPage = driver.findElement(
					By.xpath("(//*[contains(normalize-space(@class), 'booking-header-title')])[1]"));
			Thread.sleep(1000);
			if (reviewPage.isDisplayed()) {
				Log.ReportEvent("PASS", "Review Your Booking Page displayed successfully");
				ScreenShots.takeScreenShot1();

				System.out.println("Review Your Booking Page Displayed Succsesfully");
			}
			else {
				Log.ReportEvent("FAIL", "Review Your Booking Page NOT displayed as expected");
				ScreenShots.takeScreenShot1();

				System.out.println("Review Your Booking Page NOT displayed as expected");
			}

		} catch (org.openqa.selenium.NoSuchElementException e) {
			Log.ReportEvent("FAIL", "Element not found during click or verification: " + e.getMessage());
			ScreenShots.takeScreenShot1();

		} catch (Exception e) {
			Log.ReportEvent("ERROR", "Unexpected error in clickOnSaveAndContinueButton: " + e.getMessage());
			ScreenShots.takeScreenShot1();

		}
	}

	//Method to validate selected hotel data 
	public void validateSelectedHotelPage(Log Log, ScreenShots ScreenShots) {
		String hotelName = "";

		try {
			Thread.sleep(1000);

			hotelName = driver.findElement(By.xpath("(//*[contains(normalize-space(@class), 'tg-hl-name')])[1]")).getText().trim();
			System.out.println("Captured hotel name from search results: " + hotelName);
			ScreenShots.takeScreenShot1();

			Log.ReportEvent("PASS", "Captured hotel name from search results: " + hotelName);
		} catch (Exception e) {
			System.out.println("Failed to get hotel name from search results: " + e.getMessage());
			ScreenShots.takeScreenShot1();

			Log.ReportEvent("FAIL", "Failed to get hotel name from search results: " + e.getMessage());
			return;
		}

		try {
			Thread.sleep(1000);
			ScreenShots.takeScreenShot1();

			driver.findElement(By.xpath("(//div[@class='MuiGrid2-root MuiGrid2-container MuiGrid2-direction-xs-row css-uyeqkp']//button[text()='Select'])[1]")).click();
			System.out.println("Clicked on the 'Select' button.");

			Log.ReportEvent("PASS", "Clicked on the 'Select' button.");
		} catch (Exception e) {
			System.out.println("Failed to click on the 'Select' button: " + e.getMessage());
			ScreenShots.takeScreenShot1();

			Log.ReportEvent("FAIL", "Failed to click on the 'Select' button: " + e.getMessage());
			ScreenShots.takeScreenShot1();
			return;
		}

		try {
			Thread.sleep(2000);

			String selectedHotel = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-hl-hotalname')]")).getText();
			System.out.println("Hotel name on Detail page: " + selectedHotel);
			ScreenShots.takeScreenShot1();

			Log.ReportEvent("PASS", "Hotel name on Detail page: " + selectedHotel);

			if (hotelName.equals(selectedHotel)) {
				System.out.println("Selected Hotel displayed successfully in detail page.");
				ScreenShots.takeScreenShot1();

				Log.ReportEvent("PASS", "Selected Hotel displayed successfully in detail page: " + selectedHotel);
			} else {
				System.out.println("Mismatch: Expected [" + hotelName + "], but found [" + selectedHotel + "]");
				ScreenShots.takeScreenShot1();

				Log.ReportEvent("FAIL", "Hotel name mismatch. Expected: " + hotelName + ", Found: " + selectedHotel);
				ScreenShots.takeScreenShot1();
			}
		} catch (Exception e) {
			System.out.println("Failed to validate selected hotel: " + e.getMessage());
			ScreenShots.takeScreenShot1();

			Log.ReportEvent("FAIL", "Failed to validate selected hotel: " + e.getMessage());
			ScreenShots.takeScreenShot1();
		}
	}


	//Method to add room, adt and child
	public void addRoom(int roomcount, int adultCount , int childCount, int childAge) throws InterruptedException {
		Thread.sleep(2000);
		try {
			System.out.println("Expanding room selection...");
			driver.findElement(By.xpath("//*[@data-testid='ExpandMoreIcon']")).click();
		} catch (Exception e) {
			System.out.println("Failed to click expand icon: " + e.getMessage());
		}
		JavascriptExecutor js = (JavascriptExecutor) driver;

		for(int i = 0; i < roomcount - 1; i++) {
			try {
				System.out.println("Adding room: " + (i + 2));
				driver.findElement(By.xpath("//button[text()='Add Room']")).click();
			} catch (Exception e) {
				System.out.println("Failed to click 'Add Room' button at index " + i + ": " + e.getMessage());
			}
		}

		Thread.sleep(3000);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[contains(text(),'Room')]")));
		} catch (Exception e) {
			System.out.println("Room elements not visible within timeout: " + e.getMessage());
		}

		List<WebElement> listOfRooms = driver.findElements(By.xpath("//h6[contains(text(),'Room')]"));
		System.out.println("Rooms found: " + listOfRooms.size());

		for(int i = 0; i < listOfRooms.size(); i++) {
			WebElement roomElement = listOfRooms.get(i);
			String roomText = roomElement.getText();
			String[] roomTextSplit1 = roomText.split(" ");
			String finalRoomText = roomTextSplit1[1].trim();

			System.out.println("Configuring Room " + finalRoomText);
			System.out.println("Total rooms: " + listOfRooms.size());
			System.out.println("Room index: " + i);
			System.out.println("Adult count: " + adultCount);

			// Add Adults
			for(int j = 0; j < adultCount - 1; j++) {
				try {
					WebElement addAdult = driver.findElement(By.xpath("(//h6[text()='" + finalRoomText + "']/parent::div/parent::div//p/parent::div)[1]//*[contains(normalize-space(@class), 'tg-hl-adult-plus')]"));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addAdult);
					Thread.sleep(2000);
					addAdult.click();
					System.out.println("Adult added to Room " + finalRoomText);
				} catch (Exception e) {
					System.out.println("Failed to add adult in Room " + finalRoomText + " at iteration " + j + ": " + e.getMessage());
				}
			}

			// Add Children
			for (int k = 1; k < childCount + 1; k++) {
				try {
					Thread.sleep(3000);
					driver.findElement(By.xpath("(//h6[text()='" + finalRoomText + "']/parent::div/parent::div//p/parent::div)[2]//*[contains(normalize-space(@class), 'tg-hl-child-plus')]")).click();
					System.out.println("Child " + k + " added to Room " + finalRoomText);
				} catch (Exception e) {
					System.out.println("Failed to add child in Room " + finalRoomText + " at iteration " + k + ": " + e.getMessage());
				}

				try {
					driver.findElement(By.xpath("((//h6[text()='" + finalRoomText + "']/parent::div/parent::div//p/parent::div)[2]/parent::div//div[@aria-haspopup='listbox'])[" + k + "]")).click();
					Thread.sleep(2000);
					List<WebElement> childAgeList = driver.findElements(By.xpath("//ul//li"));
					childAgeList.get(childAge).click();
					System.out.println("Child " + k + " age set in Room " + finalRoomText);
				} catch (Exception e) {
					System.out.println("Failed to set child age in Room " + finalRoomText + " for child " + k + ": " + e.getMessage());
				}
			}

			// Click Done Button after final room
			if (i == listOfRooms.size() - 1) {
				try {
					WebElement doneButton = driver.findElement(By.xpath("//button[text()='Done']"));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", doneButton);
					Thread.sleep(500);
					doneButton.click();
					System.out.println("Clicked 'Done' after completing Room " + finalRoomText);
				} catch (Exception e) {
					System.out.println("Failed to click 'Done' button: " + e.getMessage());
				}
			}
		}
	}


	//Method to Select Check-In Date By Passing Two Paramenters(Date and MounthYear)
	public void selectDate(String day, String MonthandYear) throws InterruptedException
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// Method A: Using zoom
		js.executeScript("document.body.style.zoom='80%'");
		TestExecutionNotifier.showExecutionPopup();
		datePickerInput.click();
		String Date=driver.findElement(By.xpath("(//h2[@class='react-datepicker__current-month'])[1]")).getText();
		if(Date.contentEquals(MonthandYear))
		{
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='"+day+"' and @aria-disabled='false']")).click();
			Thread.sleep(4000);
		}else {
			while(!Date.contentEquals(MonthandYear))
			{
				Thread.sleep(500);
				driver.findElement(By.xpath("//button[@aria-label='Next Month']")).click();
				if(driver.findElement(By.xpath("(//div[@class='react-datepicker__header ']/child::h2)[1]")).getText().contentEquals(MonthandYear))
				{
					driver.findElement(By.xpath("(//div[@class='react-datepicker__month-container'])[1]//div[text()='"+day+"' and @aria-disabled='false']")).click();
					break;
				}

			}
		}
	}

}

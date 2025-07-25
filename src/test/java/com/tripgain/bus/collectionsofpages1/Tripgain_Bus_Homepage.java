package com.tripgain.bus.collectionsofpages1;

import static org.testng.Assert.fail;

import java.awt.AWTException;
import java.time.Duration;
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

public class Tripgain_Bus_Homepage {

	WebDriver driver;


	public Tripgain_Bus_Homepage(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	//Method to check whether Home Page is displayed
	public void validateHomePgaeIsDisplayed(Log Log, ScreenShots ScreenShots) {
		try {
			Thread.sleep(3000);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));
			// Wait for dropdown container to appear
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//h5[text()='Search']")));
			WebElement homePage = driver.findElement(By.xpath("//h5[text()='Search']"));
			if (homePage.isDisplayed()) {
				System.out.println("Home page is getting displayed successfully.");

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
	//Method to enter boarding point city
	@FindBy(xpath = "//*[text()='Enter From']/parent::div//input")
	private WebElement enterLocation;
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
	@FindBy(xpath = "//*[text()='Enter To']/parent::div//input")
	private WebElement enterDestinationLocation;

	//method to click on droping point dropdown
	public void enterCityOrHotelNameDestination(String location) {
		try {
			enterDestinationLocation.clear();
			enterDestinationLocation.sendKeys(location);
			selectCityOrHotelName(location);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int levenshteinDistanceDestination(String a, String b) {
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
	//Method to select City.
	public void selectCityOrHotelNameDestination(String location) throws TimeoutException {
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
	//Method to click on travel dropdown and to click on bus 
	public void busClick()
	{
		driver.findElement(By.xpath("(//*[@data-testid='KeyboardArrowDownIcon'])[1]")).click();
		driver.findElement(By.xpath("//a[text()='Buses']")).click();
	}

	@FindBy(xpath = "//*[contains(normalize-space(@class), 'tg-bs-date')]")
	WebElement datePickerInput;

	//Method to Select Check-In Date By Passing Two Paramenters(Date and MounthYear)
	public void selectDate(String day, String MonthandYear,Log Log, ScreenShots ScreenShots) throws InterruptedException
	{
		try
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
		catch (Exception e) {
			System.err.println("Failed to select the date: " + e.getMessage());
			e.printStackTrace();
			Log.ReportEvent("FAIL", "Failed to select the date.");
			ScreenShots.takeScreenShot();
			Assert.fail();

		} 

	}
	//Method to click on search button
	public void clickOnSearch(Log Log, ScreenShots ScreenShots)
	{
		try
		{
			driver.findElement(By.xpath("//button[text()='Search']")).click();	
			Log.ReportEvent("PASS", "Clicked on search bus button successfully.");
		}
		catch (Exception e) {
			System.err.println("Clicking on the search button failed: " + e.getMessage());
			e.printStackTrace();
			Log.ReportEvent("FAIL", "Failed to click on search button.");

		} 

	}

	//Method to click on seat
	public void pickSeats(int seatCount) {
		// find all available seats
		List<WebElement> availableSeats = driver.findElements(
				By.xpath("//*[contains(normalize-space(@class), 'tg-bsrs-seatavailable')]")
				);

		// Safety check
		if (availableSeats.size() < seatCount) 
		{
			throw new IllegalArgumentException("Only " + availableSeats.size() + " seats available, but asked for " + seatCount
					);
		}

		// Click the first `seatCount` seats
		for (int i = 0; i < seatCount; i++) {
			WebElement seat = availableSeats.get(i);

			// Optionally: scroll into view
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", seat);

			// Optionally: wait until clickable
			new WebDriverWait(driver, Duration.ofSeconds(5))
			.until(ExpectedConditions.elementToBeClickable(seat));

			seat.click();
		}
	}


	//Method to get seat names in result page
	public String[] UserInput(Log Log, ScreenShots ScreenShots)
	{
		String boardingPoint = "";
		String dropingPoint = "";
		String BoardingDateAndTime = "";
		try
		{
			boardingPoint = driver.findElement(By.xpath("(//*[contains(@class,'singleValue')])[1]")).getText();
			System.out.println(boardingPoint);
			dropingPoint = driver.findElement(By.xpath("(//*[contains(@class,'singleValue')])[2]")).getText();
			System.out.println(dropingPoint);
			BoardingDateAndTime = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-bs-date')]")).getAttribute("value");
			System.out.println(BoardingDateAndTime);
		}
		catch (Exception e) {
			System.err.println("Failed to get the userInput data in homepage : " + e.getMessage());
			e.printStackTrace();
			Log.ReportEvent("FAIL", "Failed to get the userInput data in homepage.");
			ScreenShots.takeScreenShot();
			Assert.fail();

		}
		return new String[] {boardingPoint,dropingPoint,BoardingDateAndTime};

	}

}





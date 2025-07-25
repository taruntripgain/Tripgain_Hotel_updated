package com.tripgain.hotels.collectionofpages;

import java.awt.AWTException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class Tripgain_Hotel_ShowViewMap {

	WebDriver driver;


	public Tripgain_Hotel_ShowViewMap(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	@FindBy(xpath="(//*[text()='Search location'])[2]/parent::div/parent::div")
	WebElement enterLocation;
	
	@FindBy(xpath="(//div[contains(@class, 'tg-select__control')])[3]")
	WebElement hotelName;
	
	public void enterLocations(String location) {
		try {
			enterLocation.clear();
			enterLocation.sendKeys(location);
			selectCityOrHotelName(location);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
        //Method to Click On Hotel Name component
		public void clickOnHotelName(Log log, ScreenShots screenShots) {
		    try {
		        hotelName.click();
		        System.out.println("✅ Cleared hotel name input.");
		    } catch (Exception e) {
		        System.out.println("❌ Failed to clear hotel name input: " + e.getMessage());
		        log.ReportEvent("FAIL", "Failed to clear hotel name input: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        Assert.fail("Failed to clear hotel name input.");
		    }
		}

		//Mrthod to get select Hotel Name
		public String selectHotelName(Log log, ScreenShots screenShots) {
		    try {
		        WebElement popupMenu = driver.findElement(By.xpath("(//*[contains(@class,'tg-select__menu')])[2]"));

		        if (popupMenu.isDisplayed()) {
		            List<WebElement> options = driver.findElements(By.xpath("//*[@role='option']"));
		            if (!options.isEmpty()) {
		                String hotelName = options.get(0).getText();
		                options.get(0).click();
		                System.out.println("✅ Selected hotel name: " + hotelName);
		                log.ReportEvent("INFO","✅ Selected hotel name: " + hotelName);
		                return hotelName;
		            } else {
		                System.out.println("⚠️ No hotel options found in the list.");
		                log.ReportEvent("FAIL", "No hotel options available to select.");
		                screenShots.takeScreenShot1();
		                Assert.fail("No hotel options found in the dropdown.");
		            }
		        } else {
		            System.out.println("⚠️ Hotel name popup menu is not displayed.");
		            log.ReportEvent("FAIL", "Hotel name dropdown is not displayed.");
		            screenShots.takeScreenShot1();
		            Assert.fail("Hotel name dropdown menu not displayed.");
		        }
		    } catch (Exception e) {
		        System.out.println("❌ Exception occurred while selecting hotel name: " + e.getMessage());
		        log.ReportEvent("FAIL", "Exception occurred while selecting hotel name: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        Assert.fail("Exception while selecting hotel name.");
		    }

		    return null; // return null if anything fails
		}
       //Method to get Selected Hotel Price
		public String getSelectedHotelPrice(Log log, ScreenShots screenShots) {
		    try {
		    	Thread.sleep(3000);
		        WebElement priceElement = driver.findElement(By.xpath("//div[@role='button']//div[contains(text(), '₹')]"));
		        String price = priceElement.getText();
		        System.out.println("✅ Hotel price found: " + price);
		        log.ReportEvent("INFO", "Hotel price found: " + price);
		        return price;
		    } catch (Exception e) {
		        System.out.println("❌ Failed to get hotel price: " + e.getMessage());
		        log.ReportEvent("FAIL", "Exception occurred while getting hotel price: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        Assert.fail("Exception while getting hotel price.");
		        e.printStackTrace();
		        return null;
		    }
		}

		public void closeShowViewPageButton(Log log, ScreenShots screenShots) {
		    try {
		        WebElement closeButton = driver.findElement(By.xpath("//button[text()='Close']"));
		        closeButton.click();
		        System.out.println("✅ 'Close' button clicked successfully.");
		        
		    } catch (Exception e) {
		        System.out.println("❌ Failed to click the 'Close' button: " + e.getMessage());
		        log.ReportEvent("FAIL", "Failed to click the 'Close' button: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        Assert.fail("Could not close the Show View page.");
		        e.printStackTrace();
		    }
		}

		/*
		public Map<String, Object> validateSelectedHotelNameAndPriceInShowViewMap(
		        String expectedHotelName, String expectedPrice, Log log, ScreenShots screenShots) {

		    Map<String, Object> hotelDetails = new HashMap<>();

		    try {
		    	Thread.sleep(3000);
		        // Click price element to open hotel details
		        driver.findElement(By.xpath("//div[@role='button']//div[contains(text(), '₹')]")).click();

		        Thread.sleep(5000);
		        WebElement hotelDetailDiv = driver.findElement(By.xpath("//*[@class='map-hotel-card-content']"));
		        if (hotelDetailDiv.isDisplayed()) {
		            String actualHotelName = driver.findElement(By.xpath("(//*[@class='map-hotel-card-content']//h6)[1]")).getText();
		            String actualPrice = driver.findElement(By.xpath("//*[@class='map-hotel-card-content']//*[@class='price-booking-rewards ']/h5")).getText();

		            // Normalize hotel names: remove star rating in parentheses
		            String expectedNamePart = expectedHotelName.split("\\(")[0].trim();
		            String actualNamePart = actualHotelName.split("\\(")[0].trim();

		            // Normalize prices: remove commas and spaces for comparison
		            String normalizedExpectedPrice = expectedPrice.replaceAll("[,\\s₹]", "");
		            String normalizedActualPrice = actualPrice.replaceAll("[,\\s₹]", "");

		            if (actualNamePart.equals(expectedNamePart) && normalizedActualPrice.equals(normalizedExpectedPrice)) {
		                log.ReportEvent("PASS", "Hotel Name and Price are matching: " + actualHotelName + " | " + actualPrice);

		                String hotelAddress = driver.findElement(By.xpath("(//*[@class='map-hotel-card-content']//h6)[2]")).getText();
		                List<WebElement> starReview = driver.findElements(By.xpath("//*[@class='map-hotel-card-content']//div//p//*[@data-testid]"));
		                int starRatingCount = starReview.size();
		                String policy = driver.findElement(By.xpath("(//*[contains(@class,'hotel-policy')])[2]")).getText();

		                // Put details into the map
		                hotelDetails.put("hotelAddress", hotelAddress);
		                hotelDetails.put("starRatingCount", starRatingCount);
		                hotelDetails.put("policy", policy);

		                // To click on select button
		                driver.findElement(By.xpath("(//button[text()='Select'])[2]")).click();

		                return hotelDetails;

		            } else {
		                log.ReportEvent("FAIL", "Hotel Name or Price does NOT match.\nExpected: " + expectedHotelName + ", " + expectedPrice +
		                        "\nActual: " + actualHotelName + ", " + actualPrice);
		                screenShots.takeScreenShot1();
		                Assert.fail("Hotel Name or Price does not match.");
		            }
		        } else {
		            log.ReportEvent("FAIL", "Hotel detail section is not displayed.");
		            screenShots.takeScreenShot1();
		            Assert.fail("Hotel detail section not visible.");
		        }
		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Exception occurred while validating hotel details: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        Assert.fail("Exception during hotel detail validation.");
		        e.printStackTrace();
		    }

		    return null; // failure case
		}
*/
		//Method to validate Selected Hotel Name And Price In Show View Map
		public Map<String, Object> validateSelectedHotelNameAndPriceInShowViewMap(
		        String expectedHotelName, String expectedPrice, Log log, ScreenShots screenShots) {

		    Map<String, Object> hotelDetails = new HashMap<>();

		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		        // Click price marker
		        WebElement priceButton = wait.until(ExpectedConditions.elementToBeClickable(
		            By.xpath("//div[@role='button']//div[contains(text(), '₹')]")));
		        priceButton.click();

		        // Wait for map card to appear
		        WebElement hotelDetailDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//*[@class='map-hotel-card-content']")));

		        // Get hotel name and price
		        String actualHotelName = driver.findElement(By.xpath("(//*[@class='map-hotel-card-content']//h6)[1]")).getText();
		    //    String actualPrice = driver.findElement(By.xpath("//*[@class='map-hotel-card-content']//*[@class='price-booking-rewards ']/h5")).getText();
		        String actualPrice = driver.findElement(By.xpath("(//*[contains(@class,'price-booking-rewards')])[2]//h5")).getText();

		        // Normalize names
		        String expectedNamePart = expectedHotelName.split("\\(")[0].trim();
		        System.out.println(expectedNamePart);
		        String actualNamePart = actualHotelName.split("\\(")[0].trim();
		        System.out.println(actualNamePart);

		        // Normalize prices (remove ₹, commas, whitespace)
		        String normalizedExpectedPrice = expectedPrice.replaceAll("[,\\s₹]", "");
		        System.out.println(normalizedExpectedPrice);
		        String normalizedActualPrice = actualPrice.replaceAll("[,\\s₹]", "");
		        System.out.println(normalizedActualPrice);

		        if (actualNamePart.equalsIgnoreCase(expectedNamePart) &&
		            normalizedActualPrice.equals(normalizedExpectedPrice)) {

		            log.ReportEvent("PASS", "Hotel Name and Price matched: " + actualHotelName + " | " + actualPrice);

		            // Get address, star count, and policy
		            String hotelAddress = driver.findElement(By.xpath("(//*[@class='map-hotel-card-content']//h6)[2]")).getText();
		            List<WebElement> starReview = driver.findElements(By.xpath("//*[@class='map-hotel-card-content']//div//p//*[@data-testid]"));
		            int starRatingCount = starReview.size();
		            String policy = driver.findElement(By.xpath("(//*[contains(@class,'hotel-policy')])[2]")).getText();

		            // Populate map
		            hotelDetails.put("hotelName", actualHotelName);
		            hotelDetails.put("hotelAddress", hotelAddress);
		            hotelDetails.put("starRatingCount", starRatingCount);
		            hotelDetails.put("policy", policy);
		            hotelDetails.put("actualPrice", actualPrice);

		            // Click the 'Select' button
		            WebElement selectButton = wait.until(ExpectedConditions.elementToBeClickable(
		                By.xpath("(//button[text()='Select'])[2]")));
		            selectButton.click();

		            return hotelDetails;
		        } else {
		            log.ReportEvent("FAIL", "Hotel Name or Price mismatch.\nExpected: " + expectedHotelName + ", " + expectedPrice +
		                "\nActual: " + actualHotelName + ", " + actualPrice);
		            screenShots.takeScreenShot1();
		            Assert.fail("Hotel Name or Price mismatch.");
		        }

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "Exception in hotel detail validation: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        Assert.fail("Exception during hotel detail validation.");
		        e.printStackTrace();
		    }

		    return null;
		}

}

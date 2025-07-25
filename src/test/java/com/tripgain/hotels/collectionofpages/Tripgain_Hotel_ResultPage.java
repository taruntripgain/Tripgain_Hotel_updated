package com.tripgain.hotels.collectionofpages;

import java.awt.AWTException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import com.tripgain.common.TestExecutionNotifier;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class Tripgain_Hotel_ResultPage {

	WebDriver driver;


	public Tripgain_Hotel_ResultPage(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	@FindBy(xpath="//label[text()='Currency']//parent::div//parent::div//input")
	WebElement currencyDropDown;
	
	@FindBy(xpath="(//label[text()='Search By Hotel, Address']//parent::div//parent::div//input)[1]")
	WebElement searchByHotel;
	
	@FindBy(xpath="//*[contains(@class,'tg-hl-sort')]")
	WebElement sort;
	
	// Method to get flight count on result page
	public void getFlightsCount(Log Log, ScreenShots ScreenShots) {
		try {
			WebElement Flightscount = driver.findElement(By.className("total-hotels-count"));
			String countText = Flightscount.getText();
			Log.ReportEvent("INFO", "Hotel count is: " + countText);
			System.out.println("Hotel count is: " + countText);
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Failed to retrieve flight count: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
	}

	//Method to get the hotels name text for validating result screen is displayed or not 

	public void validateResultScreen(Log Log, ScreenShots ScreenShots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(2));
			WebElement allHotelsGrid = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[@title]")));
			// Check if the hotel grid is displayed
			if (allHotelsGrid.isDisplayed()) {
				Log.ReportEvent("PASS", "Hotel grid is displayed.");
			} else {
				 try {
				        WebElement hotelNotFound = driver.findElement(
				            By.xpath("//*[@data-testid='HotelIcon']/parent::div//h5")
				        );

				        if (hotelNotFound.isDisplayed()) {
				            String message = hotelNotFound.getText();
				            Log.ReportEvent("FAIL", "No hotels found. " + message);
				            ScreenShots.takeScreenShot();
				        } else {
				            Log.ReportEvent("FAIL", "No hotels text is not found.");
				            ScreenShots.takeScreenShot();
				        }

				    } catch (Exception e) {
				        Log.ReportEvent("ERROR", "Hotel-not-found element not found: " + e.getMessage());
				        ScreenShots.takeScreenShot();
				        Assert.fail();
				    } 	
							}

			ScreenShots.takeScreenShot();

			// Get the list of hotel names
			List<WebElement> hotelNames = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-name')]"));

			if (hotelNames.size() > 0) {
				// Collect all hotel names
				List<String> allHotelNames = new ArrayList<>();
				for (WebElement hotel : hotelNames) {
					// allHotelNames.add(hotel.getText());
					Log.ReportEvent("INFO", "Hotel Names: " + hotel.getAttribute("title"));
				}
			} else {
				Log.ReportEvent("FAIL", "No hotel names are displayed.");
				ScreenShots.takeScreenShot();
				Assert.fail();
			}	

		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Exception during validation: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
		
	}



	//Method to get the hotel name in result page 
	public String getHotelNameInResultPage(Log Log, ScreenShots ScreenShots,int hotelCardIndex)
	{
		String hotelName = "";

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//*[contains(normalize-space(@class), 'tg-hl-name')])['"+hotelCardIndex+"']")));
			hotelName = driver.findElement(By.xpath("(//*[contains(normalize-space(@class), 'tg-hl-name')])['"+hotelCardIndex+"']")).getAttribute("title").trim();
			System.out.println("Captured hotel name from search results: " + hotelName);
			Log.ReportEvent("PASS", "Captured hotel name from search results: " + hotelName);
			ScreenShots.takeScreenShot();

		} catch (Exception e) {
			System.out.println("Failed to get hotel name from search results: " + e.getMessage());
			Log.ReportEvent("FAIL", "Failed to get hotel name from search results: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
		return hotelName;
	}

	//Method to click on hotel based on index
	public void selectHotelsBasedOnIndex(int hotelCardIndex) throws TimeoutException {
		try {

			WebElement selectButton = driver.findElement(By.xpath("(//button[text()='Select'])[" + hotelCardIndex + "]"));
			selectButton.click();
			System.out.println("Clicked on the Hotel 'Select' button. Index: " + hotelCardIndex);

		} catch (NoSuchElementException e) {
			System.out.println("No 'Select' button found at index " + hotelCardIndex + ": " + e.getMessage());
		} catch (StaleElementReferenceException e) {
			System.out.println("Stale element reference at index " + hotelCardIndex + ": " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Unexpected error while selecting hotel at index " + hotelCardIndex + ": " + e.getMessage());
		}
	}

	//method to get the hotel name in detail page
	public String getHotelNameInDetailPage(Log Log, ScreenShots ScreenShots) {
		try {
			WebElement hotelNameElement = driver.findElement(By.xpath("//*[contains(normalize-space(@class), 'tg-hl-hotalname')]"));
			String selectedHotel = hotelNameElement.getText().trim();

			if (selectedHotel.isEmpty()) {
				System.out.println("Hotel name on Detail page is empty.");
				ScreenShots.takeScreenShot();
				Log.ReportEvent("FAIL", "Hotel name on Detail page is empty.");
			} else {
				System.out.println("Hotel name on Detail page: " + selectedHotel);
				Log.ReportEvent("PASS", "Hotel name on Detail page: " + selectedHotel);
				ScreenShots.takeScreenShot();
			}
			return selectedHotel;

		} catch (NoSuchElementException e) {
			System.out.println("Hotel name element not found on Detail page: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Log.ReportEvent("FAIL", "Hotel name element not found on Detail page.");
			return "";
		} catch (Exception e) {
			System.out.println("Unexpected error on Hotel Detail page: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Log.ReportEvent("FAIL", "Unexpected error on Hotel Detail page: " + e.getMessage());
			return "";
		}
	}

	//Method to validate hotel name in result page and detail page 
	public void ValidateHotelDetail(String hotelName, String selectedHotel, Log Log, ScreenShots ScreenShots) {
		try {
			if (hotelName.equals(selectedHotel)) {
				System.out.println("Selected Hotel displayed successfully in detail page.");
				Log.ReportEvent("PASS", "Selected Hotel displayed successfully in detail page: " + selectedHotel);
				ScreenShots.takeScreenShot();
			} else {
				System.out.println("Mismatch: Expected [" + hotelName + "], but found [" + selectedHotel + "]");
				Log.ReportEvent("FAIL", "Hotel name mismatch. Expected: " + hotelName + ", Found: " + selectedHotel);
				ScreenShots.takeScreenShot();
				Assert.fail();
			}
		} catch (Exception e) {
			System.out.println("Failed to validate selected hotel: " + e.getMessage());
			Log.ReportEvent("FAIL", "Failed to validate selected hotel: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
	}

	//Method to validate selected hotel data 
	public void validateSelectedHotelPage(Log Log, ScreenShots ScreenShots,int index) {
		String hotelName = "";

		try {
			Thread.sleep(1000);

			hotelName = driver.findElement(By.xpath("(//*[contains(normalize-space(@class), 'tg-hl-name')])["+index+"]")).getText().trim();
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

			driver.findElement(By.xpath("(//button[text()='Select'])["+index+"]")).click();
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

				Log.ReportEvent("PASS", "Selected Hotel displayed successfully in detail page: " + selectedHotel);
			} else {
				System.out.println("Mismatch: Expected [" + hotelName + "], but found [" + selectedHotel + "]");

				Log.ReportEvent("FAIL", "Hotel name mismatch. Expected: " + hotelName + ", Found: " + selectedHotel);
			}
			ScreenShots.takeScreenShot1();

		} catch (Exception e) {
			System.out.println("Failed to validate selected hotel: " + e.getMessage());
			ScreenShots.takeScreenShot1();

			Log.ReportEvent("FAIL", "Failed to validate selected hotel: " + e.getMessage());
			ScreenShots.takeScreenShot1();
		}
	}

	//Method to click on Save and continue button 
	public void clickOnSaveAndContinueButton(Log Log, ScreenShots ScreenShots) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));
			WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//*[contains(normalize-space(@class), 'tg-hl-continue-btn')])[1]")));
			continueButton.click();
		} catch (NoSuchElementException e) {
			Log.ReportEvent("FAIL", "'Save and Continue' button not found: " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail();
		} catch (ElementClickInterceptedException e) {
			Log.ReportEvent("FAIL", "Unable to click 'Save and Continue' button (intercepted): " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail();
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "Unexpected error while clicking 'Save and Continue': " + e.getMessage());
			ScreenShots.takeScreenShot();
			Assert.fail();
		}
	}

	//Method to validate whether review your booking page is displayed
	public void validateReviewYourBookingPage(Log Log, ScreenShots ScreenShots) {
		try {
			Log.ReportEvent("INFO", "Validating Review Your Booking Page...");
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));
			WebElement reviewPage = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("(//*[contains(normalize-space(@class), 'booking-header-title')])[1]")));

			if (reviewPage.isDisplayed()) {
				Log.ReportEvent("PASS", "Review Your Booking Page displayed successfully.");
				ScreenShots.takeScreenShot();
				System.out.println("Review Your Booking Page displayed successfully.");
			} else {
				Log.ReportEvent("FAIL", "Review Your Booking Page is not visible.");
				ScreenShots.takeScreenShot();
                Assert.fail();
				System.out.println("Review Your Booking Page is not visible.");
			}

		} catch (NoSuchElementException e) {
			Log.ReportEvent("FAIL", "Element not found during verification: " + e.getMessage());
			ScreenShots.takeScreenShot();
		} catch (Exception e) {
			Log.ReportEvent("ERROR", "Unexpected error in validateReviewYourBookingPage: " + e.getMessage());
			ScreenShots.takeScreenShot();
		}
	}
	
	public void getTotalHotelCountBeforeApplyingFilter(Log log, ScreenShots screenShots) {
	    try {
	        WebElement countElement = driver.findElement(By.xpath("//*[contains(@class,'tg-hl-total-count')]/p"));
	        String hotelCount = countElement.getText().trim();

	        if (!hotelCount.isEmpty()) {
	            log.ReportEvent("Pass", "Total hotel count Before Applying filter: " + hotelCount);
	        } else {
	            log.ReportEvent("Fail", "Hotel count element is visible but empty.");
	        }

	    } catch (NoSuchElementException e) {
	        log.ReportEvent("Fail", "Hotel count element not found on the page.");
	        
	        screenShots.takeScreenShot();
	    } catch (Exception e) {
	        log.ReportEvent("Fail", "An unexpected error occurred: " + e.getMessage());
	        screenShots.takeScreenShot();
	    }
	}
	
	public void getTotalHotelCountAfterApplyingFilter(Log log, ScreenShots screenShots) {
	    try {
	        WebElement countElement = driver.findElement(By.xpath("//*[contains(@class,'tg-hl-total-count')]/p"));
	        String hotelCount = countElement.getText().trim();

	        if (!hotelCount.isEmpty()) {
	            log.ReportEvent("Pass", "Total hotel count After Applying filter: " + hotelCount);
	        } else {
	            log.ReportEvent("Fail", "Hotel count element is visible but empty.");
	        }

	    } catch (NoSuchElementException e) {
	        log.ReportEvent("Fail", "Hotel count element not found on the page.");
	        
	        screenShots.takeScreenShot();
	    } catch (Exception e) {
	        log.ReportEvent("Fail", "An unexpected error occurred: " + e.getMessage());
	        screenShots.takeScreenShot();
	    }
	}
	
	public void clickOnShowReCommended()
	{
		driver.findElement(By.xpath("//*[text()='Show Recommended']/parent::div/preceding-sibling::span")).click();
	}
	
	public void validateRecommendedFilter(Log log, ScreenShots screenShots) {
	    try {
	        List<WebElement> recommendedTags = driver.findElements(By.xpath("//*[contains(@class,'tg-recommended-tag')]"));
	        
	        if (recommendedTags.isEmpty()) {
	            log.ReportEvent("FAIL", "No recommended tags found on the page.");
	            screenShots.takeScreenShot();
	            return;
	        }

	        boolean allRecommended = true;
	        StringBuilder tagTexts = new StringBuilder();

	        for (WebElement tag : recommendedTags) {
	            String text = tag.getText().trim();
	            tagTexts.append(text).append(", ");
	            
	            if (!text.equalsIgnoreCase("Recommended")) {
	                allRecommended = false;
	            }
	        }

	        if (allRecommended) {
	            log.ReportEvent("Pass", "All hotels are showing tag: Recommended. Tags found: " + tagTexts);
	        } else {
	            log.ReportEvent("FAIL", "Not all hotels are marked as 'Recommended'. Tags found: " + tagTexts);
	            screenShots.takeScreenShot();
	        }
	        
	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "An error occurred while validating recommended tags: " + e.getMessage());
	        screenShots.takeScreenShot();
	    }
	}
	
	    /*
	    public String selectHotelBySearchByHotelName(Log log, ScreenShots screenShots) {
	        try {
	            // Click on the hotel name input to open dropdown
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	            WebElement hotelInput = wait.until(ExpectedConditions.elementToBeClickable(
	                By.xpath("(//input[@class='tg-select__input']/ancestor::div[contains(@class,'tg-select')])[1]")
	            ));
	            hotelInput.click();

	            // Wait for the dropdown menu to appear
	            WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//*[contains(@class,'tg-select__menu ')]")
	            ));

	            // Get the options
	            List<WebElement> options = driver.findElements(By.xpath("//*[@role='option']"));
	            
	            if (!options.isEmpty()) {
	                String hotelName = options.get(0).getText().trim();
	                options.get(0).click();
	                log.ReportEvent("Pass", "Selected hotel name: " + hotelName);
	                System.out.println("Selected hotel name: " + hotelName);
	                return hotelName;
	            } else {
	                log.ReportEvent("FAIL", "No hotel options found in dropdown.");
	                screenShots.takeScreenShot();
	            }

	        } catch (Exception e) {
	            log.ReportEvent("FAIL", "Unexpected error occurred: " + e.getMessage());
	            screenShots.takeScreenShot();
	        }
			return null;
	    }
*/
	public String selectHotelBySearchByHotelName(Log log, ScreenShots screenShots) throws TimeoutException {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        // Click to expand hotel dropdown
	        WebElement hotelInput = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("(//input[@class='tg-select__input']/ancestor::div[contains(@class,'tg-select')])[1]")
	        ));
	        hotelInput.click();

	        // Wait for dropdown menu
	        wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//*[contains(@class,'tg-select__menu ')]")
	        ));

	        // Wait for options to load
	        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
	            By.xpath("//*[@role='option']")
	        ));

	        if (!options.isEmpty()) {
	            String hotelName = options.get(0).getText().trim();
	            options.get(0).click();
	            log.ReportEvent("Pass", "Selected hotel name: " + hotelName);
	            System.out.println("Selected hotel name: " + hotelName);
	            return hotelName;
	        } else {
	            log.ReportEvent("FAIL", "No hotel options found in dropdown.");
	            screenShots.takeScreenShot();
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Unexpected error occurred while selecting hotel: " + e.getMessage());
	        screenShots.takeScreenShot();
	    }
	    return null;
	}

	/*
	    public void validateSearchByHotelNameFilter(String expectedHotelName, Log log, ScreenShots screenShots) throws TimeoutException {
	        try {
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	            WebElement hotelElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//*[contains(@class,'tg-hl-name')]")
	            ));

	            String actualHotelName = hotelElement.getAttribute("title").trim();

	            if (!actualHotelName.isEmpty()) {
	                if (expectedHotelName.equalsIgnoreCase(actualHotelName)) {
	                    log.ReportEvent("Pass", "Correct hotel is displayed: " + actualHotelName);
	                } else {
	                    log.ReportEvent("FAIL", "Expected hotel: " + expectedHotelName + ", but found: " + actualHotelName);
	                    screenShots.takeScreenShot();
	                }
	            } else {
	                log.ReportEvent("FAIL", "Hotel name attribute is empty.");
	                screenShots.takeScreenShot();
	            }

	        } catch (NoSuchElementException e) {
	            log.ReportEvent("FAIL", "Hotel name element not found or not visible.");
	            screenShots.takeScreenShot();
	        } catch (Exception e) {
	            log.ReportEvent("FAIL", "Unexpected error while validating hotel name: " + e.getMessage());
	            screenShots.takeScreenShot();
	        }
	    }
*/
	public void validateSearchByHotelNameFilter(String expectedHotelName, Log log, ScreenShots screenShots) throws TimeoutException {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement hotelElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//*[contains(@class,'tg-hl-name')]")
	        ));

	        String actualHotelName = hotelElement.getAttribute("title").trim();

	        // Remove anything in parentheses from expectedHotelName
	        String cleanedExpectedName = expectedHotelName.split("\\(")[0].trim();

	        if (!actualHotelName.isEmpty()) {
	            if (actualHotelName.equalsIgnoreCase(cleanedExpectedName)) {
	               // log.ReportEvent("Pass", "Correct hotel is displayed: " + actualHotelName);
	            	log.ReportEvent("Pass", "Expected and actual hotel names match: " + cleanedExpectedName);

	            } else {
	                log.ReportEvent("FAIL", "Expected hotel: " + cleanedExpectedName + ", but found: " + actualHotelName);
	                screenShots.takeScreenShot();
	            }
	        } else {
	            log.ReportEvent("FAIL", "Hotel name attribute is empty.");
	            screenShots.takeScreenShot();
	        }

	    } catch (NoSuchElementException e) {
	        log.ReportEvent("FAIL", "Hotel name element not found or not visible.");
	        screenShots.takeScreenShot();
	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Unexpected error while validating hotel name: " + e.getMessage());
	        screenShots.takeScreenShot();
	    }
	}
	
	public String selectHotelBySearchByLocation(Log log, ScreenShots screenShots) throws TimeoutException {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        // Click to expand hotel dropdown
	        WebElement Locations = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//div[text()='Search location']/parent::div/parent::div")
	        ));
	        Locations.click();

	        // Wait for dropdown menu
	        wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//*[contains(@class,'tg-select__menu ')]")
	        ));

	        // Wait for options to load
	        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
	            By.xpath("//*[@role='option']")
	        ));

	        if (!options.isEmpty()) {
	            String hotelName = options.get(0).getText().trim();
	            options.get(0).click();
	            log.ReportEvent("Pass", "Selected hotel name: " + hotelName);
	            System.out.println("Selected hotel name: " + hotelName);
	            return hotelName;
	        } else {
	            log.ReportEvent("FAIL", "No hotel options found in dropdown.");
	            screenShots.takeScreenShot();
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Unexpected error occurred while selecting hotel: " + e.getMessage());
	        screenShots.takeScreenShot();
	    }
	    return null;
	}

	/*
	public double[] adjustMinimumSliderToValue(WebDriver driver, double targetValue) throws TimeoutException {
	    double minValue = -1;
	    double maxValue = -1;

	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        // Get the left (min) thumb input
	        WebElement minSliderInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//input[@data-index='0']")));

	        minValue = Double.parseDouble(minSliderInput.getAttribute("aria-valuemin"));
	        maxValue = Double.parseDouble(minSliderInput.getAttribute("aria-valuemax"));

	        System.out.println("Slider Range: " + minValue + " to " + maxValue);

	        // Clamp the targetValue within slider limits
	        double clampedValue = Math.max(minValue, Math.min(maxValue, targetValue));
	        double percentage = (clampedValue - minValue) / (maxValue - minValue);

	        // Get the slider track
	        WebElement sliderTrack = driver.findElement(By.xpath("//*[contains(@class, 'MuiSlider-track')]"));
	        int trackWidth = sliderTrack.getSize().getWidth();
	        int targetOffset = (int) (trackWidth * percentage);

	        System.out.println("Target Value: " + clampedValue + ", Offset: " + targetOffset + "px");

	        // Get the slider thumb element (data-index='0' is the left handle)
	        WebElement thumbHandle = driver.findElement(By.xpath("//span[@data-index='0' and contains(@class, 'MuiSlider-thumb')]"));
	        int thumbX = thumbHandle.getLocation().getX();
	        int trackX = sliderTrack.getLocation().getX();
	        int currentOffset = thumbX - trackX;

	        int moveBy = targetOffset - currentOffset;

	        System.out.println("Current Offset: " + currentOffset + ", Move By: " + moveBy + "px");

	        // Move the slider using Actions
	        Actions actions = new Actions(driver);
	        actions.moveToElement(thumbHandle)
	                .clickAndHold()
	                .moveByOffset(moveBy, 0)
	                .pause(Duration.ofMillis(200))
	                .release()
	                .perform();

	        // Wait briefly to allow value update
	        Thread.sleep(800);

	        // Verify new value
	        String updatedValue = minSliderInput.getAttribute("aria-valuenow");
	        System.out.println("✅ Updated Minimum Slider Value: " + updatedValue);

	    } catch (NoSuchElementException ne) {
	        System.err.println("❌ Slider element not found: " + ne.getMessage());
	    } catch (InterruptedException ie) {
	        System.err.println("❌ Interrupted while waiting: " + ie.getMessage());
	        Thread.currentThread().interrupt();
	    } catch (Exception e) {
	        System.err.println("❌ Unexpected error: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return new double[]{minValue, maxValue};
	}
*/
	/*
	public void getDefaultValueInSlider(Log log, ScreenShots screenShots) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        // Locate the left (minimum) slider input
	        WebElement minSliderInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//input[@data-index='0']")));

	        // Get slider Price range
	        double minValue = Double.parseDouble(minSliderInput.getAttribute("min"));
	        double maxValue = Double.parseDouble(minSliderInput.getAttribute("max"));

	        System.out.println("Slider Range: " + minValue + " to " + maxValue);
	        log.ReportEvent("INFO", "Default price range of slider: Min Value = " + minValue + ", Max Value = " + maxValue);

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Error fetching default slider values: " + e.getMessage());
	        screenShots.takeScreenShot();
	    }
	    return minValue,maxValue;
	}
*/
	//Method to get Default Value In Slider
	public double[] getDefaultValueInSlider(Log log, ScreenShots screenShots) {
	    double minValue = -1;
	    double maxValue = -1;

	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        // Locate the left (minimum) slider input
	        WebElement minSliderInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//input[@data-index='0']")));

	        // Get slider price range
	        minValue = Double.parseDouble(minSliderInput.getAttribute("min"));
	        maxValue = Double.parseDouble(minSliderInput.getAttribute("max"));

	        System.out.println("Slider Range: " + minValue + " to " + maxValue);
	        log.ReportEvent("INFO", "Default price range of slider: Min Value = " + minValue + ", Max Value = " + maxValue);

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Error fetching default slider values: " + e.getMessage());
	        screenShots.takeScreenShot();
	    }

	    return new double[]{minValue, maxValue};
	}

	//Method to set Minimum Slider Value
	public void setMinimumSliderValue(WebDriver driver, double targetValue) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        // Locate the left (minimum) slider input
	        WebElement minSliderInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//input[@data-index='0']")));

	        // Get slider range
	        double minValue = Double.parseDouble(minSliderInput.getAttribute("min"));
	        double maxValue = Double.parseDouble(minSliderInput.getAttribute("max"));

	        System.out.println("Slider Range: " + minValue + " to " + maxValue);

	        // Clamp target value to be within range
	        double clampedValue = Math.max(minValue, Math.min(maxValue, targetValue));
	        System.out.println("🎯 Target Slider Value (clamped): " + clampedValue);

	        // Use JavaScript to set the slider value
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript(
	                "arguments[0].value = arguments[1];" +
	                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
	                minSliderInput,
	                String.valueOf((int) clampedValue)
	        );

	        // Wait briefly to allow UI update
	        Thread.sleep(500);

	        // Verify updated value
	        String updatedValue = minSliderInput.getAttribute("aria-valuenow");
	        System.out.println("✅ Updated Slider Value: " + updatedValue);

	    } catch (NoSuchElementException e) {
	        System.err.println("❌ Slider input not found: " + e.getMessage());
	    } catch (InterruptedException e) {
	        System.err.println("❌ Thread interrupted: " + e.getMessage());
	        Thread.currentThread().interrupt(); // restore interrupt status
	    } catch (Exception e) {
	        System.err.println("❌ Unexpected error: " + e.getMessage());
	        e.printStackTrace();
	    }
	    
	}
	
	public void setMaximumSliderValue(WebDriver driver, double targetValue) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        // Locate the right (maximum) slider input
	        WebElement maxSliderInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//input[@data-index='1']")));

	        // Get slider range
	        double minValue = Double.parseDouble(maxSliderInput.getAttribute("min"));
	        double maxValue = Double.parseDouble(maxSliderInput.getAttribute("max"));

	        System.out.println("Slider Range: " + minValue + " to " + maxValue);

	        // Clamp target value to be within range
	        double clampedValue = Math.max(minValue, Math.min(maxValue, targetValue));
	        System.out.println("🎯 Target Slider Value (clamped): " + clampedValue);

	        // Use JavaScript to set the slider value
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript(
	                "arguments[0].value = arguments[1];" +
	                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
	                maxSliderInput,
	                String.valueOf((int) clampedValue)
	        );

	        // Wait briefly to allow UI update
	        Thread.sleep(500);

	        // Verify updated value
	        String updatedValue = maxSliderInput.getAttribute("aria-valuenow");
	        System.out.println("✅ Updated Maximum Slider Value: " + updatedValue);

	    } catch (NoSuchElementException e) {
	        System.err.println("❌ Slider input not found: " + e.getMessage());
	    } catch (InterruptedException e) {
	        System.err.println("❌ Thread interrupted: " + e.getMessage());
	        Thread.currentThread().interrupt(); // restore interrupt status
	    } catch (Exception e) {
	        System.err.println("❌ Unexpected error: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	// Method to Validate Price Range Of the Price Filter
	public void verifyPriceRangeValuesOnResultScreen(Log log, ScreenShots screenShots) {
	    try {
	        Thread.sleep(3000); // Let UI settle before reading slider and prices

	        // Read the current slider min and max values from the thumb inputs
	        int min = Integer.parseInt(driver.findElement(By.xpath("//input[@data-index='0']")).getAttribute("aria-valuenow"));
	        int max = Integer.parseInt(driver.findElement(By.xpath("//input[@data-index='1']")).getAttribute("aria-valuenow"));

	        System.out.println("Slider Min: " + min);
	        System.out.println("Slider Max: " + max);

	        // Find all price elements on results page
	        List<WebElement> priceElements = driver.findElements(By.xpath("//*[contains(@class,'hotel-policy-card')]/following-sibling::h5"));

	        boolean allPricesInRange = true;

	        for (WebElement priceElement : priceElements) {
	            String rawPrice = priceElement.getText(); // e.g., "₹ 52,354" or "INR 45,678"
	            System.out.println(rawPrice);
	            // Remove all characters except digits to extract price number only
	            String cleanPrice = rawPrice.replaceAll("[^0-9]", "");
	            System.out.println(cleanPrice);
	            
	            try {
	                int price = Integer.parseInt(cleanPrice);
	                
	                if (price >= min && price <= max) {
	                    System.out.println("Price within range: ₹" + price);
	                } else {
	                    System.out.println("❌ Price out of range: ₹" + price);
	                    allPricesInRange = false;
	                }
	            } catch (NumberFormatException e) {
	                // This means price string couldn't be converted to a number
	                System.out.println("⚠️ Skipping invalid price: " + rawPrice);
	                allPricesInRange = false;
	            }
	        }

	        if (allPricesInRange) {
	            log.ReportEvent("PASS", "✅ All prices are within the range ₹" + min + " - ₹" + max);
	        } else {
	            log.ReportEvent("FAIL", "❌ Some prices are outside the range ₹" + min + " - ₹" + max);
	            Assert.fail("Some prices are out of range.");
	        }

	        screenShots.takeScreenShot1();

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Exception during price validation: " + e.getMessage());
	        screenShots.takeScreenShot1();
	        e.printStackTrace();
	        Assert.fail();
	    }
	}

/*
	public int[] selectPerNightPrice(Log log, ScreenShots screenShots) {
	    try {
	        List<WebElement> priceInputs = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-price-pernight')]/span/input"));
	        
	        if (!priceInputs.isEmpty()) {
	            String priceRangeText = priceInputs.get(0).getAttribute("value").trim(); // e.g., ₹ 1,000 - ₹ 3,000
	            priceInputs.get(0).click();
	            log.ReportEvent("INFO", "Clicked Price Range: " + priceRangeText);

	            // Clean and split the range
	            String cleaned = priceRangeText.replaceAll("[^0-9\\-]", ""); // "1000-3000"
	            String[] parts = cleaned.split("-");
	            
	            if (parts.length == 2) {
	                int minPrice = Integer.parseInt(parts[0]);
	                int maxPrice = Integer.parseInt(parts[1]);
	                return new int[]{minPrice, maxPrice};
	            } else {
	                log.ReportEvent("FAIL", "Invalid price format: " + priceRangeText);
	            }
	        } else {
	            log.ReportEvent("FAIL", "No price range input found.");
	        }
	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Error in selectPerNightPrice(): " + e.getMessage());
	        screenShots.takeScreenShot();
	    }

	    return new int[]{-1, -1}; // default if failure
	}
*/
	//Method to select Per Night Price
	/*
	public int[] selectPerNightPrice(Log log, ScreenShots screenShots) {
	    try {
	        List<WebElement> priceInputs = driver.findElements(
	            By.xpath("//*[contains(@class,'tg-hl-price-pernight')]/span/input")
	        );

	        if (!priceInputs.isEmpty()) {
	            WebElement input = priceInputs.get(0);
	            String rawValue = input.getAttribute("value").trim();
	            input.click();
	            log.ReportEvent("INFO", "Clicked Night Price Range: " + rawValue);

	            int min = -1;
	            int max = -1;

	            if (rawValue.contains("-")) {
	                // Format: ₹ 1,000 - ₹ 3,000
	                String cleaned = rawValue.replaceAll("[₹,\\s]", ""); // e.g., "1000-3000"
	                String[] parts = cleaned.split("-");
	                if (parts.length == 2) {
	                    min = Integer.parseInt(parts[0]);
	                    max = Integer.parseInt(parts[1]);
	                }
	            } else if (rawValue.contains("_")) {
	                // Format: 1000_3000 (fallback format)
	                String[] parts = rawValue.split("_");
	                if (parts.length == 2) {
	                    min = Integer.parseInt(parts[0]);
	                    max = Integer.parseInt(parts[1]);
	                }
	            }

	            if (min != -1 && max != -1) {
	                System.out.println("Parsed Min: " + min + ", Max: " + max);
	                return new int[]{min, max};
	            } else {
	                log.ReportEvent("FAIL", "Invalid price format: " + rawValue);
	            }
	        } else {
	            log.ReportEvent("FAIL", "No price range input found.");
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Error in selectPerNightPrice(): " + e.getMessage());
	        screenShots.takeScreenShot();
	        e.printStackTrace();
	    }

	    return new int[]{-1, -1}; // fallback
	}
*/
	public int[] selectPerNightPrice(Log log, ScreenShots screenShots) {
	    try {
	        List<WebElement> priceInputs = driver.findElements(
	            By.xpath("//*[contains(@class,'tg-hl-price-pernight')]/span/input")
	        );

	        if (priceInputs.isEmpty()) {
	            log.ReportEvent("INFO", "No night price available.");
	            screenShots.takeScreenShot();

	            return new int[]{-1, -1};
	        }

	        WebElement input = priceInputs.get(0);
	        String rawValue = input.getAttribute("value").trim();
	        input.click();
	        log.ReportEvent("INFO", "Clicked Night Price Range: " + rawValue);

	        int min = -1;
	        int max = -1;

	        if (rawValue.contains("-")) {
	            // Format: ₹ 1,000 - ₹ 3,000
	            String cleaned = rawValue.replaceAll("[₹,\\s]", ""); // e.g., "1000-3000"
	            String[] parts = cleaned.split("-");
	            if (parts.length == 2) {
	                min = Integer.parseInt(parts[0]);
	                max = Integer.parseInt(parts[1]);
	            }
	        } else if (rawValue.contains("_")) {
	            // Format: 1000_3000 (fallback format)
	            String[] parts = rawValue.split("_");
	            if (parts.length == 2) {
	                min = Integer.parseInt(parts[0]);
	                max = Integer.parseInt(parts[1]);
	            }
	        }

	        if (min != -1 && max != -1) {
	            System.out.println("Parsed Min: " + min + ", Max: " + max);
	            return new int[]{min, max};
	        } else {
	            log.ReportEvent("FAIL", "Invalid price format: " + rawValue);
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "Error in selectPerNightPrice(): " + e.getMessage());
	        screenShots.takeScreenShot();
	        e.printStackTrace();
	    }

	    return new int[]{-1, -1}; // fallback
	}

	//Method to verify Per Night Price
	public void verifyPerNightPrice(int min, int max, Log log, ScreenShots screenShots) {
	    try {
	        Thread.sleep(2000); // Optional: Wait for UI stability

	        List<WebElement> priceElements = driver.findElements(
	            By.xpath("//*[contains(@class,'tg-hl-pernight-price')]")
	        );

	        if (priceElements.isEmpty()) {
	            log.ReportEvent("FAIL", "❌ No per night prices found on the result screen.");
	            screenShots.takeScreenShot1();
	            Assert.fail("No prices found to validate.");
	            return;
	        }

	        boolean allPricesInRange = true;

	        for (WebElement priceElement : priceElements) {
	            String rawText = priceElement.getText().trim(); // e.g., "Per Night Price:₹ 1,785"
	            System.out.println("Raw Price Text: " + rawText);

	            // Extract numeric part using regex
	            String numberOnly = rawText.replaceAll("[^0-9]", ""); // Removes everything except digits

	            if (numberOnly.isEmpty()) {
	                System.out.println("⚠️ Skipping invalid or empty price: " + rawText);
	                allPricesInRange = false;
	                continue;
	            }

	            try {
	                int price = Integer.parseInt(numberOnly);
	                System.out.println("Parsed Price: ₹" + price);

	                if (price >= min && price <= max) {
	                    System.out.println("✅ Price within range: ₹" + price);
	                } else {
	                    System.out.println("❌ Price out of range: ₹" + price);
	                    allPricesInRange = false;
	                }
	            } catch (NumberFormatException e) {
	                System.out.println("⚠️ Failed to parse number from: " + rawText);
	                allPricesInRange = false;
	            }
	        }

	        if (allPricesInRange) {
	            log.ReportEvent("PASS", "✅ All prices are within the expected range: ₹" + min + " - ₹" + max);
	        } else {
	            log.ReportEvent("FAIL", "❌ Some prices are outside the expected range: ₹" + min + " - ₹" + max);
	            Assert.fail("Price validation failed.");
	        }

	        screenShots.takeScreenShot1();

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "❌ Exception during price validation: " + e.getMessage());
	        screenShots.takeScreenShot1();
	        e.printStackTrace();
	        Assert.fail();
	    }
	}

	public void clickOnInPolicy() {
	    try {
	        // Locate the checkbox/input element
	        WebElement checkbox = driver.findElement(By.xpath("//*[text()='Show In Policy']/parent::div/preceding-sibling::span/input"));

	        // Scroll into view using JavaScript
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", checkbox);

	        // Optional: Wait a bit for scrolling to finish
	        Thread.sleep(500);

	        // Click the checkbox
	        checkbox.click();

	        System.out.println("✅ Successfully clicked on 'Show In Policy' checkbox.");

	    } catch (Exception e) {
	        System.err.println("❌ Error while clicking 'Show In Policy': " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	public void clickOnOutOfPolicy() {
	    try {
	        // Locate the checkbox/input element
	        WebElement checkbox = driver.findElement(By.xpath("//*[text()='Show Out Of Policy']/parent::div/preceding-sibling::span/input"));

	        // Scroll into view using JavaScript
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", checkbox);

	        // Optional: Wait a bit for scrolling to finish
	        Thread.sleep(500);

	        // Click the checkbox
	        checkbox.click();

	        System.out.println("✅ Successfully clicked on 'Show In Policy' checkbox.");

	    } catch (Exception e) {
	        System.err.println("❌ Error while clicking 'Show In Policy': " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	public void clickOnShowCorporatedContract() {
	    try {
	        // Locate the checkbox/input element
	        WebElement checkbox = driver.findElement(By.xpath("//*[text()='Show Corporate Contracted']/parent::div/preceding-sibling::span/input"));

	        // Scroll into view using JavaScript
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", checkbox);

	        // Optional: Wait a bit for scrolling to finish
	        Thread.sleep(500);

	        // Click the checkbox
	        checkbox.click();

	        System.out.println("✅ Successfully clicked on 'Show In Policy' checkbox.");

	    } catch (Exception e) {
	        System.err.println("❌ Error while clicking 'Show In Policy': " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	public void validatePolicy(Log Log, ScreenShots ScreenShots, String expectedValue) {
		try {
			// Wait until at least one policy element is present
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.or(
					ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@data-tginpolicy]")),
					ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@data-tgoutpolicy]"))
					));
 
			// Re-locate all policy elements freshly (avoid stale)
			List<WebElement> inPolicyElements = driver.findElements(By.xpath("//*[@data-tginpolicy]"));
			List<WebElement> outOfPolicyElements = driver.findElements(By.xpath("//*[@data-tgoutpolicy]"));
 
			List<WebElement> allPolicies = new ArrayList<>();
			allPolicies.addAll(inPolicyElements);
			allPolicies.addAll(outOfPolicyElements);
 
			if (allPolicies.isEmpty()) {
				Log.ReportEvent("FAIL", "❌ No policy elements found.");
				ScreenShots.takeScreenShot1();
				Assert.fail("No policy elements to validate.");
				return;
			}
 
			List<String> mismatchedPolicies = new ArrayList<>();
			boolean allMatch = true;
 
			// Re-fetch text inside loop to avoid stale elements
			for (int i = 0; i < allPolicies.size(); i++) {
				try {
					WebElement freshPolicy = allPolicies.get(i);
					String policyText = freshPolicy.getText().trim();
					System.out.println("Policy Text: " + policyText);
 
					if (!policyText.equalsIgnoreCase(expectedValue)) {
						mismatchedPolicies.add(policyText);
						allMatch = false;
					}
				} catch (StaleElementReferenceException se) {
					// Try re-fetching if stale
					allPolicies = driver.findElements(By.xpath("//div[@[@data-tginpolicy] or @[@data-tgoutpolicy]"));
					i--; // retry the same index
				}
			}
 
			if (allMatch) {
				Log.ReportEvent("PASS", "✅ All displayed policies match: '" + expectedValue + "'");
			} else {
				Log.ReportEvent("FAIL", "❌ Some policies do not match '" + expectedValue + "'. Mismatched: " + mismatchedPolicies);
				Assert.fail("One or more policies do not match expected value.");
			}
 
			ScreenShots.takeScreenShot1();
 
		} catch (Exception e) {
			Log.ReportEvent("FAIL", "❌ Exception while validating policy: " + e.getMessage());
			ScreenShots.takeScreenShot1();
			e.printStackTrace();
			Assert.fail("Exception during policy validation.");
		}
	}
	
	//Method to click On Ratings
	public void clickOnRating(int rating) {
	    try {
	        // Locate the rating input element based on its value
	        WebElement ratingElement = driver.findElement(By.xpath("//input[@value='" + rating + "']"));

	        // Scroll it into view
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", ratingElement);

	        // Optional wait to allow scroll to complete
	        Thread.sleep(500);

	        // Click the rating input
	        ratingElement.click();

	        // Log the selected rating
	        System.out.println("✅ Clicked on rating: " + rating + " star(s)");

	    } catch (Exception e) {
	        System.err.println("❌ Error clicking on rating " + rating + ": " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	public void ratingBasedOnIndex(int index, int rating, Log log, ScreenShots screenShots) {
	    try {
	        // Find star rating elements for the given index
	        List<WebElement> starRatingCount = driver.findElements(
	                By.xpath("(//*[contains(@class,'tg-hl-amenity')]/preceding-sibling::p)[" + index + "]/*"));

	        if (starRatingCount != null && !starRatingCount.isEmpty()) {
	            int starRating = starRatingCount.size();

	            System.out.println("⭐ Star Count at index " + index + ": " + starRating);

	            if (starRating == rating) {
	                log.ReportEvent("PASS", "✅ Selected rating matches and is displayed: " + rating + " stars.");
	            } else {
	                log.ReportEvent("FAIL", "❌ Expected rating: " + rating + " stars, but found: " + starRating);
	                screenShots.takeScreenShot1();
	            }
	        } else {
	            log.ReportEvent("FAIL", "❌ No star elements found at index " + index);
	            screenShots.takeScreenShot1();
	        }
	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "❌ Exception in ratingBasedOnIndex(): " + e.getMessage());
	        screenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}
	
	//Method to validate Star Rating
	public void validateStarRating(int expectedStarCount, Log log, ScreenShots screenShots) {
	    try {
	        List<WebElement> hotelCards = driver.findElements(By.xpath("//*[contains(@class,'hcard')]"));

	        if (hotelCards.isEmpty()) {
	            log.ReportEvent("FAIL", "❌ No hotel cards found on the page.");
	            screenShots.takeScreenShot1();
	            return;
	        }

	        boolean allMatch = true;

	        for (int i = 0; i < hotelCards.size(); i++) {
	            WebElement card = hotelCards.get(i);

	            // Get stars in this card only
	            List<WebElement> starElements = card.findElements(By.xpath(".//*[@data-testid='StarIcon']"));
	            int actualStarCount = starElements.size();

	            System.out.println("⭐ Card index " + i + " has " + actualStarCount + " stars");

	            if (actualStarCount != expectedStarCount) {
	                allMatch = false;
	                log.ReportEvent("FAIL", "❌ Card at index " + i + " has " + actualStarCount + " stars. Expected only: " + expectedStarCount);
	                screenShots.takeScreenShot1();
	            }
	        }

	        if (allMatch) {
	            log.ReportEvent("PASS", "✅ All displayed hotel cards have exactly " + expectedStarCount + " stars.");
	        } else {
	            log.ReportEvent("FAIL", "❌ Some hotel cards do not have the expected " + expectedStarCount + " stars.");
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "❌ Exception occurred during star rating validation: " + e.getMessage());
	        screenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}



	public void clickOnUserRating(String rating) {
	    try {
	        // Locate the checkbox/input element
	        WebElement checkbox = driver.findElement(By.xpath("//*[text()='"+rating+"']/parent::div/preceding-sibling::span/input"));

	        // Scroll into view using JavaScript
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", checkbox);

	        // Optional: Wait a bit for scrolling to finish
	        Thread.sleep(500);

	        // Click the checkbox
	        checkbox.click();

	        System.out.println("✅ Successfully clicked on 'Show In Policy' checkbox.");

	    } catch (Exception e) {
	        System.err.println("❌ Error while clicking 'Show In Policy': " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	/*
	public void validateUserRating(int expectedRating, Log log, ScreenShots screenShots) {
	    try {
	        List<WebElement> ratingElements = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-rating-1')]"));

	        if (ratingElements == null || ratingElements.isEmpty()) {
	            log.ReportEvent("FAIL", "❌ No rating elements found on the page.");
	            screenShots.takeScreenShot1();
	            return;
	        }

	        boolean allRatingsValid = true;

	        for (WebElement rate : ratingElements) {
	            String rawRatingText = rate.getText().trim(); // e.g., "4.7"
	            if (rawRatingText.isEmpty()) {
	                log.ReportEvent("INFO", "⚠️ Empty rating text found, skipping...");
	                continue;
	            }

	            try {
	                double actualRating = Double.parseDouble(rawRatingText); // e.g., 4.7
	                int roundedRating = (int) Math.floor(actualRating); // e.g., 4.7 → 4

	                System.out.println("⭐ Actual Rating Parsed: " + actualRating + ", Rounded: " + roundedRating);

	                if (roundedRating >= expectedRating) {
	                    log.ReportEvent("PASS", "✅ Rating " + actualRating + " meets or exceeds expected: " + expectedRating);
	                } else {
	                    log.ReportEvent("FAIL", "❌ Rating " + actualRating + " is below expected: " + expectedRating);
	                    allRatingsValid = false;
	                }

	            } catch (NumberFormatException e) {
	                log.ReportEvent("FAIL", "❌ Unable to parse rating from text: " + rawRatingText);
	                allRatingsValid = false;
	            }
	        }

	        if (!allRatingsValid) {
	            screenShots.takeScreenShot1();
	            Assert.fail("One or more ratings did not meet the expected value.");
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "❌ Exception in validateUserRating(): " + e.getMessage());
	        screenShots.takeScreenShot1();
	        e.printStackTrace();
	        Assert.fail();
	    }
	}
	*/

	//Method To validate User Rating
	/*
public void validateUserRating(double expectedRating, Log log, ScreenShots screenShots) {
    try {
        List<WebElement> ratingElements = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-rating-1')]"));

        if (ratingElements == null || ratingElements.isEmpty()) {
            log.ReportEvent("FAIL", "❌ No rating elements found on the page.");
            screenShots.takeScreenShot1();
            return;
        }

        boolean allRatingsValid = true;

        for (WebElement rate : ratingElements) {
            String rawRatingText = rate.getText().trim(); // e.g., "4.7"
            if (rawRatingText.isEmpty()) {
                log.ReportEvent("INFO", "⚠️ Empty rating text found, skipping...");
                Assert.fail();
                continue;
                
            }

            try {
                double actualRating = Double.parseDouble(rawRatingText); // e.g., 4.7

                System.out.println("⭐ Actual Rating Parsed: " + actualRating);

                if (actualRating >= expectedRating) {
                    log.ReportEvent("PASS", "✅ Rating " + actualRating + " meets or exceeds expected: " + expectedRating);
                } else {
                    log.ReportEvent("FAIL", "❌ Rating " + actualRating + " is below expected: " + expectedRating);
                    allRatingsValid = false;
                }

            } catch (NumberFormatException e) {
                log.ReportEvent("FAIL", "❌ Unable to parse rating from text: " + rawRatingText);
                allRatingsValid = false;
            }
        }

        if (!allRatingsValid) {
            screenShots.takeScreenShot1();
            Assert.fail("❌ One or more ratings did not meet the expected value.");
        }

    } catch (Exception e) {
        log.ReportEvent("FAIL", "❌ Exception in validateUserRating(): " + e.getMessage());
        screenShots.takeScreenShot1();
        e.printStackTrace();
        Assert.fail();
    }
}
*/
	public void validateUserRating(double expectedRating, Log log, ScreenShots screenShots) {
	    try {
	        List<WebElement> ratingElements = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-rating-1')]"));

	        if (ratingElements.isEmpty()) {
	            log.ReportEvent("FAIL", "❌ No rating elements found on the page.");
	            screenShots.takeScreenShot1();
	            Assert.fail("No rating elements found.");
	            return;
	        }

	        boolean allRatingsValid = true;

	        for (WebElement rate : ratingElements) {
	            String rawRatingText = rate.getText().trim(); // e.g., "4.7"

	            if (rawRatingText.isEmpty()) {
	                log.ReportEvent("WARN", "⚠️ Empty rating text found for an element. Skipping...");
	                continue;
	            }

	            try {
	                double actualRating = Double.parseDouble(rawRatingText);
	                System.out.println("⭐ Parsed Rating: " + actualRating);

	                if (actualRating >= expectedRating) {
	                    log.ReportEvent("PASS", "✅ Rating " + actualRating + " meets or exceeds expected: " + expectedRating);
	                } else {
	                    log.ReportEvent("FAIL", "❌ Rating " + actualRating + " is below expected: " + expectedRating);
	                    allRatingsValid = false;
	                }

	            } catch (NumberFormatException e) {
	                log.ReportEvent("FAIL", "❌ Unable to parse rating from text: '" + rawRatingText + "'");
	                allRatingsValid = false;
	            }
	        }

	        if (!allRatingsValid) {
	            screenShots.takeScreenShot1();
	            Assert.fail("❌ One or more ratings did not meet the expected value of " + expectedRating);
	        }

	    } catch (Exception e) {
	        log.ReportEvent("FAIL", "❌ Exception in validateUserRating(): " + e.getMessage());
	        screenShots.takeScreenShot1();
	        e.printStackTrace();
	        Assert.fail("Exception occurred: " + e.getMessage());
	    }
	}

	//Method to click On Amenities
	public void clickOnAmenities(String value) {
	    try {
	        if (value == null || value.trim().isEmpty()) {
	            System.err.println("❌ Invalid amenity value provided.");
	            return;
	        }

	        // Locate the checkbox/input element by value
	        WebElement checkbox = driver.findElement(By.xpath("//input[@value='" + value + "']"));

	        // Scroll into view smoothly
	        ((JavascriptExecutor) driver).executeScript(
	            "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", checkbox
	        );

	        Thread.sleep(500); // optional wait for smooth scroll

	        // Click the checkbox
	        checkbox.click();

	        System.out.println("✅ Successfully clicked on amenity: '" + value + "'");

	    } catch (Exception e) {
	        System.err.println("❌ Error while clicking amenity: '" + value + "'. Message: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	//Method to get random amenities
	public String getAmenitiesRandomly() {
	    List<WebElement> amenities = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-amenities-filter')]/span/following-sibling::span"));

	    if (amenities.isEmpty()) {
	        return null; // or throw an exception or return a default value
	    }

	    Random rand = new Random();
	    int randomIndex = rand.nextInt(amenities.size());
	    WebElement amenity = amenities.get(randomIndex);

	    return amenity.getText();
	}

	//Method to validate default Currency Value
	public void defaultCurrencyValue(Log Log, ScreenShots screenShots) {
	    try {
	        // Step 1: Get the default currency from the dropdown
	        String defaultCurrency = driver.findElement(
	            By.xpath("//*[contains(@class, 'tg-currency-change')]//*[contains(@class, 'tg-select__single-value')]")
	        ).getText().trim();

	        String expectedSymbol = getCurrencySymbol(defaultCurrency); // Helper method below

	        if (expectedSymbol != null) {
	            Log.ReportEvent("PASS", "Default currency value displayed: " + defaultCurrency);
	          
	            // Step 2: Validate all 'FROM' prices
	            List<WebElement> fromPrices = driver.findElements(By.xpath("//*[contains(@class, 'tg-fromprice')]"));
	            boolean allFromValid = validateCurrencyPrices(fromPrices, expectedSymbol, "FROM", Log);

	            // Step 3: Validate all 'TO' prices
	            List<WebElement> toPrices = driver.findElements(By.xpath("//*[contains(@class, 'tg-toprice')]"));
	            boolean allToValid = validateCurrencyPrices(toPrices, expectedSymbol, "TO", Log);

	            // Step 4: Final check
	            if (!allFromValid || !allToValid) {
	                Assert.fail("One or more prices are not in the expected currency format.");
	                screenShots.takeScreenShot1();
	            }

	        } else {
	            Log.ReportEvent("FAIL", "Unsupported or unexpected default currency: " + defaultCurrency);
	            screenShots.takeScreenShot1();
	            Assert.fail("No symbol found for default currency: " + defaultCurrency);
	            
	        }

	        screenShots.takeScreenShot1(); // Always take screenshot

	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "Exception while validating currency: " + e.getMessage());
	        screenShots.takeScreenShot1();
	        e.printStackTrace();
	        Assert.fail("Exception occurred during currency validation");
	    }
	}
	private String getCurrencySymbol(String currencyCode) {
	    switch (currencyCode) {
	        case "INR": return "₹";
	        case "USD": return "$";
	        case "EUR": return "€";
	        // Add more currencies as needed
	        default: return null;
	    }
	}
	private boolean validateCurrencyPrices(List<WebElement> prices, String expectedSymbol, String label, Log Log) {
	    boolean allValid = true;

	    for (WebElement priceElement : prices) {
	        String value = priceElement.getText().trim();
	        System.out.println(label + " Price Displayed: " + value);

	        if (value.startsWith(expectedSymbol)) {
	            System.out.println("✅ Valid " + label + " price: " + value);
	        } else {
	            System.out.println("❌ Invalid " + label + " price: " + value);
	            Log.ReportEvent("FAIL", "Invalid " + label + " currency value: " + value);
	            allValid = false;
	        }
	    }

	    return allValid;
	}
	//Method to click On Currency DropDown
		public void clickOnCurrencyDropDown() throws InterruptedException {
			Thread.sleep(2000);
			//  driver.findElement(By.xpath("//label[text()='Currency']//parent::div//parent::div//input")).click();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0, -document.body.scrollHeight)");
			Thread.sleep(2000);
			currencyDropDown.click();
		}
		
		//method to select Currency Drop Down Values
		public void selectCurrencyDropDownValues(WebDriver driver,String value) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

			System.out.println(value);
			WebElement CurrencyValue = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[text()='"+value+"']")
					));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			// 🛠️ FIXED: "argument" → "arguments"
			js.executeScript("arguments[0].scrollIntoView(true);", CurrencyValue);

			wait.until(ExpectedConditions.elementToBeClickable(CurrencyValue)).click();
		}
		
		//Method to validate Currency
		public void validateCurrency(String currencyValue, Log Log, ScreenShots ScreenShots) {
	        try {
	            Thread.sleep(6000);
	            
	            // Use CSS selector for elements with both classes
	            List<WebElement> currencyTexts = driver.findElements(By.xpath("//*[contains(@class,'other-currency-price')]"));
	            
	            for (WebElement currencyText : currencyTexts) {
	                System.out.println(currencyText.getText());
	                currencyText.getText().contains(currencyValue);
	            }
	            
	            String currencyData = currencyTexts.get(0).getText();
	            String currencyCode = currencyData.substring(0, 3);
	            System.out.println(currencyCode);
	            System.out.println(currencyValue);

	            if (currencyValue.contentEquals(currencyCode)) {
	                Log.ReportEvent("PASS", "Currencies are Displayed Based on User Search " + currencyCode + " is Successful");
	               
	            } else {
	                Log.ReportEvent("FAIL", "Currencies are Not Displayed Based on User " + currencyCode);
	                ScreenShots.takeScreenShot1();
	                Assert.fail();
	            }
	        } catch (Exception e) {
	            Log.ReportEvent("FAIL", "Currencies are Not Displayed Based on User Search: " + e.getMessage());
	            ScreenShots.takeScreenShot1();
	            Assert.fail();
	        }
	    }
		
		//Method to click On Search By Hotel
		public void clickOnSearchByHotel()
		{
			searchByHotel.click();
		}
		
		//Method to Search By Hotel
		public void SearchByHotel(String location)
		{
			searchByHotel.sendKeys(location);
		}
		//Method to Search By Hotel Clear And Search
		public void SearchByHotelClearAndSearch(String locations) throws InterruptedException
		{
			
		    searchByHotel.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
			Thread.sleep(2000);
			searchByHotel.sendKeys(locations);
		}
		
		//Method to get Hotel Name
		public String getHotelName() {
		    try {
		        WebElement hotelElement = driver.findElement(By.xpath("//*[contains(@class,'tg-hl-name')]"));
		        String hotelName = hotelElement.getAttribute("title").trim();
		        System.out.println("✅ Hotel Name: " + hotelName);
		        return hotelName;
		    } catch (Exception e) {
		        System.err.println("❌ Failed to fetch hotel name: " + e.getMessage());
		        return null;
		    }
		}

		//Method to validate Hotel Name
		public void validateHotelName(String expectedHotelName, Log log, ScreenShots screenShots) {
		    try {
		        WebElement hotelElement = driver.findElement(By.xpath("//*[contains(@class,'tg-hl-name')]"));
		        String actualHotelName = hotelElement.getAttribute("title").trim();
		        System.out.println("✅ Actual Hotel Name: " + actualHotelName);

		        if (expectedHotelName.equalsIgnoreCase(actualHotelName)) {
		            log.ReportEvent("PASS", "✅ User Entered  Hotel name matches: " + actualHotelName);
		        } else {
		            log.ReportEvent("FAIL", "❌ User Entered Hotel name mismatch. Expected: " + expectedHotelName + ", Found: " + actualHotelName);
		            screenShots.takeScreenShot1();
		            Assert.fail("Hotel name does not match.");
		        }

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "❌ Exception while validating hotel name: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        e.printStackTrace();
		        Assert.fail("Exception during hotel name validation.");
		    }
		}
		
		//Method to validate Hotel Address
		public void validateHotelAddress(String expectedHotelName, Log log, ScreenShots screenShots) {
		    try {
		        List<WebElement> hotelAddresses = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-address')]"));
		        boolean matchFound = false;

		        for (WebElement addressElement : hotelAddresses) {
		            String actualAddress = addressElement.getText().trim();

		            if (actualAddress.contains(expectedHotelName)) {
		                log.ReportEvent("PASS", "✅ User Entered  Hotel name found in address: " + actualAddress);
		                matchFound = true;
		                break; // no need to check further
		            }
		        }
               
		        
		         if (!matchFound) {
		            log.ReportEvent("FAIL", "❌ User Entered Hotel name not found in any address. Expected name: " + expectedHotelName);
		            screenShots.takeScreenShot1();
		            Assert.fail(" User Entered Hotel name not found in address list.");
		        }

		    } catch (Exception e) {
		        log.ReportEvent("FAIL", "❌ Exception during hotel address validation: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        e.printStackTrace();
		        Assert.fail("Exception during hotel address validation.");
		    }
		}

		//Method to click On Sort Component
		public void clickOnSort()
		{
			sort.click();
		}
		
		//Method to select Value In Sort DropDown
		public void checkOnSortOptions(String value) {
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		    
		    try {
		        WebElement menuOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@role='listbox']")));
		        
		        WebElement desiredOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[text()='" + value + "']")));
		        desiredOption.click();
		        
		    } catch (NoSuchElementException e) {
		        System.out.println("The option '" + value + "' was not found in the list.");
		    }
		}
		/*
		public void validateHotelSortingAscending() {
		   

		    // Step 1: Wait for the new hotel list to load (better than Thread.sleep)
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(@class,'tg-hl-name')]")));

		    // Step 2: Re-fetch the elements AFTER waiting
		    List<WebElement> hotelElements = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-name')]"));

		    List<String> actualList = new ArrayList<>();
		    for (WebElement hotel : hotelElements) {
		        actualList.add(hotel.getText().trim());
		    }

		    // Step 3: Create sorted copy and compare
		    List<String> sortedList = new ArrayList<>(actualList);
		    Collections.sort(sortedList);

		    if (actualList.equals(sortedList)) {
		        System.out.println("✅ Hotel names are sorted in Ascending order.");
		        System.out.println("Expected: " + sortedList);
		        System.out.println("Actual:   " + actualList);
		    } else {
		        System.out.println("❌ Hotel names are NOT sorted in Ascending order.");
		        System.out.println("Expected: " + sortedList);
		        System.out.println("Actual:   " + actualList);
		        Assert.fail("Hotel names are not in ascending order.");
		    }
		}
*/
		/*
		public void validateHotelSortingAscending(Log log, ScreenShots screenShots) {
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    By hotelNameLocator = By.xpath("//*[contains(@class,'tg-hl-name')]");

		    // Step 1: Wait until hotel names are visible
		    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(hotelNameLocator));

		    // Step 2: Initialize actual list
		    List<String> actualList = new ArrayList<>();

		    try {
		        // Step 3: Fetch elements and get text immediately to avoid stale reference
		        List<WebElement> hotelElements = driver.findElements(hotelNameLocator);
		        for (WebElement hotel : hotelElements) {
		            actualList.add(hotel.getText().trim());
		        }
		    } catch (StaleElementReferenceException e) {
		        // Step 4: Retry once if stale exception occurs
		        System.out.println("⚠️ StaleElementReferenceException occurred. Retrying fetch...");
		        actualList.clear(); // clear previous partial results
		        List<WebElement> hotelElements = driver.findElements(hotelNameLocator);
		        for (WebElement hotel : hotelElements) {
		            actualList.add(hotel.getText().trim());
		        }
		    }

		    // Step 5: Create a sorted copy for comparison
		    List<String> expectedSortedList = new ArrayList<>(actualList);
		    Collections.sort(expectedSortedList);

		    // Step 6: Compare and log result
		    if (actualList.equals(expectedSortedList)) {
		        System.out.println("✅ Hotel names are sorted in Ascending order.");
		        System.out.println("Expected: " + expectedSortedList);
		        System.out.println("Actual:   " + actualList);
		        log.ReportEvent("FAIL", "❌ Hotel name mismatch. Expected: " + expectedHotelName + ", Found: " + actualHotelName);
	            screenShots.takeScreenShot1();
	            Assert.fail("Hotel name does not match.");
		    } else {
		        System.out.println("❌ Hotel names are NOT sorted in Ascending order.");
		        System.out.println("Expected: " + expectedSortedList);
		        System.out.println("Actual:   " + actualList);
		        Assert.fail("Hotel names are not in ascending order.");
		        log.ReportEvent("FAIL", "❌ Hotel name mismatch. Expected: " + expectedHotelName + ", Found: " + actualHotelName);
	            screenShots.takeScreenShot1();
	            Assert.fail("Hotel name does not match.");
		    }
		}
		*/
		//Method to validate Hotel Sorting Ascending
		public void validateHotelSortingAscending(Log log, ScreenShots screenShots) throws InterruptedException {
			Thread.sleep(3000);
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    By hotelNameLocator = By.xpath("//*[contains(@class,'tg-hl-name')]");

		    // Step 1: Wait for hotel names to be visible
		    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(hotelNameLocator));

		    List<String> actualList = new ArrayList<>();

		    try {
		        // Step 2: Fetch elements and get hotel names
		        List<WebElement> hotelElements = driver.findElements(hotelNameLocator);
		        for (WebElement hotel : hotelElements) {
		            actualList.add(hotel.getText().trim());
		        }
		    } catch (StaleElementReferenceException e) {
		        System.out.println("⚠️ StaleElementReferenceException occurred. Retrying fetch...");
		        actualList.clear();
		        List<WebElement> hotelElements = driver.findElements(hotelNameLocator);
		        for (WebElement hotel : hotelElements) {
		            actualList.add(hotel.getText().trim());
		        }
		    }

		    // Step 3: Sort the actual list to get the expected sorted order
		    List<String> expectedSortedList = new ArrayList<>(actualList);
		    Collections.sort(expectedSortedList);

		    // Step 4: Compare actual vs expected
		    if (actualList.equals(expectedSortedList)) {
		        System.out.println("✅ Hotel names are sorted in Ascending order.");
		        System.out.println("Expected: " + expectedSortedList);
		        System.out.println("Actual:   " + actualList);
		        log.ReportEvent("PASS", "✅ Hotel names are sorted in ascending order as expected.");
		    } else {
		        System.out.println("❌ Hotel names are NOT sorted in Ascending order.");
		        System.out.println("Expected: " + expectedSortedList);
		        System.out.println("Actual:   " + actualList);

		        // Log the mismatch
		        log.ReportEvent("FAIL", "❌ Hotel names are NOT sorted in ascending order. \nExpected: " 
		            + expectedSortedList + "\nActual: " + actualList);

		        // Take a screenshot
		        screenShots.takeScreenShot1();

		        // Fail the test
		        Assert.fail("Hotel names are not in ascending order.");
		    }
		}
		//Method to validate Hotel Sorting Descending
		public void validateHotelSortingDescending(Log log, ScreenShots screenShots) throws InterruptedException {
			Thread.sleep(3000);
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    By hotelNameLocator = By.xpath("//*[contains(@class,'tg-hl-name')]");

		    // Step 1: Wait for hotel names to become visible
		    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(hotelNameLocator));

		    List<String> actualList = new ArrayList<>();

		    try {
		        // Step 2: Fetch hotel elements and extract names
		        List<WebElement> hotelElements = driver.findElements(hotelNameLocator);
		        for (WebElement hotel : hotelElements) {
		            actualList.add(hotel.getText().trim());
		        }
		    } catch (StaleElementReferenceException e) {
		        // Step 3: Retry once in case of stale elements
		        System.out.println("⚠️ StaleElementReferenceException occurred. Retrying fetch...");
		        actualList.clear();
		        List<WebElement> hotelElements = driver.findElements(hotelNameLocator);
		        for (WebElement hotel : hotelElements) {
		            actualList.add(hotel.getText().trim());
		        }
		    }

		    // Step 4: Sort the actual list in descending order to create expected list
		    List<String> expectedSortedList = new ArrayList<>(actualList);
		    expectedSortedList.sort(Collections.reverseOrder());

		    // Step 5: Compare actual with expected sorted list
		    if (actualList.equals(expectedSortedList)) {
		        System.out.println("✅ Hotel names are sorted in Descending order.");
		        System.out.println("Expected: " + expectedSortedList);
		        System.out.println("Actual:   " + actualList);
		        log.ReportEvent("PASS", "✅ Hotel names are sorted in descending order as expected.");
		    } else {
		        System.out.println("❌ Hotel names are NOT sorted in Descending order.");
		        System.out.println("Expected: " + expectedSortedList);
		        System.out.println("Actual:   " + actualList);

		        log.ReportEvent("FAIL", "❌ Hotel names are NOT sorted in descending order.\nExpected: "
		                + expectedSortedList + "\nActual: " + actualList);

		        screenShots.takeScreenShot1();
		        Assert.fail("Hotel names are not in descending order.");
		    }
		}

		//Method to validate Price In Ascending
		public void validatePriceInAscending(Log log, ScreenShots screenShots) throws InterruptedException {
			Thread.sleep(5000);
		    // Extract price elements
		    List<WebElement> priceElements = driver.findElements(By.xpath("//*[contains(@class,'tg-policy')]/following-sibling::h5"));
		    int priceNumber = priceElements.size();

		    List<Integer> actualList = new ArrayList<>();

		    try {
		        // Convert prices to integers and store in a list
		        for (WebElement priceElement : priceElements) {
		            String priceText = priceElement.getText();         // e.g. "₹ 1,371"
		            String priceNumberStr = priceText.split("₹")[1]    // " 1,371"
		                                      .replace(",", "")       // "1371"
		                                      .trim();                // "1371"
		            actualList.add(Integer.parseInt(priceNumberStr));
		        }
		    } catch (StaleElementReferenceException e) {
		        System.out.println("⚠️ StaleElementReferenceException occurred. Retrying fetch...");
		        actualList.clear();

		        // Retry fetching elements and values
		        priceElements = driver.findElements(By.xpath("//*[contains(@class,'tg-policy')]/following-sibling::h5"));
		        for (WebElement priceElement : priceElements) {
		            String priceText = priceElement.getText();
		            String priceNumberStr = priceText.split("₹")[1]
		                                      .replace(",", "")
		                                      .trim();
		            actualList.add(Integer.parseInt(priceNumberStr));
		        }
		    }

		    // Copy actual list for sorting
		    List<Integer> sortedList = new ArrayList<>(actualList);

		    // Bubble sort on sortedList
		    for (int i = 0; i < priceNumber - 1; i++) {
		        for (int j = 0; j < priceNumber - i - 1; j++) {
		            if (sortedList.get(j) > sortedList.get(j + 1)) {
		                // Swap
		                int temp = sortedList.get(j);
		                sortedList.set(j, sortedList.get(j + 1));
		                sortedList.set(j + 1, temp);
		            }
		        }
		    }

		    // Compare actual vs sorted
		    if (actualList.equals(sortedList)) {
		        System.out.println("✅ Prices are sorted in ascending order.");
		        log.ReportEvent("PASS", "Prices are sorted in ascending order.");
		    } else {
		        System.out.println("❌ Prices are NOT sorted in ascending order.");
		        System.out.println("Actual: " + actualList);
		        System.out.println("Expected (sorted): " + sortedList);

		        log.ReportEvent("FAIL", "Prices are NOT sorted ascending. \nActual: " + actualList + "\nExpected: " + sortedList);
		        screenShots.takeScreenShot1();
		        Assert.fail("Prices are not sorted in ascending order.");
		    }
		}
		//Method to validate Price In Descending
		public void validatePriceInDescending(Log log, ScreenShots screenShots) throws InterruptedException {
			Thread.sleep(5000);
		    // Extract price elements
		    List<WebElement> priceElements = driver.findElements(By.xpath("//*[contains(@class,'tg-policy')]/following-sibling::h5"));
		    int priceNumber = priceElements.size();

		    List<Integer> actualList = new ArrayList<>();

		    try {
		        // Convert prices to integers and store in a list
		        for (WebElement priceElement : priceElements) {
		            String priceText = priceElement.getText();         // e.g. "₹ 1,371"
		            String priceNumberStr = priceText.split("₹")[1]    // " 1,371"
		                                      .replace(",", "")       // "1371"
		                                      .trim();                // "1371"
		            actualList.add(Integer.parseInt(priceNumberStr));
		        }
		    } catch (StaleElementReferenceException e) {
		        System.out.println("⚠️ StaleElementReferenceException occurred. Retrying fetch...");
		        actualList.clear();

		        // Retry fetching elements and values
		        priceElements = driver.findElements(By.xpath("//*[contains(@class,'tg-policy')]/following-sibling::h5"));
		        for (WebElement priceElement : priceElements) {
		            String priceText = priceElement.getText();
		            String priceNumberStr = priceText.split("₹")[1]
		                                      .replace(",", "")
		                                      .trim();
		            actualList.add(Integer.parseInt(priceNumberStr));
		        }
		    }

		    // Copy actual list for sorting
		    List<Integer> sortedList = new ArrayList<>(actualList);

		    //bubble sort 
		    for (int i = priceNumber - 1; i >= 1; i--) {
		        for (int j = 0; j < i; j++) {
		            if (sortedList.get(j) < sortedList.get(j + 1)) {
		                // Swap
		                int temp = sortedList.get(j);
		                sortedList.set(j, sortedList.get(j + 1));
		                sortedList.set(j + 1, temp);
		            }
		        }
		    }


		    // Compare actual vs sorted
		    if (actualList.equals(sortedList)) {
		        System.out.println("✅ Prices are sorted in Descending order.");
		        log.ReportEvent("PASS", "Prices are sorted in Descending order.");
		    } else {
		        System.out.println("❌ Prices are NOT sorted in Descending order.");
		        System.out.println("Actual: " + actualList);
		        System.out.println("Expected (sorted): " + sortedList);

		        log.ReportEvent("FAIL", "Prices are NOT sorted Descending. \nActual: " + actualList + "\nExpected: " + sortedList);
		        screenShots.takeScreenShot1();
		        Assert.fail("Prices are not sorted in Descending order.");
		    }
		}

		//Method to validate Star Rating In  Ascending Order
		public void validateStarRatingInAscendingOrder(Log log, ScreenShots screenShots) throws InterruptedException
		{
			Thread.sleep(4000);
		   //Get total hotel count 
			List<WebElement> hotelCardCount=driver.findElements(By.xpath("(//*[contains(@class,'hcard')])"));
			int count=hotelCardCount.size();
			System.out.println("Total Hotel Count :"+count);
			List <Integer> acutalCount = new ArrayList<Integer>();
			for(int i=0;i<count-1;i++)
			{
				//get hotel star count based in hotel card
			List<WebElement> starRating =driver.findElements(By.xpath("(//*[contains(@class,'hcard')])[" + i + "]//*[@data-testid='StarIcon']"));
			int starCount=starRating.size();
			System.out.println("Star Review Count Based "+i+ " :"+starCount);
			acutalCount.add(starCount);
			}
			List <Integer> sortedCount = new ArrayList<Integer>(acutalCount);
			for (int i = 0; i < sortedCount.size() - 1; i++) {
			    for (int j = 0; j < sortedCount.size() - i - 1; j++) {
			        if (sortedCount.get(j) > sortedCount.get(j + 1)) {
			            // Swap
			            int temp = sortedCount.get(j);
			            sortedCount.set(j, sortedCount.get(j + 1));
			            sortedCount.set(j + 1, temp);
			        }
			    }
			}
			/*
           //Used To Compare List 
			boolean isSorted = true;
			for (int i = 0; i < acutalCount.size(); i++) {
			    if (!acutalCount.get(i).equals(sortedCount.get(i))) {
			        isSorted = false;
			        break;
			    }
			}
			
			if (isSorted) {
			    System.out.println("✅ Star ratings are sorted in ascending order.");
			    log.ReportEvent("PASS", "Star ratings are sorted in ascending order.");
			} else {
			    System.out.println("❌ Star ratings are NOT sorted in ascending order.");
			    System.out.println("Actual: " + acutalCount);
			    System.out.println("Expected (sorted): " + sortedCount);
			    log.ReportEvent("FAIL", "Star ratings are NOT sorted in ascending order.\nActual: " 
			                     + acutalCount + "\nExpected: " + sortedCount);
			    screenShots.takeScreenShot1();
			    Assert.fail("Star ratings are not sorted in ascending order.");
			}
			*/
			 // Compare actual vs sorted
		    if (acutalCount.equals(sortedCount)) {
		        System.out.println("✅ Star ratings are sorted in ascending order.");
		        log.ReportEvent("PASS", "Star ratings are sorted in ascending order.");
		    } else {
		        System.out.println("❌ Star ratings are NOT sorted in ascending order.");
		        System.out.println("Actual: " + acutalCount);
		        System.out.println("Expected (sorted): " + sortedCount);

		        log.ReportEvent("FAIL", "Star ratings are NOT sorted in ascending order.\nActual: " 
		                         + acutalCount + "\nExpected: " + sortedCount);
		        screenShots.takeScreenShot1();
		        Assert.fail("Star ratings are not sorted in ascending order.");
		    }
			
		}
		//Method to validate Star Rating In Descending Order
		public void validateStarRatingInDescendingOrder(Log log, ScreenShots screenShots) throws InterruptedException {
			Thread.sleep(3000);
		    // Get all hotel cards
		    List<WebElement> hotelCardCount = driver.findElements(By.xpath("//*[contains(@class,'hcard')]"));
		    int count = hotelCardCount.size();
		    System.out.println("Total Hotel Count: " + count);

		    List<Integer> actualCount = new ArrayList<>();

		    // Loop through hotel cards using correct XPath index (1-based)
		    for (int i = 1; i <= count; i++) {
		        // Dynamic XPath to get star icons under the i-th hotel card
		        List<WebElement> starRating = driver.findElements(By.xpath("(//*[contains(@class,'hcard')])[" + i + "]//*[@data-testid='StarIcon']"));
		        int starCount = starRating.size();
		        System.out.println("Hotel " + i + " Star Rating: " + starCount);
		        actualCount.add(starCount);
		    }

		    // Bubble sort in descending order
		    List<Integer> sortedCount = new ArrayList<>(actualCount);

		    for (int i = sortedCount.size() - 1; i >= 1; i--) {
		        for (int j = 0; j < i; j++) {
		            if (sortedCount.get(j) < sortedCount.get(j + 1)) {
		                // Swap for descending order
		                int temp = sortedCount.get(j);
		                sortedCount.set(j, sortedCount.get(j + 1));
		                sortedCount.set(j + 1, temp);
		            }
		        }
		    }

		    // Compare actual vs sorted lists
		    if (actualCount.equals(sortedCount)) {
		        System.out.println("✅ Star ratings are sorted in Descending order.");
		        log.ReportEvent("PASS", "Star ratings are sorted in Descending order.");
		    } else {
		        System.out.println("❌ Star ratings are NOT sorted in Descending order.");
		        System.out.println("Actual: " + actualCount);
		        System.out.println("Expected (sorted): " + sortedCount);

		        log.ReportEvent("FAIL", "Star ratings are NOT sorted in Descending order.\nActual: " 
		                         + actualCount + "\nExpected: " + sortedCount);
		        screenShots.takeScreenShot1();
		        Assert.fail("Star ratings are not sorted in Descending order.");
		    }
		}

		//Method to validate Distance In Ascending Order
		public void validateDistanceInAscendingOrder(Log log, ScreenShots screenShots) throws InterruptedException {
			Thread.sleep(3000);
		    List<WebElement> distanceElements = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-distance')]"));
		    List<Double> actualDistance = new ArrayList<>();

		    for (WebElement element : distanceElements) {
		        String text = element.getText(); // e.g., "46.23 km from Bengaluru, Karnataka, India"
		        String[] parts = text.split(" ");
		        System.out.println(parts[0]);
		        try {
		            double value = Double.parseDouble(parts[0]); // Safely parse float values
		            actualDistance.add(value);
		        } catch (NumberFormatException e) {
		            System.out.println("⚠️ Skipping invalid distance value: " + parts[0]);
		        }
		    }

		    List<Double> sortedDistance = new ArrayList<>(actualDistance);

		    // Bubble sort for ascending order
		    for (int i = 0; i < sortedDistance.size() - 1; i++) {
		        for (int j = 0; j < sortedDistance.size() - i - 1; j++) {
		            if (sortedDistance.get(j) > sortedDistance.get(j + 1)) {
		                double temp = sortedDistance.get(j);
		                sortedDistance.set(j, sortedDistance.get(j + 1));
		                sortedDistance.set(j + 1, temp);
		            }
		        }
		    }

		    // Compare actual vs sorted
		    if (actualDistance.equals(sortedDistance)) {
		        System.out.println("✅ Distances are sorted in Ascending order.");
		        log.ReportEvent("PASS", "Distances are sorted in Ascending order.");
		    } else {
		        System.out.println("❌ Distances are NOT sorted in Ascending order.");
		        System.out.println("Actual: " + actualDistance);
		        System.out.println("Expected (sorted): " + sortedDistance);

		        log.ReportEvent("FAIL", "Distances are NOT sorted in Ascending order.\nActual: " 
		                         + actualDistance + "\nExpected: " + sortedDistance);
		        screenShots.takeScreenShot1();
		        Assert.fail("Distances are not sorted in Ascending order.");
		    }
		}
		/*     //this code for optional and more effective 
		 public void validateDistanceInAscendingOrder(Log log, ScreenShots screenShots) {
    // Step 1: Get all distance elements
    List<WebElement> distanceElements = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-distance')]"));
    List<Double> actualDistance = new ArrayList<>();

    // Step 2: Extract and parse distance values (e.g., "46.23 km from ...")
    for (WebElement element : distanceElements) {
        String text = element.getText(); // Example: "46.23 km from Bengaluru, Karnataka, India"
        String[] parts = text.split(" ");
        try {
            double value = Double.parseDouble(parts[0]);
            actualDistance.add(value);
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Skipping invalid distance value: " + parts[0]);
        }
    }

    // Step 3: Sort a copy of the list using bubble sort (ascending order)
    List<Double> sortedDistance = new ArrayList<>(actualDistance);

    for (int i = 0; i < sortedDistance.size() - 1; i++) {
        for (int j = 0; j < sortedDistance.size() - i - 1; j++) {
            if (sortedDistance.get(j) > sortedDistance.get(j + 1)) {
                // Swap
                double temp = sortedDistance.get(j);
                sortedDistance.set(j, sortedDistance.get(j + 1));
                sortedDistance.set(j + 1, temp);
            }
        }
    }

    // Step 4: Validate if actual list is sorted
    if (actualDistance.equals(sortedDistance)) {
        System.out.println("✅ Distances are sorted in Ascending order.");
        log.ReportEvent("PASS", "Distances are sorted in Ascending order.");
    } else {
        System.out.println("❌ Distances are NOT sorted in Ascending order.");
        System.out.println("Actual: " + actualDistance);
        System.out.println("Expected (sorted): " + sortedDistance);

        // Log mismatches for debugging
        for (int i = 0; i < actualDistance.size(); i++) {
            if (!actualDistance.get(i).equals(sortedDistance.get(i))) {
                System.out.println("🔴 Mismatch at index " + i +
                                   ": actual=" + actualDistance.get(i) +
                                   ", expected=" + sortedDistance.get(i));
            }
        }

        // Log and fail
        log.ReportEvent("FAIL", "Distances are NOT sorted in Ascending order.\nActual: " 
                         + actualDistance + "\nExpected: " + sortedDistance);
        screenShots.takeScreenShot1();
        Assert.fail("Distances are not sorted in Ascending order.");
    }
}

		 */
		//Method to validate Distance In Descending Order
		public void validateDistanceInDescendingOrder(Log log, ScreenShots screenShots) throws InterruptedException {
		    Thread.sleep(3000);
		    
		    List<WebElement> distanceElements = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-distance')]"));
		    List<Double> actualDistance = new ArrayList<>();

		    // Extract and parse distance values
		    for (WebElement element : distanceElements) {
		        String text = element.getText(); // Example: "46.23 km from Bengaluru"
		        String[] parts = text.split(" ");
		        System.out.println(parts[0]);
		        try {
		            double value = Double.parseDouble(parts[0]);
		            actualDistance.add(value);
		        } catch (NumberFormatException e) {
		            System.out.println("⚠️ Skipping invalid distance value: " + parts[0]);
		        }
		    }

		    List<Double> sortedDistance = new ArrayList<>(actualDistance);

		    // Bubble sort for descending order
		    for (int i = 0; i < sortedDistance.size() - 1; i++) {
		        for (int j = 0; j < sortedDistance.size() - i - 1; j++) {
		            if (sortedDistance.get(j) < sortedDistance.get(j + 1)) {
		                double temp = sortedDistance.get(j);
		                sortedDistance.set(j, sortedDistance.get(j + 1));
		                sortedDistance.set(j + 1, temp);
		            }
		        }
		    }

		    // Compare actual vs sorted
		    if (actualDistance.equals(sortedDistance)) {
		        System.out.println("✅ Distances are sorted in Descending order.");
		        log.ReportEvent("PASS", "Distances are sorted in Descending order.");
		    } else {
		        System.out.println("❌ Distances are NOT sorted in Descending order.");
		        System.out.println("Actual: " + actualDistance);
		        System.out.println("Expected (sorted): " + sortedDistance);

		        log.ReportEvent("FAIL", "Distances are NOT sorted in Descending order.\nActual: " 
		                         + actualDistance + "\nExpected: " + sortedDistance);
		        screenShots.takeScreenShot1();
		        Assert.fail("Distances are not sorted in Descending order.");
		    }
		}

		//Method to click On Show Map Button
		public void clickOnShowMap()
		{
			driver.findElement(By.xpath("//button[text()='Show Map View']")).click();
		}
		public void ValidateSelectedHotelInShowViewMap(String hotel, String priceRate, Log log, ScreenShots screenShots) {
		    try {
		        String hotelName = driver.findElement(By.xpath("//*[contains(@class,'hcard')]//*[@title]")).getAttribute("title");
		        String price = driver.findElement(By.xpath("//*[contains(@class,'hcard')]//*[contains(@class,'tg-hl-price')]")).getText();

		        if (hotelName.equals(hotel) && priceRate.equals(price)) {
		            System.out.println("✅ Hotel name and price match.");
		            log.ReportEvent("PASS", "Hotel name and price match. \nHotel: " + hotelName + "\nPrice: " + price);
		        } else {
		            System.out.println("❌ Mismatch in hotel name or price.");
		            log.ReportEvent("FAIL", "Mismatch in selected hotel details.\nExpected Hotel: " + hotel + "\nActual Hotel: " + hotelName +
		                            "\nExpected Price: " + priceRate + "\nActual Price: " + price);
		            screenShots.takeScreenShot1();
		            Assert.fail("Selected hotel name or price does not match.");
		        }
		    } catch (Exception e) {
		        System.out.println("❌ Exception while validating hotel details: " + e.getMessage());
		        log.ReportEvent("FAIL", "Exception while validating hotel details: " + e.getMessage());
		        screenShots.takeScreenShot1();
		        Assert.fail("Exception while validating hotel in Show View Map.");
		        e.printStackTrace();
		    }
		}

		//Method to get User Entered Data While searching
		public String[] userEnteredData() {
		    try {
		        String checkInData = driver.findElement(By.xpath("(//input[@class='DayPickerInput input'])[1]")).getAttribute("value").trim();
		        String checkOutData = driver.findElement(By.xpath("(//input[@class='DayPickerInput input'])[2]")).getAttribute("value").trim();
		        
		        return new String[] { checkInData, checkOutData };
		    } catch (Exception e) {
		        System.err.println("❌ Error retrieving check-in or check-out data: " + e.getMessage());
		        e.printStackTrace();
		        return new String[] { "", "" }; // Or return null or default values as needed
		    }
		}
		
		//Method to select Hotel Based On Index
		public void selectHotelBasedOnIndex(int index)
		{
			driver.findElement(By.xpath("(//button[text()='Select'])['"+index+"']")).click();
		}

}

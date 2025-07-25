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
import org.openqa.selenium.ElementClickInterceptedException;
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

public class Tripgain_Hotel_hotelDetailPage {

	WebDriver driver;


	public Tripgain_Hotel_hotelDetailPage(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	//neeed to valiadte userenter date and rooms
	/*
	public void validateHotelDetailPage(Log Log, ScreenShots ScreenShots, Map<String, Object> hotelDetails) {
	    try {
	        // Extract expected values from the passed map
	        String expectedHotelName = (String) hotelDetails.get("hotelName");
	        String expectedHotelAddress = (String) hotelDetails.get("hotelAddress");
	        int expectedStarRating = (int) hotelDetails.get("starRatingCount");
	        String expectedPolicy = (String) hotelDetails.get("policy");
	        String actualPrices = (String) hotelDetails.get("actualPrice");

	        // Actual values from the UI
	        String actualHotelName = driver.findElement(By.xpath("//*[contains(@class,'tg-hl-hotalname')]")).getText().trim();
	        String actualHotelAddress = driver.findElement(By.xpath("//*[contains(@class,'tg-hl-addr')]")).getText().trim();
	     // Remove the last part after the last comma  //ex:Ittaninagar, Jigani, Bengaluru Anekal, Anekal ->Ittaninagar, Jigani, Bengaluru Anekal
	        if (actualHotelAddress.contains(",")) {
	        	actualHotelAddress = actualHotelAddress.substring(0, actualHotelAddress.lastIndexOf(",")).trim();
	        }
	        List<WebElement> starElements = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-stars')]//*[@data-testid='StarIcon']"));
	        int actualStarRating = starElements.size();
	        String actualPrice = driver.findElement(By.xpath("(//*[contains(@class,'tg-hl-price')])[1]")).getText().trim();

	        // Validate policy (using your existing function)
	        validatePolicy(Log, ScreenShots, expectedPolicy);

	        System.out.println(actualHotelName);
	        System.out.println(expectedHotelName);
	        System.out.println(actualStarRating);
	        System.out.println(expectedStarRating);
	        System.out.println(actualPrice);
	        System.out.println(actualPrices);
	        
	        // Comparison and logging
	        if (actualHotelName.equalsIgnoreCase(expectedHotelName)
	                && actualHotelAddress.equalsIgnoreCase(expectedHotelAddress)
	                && actualStarRating == expectedStarRating
	                && actualPrice.equals(actualPrices) ) {

	            Log.ReportEvent("PASS", "✅ Hotel details matched: Name='" + actualHotelName + "', Address='" + actualHotelAddress +
	                    "', StarsRating =" + actualStarRating +"',Price='"+actualPrice);
	           
	            
	        } else {
	        	Log.ReportEvent("FAIL", 
	        		    "❌ Hotel details mismatch.\n" +
	        		    "🔹 Expected Hotel Name : " + expectedHotelName + "\n" +
	        		    "🔹 Expected Address    : " + expectedHotelAddress + "\n" +
	        		    "🔹 Expected Stars      : " + expectedStarRating + "\n" +
	        		    "🔹 Expected Price      : " + actualPrices + "\n\n" +
	        		    "🔸 Actual Hotel Name   : " + actualHotelName + "\n" +
	        		    "🔸 Actual Address      : " + actualHotelAddress + "\n" +
	        		    "🔸 Actual Stars        : " + actualStarRating + "\n" +
	        		    "🔸 Actual Price        : " + actualPrice
	        		);


	            ScreenShots.takeScreenShot1();
	            Assert.fail("Hotel details did not match expected.");
	        }

	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "❌ Exception in hotel detail validation: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	        Assert.fail("Exception while validating hotel detail.");
	    }
	}
*/
	//Method to validate Hotel Detail Page
	public Map<String, Object> validateHotelDetailPage(Log Log, ScreenShots ScreenShots, Map<String, Object> hotelDetails) {
	    Map<String, Object> actualData = new HashMap<>();

	    try {
	        // Expected data from input
	        String expectedHotelName = (String) hotelDetails.get("hotelName");
	        String expectedHotelAddress = (String) hotelDetails.get("hotelAddress");
	        int expectedStarRating = (int) hotelDetails.get("starRatingCount");
	        String expectedPolicy = (String) hotelDetails.get("policy");
	        String expectedPrice = (String) hotelDetails.get("actualPrice");

	        // Actual values from UI
	        String actualHotelName = driver.findElement(By.xpath("//*[contains(@class,'tg-hl-hotalname')]")).getText().trim();
	        String actualHotelAddress = driver.findElement(By.xpath("//*[contains(@class,'tg-hl-addr')]")).getText().trim();

	        // Remove last part after last comma
	        if (actualHotelAddress.contains(",")) {
	            actualHotelAddress = actualHotelAddress.substring(0, actualHotelAddress.lastIndexOf(",")).trim();
	        }

	        List<WebElement> starElements = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-stars')]//*[@data-testid='StarIcon']"));
	        int actualStarRating = starElements.size();

	        String actualPrice = driver.findElement(By.xpath("(//*[contains(@class,'tg-hl-price')])[1]")).getText().trim();
	        
	   //    String NumberOfNightDays= driver.findElement(By.xpath("//*[contains(@class,'tg-hl-priceDays')]")).getText().trim();
	     //  String pricePerNight= driver.findElement(By.xpath("//*[contains(@class,'tg-hl-priceDays')]")).getText().trim();
	       String acutalPricePerNight= driver.findElement(By.xpath("//*[contains(@class,'tg-hl-pricefornight')]")).getText().trim();

	        // Validate policy
	      String policy=  validatePolicy(Log, ScreenShots, expectedPolicy);

	        // Logging for debug
	        System.out.println("Expected Name: " + expectedHotelName + ", Actual: " + actualHotelName);
	        System.out.println("Expected Address: " + expectedHotelAddress + ", Actual: " + actualHotelAddress);
	        System.out.println("Expected Stars: " + expectedStarRating + ", Actual: " + actualStarRating);
	        System.out.println("Expected Price: " + expectedPrice + ", Actual: " + actualPrice);

	        // Compare all values
	        if (actualHotelName.equalsIgnoreCase(expectedHotelName)
	                && actualHotelAddress.equalsIgnoreCase(expectedHotelAddress)
	                && actualStarRating == expectedStarRating
	                && actualPrice.equals(expectedPrice)) {

	            Log.ReportEvent("PASS", "✅ Hotel details matched:\n" +
	                    "Hotel Name: " + actualHotelName + "\nAddress: " + actualHotelAddress +
	                    "\nStars: " + actualStarRating + "\nPrice: " + actualPrice);

	        } else {
	            Log.ReportEvent("FAIL",
	                    "❌ Hotel details mismatch.\n" +
	                    "🔹 Expected Hotel Name : " + expectedHotelName + "\n" +
	                    "🔹 Expected Address    : " + expectedHotelAddress + "\n" +
	                    "🔹 Expected Stars      : " + expectedStarRating + "\n" +
	                    "🔹 Expected Price      : " + expectedPrice + "\n\n" +
	                    "🔸 Actual Hotel Name   : " + actualHotelName + "\n" +
	                    "🔸 Actual Address      : " + actualHotelAddress + "\n" +
	                    "🔸 Actual Stars        : " + actualStarRating + "\n" +
	                    "🔸 Actual Price        : " + actualPrice
	            );
	            ScreenShots.takeScreenShot1();
	            Assert.fail("Hotel details did not match expected.");
	        }

	        // Add actual values to return map
	        actualData.put("actualHotelName", actualHotelName);
	        actualData.put("actualHotelAddress", actualHotelAddress);
	        actualData.put("actualStarRating", actualStarRating);
	        actualData.put("actualPrice", actualPrice);
	        actualData.put("policy", policy);
	      //  actualData.put("NumberOfNightDays", NumberOfNightDays);
	        actualData.put("acutalPricePerNight", acutalPricePerNight);

	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "❌ Exception in hotel detail validation: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	        Assert.fail("Exception while validating hotel detail.");
	    }

	    return actualData;
	}

	public String validatePolicy(Log Log, ScreenShots ScreenShots, String expectedValue) {
	    try {
	        // Wait until any policy element appears
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        wait.until(ExpectedConditions.or(
	                ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@data-tginpolicy]")),
	                ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@data-tgoutpolicy]"))
	        ));

	        // Find both in-policy and out-of-policy elements
	        List<WebElement> policies = new ArrayList<>();
	        policies.addAll(driver.findElements(By.xpath("//*[@data-tginpolicy]")));
	        policies.addAll(driver.findElements(By.xpath("//*[@data-tgoutpolicy]")));

	        if (policies.isEmpty()) {
	            Log.ReportEvent("FAIL", "❌ No policy elements found.");
	            ScreenShots.takeScreenShot1();
	            Assert.fail("No policy elements to validate.");
	            return null;
	        }

	        List<String> mismatches = new ArrayList<>();

	        for (WebElement policy : policies) {
	            try {
	                String text = policy.getText().trim();
	                System.out.println("Policy Text: " + text);
	                if (!text.equalsIgnoreCase(expectedValue)) {
	                    mismatches.add(text);
	                }
	            } catch (StaleElementReferenceException e) {
	                // Retry the element once
	                policy = driver.findElement(By.xpath("//*[text()='" + expectedValue + "']"));
	            }
	        }

	        if (mismatches.isEmpty()) {
	            Log.ReportEvent("PASS", "✅ All policies match: '" + expectedValue + "'");
	            return expectedValue;
	        } else {
	            Log.ReportEvent("FAIL", "❌ Mismatched policies found. Expected: '" + expectedValue + "', Found: " + mismatches);
	            ScreenShots.takeScreenShot1();
	            Assert.fail("Some policies did not match.");
	            return null;
	        }

	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "❌ Exception during policy validation: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	        Assert.fail("Exception occurred in validatePolicy.");
	        return null;
	    }
	}

/*
	public void getHotelCardDetails(Log Log, ScreenShots ScreenShots, int hotelDetailsCardIndex, Map<String, Object> hotelDetails) {
	    try {
	    	  Map<String, Object> actualData = new HashMap<>();
	        // XPath indexing starts at 1
	        WebElement hotelCard = driver.findElement(By.xpath("(//*[contains(@class,'tg-hl-rooms-card')])[" + hotelDetailsCardIndex + "]"));
	        
	        if (hotelCard.isDisplayed()) {
	            // Extract actual data from the card
	            String fare = driver.findElement(By.xpath("(//*[contains(@class,'tg-hl-refundable')])[" + hotelDetailsCardIndex + "]")).getText().trim();
	            String cancellationPolicy = driver.findElement(By.xpath("(//*[contains(@class,'tg-hl-refundable')]/span)[" + hotelDetailsCardIndex + "]")).getText().trim();
	            String meals = driver.findElement(By.xpath("(//*[contains(@class,'tg-hl-meals')])[" + hotelDetailsCardIndex + "]")).getText().trim();
	            String policy = driver.findElement(By.xpath("(//*[contains(@class,'tg-policy')])[2]")).getText().trim();
	            String roomPrice = driver.findElement(By.xpath("(//*[contains(@class,'tg-hl-roomprice')])[" + hotelDetailsCardIndex + "]")).getText().trim();
	            roomPrice=roomPrice.split("Per Night")[0].trim();//₹ 2,419 Per Night Price: ₹ 806-->₹ 2,419
	            
	            String hotelfare=driver.findElement(By.xpath("//*[contains(@class,'hotel-fare-type')]")).getText().trim();
	            
	            // Get expected values from input map
	            String expectedPolicy = (String) hotelDetails.get("policy");
	            String expectedPrice = (String) hotelDetails.get("actualPrice");

	            System.out.println(roomPrice);
	            System.out.println(expectedPrice);
	            // Validate policy and price
	            if (policy.equalsIgnoreCase(expectedPolicy) && roomPrice.equals(expectedPrice)) {
	                Log.ReportEvent("PASS", "✅ Policy and Price matched for hotel card index " + hotelDetailsCardIndex);
	                driver.findElement(By.xpath("(//button[text()='Select & Continue'])[" + hotelDetailsCardIndex + "]")).click();
	                actualData.put("hotelfare",hotelfare);
	    	        actualData.put("fare",fare);
	    	        actualData.put("cancellationPolicy",cancellationPolicy);
	    	        actualData.put("meals",meals);
	    	        actualData.put("meals",meals);
	            } else {
	                Log.ReportEvent("FAIL", "❌ Mismatch found in hotel card index " + hotelDetailsCardIndex +
	                        "\nExpected Policy: " + expectedPolicy + ", Actual Policy: " + policy +
	                        "\nExpected Price: " + expectedPrice + ", Actual Price: " + roomPrice);
	                ScreenShots.takeScreenShot1();
	            }
	        } else {
	            Log.ReportEvent("FAIL", "❌ Hotel card at index " + hotelDetailsCardIndex + " is not visible.");
	            ScreenShots.takeScreenShot1();
	        }
	        
	        
	        
	    } catch (NoSuchElementException e) {
	        Log.ReportEvent("FAIL", "❌ Element not found for hotel card index " + hotelDetailsCardIndex + ": " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "❌ Exception in getHotelCardDetails: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	    }
	}
*/
	//Method to get selected Hotel card Details
	public Map<String, Object> getHotelCardDetails(Log Log, ScreenShots ScreenShots, int hotelDetailsCardIndex, Map<String, Object> hotelDetails) {
	    Map<String, Object> actualData = new HashMap<>();

	    try {
	        // Identify the hotel card block
	        WebElement hotelCard = driver.findElement(
	            By.xpath("(//*[contains(@class,'tg-hl-rooms-card')])[" + hotelDetailsCardIndex + "]")
	        );

	        if (!hotelCard.isDisplayed()) {
	            Log.ReportEvent("FAIL", "❌ Hotel card at index " + hotelDetailsCardIndex + " is not visible.");
	            ScreenShots.takeScreenShot1();
	            return actualData;
	        }

	        // Extract UI data
	        String fare = driver.findElement(By.xpath("(//*[contains(@class,'tg-hl-refundable')])[" + hotelDetailsCardIndex + "]")).getText().trim();
	        String cancellationPolicy = driver.findElement(By.xpath("(//*[contains(@class,'tg-hl-refundable')]/span)[" + hotelDetailsCardIndex + "]")).getText().trim();
	        String meals = driver.findElement(By.xpath("(//*[contains(@class,'tg-hl-meals')])[" + hotelDetailsCardIndex + "]")).getText().trim();
	        String policy = driver.findElement(By.xpath("(//*[contains(@class,'tg-policy')])[2]")).getText().trim();

	        String roomPriceFull = driver.findElement(By.xpath("(//*[contains(@class,'tg-hl-roomprice')])[" + hotelDetailsCardIndex + "]")).getText().trim();
	        String roomPrice = roomPriceFull.split("Per Night")[0].trim(); // Clean to only include main price

	        String hotelFareType = driver.findElement(By.xpath("//*[contains(@class,'hotel-fare-type')]")).getText().trim();

	        // Expected values
	        String expectedPolicy = (String) hotelDetails.get("policy");
	        String expectedPrice = (String) hotelDetails.get("actualPrice");

	        // Validation
	        boolean isPolicyMatch = policy.equalsIgnoreCase(expectedPolicy);
	        boolean isPriceMatch = roomPrice.equals(expectedPrice);

	        if (isPolicyMatch && isPriceMatch) {
	            Log.ReportEvent("PASS", "✅ Policy and Price matched for hotel card index " + hotelDetailsCardIndex);
	            driver.findElement(By.xpath("(//button[text()='Select & Continue'])[" + hotelDetailsCardIndex + "]")).click();

	            // Add data to return map
	            actualData.put("hotelFareType", hotelFareType);
	            actualData.put("fare", fare);
	            actualData.put("cancellationPolicy", cancellationPolicy);
	            actualData.put("meals", meals);
	        } else {
	            Log.ReportEvent("FAIL", 
	                "❌ Mismatch in hotel card index " + hotelDetailsCardIndex +
	                "\n🔹 Expected Policy: " + expectedPolicy +
	                "\n🔹 Actual Policy  : " + policy +
	                "\n🔹 Expected Price : " + expectedPrice +
	                "\n🔹 Actual Price   : " + roomPrice);
	            ScreenShots.takeScreenShot1();
	        }

	    } catch (NoSuchElementException e) {
	        Log.ReportEvent("FAIL", "❌ Element not found: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "❌ Exception in getHotelCardDetails: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	    }

	    return actualData;
	}
	
	//Method to validate Currency In Hotel Details Page
	public void validateCurrencyInHotelDetailsPage(String currencyFormat, Log Log, ScreenShots ScreenShots) {
	    try {
	        boolean isCurrencyFound = false;

	        // Check main total price
	        String totalPrice = driver.findElement(By.xpath("//*[contains(@class,'tg-hl-othercurrency')]")).getText();
	        if (totalPrice.contains(currencyFormat)) {
	            isCurrencyFound = true;
	        }

	        // Check room-level prices
	        List<WebElement> roomPrices = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-roomotherprice')]"));
	        for (WebElement price : roomPrices) {
	            String roomPriceText = price.getText();
	            if (roomPriceText.contains(currencyFormat)) {
	                isCurrencyFound = true;
	              System.out.println("Currency Found :"+roomPriceText);
	            }
	        }

	        if (isCurrencyFound) {
	            Log.ReportEvent("PASS", "✅ Currency format '" + currencyFormat + "' is correctly displayed in hotel details page.");
	        } else {
	            Log.ReportEvent("FAIL", "❌ Currency format '" + currencyFormat + "' not found in hotel details page.");
	            ScreenShots.takeScreenShot1();
	        }

	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "❌ Exception while validating currency format: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}

	/*
	public void validateAmenities(String amenityToCheck, Log Log, ScreenShots ScreenShots) {
	    try {
	        List<WebElement> amenitiesElements = driver.findElements(By.xpath("//*[contains(@class,'tg-hl-amenities-card')]//*[contains(@class,'tg-hl-amenities')]"));

	        boolean amenityFound = false;
	        for (WebElement amenity : amenitiesElements) {
	            String text = amenity.getText().trim();
	            if (text.equalsIgnoreCase(amenityToCheck)) {
	                amenityFound = true;
	                break;
	            }
	        }

	        if (amenityFound) {
	            Log.ReportEvent("PASS", "✅ Amenity '" + amenityToCheck + "' is present on the hotel details page.");
	        } else {
	            Log.ReportEvent("FAIL", "❌ Amenity '" + amenityToCheck + "' not found on the hotel details page.");
	            ScreenShots.takeScreenShot1();
	        }

	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "❌ Exception while validating amenity '" + amenityToCheck + "': " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}
*/
	
	//Method to validate Amenities
	public void validateAmenities(String amenityToCheck, Log Log, ScreenShots ScreenShots) {
	    try {
	        List<WebElement> amenitiesElements = driver.findElements(By.xpath(
	            "//*[contains(@class,'tg-hl-amenities-card')]//*[contains(@class,'tg-hl-amenities')]"));

	        boolean amenityFound = false;
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        for (WebElement amenity : amenitiesElements) {
	            // Scroll the element into view
	            js.executeScript("arguments[0].scrollIntoView(true);", amenity);
	            Thread.sleep(300); // Optional: Allow some time for the scroll

	            String text = amenity.getText().trim();
	            if (text.equalsIgnoreCase(amenityToCheck)) {
	                amenityFound = true;
	                break;
	            }
	        }

	        if (amenityFound) {
	            Log.ReportEvent("PASS", "✅ Amenity '" + amenityToCheck + "' is present on the hotel details page.");
	        } else {
	            Log.ReportEvent("FAIL", "❌ Amenity '" + amenityToCheck + "' not found on the hotel details page.");
	            ScreenShots.takeScreenShot1();
	        }

	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "❌ Exception while validating amenity '" + amenityToCheck + "': " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	        e.printStackTrace();
	    }
	}

	//Method to select Hotel In Details Page Based On Index
	public void selectHotelInDetailsPageBasedOnIndex(int index) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        String xpath = "(//*[contains(@class,'tg-hl-rooms-card')]//button[text()='Select & Continue'])[" + index + "]";
	        
	        WebElement selectButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

	        // Scroll the button into center view (better than top)
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectButton);
	        Thread.sleep(500); // Let scroll complete

	        try {
	            wait.until(ExpectedConditions.elementToBeClickable(selectButton));
	            selectButton.click();
	        } catch (ElementClickInterceptedException e) {
	            System.out.println("⚠️ Native click failed, trying JS click due to: " + e.getMessage());
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectButton);
	        }

	    } catch (Exception e) {
	        System.out.println("❌ Exception while selecting flight at index " + index + ": " + e.getMessage());
	        e.printStackTrace();
	    }
	}
		
	}




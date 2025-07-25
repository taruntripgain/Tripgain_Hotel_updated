package com.tripgain.bus.collectionofpages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;

public class Tripgain_bus_resultPage {


WebDriver driver;
	

	public Tripgain_bus_resultPage(WebDriver driver) {

        PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	public void validateResultPageIsDisplayed(Log Log, ScreenShots ScreenShots) {
	    try {
	      //  WebElement resultPageGrid = driver.findElement(By.xpath("//*[contains(@class, 'MuiGrid2-grid-md-9')]"));
	        WebElement resultPageGrid = driver.findElement(By.xpath("//*[contains(@class,'tg-bsrs-operatorname')]"));
	        
	        if (resultPageGrid.isDisplayed()) {
	            Log.ReportEvent("PASS", "Result page is getting displayed successfully.");
	            System.out.println("Result page is getting displayed successfully.");
	        } else {
	            Log.ReportEvent("FAIL", "Result page is not displayed.");
	            System.out.println("Result page is not displayed.");
	        }

	        ScreenShots.takeScreenShot1(); // Take screenshot in both cases
	    } catch (NoSuchElementException e) {
	        Log.ReportEvent("FAIL", "Result page element not found.");
	        System.out.println("Result page element not found.");
	        ScreenShots.takeScreenShot1();
	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "An unexpected error occurred: " + e.getMessage());
	        System.out.println("Unexpected error: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	    }
	}
	
	public void getFirstBusOperator(Log Log, ScreenShots ScreenShots) {
	    List<WebElement> busOperators = driver.findElements(
	        By.xpath("//*[text()='OPERATOR NAME']/parent::div//span[contains(@class,'MuiListItemText-primary')]")
	    );

	    if (!busOperators.isEmpty()) {
	        // Log all operators
	        for (WebElement busOperator : busOperators) {
	            String name = busOperator.getText();
	            Log.ReportEvent("INFO", "Bus operator: " + name);
	            System.out.println("Bus operator: " + name);
	        }

	       

	    } else {
	        Log.ReportEvent("FAIL", "No bus operator found.");
	        System.out.println("No bus operator found.");
	        ScreenShots.takeScreenShot1();
	        
	    }
	}
	public String[] selectBusCardBasedOnIndex(int index) {
	    String[] result = new String[3]; // [operatorName, duration, departureTime]

	    try {
	        // XPath for 'View Seats' button
	        String viewSeatsXpath = "(//*[contains(@class, 'MuiPaper-root') and contains(@class, 'MuiPaper-elevation1')]//button[text()='View Seats'])[" + index + "]";

	        // Correct XPath indexing
	        String operatorXpath = "(//*[contains(@class,'tg-bsrs-operatorname')]/h6)[" + index + "]";
	        String durationXpath = "(//*[contains(@class,'tg-bsrs-duration')])[" + index + "]";
	        String departureTimeXpath = "(//*[contains(@class,'g-bsrs-deptime')])[" + index + "]";

	        // Fetch values
	        String busOperatorName = driver.findElement(By.xpath(operatorXpath)).getText();
	        String duration = driver.findElement(By.xpath(durationXpath)).getText();
	        String departureTime = driver.findElement(By.xpath(departureTimeXpath)).getText();

	        // Click the 'View Seats' button
	        driver.findElement(By.xpath(viewSeatsXpath)).click();

	        // Store values
	        result[0] = busOperatorName;
	        result[1] = duration;
	        result[2] = departureTime;

	    } catch (Exception e) {
	        System.out.println("Exception while selecting bus card at index " + index + ": " + e.getMessage());
	        e.printStackTrace();

	        // Assign fallback values
	        result[0] = "N/A";
	        result[1] = "N/A";
	        result[2] = "N/A";
	    }

	    return result;
	}


	public String selectBoardingPoint(Log Log, ScreenShots ScreenShots) {
	    try {
	        WebElement boardingPointDropDown = driver.findElement(
	            By.xpath("//div[contains(@class,'tg-bsrs-boardingpoint')]")
	        );

	        if (boardingPointDropDown.isDisplayed()) {
	            boardingPointDropDown.click();

	            // Wait for dropdown menu to appear
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	            WebElement dropDownMenuOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//*[contains(@class, 'MuiPaper-root') and contains(@class, 'MuiPopover-paper') and contains(@class, 'MuiMenu-paper')]")
	            ));

	            if (dropDownMenuOption.isDisplayed()) {
	                WebElement selectedBoardingPointLocation = driver.findElement(
	                    By.xpath("(//li[@role='option'])[1]")
	                );

	                // Get the text of the selected option
	                String boardingPointLocation = selectedBoardingPointLocation
	                    .findElement(By.xpath("./div/span")).getText().trim();
	                    
	                //Incase getText is returning empty it will search in DOM
	                if (boardingPointLocation.isEmpty()) {
	                    // Fallback to JS if needed
	                    JavascriptExecutor js = (JavascriptExecutor) driver;
	                    boardingPointLocation = (String) js.executeScript(
	                        "return arguments[0].innerText;", selectedBoardingPointLocation
	                    );
	                }
	                
	                
	                
	                JavascriptExecutor js = (JavascriptExecutor) driver;
	                js.executeScript("arguments[0].click();", selectedBoardingPointLocation);

	               // selectedBoardingPointLocation.click();

	                // Remove trailing time like "(20:30)"
	                String cleanedBoardingPoint = boardingPointLocation
	                    .replaceAll("\\s*\\(\\d{1,2}:\\d{2}\\)$", "")
	                    .trim();

	                Log.ReportEvent("PASS", "Selected boarding point: " + cleanedBoardingPoint);
	                System.out.println("Selected boarding point: " + cleanedBoardingPoint);
	                ScreenShots.takeScreenShot1();

	                return cleanedBoardingPoint;
	            } else {
	                Log.ReportEvent("FAIL", "Dropdown menu is not visible after clicking.");
	                System.out.println("Dropdown menu is not visible.");
	                ScreenShots.takeScreenShot1();
	            }
	        } else {
	            Log.ReportEvent("FAIL", "Boarding point dropdown is not displayed.");
	            System.out.println("Boarding point dropdown is not displayed.");
	            ScreenShots.takeScreenShot1();
	        }
	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "Error selecting boarding point: " + e.getMessage());
	        System.out.println("Exception: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	    }

	    return null;
	}

	public String selectDroppingPoint(Log Log, ScreenShots ScreenShots) {
	    try {
	        WebElement droppingPointDropDown = driver.findElement(
	            By.xpath("//*[contains(@class,'tg-bsrs-droppingpoint')]")
	        );

	        if (droppingPointDropDown.isDisplayed()) {
	            droppingPointDropDown.click();

	            // Wait for the dropdown options to be visible
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	            WebElement dropDownMenuOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//*[contains(@class, 'MuiPaper-root') and contains(@class, 'MuiPopover-paper') and contains(@class, 'MuiMenu-paper')]")
	            ));

	            if (dropDownMenuOption.isDisplayed()) {
	                WebElement selectedDroppingPointLocation = driver.findElement(
	                    By.xpath("(//li[@role='option'])[1]")
	                );

	                // Get the visible text
	                String droppingPointLocation = selectedDroppingPointLocation
	                    .findElement(By.xpath("./div/span"))
	                    .getText();

	              //  selectedDroppingPointLocation.click();
	                JavascriptExecutor js = (JavascriptExecutor) driver;
	                js.executeScript("arguments[0].click();", selectedDroppingPointLocation);

	                Log.ReportEvent("PASS", "Selected Dropping Point: " + droppingPointLocation);
	                System.out.println("Selected Dropping Point: " + droppingPointLocation);
	                ScreenShots.takeScreenShot1();

	                return droppingPointLocation;
	            }
	        } else {
	            Log.ReportEvent("FAIL", "Dropping point dropdown is not displayed.");
	            System.out.println("Dropping point dropdown is not displayed.");
	            ScreenShots.takeScreenShot1();
	        }
	    } catch (Exception e) {
	        Log.ReportEvent("FAIL", "Error selecting dropping point: " + e.getMessage());
	        System.out.println("Exception while selecting dropping point: " + e.getMessage());
	        ScreenShots.takeScreenShot1();
	    }

	    return null;
	}


	/*
	public List<String> getSelectedSeat(Log Log, ScreenShots ScreenShots) {
	    List<String> selectedSeats = new ArrayList<>();

	    try {
	        List<WebElement> seatElements = driver.findElements(By.xpath("//span[@class='seat-list']"));
	       int seatCount= seatElements.size();
	       //helper method
	       validateSeatCount(seatCount, Log,  ScreenShots);
	       
	        if (!seatElements.isEmpty()) {
	            for (WebElement seatElement : seatElements) {
	                String fullText = seatElement.getText(); // e.g., "U3 - ₹ 1,418"
	                if (fullText != null && fullText.contains("-")) {
	                    String seat = fullText.split("-")[0].trim(); // Extract "U3"
	                    selectedSeats.add(seat);
	                }
	            }
	        } else {
	            System.out.println("No selected seats found.");
	        }

	    } catch (Exception e) {
	        System.out.println("Exception while getting selected seats: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return selectedSeats;
	}

    // helper method
	public void validateSeatCount(int expectedSeatCount, Log Log, ScreenShots ScreenShots) {
	    try {
	        WebElement totalSeatCount = driver.findElement(By.xpath("//*[contains(@class,'tg-bsrs-availableseats')]"));
	        String seatCountText = totalSeatCount.getText(); // e.g., "36 Seats Available"

	        // Extract digits only
	        String seatCountOnly = seatCountText.replaceAll("\\D+", ""); // "36"

	        // Convert to int
	        int actualSeatCount = Integer.parseInt(seatCountOnly);

	        if (actualSeatCount == expectedSeatCount) {
	            Log.ReportEvent("PASS", "Total Seat Count is matching: " + actualSeatCount);
	            ScreenShots.takeScreenShot1();
	        } else {
	            Log.ReportEvent("FAIL", "Expected Seat Count: " + expectedSeatCount + ", but found: " + actualSeatCount);
	            ScreenShots.takeScreenShot1();
	        }

	        System.out.println("Available Seat Count (int): " + actualSeatCount);
	    } catch (Exception e) {
	        Log.ReportEvent("ERROR", "Exception during seat count validation: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
*/
	public List<String> getSelectedSeat(int index,Log Log, ScreenShots ScreenShots) {
	    List<String> selectedSeats = new ArrayList<>();

	    try {
	    	//To get seat available 
	        List<WebElement> seatavailable = driver.findElements(By.xpath("//*[contains(@class,'tg-bsrs-seatavailable')]"));
	        //seat selected
	        List<WebElement> seatElements = driver.findElements(By.xpath("//*[contains(@class,'tg-bsrs-seatandprice')]"));
	        int seatCount = seatavailable.size();
	        System.out.println("Available Seat :"+ seatCount);

	        // Call helper method to validate the displayed seat count
	        validateSeatCount(seatCount,index, Log, ScreenShots);

	        if (!seatElements.isEmpty()) {
	            for (WebElement seatElement : seatElements) {
	                String fullText = seatElement.getText(); // e.g., "U3 - ₹ 1,418"
	                if (fullText != null && fullText.contains("-")) {
	                    String seat = fullText.split("-")[0].trim(); // Extract "U3"
	                    selectedSeats.add(seat);
	                }
	            }
	            Log.ReportEvent("PASS", "Selected seats extracted: " + selectedSeats);
	        } else {
	            Log.ReportEvent("INFO", "No selected seats found.");
	        }

	    } catch (Exception e) {
	        Log.ReportEvent("ERROR", "Exception while getting selected seats: " + e.getMessage());
	        e.printStackTrace();
	    }
	    System.out.println("hi: "+selectedSeats.size());

	    return selectedSeats;
	}
	//Helper Method
	public void validateSeatCount(int expectedSeatCount,int index, Log Log, ScreenShots ScreenShots) {
	    try {
	        WebElement totalSeatCount = driver.findElement(By.xpath("(//*[contains(@class,'tg-bsrs-availableseats')])['"+index+"']"));
	        String seatCountText = totalSeatCount.getText(); // e.g., "36 Seats Available"

	        // Extract digits only
	        String seatCountOnly = seatCountText.replaceAll("\\D+", ""); // "36"
	        int actualSeatCount = Integer.parseInt(seatCountOnly);

	        if (actualSeatCount == expectedSeatCount) {
	            Log.ReportEvent("PASS", "Total Seat Count is matching: " + actualSeatCount);
	        } else {
	            Log.ReportEvent("FAIL", "Expected Seat Count: " + expectedSeatCount + ", but found: " + actualSeatCount);
	        }

	        ScreenShots.takeScreenShot1();
	        System.out.println("Available Seat Count (int): " + actualSeatCount);
	    } catch (Exception e) {
	        Log.ReportEvent("ERROR", "Exception during seat count validation: " + e.getMessage());
	        e.printStackTrace();
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

	public void selectSeat() {
	    try {
	        List<WebElement> pickSeats = driver.findElements(By.xpath("//*[contains(@class,'tg-bsrs-seatavailable')]"));
	        if (!pickSeats.isEmpty()) {
	            pickSeats.get(pickSeats.size() - 1).click();  // Click the last available seat
	            System.out.println("Seat selected successfully.");
	        } else {
	            System.out.println("No available seats found.");
	        }
	    } catch (Exception e) {
	        System.out.println("An error occurred while selecting a seat: " + e.getMessage());
	        e.printStackTrace();  // Optional: prints the full stack trace for debugging
	    }
	}
	
	public void clickOnConfirmSeat() throws InterruptedException
	{
		driver.findElement(By.xpath("//button[text()='Confirm Seat']")).click();
		reasonForSelectionPopUp1();
	}
	//Method to close reason For Selection PopUp
			public void reasonForSelectionPopUp1() throws InterruptedException {
			    String value = "Personal Preference";

			    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			    try {
			    	Thread.sleep(8000);
			    //	 WebElement popup=driver.findElement(By.xpath("//h2[@id='alert-dialog-title']"));
			        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(
			            By.xpath("//*[@id='alert-dialog-title']")
		        ));

			        if (popup.isDisplayed()) {
			            WebElement reasonOption = driver.findElement(
			                By.xpath("//*[text()='" + value + "']//parent::label")
			            );
			            reasonOption.click();
			            //click on Proceed to Booking
			            driver.findElement(By.xpath("//button[text()='Proceed to Booking']")).click();
			          //  Thread.sleep(3000);
			            //click on Continue button
//			            driver.findElement(By.xpath("//div[@class='bottom-container-1']//button[text()='Continue']")).click();
			        }

			    } catch (TimeoutException e) {
			        System.out.println("Popup did not appear in time.");
			    }
			}

}

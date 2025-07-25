package com.tripgain.bus.collectionofpages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;
import com.tripgain.common.TestExecutionNotifier;

public class Tripgain_bus_homepage {

WebDriver driver;
	

	public Tripgain_bus_homepage(WebDriver driver) {

        PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	@FindBy(xpath = "//*[text()='Enter From']//parent::div//parent::div//input")
    private WebElement enterFromLocation;
	
	@FindBy(xpath = "//*[text()='Enter To']//parent::div//parent::div//input")
    private WebElement enterToLocation;
	
	@FindBy(xpath = "//input[contains(@class,'tg-bs-date')]")
    WebElement datePickerInput;
	
	public void enterFromLocation(String location) {
		enterFromLocation.clear();
		enterFromLocation.sendKeys(location);
	    
	 //   WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	  //  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']")));
	    
	    selectLocation(location);
	    
	}
	public void enterToLocation(String location) {
		enterToLocation.clear();
		enterToLocation.sendKeys(location);
	    
	 //   WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	  //  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']")));
	    
	    selectLocation(location);
	    
	}
	
	
	 public void selectLocation(String location) {
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

		    } catch (TimeoutException e) {
		        System.out.println("Timeout waiting for suggestions: " + e.getMessage());
		    } catch (NoSuchElementException e) {
		        System.out.println("Input or dropdown not found: " + e.getMessage());
		    } catch (Exception e) {
		        System.out.println("Unexpected error while selecting city or hotel: " + e.getMessage());
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
		                dp[i][j] = Math.min(Math.min(
		                    dp[i - 1][j] + 1,       // deletion
		                    dp[i][j - 1] + 1),      // insertion
		                    dp[i - 1][j - 1] + cost // substitution
		                );
		            }
		        }
		    }
		    return dp[a.length()][b.length()];
		}
		
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
        
        public void searchButton()
        {
        	driver.findElement(By.xpath("//button[text()='Search']")).click();
        }

        public void validateHomePageIsDisplayed(Log Log, ScreenShots ScreenShots) {
            try {
                WebElement busTitlePage = driver.findElement(By.xpath("//*[text()='Book Buses']"));

                if (busTitlePage.isDisplayed()) {
                    Log.ReportEvent("PASS", "Home page is getting displayed successfully.");
                    System.out.println("Home page is getting displayed successfully.");
                } else {
                    Log.ReportEvent("FAIL", "Home page is not displayed.");
                    System.out.println("Home page is not displayed.");
                }

                ScreenShots.takeScreenShot1(); // Screenshot taken in both cases

            } catch (NoSuchElementException e) {
                Log.ReportEvent("FAIL", "Home page element not found.");
                System.out.println("Home page element not found.");
                ScreenShots.takeScreenShot1();
            } catch (Exception e) {
                Log.ReportEvent("FAIL", "An unexpected error occurred: " + e.getMessage());
                System.out.println("Unexpected error: " + e.getMessage());
                ScreenShots.takeScreenShot1();
            }
        }

        public String[] userSearchDate() {
            String[] result = new String[3]; // [from, to, date]

            try {
                String fromLocation = driver.findElement(By.xpath("(//*[contains(@class,'singleValue')])[1]")).getText();
                String toLocation = driver.findElement(By.xpath("(//*[contains(@class,'singleValue')])[2]")).getText();
                String userEnterDate = driver.findElement(By.xpath("//input[contains(@class,'tg-bs-date')]")).getAttribute("value");

                result[0] = fromLocation;
                result[1] = toLocation;
                result[2] = userEnterDate;

            } catch (Exception e) {
                System.out.println("Exception while fetching search data: " + e.getMessage());
                e.printStackTrace();

                // Optional: set default or empty values
                result[0] = "N/A";
                result[1] = "N/A";
                result[2] = "N/A";
            }

            return result;
        }

}

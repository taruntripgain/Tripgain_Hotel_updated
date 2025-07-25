
package com.tripgain.hotaltestscripts;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tripgain.collectionofpages.*;
import com.tripgain.common.*;
import com.tripgain.hotels.collectionofpages.Tripgain_Hotel_ResultPage;
import com.tripgain.hotels.collectionofpages.Tripgain_Hotel_homepage;
import com.tripgain.testscripts.BaseClass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;

@Listeners(TestListener.class)
public class TCH_02_VerifySearchFunctionalityForHotalForFiveDays extends BaseClass {

	WebDriver driver;    
	ExtentReports extent;
	ExtentTest test;
	String className = "";
	Log Log;  // Declare Log object
	ScreenShots screenShots;  // Declare Log object
	ExtantManager extantManager;
	// ThreadLocal to store Excel data per test thread
	static ThreadLocal<Map<String, String>> excelDataThread = new ThreadLocal<>();

	int number=1;

	@Test(dataProvider = "sheetBasedData", dataProviderClass = DataProviderUtils.class)
	public void myTest(Map<String, String> excelData) throws InterruptedException, IOException {
		System.out.println("Running test with: " + excelData);
		//To get Data from Excel
		try{
			String userName1 = excelData.get("UserName");
			String password1 = excelData.get("Password");
	        String[] dates=GenerateDates.GenerateDatesToSelectFlights();
	        String fromDate=dates[0];
			String returnDate=dates[1];
			String fromMonthYear=dates[2];
			String returnMonthYear=dates[3];
			number++;
			String city = excelData.get("City");
			int Index = Integer.parseInt(excelData.get("index"));
            int roomcount = Integer.parseInt(excelData.get("roomCount"));
			int adultcount = Integer.parseInt(excelData.get("adultCount"));
			int childcount = Integer.parseInt(excelData.get("childCount"));
			int childAge = Integer.parseInt(excelData.get("ChildAge"));

			test.log(Status.INFO, "Hotel city search :" +" "+city);
		    test.log(Status.INFO, "Number of rooms: :" +" "+roomcount);
		    test.log(Status.INFO, "Number of Adult :" +" "+adultcount);
		    test.log(Status.INFO, "Number of child:" +" "+childcount);
		    test.log(Status.INFO, "Flight From Date:" +" "+fromDate+" "+"and MonthAndYear:"+" "+fromMonthYear);
		    test.log(Status.INFO, "Flight Return Date:" +" "+returnDate+" "+"and MonthAndYear:"+" "+returnMonthYear);

			//Functions to Login TripGain Application
			Tripgain_Login tripgainLogin= new Tripgain_Login(driver);
			Tripgain_Hotel_homepage hotelHomepage = new Tripgain_Hotel_homepage(driver);
			Tripgain_Hotel_ResultPage resultpage = new Tripgain_Hotel_ResultPage(driver);

			tripgainLogin.enterUserName(userName1);
			tripgainLogin.enterPasswordName(password1);
			tripgainLogin.clickButton();
			Thread.sleep(2000);
			hotelHomepage.hotelClick();
			hotelHomepage.validateHomePgaeIsDisplayed(Log, screenShots);
            hotelHomepage.clickOnPropertySearchfield(city);
		    hotelHomepage.selectDate(fromDate, fromMonthYear);
			hotelHomepage.selectReturnDate(returnDate, returnMonthYear);
			hotelHomepage.addRoom(roomcount,adultcount,childcount,childAge);
			long startTime = System.currentTimeMillis();
            hotelHomepage.clickOnSearch();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[@title]")));
            long endTime = System.currentTimeMillis();
			long loadTimeInSeconds = (endTime - startTime) / 1000;
			Log.ReportEvent("INFO", "Hotel search results loaded in " + loadTimeInSeconds + " seconds");

			resultpage.getFlightsCount(Log, screenShots);
			resultpage.validateResultScreen(Log, screenShots);
			String GetHotelNameInResultPage = resultpage.getHotelNameInResultPage(Log, screenShots, Index);
			resultpage.selectHotelsBasedOnIndex(Index);
			String GetHotelNameInDetailPage = resultpage.getHotelNameInDetailPage(Log, screenShots);
			resultpage.ValidateHotelDetail(GetHotelNameInResultPage, GetHotelNameInDetailPage, Log, screenShots);
			resultpage.clickOnSaveAndContinueButton(Log, screenShots);
			resultpage.validateReviewYourBookingPage(Log, screenShots);
			

		}catch (Exception e)
		{
			String errorMessage = "Exception occurred: " + e.toString();
			Log.ReportEvent("FAIL", errorMessage);
			screenShots.takeScreenShot();
			e.printStackTrace();  // You already have this, good for console logs
			Assert.fail(errorMessage);
		}

	}

	@BeforeMethod(alwaysRun = true)
	@Parameters("browser")
	public void launchApplication(String browser, Method method, Object[] testDataObjects) {
		// Get test data passed from DataProvider
		@SuppressWarnings("unchecked")
		Map<String, String> testData = (Map<String, String>) testDataObjects[0];
		excelDataThread.set(testData);  // Set it early!

		String url = (testData != null && testData.get("URL") != null) ? testData.get("URL") : "https://defaulturl.com";

		extantManager = new ExtantManager();
		extantManager.setUpExtentReporter(browser);
		className = this.getClass().getSimpleName();
		String testName = className + "_" + number;
		extantManager.createTest(testName);
		test = ExtantManager.getTest();
		extent = extantManager.getReport();
		test.log(Status.INFO, "Execution Started Successfully");

		driver = launchBrowser(browser, url);
		Log = new Log(driver, test);
		screenShots = new ScreenShots(driver, test);
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
			extantManager.flushReport();
		}
	}



}

package com.tripgain.busestestscript1;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tripgain.bus.collectionsofpages1.Tripgain_Bus_BookingPage;
import com.tripgain.bus.collectionsofpages1.Tripgain_Bus_Homepage;
import com.tripgain.bus.collectionsofpages1.Tripgain_Bus_ResultPage;
import com.tripgain.collectionofpages.Tripgain_Login;
import com.tripgain.common.DataProviderUtils;
import com.tripgain.common.ExtantManager;
import com.tripgain.common.GenerateDates;
import com.tripgain.common.Log;
import com.tripgain.common.ScreenShots;
import com.tripgain.testscripts.BaseClass;

public class TCB_02_VerifySearchFunctionalityForBusForFiveDays extends BaseClass {
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
			String fromMonthYear=dates[2];

			number++;
			int boardingPointIndex = Integer.parseInt(excelData.get("BoardingPointIndex"));

			String citySource = excelData.get("CitySource");
			int seatCount = Integer.parseInt(excelData.get("SeatCount"));
			int dropingPointIndex = Integer.parseInt(excelData.get("DropingPointIndex"));
			int viewBus = Integer.parseInt(excelData.get("ViewBus"));
            String cityDestination = excelData.get("CityDestination");
            
			test.log(Status.INFO, "Bus boarding city search :" +" "+boardingPointIndex);
			test.log(Status.INFO, "Bus droping city search: :" +" "+dropingPointIndex);
			test.log(Status.INFO, "Bus boarding date:" +" "+fromDate+" "+"and MonthAndYear:"+" "+fromMonthYear);
			
			Tripgain_Bus_Homepage bushomepage= new Tripgain_Bus_Homepage(driver);
			Tripgain_Login tripgainLogin= new Tripgain_Login(driver);
			Tripgain_Bus_ResultPage tripgainresult = new Tripgain_Bus_ResultPage(driver);
			Tripgain_Bus_BookingPage tripgainbookingpage = new Tripgain_Bus_BookingPage(driver);
			tripgainLogin.enterUserName(userName1);
			tripgainLogin.enterPasswordName(password1);
			tripgainLogin.clickButton();
			Thread.sleep(2000);
			bushomepage.busClick();
			bushomepage.validateHomePgaeIsDisplayed(Log, screenShots);

			bushomepage.enterCityOrHotelName(citySource);
			bushomepage.enterCityOrHotelNameDestination(cityDestination);
			bushomepage.selectDate(fromDate,fromMonthYear,Log, screenShots);
			String[] userInput = bushomepage.UserInput(Log, screenShots);
			long startTime = System.currentTimeMillis();
			bushomepage.clickOnSearch(Log, screenShots);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(2));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[text()='View Seats'])[1]")));
			long endTime = System.currentTimeMillis();
			long loadTimeInSeconds = (endTime - startTime) / 1000;
			//result page
			tripgainresult.validateResultPageIsDisplayed(Log, screenShots);
			tripgainresult.getOperatorsName(Log, screenShots);
			tripgainresult.clickOnViewSeatButton(Log, screenShots,viewBus);
			tripgainresult.clickOnBoardingPoint(boardingPointIndex,Log, screenShots);
			tripgainresult.clickOnDropingPoint(dropingPointIndex,Log, screenShots);
			tripgainresult.pickSeat(seatCount,Log, screenShots);
			List<String> getSeatNames = (List<String>) tripgainresult.getSeatNames(Log, screenShots);
			String[] getTextInResultPage = tripgainresult.getTextInResultPage();
			String totalAmount = tripgainresult.selectedTotalAmountPrice();

			tripgainresult.clickOnConfirmSeat(Log, screenShots);
			tripgainresult.reasonForSelectionPopUp1(Log, screenShots);
			//booking page 
			tripgainbookingpage.validateReviewYourTripPage(Log, screenShots);
			List<String> fetchSeatInBookingPage = tripgainbookingpage.fetchSeatInBookingPage(Log, screenShots);
			String[] getTextInBookingPage = tripgainbookingpage.getTextInBookingPage();
			tripgainbookingpage.validateResultAndBookingPageData(getTextInResultPage,getTextInBookingPage,getSeatNames,fetchSeatInBookingPage,Log, screenShots);
			tripgainbookingpage.validateUserSearchDataAndBookingPageData(userInput,getTextInBookingPage,Log, screenShots);
			tripgainbookingpage.validatePriceInBookingPage(totalAmount,Log, screenShots);

		}
		catch (Exception e)
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
			//driver.quit();
			extantManager.flushReport();
		}
	}

}

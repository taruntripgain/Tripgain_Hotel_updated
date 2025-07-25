
package com.tripgain.busestestscript;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tripgain.bus.collectionofpages.Tripgain_bus_bookingPage;
import com.tripgain.bus.collectionofpages.Tripgain_bus_homepage;
import com.tripgain.bus.collectionofpages.Tripgain_bus_resultPage;
import com.tripgain.collectionofpages.*;
import com.tripgain.common.*;

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
import java.util.List;
import java.util.Map;

@Listeners(TestListener.class)
public class TCB_01_VerifySearchFunctionalityForBuses extends BaseClass {

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
			
			
			number++;
			
			String FromLocation = excelData.get("FromLocation");
			String ToLocation = excelData.get("ToLocation");
			String travelOption = excelData.get("TravelOption");
			int selectBusBasedOnIndex = Integer.parseInt(excelData.get("selectBusBasedOnIndex"));


			test.log(Status.INFO, "Bus FromLocation :" +" "+FromLocation);
			test.log(Status.INFO, "Bus ToLocation :" +" "+ToLocation);

			//Functions to Login TripGain Application
			Tripgain_Login tripgainLogin= new Tripgain_Login(driver);
			
			tripgainLogin.enterUserName(userName1);
			tripgainLogin.enterPasswordName(password1);
			tripgainLogin.clickButton();
			Thread.sleep(2000);
			tripgainLogin.verifyHomePageIsDisplayed(Log,screenShots);

			//Functions to Search flights on Home Page
			Tripgain_homepage tripgainhomepage = new Tripgain_homepage(driver);

			tripgainhomepage.clickOnTravelDropDown();
			tripgainhomepage.selectTravelOptionsOnHomeScreen(travelOption);
			
			Tripgain_bus_homepage  tripgain_bus_homepage = new Tripgain_bus_homepage(driver);
			Tripgain_bus_resultPage tripgain_bus_resultpage = new Tripgain_bus_resultPage(driver);
			Tripgain_bus_bookingPage tripgain_bus_bookingPage = new Tripgain_bus_bookingPage(driver);
			 Tripgain_FutureDates futureDates = new Tripgain_FutureDates();
		        Map<String, Tripgain_FutureDates.DateResult> dateResults = futureDates.furtherDate();
		        Tripgain_FutureDates.DateResult date2 = dateResults.get("datePlus2");
		        String fromMonthYear = date2.month + " " + date2.year;
		       
		
		        tripgain_bus_homepage.validateHomePageIsDisplayed(Log, screenShots);        
		        tripgain_bus_homepage.enterFromLocation(FromLocation);
		        tripgain_bus_homepage.enterToLocation(ToLocation);
		        Thread.sleep(2000);
		        tripgain_bus_homepage.selectDate(date2.day,fromMonthYear);
		        String userEnterData[] =tripgain_bus_homepage.userSearchDate();
		        tripgain_bus_homepage.searchButton();
		        tripgain_bus_resultpage.validateResultPageIsDisplayed(Log, screenShots);
		        tripgain_bus_resultpage.getFirstBusOperator(Log, screenShots);
		       String busDeatail [] = tripgain_bus_resultpage.selectBusCardBasedOnIndex(selectBusBasedOnIndex);
		       String boardingPoint= tripgain_bus_resultpage.selectBoardingPoint(Log, screenShots);
		       String droppingPooint= tripgain_bus_resultpage.selectDroppingPoint(Log, screenShots);
		        tripgain_bus_resultpage.selectSeat();
		        List<String> selectedSeat  = tripgain_bus_resultpage.getSelectedSeat(selectBusBasedOnIndex,Log, screenShots);
		        
		       String totalAmount = tripgain_bus_resultpage.selectedTotalAmountPrice();
		       
		       tripgain_bus_resultpage.clickOnConfirmSeat();
		       
		       tripgain_bus_bookingPage.validateReviewYourTripPage(Log, screenShots);
		       tripgain_bus_bookingPage.validateBookingPage(userEnterData,busDeatail,boardingPoint,droppingPooint,Log, screenShots);
		       
		        
		       tripgain_bus_bookingPage.validateBookingPageSeat(selectedSeat,Log, screenShots);
		       tripgain_bus_bookingPage.validatePriceInBookingPage(totalAmount,Log, screenShots);
		        
		        
		        
		        
		        
		}catch (Exception e)
		{
			Log.ReportEvent("FAIL", "Occurred Exception"+ e.getMessage());
			screenShots.takeScreenShot1();
			e.printStackTrace();
			Assert.fail();
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
		//	driver.quit();
			extantManager.flushReport();
		}
	}



}

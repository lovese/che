/*
 * Copyright (c) 2012-2017 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.selenium.pageobject.dashboard;

import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.ELEMENT_TIMEOUT_SEC;
import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.EXPECTED_MESS_IN_CONSOLE_SEC;
import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.LOADER_TIMEOUT_SEC;
import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.LOAD_PAGE_TIMEOUT_SEC;
import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.REDRAW_UI_ELEMENTS_TIMEOUT_SEC;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javax.annotation.PreDestroy;
import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.provider.TestDashboardUrlProvider;
import org.eclipse.che.selenium.core.provider.TestIdeUrlProvider;
import org.eclipse.che.selenium.core.user.DefaultTestUser;
import org.eclipse.che.selenium.core.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Musienko Maxim */
@Singleton
public class Dashboard {
  protected final SeleniumWebDriver seleniumWebDriver;
  protected final DefaultTestUser defaultUser;

  private final TestIdeUrlProvider testIdeUrlProvider;
  private final TestDashboardUrlProvider testDashboardUrlProvider;

  @Inject
  public Dashboard(
      SeleniumWebDriver seleniumWebDriver,
      DefaultTestUser defaultUser,
      TestIdeUrlProvider testIdeUrlProvider,
      TestDashboardUrlProvider testDashboardUrlProvider) {
    this.seleniumWebDriver = seleniumWebDriver;
    this.defaultUser = defaultUser;
    this.testIdeUrlProvider = testIdeUrlProvider;
    this.testDashboardUrlProvider = testDashboardUrlProvider;
    PageFactory.initElements(seleniumWebDriver, this);
  }

  private interface Locators {
    String DASHBOARD_TOOLBAR_TITLE = "div [aria-label='Dashboard']";
    String NEW_PROJECT_LINK = "//a[@href='#/create-project']/span[text()='Create Workspace']";
    String COLLAPSE_DASH_NAVBAR_BTN = "ide-iframe-button-link";
    String DASHBOARD_ITEM_XPATH = "//a[@href='#/']//span[text()='Dashboard']";
    String WORKSPACES_ITEM_XPATH = "//a[@href='#/workspaces']//span[text()='Workspaces']";
    String FACTORIES_ITEM_XPATH = "//a[@href='#/factories']//span[text()='Factories']";
    String NOTIFICATION_CONTAINER = "che-notification-container";
    String DEVELOPERS_FACE_XPATH = "//img[@class='developers-face']";
    String USER_NAME = "//span[text()='%s']";
    String LICENSE_NAG_MESSAGE_XPATH = "//div[contains(@class, 'license-message')]";
  }

  @FindBy(css = Locators.DASHBOARD_TOOLBAR_TITLE)
  WebElement dashboardTitle;

  @FindBy(xpath = Locators.NEW_PROJECT_LINK)
  WebElement newProjectLink;

  @FindBy(id = Locators.COLLAPSE_DASH_NAVBAR_BTN)
  WebElement collapseDashNavbarBtn;

  @FindBy(xpath = Locators.WORKSPACES_ITEM_XPATH)
  WebElement workspacesItem;

  @FindBy(xpath = Locators.FACTORIES_ITEM_XPATH)
  WebElement factoriesItem;

  @FindBy(xpath = Locators.DEVELOPERS_FACE_XPATH)
  WebElement developersFace;

  @FindBy(xpath = Locators.DASHBOARD_ITEM_XPATH)
  WebElement dashboardItem;

  @FindBy(id = Locators.NOTIFICATION_CONTAINER)
  WebElement notificationPopUp;

  @FindBy(xpath = Locators.LICENSE_NAG_MESSAGE_XPATH)
  WebElement licenseNagMessage;

  /** wait button with drop dawn icon (left top corner) */
  public void waitDashboardToolbarTitle() {
    new WebDriverWait(seleniumWebDriver, LOAD_PAGE_TIMEOUT_SEC)
        .until(ExpectedConditions.visibilityOf(dashboardTitle));
  }

  public void clickOnNewProjectLinkOnDashboard() {
    waitDashboardToolbarTitle();
    newProjectLink.click();
  }

  /** click on the 'Workspaces' item on the dashboard */
  public void selectWorkspacesItemOnDashboard() {
    new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
        .until(ExpectedConditions.visibilityOf(workspacesItem))
        .click();
  }

  /** click on the 'Dashboard' item on the dashboard */
  public void selectDashboardItemOnDashboard() {
    new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
        .until(ExpectedConditions.visibilityOf(dashboardItem))
        .click();
  }

  /** click on the 'Factories' item on the dashboard */
  public void selectFactoriesOnDashbord() {
    new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
        .until(ExpectedConditions.visibilityOf(factoriesItem))
        .click();
  }

  /**
   * * wait expected text in the pop up notification
   *
   * @param notification
   */
  public void waitNotificationMessage(String notification) {
    new WebDriverWait(seleniumWebDriver, LOADER_TIMEOUT_SEC)
        .until(ExpectedConditions.textToBePresentInElement(notificationPopUp, notification));
  }

  /** wait closing of notification pop up */
  public void waitNotificationIsClosed() {
    WaitUtils.sleepQuietly(1);
    new WebDriverWait(seleniumWebDriver, ELEMENT_TIMEOUT_SEC)
        .until(
            ExpectedConditions.invisibilityOfElementLocated(
                By.id(Locators.NOTIFICATION_CONTAINER)));
  }

  /** wait opening of notification pop up */
  public void waitNotificationIsOpen() {
    new WebDriverWait(seleniumWebDriver, ELEMENT_TIMEOUT_SEC)
        .until(ExpectedConditions.visibilityOf(notificationPopUp));
  }

  /** Wait developer avatar is present on dashboard */
  public void waitDeveloperFaceImg() {
    new WebDriverWait(seleniumWebDriver, EXPECTED_MESS_IN_CONSOLE_SEC)
        .until(ExpectedConditions.visibilityOf(developersFace));
  }

  /**
   * Wait user name is present on dashboard
   *
   * @param userName name of user
   */
  public void checkUserName(String userName) {
    new WebDriverWait(seleniumWebDriver, EXPECTED_MESS_IN_CONSOLE_SEC)
        .until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath(String.format(Locators.USER_NAME, userName))));
  }

  /**
   * Get text of nag message which displays at the top of Dashboard, or throw an exception if nag
   * message doesn't appear in some time.
   */
  public String getLicenseNagMessage() {
    return new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
        .until(visibilityOf(licenseNagMessage))
        .getText();
  }

  /** Wait until nag message disappears. */
  public void waitNagMessageDisappear() {
    new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
        .until(invisibilityOfElementLocated(By.xpath(Locators.LICENSE_NAG_MESSAGE_XPATH)));
  }

  /** Open dashboard as default uses */
  public void open() {
    open(defaultUser.getAuthToken());
  }

  public void open(String authToken) {
    seleniumWebDriver.get(testIdeUrlProvider.get().toString());

    Cookie accessKey = new Cookie("session-access-key", authToken);
    seleniumWebDriver.manage().addCookie(accessKey);

    seleniumWebDriver.get(testDashboardUrlProvider.get().toString());

    // renew session to avoid an error "HTTP Status 403 - CSRF nonce validation failed" https://github.com/codenvy/codenvy/issues/2255
    seleniumWebDriver.get(testDashboardUrlProvider.get().toString());
  }

  public WebDriver driver() {
    return seleniumWebDriver;
  }

  @PreDestroy
  public void close() {
    seleniumWebDriver.quit();
  }
}

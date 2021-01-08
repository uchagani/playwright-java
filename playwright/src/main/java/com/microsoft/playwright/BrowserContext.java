/*
 * Copyright (c) Microsoft Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.microsoft.playwright;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * - extends: [EventEmitter](https://nodejs.org/api/events.html#events_class_eventemitter)
 * <p>
 * BrowserContexts provide a way to operate multiple independent browser sessions.
 * <p>
 * If a page opens another page, e.g. with a {@code window.open} call, the popup will belong to the parent page's browser
 * <p>
 * context.
 * <p>
 * Playwright allows creation of "incognito" browser contexts with {@code browser.newContext()} method. "Incognito" browser
 * <p>
 * contexts don't write any browsing data to disk.
 * <p>
 * 
 * <p>
 */
public interface BrowserContext {
  enum SameSite { STRICT, LAX, NONE }

  class HTTPCredentials {
    private final String username;
    private final String password;

    public HTTPCredentials(String username, String password) {
      this.username = username;
      this.password = password;
    }

    public String username() {
      return username;
    }

    public String password() {
      return password;
    }
  }

  class StorageState {
    public List<AddCookie> cookies;
    public List<OriginState> origins;

    public static class OriginState {
      public final String origin;
      public List<LocalStorageItem> localStorage;

      public static class LocalStorageItem {
        public String name;
        public String value;
        public LocalStorageItem(String name, String value) {
          this.name = name;
          this.value = value;
        }
      }

      public OriginState(String origin) {
        this.origin = origin;
      }

      public OriginState withLocalStorage(List<LocalStorageItem> localStorage) {
        this.localStorage = localStorage;
        return this;
      }
    }

    public StorageState() {
      cookies = new ArrayList<>();
      origins = new ArrayList<>();
    }

    public List<AddCookie> cookies() {
      return this.cookies;
    }
    public List<OriginState> origins() {
      return this.origins;
    }
  }

  class FutureEventOptions {
    public Integer timeout;
    public Predicate<Event<EventType>> predicate;
    public FutureEventOptions withTimeout(int millis) {
      timeout = millis;
      return this;
    }
    public FutureEventOptions withPredicate(Predicate<Event<EventType>> predicate) {
      this.predicate = predicate;
      return this;
    }
  }

  enum EventType {
    CLOSE,
    PAGE,
  }

  void addListener(EventType type, Listener<EventType> listener);
  void removeListener(EventType type, Listener<EventType> listener);
  class AddCookie {
    /**
     * **required**
     */
    public String name;
    /**
     * **required**
     */
    public String value;
    /**
     * either url or domain / path are required. Optional.
     */
    public String url;
    /**
     * either url or domain / path are required Optional.
     */
    public String domain;
    /**
     * either url or domain / path are required Optional.
     */
    public String path;
    /**
     * Unix time in seconds. Optional.
     */
    public Long expires;
    /**
     * Optional.
     */
    public Boolean httpOnly;
    /**
     * Optional.
     */
    public Boolean secure;
    /**
     * Optional.
     */
    public SameSite sameSite;

    public AddCookie withName(String name) {
      this.name = name;
      return this;
    }
    public AddCookie withValue(String value) {
      this.value = value;
      return this;
    }
    public AddCookie withUrl(String url) {
      this.url = url;
      return this;
    }
    public AddCookie withDomain(String domain) {
      this.domain = domain;
      return this;
    }
    public AddCookie withPath(String path) {
      this.path = path;
      return this;
    }
    public AddCookie withExpires(Long expires) {
      this.expires = expires;
      return this;
    }
    public AddCookie withHttpOnly(Boolean httpOnly) {
      this.httpOnly = httpOnly;
      return this;
    }
    public AddCookie withSecure(Boolean secure) {
      this.secure = secure;
      return this;
    }
    public AddCookie withSameSite(SameSite sameSite) {
      this.sameSite = sameSite;
      return this;
    }
  }
  class Cookie {
    private String name;
    private String value;
    private String domain;
    private String path;
    /**
     * Unix time in seconds.
     */
    private long expires;
    private boolean httpOnly;
    private boolean secure;
    private SameSite sameSite;

    public String name() {
      return this.name;
    }
    public String value() {
      return this.value;
    }
    public String domain() {
      return this.domain;
    }
    public String path() {
      return this.path;
    }
    public long expires() {
      return this.expires;
    }
    public boolean httpOnly() {
      return this.httpOnly;
    }
    public boolean secure() {
      return this.secure;
    }
    public SameSite sameSite() {
      return this.sameSite;
    }
  }
  class ExposeBindingOptions {
    /**
     * Whether to pass the argument as a handle, instead of passing by value. When passing a handle, only one argument is
     * supported. When passing by value, multiple arguments are supported.
     */
    public Boolean handle;

    public ExposeBindingOptions withHandle(Boolean handle) {
      this.handle = handle;
      return this;
    }
  }
  class GrantPermissionsOptions {
    /**
     * The [origin] to grant permissions to, e.g. "https://example.com".
     */
    public String origin;

    public GrantPermissionsOptions withOrigin(String origin) {
      this.origin = origin;
      return this;
    }
  }
  class StorageStateOptions {
    /**
     * The file path to save the storage state to. If {@code path} is a relative path, then it is resolved relative to
     * [current working directory](https://nodejs.org/api/process.html#process_process_cwd). If no path is provided, storage
     * state is still returned, but won't be saved to the disk.
     */
    public Path path;

    public StorageStateOptions withPath(Path path) {
      this.path = path;
      return this;
    }
  }
  /**
   * Adds cookies into this browser context. All pages within this context will have these cookies installed. Cookies can be
   * <p>
   * obtained via [{@code method: BrowserContext.cookies}].
   * <p>
   * 
   * <p>
   */
  void addCookies(List<AddCookie> cookies);
  default void addInitScript(String script) {
    addInitScript(script, null);
  }
  /**
   * Adds a script which would be evaluated in one of the following scenarios:
   * <p>
   * - Whenever a page is created in the browser context or is navigated.
   * <p>
   * - Whenever a child frame is attached or navigated in any page in the browser context. In this case, the script is
   * <p>
   *   evaluated in the context of the newly attached frame.
   * <p>
   * The script is evaluated after the document was created but before any of its scripts were run. This is useful to amend
   * <p>
   * the JavaScript environment, e.g. to seed {@code Math.random}.
   * <p>
   * 
   * <p>
   * 
   * <p>
   * > <strong>NOTE</strong> The order of evaluation of multiple scripts installed via [{@code method: BrowserContext.addInitScript}] and
   * <p>
   * [{@code method: Page.addInitScript}] is not defined.
   * @param script Script to be evaluated in all pages in the browser context.
   * @param arg Optional argument to pass to {@code script} (only supported when passing a function).
   */
  void addInitScript(String script, Object arg);
  /**
   * Returns the browser instance of the context. If it was launched as a persistent context null gets returned.
   */
  Browser browser();
  /**
   * Clears context cookies.
   */
  void clearCookies();
  /**
   * Clears all permission overrides for the browser context.
   * <p>
   * 
   * <p>
   */
  void clearPermissions();
  /**
   * Closes the browser context. All the pages that belong to the browser context will be closed.
   * <p>
   * > <strong>NOTE</strong> the default browser context cannot be closed.
   */
  void close();
  default List<Cookie> cookies() { return cookies((List<String>) null); }
  default List<Cookie> cookies(String url) { return cookies(Arrays.asList(url)); }
  /**
   * If no URLs are specified, this method returns all cookies. If URLs are specified, only cookies that affect those URLs
   * <p>
   * are returned.
   * @param urls Optional list of URLs.
   */
  List<Cookie> cookies(List<String> urls);
  default void exposeBinding(String name, Page.Binding callback) {
    exposeBinding(name, callback, null);
  }
  /**
   * The method adds a function called {@code name} on the {@code window} object of every frame in every page in the context. When
   * <p>
   * called, the function executes {@code callback} and returns a [Promise] which resolves to the return value of {@code callback}. If
   * <p>
   * the {@code callback} returns a [Promise], it will be awaited.
   * <p>
   * The first argument of the {@code callback} function contains information about the caller: `{ browserContext: BrowserContext,
   * <p>
   * page: Page, frame: Frame }`.
   * <p>
   * See [{@code method: Page.exposeBinding}] for page-only version.
   * <p>
   * 
   * <p>
   * 
   * <p>
   * 
   * @param name Name of the function on the window object.
   * @param callback Callback function that will be called in the Playwright's context.
   */
  void exposeBinding(String name, Page.Binding callback, ExposeBindingOptions options);
  /**
   * The method adds a function called {@code name} on the {@code window} object of every frame in every page in the context. When
   * <p>
   * called, the function executes {@code callback} and returns a [Promise] which resolves to the return value of {@code callback}.
   * <p>
   * If the {@code callback} returns a [Promise], it will be awaited.
   * <p>
   * See [{@code method: Page.exposeFunction}] for page-only version.
   * <p>
   * 
   * <p>
   * 
   * @param name Name of the function on the window object.
   * @param callback Callback function that will be called in the Playwright's context.
   */
  void exposeFunction(String name, Page.Function callback);
  default void grantPermissions(List<String> permissions) {
    grantPermissions(permissions, null);
  }
  /**
   * Grants specified permissions to the browser context. Only grants corresponding permissions to the given origin if
   * <p>
   * specified.
   * @param permissions A permission or an array of permissions to grant. Permissions can be one of the following values:
   * - {@code 'geolocation'}
   * - {@code 'midi'}
   * - {@code 'midi-sysex'} (system-exclusive midi)
   * - {@code 'notifications'}
   * - {@code 'push'}
   * - {@code 'camera'}
   * - {@code 'microphone'}
   * - {@code 'background-sync'}
   * - {@code 'ambient-light-sensor'}
   * - {@code 'accelerometer'}
   * - {@code 'gyroscope'}
   * - {@code 'magnetometer'}
   * - {@code 'accessibility-events'}
   * - {@code 'clipboard-read'}
   * - {@code 'clipboard-write'}
   * - {@code 'payment-handler'}
   */
  void grantPermissions(List<String> permissions, GrantPermissionsOptions options);
  /**
   * Creates a new page in the browser context.
   */
  Page newPage();
  /**
   * Returns all open pages in the context. Non visible pages, such as {@code "background_page"}, will not be listed here. You can
   * <p>
   * find them using [{@code method: ChromiumBrowserContext.backgroundPages}].
   */
  List<Page> pages();
  void route(String url, Consumer<Route> handler);
  void route(Pattern url, Consumer<Route> handler);
  /**
   * Routing provides the capability to modify network requests that are made by any page in the browser context. Once route
   * <p>
   * is enabled, every request matching the url pattern will stall unless it's continued, fulfilled or aborted.
   * <p>
   * 
   * <p>
   * or the same snippet using a regex pattern instead:
   * <p>
   * 
   * <p>
   * Page routes (set up with [{@code method: Page.route}]) take precedence over browser context routes when request matches both
   * <p>
   * handlers.
   * <p>
   * > <strong>NOTE</strong> Enabling routing disables http cache.
   * @param url A glob pattern, regex pattern or predicate receiving [URL] to match while routing.
   * @param handler handler function to route the request.
   */
  void route(Predicate<String> url, Consumer<Route> handler);
  /**
   * This setting will change the default maximum navigation time for the following methods and related shortcuts:
   * <p>
   * - [{@code method: Page.goBack}]
   * <p>
   * - [{@code method: Page.goForward}]
   * <p>
   * - [{@code method: Page.goto}]
   * <p>
   * - [{@code method: Page.reload}]
   * <p>
   * - [{@code method: Page.setContent}]
   * <p>
   * - [{@code method: Page.waitForNavigation}]
   * <p>
   * > <strong>NOTE</strong> [{@code method: Page.setDefaultNavigationTimeout}] and [{@code method: Page.setDefaultTimeout}] take priority over
   * <p>
   * [{@code method: BrowserContext.setDefaultNavigationTimeout}].
   * @param timeout Maximum navigation time in milliseconds
   */
  void setDefaultNavigationTimeout(int timeout);
  /**
   * This setting will change the default maximum time for all the methods accepting {@code timeout} option.
   * <p>
   * > <strong>NOTE</strong> [{@code method: Page.setDefaultNavigationTimeout}], [{@code method: Page.setDefaultTimeout}] and
   * <p>
   * [{@code method: BrowserContext.setDefaultNavigationTimeout}] take priority over [{@code method: BrowserContext.setDefaultTimeout}].
   * @param timeout Maximum time in milliseconds
   */
  void setDefaultTimeout(int timeout);
  /**
   * The extra HTTP headers will be sent with every request initiated by any page in the context. These headers are merged
   * <p>
   * with page-specific extra HTTP headers set with [{@code method: Page.setExtraHTTPHeaders}]. If page overrides a particular
   * <p>
   * header, page-specific header value will be used instead of the browser context header value.
   * <p>
   * > <strong>NOTE</strong> {@code browserContext.setExtraHTTPHeaders} does not guarantee the order of headers in the outgoing requests.
   * @param headers An object containing additional HTTP headers to be sent with every request. All header values must be strings.
   */
  void setExtraHTTPHeaders(Map<String, String> headers);
  /**
   * Sets the context's geolocation. Passing {@code null} or {@code undefined} emulates position unavailable.
   * <p>
   * 
   * <p>
   * > <strong>NOTE</strong> Consider using [{@code method: BrowserContext.grantPermissions}] to grant permissions for the browser context pages
   * <p>
   * to read its geolocation.
   */
  void setGeolocation(Geolocation geolocation);
  /**
   * 
   * @param offline Whether to emulate network being offline for the browser context.
   */
  void setOffline(boolean offline);
  default StorageState storageState() {
    return storageState(null);
  }
  /**
   * Returns storage state for this browser context, contains current cookies and local storage snapshot.
   */
  StorageState storageState(StorageStateOptions options);
  default void unroute(String url) { unroute(url, null); }
  default void unroute(Pattern url) { unroute(url, null); }
  default void unroute(Predicate<String> url) { unroute(url, null); }
  void unroute(String url, Consumer<Route> handler);
  void unroute(Pattern url, Consumer<Route> handler);
  /**
   * Removes a route created with [{@code method: BrowserContext.route}]. When {@code handler} is not specified, removes all routes for
   * <p>
   * the {@code url}.
   * @param url A glob pattern, regex pattern or predicate receiving [URL] used to register a routing with
   * [{@code method: BrowserContext.route}].
   * @param handler Optional handler function used to register a routing with [{@code method: BrowserContext.route}].
   */
  void unroute(Predicate<String> url, Consumer<Route> handler);
  default Deferred<Event<EventType>> futureEvent(EventType event) {
    return futureEvent(event, (FutureEventOptions) null);
  }
  default Deferred<Event<EventType>> futureEvent(EventType event, Predicate<Event<EventType>> predicate) {
    FutureEventOptions options = new FutureEventOptions();
    options.predicate = predicate;
    return futureEvent(event, options);
  }
  /**
   * Waits for event to fire and passes its value into the predicate function. Returns when the predicate returns truthy
   * <p>
   * value. Will throw an error if the context closes before the event is fired. Returns the event data value.
   * <p>
   * 
   * <p>
   * 
   * @param event Event name, same one would pass into {@code browserContext.on(event)}.
   */
  Deferred<Event<EventType>> futureEvent(EventType event, FutureEventOptions options);
}


/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2016-07-08 17:28:43 UTC)
 * on 2016-10-06 at 23:44:40 UTC 
 * Modify at your own risk.
 */

package com.example.grant.myapplication.backend.myApi;

/**
 * Service definition for MyApi (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link MyApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class MyApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.22.0 of the myApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://myApplicationId.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "myApi/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public MyApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  MyApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getAttributes".
   *
   * This request holds the parameters needed by the myApi server.  After setting any optional
   * parameters, call the {@link GetAttributes#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public GetAttributes getAttributes() throws java.io.IOException {
    GetAttributes result = new GetAttributes();
    initialize(result);
    return result;
  }

  public class GetAttributes extends MyApiRequest<com.example.grant.myapplication.backend.myApi.model.IAHBean> {

    private static final String REST_PATH = "iahbean";

    /**
     * Create a request for the method "getAttributes".
     *
     * This request holds the parameters needed by the the myApi server.  After setting any optional
     * parameters, call the {@link GetAttributes#execute()} method to invoke the remote operation. <p>
     * {@link GetAttributes#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientR
     * equest)} must be called to initialize this instance immediately after invoking the constructor.
     * </p>
     *
     * @since 1.13
     */
    protected GetAttributes() {
      super(MyApi.this, "GET", REST_PATH, null, com.example.grant.myapplication.backend.myApi.model.IAHBean.class);
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetAttributes setAlt(java.lang.String alt) {
      return (GetAttributes) super.setAlt(alt);
    }

    @Override
    public GetAttributes setFields(java.lang.String fields) {
      return (GetAttributes) super.setFields(fields);
    }

    @Override
    public GetAttributes setKey(java.lang.String key) {
      return (GetAttributes) super.setKey(key);
    }

    @Override
    public GetAttributes setOauthToken(java.lang.String oauthToken) {
      return (GetAttributes) super.setOauthToken(oauthToken);
    }

    @Override
    public GetAttributes setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetAttributes) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetAttributes setQuotaUser(java.lang.String quotaUser) {
      return (GetAttributes) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetAttributes setUserIp(java.lang.String userIp) {
      return (GetAttributes) super.setUserIp(userIp);
    }

    @Override
    public GetAttributes set(String parameterName, Object value) {
      return (GetAttributes) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getImage".
   *
   * This request holds the parameters needed by the myApi server.  After setting any optional
   * parameters, call the {@link GetImage#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public GetImage getImage(java.lang.Integer id) throws java.io.IOException {
    GetImage result = new GetImage(id);
    initialize(result);
    return result;
  }

  public class GetImage extends MyApiRequest<com.example.grant.myapplication.backend.myApi.model.ImageBean> {

    private static final String REST_PATH = "imagebean/{id}";

    /**
     * Create a request for the method "getImage".
     *
     * This request holds the parameters needed by the the myApi server.  After setting any optional
     * parameters, call the {@link GetImage#execute()} method to invoke the remote operation. <p>
     * {@link
     * GetImage#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected GetImage(java.lang.Integer id) {
      super(MyApi.this, "GET", REST_PATH, null, com.example.grant.myapplication.backend.myApi.model.ImageBean.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetImage setAlt(java.lang.String alt) {
      return (GetImage) super.setAlt(alt);
    }

    @Override
    public GetImage setFields(java.lang.String fields) {
      return (GetImage) super.setFields(fields);
    }

    @Override
    public GetImage setKey(java.lang.String key) {
      return (GetImage) super.setKey(key);
    }

    @Override
    public GetImage setOauthToken(java.lang.String oauthToken) {
      return (GetImage) super.setOauthToken(oauthToken);
    }

    @Override
    public GetImage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetImage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetImage setQuotaUser(java.lang.String quotaUser) {
      return (GetImage) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetImage setUserIp(java.lang.String userIp) {
      return (GetImage) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Integer id;

    /**

     */
    public java.lang.Integer getId() {
      return id;
    }

    public GetImage setId(java.lang.Integer id) {
      this.id = id;
      return this;
    }

    @Override
    public GetImage set(String parameterName, Object value) {
      return (GetImage) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "postImage".
   *
   * This request holds the parameters needed by the myApi server.  After setting any optional
   * parameters, call the {@link PostImage#execute()} method to invoke the remote operation.
   *
   * @param picture
   * @param user
   * @param lat
   * @param lon
   * @return the request
   */
  public PostImage postImage(java.lang.String picture, java.lang.String user, java.lang.Double lat, java.lang.Double lon) throws java.io.IOException {
    PostImage result = new PostImage(picture, user, lat, lon);
    initialize(result);
    return result;
  }

  public class PostImage extends MyApiRequest<com.example.grant.myapplication.backend.myApi.model.MyBean> {

    private static final String REST_PATH = "postImage/{picture}/{user}/{lat}/{lon}";

    /**
     * Create a request for the method "postImage".
     *
     * This request holds the parameters needed by the the myApi server.  After setting any optional
     * parameters, call the {@link PostImage#execute()} method to invoke the remote operation. <p>
     * {@link
     * PostImage#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param picture
     * @param user
     * @param lat
     * @param lon
     * @since 1.13
     */
    protected PostImage(java.lang.String picture, java.lang.String user, java.lang.Double lat, java.lang.Double lon) {
      super(MyApi.this, "POST", REST_PATH, null, com.example.grant.myapplication.backend.myApi.model.MyBean.class);
      this.picture = com.google.api.client.util.Preconditions.checkNotNull(picture, "Required parameter picture must be specified.");
      this.user = com.google.api.client.util.Preconditions.checkNotNull(user, "Required parameter user must be specified.");
      this.lat = com.google.api.client.util.Preconditions.checkNotNull(lat, "Required parameter lat must be specified.");
      this.lon = com.google.api.client.util.Preconditions.checkNotNull(lon, "Required parameter lon must be specified.");
    }

    @Override
    public PostImage setAlt(java.lang.String alt) {
      return (PostImage) super.setAlt(alt);
    }

    @Override
    public PostImage setFields(java.lang.String fields) {
      return (PostImage) super.setFields(fields);
    }

    @Override
    public PostImage setKey(java.lang.String key) {
      return (PostImage) super.setKey(key);
    }

    @Override
    public PostImage setOauthToken(java.lang.String oauthToken) {
      return (PostImage) super.setOauthToken(oauthToken);
    }

    @Override
    public PostImage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (PostImage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public PostImage setQuotaUser(java.lang.String quotaUser) {
      return (PostImage) super.setQuotaUser(quotaUser);
    }

    @Override
    public PostImage setUserIp(java.lang.String userIp) {
      return (PostImage) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String picture;

    /**

     */
    public java.lang.String getPicture() {
      return picture;
    }

    public PostImage setPicture(java.lang.String picture) {
      this.picture = picture;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String user;

    /**

     */
    public java.lang.String getUser() {
      return user;
    }

    public PostImage setUser(java.lang.String user) {
      this.user = user;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Double lat;

    /**

     */
    public java.lang.Double getLat() {
      return lat;
    }

    public PostImage setLat(java.lang.Double lat) {
      this.lat = lat;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Double lon;

    /**

     */
    public java.lang.Double getLon() {
      return lon;
    }

    public PostImage setLon(java.lang.Double lon) {
      this.lon = lon;
      return this;
    }

    @Override
    public PostImage set(String parameterName, Object value) {
      return (PostImage) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link MyApi}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link MyApi}. */
    @Override
    public MyApi build() {
      return new MyApi(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link MyApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setMyApiRequestInitializer(
        MyApiRequestInitializer myapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(myapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}

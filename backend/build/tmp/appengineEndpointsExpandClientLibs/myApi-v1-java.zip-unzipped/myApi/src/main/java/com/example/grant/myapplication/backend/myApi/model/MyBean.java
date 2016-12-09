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
 * (build: 2016-10-17 16:43:55 UTC)
 * on 2016-12-09 at 08:37:12 UTC 
 * Modify at your own risk.
 */

package com.example.grant.myapplication.backend.myApi.model;

/**
 * Model definition for MyBean.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the myApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MyBean extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean data;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String info;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer postID;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getData() {
    return data;
  }

  /**
   * @param data data or {@code null} for none
   */
  public MyBean setData(java.lang.Boolean data) {
    this.data = data;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getInfo() {
    return info;
  }

  /**
   * @param info info or {@code null} for none
   */
  public MyBean setInfo(java.lang.String info) {
    this.info = info;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getPostID() {
    return postID;
  }

  /**
   * @param postID postID or {@code null} for none
   */
  public MyBean setPostID(java.lang.Integer postID) {
    this.postID = postID;
    return this;
  }

  @Override
  public MyBean set(String fieldName, Object value) {
    return (MyBean) super.set(fieldName, value);
  }

  @Override
  public MyBean clone() {
    return (MyBean) super.clone();
  }

}

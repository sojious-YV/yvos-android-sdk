# Youverify Android SDK

The official SDK for Youverify OS. The SDK provides you with a seamless way to integrate Youverify services into your Android app. With the SDK your users can fill out forms created with our Workflow builder, perform a liveness check and a document capture while you're provided with the status and details of each process in-app.

The Android SDK supports both _Kotlin_ and _Java_, but we strongly recommend using _Kotlin_.

## Requirement

Your app must declare a `minimum SDK version` of at least **24** (Android 6.0 and above).

## Including in your project

[![Maven Central](https://img.shields.io/maven-central/v/com.github.skydoves/balloon.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22balloon%22)

### Gradle

Add the dependency below to your **module**'s `build.gradle` file located under `app/build.grade`:

```gradle
dependencies {
    implementation "co.youverify.yvos:android:1.0.0"
}
```

## How to Use

The SDK offers 3 services separated into **Modules**. Each Module provides a `Builder` class to instantiate and gain access to its functionality.

You can also provide optional parameters like:

- **User personal information**,
- **customization options**
- **metadata ( to be passed to your webhook Url)"** to the `Builder object`.

The customization options are used to customize the SDK's UI to align with your app's theme and also show a personalized message (using the user's first name) to the user before any of the processes starts while the user information is either used to automatically fill in some fields in the form or is stored alongside the result of a successful liveness check to our remote server.
The SDK supports both Kotlin and Java projects, so you can reference it in your language.

### Using the WorkflowBuilder Module with Kotlin

First off, create an instance of the WorkflowBuilderModule with the `WorkflowBuilderModule.Builder` class, passing in your merchant key and the template Id of the form to be displayed.

```kotlin
val workflowBuilderModule = WorkflowBuilderModule.Builder(publicMerchantKey = "61d880f1e8e18f1a", formId = "64a6c1501dae409be")
            .onSuccess { formData ->
                //form was submitted successfully. perform some action here.
                //"formData" is a JSON String containing key-value pairs of the form fields and their values.
                //say you wanted to retrieve the value that was filled for the "first name" field in the form:
                //val  userFirstName  = JSONObject(formData).getJSONObject("fields").getString("firstName");

            }
            .onFailed {
                //form submission failed due to some error. perform some action here
            }
            .build()
```

#### Specifying optional parameters:

```kotlin
val workflowBuilderModule = WorkflowBuilderModule.Builder(publicMerchantKey = "61d880f1e8f1a", formId = "64a6c15e409be" )
            .dev(true) // only set to true if you're in development mode. The default value is false.
            .userInfo(
                //specify user details to be automatically filled and omitted from the form
                UserInfo.Builder()
                    .firstName("Jagaban")
                    .lastName("Cynthia")
                    .gender(GenderType.FEMALE)
                    .mobile("08011223344")
                    .
                    .
                    .build()
            )
            .customization(
                // customize the UI and the message shown to your user before the form shows up
                Customization.Builder()
                    .primaryColor("#ffffff")
                    .greetingMessage("We will need to verify your identity. It will only take a moment.")
                    .actionButtonText("Proceed")
                    .actionButtonTextColor("#46B2C8")
                    .
                    .
                    .build()
            )
            .onSuccess { formData ->
                //form was submitted successfully. perform some action here
            }
            .onFailed {
                //form submission failed due to some error. perform some action here
            }
            .onCompleted {formData ->
                //form was submitted successfully. This is called immediately after "0nSuccess()".
                // perform some action here
            }
            .metaData(emptyMap()) // Any other inormation you wish to pass to your Webhook url
            .build()
```

#### Display the form

Call `start()` on the `WorkflowBuilderModule` instance passing in a `Context` object to show the form to your user:

```kotlin
 workflowBuilderModule.start(context)
```

#### Cancel the process

The process is automatically canceled once either of the `onSuccess` or `onCompleted` callbacks returns, but you can also cancel manually by calling `close()`:

```kotlin
 workflowBuilderModule.close() // cancel the process
```

### Use WorkflowBuilderModule with Java

You can create an instance of the WorkflowBuilderModule with Java by using the `WorkflowBuilderModule.Builder` class, as the following example below:

```java
 WorkflowBuilderModule workflowBuilderModule = new WorkflowBuilderModule.Builder("61d880f1e8f1a", "64a6c15e409be")
                .dev(true) // only set to true if you're in development mode. The default value is false.
                .userInfo(
                        //specify user details to be automatically filled and omitted from the form
                        new UserInfo.Builder()
                                .firstName("Jagaban")
                                .lastName("Cynthia")
                                .gender(GenderType.FEMALE)
                                .mobile("08011223344")
                                .build()
                )
                .customization(
                        // customize the UI and the message shown to your user before the form shows up
                        new Customization.Builder()
                                .primaryColor("#ffffff")
                                .greetingMessage("We will need to verify your identity. It will only take a moment.")
                                .actionButtonText("Proceed")
                                .actionButtonTextColor("#46B2C8")
                                .build()
                )
                .onSuccess(new Function1<String, Unit>() {
                    @Override
                    public Unit invoke(String formData) {
                        //form was submitted successfully. perform some action here
                        return null;
                    }
                })
                .onFailed(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        //form submission failed due to some error. perform some action here
                        return null;
                    }
                })
                .onCompleted(new Function1<String, Unit>() {
                    @Override
                    public Unit invoke(String formData) {
                        //form was submitted successfully. This is called immediately after "0nSuccess()".
                        // perform some action here
                        return null;
                    }
                })
                .metaData(Collections.emptyMap()) // any other information you wish to pass to your Webhook Url
                .build();
```

#### Display the Form

```java
workflowBuilderModule.start(context);
```

#### Cancel the process

```java
workflowBuilderModule.close();
```

### Using the LivenessCheck Module with Kotlin

First off, create an instance of the LivenessCheckModule with the `LivenessCheckModule.Builder` class, passing in your merchant key.

```kotlin
val livenessCheckModule = LivenessCheckModule.Builder(publicMerchantKey = "61d880f1e8e18f1a")
            .onSuccess { livenessPhoto ->
                //The liveness check completes successfully. perform some action here.
                //"livenessPhoto" is the base64 encoded String of the snapshot taken during the liveness check.

            }
            .onFailed {
                //Liveness check failed. perform some action here
            }
            .build()
```

#### Specifying optional parameters:

```kotlin
val livenessCheckModule = LivenessCheckModule.Builder(publicMerchantKey = "61d880f1e8f1a" )
            .dev(true) // only set to true if you're in development mode. The default value is false.
            .userInfo(
                //specify user details to be stored alongside the liveness snapshot photo
                UserInfo.Builder()
                    .firstName("Jagaban")
                    .lastName("Cynthia")
                    .build()
            )
            .customization(
                // customize the UI and the message shown to your user before the liveness check process starts
                Customization.Builder()
                    .primaryColor("#ffffff")
                    .greetingMessage("We will need to carry out a liveness check. It will only take a moment.")
                    .actionButtonText("Start Liveness Test")
                    .actionButtonTextColor("#46B2C8")
                    .
                    .
                    .build()
            )
            .onSuccess { livenessPhoto ->
                //The liveness check completes successfully. perform some action here
            }
             .onFailed {
                // Liveness check failed. perform some action here
            }
            .onRetry {
                // Liveness check is retried by user after failing. perform some action here
            }
            .onCancel {
                // Liveness check cancelled by user. Perform some action here
            }
            .onClose {
                // Liveness check closed by user. Perform some action here
            }
            .metaData(emptyMap()) // Any other inormation you wish to pass to your Webhook url
            .build()
```

#### Start the process

Call `start()` on the `LivenessCheckModule` instance passing in a `Context` object to start the process:

```kotlin
 livenessCheckModule.start(context)
```

#### Cancel the process

The process is automatically canceled once the `onSuccess` callbacks returns, but you can also cancel manually by calling `close()`:

```kotlin
 livenessCheckModule.close() // cancel the process
```

### Use LivenessCheckModule with Java

You can create an instance of the LivenessCheckModule with Java by using the `LivenessCheckModule.Builder` class, as the following example below:

```java
 LivenessCheckModule livenessCheckModule = new LivenessCheckModule.Builder("61d880f1e8f1a")
                .dev(true) // only set to true if you're in development mode. The default value is false.
                .userInfo(
                        //specify user details to be stored alongside the liveness snapshot photo
                        new UserInfo.Builder()
                                .firstName("Jagaban")
                                .lastName("Cynthia")
                                .build()
                )
                .customization(
                        // customize the UI and the message shown to your user before the form shows up
                        new Customization.Builder()
                                .primaryColor("#ffffff")
                                .greetingMessage("We will need to carry out a liveness check. It will only take a moment.")
                                .actionButtonText("Start Liveness Test")
                                .actionButtonTextColor("#46B2C8")
                                .build()
                )
                .onSuccess(new Function1<String, Unit>() {
                    @Override
                    public Unit invoke(String livenessPhoto) {
                        //The liveness check completes successfully. perform some action here
                        return null;
                    }
                })
                .onFailed(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        // Liveness check failed. perform some action here
                        return null;
                    }
                })
                .onRetry(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        // Liveness check Retried after failing. perform some action here
                        return null;
                    }
                })
                .onCancel(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        // Liveness check cancelled. perform some action here
                        return null;
                    }
                })
                .onClose(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        // Liveness check closed. perform some action here
                        return null;
                    }
                })
                .metaData(Collections.emptyMap()) // any other information you wish to pass to your Webhook Url
                .build();
```

#### Start the process

```java
LivenessCheckModule.start(context);
```

#### Cancel the process

```java
LivenessCheckModule.close();
```

### Using the DocumentCapture Module with Kotlin

First off, create an instance of the DocumentCaptureModule with the `DocumentCaptureModule.Builder` class, passing in your merchant key.

```kotlin
val documentCaptureModule = DocumentCaptureModule.Builder(publicMerchantKey = "61d880f1e8e18f1a")
            .onSuccess { documentData ->
                //The document capture was successfull. perform some action here
                //"documentData" is the data object that holds the details extracted from the document

            }
            .onFailed {
                //Document capture failed. perform some action here
            }
            .build()
```

#### Specifying optional parameters:

```kotlin
val documentCaptureModule = DocumentCaptureModule.Builder(publicMerchantKey = "61d880f1e8f1a" )
            .dev(true) // only set to true if you're in development mode. The default value is false.
            .userInfo(
                //specify user's first name to be used in the personalized message shown to the user
                UserInfo.Builder()
                    .firstName("Jagaban")
                    .build()
            )
            .customization(
                // customize the UI and the message shown to your user before the document capture process starts
                Customization.Builder()
                    .primaryColor("#ffffff")
                    .greetingMessage("We will need to carry out a  document capture. It will only take a moment.")
                    .actionButtonText("Start Document Capture")
                    .actionButtonTextColor("#46B2C8")
                    .
                    .
                    .build()
            )
            .onSuccess { documentData ->
                //The document capture completes successfully. perform some action here
            }
             .onFailed {
                // Document capture failed. perform some action here
            }
            .onRetry {
                // Document capture is retried by user after failing. perform some action here
            }
            .onCancel {
                // Document capture cancelled by user. Perform some action here
            }
            .onClose {
                // Document capture closed by user. Perform some action here
            }
            .metaData(emptyMap()) // Any other inormation you wish to pass to your Webhook url
            .build()
```

#### Start the process

Call `start()` on the `DocumentCaptureModule` instance passing in a `Context` object to start the process:

```kotlin
 documentCaptureModule.start(context)
```

#### Cancel the process

The process is automatically canceled once the `onSuccess` callbacks returns, but you can also cancel manually by calling `close()`:

```kotlin
 documentCaptureModule.close() // cancel the process
```

### Use DocumentCaptureModule with Java

You can create an instance of the DocumentCaptureModule with Java by using the `DocumentCaptureModule.Builder` class, as the following example below:

```java
 DocumentCaptureModule documentCaptureModule = new DocumentCaptureModule.Builder("61d880f1e8f1a")
                .dev(true) // only set to true if you're in development mode. The default value is false.
                .userInfo(
                        //specify user's first name to be used in the personalized message shown to the user
                        new UserInfo.Builder()
                                .firstName("Jagaban")

                )
                .customization(
                        // customize the UI and the message shown to your user before the document capture process starts
                        new Customization.Builder()
                                .primaryColor("#ffffff")
                                .greetingMessage("We will need to carry out a  document capture. It will only take a moment.")
                                .actionButtonText("Start Document Capture")
                                .actionButtonTextColor("#46B2C8")
                                .build()
                )
                .onSuccess(new Function1<String, Unit>() {
                    @Override
                    public Unit invoke(String livenessPhoto) {
                        // Document capture completes successfully. perform some action here
                        return null;
                    }
                })
                .onFailed(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        // Document capture failed. perform some action here
                        return null;
                    }
                })
                .onRetry(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        // Document capture Retried by user. perform some action here
                        return null;
                    }
                })
                .onCancel(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        // Document capture cancelled by user. perform some action here
                        return null;
                    }
                })
                .onClose(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        // Document capture closed. perform some action here
                        return null;
                    }
                })
                .metaData(Collections.emptyMap()) // any other information you wish to pass to your Webhook Url
                .build();
```

#### Start the process

```java
documentCaptureModule.start(context);
```

#### Cancel the process

```java
documentCaptureModule.close();
```

## Report Issues

Use the issue tracker to report any bug or request a feature. We promise to attend to issues as soon as they're created.

## Credits

This SDK is developed and maintained solely by [Youverify](https://youverify.co)

## License

[MIT](https://choosealicense.com/licenses/mit/)

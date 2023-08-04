package co.youverify.vformapp;

import java.util.Collections;

import co.youverify.yvos_sdk.Customization;
import co.youverify.yvos_sdk.GenderType;
import co.youverify.yvos_sdk.UserInfo;
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessCheckModule;
import co.youverify.yvos_sdk.modules.workflowBuilder.WorkflowBuilderModule;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class SdkInJava {

    void testVform() {

        WorkflowBuilderModule workflowBuilderModule = new WorkflowBuilderModule.Builder("61d880f1e8f1a", "64a6c15e409be")
                .dev(true) // only set to true if you're in development mode. The default value is false.
                .userInfo(
                        //specify user details to be used to prepopulate the form
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
    }

    void testLiveness(){
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
    }

}

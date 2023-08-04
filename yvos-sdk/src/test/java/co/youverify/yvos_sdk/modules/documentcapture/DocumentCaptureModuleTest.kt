package co.youverify.yvos_sdk.modules.documentcapture

import co.youverify.yvos_sdk.Customization
import co.youverify.yvos_sdk.UserInfo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DocumentCaptureModuleTest{
    private lateinit var moduleBuilder: DocumentCaptureModule.Builder

    @Before
    fun initializeBuilderWithConstructorOnly(){

        moduleBuilder = DocumentCaptureModule.Builder("16A521")
    }
    @Test
    fun builderWithConstructorOnly_shouldCreateInstanceWithCorrectMerchantKey(){
        val workflowBuilderModule = moduleBuilder.build()
        assertEquals("16A521",workflowBuilderModule.publicMerchantKey)
    }



    @Test
    fun builderWithoutDev_shouldCreateInstanceWithFalseDev(){
        val workflowBuilderModule = moduleBuilder.build()
        assertFalse(workflowBuilderModule.dev)
    }

    @Test
    fun builderWithTrueDev_shouldCreateInstanceWithTrueDev(){
        val workflowBuilderModule = moduleBuilder.dev(true).build()
        assertTrue(workflowBuilderModule.dev)
    }

    @Test
    fun builderWithFalseDev_shouldCreateInstanceWithFalseDev(){
        val workflowBuilderModule = moduleBuilder.dev(false).build()
        assertFalse(workflowBuilderModule.dev)
    }

    @Test
    fun builderWithoutCustomization_shouldCreateInstanceWithDefaultCustomization(){
        val defaultCustomization = Customization.Builder().build()
        val workflowBuilderModule = moduleBuilder.build()
        assertEquals(defaultCustomization,workflowBuilderModule.customization)
    }

    @Test
    fun builderWithCustomization_shouldCreateInstanceWithCorrectCustomization(){
        val customization = Customization.Builder()
            .primaryColor("#ffffff")
            .actionButtonText("Let's go")
            .build()
        val workflowBuilderModule = moduleBuilder.customization(
            Customization.Builder()
                .primaryColor("#ffffff")
                .actionButtonText("Let's go")
                .build()
        )
            .build()
        assertEquals(customization,workflowBuilderModule.customization)
    }

    @Test
    fun builderWithoutUserInfo_shouldCreateInstanceWithDefaultUserInfo(){
        val defaultUserInfo = null
        val workflowBuilderModule = moduleBuilder.build()
        assertNull(defaultUserInfo,workflowBuilderModule.userInfo)
    }

    @Test
    fun builderWithUserInfo_shouldCreateInstanceWithCorrectUserInfo(){
        val userInfo = UserInfo.Builder()
            .firstName("Joseph")
            .lastName("Mbutu")
            .mobile("080000000")
            .build()

        val workflowBuilderModule = moduleBuilder.userInfo(
            UserInfo.Builder()
                .firstName("Joseph")
                .lastName("Mbutu")
                .mobile("080000000")
                .build()
        ).build()
        assertEquals(userInfo,workflowBuilderModule.userInfo)
    }

    @Test
    fun builderWithOnSuccess_shouldCreateInstanceWithCorrectOnSuccess(){

        val onSuccessCallback:(DocumentData) -> Unit = {
            println(it.documentNumber)
        }
        val workflowBuilderModule = moduleBuilder.onSuccess(onSuccessCallback)
        assertEquals(onSuccessCallback,workflowBuilderModule.onSuccess)
    }
    @Test
    fun builderWithOnFailed_shouldCreateInstanceWithCorrectOnFailed(){

        val onFailedCallback:() -> Unit = {
            println("")
        }

        val workflowBuilderModule = moduleBuilder.onFailed(onFailedCallback)
        assertEquals(onFailedCallback,workflowBuilderModule.onFailed)
    }



    @Test
    fun builderWithoutMetaData_shouldCreateInstanceWithEmptyMetaData(){

        val workflowBuilderModule = moduleBuilder.build()
        assertTrue(workflowBuilderModule.metaData.isEmpty())
    }

    @Test
    fun builderWithMetaData_shouldCreateInstanceWithCorrectMetaData(){

        val metadata = mapOf("Tola" to "Ridwan", "Kemi" to 0)
        val workflowBuilderModule = moduleBuilder.metaData(metadata)
        assertTrue(workflowBuilderModule.metaData.containsValue("Ridwan"))
    }
}
package co.youverify.yvos_sdk.modules.workflowBuilder

import co.youverify.yvos_sdk.Customization
import co.youverify.yvos_sdk.UserInfo
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows


class WorkflowBuilderModuleTest {

    private lateinit var moduleBuilder: WorkflowBuilderModule.Builder

    @Before
    fun initializeBuilderWithConstructorOnly() {

        moduleBuilder = WorkflowBuilderModule.Builder("16A521", "3214")
    }

    @Test
    fun builderWithConstructorOnly_shouldCreateInstanceWithCorrectMerchantKey() {
        val workflowBuilderModule = moduleBuilder.build()
        Assert.assertEquals("16A521", workflowBuilderModule.publicMerchantKey)
    }

    @Test
    fun builderWithConstructorOnly_shouldCreateInstanceWithCorrectFormId() {
        val workflowBuilderModule = moduleBuilder.build()
        Assert.assertEquals("3214", workflowBuilderModule.formId)
    }

    @Test
    fun builderWithoutDev_shouldCreateInstanceWithFalseDev() {
        val workflowBuilderModule = moduleBuilder.build()
        Assert.assertFalse(workflowBuilderModule.dev)
    }

    @Test
    fun builderWithTrueDev_shouldCreateInstanceWithTrueDev() {
        val workflowBuilderModule = moduleBuilder.dev(true).build()
        Assert.assertTrue(workflowBuilderModule.dev)
    }

    @Test
    fun builderWithFalseDev_shouldCreateInstanceWithFalseDev() {
        val workflowBuilderModule = moduleBuilder.dev(false).build()
        Assert.assertFalse(workflowBuilderModule.dev)
    }

    @Test
    fun builderWithoutCustomization_shouldCreateInstanceWithDefaultCustomization() {
        val defaultCustomization = Customization.Builder().build()
        val workflowBuilderModule = moduleBuilder.build()
        Assert.assertEquals(defaultCustomization, workflowBuilderModule.customization)
    }

    @Test
    fun builderWithCustomization_shouldCreateInstanceWithCorrectCustomization() {
        val customization = Customization.Builder()
            .primaryColor("#000000")
            .actionButtonText("Let's Start")
            .build()
        val workflowBuilderModule = moduleBuilder.customization(
            Customization.Builder()
                .primaryColor("#000000")
                .actionButtonText("Let's Start")
                .build()
        )
            .build()
        Assert.assertEquals(customization, workflowBuilderModule.customization)
    }

    @Test
    fun builderWithoutUserInfo_shouldCreateInstanceWithDefaultUserInfo() {
        val defaultUserInfo = null
        val workflowBuilderModule = moduleBuilder.build()
        Assert.assertNull(defaultUserInfo, workflowBuilderModule.userInfo)
    }

    @Test
    fun builderWithUserInfo_shouldCreateInstanceWithCorrectUserInfo() {
        val userInfo = UserInfo.Builder()
            .firstName("Kaka")
            .lastName("Igwe")
            .mobile("08088987385")
            .build()

        val workflowBuilderModule = moduleBuilder.userInfo(
            UserInfo.Builder()
                .firstName("Kaka")
                .lastName("Igwe")
                .mobile("08088987385")
                .build()
        ).build()
        Assert.assertEquals(userInfo, workflowBuilderModule.userInfo)
    }

    @Test
    fun builderWithOnSuccess_shouldCreateInstanceWithCorrectOnSuccess() {

        val onSuccessCallback: (String) -> Unit = {
            println(it)
        }
        val workflowBuilderModule = moduleBuilder.onSuccess(onSuccessCallback)
        Assert.assertEquals(onSuccessCallback, workflowBuilderModule.onSuccess)
    }

    @Test
    fun builderWithOnFailed_shouldCreateInstanceWithCorrectOnFailed() {

        val onFailedCallback: () -> Unit = {
            println("")
        }

        val workflowBuilderModule = moduleBuilder.onFailed(onFailedCallback)
        Assert.assertEquals(onFailedCallback, workflowBuilderModule.onFailed)
    }

    @Test
    fun builderWithOnCompleted_shouldCreateInstanceWithCorrectOnCompleted() {

        val onCompletedCallback: (String) -> Unit = {
            println(it)
        }

        val workflowBuilderModule = moduleBuilder.onCompleted(onCompletedCallback)
        Assert.assertEquals(onCompletedCallback, workflowBuilderModule.onCompleted)
    }

    @Test
    fun builderWithoutMetaData_shouldCreateInstanceWithEmptyMetaData() {

        val workflowBuilderModule = moduleBuilder.build()
        Assert.assertTrue(workflowBuilderModule.metaData.isEmpty())
    }

    @Test
    fun builderWithMetaData_shouldCreateInstanceWithCorrectMetaData() {

        val metadata = mapOf("Tola" to 1, "Kemi" to 2)
        val workflowBuilderModule = moduleBuilder.metaData(metadata)
        Assert.assertTrue(workflowBuilderModule.metaData.containsKey("Kemi"))
    }

    @Test
    fun whenFormId_isEmpty_shouldThrowException() {
        val workflowBuilderModule = WorkflowBuilderModule.Builder(
            publicMerchantKey = "t568210A756ETY78354671PA",
            formId = ""
        ).build()
        assertThrows<IllegalArgumentException> {
            workflowBuilderModule.validateFormId()
        }
    }

    @Test
    fun whenFormId_isLessThan24Characters_shouldThrowException() {
        val workflowBuilderModule = WorkflowBuilderModule
            .Builder(publicMerchantKey = "t568210A756ETY78354671PA", formId = "123E")
            .build()
        assertThrows<IllegalArgumentException> {
            workflowBuilderModule.validateFormId()
        }
    }

    @Test
    fun whenFormId_isGreaterThan24Characters_shouldThrowException() {
        val workflowBuilderModule = WorkflowBuilderModule
            .Builder(
                publicMerchantKey = "t568210A756ETY78354671PA",
                formId = "123EA56782ERath67E43ETKMS"
            )
            .build()
        assertThrows<IllegalArgumentException> {
            workflowBuilderModule.validateFormId()
        }
    }


        @Test
        fun whenFormId_is24Characters_shouldNotThrowException() {
            val workflowBuilderModule = WorkflowBuilderModule
                .Builder(
                    publicMerchantKey = "t568210A756ETY78354671PA",
                    formId = "123EA56782ERath67E43ETKM"
                )
                .build()
            assertDoesNotThrow {
                workflowBuilderModule.validateFormId()
            }
        }
}

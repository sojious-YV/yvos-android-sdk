package co.youverify.yvos_sdk.util

import co.youverify.yvos_sdk.Customization
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

//Roboelectric has to come in here cos this unit test depend on an Android Framework class - "android.graphics.Color"
@RunWith(RobolectricTestRunner::class)
class UtilTest{

    @Test
    fun whenMerchantKey_isEmpty_shouldThrowException(){
       assertThrows<IllegalArgumentException> {
           validatePublicMerchantKeyAndAppearance(
               publicMerchantKey = "",
               appearance = Customization.Builder().build()
           )
       }
    }

    @Test
    fun whenMerchantKey_islessThan24Characters_shouldThrowException(){
        assertThrows<IllegalArgumentException> {
            validatePublicMerchantKeyAndAppearance(
                publicMerchantKey = "t568210A7",
                appearance = Customization.Builder().build()
            )
        }
    }

    @Test
    fun whenMerchantKey_isGreaterThan24Characters_shouldThrowException(){
       assertThrows<IllegalArgumentException> {
           validatePublicMerchantKeyAndAppearance(
               publicMerchantKey = "t568210A756ETY78354671PA5",
               appearance = Customization.Builder().build()
           )
       }
    }

    @Test
    fun whenMerchantKey_is24Characters_shouldNotThrowException(){

        assertDoesNotThrow {
            validatePublicMerchantKeyAndAppearance(
                publicMerchantKey = "t568210A756ETY7835467127",
                appearance = Customization.Builder().build()
            )
        }
    }

    @Test
    fun whenPrimaryColorString_isInvalid_shouldThrowException(){
        assertThrows<Exception> {
            validatePublicMerchantKeyAndAppearance(
                publicMerchantKey = "t568210A756ETY78354671PA",
                appearance = Customization.Builder().primaryColor("111111").build()
            )
        }
    }

    @Test
    fun whenButtonBackgroundColorString_isInvalid_shouldThrowException(){
        assertThrows<Exception> {
            validatePublicMerchantKeyAndAppearance(
                publicMerchantKey = "t568210A756ETY78354671PA",
                appearance = Customization.Builder().actionButtonBackgroundColor("#11111").build()
            )
        }
    }

    @Test
    fun whenButtonTextColorString_isInvalid_shouldThrowException(){
        assertThrows<Exception> {
            validatePublicMerchantKeyAndAppearance(
                publicMerchantKey = "t568210A756ETY78354671PA",
                appearance = Customization.Builder().actionButtonTextColor(" ").build()
            )
        }
    }

}
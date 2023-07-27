package co.youverify.yvos_sdk


/**
 * This class specifies the customization options of the SDK. The SDK uses the options supplied to customize the look of it's UI.
 * @property greeting the greeting message that is shown to the user before starting any of the SDK services.
 * Note that the greeting message only appears if you passed the user's first name while creating the user's personal information.
 * @property actionText the text of the action button beneath the greeting message.
 * @property buttonBackgroundColor the hex color code String that defines the background color of the action button. Passing an invalid color string will result in an [InvalidArgumentException]
 * @property buttonTextColor the hex color code String that defines the color of the action button text. Passing an invalid color string will result in an [InvalidArgumentException]
 * @property primaryColor the hex color code String that defines the primary color of the UI.This color is used to paint the progress indicator and some other UI elements. Passing an invalid color string will result in an [InvalidArgumentException]
 */

 class Customization private constructor(builder:Builder){

     var greeting:String
     private set
     var actionText:String
     private set
     var buttonBackgroundColor:String
     private set
     var buttonTextColor:String
     private set
     var primaryColor :String
     private set

    init {
        this.greeting = builder.greeting
        this.actionText = builder.actionText
        this.buttonBackgroundColor = builder.buttonBackgroundColor
        this.buttonTextColor = builder.buttonTextColor
        this.primaryColor = builder.primaryColor
    }
     class  Builder{
         var greeting = "We will need to verify your identity. It will only take a moment."
         private set
         var actionText = "Verify Identity"
             private set
         var buttonBackgroundColor = "#46B2C8"
             private set
         var buttonTextColor = "#ffffff"
             private set
         var primaryColor = "#46B2C8"
             private set

         fun greetingMessage(greetingMessage:String): Builder {
             greeting = greetingMessage
             return this
         }
         fun actionButtonText(buttonText:String): Builder {
             this.actionText = buttonText
             return this
         }

         fun primaryColor(colorString:String): Builder {
             primaryColor = colorString
             return this
         }

         fun actionButtonBackgroundColor(colorString:String): Builder {
             buttonBackgroundColor = colorString
             return this
         }

         fun actionButtonTextColor(colorString:String): Builder {
             buttonTextColor = colorString
             return this
         }
         fun build(): Customization {
             return Customization(this)
         }
     }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (other !is Customization) return false
        return this.primaryColor==other.primaryColor && this.actionText==other.actionText
                && this.greeting==other.greeting && this.buttonTextColor==other.buttonTextColor
                && this.buttonBackgroundColor==other.buttonBackgroundColor


    }

    override fun hashCode(): Int = 31 * primaryColor.hashCode() + actionText.hashCode() +
            greeting.hashCode() + buttonTextColor.hashCode() + buttonBackgroundColor.hashCode()

}
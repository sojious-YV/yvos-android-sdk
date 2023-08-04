package co.youverify.yvos_sdk


open class SdkModule(
    publicMerchantKey:String,
    dev:Boolean,
    customization: Customization,
    userInfo: UserInfo?,
    metaData: Map<String,Any>,
){
    var publicMerchantKey: String
        private set
    var dev:Boolean = false
        private set
    var customization: Customization
        private set
    var userInfo: UserInfo?
        private set
    var metaData: Map<String,Any>
        private set

    init {
        this.publicMerchantKey = publicMerchantKey
        this.customization = customization
        this.dev = dev
        this.userInfo = userInfo
        this.metaData = metaData
    }
}
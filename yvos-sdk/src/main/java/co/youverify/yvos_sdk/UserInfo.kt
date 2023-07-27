package co.youverify.yvos_sdk


class UserInfo private constructor(builder:Builder){


    var firstName: String?
        private set
    var lastName: String?
        private set
    var middleName: String?
        private set
    var email: String?
        private set
    var mobile: String?
        private set
    var gender: GenderType
        private set

    init {
        this.firstName = builder.firstName
        this.lastName = builder.lastName
        this.middleName = builder.middleName
        this.email = builder.email
        this.mobile = builder.mobile
        this.gender = builder.gender
    }



    class Builder{
        var firstName: String?=null
            private set
        var lastName: String?=null
            private set
        var middleName: String?=null
            private set
        var email: String?=null
            private set
        var mobile: String?=null
            private set
        var gender: GenderType = GenderType.NOT_IDENTIFIED
            private set

        fun firstName(firstName:String): Builder {
            this.firstName = firstName
            return this
        }

        fun lastName(lastName:String): Builder {
            this.lastName = lastName
            return this
        }
        fun middleName(middleName:String): Builder {
            this.middleName = middleName
            return this
        }
        fun email(email:String): Builder {
            this.email = email
            return this
        }
        fun mobile(value:String): Builder {
            this.mobile = mobile
            return this
        }
        fun gender(genderType:GenderType): Builder {
            this.gender = genderType
            return this
        }
        fun build(): UserInfo {
            return UserInfo(this)
        }
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (other !is UserInfo) return false
        return this.firstName==other.firstName && this.lastName==other.lastName
                && this.middleName==other.middleName && this.mobile==other.mobile
                && this.email==other.email && this.gender==other.gender


    }

    override fun hashCode(): Int = 31 * firstName.hashCode() + lastName.hashCode() +
            middleName.hashCode() + mobile.hashCode() + email.hashCode() + gender.hashCode()
}

enum class GenderType{
    MALE,
    FEMALE,
    NOT_IDENTIFIED
}
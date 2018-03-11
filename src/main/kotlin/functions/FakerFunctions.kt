package functions

import com.github.javafaker.Faker
import java.util.*

class FakerFunctions : AbstractFunctions() {

    private val faker = Faker(Locale.FRANCE)
    @Generator("zip_code")
    fun zipCode(): String {
        return faker.address().zipCode()
    }

    @Generator("city")
    fun city(): String {
        return faker.address().city()
    }

    @Generator("country")
    fun country(): String {
        return faker.address().country()
    }

    @Generator("street")
    fun street(): String {
        return faker.address().streetAddress()
    }
    @Generator("building")
    fun building(): String {
        return faker.address().buildingNumber()
    }

    @Generator("email")
    fun email(): String {
        return faker.internet().emailAddress()
    }

    @Generator("phone")
    fun phone(): String {
        return faker.phoneNumber().phoneNumber()
    }

    @Generator("first_name")
    fun firstName(): String {
        return faker.name().firstName()
    }

    @Generator("last_name")
    fun lastName(): String {
        return faker.name().lastName()
    }

    @Generator("company")
    fun company() : String {
        return faker.company().name()
    }

    @Generator("iban")
    fun iban() : String {
        return faker.finance().iban()
    }

    @Generator("bic")
    fun bic(): String {
        return faker.finance().bic()
    }

    @Generator("credit_card")
    fun index(): String {
        return faker.finance().creditCard()
    }

}
package com.sport.event

//object for account constants
object Constants {
    //com.sport.event.Constants for account manager
    const val ACCOUNT_TYPE = "accountType"
    const val ACCOUNT_NAME = "SportEvent"
    const val AUTH_TOKEN_TYPE = "authTokenType"
    const val AUTH_TOKEN = "authToken"
    const val USER_DATA = "userdata"
    const val REFRESH_TOKEN = "refreshToken"
    const val VERIFY_TOKEN = "verifyToken"
    const val USER_ID = "userId"
    const val USERNAME = "username"

    //com.sport.event.Constants for registration
    const val NAME = "name"
    const val SURNAME = "surname"
    const val BIRTHDAY = "birthday"
    const val EMAIL = "email"
    const val COUNTRY = "country"
    const val LOCALITY = "locality"

    //Constants for create event fragment
    val month = arrayOf("Январь", "Февраль", "Март","Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")
    val monthRP = arrayOf("января", "февраля", "марта","апреля", "мая", "июня", "июля", "августя", "сентября", "октября", "ноября", "декабря")
    val daysOfWeek = arrayOf("ВС", "ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ")
    val fullDaysOfWeek = arrayOf("Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота")
}

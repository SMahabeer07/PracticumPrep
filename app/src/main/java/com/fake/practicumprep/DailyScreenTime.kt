package com.fake.practicumprep

data class DailyScreenTime(
    val date: String,
    val morningMinutes: Int,
    val afternoonMinutes: Int,
    val activityNote: String
){
    // Helper method to get total daily usage
    fun getTotalMinutes(): Int = morningMinutes + afternoonMinutes
}

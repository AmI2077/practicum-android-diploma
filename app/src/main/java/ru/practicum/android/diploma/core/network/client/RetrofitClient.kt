package ru.practicum.android.diploma.core.network.client

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.core.dto.request.VacancyRequestDto
import ru.practicum.android.diploma.core.dto.response.VacancyResponseDto
import ru.practicum.android.diploma.core.network.HttpCodes
import ru.practicum.android.diploma.core.network.NetworkResult
import java.io.IOException

object RetrofitClient : NetworkClient {

    private const val BASE_URL = "https://android-diploma.education-services.ru"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }
    private val authInterceptor = AuthorizationInterceptor(token = BuildConfig.API_ACCESS_TOKEN)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    private val apiService = retrofit.create(VacanciesApiService::class.java)

    override suspend fun fetchVacancies(vacancyRequestDto: VacancyRequestDto): NetworkResult<VacancyResponseDto?> {
        return try {
            val response = apiService.fetchVacancies(
                area = vacancyRequestDto.area,
                industry = vacancyRequestDto.industry,
                text = vacancyRequestDto.text,
                salary = vacancyRequestDto.salary,
                page = vacancyRequestDto.page,
                onlyWithSalary = vacancyRequestDto.onlyWithSalary
            )
            if (response.isSuccessful) {
                NetworkResult.Success(response.body())
            } else {
                NetworkResult.Error(HttpCodes.fromInt(response.code()))
            }
        } catch (e: IOException) {
            Log.d("RETROFIT_CLIENT", "FETCH", e)
            NetworkResult.Error(HttpCodes.NO_INTERNET_CONNECTION_ERROR_CODE)
        }
    }
}

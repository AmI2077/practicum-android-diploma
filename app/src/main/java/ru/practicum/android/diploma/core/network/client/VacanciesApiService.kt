package ru.practicum.android.diploma.core.network.client

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.core.dto.response.VacancyResponseDto

interface VacanciesApiService {

    @GET("/vacancies")
    suspend fun fetchVacancies(
        @Query("area") area: Int? = null,
        @Query("industry") industry: Int? = null,
        @Query("text") text: String,
        @Query("salary") salary: Int? = null,
        @Query("page") page: Int? = null,
        @Query("only_with_salary ") onlyWithSalary: Boolean? = null,
    ): Response<VacancyResponseDto>
}

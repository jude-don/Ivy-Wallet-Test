//import com.ivy.exchange.fawazahmed0.Fawazahmed0ExchangeProvider
//
//package com.ivy.exchange.fawazahmed0
////
////import io.mockk.coEvery
////import io.mockk.mockk
////import com.ivy.network.ktorClient
////import io.ktor.client.call.*
////import io.ktor.client.request.*
////import kotlinx.coroutines.runBlocking
////import org.junit.jupiter.api.Assertions.*
////import org.junit.jupiter.api.BeforeEach
////import org.junit.jupiter.api.Test
////
////class Fawazahmed0ExchangeProviderTest {
////
////    // Mocking the Ktor client
////    private val client = mockk<HttpClient>()
////
////    // Instance of the class under test
//private lateinit var provider: Fawazahmed0ExchangeProvider
////
////    // Setup function to initialize before each test
////    @BeforeEach
////    fun setUp() {
////        // Initializing provider with mocked Ktor client
////        provider = Fawazahmed0ExchangeProvider()
////
////        // Replacing the actual ktorClient with the mocked client
////        setKtorClientMock(client)
////    }
////
////    // Test to check if the fetchExchangeRates function returns failure on empty base currency
////    @Test
////    fun `fetchExchangeRates returns failure on blank base currency`() = runBlocking {
////        val result = provider.fetchExchangeRates("")
////        assertEquals(emptyMap<String, Double>(), result.ratesMap)
////    }
////
////    // Test to check if the fetchExchangeRates function returns correct rates
////    @Test
////    fun `fetchExchangeRates returns correct rates`() = runBlocking {
////        // Mocking the response body for the Ktor client
////        val responseBody = mockk<HttpResponse>()
////        coEvery { responseBody.body<Fawazahmed0Response>() } returns Fawazahmed0Response(
////            eur = mapOf("usd" to 1.2, "bgn" to 1.955902)
////        )
////
////        // Mocking the Ktor client to return the mocked response
////        coEvery { client.get(any<String>()) } returns responseBody
////
////        // Performing the action
////        val result = provider.fetchExchangeRates("EUR")
////
////        // Asserting the expected outcome
////        val expectedRatesMap = mapOf("USD" to 1.2, "BGN" to 1.955902)
////        assertEquals(expectedRatesMap, result.ratesMap)
////    }
////
////    // Function to replace the actual ktorClient with a mock
////    private fun setKtorClientMock(mockedClient: HttpClient) {
////        // This assumes you have a way to inject the mock client into your provider,
////        // such as a setter, a constructor parameter, or an exposed property.
////        // Update this function to match your actual injection method.
////        provider.ktorClient = mockedClient
////    }
////}
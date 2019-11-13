package de.thro.inf.prg3.a07.tests;

import de.thro.inf.prg3.a07.api.OpenMensaAPI;
import de.thro.inf.prg3.a07.model.Meal;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenMensaAPITests
{
	private static final Logger logger = LogManager.getLogger(OpenMensaAPITests.class);
	private OpenMensaAPI openMensaAPI;

	@BeforeAll
	void setup()
	{
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient client = new OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.build();

		Retrofit retrofit = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("https://openmensa.org/api/v2/")
			.client(client)
			.build();

		openMensaAPI = retrofit.create(OpenMensaAPI.class);
	}

	@Test
	void testGetMeals() throws IOException
	{
		Call<Meal[]> call = openMensaAPI.getMeals(229, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

		List<Meal> meals = Arrays.asList(call.execute().body());

		assertNotNull(meals);
		System.out.println("size: " + meals.size());
		assertNotEquals(0, meals.size());

		for (Meal m : meals)
			logger.info(m.toString());
	}
}

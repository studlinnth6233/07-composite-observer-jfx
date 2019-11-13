package de.thro.inf.prg3.a07.api;

import de.thro.inf.prg3.a07.model.Meal;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Peter Kurfer on 11/19/17.
 */

public interface OpenMensaAPI
{
	@GET("canteens/{canteen}/days/{date}/meals")
	Call<Meal[]> getMeals(@Path("canteen") int canteen, @Path("date") String date);
}

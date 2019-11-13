package de.thro.inf.prg3.a07.controllers;

import de.thro.inf.prg3.a07.api.OpenMensaAPI;
import de.thro.inf.prg3.a07.model.Meal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainController implements Initializable
{
	@FXML private Button           btnRefresh;
	@FXML private Button 	       btnClose;
	@FXML private CheckBox         chkVegetarian;
	@FXML private ListView<String> mealsList;

	private ObservableList<Meal> meals = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("https://openmensa.org/api/v2/")
			.addConverterFactory(GsonConverterFactory.create())
			.build();

		OpenMensaAPI mensaAPI = retrofit.create(OpenMensaAPI.class);

		btnRefresh.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				meals.clear();

				Call<Meal[]> call = mensaAPI.getMeals(229, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

				call.enqueue(new Callback<Meal[]>()
				{
					@Override
					public void onResponse(Call<Meal[]> call, Response<Meal[]> response)
					{
						if (!response.isSuccessful())
							System.out.println(response.body());

						else
						{
							Collections.addAll(meals, response.body());

							displayData(chkVegetarian.isSelected());
						}
					}

					@Override
					public void onFailure(Call<Meal[]> call, Throwable t)
					{
						System.out.println(t.getMessage());
					}
				});
			}
		});

		chkVegetarian.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				displayData(chkVegetarian.isSelected());
			}
		});

		btnClose.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				System.exit(0);
			}
		});
	}

	private void displayData(boolean vegetarianFilter)
	{
		if (vegetarianFilter)
			mealsList.setItems(meals.stream()
				.filter(Meal::isVegetarian)
				.map(Meal::getName)
				.collect(Collectors.toCollection(FXCollections::observableArrayList)));

		else
			mealsList.setItems(meals.stream()
				.map(Meal::getName)
				.collect(Collectors.toCollection(FXCollections::observableArrayList)));
	}
}

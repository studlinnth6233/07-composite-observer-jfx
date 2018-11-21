package de.thro.inf.prg3.a07.controllers;

import de.thro.inf.prg3.a07.api.OpenMensaAPI;
import de.thro.inf.prg3.a07.model.Meal;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainController implements Initializable {
	public MainController() {
		// initialize Retrofit
		Retrofit retrofit = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("https://openmensa.org/api/v2/")
			.build();

		api = retrofit.create(OpenMensaAPI.class);
	}

	// use annotation to tie to component in XML
	@FXML
	private Button btnRefresh;

	@FXML
	private ListView<Meal> mealsList;

	@FXML
	private CheckBox chkVegetarian;

	private OpenMensaAPI api;

	// list to carry the data
	private ObservableList<Meal> meals = FXCollections.observableList(new LinkedList<>());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// no meals (empty list) yet
		mealsList.setItems(meals);

		// set the event handler (callback)
		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// vegie?
				boolean v = chkVegetarian.isSelected();

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
				final String today = sdf.format(new Date());

				api.getMeals(229, today).enqueue(new Callback<List<Meal>>() {
					@Override
					public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
						if (!response.isSuccessful()) return;
						if (response.body() == null) return;
						Platform.runLater(() -> {

							mealsList.getItems().clear();
							mealsList.getItems().addAll(response.body());
						});
					}

					@Override
					public void onFailure(Call<List<Meal>> call, Throwable t) {
						System.out.println("Meh");
					}
				});


			}
		});
	}
}

package com.bwap.weatherapp.WeatherApp.views;

import com.bwap.weatherapp.WeatherApp.controller.WeatherServices;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@SpringUI(path = "")

public class MainView extends UI {

    @Autowired
    private WeatherServices weatherService;

    private VerticalLayout mainLayout;
    private NativeSelect<String> uniSelect;
    private  TextField cityTextField;
    private Button SearchButton;
    private HorizontalLayout dashboard;
    private Label location;
    private Label currentTemp;

    private HorizontalLayout mainDescriptionLayout;

    private Label weatherDescription;

    private Label MaxWeather;

    private Label MinWeather;

    private Label Humidity;
    private Label Pressure;

    private Label Wind;

    private Label FeelsLike;

    private Image  IconImage;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainLayout();
        setHeader();
        setlogo();
        setForm();
        dashBoard();
        DashBoardDetails();
        SearchButton.addClickListener(clickEvent ->{
          if(!cityTextField.getValue().equals(""))
          {
              try
              {
                upadateUI();
              }
                catch (JSONException J)
                {
                    J.printStackTrace();
                }

          }
          else
              Notification.show("Enter the City Name");
        }
        );
    }


    private void mainLayout() {
        IconImage=new Image();
        mainLayout=new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(mainLayout);

    }

    private  void setHeader()
    {
        HorizontalLayout header=new HorizontalLayout();
        header.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label title=new Label("Weather App By Shriram");
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        title.addStyleName(ValoTheme.LABEL_COLORED);
        header.addComponent(title);

        mainLayout.addComponent(header);
    }

    private void setlogo()
    {
        HorizontalLayout logo=new HorizontalLayout();
        logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Image img =new Image(null,new ClassResource("/static/logo.png"));
        logo.setWidth("500px");
        logo.setHeight("500px");

        logo.addComponent(img);
        mainLayout.addComponent(logo);
    }

    private void setForm()
    {
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        //formLayout.setSpacing(true);
        formLayout.setMargin(true);

        //Selction Comment
        uniSelect = new NativeSelect<>();
        ArrayList<String> items =new ArrayList<>();
        items.add("C");
        items.add("F");

        uniSelect.setItems(items);
        uniSelect.setValue(items.get(0));
        formLayout.addComponent(uniSelect);

        //City Filed
        cityTextField =new TextField();
        cityTextField.setWidth("80%");
        formLayout.addComponent(cityTextField);

        //
        SearchButton =new Button();
        SearchButton.setIcon(VaadinIcons.SEARCH);
        formLayout.addComponent(SearchButton);

        mainLayout.addComponents(formLayout);
    }

    private void dashBoard()
    {
        dashboard=new HorizontalLayout();
        dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //city location
        location =new Label("Currently in Coimbatore");
        location.addStyleName(ValoTheme.LABEL_H2);
        location.addStyleName(ValoTheme.LABEL_LIGHT);

        // current Temp

        currentTemp=new Label("18C");
        currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.setStyleName(ValoTheme.LABEL_H1);

        dashboard.addComponents(location,IconImage,currentTemp);

    }

    private void DashBoardDetails()
    {
        mainDescriptionLayout=new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //Description

        VerticalLayout description=new VerticalLayout();
          description.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        //Weather description

        weatherDescription =new Label("Description: Clear Skies");
        weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
        description.addComponents(weatherDescription);

        //Min Weather

        MinWeather =new Label("Min:18");
        description.addComponent(MinWeather);


        MaxWeather =new Label("Max:32");
        description.addComponent(MaxWeather);

        VerticalLayout pressureLayout=new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);


        Pressure=new Label("Pressure:200pa");
        pressureLayout.addComponents(Pressure);


        Humidity=new Label("Humidity:23");
        pressureLayout.addComponents(Humidity);


        Wind=new Label("Wind:231");
        pressureLayout.addComponents(Wind);

        FeelsLike=new Label("FeelsLike:Cloudy");
        pressureLayout.addComponents(FeelsLike);

        mainDescriptionLayout.addComponents(description,pressureLayout);
    }


    private void upadateUI() throws JSONException {
        String city = cityTextField.getValue();
        String defaultUnit;
        weatherService.setCityName(city);

        //Checking Units and Assigning value to API url using setUnit()
        if(uniSelect.getValue().equals("F")){
            weatherService.setUnit( "imperial");
            uniSelect.setValue("F");
            defaultUnit = "\u00b0"+"F";
        }else {
            weatherService.setUnit("metric");
            defaultUnit = "\u00b0" + "C";
            uniSelect.setValue("C");
        }


        //Updaing City Temp and Unit
        location.setValue("Currently in "+city);
        JSONObject mainObject = weatherService.returnMain();
        int temp = mainObject.getInt("temp");
        currentTemp.setValue(temp + defaultUnit);

        //Getting Icon from API
        String iconCode=null;
        String weatherDescriptionNew=null;
        JSONArray jsonArray=weatherService.returnsWeatherArray();
        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject weatherObject=jsonArray.getJSONObject(i);
            iconCode=weatherObject.getString("icon");
            weatherDescriptionNew=weatherObject.getString("description");
        }
        IconImage.setSource(new ExternalResource("http://openweathermap.org/img/wn/"+iconCode+"@2x.png"));

        weatherDescription.setValue("Descrption:"+weatherDescriptionNew);

        MinWeather.setValue("Minimum Temperature: "+weatherService.returnMain().getInt("temp_min")+"\u00b0"+uniSelect.getValue());
        MaxWeather.setValue("Maximum Temperature: "+weatherService.returnMain().getInt("temp_max")+"\u00b0"+uniSelect.getValue());
        Pressure.setValue("Pressure: "+weatherService.returnMain().getInt("pressure"));
        Humidity.setValue("Humidity: "+weatherService.returnMain().getInt("humidity"));
        Wind.setValue("Wind: "+weatherService.returnWind().getInt("speed"));
        FeelsLike.setValue("Feels Like: "+weatherService.returnMain().getDouble("feels_like"));

        mainLayout.addComponents(dashboard,mainDescriptionLayout);
    }
}

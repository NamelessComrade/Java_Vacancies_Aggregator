package project;

import project.model.Model;

import java.io.IOException;


public class Controller
{
    private Model model;

    public Controller(Model model)
    {
        if (model == null)
        {
            throw new IllegalArgumentException();
        }
        this.model = model;
    }

    public void onCitySelect(String cityName) throws IOException // Emulates user input.
    {
        model.selectCity(cityName);
    }
}

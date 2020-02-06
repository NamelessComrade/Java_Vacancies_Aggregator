package project.model;


import project.view.View;
import project.vo.Vacancy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Model
{
    private View view;
    private Provider[] providers;

    public Model(View view, Provider ... providers)
    {
        if (view == null || providers == null || providers.length == 0)
        {
            throw new IllegalArgumentException();
        }
        this.view = view;
        this.providers = providers;
    }

    public void selectCity(String city) throws IOException // Gets vacancies from all PROVIDERS and updates output file with them.
    {
        List<Vacancy> vac = new ArrayList<>();
        for (Provider p : providers)
        {
           vac.addAll(p.getJavaVacancies(city));
        }
        view.update(vac);
    }
}

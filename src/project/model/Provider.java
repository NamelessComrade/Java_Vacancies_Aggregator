package project.model;

import project.vo.Vacancy;

import java.io.IOException;
import java.util.List;

public class Provider
{
    private Strategy strategy;

    public Provider(Strategy strategy)
    {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy)
    {
        this.strategy = strategy;
    }

    public List<Vacancy> getJavaVacancies(String searchString) throws IOException // Gets vacancies with help of strategy.
    {
        return strategy.getVacancies(searchString);
    }
}

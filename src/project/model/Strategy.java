package project.model;

import project.vo.Vacancy;

import java.io.IOException;
import java.util.List;

public interface Strategy
{
    List<Vacancy> getVacancies(String searchString) throws IOException; // Parses vacancies.
}

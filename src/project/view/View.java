package project.view;

import project.Controller;
import project.vo.Vacancy;

import java.util.List;

public interface View
{
    void update(List<Vacancy> vacancies); // Updates output file with vacancies.
    void setController(Controller controller); // Sets controller.
}

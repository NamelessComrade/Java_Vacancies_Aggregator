package project.model;

import project.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RosrabotaStrategy implements Strategy // Parsing strategy for RosRabota.
{
    private static final String URL_FORMAT = "http://krs.rosrabota.ru/vac?text=java&page=%d"; // %d page
    private static final String USER_AGENT = "Chrome/55.0.2883.87";
    private static final String REFERRER = "https://google.com/";

    @Override
    public List<Vacancy> getVacancies(String searchString) throws IOException
    {
        List<Vacancy> vacancies = new ArrayList<>();

        for (int i = 1; ; i++)
        {
            Document doc = getDocument(searchString, i);
            Elements plainVacancies = doc.select(".vac");
            if (plainVacancies.size() == 0)
            {
                break;
            }

            Iterator iterator = plainVacancies.iterator();
            while(iterator.hasNext())
            {
                Vacancy vacancy = new Vacancy();
                Element rawVacancy = (Element)iterator.next();

                vacancy.setTitle(rawVacancy.getElementsByTag("span").first().text());
                vacancy.setUrl(rawVacancy.getElementsByTag("a").first().attr("href"));
                vacancy.setSiteName("krs.rosrabota.ru");
                vacancy.setCompanyName(rawVacancy.select(".company").first().text());
                vacancy.setCity(rawVacancy.select(".rpad").get(1).text().substring(2));
                Element salary = rawVacancy.getElementsByTag("span").get(1);
                if (salary != null)
                {
                    vacancy.setSalary(salary.text() + " руб.");
                } else
                {
                    vacancy.setSalary("");
                }
                vacancies.add(vacancy);
            }
            if (plainVacancies.size()%25 != 0)
            {
                break;
            }
        }
        return vacancies;
    }

    private Document getDocument(String searchString, int page) throws IOException
    {
        return Jsoup.connect(String.format(URL_FORMAT, page)).userAgent(USER_AGENT).referrer(REFERRER).get();
    }
}

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

public class MoikrugStrategy implements Strategy // Parsing strategy for MoiKrug.
{
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?page=%d&q=java+%s"; // page(from 1), city
    private static final String USER_AGENT = "Chrome/55.0.2883.87";
    private static final String REFERRER = "google.com/";

    @Override
    public List<Vacancy> getVacancies(String searchString) throws IOException
    {
        List<Vacancy> vacancies = new ArrayList<>();

        for (int i = 1; ; i++)
        {
                Document doc = getDocument(searchString, i);
                Elements plainVacancies = doc.getElementsByClass("job");
                if (plainVacancies.size() == 0)
                {
                    break;
                }

                Iterator iterator = plainVacancies.iterator();
                while (iterator.hasNext())
                {
                    Vacancy vac = new Vacancy();
                    Element rawVac = (Element) iterator.next();

                    vac.setTitle(rawVac.select(".title").first().text());
                    vac.setUrl("https://moikrug.ru" + rawVac.select(".title").first().getElementsByTag("a").first().attr("href"));
                    vac.setSiteName("moikrug.ru");
                    vac.setCompanyName(rawVac.select(".company_name").first().getElementsByTag("a").first().text());
                    Elements cities = rawVac.select(".location");
                    if (cities.size() == 0)
                    {
                        vac.setCity("");
                    } else
                    {
                        vac.setCity(cities.first().text());
                    }
                    Elements salary = rawVac.select(".count");
                    if (salary.size() == 0)
                    {
                        vac.setSalary("");
                    } else
                    {
                        vac.setSalary(salary.first().text());
                    }
                    vacancies.add(vac);
                }
            }
        return vacancies;
    }

    private Document getDocument(String cityName, int pageNum) throws IOException
    {
        return Jsoup.connect(String.format(URL_FORMAT, pageNum, cityName)).userAgent(USER_AGENT).referrer(REFERRER).get();
    }

}

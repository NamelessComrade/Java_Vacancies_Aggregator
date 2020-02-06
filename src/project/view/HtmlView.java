package project.view;

import project.Controller;
import project.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import sun.net.ResourceManager;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

public class HtmlView implements View
{
    private Controller controller;
    private final String filePath = System.getProperty("user.dir") + File.separator + "vacancies.html"; // filepath to output  HTML file.

    public void userCitySelectEmulationMethod() throws IOException // Emulates user choice of "krasnoyarsk".
    {
        controller.onCitySelect("krasnoyarsk");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) // Returns HTML String of parsed VACANCIES which will be used to update output file.
    {
        Document doc = null;
        try
        {
            doc = getDocument();

        Element template = doc.getElementsByClass("template").first().clone();
        template.removeClass("template");
        template.removeAttr("style");
        for (Element elem : doc.getElementsByClass("vacancy"))
        {
            if (!elem.hasClass("template"))
            {
                elem.remove();
            }
        }

        for (Vacancy vac : vacancies)
        {
            Element currentVac = template.clone();

            currentVac.getElementsByClass("city").first().text(vac.getCity());
            currentVac.getElementsByClass("companyName").first().text(vac.getCompanyName());
            currentVac.getElementsByClass("salary").first().text(vac.getSalary());
            currentVac.getElementsByTag("a").first().text(vac.getTitle());
            currentVac.getElementsByTag("a").first().attr("href", vac.getUrl());

            doc.select(".vacancy.template").first().before(currentVac.outerHtml());
        }
        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Some exception occurred.");
        }

        if (doc != null)
        {
            return doc.html();
        } else
        {
            return null;
        }
    }

    private void updateFile(String fileContent) // Updates output HTML file with String of parsed VACANCIES.
    {
        try (FileWriter fw = new FileWriter(filePath))
        {
            if (fileContent != null)
            {
                fw.write(fileContent);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected Document getDocument() throws IOException // Parses output HTML file, which will be updated.
    {
        // If output file does not exist, it will be copied from compiled JAR file to the same folder as JAR.
        // If app running from IDE HTML file should exist in root folder of the project.
        File existTest = new File(filePath);
        if (!existTest.exists())
        {
            extract("/vacancies.html");
        }

       return Jsoup.parse(new File(filePath), Charset.defaultCharset().toString());
    }

    @Override
    public void update(List<Vacancy> vacancies) // Updates HTML output file.
    {
        String fileContent = getUpdatedFileContent(vacancies);
        updateFile(fileContent);
    }

    @Override
    public void setController(Controller controller) // Sets controller.
    {
        this.controller = controller;
    }

    public static String extract(String jarFilePath) // Extracts HTML output file from compiled JAR to the same folder as JAR.
    {
        if(jarFilePath == null)
        {
            return null;
        }

        try
        {
            InputStream fileStream = ResourceManager.class.getResourceAsStream(jarFilePath);
            if(fileStream == null)
            {
                return null;
            }

            String[] chopped = jarFilePath.split("\\/");
            String fileName = chopped[chopped.length-1];
            File tempFile = new File(fileName);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int len = fileStream.read(buffer);
            while (len != -1)
            {
                out.write(buffer, 0, len);
                len = fileStream.read(buffer);
            }
            fileStream.close();
            out.close();

            return tempFile.getAbsolutePath();
        }
        catch (IOException e)
        {
            return null;
        }
    }
}

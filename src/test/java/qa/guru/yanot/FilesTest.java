package qa.guru.yanot;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.selector.ByText;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тесты на файлы")
public class FilesTest {

    @Test
    @DisplayName("Загрузка файла из resources")
    void uploadFileResources() {
        open("https://dropmefiles.com/");
        $("input[type='file']").uploadFromClasspath("exampleYanot.txt");
        $(".percent").shouldHave(Condition.text("загружено"));
    }

    @Test
    @DisplayName("Скачивание файла")
    void downloadFile() throws IOException {
        open("https://filesamples.com/formats/txt");
        File download = $("a[href*='samples/document/txt/sample1.txt'").download();
        String fileContent = IOUtils.toString(new FileReader(download));
        assertTrue(fileContent.contains("Utilitatis causa amicitia est quaesita."));
    }

    @Test
    @DisplayName("Скачивание PDF файла")
    void downloadFilePDF() throws IOException {
        open("https://filesamples.com/formats/pdf");
        File download = $("a[href*='samples/document/pdf/sample1.pdf'").download();
        PDF parsedPdf = new PDF(download);
        assertEquals(1, parsedPdf.numberOfPages);
    }

    @Test
    @DisplayName("Скачивание XLS файла")
    void downloadFileXLS() throws IOException {
        open("https://filesamples.com/formats/xls");
        File download = $("a[href*='samples/document/xls/sample1.xls'").download();
        XLS parsedXls = new XLS(download);
        boolean checkPassed = parsedXls.excel
                .getSheetAt(2)
                .getRow(3)
                .getCell(0)
                .getStringCellValue()
                .contains("http://www.cmu.edu/blackboard");

        assertTrue(checkPassed);
    }

    @Test
    @DisplayName("Парсинг CSV файлов")
    void downloadFileCSV() throws IOException, CsvException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("csvYanot.csv");
             Reader reader = new InputStreamReader(is)) {
            CSVReader csvReader = new CSVReader(reader);

            List<String[]> strings = csvReader.readAll();
            assertEquals(3, strings.size());
        }
    }

    @Test
    @DisplayName("Парсинг ZIP файлов")
    void downloadFileZIP() throws IOException{
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("sample1.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            Set<String> expectedFileNames = new HashSet<>();
            expectedFileNames.add("sample1.txt");
            Set<String> realFileNames = new HashSet<>();
            while ((entry = zis.getNextEntry()) != null) {
                realFileNames.add(entry.getName());
            }
            assertEquals(expectedFileNames, realFileNames);
            }
        }
}

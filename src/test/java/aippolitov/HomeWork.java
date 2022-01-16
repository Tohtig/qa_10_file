package aippolitov;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.codeborne.pdftest.PDF.containsText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

public class HomeWork {
    private ClassLoader cl = aippolitov.HomeWork.class.getClassLoader();

    @Test
    void zipTest() throws Exception {

        try (ZipFile zipFile = new ZipFile("src\\test\\resources\\resources.zip")) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String extension = FilenameUtils.getExtension(entry.getName());
                InputStream inputStream;
                switch (extension) {
                    case ("xlsx"):
                        inputStream = zipFile.getInputStream(entry);
                        XLS xlsParsed = new XLS(inputStream);
                        System.out.println();
                        assertThat(xlsParsed.excel.getSheetAt(0).getRow(1).getCell(0).getStringCellValue())
                                .isEqualTo("проверка пользователя");
                        break;
                    case ("pdf"):
                        inputStream = zipFile.getInputStream(entry);
                        PDF pdfParsed = new PDF(inputStream);
                        assertThat(pdfParsed, containsText("Успешно"));
                        break;
                    case ("csv"):
                        inputStream = zipFile.getInputStream(entry);
                        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
                        List<String[]> list = reader.readAll();
                        assertThat(list)
                                .hasSize(3)
                                .contains(
                                        new String[] {"Author", "Book"},
                                        new String[] {"Block", "Apteka"},
                                        new String[] {"Esenin", "Cherniy Chelovek"}
                                );
                        break;
                }
            }

        }
    }
}
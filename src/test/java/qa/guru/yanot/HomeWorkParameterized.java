package qa.guru.yanot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class HomeWorkParameterized {

    @BeforeEach
    void beforeEach() {
        open("https://ru.wikipedia.org/");}

    static Stream<Arguments> commonSearchDataProvider(){
        return Stream.of(
                Arguments.of("Металлика", "Metallica была основана в Лос-Анджелесе 28 октября 1981 года"),
                Arguments.of("Джеймс Хетфилд", "вокалист и ритм-гитарист метал-группы Metallica")
        );
    }

    @Disabled
    @ValueSource(strings = {"Металлика", "Джеймс Хетфилд"})
    @ParameterizedTest(name = "Тестирование с тестданными: {0}")
    void searchValueSource(String testData){
    $("[id='mw-head']").$("[id='p-search']").click();
    $("[id='searchInput']").setValue(testData).pressEnter();
    $("[id='content']").shouldHave(text(testData));
    }

    @Disabled
    @CsvSource(value = {"Металлика, Metallica была основана в Лос-Анджелесе 28 октября 1981 года",
            "Джеймс Хетфилд, вокалист и ритм-гитарист метал-группы Metallica"})
    @ParameterizedTest(name = "Тестирование с тестданными: {0}")
    void searchCsvSource(String testData, String expectedResult){
        $("[id='mw-head']").$("[id='p-search']").click();
        $("[id='searchInput']").setValue(testData).pressEnter();
        $("[id='content']").shouldHave(text(expectedResult));
    }

    @MethodSource("commonSearchDataProvider")
    @ParameterizedTest(name = "Тестирование с тестданными: {0}")
    void searchWithMethodSourceTest(String testData, String expectedResult){
        $("[id='mw-head']").$("[id='p-search']").click();
        $("[id='searchInput']").setValue(testData).pressEnter();
        $("[id='content']").shouldHave(text(expectedResult));
    }
}
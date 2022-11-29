package ru.citilink;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CitilinkTest extends TestBase {

    static Stream<Arguments> checkAvailabilityFilters() {
        return Stream.of(
                Arguments.of("Мониторы", List.of("Наличие товара", "Статус товара",
                        "Товары со скидкой", "Оценка товара по отзывам", "Бренд", "Диагональ", "Разрешение экрана",
                        "Тип матрицы",
                        "Частота обновления", "Игровой", "Изогнутый", "Поверхность экрана", "Радиус изгиба", "Разъемы",
                        "Время отклика (GTG)", "Поддержка синхронизации", "Особенности", "Эргономика", "Крепление к стене",
                        "Цвет рамки", "Соотношение сторон", "Яркость экрана", "Тип блока питания", "Гарантия", "Серия")),
                Arguments.of("Смартфоны", List.of("Наличие товара", "Статус товара", "Товары со скидкой",
                        "Оценка товара по отзывам", "Бренд", "Серия", "Оперативная память", "Встроенная память", "Поддержка карт памяти",
                        "Диагональ экрана", "Тип экрана", "Особенности экрана", "Количество SIM-карт", "Поддержка eSIM",
                        "Количество основных камер", "Цвет", "Особенности", "Бесконтактные платежи (NFC)", "Форм-фактор",
                        "Расположение сканера отпечатка пальцев", "Процессор", "Наименование процессора", "Частота процессора",
                        "Разрешение экрана", "Частота обновления экрана", "Частота обновления сенсора", "Основная камера",
                        "Тип фронтальной камеры", "Интернет", "Навигация", "Операционная система", "Гарантия",
                        "Емкость батареи", "Интерфейс USB", "Разъем для наушников")),
                Arguments.of("LED панели", List.of("Наличие товара", "Статус товара", "Бренд", "Диагональ экрана",
                        "Тип матрицы", "HDTV", "Разъемы", "Особенности", "Гарантия")),
                Arguments.of("Сетевые хранилища NAS", List.of("Наличие товара", "Статус товара", "Товары со скидкой", "Оценка товара по отзывам",
                        "Бренд", "Количество отсеков для HDD", "Общий объем", "Интерфейс HDD", "Поддержка RAID", "Особенности", "Разъемы и интерфейсы",
                        "Процессор", "Форм-фактор", "Монтаж в стойку", "Форм-фактор HDD", "Оперативная память", "Гарантия", "Скорость передачи данных"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Проверка наличия в категории \"{0}\"фильтров: {1}")
    @Tag("Height")
    void checkAvailabilityFilters(String category, List<String> filters) {
        open("/catalog");
        $(".CatalogLayout__content").$(byText(category)).click();
        $$("[data-meta-name='FilterDropdown__title']").shouldHave(CollectionCondition.texts(filters));
    }

    @ValueSource(strings = {"APPLE", "SAMSUNG", "XIAOMI", "VIVO"})
    @ParameterizedTest(name = "Проверка фильтра по бренду {0}")
    @Tag("Height")
    void verifyFilterByBrand(String brand) {
        open("/catalog/smartfony");
        $x("//span[text()='Оценка товара по отзывам']").scrollTo();
        $("div[data-meta-value='" + brand + "'] label").click();
        $$(".product_data__gtm-js.product_data__pageevents-js.ProductCardHorizontal.js--ProductCardInListing.js--ProductCardInWishlist")
                .shouldHave(CollectionCondition.allMatch("Проверяем наличие названия бренда в заголовке в каждом элементе коллекции",
                        (element -> element.getText().contains(brand.toLowerCase().substring(1)))));
    }

    @CsvSource(value = {"10000,20000", "10001,20001", "9999,19999 "})
    @ParameterizedTest(name = "Проверка фильтра диапазона цен от {0} до {1}")
    @Tag("Height")
    void verifyFilterByPrice(String minPrice, String maxPrice) {
        open("/catalog/televizory/");
        $(".e4uhfkv0.css-1twabx7.e4mggex0").scrollTo();
        $$("[name='input-min']").get(1).setValue(minPrice);
        $$("[name='input-max']").get(1).setValue(maxPrice);
        $$("[name='input-min']").get(1).click();
        refresh();
        $$(".ProductCardHorizontal__price_current-price.js--ProductCardHorizontal__price_current-price").shouldHave(CollectionCondition.allMatch("",
                el -> Integer.parseInt(el.getText().replaceAll(" ", "")) >= Integer.parseInt(minPrice) &&
                        Integer.parseInt(el.getText().replaceAll(" ", "")) <= Integer.parseInt(maxPrice)));
    }
}

package net.serebryansky.max.com.unikoom.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@ActiveProfiles("test")
class TestApplicationTests {

    @Autowired
    private MockMvc mvc;

    /**
     * <p>Приложение должно хранить следующую информацию о пользователе:
     * <ul>
     *     <li>ID</li>
     *     <li>username</li>
     *     <li>ФИО</li>
     *     <li>email</li>
     *     <li>Дата рождения</li>
     *     <li>Пол</li>
     * </ul>
     * </p>
     *
     * <p>Бэкенд должен предоставлять REST API для выполнения следующих операций:
     * <ul>
     *     <li>Добавление нового пользователя</li>
     *     <li>Получение списка всех пользователей (только ID, username и ФИО)</li>
     *     <li>Получение всех данных о пользователе по ID</li>
     * </ul>
     * </p>
     */
    @Test
    void testUserApi() throws Exception {
        MvcResult result = mvc.perform(post("/users").content("" +
                "{\n" +
                "  \"username\": \"test\",\n" +
                "  \"name\": \"test FIO\",\n" +
                "  \"email\": \"test@test.com\",\n" +
                "  \"birthDate\": \"2000-01-01\",\n" +
                "  \"sex\": \"MALE\",\n" +
                "  \"photo\": {\n" +
                "    \"type\": \"image/png\",\n" +
                "    \"name\": \"scale_1200.png\",\n" +
                "    \"bytes\": \"iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAMAAAAp4XiDAAAAV1BMVEUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOl5NtAAAAHHRSTlMABgcIFjY3Ojs9hoeIiYrCw8XHyszS09bb5eb84A1lyQAAAKVJREFUSMftlsEOwiAMhovgnMq2bk4m8L//c3pZCB40ZSZLlvClBy5f0lIoEFX24dQHCAidSkoPITYpARdJMi18WgM08hq/APIlD2vIFREfipT/lA2JHVPRE5cqFs9CpYnx/FXhrANMRMSzIb2kAyxRHnDGYtEF5RuHV4xN0Y4Zl98rWflmnnTtfu3+QcZFQCsxrtljYaXz9ZYU1XmJ4O+qfhJ24g38OTM/uGjw0AAAAABJRU5ErkJggg==\"\n" +
                "  }\n" +
                "}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        //noinspection ConstantConditions
        String rawId = location.substring(location.lastIndexOf("/") + 1);

        mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value("test"))
                .andExpect(jsonPath("$.name").value("test FIO"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.birthDate").value("2000-01-01"))
                .andExpect(jsonPath("$.sex").value("MALE"))
                .andExpect(jsonPath("$.photo.type").value("image/png"))
                .andExpect(jsonPath("$.photo.name").value("scale_1200.png"))
                .andExpect(jsonPath("$.photo.type").isNotEmpty());

        mvc.perform(get("/users?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[?(@.id == %s)].username", rawId).value("test"))
                .andExpect(jsonPath("$.content[?(@.id == %s)].name", rawId).value("test FIO"))
                .andExpect(jsonPath("$.content[?(@.id == %s)].email", rawId).doesNotExist())
                .andExpect(jsonPath("$.content[?(@.id == %s)].birthDate", rawId).doesNotExist())
                .andExpect(jsonPath("$.content[?(@.id == %s)].sex", rawId).doesNotExist())
                .andExpect(jsonPath("$.content[?(@.id == %s)].photo", rawId).doesNotExist());
    }

    /**
     * Запрещены все методы кроме:
     * <ul>
     *     <li>Collection HEAD</li>
     *     <li>Collection GET</li>
     *     <li>Collection POST</li>
     *     <li>Item HEAD</li>
     *     <li>Item GET</li>
     * </ul>
     */
    @Test
    void forbidden() throws Exception {
        MvcResult result = mvc
                .perform(
                        post("/users")
                                .content("" +
                                        "{\n" +
                                        "  \"username\": \"test\",\n" +
                                        "  \"name\": \"test FIO\",\n" +
                                        "  \"email\": \"test@test.com\",\n" +
                                        "  \"birthDate\": \"2000-01-01\",\n" +
                                        "  \"sex\": \"MALE\"\n" +
                                        "}")
                )
                .andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        //noinspection ConstantConditions
        String rawId = location.substring(location.lastIndexOf("/") + 1);

        // не предоставленные методы
        mvc
                .perform(put("/users").content("{\"username\": \"test collection put\", \"name\": \"test FIO\", \"email\": \"test@test.com\", \"birthDate\": \"2000-01-01\", \"sex\": \"MALE\"}"))
                .andExpect(status().is(NOT_FOUND.value()));
        mvc
                .perform(patch("/users").content("{\"username\": \"test collection patch\", \"name\": \"test FIO\", \"email\": \"test@test.com\", \"birthDate\": \"2000-01-01\", \"sex\": \"MALE\"}"))
                .andExpect(status().is(NOT_FOUND.value()));
        mvc
                .perform(delete("/users"))
                .andExpect(status().is(NOT_FOUND.value()));
        mvc
                .perform(post("/users/" + rawId).content("{\"username\": \"test item post\", \"name\": \"test FIO\", \"email\": \"test@test.com\", \"birthDate\": \"2000-01-01\", \"sex\": \"MALE\"}"))
                .andExpect(status().is(NOT_FOUND.value()));

        // отключенные методы
        mvc
                .perform(put("/users/" + rawId).content("{\"username\": \"test item put\", \"name\": \"test FIO\", \"email\": \"test@test.com\", \"birthDate\": \"2000-01-01\", \"sex\": \"MALE\"}"))
                .andExpect(status().is(METHOD_NOT_ALLOWED.value()));
        mvc
                .perform(patch("/users/" + rawId).content("{\"username\": \"test item patch\", \"name\": \"test FIO\", \"email\": \"test@test.com\", \"birthDate\": \"2000-01-01\", \"sex\": \"MALE\"}"))
                .andExpect(status().is(METHOD_NOT_ALLOWED.value()));
        mvc
                .perform(delete("/users/" + rawId))
                .andExpect(status().is(METHOD_NOT_ALLOWED.value()));

    }
}

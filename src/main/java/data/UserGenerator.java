package data;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    public static UserData getRandomUser() {
        String email = RandomStringUtils.randomAlphabetic(8)+ "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);

        return new UserData(email, password, name);
    }

}


package ModelRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RegistrationRequest {
    private String group_name;
    private String birthday;

    public RegistrationRequest(String group_name, String birthday) {
        this.group_name = group_name;
        this.birthday = birthday;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    // Валидация формата даты
    public boolean isValidBirthday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Исправленный формат даты
        dateFormat.setLenient(false);
        try {
            Date parsedDate = dateFormat.parse(birthday);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Валидация формата имени группы
    public boolean isValidGroupName() {
        // Пример валидации: проверка на соответствие шаблону "ИСП-421п"
        return group_name.matches("^[А-ЯЁа-яёA-Za-z0-9\\-]+$");
    }
}

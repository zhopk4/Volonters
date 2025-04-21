package ModelRequest;

public class RegistrationResponse {
    private String name;
    private String surname;
    private String patronymic;
    private String group_name;
    private String birthday;
    private String login;
    private String email;
    private String status;

    // Конструктор
    public RegistrationResponse(String name, String surname, String patronymic, String group_name, String birthday, String login, String email, String status) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.group_name = group_name;
        this.birthday = birthday;
        this.login = login;
        this.email = email;
        this.status = status;
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

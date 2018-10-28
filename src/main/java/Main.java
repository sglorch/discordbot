import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "MzgzOTg3ODUwODcxNDM5MzYy.Dre9jg._WKs7uv9uOlZtYbR9Rz2W7uZdtc";
        builder.setToken(token);
        builder.buildAsync();
    }

}

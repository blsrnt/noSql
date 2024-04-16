import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

import java.util.Date;

import static java.lang.System.out;

public class RedisStorage
{
    private RedissonClient redisson;
    private RKeys rKeys;
    private RScoredSortedSet<String> onlineUsers;

    private final static String KEY = "USERS";

    private double getRegistrationTime() {
        return new Date().getTime();
    }

    void init() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        try {
            redisson = Redisson.create(config);
        } catch (RedisConnectionException Exc) {
            out.println("Не удалось подключиться к Redis");
            out.println(Exc.getMessage());
        }
        rKeys = redisson.getKeys();
        onlineUsers = redisson.getScoredSortedSet(KEY);
        rKeys.delete(KEY);
    }

    void logUser(int user_id)
    {
        onlineUsers.add(getRegistrationTime(), String.valueOf(user_id));
    }
    int getFirstUser(){
        out.println("На главной странице показываем пользователя " + onlineUsers.first());
        return Integer.parseInt(onlineUsers.first());
    }


}

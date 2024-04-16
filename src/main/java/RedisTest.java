import java.util.Random;


public class RedisTest {



    private static final int USERS = 20;
    private static final int SLEEP = 1000;
    private static final RedisStorage redis = new RedisStorage();

    private static void buyer(int userId) {
        String log = String.format("Пользователь %d оплатил платную услугу", userId);
        System.out.println(log);
        System.out.println("На главной странице показываем пользователя " + userId);
        redis.logUser(userId);
    }


    public static void main(String[] args) throws InterruptedException {


        redis.init();
        for (int i = 1; i <= USERS; i++) {
            redis.logUser(i);
        }

        int counter = 1;
        boolean flag = true;
        while (true) {
            if(counter < 10 && flag){
                int rnd = new Random().nextInt(2);
                if(rnd == 1) {
                    int userId = new Random().nextInt(USERS) + 1;
                    buyer(userId);
                    flag = false;
                    counter++;
                    Thread.sleep(SLEEP);
                    continue;
                }
            }
            if(counter == 10){
                counter = 0;
                flag = true;
            }
            redis.logUser(redis.getFirstUser());
            counter++;
        }
    }
}

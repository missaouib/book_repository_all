package am.egs.bookRepository.util;

import java.time.LocalDateTime;

public class UserLocalDateTime {

    private static LocalDateTime currentDateTime;
    private static LocalDateTime expireDate;
    private static int countYears = 1;

    public static LocalDateTime userExpiredDateTime() {

        currentDateTime = LocalDateTime.now();
        expireDate = currentDateTime.plusYears(countYears);
        return expireDate;
    }
}



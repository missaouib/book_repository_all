package am.egs.socialSyte.util;

import java.time.LocalDateTime;

public class UserLocalDateTime {

    LocalDateTime currentDateTime = LocalDateTime.now();

    public void userCurrentDateTime(long countYears) {
        boolean expireStatus = true;

        LocalDateTime expireDate = currentDateTime.plusYears(countYears);
        while (expireDate.isBefore(currentDateTime)) {
             expireStatus = false;
        }
    }
}

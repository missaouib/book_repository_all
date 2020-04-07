package am.egs.socialSyte.util;

import am.egs.socialSyte.model.User;

import java.time.LocalDateTime;

public class UserLocalDateTime {

    private static LocalDateTime currentDateTime;
    private static LocalDateTime expireDate;
    private static int countYears = 2;
    private static User user;

    public static LocalDateTime userExpiredDateTime() {

        currentDateTime = LocalDateTime.now();
        expireDate = currentDateTime.plusYears(countYears);
        return expireDate;

//        user.setExpireDate(expireDate);
//        boolean expireStatus = true;
//        while (expireDate.isBefore(currentDateTime)) {
//            return expireStatus = false;
//            return expireStatus = true;
//            user.setExpireDate(expireDate);
    }

    public static boolean isUserNonLocked() {
        LocalDateTime lockedTime, unLockedTime;
        int afterSecondsUserUnLocked = 10;
        lockedTime = user.getLokedTime();
        unLockedTime = lockedTime.plusSeconds(afterSecondsUserUnLocked);
        if (currentDateTime.isBefore(unLockedTime)) {
            return false;
        }
        return true;
    }

    public static LocalDateTime userUnLockedTime() {
        LocalDateTime lockedTime, unLockedTime;
        int afterSecondsUserUnLocked = 10;
        lockedTime = user.getLokedTime();
        unLockedTime = lockedTime.plusSeconds(afterSecondsUserUnLocked);
        return unLockedTime;
    }


}



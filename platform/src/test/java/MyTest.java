import com.platform.dao.FansInfoMapper;
import com.platform.dto.fansInfo.FansInfoPo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class MyTest {
@Autowired
FansInfoMapper fansInfoMapper;
    @Test
    public void TestRegex() {
        String regex = "";
        boolean flag = Pattern.matches("^.{8,20}$", "1231111s");


        System.out.println(flag);
        String phone = "19906723165";
    }


    public String test() {
        for (int k = 0; k < 100000; k++) {

            FansInfoPo fansInfoPo = new FansInfoPo();
            String str1 = "ADD_SCENE_SEARCH";
            String str2 = "ADD_SCENE_ACCOUNT_MIGRATION";
            String str3 = "ADD_SCENE_PROFILE_CARD";
            String str4 = "ADD_SCENE_QR_CODE";
            String str5 = "ADD_SCENE_PROFILE_LINK";
            String str6 = "ADD_SCENE_PROFILE_ITEM";
            String str7 = "ADD_SCENE_PAID";
            String str8 = "ADD_SCENE_WECHAT_ADVERTISEMENT";
            String str9 = "ADD_SCENE_OTHERS";

            Random random = new Random();
            int i = random.nextInt(9);
            if (i == 0) {
                fansInfoPo.setSubscribe(0);
            } else {
                fansInfoPo.setSubscribe(1);
            }
            fansInfoPo.setOpenid(UUID.randomUUID().toString());

            fansInfoPo.setSex(String.valueOf(random.nextInt(3)));
            fansInfoPo.setBindStatus(random.nextInt(2));
            int a = random.nextInt(9);
            if (a == 1) {
                fansInfoPo.setSubscribeScene(str9);
            } else if (a == 2) {
                fansInfoPo.setSubscribeScene(str2);
            } else if (a == 3) {
                fansInfoPo.setSubscribeScene(str3);
            } else if (a == 4) {
                fansInfoPo.setSubscribeScene(str4);
            } else if (a == 5) {
                fansInfoPo.setSubscribeScene(str5);
            }else if(a==6){
                fansInfoPo.setSubscribeScene(str6);
            }else if(a==7){
                fansInfoPo.setSubscribeScene(str7);
            }else if(a==8){
                fansInfoPo.setSubscribeScene(str8);
            }else if(a==0){
                fansInfoPo.setSubscribeScene(str1);
            }




            Date date = new Date();
            long time = date.getTime();

            String j = String.valueOf(random.nextInt(61104));
            String str = j + "000000";
            BigInteger bigInteger = new BigInteger(str);
            BigInteger oneYear = new BigInteger(String.valueOf(time));
            BigInteger res = oneYear.subtract(bigInteger);


            fansInfoPo.setSubscribeTime(res.toString());
            fansInfoMapper.insertFansInfo(fansInfoPo);
        }

        return "yiwancheng";
    }


}

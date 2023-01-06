package com.bzh.data.meizi;

import com.bzh.data.repository.RetrofitManager;

import org.junit.Before;
import org.junit.Test;

import okhttp3.ResponseBody;
import rx.Subscriber;

import static org.junit.Assert.*;

/**
 * ==========================================================<br>
 * <b>版权</b>：　　　别志华 版权所有(c)2016<br>
 * <b>作者</b>：　　  biezhihua@163.com<br>
 * <b>创建日期</b>：　16-3-29<br>
 * <b>描述</b>：　　　<br>
 * <b>版本</b>：　    V1.0<br>
 * <b>修订历史</b>：　<br>
 * ==========================================================<br>
 */
public class IMeiZiServiceTest {

    private IMeiZiService iMeiZiService;

    @Before
    public void setUp() throws Exception {
        iMeiZiService = RetrofitManager.getInstance().getiMeiZiService();

    }

    @Test
    public void testGetMeiZi() throws Exception {
        iMeiZiService.getMeiZi(0).subscribe(new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("IMeiZiServiceTest.onError");

                assertNull(e);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                System.out.println("IMeiZiServiceTest.onNext");
                System.out.println("responseBody = [" + responseBody + "]");
                assertNotNull(responseBody);
            }
        });
    }
}
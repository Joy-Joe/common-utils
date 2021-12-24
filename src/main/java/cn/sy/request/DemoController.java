package cn.sy.request;

import cn.sy.check.ValidateUtil;
import cn.sy.constants.SyApiRespMsg;
import cn.sy.dto.ApiResponse;
import cn.sy.exception.SyException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author joy joe
 * @date 2021/12/24 下午9:46
 * @DESC TODO
 */
@RestController
@Slf4j
public class DemoController {
    @PostMapping("/demo")
    public ApiResponse queryUserClockFinishRecord(@RequestBody JSONObject inParamJson) {
        log.info(this.getClass().getSimpleName() + " edmo_inParam:{}", JSON.toJSONString(inParamJson));
        try {
            ValidateUtil.assertObjectNull(inParamJson, SyApiRespMsg.EP_000001_CODE, SyApiRespMsg.EP_000001_MSG);
            String a = inParamJson.getString("a");
            ValidateUtil.assertEmptyNull(a, SyApiRespMsg.EP_000002_CODE, SyApiRespMsg.EP_000002_MSG);
            Object o =new Object();
            return ApiResponse.builder().code(SyApiRespMsg.EP_000000_CODE).msg(SyApiRespMsg.EP_000000_MSG).data(o).build();
        } catch (SyException e) {
            log.error(this.getClass().getSimpleName() + " demo_syError,inParam:{}", JSON.toJSONString(inParamJson), e);
            return ApiResponse.builder().code(e.getErrorCode()).msg(e.getMessage()).build();
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + " demo_error,inParam:{}", JSON.toJSONString(inParamJson), e);
            return ApiResponse.builder().code(SyApiRespMsg.EP_999999_CODE).msg(SyApiRespMsg.EP_999999_MSG).build();
        }
    }
}

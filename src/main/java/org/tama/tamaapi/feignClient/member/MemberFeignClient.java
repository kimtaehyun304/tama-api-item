package org.tama.tamaapi.feignClient.member;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//k8s로 바꾸면서 url 옵션 생략
@FeignClient(name = "member-service"
        , configuration = MemberFeignClientConfig.class
        , fallbackFactory = MemberFallbackFactory.class)
public interface MemberFeignClient {

    @GetMapping("/api/member/{memberId}/authority")
    Authority findAuthority(@PathVariable Long memberId);

}
